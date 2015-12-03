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
	static int S_MAIN_START;
	static int S_MAIN_END;
	public InventoryCustomPlayer invo;
	static int S_ECHEST;
	static int S_PEARL;
	static final int OFFSCREEN = 600;
	public ContainerCustomPlayer(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		int i,j,ix,x,y;

        S_MAIN_START = this.inventorySlots.size();
        
        //TODO: swap what is hidden based on features/buttons/powerups etc . for now, test
        boolean isHidden = Math.random() > 0.5;

        for (i = 0; i < 3*2; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
            	ix = j + (i + 1) * 9;
            	x = 8 + j * 18;
            	y = 84 + i * 18;
            	if(ix < 36)
            		this.addSlotToContainer(new Slot(inventoryPlayer, ix, x,y));
            	else{
            		if(isHidden){x += OFFSCREEN;}//off the screen
            		this.addSlotToContainer(new Slot(inventoryCustom, ix, x,y));
            	}
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
