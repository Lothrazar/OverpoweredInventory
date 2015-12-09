package com.lothrazar.powerinventory;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
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
		if (event.modID.equals(Const.MODID))
			ModConfig.syncConfig();
	}

	@SubscribeEvent
	public void onEntityConstruct(EntityConstructing event) // More reliable than on entity join
	{
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;

			if (PlayerPersistProperty.get(player) == null) {
				PlayerPersistProperty.register(player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		if (event.wasDeath == false || // changing dimensions -> so always do it or it was    a death  => maybe     do it           
				(ModConfig.persistUnlocksOnDeath && event.wasDeath))				
		{
			PlayerPersistProperty.clonePlayerData(event.original, event.entityPlayer);
		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		if (event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
			EntityPlayer p = (EntityPlayer) event.entityLiving;

			PlayerPersistProperty prop = PlayerPersistProperty.get(p);
			// the vanilla inventory stuff (first hotbar) already drops by default
 
			for (int i = Const.HOTBAR_SIZE; i < prop.inventory.getSizeInventory(); ++i) {
				prop.inventory.dropStackInSlot(p, i);
			}

			prop.inventory.dropStackInSlot(p, Const.SLOT_ECHEST);
			prop.inventory.dropStackInSlot(p, Const.SLOT_EPEARL);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event) {
		if (event.gui == null) {
			return;
		}// probably doesnt ever happen

		if (event.gui instanceof net.minecraft.client.gui.inventory.GuiInventory) {
			// omg thanks so much to this guy
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/mods-discussion/1390983-making-guis-scale-to-screen-width-height
			ScaledResolution res = new ScaledResolution(event.gui.mc,event.gui.mc.displayWidth,event.gui.mc.displayHeight);
					//,event.gui.width,event.gui.height);//NPOE

			int screenWidth = res.getScaledWidth();
			int screenHeight = res.getScaledHeight();

			int button_id = 256;
			int h = 10, w = 20, x, y;

			x = screenWidth / 2 + Const.VWIDTH / 2 - w;// align tight to top 
			y = screenHeight / 2 - Const.VHEIGHT / 2 - 2 * h - 1;
			
			event.buttonList.add(new GuiButtonOpenInventory(button_id++, x, y));
			/*
			int padding=4;
			h = 10;
			w = 10;

			PlayerPersistProperty prop = PlayerPersistProperty.get(event.gui.mc.thePlayer);
			// position them exactly on players inventory
			x = screenWidth / 2 - Const.VWIDTH/2 + padding+78;//screenWidth / 2 + Const.VWIDTH / 2 - w * 3;
			y = screenHeight / 2 - Const.VHEIGHT/2 + padding+62;//screenHeight / 2 - Const.VHEIGHT / 2 + padding;
			//TODO: in large mode we might need two rows or something

			// int storage = prop.getStorageCount();
			for (int i = 1; i <= ModConfig.getMaxSections(); i++) {

				if (prop.hasStorage(i))
					event.buttonList.add(new GuiButtonRotate(button_id++, x, y, w, h,""+i, i));

				x += w + padding;//-= 2 * w - padding;// 
			}
			*/
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
