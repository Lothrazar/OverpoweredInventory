package com.lothrazar.powerinventory.inventory;

import java.util.Arrays;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.InventoryRenderer;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.inventory.button.GuiButtonRotate;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockChest;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockPearl;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockStorage;
import com.lothrazar.powerinventory.inventory.button.IGuiTooltip;
import com.lothrazar.powerinventory.inventory.slot.*;
import com.lothrazar.powerinventory.util.UtilExperience;
import com.lothrazar.powerinventory.util.UtilTextureRender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiOverpowered extends GuiContainer {
	private ResourceLocation bkg = new ResourceLocation(Const.MODID, "textures/gui/inventory.png");
	private ResourceLocation bkg_large = new ResourceLocation(Const.MODID, "textures/gui/inventory_large.png");

	private ResourceLocation bkg_3x9 = new ResourceLocation(Const.MODID, "textures/gui/slots3x9.png");
	public static ResourceLocation slot = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot.png");

	public static boolean SHOW_DEBUG_NUMS = false;
	private final InventoryOverpowered inventory;
	private ContainerOverpowered container;
	final int h = 20;
	final int w = 20;// default button dims
	final int padding = 6;// on the far outer sizes
	final EntityPlayer thePlayer;

	public GuiOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryOverpowered inventoryCustom) {
		// the player.inventory gets passed in here
		super(new ContainerOverpowered(player, inventoryPlayer, inventoryCustom));
		container = (ContainerOverpowered) this.inventorySlots;
		inventory = inventoryCustom;
		thePlayer = player;

		// fixed numbers from the .png resource size
		this.xSize = ModConfig.getInvoWidth();
		this.ySize = ModConfig.getInvoHeight();
	}

	@Override
	public void initGui() {
		super.initGui();
		PlayerPersistProperty prop = PlayerPersistProperty.get(thePlayer);

		int button_id = 99;
		String label;
		GuiButton b;

		if (prop.isEPearlUnlocked() == false) {

			int current = (int) UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostPearl;

			b = new GuiButtonUnlockPearl(button_id++, this.guiLeft + padding, this.guiTop + padding, label);
			this.buttonList.add(b);

			b.enabled = (current >= ModConfig.expCostPearl);
		}
		if (prop.isEChestUnlocked() == false) {

			int current = (int) UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostEChest;

			b = new GuiButtonUnlockChest(button_id++, this.guiLeft + ModConfig.getInvoWidth() - padding - GuiButtonUnlockChest.width, this.guiTop + padding, label);
			this.buttonList.add(b);

			b.enabled = (current >= ModConfig.expCostEChest);
		}
		
		int current = (int) UtilExperience.getExpTotal(thePlayer);
		// draw only one single button then stop
		for (int i = 1; i <= ModConfig.getMaxSections(); i++) {
			if (prop.hasStorage(i) == false) {

				label = current + "/" + ModConfig.expCostStorage + " XP";

				b = new GuiButtonUnlockStorage(button_id++, this.guiLeft + InventoryRenderer.xPosBtn(i), this.guiTop + InventoryRenderer.yPosBtn(i), label, i);

				this.buttonList.add(b);

				b.enabled = (current >= ModConfig.expCostStorage);
				break;
			}
		}
		
		// position them exactly on players inventory
		int x = this.guiLeft+ 30;//screenWidth / 2 + Const.VWIDTH / 2 - w * 3;
		int y = this.guiTop + 7;//screenHeight / 2 - Const.VHEIGHT / 2 + padding;
		
		//TODO: in large mode we might need two rows or something

		// int storage = prop.getStorageCount();
		for (int i = 1; i <= ModConfig.getMaxSections(); i++) {

			if (prop.hasStorage(i))
				this.buttonList.add(new GuiButtonRotate(button_id++, x, y, w, h, i));

			x += w + padding;//-= 2 * w - padding;// 
		}
	}

	@Override
	public void drawScreen(int x, int y, float par3) {
		super.drawScreen(x, y, par3);

		GuiButton btn;
		for (int i = 0; i < buttonList.size(); i++) {
			btn = buttonList.get(i);
			if (btn instanceof IGuiTooltip && btn.isMouseOver()) {
				String tooltip = ((IGuiTooltip) btn).getTooltip();
				if (tooltip != null) {
					// it takes a list, one on each line. but we use single line
					// tooltips
					drawHoveringText(Arrays.asList(new String[] { tooltip }), x, y, fontRendererObj);
				}
			}
		}

		if (SHOW_DEBUG_NUMS) {
			for (Slot s : this.container.inventorySlots) {
				// each slot has two different numbers. the slotNumber is
				// UNIQUE, the index is not
				this.drawString(this.fontRendererObj, "" + s.getSlotIndex(), this.guiLeft + s.xDisplayPosition, this.guiTop + s.yDisplayPosition + 4, 16777120);// font
																																								// color
			}
		}
	}

	final int s = 16;

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		PlayerPersistProperty prop = PlayerPersistProperty.get(thePlayer);

		if (prop.isEPearlUnlocked() && inventory.getStackInSlot(Const.SLOT_EPEARL) == null) {
			UtilTextureRender.drawTextureSimple(SlotEnderPearl.background, SlotEnderPearl.posX, SlotEnderPearl.posY, s, s);
		}

		if (prop.isEChestUnlocked() && inventory.getStackInSlot(Const.SLOT_ECHEST) == null) {
			UtilTextureRender.drawTextureSimple(SlotEnderChest.background, SlotEnderChest.posX, SlotEnderChest.posY, s, s);
		}
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		if (ModConfig.isLargeScreen())
			UtilTextureRender.drawTextureSimple(bkg_large, this.guiLeft, this.guiTop, this.xSize, this.ySize);
		else
			UtilTextureRender.drawTextureSimple(bkg, this.guiLeft, this.guiTop, this.xSize, this.ySize);

		PlayerPersistProperty prop = PlayerPersistProperty.get(thePlayer);

		for (int i = 1; i <= ModConfig.getMaxSections(); i++) {
			if (prop.hasStorage(i))
				drawSlotSectionAt(this.guiLeft + InventoryRenderer.xPosTexture(i), this.guiTop + InventoryRenderer.yPosTexture(i));
		}

		if (prop.isEChestUnlocked()) {
			drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);
		}
		if (prop.isEPearlUnlocked()) {
			drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);
		}
	}

	private void drawSlotSectionAt(int x, int y) {
		UtilTextureRender.drawTextureSimple(bkg_3x9, x, y, Const.SLOTS_WIDTH, Const.SLOTS_HEIGHT);
	}

	private void drawSlotAt(int x, int y) {
		UtilTextureRender.drawTextureSimple(slot, this.guiLeft + x - 1, this.guiTop + y - 1, Const.SQ, Const.SQ);
	}
}
