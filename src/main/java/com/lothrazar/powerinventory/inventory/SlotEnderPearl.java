package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
    }/*
	@Override
	@SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getBackgroundSprite()
    { 
        String name = getSlotTexture();
		System.out.println("getBackgroundSprite    "+name);//works but does naaaadda
        return name == null ? null : getBackgroundMap().getAtlasSprite(name);
    }
	*/
	
	@Override
	@SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {/*
		String loc = "items/empty_enderpearl";//textures/
		  ResourceLocation back = new ResourceLocation(Const.MODID,loc);
			Minecraft.getMinecraft().getTextureManager().bindTexture(back);

			this.setBackgroundLocation(back);
			*/

        return "minecraft:items/ender_pearl";//????
      //  return Const.MODID+":"+loc; 
    }
}
