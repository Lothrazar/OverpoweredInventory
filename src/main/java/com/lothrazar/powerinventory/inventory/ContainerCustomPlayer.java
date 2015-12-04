package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerCustomPlayer extends Container
{ 
	public InventoryCustomPlayer invo;
	static int S_MAIN_START;
	static int S_MAIN_END;
	static int S_ECHEST;
	static int S_PEARL;
	static final int OFFSCREEN = 600;
	public ContainerCustomPlayer(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		int i,j,slotNum,x=0,y=0,yStart = 84;

        S_MAIN_START = this.inventorySlots.size();
        
        //TODO: swap what is hidden based on features/buttons/powerups etc . for now, test
       // boolean isHidden = Math.random() > 0.5;

        // TOP LEFT: the player inventory mirror
        
        for (i = 0; i < Const.ROWS_VANILLA; ++i)
        {
            for (j = 0; j < Const.COLS_VANILLA; ++j)
            {
            	slotNum = j + (i + 1) * 9;
          
            	x = Const.paddingLrg + j * Const.SQ;
            	y = yStart + i * Const.SQ;
        		this.addSlotToContainer(new Slot(inventoryPlayer, slotNum, x,y));
            }
        }

        int oldx = x + Const.SQ;
        int oldy = y + Const.SQ;
        // TOP RIGHT
        for (i = 0; i < Const.ROWS_VANILLA; ++i)
        {
            for (j = 0; j < Const.COLS_VANILLA; ++j)
            {
            	slotNum = Const.VSIZE + j + (i + 1) * 9;
       
            	x = oldx + j * Const.SQ;
            	y = yStart + i * Const.SQ;
        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
            }
        }
        
        // BOTTOM LEFT:
        for (i = 0; i < Const.ROWS_VANILLA; ++i)
        {
            for (j = 0; j < Const.COLS_VANILLA; ++j)
            {
            	slotNum = Const.VSIZE*2 + j + (i + 1) * 9;
       
            	x = Const.paddingLrg + j * Const.SQ;
            	y = oldy + i * Const.SQ;
        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
            }
        }
        // BOTTOM RIGHT
        for (i = 0; i < Const.ROWS_VANILLA; ++i)
        {
            for (j = 0; j < Const.COLS_VANILLA; ++j)
            {
            	slotNum = Const.VSIZE*3 + j + (i + 1) * 9;
       
            	x = oldx + j * Const.SQ;
            	y = oldy + i * Const.SQ;
        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
            }
        }
        
        S_MAIN_END = this.inventorySlots.size() - 1;

		S_PEARL =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderPearl(inventoryCustom, Const.enderPearlSlot));

        S_ECHEST =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderChest(inventoryCustom, Const.enderChestSlot)); 

		invo = inventoryCustom;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int slotNumber)
	{
		ItemStack stackCopy = null;
		
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);
		
		if (slot != null && slot.getHasStack())
		{
			
			ItemStack stackOrig = slot.getStack();
			
			stackCopy = stackOrig.copy();
	
			if (slotNumber >= S_MAIN_START && slotNumber <= S_MAIN_END) // main inv grid
			{ 
            	if(stackCopy.getItem() == Items.ender_pearl && 
            		(	
        			invo.getStackInSlot(Const.enderPearlSlot) == null || 
        			invo.getStackInSlot(Const.enderPearlSlot).stackSize < Items.ender_pearl.getItemStackLimit(stackCopy))
        			)
        		{
            		if (!this.mergeItemStack(stackOrig, S_PEARL, S_PEARL+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Item.getItemFromBlock(Blocks.ender_chest) && 
            		(
        			invo.getStackInSlot(Const.enderChestSlot) == null || 
        			invo.getStackInSlot(Const.enderChestSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_ECHEST, S_ECHEST+1, false))
                	{ 
                        return null;
                    }  
        		}
            }
            else if(slotNumber == S_PEARL || slotNumber == S_ECHEST)
            { 
            	if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
            	{
                    return null;
                }
            }
			
			//now cleanup steps

			if (stackOrig.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (stackOrig.stackSize == stackCopy.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(p, stackOrig);

		}

		return stackCopy;
	
	} //end transfer function
}
