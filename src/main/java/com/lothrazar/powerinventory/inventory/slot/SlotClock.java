package com.lothrazar.powerinventory.inventory.slot;

import com.lothrazar.powerinventory.Const;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotClock extends Slot
{
	private final static String _background = "textures/items/empty_clock.png";
	public final static ResourceLocation background = new ResourceLocation(Const.MODID,_background);
	public static int posX;
	public static int posY;
	public int slotIndex;//overrides the private internal one
	
	public SlotClock(IInventory inventoryIn, int index) 
	{
		super(inventoryIn, index, posX, posY);
 
		slotIndex = index;
		//this.setBackgroundName(background);//doesnt actually work
	}
	
	@Override
	public int getSlotIndex()
    {
        return slotIndex;
    }
	
	@Override
	public boolean isItemValid(ItemStack stack)
    {
		return (stack != null && stack.getItem() == Items.clock);
    }
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}
