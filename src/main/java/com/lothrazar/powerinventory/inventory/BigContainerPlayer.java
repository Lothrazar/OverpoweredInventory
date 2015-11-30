package com.lothrazar.powerinventory.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.slot.*;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class BigContainerPlayer extends ContainerPlayer
{	
	public static int ALL_COLS;
	public static int ALL_ROWS;
	public static final int craftSize = 3;//did not exist before, was magic'd as 2 everywhere
    private final EntityPlayer thePlayer;
 
	public BigInventoryPlayer invo;
    public boolean isLocalWorld;
    public static int S_BAROTHER_START;
    public static int S_BAROTHER_END;

	//these get used here for actual slot, and in GUI for texture
    //ender pearl is in the far bottom right corner, and the others move left relative to this

	
	
//store slot numbers  (not indexes) as we go. so that transferStack.. is actually readable
	 
	static int S_RESULT;
	static int S_CRAFT_START;
	static int S_CRAFT_END;
	static int S_ARMOR_START;
	static int S_ARMOR_END;
	static int S_BAR_START;
	static int S_BAR_END;
	static int S_MAIN_START;
	static int S_MAIN_END;
	static int S_ECHEST;
	static int S_PEARL;
	static int S_CLOCK;
	static int S_COMPASS;
	static int S_BOTTLE;
	static int S_UNCRAFT;
	
	public BigContainerPlayer(BigInventoryPlayer playerInventory, boolean isLocal, EntityPlayer player)
	{
		super(playerInventory, isLocal, player);
		
        this.thePlayer = player;
		inventorySlots = Lists.newArrayList();//undo everything done by super()
		craftMatrix = new InventoryCrafting(this, craftSize, craftSize);
 
        int i,j,cx,cy;//rows and cols of vanilla, not extra
   
        S_RESULT = this.inventorySlots.size();
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 
        		200,  
        		40));

        S_CRAFT_START = this.inventorySlots.size();
        for (i = 0; i < craftSize; ++i)
        { 
            for (j = 0; j < craftSize; ++j)
            {  
    			cx = 114 + j * Const.SQ ; 
    			cy = 20 + i * Const.SQ ;

        		this.addSlotToContainer(new Slot(this.craftMatrix, j + i * craftSize, cx , cy)); 
            }
        }
        S_CRAFT_END = this.inventorySlots.size() - 1;
        S_ARMOR_START = this.inventorySlots.size();
 
        for (i = 0; i < Const.ARMOR_SIZE; ++i)
        {
        	cx = 8;
        	cy = 8 + i * Const.SQ;
            final int k = i;
 
            this.addSlotToContainer(new Slot(playerInventory,  playerInventory.getSizeInventory() - 1 - i, cx, cy)
            { 
            	public int getSlotStackLimit()
	            {
	                return 1;
	            }
	            public boolean isItemValid(ItemStack stack)
	            {
	                if (stack == null) return false;
	                return stack.getItem().isValidArmor(stack, k, thePlayer);
	            }
	            @SideOnly(Side.CLIENT)
	            public String getSlotTexture()
	            {
	                return ItemArmor.EMPTY_SLOT_NAMES[k];
	            }
            }); 
        }
        S_ARMOR_END = this.inventorySlots.size() - 1;
        
        //how many rows were added extra on top of vanilla
        int moreRows = BigContainerPlayer.ALL_ROWS - Const.ROWS_VANILLA;
        //int MORE_COLS = BigContainerPlayer.ALL_COLS - Const.COLS_VANILLA;
         
        int hotbarX = Const.paddingLrg;
        int hotbarY = 142 + (Const.SQ * moreRows);
		
        S_BAR_START = this.inventorySlots.size();
        for (i = 0; i < Const.HOTBAR_SIZE; ++i)
        { 
        	cx = hotbarX + i * Const.SQ; 
 
            this.addSlotToContainer(new Slot(playerInventory, i, cx, hotbarY));
        }
        S_BAR_END = this.inventorySlots.size() - 1;
 
        S_MAIN_START = this.inventorySlots.size();
        int slotIndex = Const.HOTBAR_SIZE;
        
        for( i = 0; i < ALL_ROWS; i++)
		{
            for ( j = 0; j < ALL_COLS; ++j)
            { 
            	cx = 8 + j * Const.SQ;
            	cy = 84 + i * Const.SQ;
                this.addSlotToContainer(new Slot(playerInventory, slotIndex, cx, cy));
            	slotIndex++;
            }
        }
        S_MAIN_END = this.inventorySlots.size() - 1;
        
        //second hotbar
        S_BAROTHER_START = this.inventorySlots.size();
        for (i = Const.HOTBAR_SIZE; i < 2*Const.HOTBAR_SIZE; ++i)
        { 
        	cx = hotbarX + i * Const.SQ; 
        	
        	//TODO: stop using magic strings everywhere ya dufus
        	//make it like ModConfig.isLarge(), etc
        	if(ModConfig.smallMedLarge.equalsIgnoreCase("large") || ModConfig.smallMedLarge.equalsIgnoreCase("normal"))
        	{
        		cx += Const.SQ;//left an empty gap eh
        	}
 
            this.addSlotToContainer(new Slot(playerInventory, slotIndex, cx, hotbarY));
        	slotIndex++;
        }
        S_BAROTHER_END = this.inventorySlots.size() - 1;
        
        S_PEARL =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderPearl(playerInventory, Const.enderPearlSlot));

        S_ECHEST =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotEnderChest(playerInventory, Const.enderChestSlot)); 

        S_CLOCK =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotClock(playerInventory, Const.clockSlot)); 

        S_COMPASS =  this.inventorySlots.size() ;
        this.addSlotToContainer(new SlotCompass(playerInventory, Const.compassSlot)); 
        
        if(ModConfig.enableEnchantBottles)
        {
	        S_BOTTLE =  this.inventorySlots.size() ;
	        this.addSlotToContainer(new SlotBottle(playerInventory, Const.bottleSlot, Const.bottleX, Const.bottleY)); 
        }
        
        if(ModConfig.enableUncrafting)
        {
	        S_UNCRAFT =  this.inventorySlots.size() ; 
	        this.addSlotToContainer(new Slot(playerInventory, Const.uncraftSlot, Const.uncraftX, Const.uncraftY)); 
        }
        
        
        
 
        
        this.onCraftMatrixChanged(this.craftMatrix);
		this.invo = playerInventory; 
	}
  
	@Override
	public Slot getSlotFromInventory(IInventory invo, int id)
	{
		Slot slot = super.getSlotFromInventory(invo, id);
		/*
		if(slot == null)
		{
			Exception e = new NullPointerException();
			 
			ModInv.logger.log(Level.FATAL, e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + ":" + e.getStackTrace()[1].getLineNumber() + " is requesting slot " + id + " from inventory " + invo.getInventoryName() + " (" + invo.getClass().getName() + ") and got NULL!", e);
		}*/
		return slot;
	}
	
	@Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        for (int i = 0; i < craftSize*craftSize; ++i) // was 4
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

            if (itemstack != null)
            {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }

        this.craftResult.setInventorySlotContents(0, (ItemStack)null);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer p, int slotNumber)
    {  
		if(p.capabilities.isCreativeMode)
		{
			return super.transferStackInSlot(p, slotNumber);
		}
		//Thanks to coolAlias on the forums : 
		//http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571051-custom-container-how-to-properly-override-shift
		//above is from 2013 but still relevant
        ItemStack stackCopy = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack())
        {
            ItemStack stackOrig = slot.getStack();
            stackCopy = stackOrig.copy();
            if (slotNumber == S_RESULT)  
            { 
            	//System.out.printf("\ntest result %d %d ___  ",S_MAIN_START,S_MAIN_END);
                if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))//was starting at S_BAR_START
                {
                    return null;
                }

                slot.onSlotChange(stackOrig, stackCopy);
            }
            else if (slotNumber >= S_CRAFT_START && slotNumber <= S_CRAFT_END) 
            { 
                if (!this.mergeItemStack(stackOrig,  S_BAR_START, S_MAIN_END, false))//was 9,45
                {
                    return null;
                }
            }
            else if (slotNumber >= S_ARMOR_START && slotNumber <= S_ARMOR_END) 
            { 
                if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
                {
                    return null;
                }
            }
            else if (stackCopy.getItem() instanceof ItemArmor 
            		&& !((Slot)this.inventorySlots.get(S_ARMOR_START + ((ItemArmor)stackCopy.getItem()).armorType)).getHasStack()) // Inventory to armor
            { 
            	int j = S_ARMOR_START + ((ItemArmor)stackCopy.getItem()).armorType;
           
            	if (!this.mergeItemStack(stackOrig, j, j+1, false))
                {
                    return null;
                } 
            }
            else if (slotNumber >= S_MAIN_START && slotNumber <= S_MAIN_END) // main inv grid
            { 
            	//only from here are we doing the special items
            	
            	if(stackCopy.getItem() == Items.ender_pearl && 
            		(
        			p.inventory.getStackInSlot(Const.enderPearlSlot) == null || 
        			p.inventory.getStackInSlot(Const.enderPearlSlot).stackSize < Items.ender_pearl.getItemStackLimit(stackCopy))
        			)
        		{
            		 
            		if (!this.mergeItemStack(stackOrig, S_PEARL, S_PEARL+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Item.getItemFromBlock(Blocks.ender_chest) && 
            		(
        			p.inventory.getStackInSlot(Const.enderChestSlot) == null || 
        			p.inventory.getStackInSlot(Const.enderChestSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_ECHEST, S_ECHEST+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.compass && 
            		(
        			p.inventory.getStackInSlot(Const.compassSlot) == null || 
        			p.inventory.getStackInSlot(Const.compassSlot).stackSize < 1)
        			)
        		{ 
            		if (!this.mergeItemStack(stackOrig, S_COMPASS, S_COMPASS+1, false))
                	{ 
                        return null;
                    }  
        		}
            	else if(stackCopy.getItem() == Items.clock && 
            		(
        			p.inventory.getStackInSlot(Const.clockSlot) == null || 
        			p.inventory.getStackInSlot(Const.clockSlot).stackSize < 1)
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
            }
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
            else if (!this.mergeItemStack(stackOrig, 9, invo.getSlotsNotArmor() + 9, false)) // Full range
            {
                return null;
            }
            if (stackOrig.stackSize == 0)
            { 
                slot.putStack((ItemStack)null);
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
    }
}
