package com.lothrazar.powerinventory.inventory.slot;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.config.ModConfig;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotEnderChest extends Slot {
	private final static String _background = "textures/items/empty_enderchest.png";
	public final static ResourceLocation background = new ResourceLocation(Const.MODID, _background);
	public static final int posX = ModConfig.getInvoWidth() - 6 - Const.SQ;
	public static final int posY = 8;

	public int slotIndex;// overrides the private internal one

	public SlotEnderChest(IInventory inventoryIn, int index) {
		super(inventoryIn, index, posX, posY);

		slotIndex = index;
	}

	@Override
	public int getSlotIndex() {
		return slotIndex;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return (stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.ender_chest));
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
