package com.lothrazar.powerinventory.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.ModSettings;
import com.lothrazar.powerinventory.inventory.client.GuiBigInventory;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class BigContainerPlayer extends ContainerPlayer
{
	private int craftSize = 3;//did not exist before, was magic'd as 2 everywhere
	public int scrollPos = 0;
	public BigInventoryPlayer invo;
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;
	/**
	 * A more organised version of 'inventorySlots' that doesn't include the hotbar
	 */
	Slot[] slots = new Slot[ModSettings.invoSize];
	Slot[] hotbar = new Slot[9];
	Slot[] crafting = new Slot[craftSize*craftSize];
	Slot result;
	
	@SuppressWarnings("unchecked")
	public BigContainerPlayer(BigInventoryPlayer playerInventory, boolean isLocal, EntityPlayer player)
	{
		super(playerInventory, isLocal, player);
        this.thePlayer = player;
		inventorySlots = Lists.newArrayList();//undo everything done by super()
		craftMatrix = new InventoryCrafting(this, craftSize, craftSize);

        boolean onHold = false;
        int[] holdSlot = new int[5];
        int[] holdX = new int[5];
        int[] holdY = new int[5];
        int h = 0;


		int shiftxOut = 8;//was 9
        int shiftyOut = 6; 
        int shiftx = -7;
        int shifty = 0;
        //turn off all the shifts, if we are staying wtih a 2x2 version
        if(this.craftSize == 2)
        {
        	shiftxOut = 0;
        	shiftyOut = 0;
        	shiftx = 0;
        	shifty = 0;
        }
        

        int slotNumber = 0;//the ID for the inventory slot
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, slotNumber, 144+shiftxOut, 36+shiftyOut));
       
        result = (Slot)this.inventorySlots.get(0);
		//result.xDisplayPosition = 144 +shiftxOut-1;
		//result.yDisplayPosition = 36 +shiftyOut; //TODO: fix these numbers
        
        
        int i,j,cx,cy;

        for (i = 0; i < craftSize; ++i)
        {
        	onHold = false;
        	if( i == this.craftSize-1) onHold = true; //hold right and bottom column
        	
            for (j = 0; j < craftSize; ++j)
            {
            	if(j == this.craftSize-1)onHold = true; //hold right and bottom column
            	
            	slotNumber = j + i * this.craftSize;
                //System.out.println("crafting = "+slotNumber);
            
            	cx = 88 + j * GuiBigInventory.square + shiftx;
            	cy = 26 + i * GuiBigInventory.square + shifty;
            	if(this.craftSize == 3 && onHold)
            	{
            		//save these to add at the end
            		//System.out.println("on hold "+slotNumber);
            		holdSlot[h] = slotNumber;
            		holdX[h] = cx;
            		holdY[h] = cy;
            		h++;
            	}
            	else
            		this.addSlotToContainer(new Slot(this.craftMatrix, slotNumber, cx , cy));
 
            }
        }

        for (i = 0; i < 4; ++i)
        {
        	cx = 8;
        	cy = 8 + i * GuiBigInventory.square;
            final int k = i;
            slotNumber =  playerInventory.getSizeInventory() - 1 - i;
            //used to be its own class SlotArmor
           // System.out.println("armor = "+slotNumber);
            this.addSlotToContainer(new Slot(playerInventory, slotNumber, cx, cy)
            {
             //   private static final String __OBFID = "CL_00001755";
                /**
                 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
                 * in the case of armor slots)
                 */
                public int getSlotStackLimit()
                {
                    return 1;
                }
                /**
                 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
                 */
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

        for (i = 0; i < 3; ++i)      //inventory is 3 rows by 9 columns
        {
            for (j = 0; j < 9; ++j)
            {
            	slotNumber = j + (i + 1) * 9;
               // System.out.println("plain invo = "+slotNumber);
            	cx = 8 + j * GuiBigInventory.square;
            	cy = 84 + i * GuiBigInventory.square;
                this.addSlotToContainer(new Slot(playerInventory, slotNumber, cx, cy));
            }
        }

        for (i = 0; i < 9; ++i)
        {
        	slotNumber = i;
        	cx = 8 + i * GuiBigInventory.square;
        	cy = 142 + (GuiBigInventory.square * ModSettings.MORE_ROWS);
           // System.out.println("hotbar = "+slotNumber);
            this.addSlotToContainer(new Slot(playerInventory, slotNumber, cx, cy));
        }
        
        for( i = 1; i < 5; i++)
		{
			crafting[i - 1] = (Slot)this.inventorySlots.get(i);
		}
		int samup = 23;
		int samleft = 7;
		for( i = 0; i < 4; i++)
		{
			Slot hs = crafting[i];
			hs.xDisplayPosition = 88 + ((i%2) * 18)-samleft;
			hs.yDisplayPosition = 43 + ((i/2) * 18)-samup;
		}

        this.onCraftMatrixChanged(this.craftMatrix);
		this.invo = (BigInventoryPlayer)playerInventory;
		
		for(i = 9; i < 36; i++)
		{
			// Add all the previous inventory slots to the organised array
			 Slot os = (Slot)this.inventorySlots.get(i);
			 
			 Slot ns = new Slot(os.inventory, os.getSlotIndex(), os.xDisplayPosition, os.yDisplayPosition);
			 ns.slotNumber = os.slotNumber;
			 this.inventorySlots.set(i, ns);
			 ns.onSlotChanged();
			 slots[i - 9] = ns;
		}
		
		for( i = 36; i < 45; i++)
		{
			// Get the hotbar for repositioning
			hotbar[i - 36] = (Slot)this.inventorySlots.get(i);
		}
		
		
		/*
		for( i = 0; i < 9; i++)
		{
			Slot hs = hotbar[i];
			hs.xDisplayPosition = 8 + (i * 18);
			hs.yDisplayPosition = 142 + (18 * ModSettings.MORE_ROWS);
		}*/

        for ( i = 3; i < MathHelper.ceiling_float_int((float)ModSettings.invoSize/9F); ++i)
        {
            for ( j = 0; j < 9; ++j)
            {
            	if(j + (i * 9) >= ModSettings.invoSize && ModSettings.invoSize > 27)
            	{
            		break;
            	} else
            	{
            		// Moved off screen to avoid interaction until screen scrolls over the row
            		slotNumber =  j + (i + 1) * 9;
            		cx = -999;
            		cy = -999;
                  //  System.out.println("new slots = "+slotNumber);
            		Slot ns = new Slot(playerInventory,slotNumber, cx,cy);
            		slots[slotNumber - 9] = ns;
            		this.addSlotToContainer(ns);
            	}
            }
        }
        if(craftSize == 3)// Finally, add the five new slots to the 3x3 crafting grid (they end up being 45-49 inclusive)
        {
	        for(h = 0; h < 5; ++h)
	        {
	        	samup=6;
	        	slotNumber = holdSlot[h];
	    		cx = holdX[h];
	    		cy = holdY[h]-samup;
	    		Slot ns = new Slot(this.craftMatrix, slotNumber, cx , cy );
	        	this.addSlotToContainer(ns);
	          //	System.out.println(" -from hold"+slotNumber+","+cx+","+cy+";");
	        	crafting[3+h] = ns;
	        }
        }
     
        this.updateScroll();
	}
	
	@Override
	public Slot getSlotFromInventory(IInventory invo, int id)
	{
		Slot slot = super.getSlotFromInventory(invo, id);
		if(slot == null)
		{
			Exception e = new NullPointerException();
			 
			ModInv.logger.log(Level.FATAL, e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + ":" + e.getStackTrace()[1].getLineNumber() + " is requesting slot " + id + " from inventory " + invo.getName() + " (" + invo.getClass().getName() + ") and got NULL!", e);
		}
		return slot;
	}
	
	
	@Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
//from  https://github.com/PrinceOfAmber/SamsPowerups
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

	public void updateScroll()
	{
		if(scrollPos > MathHelper.ceiling_float_int((float)ModSettings.invoSize/(float)(9 + ModSettings.MORE_COLS)) - (3 + ModSettings.MORE_ROWS))
		{
			scrollPos = MathHelper.ceiling_float_int((float)ModSettings.invoSize/(float)(9 + ModSettings.MORE_COLS)) - (3 + ModSettings.MORE_ROWS);
		}
		
		if(scrollPos < 0)
		{
			scrollPos = 0;
		}
		
		for(int i = 0; i < MathHelper.ceiling_float_int((float)ModSettings.invoSize/(float)(9 + ModSettings.MORE_COLS)); i++)
		{
            for (int j = 0; j < 9 + ModSettings.MORE_COLS; ++j)
            {
            	int index = j + (i * (9 + ModSettings.MORE_COLS));
            	//System.out.println("updateScroll from "+scrollPos+"::"+index);
            	if(index >= ModSettings.invoSize && index >= 27)
            	{
            		break;
            	} else
            	{
            		if(i >= scrollPos && i < scrollPos + 3 + ModSettings.MORE_ROWS && index < invo.getUnlockedSlots() - 9 && index < ModSettings.invoSize)
            		{
            			Slot s = slots[index];
            			s.xDisplayPosition = 8 + j * 18;
            			s.yDisplayPosition = 84 + (i - scrollPos) * 18;
            		} else
            		{
            			Slot s = slots[index];
            			s.xDisplayPosition = -999;
            			s.yDisplayPosition = -999;
            		}
            	}
            }
		}
	}

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
		int vLocked = invo.getUnlockedSlots() < 36? 36 - invo.getUnlockedSlots() : 0;
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ == 0) // Crafting result
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (p_82846_2_ >= 1 && p_82846_2_ < 5) // Crafting grid
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 5 && p_82846_2_ < 9) // Armor
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack()) // Inventory to armor
            {
                int j = 5 + ((ItemArmor)itemstack.getItem()).armorType;

                if (!this.mergeItemStack(itemstack1, j, j + 1, false))
                {
                    return null;
                }
            }
            else if ((p_82846_2_ >= 9 && p_82846_2_ < 36) || (p_82846_2_ >= 45 && p_82846_2_ < invo.getUnlockedSlots() + 9))
            {
                if (!this.mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 36 && p_82846_2_ < 45) // Hotbar
            {
                if (!this.mergeItemStack(itemstack1, 9, 36 - vLocked, false) && (invo.getUnlockedSlots() - 36 <= 0 || !this.mergeItemStack(itemstack1, 45, 45 + (invo.getUnlockedSlots() - 36), false)))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 9, invo.getUnlockedSlots() + 9, false)) // Full range
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }
}
