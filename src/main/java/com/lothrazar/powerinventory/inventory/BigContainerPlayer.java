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
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.inventory.client.GuiBigInventory;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class BigContainerPlayer extends ContainerPlayer
{	
	private final int craftSize = 3;//did not exist before, was magic'd as 2 everywhere
    private final EntityPlayer thePlayer;
 
	public BigInventoryPlayer invo;
    public boolean isLocalWorld;

	//these get used here for actual slot, and in GUI for texture
	public final int pearlX = GuiBigInventory.texture_width - Const.square-6; //we used padding six on gui
	public final int pearlY = GuiBigInventory.texture_height - Const.square-6; 
	public final int echestX = pearlX - 2*Const.square;
	public final int echestY = pearlY;
//store slot numbers as we go. so that transferStack.. is actually readable
	
	static int S_RESULT;
	static int S_CRAFT_START;
	static int S_CRAFT_END;
	static int S_ARMOR_START;
	static int S_ARMOR_END;
	static int S_BAR_START;
	static int S_BAR_END;
	static int S_MAIN_START;
	static int S_MAIN_END;
	public BigContainerPlayer(BigInventoryPlayer playerInventory, boolean isLocal, EntityPlayer player)
	{
		super(playerInventory, isLocal, player);
		
        this.thePlayer = player;
		inventorySlots = Lists.newArrayList();//undo everything done by super()
		craftMatrix = new InventoryCrafting(this, craftSize, craftSize);
 
        int i,j,cx,cy;//rows and cols of vanilla, not extra
   
        
        S_RESULT = this.inventorySlots.size();
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 
        		174, 
        		40));

        S_CRAFT_START = this.inventorySlots.size();
        for (i = 0; i < craftSize; ++i)
        { 
            for (j = 0; j < craftSize; ++j)
            {  
    			cx = 88 + j * Const.square ;
    			cy = 20 + i * Const.square ;

        		this.addSlotToContainer(new Slot(this.craftMatrix, j + i * this.craftSize, cx , cy)); 
            }
        }
        S_CRAFT_END = this.inventorySlots.size() - 1;
        S_ARMOR_START = this.inventorySlots.size();
        for (i = 0; i < Const.armorSize; ++i)
        {
        	cx = 8;
        	cy = 8 + i * Const.square;
            final int k = i;
         
            this.addSlotToContainer(new Slot(playerInventory,  playerInventory.getSizeInventory() - 1 - i, 
            		cx, cy)
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
        S_BAR_START = this.inventorySlots.size();
        for (i = 0; i < Const.hotbarSize; ++i)
        { 
        	cx = 8 + i * Const.square;
        	cy = 142 + (Const.square * Const.MORE_ROWS);
 
            this.addSlotToContainer(new Slot(playerInventory, i, cx, cy));
        }
        S_BAR_END = this.inventorySlots.size() - 1;
        S_MAIN_START = this.inventorySlots.size();
        int sn = Const.hotbarSize;
        for( i = 0; i < MathHelper.ceiling_float_int((float)Const.invoSize/(float)(Const.ALL_COLS)); i++)
		{
            for ( j = 0; j < Const.ALL_COLS; ++j)
            { 
            	sn++;
            	cx = 8 + j * Const.square;
            	cy = 84 + i * Const.square;
                this.addSlotToContainer(new Slot(playerInventory, sn, cx, cy));
            }
        }
        S_MAIN_END = this.inventorySlots.size() - 1;
        this.addSlotToContainer(new SlotEnderPearl(playerInventory, Const.enderPearlSlot, pearlX, pearlY));
        this.addSlotToContainer(new SlotEnderChest(playerInventory, Const.enderChestSlot, echestX, echestY)); 
        
        this.onCraftMatrixChanged(this.craftMatrix);
		this.invo = (BigInventoryPlayer)playerInventory;
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

       // if(playerIn.capabilities.isCreativeMode == false) //i think we were dropping stuff from hotbar?
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
		//bugs found:
		System.out.println(slotNumber);
		//-from hotbar/inventory sends to 22, not top left (9)
		//- shift clicking out of armor sends it to crafting lwer right
		//out of crafting result (0) goes to slot 30 instead of top left (9)
        ItemStack stackCopy = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNumber);
//int realSlot = craft - 14;//the magic number;9+4+1
//System.out.println(realSlot);
if(p.worldObj.isRemote ){return null;}//ignore clientside..stops double span on System, but probably remove for production
        if (slot != null && slot.getHasStack())
        {
            ItemStack stackOrig = slot.getStack();
            stackCopy = stackOrig.copy();
            if (slotNumber == this.S_RESULT) // Crafting result
            {
            	System.out.println("result ");
            	//does not dup, but does not start in top left, goes to top mid
                if (!this.mergeItemStack(stackOrig, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(stackOrig, stackCopy);
            }
            else if (slotNumber >= S_CRAFT_START && slotNumber <= S_CRAFT_END) // Crafting grid
            {
            	System.out.println("?crafting");///goes to right plac,e but it DUPS
            	//WELL IT only DUPLICATES from lower right - the 9
                if (!this.mergeItemStack(stackOrig, 10, 45, false))//was 9,45
                {
                    return null;
                }
            }
            else if (slotNumber >= S_ARMOR_START && slotNumber <= S_ARMOR_END) // Armor
            {
            	System.out.println("?fromarmor");
                if (!this.mergeItemStack(stackOrig, 10, 45, false))//if 9, it goes to craft grid
                {
                    return null;
                }
            }
            else if (stackCopy.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)stackCopy.getItem()).armorType)).getHasStack()) // Inventory to armor
            {
            	System.out.println("?toarmor "+S_ARMOR_START+" "+S_ARMOR_END);//goes to craft grid- so it is a bug
            	
            	int OLDLOGIC = 5 + ((ItemArmor)stackCopy.getItem()).armorType;
            	System.out.println("OLDLOGIC  "+OLDLOGIC); 
            	int TEST = S_CRAFT_END + ((ItemArmor)stackCopy.getItem()).armorType;
            	System.out.println("TEST  "+TEST); 
            	
            	if (!this.mergeItemStack(stackOrig, S_ARMOR_START, S_ARMOR_END, false))
                {
                    return null;
                } 
            	/*
                int j = 5 + ((ItemArmor)stackCopy.getItem()).armorType;

                if (!this.mergeItemStack(stackOrig, j, j + 1, false))
                {
                    return null;
                }*/
            }
            else if (slotNumber >= S_MAIN_START && slotNumber < S_MAIN_END) // Hotbar
            {
            	System.out.println("MAIN");//does not duplciate, but doesnt work right, sends to craft grid
               // if (!this.mergeItemStack(stackOrig, 9, 36, false) && (invo.getSlotsNotArmor() - 36 <= 0 || !this.mergeItemStack(stackOrig, 45, 45 + (invo.getSlotsNotArmor() - 36), false)))

            	if (!this.mergeItemStack(stackOrig, S_BAR_START, S_BAR_END, false)//try the hotbar, and if that doesnt work
            		//	|| !this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false)
            			)
            	{
                    return null;
                }
            }
            /*
            else if ((craft >= 9 && craft < 36) || (craft >= 45 && craft < invo.getSlotsNotArmor() + 9))
            {
            	System.out.println("?9 36 45+ weird");//does not duplicate,b but always goes to 22 or something in
            	//top mid
            	//also, the hotbar slots hit this code
                if (!this.mergeItemStack(stackOrig, 36, 45, false))
                {
                    return null;
                }
            }*/
            else if (slotNumber >= S_BAR_START && slotNumber <= S_BAR_END) // Hotbar
            {
            	System.out.println("?hotbar");//does not duplciate, but doesnt work right, sends to craft grid
               // if (!this.mergeItemStack(stackOrig, 9, 36, false) && (invo.getSlotsNotArmor() - 36 <= 0 || !this.mergeItemStack(stackOrig, 45, 45 + (invo.getSlotsNotArmor() - 36), false)))

            	if (!this.mergeItemStack(stackOrig, S_MAIN_START, S_MAIN_END, false))
            	{
                    return null;
                }
            }
            else if (!this.mergeItemStack(stackOrig, 9, invo.getSlotsNotArmor() + 9, false)) // Full range
            {
            	System.out.println("?Full range//DEFAULT");
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
