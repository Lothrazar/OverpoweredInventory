package com.lothrazar.powerinventory.inventory.slot;

import net.minecraft.init.Blocks; 
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotEnderChest extends Slot
{
	public int slotIndex;//overrides the private internal one
	
	public SlotEnderChest(IInventory inventoryIn, int index, int xPosition,int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);
 
		slotIndex = index;
	}
	
	@Override
	public int getSlotIndex()
    {
        return slotIndex;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
		return (stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.ender_chest));
    }
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}
