package com.lothrazar.powerinventory.inventory.slot;

import com.lothrazar.powerinventory.Const;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotEnderPearl extends Slot
{
	private final static String _background = "textures/items/empty_enderpearl.png";
	public final static ResourceLocation background = new ResourceLocation(Const.MODID,_background);
	public static int posX;
	public static int posY;
	public int slotIndex;//overrides the private internal one
	
	public SlotEnderPearl(IInventory inventoryIn, int index) 
	{
		super(inventoryIn, index, posX, posY);
 
		slotIndex = index;
		
		//I TRIED THIS< it doesnt WORK
		// this.setBackgroundIconTexture( new ResourceLocation(Const.MODID, "textures/items/empty_enderpearl.png"));
	}
	
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
}
