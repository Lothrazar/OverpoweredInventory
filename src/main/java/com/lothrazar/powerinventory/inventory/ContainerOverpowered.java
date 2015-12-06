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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerOverpowered extends Container
{ 
	public InventoryOverpowered invo;
	public InventoryCrafting craftMatrix;
    public IInventory craftResult = new InventoryCraftResult();
    public boolean epearlSlotEnabled;
    public boolean echestSlotEnabled;
   // public boolean[] storageEnabled = new boolean[4];//slot zero not used
    final static int DISABLED=-1;//since they cant be null in Java
	static int S_RESULT;
	static int S_MAIN_START;
	static int S_MAIN_END;
	static int S_ECHEST=DISABLED;
	static int S_PEARL=DISABLED;

	static int S_BAR_START;
	static int S_BAR_END;
    static int S_BAROTHER_START;
    static int S_BAROTHER_END;

    final int hotbarX = 8;
    final int hotbarY = 142 + (Const.SQ * 9);
	final int offset = 4;//size of grey space between the sections
	
	//static final int OFFSCREEN = 600;
    private final EntityPlayer thePlayer;
	public ContainerOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryOverpowered inventoryCustom)
	{
		thePlayer = player;
		
		PlayerPersistProperty prop = PlayerPersistProperty.get(thePlayer);

		int i,j,slotNum=0,x=0,y=0,yStart = 13+Const.SQ, paddingLrg=8;
 
	  	int rowsMoreThanV = 3;
        int hotbarY = 142 + (Const.SQ * rowsMoreThanV) + offset;
		
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
          
            	x = paddingLrg + j * Const.SQ;
            	y = yStart + i * Const.SQ;
        		this.addSlotToContainer(new Slot(inventoryPlayer, slotNum, x,y));
            }
        }

        int oldx = x + Const.SQ;
        int oldy = y + Const.SQ;
        
        
        
        
        // TOP RIGHT
        if (prop.getStorage(Const.STORAGE_1))
	        for (i = 0; i < Const.ROWS_VANILLA; ++i)
	        {
	            for (j = 0; j < Const.COLS_VANILLA; ++j)
	            {
	            	slotNum = Const.V_INVO_SIZE + j + (i + 1) * 9;
	       
	            	x = oldx + j * Const.SQ + offset;
	            	y = yStart + i * Const.SQ;
	        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
	            }
	        }
        
        // BOTTOM LEFT:
        if (prop.getStorage(Const.STORAGE_2))
	        for (i = 0; i < Const.ROWS_VANILLA; ++i)
	        {
	            for (j = 0; j < Const.COLS_VANILLA; ++j)
	            {
	            	slotNum = Const.V_INVO_SIZE*2 + j + (i + 1) * 9;
	       
	            	x = paddingLrg + j * Const.SQ;
	            	y = oldy + i * Const.SQ + offset;
	        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
	            }
	        }
        // BOTTOM RIGHT
        if (prop.getStorage(Const.STORAGE_3))
	        for (i = 0; i < Const.ROWS_VANILLA; ++i)
	        {
	            for (j = 0; j < Const.COLS_VANILLA; ++j)
	            {
	            	slotNum = Const.V_INVO_SIZE*3 + j + (i + 1) * 9;
	       
	            	x = oldx + j * Const.SQ + offset;
	            	y = oldy + i * Const.SQ + offset;
	        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
	            }
	        }
        oldx = x + Const.SQ;
        oldy = y + Const.SQ;
        //another row down
        if (prop.getStorage(Const.STORAGE_4))
	        for (i = 0; i < Const.ROWS_VANILLA; ++i)
	        {
	            for (j = 0; j < Const.COLS_VANILLA; ++j)
	            {
	            	slotNum = Const.V_INVO_SIZE*4 + j + (i + 1) * 9;
	       
	            	x = paddingLrg + j * Const.SQ;
	            	y = oldy + i * Const.SQ + offset;
	        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
	            }
	        }
        if (prop.getStorage(Const.STORAGE_5))
	        for (i = 0; i < Const.ROWS_VANILLA; ++i)
	        {
	            for (j = 0; j < Const.COLS_VANILLA; ++j)
	            {
	            	slotNum = Const.V_INVO_SIZE*5 + j + (i + 1) * 9;
	       
	            	x = oldx + j * Const.SQ + offset;
	            	y = oldy + i * Const.SQ + offset;
	        		this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x,y));
	            }
	        }
        S_MAIN_END = this.inventorySlots.size() - 1;

        S_BAROTHER_START = this.inventorySlots.size();
        for (i = Const.HOTBAR_SIZE; i < 2*Const.HOTBAR_SIZE; ++i)
        { 
        	x = hotbarX + i * Const.SQ + offset; 
     
            slotNum++;
            this.addSlotToContainer(new Slot(inventoryCustom, slotNum, x, hotbarY));
        }
        S_BAROTHER_END = this.inventorySlots.size() - 1;
        
        if(epearlSlotEnabled){
			S_PEARL =  this.inventorySlots.size() ;
	        this.addSlotToContainer(new SlotEnderPearl(inventoryCustom, Const.SLOT_EPEARL));
        }
        
        if(echestSlotEnabled){
	        S_ECHEST =  this.inventorySlots.size() ;
	        this.addSlotToContainer(new SlotEnderChest(inventoryCustom, Const.SLOT_ECHEST)); 
        }
 
		invo = inventoryCustom;
	} 
	@Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn); 
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
            	if(this.epearlSlotEnabled && stackCopy.getItem() == Items.ender_pearl && 
            		(	
        			invo.getStackInSlot(Const.SLOT_EPEARL) == null || 
        			invo.getStackInSlot(Const.SLOT_EPEARL).stackSize < Items.ender_pearl.getItemStackLimit(stackCopy))
        			)
        		{
            		if (!this.mergeItemStack(stackOrig, S_PEARL, S_PEARL + 1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(this.echestSlotEnabled && stackCopy.getItem() == Item.getItemFromBlock(Blocks.ender_chest) && 
            		(
        			invo.getStackInSlot(Const.SLOT_ECHEST) == null || 
        			invo.getStackInSlot(Const.SLOT_ECHEST).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_ECHEST, S_ECHEST+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if (!this.mergeItemStack(stackOrig, S_BAR_START, S_BAR_END + 1, false)            			)
            	{
                    return null;
                }
            }
            else if (slotNumber >= S_BAR_START && slotNumber <= S_BAR_END || 
            		 slotNumber >= S_BAROTHER_START && slotNumber <= S_BAROTHER_END) // Hotbars
            { 
            	if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
            	{
                    return null;
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
