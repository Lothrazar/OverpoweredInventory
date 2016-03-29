package com.lothrazar.powerinventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
	public void onEntityConstruct(EntityConstructing event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();

			if (PlayerPersistProperty.get(player) == null) {
				PlayerPersistProperty.register(player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (event.isWasDeath() == false || // changing dimensions -> so always do it
										// or it was a death => maybe do it
		(ModConfig.persistUnlocksOnDeath && event.isWasDeath())) {
			PlayerPersistProperty.clonePlayerData(event.getOriginal(), event.getEntityPlayer());
			
		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		//if config says they persist... do not drop on ground
		
		if (ModConfig.persistUnlocksOnDeath == false && 
				event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().worldObj.isRemote) {
			EntityPlayer p = (EntityPlayer) event.getEntityLiving();

			PlayerPersistProperty prop = PlayerPersistProperty.get(p);
			// the vanilla inventory stuff (first hotbar) already drops  
			
			for (int i = Const.HOTBAR_SIZE; i < prop.inventory.getSizeInventory(); ++i) {
				prop.inventory.dropStackInSlot(p, i);
			}

			prop.inventory.dropStackInSlot(p, Const.SLOT_ECHEST);
			prop.inventory.dropStackInSlot(p, Const.SLOT_EPEARL);
		}
	}
	/*
	// InitGuiEvent.Post
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawScreenEvent(DrawScreenEvent event) {
		if (event.gui != null && event.gui instanceof net.minecraft.client.gui.inventory.GuiInventory) {
			
		}
	}
	*/

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event) {
		GuiScreen gui = event.getGui();
		if (gui == null) {
			return;
		}// probably doesnt ever happen

		
		if (ModConfig.showGuiButton && gui != null && gui instanceof net.minecraft.client.gui.inventory.GuiInventory) {
			// omg thanks so much to this guy
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/mods-discussion/1390983-making-guis-scale-to-screen-width-height
			ScaledResolution res = new ScaledResolution(gui.mc);

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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		// force it to always show food - otherwise its hidden when riding a
		// horse

		if (ModConfig.alwaysShowHungerbar) {
			GuiIngameForge.renderFood = true;
		}
	}
}
