package com.lothrazar.powerinventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.inventory.button.GuiButtonOpenInventory;
import com.lothrazar.powerinventory.net.EnderChestPacket;
import com.lothrazar.powerinventory.net.EnderPearlPacket;
import com.lothrazar.powerinventory.net.HotbarSwapPacket;
import com.lothrazar.powerinventory.net.OpenInventoryPacket;
import com.lothrazar.powerinventory.proxy.ClientProxy;
import com.lothrazar.powerinventory.util.UtilPlayerInventoryFilestorage;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    if (ClientProxy.keyEnderpearl.isPressed()) {
      ModInv.instance.network.sendToServer(new EnderPearlPacket());
    }
    if (ClientProxy.keyEnderchest.isPressed()) {
      ModInv.instance.network.sendToServer(new EnderChestPacket());
    }
    if (ClientProxy.keyHotbar.isPressed()) {
      ModInv.instance.network.sendToServer(new HotbarSwapPacket());
    }
    if (ClientProxy.keyInventory.isPressed()) {
      ModInv.instance.network.sendToServer(new OpenInventoryPacket());
    }
  }
  @SubscribeEvent
  public void onConfigChanged(OnConfigChangedEvent event) {
    if (event.getModID().equals(Const.MODID))
      ModConfig.syncConfig();
  }
  @SubscribeEvent
  public void onPlayerClone(PlayerEvent.Clone event) {
    if (event.isWasDeath() == false) {
      //then it was an end portal transition
      IPlayerExtendedProperties src = CapabilityRegistry.getPlayerProperties(event.getOriginal());
      IPlayerExtendedProperties dest = CapabilityRegistry.getPlayerProperties(event.getEntityPlayer());
      dest.setDataFromNBT(src.getDataAsNBT());
    }
    else {
      //it was a real death. so only copy if config says to save it (defaults true)
      if (ModConfig.persistUnlocksOnDeath == true) {
        IPlayerExtendedProperties src = CapabilityRegistry.getPlayerProperties(event.getOriginal());
        IPlayerExtendedProperties dest = CapabilityRegistry.getPlayerProperties(event.getEntityPlayer());
        dest.setDataFromNBT(src.getDataAsNBT());
      }
    }
  }
  @SubscribeEvent
  public void onEntityDeath(LivingDeathEvent event) {
    //if config says they persist... do not drop on ground // not http://www.minecraftforge.net/wiki/Event_Reference#LivingDropsEvent
    Entity entityLiving = event.getEntity();
    if (ModConfig.persistUnlocksOnDeath == false &&
        entityLiving instanceof EntityPlayer && !entityLiving.world.isRemote) {
      EntityPlayer p = (EntityPlayer) entityLiving;
      // the vanilla inventory stuff (first hotbar) already drops  
      for (int i = Const.HOTBAR_SIZE; i < UtilPlayerInventoryFilestorage.getPlayerInventory(p).getSizeInventory(); ++i) {
        UtilPlayerInventoryFilestorage.getPlayerInventory(p).dropStackInSlot(p, i);
      }
      UtilPlayerInventoryFilestorage.getPlayerInventory(p).dropStackInSlot(p, Const.SLOT_ECHEST);
      UtilPlayerInventoryFilestorage.getPlayerInventory(p).dropStackInSlot(p, Const.SLOT_EPEARL);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onGuiPostInit(InitGuiEvent.Post event) {
    Gui gui = event.getGui();
    if (gui == null) { return; } // probably doesnt ever happen
    if (ModConfig.showGuiButton && gui instanceof net.minecraft.client.gui.inventory.GuiInventory) {
      // omg thanks so much to this guy
      // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/mods-discussion/1390983-making-guis-scale-to-screen-width-height
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      int screenWidth = res.getScaledWidth();
      int screenHeight = res.getScaledHeight();
      int button_id = 256;
      int x, y;
      //align t top right
      x = screenWidth / 2 + Const.VWIDTH / 2 - GuiButtonOpenInventory.width - 1;
      y = screenHeight / 2 - Const.VHEIGHT / 2 - GuiButtonOpenInventory.height + 1;
      boolean hasPotions = Minecraft.getMinecraft().player.getActivePotionEffects().size() > 0;
      if (hasPotions) {
        x += 60;
      }
      //this is the tab button
      event.getButtonList().add(new GuiButtonOpenInventory(button_id++, x, y));
    }
  }
  // https://github.com/PrinceOfAmber/Cyclic/blob/61016a1714551389ff2d9344b3cf6053a425dc70/src/main/java/com/lothrazar/cyclicmagic/event/core/EventPlayerData.java
  @SubscribeEvent
  public void onSpawn(PlayerLoggedInEvent event) {
    if (event.player instanceof EntityPlayerMP && event.player.world.isRemote == false) {
      EntityPlayerMP p = (EntityPlayerMP) event.player;
      if (p != null) {
        CapabilityRegistry.syncServerDataToClient(p);
      }
    }
  }
  @SubscribeEvent
  public void onSpawn(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof EntityPlayerMP && event.getEntity().world.isRemote == false) {
      EntityPlayerMP p = (EntityPlayerMP) event.getEntity();
      if (p != null) {
        CapabilityRegistry.syncServerDataToClient(p);
      }
    }
  }
  @SubscribeEvent
  public void onEntityConstruct(AttachCapabilitiesEvent.Entity evt) {
    if (evt.getEntity() instanceof EntityPlayer == false) { return;//mod compatibility: IE Tinkers construct
    }
    evt.addCapability(new ResourceLocation(Const.MODID, "OPI"), new ICapabilitySerializable<NBTTagCompound>() {
      IPlayerExtendedProperties inst = ModInv.CAPABILITYSTORAGE.getDefaultInstance();
      @Override
      public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ModInv.CAPABILITYSTORAGE;
      }
      @Override
      public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == ModInv.CAPABILITYSTORAGE ? ModInv.CAPABILITYSTORAGE.<T> cast(inst) : null;
      }
      @Override
      public NBTTagCompound serializeNBT() {
        try {
          return (NBTTagCompound) ModInv.CAPABILITYSTORAGE.getStorage().writeNBT(ModInv.CAPABILITYSTORAGE, inst, null);
        }
        catch (java.lang.ClassCastException e) {
          return new NBTTagCompound();
        }
      }
      @Override
      public void deserializeNBT(NBTTagCompound nbt) {
        ModInv.CAPABILITYSTORAGE.getStorage().readNBT(ModInv.CAPABILITYSTORAGE, inst, null, nbt);
      }
    });
  }
}
