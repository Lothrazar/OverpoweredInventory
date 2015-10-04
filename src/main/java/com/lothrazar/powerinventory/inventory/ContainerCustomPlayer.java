package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModConfig;
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
	//private EntityPlayer thePlayer;
 

	static int S_BAR_START;
	static int S_BAR_END;
	static int S_MAIN_START;
	static int S_MAIN_END;
	public InventoryCustomPlayer invo;
	static int S_BOTTLE;
	static int S_UNCRAFT;
	static int S_ECHEST;
	static int S_PEARL;
	static int S_CLOCK;
	static int S_COMPASS;
	public ContainerCustomPlayer(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
	//	thePlayer = player;

		int i;
		
		
	 
		
		//armor slots would go here
	/*	 for (i = 0; i < Const.armorSize; ++i)
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
        }*/


        //hotbar
        S_BAR_START = this.inventorySlots.size();
        for (i = 0; i < Const.hotbarSize; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
        S_BAR_END = this.inventorySlots.size() - 1;
        
		//vanilla invo slots
        S_MAIN_START = this.inventorySlots.size();
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }
        S_MAIN_END = this.inventorySlots.size() - 1;


		S_PEARL =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderPearl(inventoryCustom, Const.enderPearlSlot, Const.pearlX, Const.pearlY));

        S_ECHEST =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderChest(inventoryCustom, Const.enderChestSlot, Const.echestX, Const.echestY)); 

        S_CLOCK =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotClock(inventoryCustom, Const.clockSlot, Const.clockX, Const.clockY)); 

        S_COMPASS =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotCompass(inventoryCustom, Const.compassSlot, Const.compassX, Const.compassY)); 
        
		
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
            	//only from here are we doing the special items
            //ALL COPIED FROM BigContainer
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
            	else if(stackCopy.getItem() == Items.compass && 
            		(
        			invo.getStackInSlot(Const.compassSlot) == null || 
        			invo.getStackInSlot(Const.compassSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_COMPASS, S_COMPASS+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.clock && 
            		(
        			invo.getStackInSlot(Const.clockSlot) == null || 
        			invo.getStackInSlot(Const.clockSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_CLOCK, S_CLOCK+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.glass_bottle && ModConfig.enableEnchantBottles )
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_BOTTLE, S_BOTTLE+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if (!this.mergeItemStack(stackOrig, S_BAR_START, S_BAR_END+1, false)            			)
            	{
            		
                    return null;
                }
            }///===
            else if (slotNumber >= S_BAR_START && slotNumber <= S_BAR_END) // Hotbar
            { 
            	if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
            	{
                    return null;
                }
            }
            else if(slotNumber == S_PEARL || slotNumber == S_ECHEST  || slotNumber == S_COMPASS  || slotNumber == S_CLOCK || slotNumber == S_BOTTLE
            		|| slotNumber == S_UNCRAFT)
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
