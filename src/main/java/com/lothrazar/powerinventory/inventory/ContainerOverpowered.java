package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.CapabilityRegistry;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.InventoryRenderer;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.inventory.slot.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerOverpowered extends Container {
	public InventoryOverpowered invo;
	public InventoryCrafting craftMatrix;
	public IInventory craftResult = new InventoryCraftResult();

	final static int DISABLED = -1;// since they cant be null in Java
	static int S_RESULT;
	static int S_MAIN_START;
	static int S_MAIN_END;
	static int S_ECHEST = DISABLED;
	static int S_PEARL = DISABLED;

	static int S_BAR_START;
	static int S_BAR_END;
	static int S_BAROTHER_START;
	static int S_BAROTHER_END;

	final int hotbarX = 8;
	final int pad = 4;// size of grey space between the sections
	final int hotbarY = ModConfig.getInvoHeight() - Const.SQ - 7;

	// static final int OFFSCREEN = 600;
	private final EntityPlayer thePlayer;

	public ContainerOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryOverpowered inventoryCustom) {
		thePlayer = player;

		IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(thePlayer);

		int i, j, slotNum = 0, x = 0, y = 0;

		S_BAR_START = this.inventorySlots.size();
		for (i = 0; i < Const.HOTBAR_SIZE; ++i) {
			x = hotbarX + i * Const.SQ;
			slotNum = i;
			this.addSlotToContainer(new Slot(inventoryPlayer, slotNum, x, hotbarY));
		}
		S_BAR_END = this.inventorySlots.size() - 1;

		S_BAROTHER_START = this.inventorySlots.size();
		for (i = Const.HOTBAR_SIZE; i < 2 * Const.HOTBAR_SIZE; ++i) {
			x = hotbarX + i * Const.SQ + pad;

			slotNum = i;
			this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x, hotbarY));
		}
		S_BAROTHER_END = this.inventorySlots.size() - 1;

		if (prop.isEPearlUnlocked()) {
			S_PEARL = this.inventorySlots.size();
			this.addSlotToContainer(new SlotEnderPearl(inventoryCustom, Const.SLOT_EPEARL));
		}

		if (prop.isEChestUnlocked()) {
			S_ECHEST = this.inventorySlots.size();
			this.addSlotToContainer(new SlotEnderChest(inventoryCustom, Const.SLOT_ECHEST));
		}
		
		S_MAIN_START = this.inventorySlots.size();
		// TOP LEFT: the player inventory mirror

		int xStart;// = 2*pad;
		int yStart;// = 13+Const.SQ;//leaving one space for the slots on top row

		for (int k = 1; k <= ModConfig.getMaxSections(); k++) {
			if (prop.hasStorage(k)) {
				xStart = InventoryRenderer.xPosSlotsStart(k);
				yStart = InventoryRenderer.yPosSlotsStart(k);
				for (i = 0; i < Const.ROWS_VANILLA; ++i) {
					for (j = 0; j < Const.COLS_VANILLA; ++j) {
						slotNum = (k - 1) * Const.V_INVO_SIZE + j + (i + 1+1) * (Const.HOTBAR_SIZE);

						x = xStart + j * Const.SQ;
						y = yStart + i * Const.SQ;
						this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x, y));
					}
				}
			}
		}

		S_MAIN_END = this.inventorySlots.size() - 1;

		invo = inventoryCustom;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int slotNumber) {
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(thePlayer);
		ItemStack stackCopy = null;

		Slot slot = (Slot) this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {

			ItemStack stackOrig = slot.getStack();

			stackCopy = stackOrig.copy();

			if (slotNumber >= S_MAIN_START && slotNumber <= S_MAIN_END) // main
																		// inv
																		// grid
			{
				if (prop.isEPearlUnlocked() && stackCopy.getItem() == Items.ENDER_PEARL && (invo.getStackInSlot(Const.SLOT_EPEARL) == null || invo.getStackInSlot(Const.SLOT_EPEARL).stackSize < Items.ENDER_PEARL.getItemStackLimit(stackCopy))) {
					if (!this.mergeItemStack(stackOrig, S_PEARL, S_PEARL + 1, false)) {
						return null;
					}
				} else if (prop.isEChestUnlocked() && stackCopy.getItem() == Item.getItemFromBlock(Blocks.ENDER_CHEST) && (invo.getStackInSlot(Const.SLOT_ECHEST) == null || invo.getStackInSlot(Const.SLOT_ECHEST).stackSize < 1)) {
					if (!this.mergeItemStack(stackOrig, S_ECHEST, S_ECHEST + 1, false)) {
						return null;
					}
				} else if (!this.mergeItemStack(stackOrig, S_BAR_START, S_BAR_END + 1, false)) {
					return null;
				}
			} else if (slotNumber >= S_BAR_START && slotNumber <= S_BAR_END || slotNumber >= S_BAROTHER_START && slotNumber <= S_BAROTHER_END) // Hotbars
			{
				if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false)) {
					return null;
				}
			} else if (slotNumber == S_PEARL || slotNumber == S_ECHEST) {
				if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false)) {
					return null;
				}
			}

			// now cleanup steps

			if (stackOrig.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (stackOrig.stackSize == stackCopy.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(p, stackOrig);

		}

		return stackCopy;

	} // end transfer function
}
