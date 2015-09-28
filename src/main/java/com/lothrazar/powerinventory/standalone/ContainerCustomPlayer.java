package com.lothrazar.powerinventory.standalone;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.SlotBottle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ContainerCustomPlayer extends Container
{
	private static final int ARMOR_START = InventoryCustomPlayer.INV_SIZE, ARMOR_END = ARMOR_START+3,

			INV_START = ARMOR_END+1, INV_END = INV_START+26, HOTBAR_START = INV_END+1,

			HOTBAR_END = HOTBAR_START+8;

	static int S_BOTTLE;
	static int S_UNCRAFT;
	private EntityPlayer thePlayer;
	public ContainerCustomPlayer(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		thePlayer = player;

		int i;
		
		
		//int slot = 0;
		
	    if(ModConfig.enableEnchantBottles)
        {
	        S_BOTTLE =  this.inventorySlots.size() ;
	        this.addSlotToContainer(new SlotBottle(inventoryCustom, Const.bottleSlot, Const.bottleX, Const.bottleY)); 
        }
        
        if(ModConfig.enableUncrafting)
        {
	        S_UNCRAFT =  this.inventorySlots.size() ; 
	        this.addSlotToContainer(new Slot(inventoryCustom, Const.uncraftSlot, Const.uncraftX, Const.uncraftY)); 
        }
		//this.addSlotToContainer(new Slot(inventoryCustom, slot++, 81, 8));

		//this.addSlotToContainer(new Slot(inventoryCustom, slot++, 81, 26));
	
		
		//armor slots would go here
		 for (i = 0; i < Const.armorSize; ++i)
        {
            final int k = i;
            this.addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - i, 8, 8 + i * 18)
            {
               // private static final String __OBFID = "CL_00001755";
              
                public int getSlotStackLimit()
                {
                    return 1;
                }
             
                public boolean isItemValid(ItemStack p_75214_1_)
                {
                    if (p_75214_1_ == null) return false;
                    return p_75214_1_.getItem().isValidArmor(p_75214_1_, k, thePlayer);
                }
           
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex()
                {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }

		
		//vanilla invo slots
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //hotbar
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }

		
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{

		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{
		ItemStack itemstackCopy = null;
		
		Slot slot = (Slot) this.inventorySlots.get(slotNum);
		
		if (slot != null && slot.getHasStack())
		{
			
			ItemStack stack = slot.getStack();
			
			itemstackCopy = stack.copy();
			// Either armor slot or custom item slot was clicked
			
			if (slotNum < INV_START)
			{
				// try to place in player inventory / action bar
				
				if (!this.mergeItemStack(stack, INV_START, HOTBAR_END + 1, true))
				{			
					return null;			
				}

				slot.onSlotChange(stack, itemstackCopy);
			
			}// Item is in inventory / hotbar, try to place either in custom or armor slots
			else 
			{
			 
			  // any specific items moved into our own slots
				if(itemstackCopy.getItem() == Items.glass_bottle )
        		{ 
            		if (!this.mergeItemStack(stack, S_BOTTLE, S_BOTTLE+1, false))
                	{ 
                        return null;
                    }  
        		}
				else  if (slotNum >= INV_START && slotNum < HOTBAR_START)
				{
				
					// place in action bar
					
					if (!this.mergeItemStack(stack, HOTBAR_START, HOTBAR_START + 1, false))
					
					{
					
					return null;
					
					}

				}// item in action bar - place in player inventory
				else if (slotNum >= HOTBAR_START && slotNum < HOTBAR_END + 1)
				{
					if (!this.mergeItemStack(stack, INV_START, INV_END + 1, false))
					{
					return null;
					}
				}

			}//end of giant else branch

			//now cleanup steps

			if (stack.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (stack.stackSize == itemstackCopy.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(player, stack);

		}

		return itemstackCopy;
	
	} //end transfer function
}
