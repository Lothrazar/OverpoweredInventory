package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerOverpowered extends Container
{ 
	public InventoryOverpowered invo;
	public InventoryCrafting craftMatrix;
    public IInventory craftResult = new InventoryCraftResult();
    public boolean craftingEnabled = true;//TODO: toggle this somehow from somewhere?
	public static final int CRAFTSIZE = 3;//did not exist before, was magic'd as 2 everywhere
	static int S_RESULT;
	static int S_CRAFT_START;
	static int S_CRAFT_END;
	static int S_MAIN_START;
	static int S_MAIN_END;
	static int S_ECHEST;
	static int S_PEARL;

	static int S_BAR_START;
	static int S_BAR_END;
    static int S_BAROTHER_START;
    static int S_BAROTHER_END;

    int hotbarX = Const.paddingLrg;
    int hotbarY = 142 + (Const.SQ * 9);
	
	//static final int OFFSCREEN = 600;
    private final EntityPlayer thePlayer;
	public ContainerOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryOverpowered inventoryCustom)
	{
		thePlayer = player;
		
		PlayerPersistProperty prop = PlayerPersistProperty.get(player);
		
		craftingEnabled = prop.hasInvoCrafting();
		
		int i,j,slotNum=0,x=0,y=0,yStart = 84;

		if(craftingEnabled){
			craftMatrix = new InventoryCrafting(this, CRAFTSIZE, CRAFTSIZE);
			
			S_RESULT = this.inventorySlots.size();
	        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 
	        		3+yStart + GuiOverpowered.craftX, //  +111
	        		3+3+17 + GuiOverpowered.craftY)); //+17
	
	        S_CRAFT_START = this.inventorySlots.size();
	        for (i = 0; i < CRAFTSIZE; ++i)
	        { 
	            for (j = 0; j < CRAFTSIZE; ++j)
	            {  
	    			x = 3+ GuiOverpowered.craftX + j * Const.SQ ; 
	    			y = 3+ GuiOverpowered.craftY + i * Const.SQ ;
	
	        		this.addSlotToContainer(new Slot(this.craftMatrix, j + i * CRAFTSIZE, x , y)); 
	            }
	        }
	        S_CRAFT_END = this.inventorySlots.size() - 1;
		}
		
		
	  	int rowsMoreThanV = 3;
        int hotbarY = 142 + (Const.SQ * rowsMoreThanV);
		
        S_BAR_START = this.inventorySlots.size();
        for (i = 0; i < Const.HOTBAR_SIZE; ++i)
        { 
        	x = hotbarX + i * Const.SQ; 
 
            this.addSlotToContainer(new Slot(inventoryPlayer, i, x, hotbarY));
        }
        S_BAR_END = this.inventorySlots.size() - 1;
		
        S_MAIN_START = this.inventorySlots.size();
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

        S_BAROTHER_START = this.inventorySlots.size();
        for (i = Const.HOTBAR_SIZE; i < 2*Const.HOTBAR_SIZE; ++i)
        { 
        	x = hotbarX + i * Const.SQ; 
     
            slotNum++;
            this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x, hotbarY));
        }
        S_BAROTHER_END = this.inventorySlots.size() - 1;
        
		S_PEARL =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderPearl(inventoryCustom, Const.enderPearlSlot));

        S_ECHEST =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderChest(inventoryCustom, Const.enderChestSlot)); 

		if(craftingEnabled){
			this.onCraftMatrixChanged(this.craftMatrix);
		}
		invo = inventoryCustom;
	}
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
    {
		if(craftingEnabled){
			this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
		}
        super.onCraftMatrixChanged(inventoryIn);
    }
	@Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
		if(craftingEnabled){
	
	        for (int i = 0; i < CRAFTSIZE * CRAFTSIZE; ++i)  
	        {
	            ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
	
	            if (itemstack != null)
	            {
	                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
	            }
	        }
	
	        this.craftResult.setInventorySlotContents(0, (ItemStack)null);
		}
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
