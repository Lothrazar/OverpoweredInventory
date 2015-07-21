package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;

import net.minecraft.init.Blocks; 
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotEnderChest extends Slot
{
	public int slotIndex;//overrides the private internal one
	
	public SlotEnderChest(IInventory inventoryIn, int index, int xPosition,int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);
 
		//this.setBackgroundName(Const.MODID+":items/empty_enderchest");
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
	 
	@Override
	@SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {
	
		 return Const.MODID+":items/empty_enderchest"; 
 
        //like  public static final String[] EMPTY_SLOT_NAMES = new String[] {"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
    }
/*
	@Override
 @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getBackgroundSprite()
    { 
        String name = getSlotTexture();
		//System.out.println("getBackgroundSprite    "+name);//works but does naaaadda
        return name == null ? null : getBackgroundMap().getAtlasSprite(name);
    }*/

}
