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
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
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

//	@SubscribeEvent
//	public void onEntityConstruct(EntityConstructing event) {
//    Entity entity = event.getEntity();
//		if (entity instanceof EntityPlayer) {
//			EntityPlayer player = (EntityPlayer) entity;
//
//			if (PlayerPersistProperty.get(player) == null) {
//				PlayerPersistProperty.register(player);
//			}
//		}
//	}

//	@SubscribeEvent
//	public void onPlayerClone(PlayerEvent.Clone event) {
//		if (event.isWasDeath() == false || // changing dimensions -> so always do it
//										// or it was a death => maybe do it
//		(ModConfig.persistUnlocksOnDeath && event.isWasDeath())) {
//			PlayerPersistProperty.clonePlayerData(event.getOriginal(), event.getEntityPlayer());
//			
//		}
//	}

//	@SubscribeEvent
//	public void onEntityDeath(LivingDeathEvent event) {
//		//if config says they persist... do not drop on ground
//	  Entity entityLiving = event.getEntity();
//		
//		if (ModConfig.persistUnlocksOnDeath == false && 
//				entityLiving instanceof EntityPlayer && !entityLiving.worldObj.isRemote) {
//			EntityPlayer p = (EntityPlayer) entityLiving;
//
//			PlayerPersistProperty prop = PlayerPersistProperty.get(p);
//			// the vanilla inventory stuff (first hotbar) already drops  
//			
//			for (int i = Const.HOTBAR_SIZE; i < prop.inventory.getSizeInventory(); ++i) {
//				prop.inventory.dropStackInSlot(p, i);
//			}
//
//			prop.inventory.dropStackInSlot(p, Const.SLOT_ECHEST);
//			prop.inventory.dropStackInSlot(p, Const.SLOT_EPEARL);
//		}
//	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event) {
	  Gui gui = event.getGui();
		if (gui == null) {
			return;
		}// probably doesnt ever happen

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

			boolean hasPotions = Minecraft.getMinecraft().thePlayer.getActivePotionEffects().size() > 0;
			if(hasPotions){
				x += 60;
			}
			//this is the tab button
			event.getButtonList().add(new GuiButtonOpenInventory(button_id++, x, y));
		}
	}

	
	// https://github.com/PrinceOfAmber/Cyclic/blob/61016a1714551389ff2d9344b3cf6053a425dc70/src/main/java/com/lothrazar/cyclicmagic/event/core/EventPlayerData.java
	 @SubscribeEvent
	  public void onSpawn(PlayerLoggedInEvent event) {
	    if (event.player instanceof EntityPlayerMP && event.player.worldObj.isRemote == false) {
	      EntityPlayerMP p = (EntityPlayerMP) event.player;
	      if (p != null) {
	        CapabilityRegistry.syncServerDataToClient(p);
	      }
	    }
	  }
	  @SubscribeEvent
	  public void onSpawn(EntityJoinWorldEvent event) {
	    if (event.getEntity() instanceof EntityPlayerMP && event.getEntity().worldObj.isRemote == false) {
	      EntityPlayerMP p = (EntityPlayerMP) event.getEntity();
	      if (p != null) {
	        CapabilityRegistry.syncServerDataToClient(p);
	      }
	    }
	  }
	  @SubscribeEvent
	  public void onEntityConstruct(AttachCapabilitiesEvent evt) {
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
