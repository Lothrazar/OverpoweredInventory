package com.lothrazar.powerinventory.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnderPearl extends Slot
{
	public SlotEnderPearl(IInventory inventoryIn, int index, int xPosition,int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);

		slotIndex = index;
	}
	public int slotIndex;//overrides the private internal one
	@Override
	public int getSlotIndex()
    {
        return slotIndex;
    }
	@Override
	public boolean isItemValid(ItemStack stack)
    {
		return (stack != null && stack.getItem() == Items.ender_pearl);
    }
}
