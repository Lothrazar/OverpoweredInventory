package com.lothrazar.powerinventory.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSlotStackLimit()
    {
        return Items.ender_pearl.getItemStackLimit();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {  
        return "minecraft:items/ender_pearl"; 
    }
}
