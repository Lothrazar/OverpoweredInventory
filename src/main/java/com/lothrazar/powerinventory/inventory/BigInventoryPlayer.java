package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const; 

import java.util.concurrent.Callable;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class BigInventoryPlayer extends InventoryPlayer
{
    @SideOnly(Side.CLIENT)
    private ItemStack currentItemStack;
    private ItemStack enderPearlStack;
    private ItemStack enderChestStack;
    private ItemStack clockStack;
    private ItemStack compassStack;
    private ItemStack bottleStack;
   
	public BigInventoryPlayer(EntityPlayer player)
	{
		super(player);
		this.mainInventory = new ItemStack[Const.invoSize + Const.hotbarSize];
 
		if(player.inventory != null)
		{
			ItemStack[] oldMain = player.inventory.mainInventory;
			ItemStack[] oldArmor = player.inventory.armorInventory;
			
			for(int i = 0; i < this.mainInventory.length && i < oldMain.length; i++)
			{
				this.mainInventory[i] = oldMain[i];
			}
			
			this.armorInventory = oldArmor;
		}
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
    {
        ItemStack[] aitemstack = this.mainInventory;
        //check these first, otherwise it crashes thinking they are armor
        if(index == Const.enderPearlSlot){return enderPearlStack;}
        if(index == Const.enderChestSlot){return enderChestStack;} 
        if(index == Const.clockSlot){return clockStack;}
        if(index == Const.compassSlot){return compassStack;} 
        if(index == Const.bottleSlot){return bottleStack;} 
        
        if (index >= aitemstack.length)
        {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }

        return aitemstack[index];
    }
	
	@Override
	public void dropAllItems()
	{
		super.dropAllItems();
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
    {
		if(this.player.capabilities.isCreativeMode && this.player.worldObj.isRemote)
		{
            Minecraft.getMinecraft().playerController.sendSlotPacket(stack, slot);
		}
		if(slot == Const.enderPearlSlot)
		{
			enderPearlStack = stack;  
		}
		else if(slot == Const.enderChestSlot)
		{
			enderChestStack = stack;  
		}
		else if(slot == Const.clockSlot)
		{
			clockStack = stack;  
		}
		else if(slot == Const.compassSlot)
		{
			compassStack = stack;  
		}
		else if(slot == Const.bottleSlot)
		{
			bottleStack = stack;  
		}
		else
		{
			super.setInventorySlotContents(slot, stack);
		}
    }
	
	public int getSlotsNotArmor()
	{  
		return this.getSizeInventory() - Const.armorSize; 
	}
	
    private int func_146029_c(Item stack)
    {
        for (int i = 0; i < this.getSlotsNotArmor(); ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == stack)
            {
                return i;
            }
        }

        return -1;
    }

    private int storeItemStack(ItemStack stack)
    {
        for (int i = 0; i < this.getSlotsNotArmor(); ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == stack.getItem() && this.mainInventory[i].isStackable() && this.mainInventory[i].stackSize < this.mainInventory[i].getMaxStackSize() && this.mainInventory[i].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[i].getHasSubtypes() || this.mainInventory[i].getItemDamage() == stack.getItemDamage()) && ItemStack.areItemStackTagsEqual(this.mainInventory[i], stack))
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getFirstEmptyStack()
    {
        for (int i = 0; i < this.getSlotsNotArmor(); ++i)
        {
            if (this.mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    @SideOnly(Side.CLIENT)
    private int func_146024_c(Item item, int meta)//getSlotNumberForItem
    {
        for (int j = 0; j < this.getSlotsNotArmor(); ++j)
        {
            if (this.mainInventory[j] != null && this.mainInventory[j].getItem() == item && this.mainInventory[j].getItemDamage() == meta)
            {
                return j;
            }
        }

        return -1;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void setCurrentItem(Item item, int meta, boolean var3flag, boolean var4flag)
    {
        this.currentItemStack = this.getCurrentItem();
        int k;

        if (var3flag)
        {
            k = this.func_146024_c(item, meta);
        }
        else
        {
            k = this.func_146029_c(item);
        }

        if (k >= 0 && k < 9)
        {
            this.currentItem = k;
        }
        else
        {
            if (var4flag && item != null)
            {
                int j = this.getFirstEmptyStack();

                if (j >= 0 && j < 9)
                {
                    this.currentItem = j;
                }

                this.func_70439_a(item, meta);
            }
        }
    }

    @SideOnly(Side.CLIENT)  // @Override
    public void func_70439_a(Item item, int meta)//set item in slot
    {
    
        if (item != null)
        { 
            if (this.currentItemStack != null && this.currentItemStack.isItemEnchantable() && this.func_146024_c(this.currentItemStack.getItem(), this.currentItemStack.getItemDamage()) == this.currentItem)
            {
                return;
            }

            int j = this.func_146024_c(item, meta);

            if (j >= 0)
            {
                int k = this.mainInventory[j].stackSize;
                this.mainInventory[j] = this.mainInventory[this.currentItem];
                this.mainInventory[this.currentItem] = new ItemStack(item, k, meta);
            }
            else
            {
                this.mainInventory[this.currentItem] = new ItemStack(item, 1, meta);
            }
        }
    }

    /**
     * This function stores as many items of an ItemStack as possible in a matching slot and returns the quantity of
     * left over items.
     */
    private int storePartialItemStack(ItemStack stack)
    {
        Item item = stack.getItem();
        int i = stack.stackSize;
        int j;

        if (stack.getMaxStackSize() == 1)
        {
            j = this.getFirstEmptyStack();

            if (j < 0)
            {
                return i;
            }
            else
            {
                if (this.mainInventory[j] == null)
                {
                    this.mainInventory[j] = ItemStack.copyItemStack(stack);
                }

                return 0;
            }
        }
        else
        {
            j = this.storeItemStack(stack);

            if (j < 0)
            {
                j = this.getFirstEmptyStack();
            }

            if (j < 0)
            {
                return i;
            }
            else
            {
                if (this.mainInventory[j] == null)
                {
                    this.mainInventory[j] = new ItemStack(item, 0, stack.getItemDamage());

                    if (stack.hasTagCompound())
                    {
                        this.mainInventory[j].setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                    }
                }

                int k = i;

                if (i > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize)
                {
                    k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
                }

                if (k > this.getInventoryStackLimit() - this.mainInventory[j].stackSize)
                {
                    k = this.getInventoryStackLimit() - this.mainInventory[j].stackSize;
                }

                if (k == 0)
                {
                    return i;
                }
                else
                {
                    i -= k;
                    this.mainInventory[j].stackSize += k;
                    this.mainInventory[j].animationsToGo = 5;
                    return i;
                }
            }
        }
    }

    /**
     * Adds the item stack to the inventory, returns false if it is impossible.
     */
    @SuppressWarnings("rawtypes")
	@Override
    public boolean addItemStackToInventory(final ItemStack stack)
    {
        if (stack != null && stack.stackSize != 0 && stack.getItem() != null)
        {
            try
            {
                int i;

                if (stack.isItemDamaged())
                {
                    i = this.getFirstEmptyStack();

                    if (i >= 0)
                    {
                        this.mainInventory[i] = ItemStack.copyItemStack(stack);
                        this.mainInventory[i].animationsToGo = 5;
                        stack.stackSize = 0;
                        return true;
                    }
                    else if (this.player.capabilities.isCreativeMode)
                    {
                        stack.stackSize = 0;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    do
                    {
                        i = stack.stackSize;
                        stack.stackSize = this.storePartialItemStack(stack);
                    }
                    while (stack.stackSize > 0 && stack.stackSize < i);

                    if (stack.stackSize == i && this.player.capabilities.isCreativeMode)
                    {
                        stack.stackSize = 0;
                        return true;
                    }
                    else
                    {
                        return stack.stackSize < i;
                    }
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(stack.getItem())));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(stack.getItemDamage()));
                crashreportcategory.addCrashSectionCallable("Item name", new Callable()
                {
                    public String call()
                    {
                        return stack.getDisplayName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Modified from the original to allow for more than 255 inventory slots
     */
    public NBTTagList writeToNBT(NBTTagList tags)
    {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger(Const.NBT_SLOT, i);
                this.mainInventory[i].writeToNBT(nbttagcompound);
                tags.appendTag(nbttagcompound);
            }
        }

        if(this.enderChestStack != null)
        {
        	nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger(Const.NBT_SLOT, Const.enderChestSlot);  
            this.enderChestStack.writeToNBT(nbttagcompound);
            tags.appendTag(nbttagcompound);
        }
        if(this.enderPearlStack != null)
        {
        	nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger(Const.NBT_SLOT, Const.enderPearlSlot);  
            this.enderPearlStack.writeToNBT(nbttagcompound);
            tags.appendTag(nbttagcompound);
        }
        if(this.clockStack != null)
        {
        	nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger(Const.NBT_SLOT, Const.clockSlot);  
            this.clockStack.writeToNBT(nbttagcompound);
            tags.appendTag(nbttagcompound);
        }
        if(this.compassStack != null)
        {
        	nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger(Const.NBT_SLOT, Const.compassSlot);  
            this.compassStack.writeToNBT(nbttagcompound);
            tags.appendTag(nbttagcompound);
        }
        if(this.bottleStack != null)
        {
        	nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger(Const.NBT_SLOT, Const.bottleSlot);  
            this.bottleStack.writeToNBT(nbttagcompound);
            tags.appendTag(nbttagcompound);
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger(Const.NBT_SLOT, i + (Integer.MAX_VALUE - 100)); // Give armor slots the last 100 integer spaces
                this.armorInventory[i].writeToNBT(nbttagcompound);
                tags.appendTag(nbttagcompound);
                
            }
        }
        
        return tags;
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
    	//without this, ender pearls cannot be taken out
        ItemStack itemstack;
//TODO: these ifelse brnaches are almost all identical. find a way to share code? make function?
    	if(index == Const.enderChestSlot)
    	{ 
            itemstack = this.enderChestStack;
            this.enderChestStack = null;
            return itemstack;
    	}    	
    	else if(index == Const.enderPearlSlot)
    	{
    		 if (this.enderPearlStack.stackSize <= count)
             {
                 itemstack = this.enderPearlStack;
                 this.enderPearlStack = null;
                 return itemstack;
             }
    		 else
             {
                 itemstack = this.enderPearlStack.splitStack(count);

                 if (this.enderPearlStack.stackSize == 0)
                 {
                	 this.enderPearlStack = null;
                 }

                 return itemstack;
             }
    	}	
    	else if(index == Const.clockSlot)
    	{
    		 if (this.clockStack.stackSize <= count)
             {
                 itemstack = this.clockStack;
                 this.clockStack = null;
                 return itemstack;
             }
    		 else
             {
                 itemstack = this.clockStack.splitStack(count);

                 if (this.clockStack.stackSize == 0)
                 {
                	 this.clockStack = null;
                 }

                 return itemstack;
             }
    	}
    	else if(index == Const.compassSlot)
    	{
    		 if (this.compassStack.stackSize <= count)
             {
                 itemstack = this.compassStack;
                 this.compassStack = null;
                 return itemstack;
             }
    		 else
             {
                 itemstack = this.compassStack.splitStack(count);

                 if (this.compassStack.stackSize == 0)
                 {
                	 this.compassStack = null;
                 }

                 return itemstack;
             }
    	}
    	else if(index == Const.bottleSlot)
    	{
    		 if (this.bottleStack.stackSize <= count)
             {
                 itemstack = this.bottleStack;
                 this.bottleStack = null;
                 return itemstack;
             }
    		 else
             {
                 itemstack = this.bottleStack.splitStack(count);

                 if (this.bottleStack.stackSize == 0)
                 {
                	 this.bottleStack = null;
                 }

                 return itemstack;
             }
    	}
    	else 
    		return super.decrStackSize(index, count);
    }

    /**
     * Modified from the original to allow for more than 255 inventory slots
     */
	@Override
    public void readFromNBT(NBTTagList tags)
    {
        this.mainInventory = new ItemStack[MathHelper.clamp_int(Const.invoSize, 27, Integer.MAX_VALUE - 100) + 9];
        this.armorInventory = new ItemStack[armorInventory == null? 4 : armorInventory.length]; // Just in case it isn't standard size
        
        for (int i = 0; i < tags.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = tags.getCompoundTagAt(i);
            int j = nbttagcompound.getInteger(Const.NBT_SLOT);
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
            	if(j == Const.enderPearlSlot)
            	{
            		enderPearlStack = itemstack;
            	}
            	if(j == Const.enderChestSlot)
                {
                	enderChestStack = itemstack;
                }
            	if(j == Const.clockSlot)
                {
                	clockStack = itemstack;
                }
            	if(j == Const.compassSlot)
                {
            		compassStack = itemstack;
                }
            	if(j == Const.bottleSlot)
                {
            		bottleStack = itemstack;
                }
                if (j >= 0 && j < this.mainInventory.length)
                {
            		this.mainInventory[j] = itemstack;
                }
                if (j >= (Integer.MAX_VALUE - 100) && j <= Integer.MAX_VALUE && j - (Integer.MAX_VALUE - 100) < this.armorInventory.length)
                {
            		this.armorInventory[j - (Integer.MAX_VALUE - 100)] = itemstack;
                }
            }
        }
    }
}
