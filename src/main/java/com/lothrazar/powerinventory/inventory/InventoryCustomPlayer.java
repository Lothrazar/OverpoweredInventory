package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

public class InventoryCustomPlayer implements IInventory
{
	// inventory blocks of 3x9 columns, and  hotbarsConst.HOTBAR_SIZE
	public static final int INV_SIZE = Const.COLS_VANILLA*Const.ROWS_VANILLA*4 + Const.HOTBAR_SIZE*2;
	ItemStack[] inventory = new ItemStack[INV_SIZE];
//thanks for http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571597-forge-1-6-4-1-8-custom-inventories-in-items-and
	private final String tagName = "opinvtags";
	private final String tagSlot = "Slot";
	
    private ItemStack enderPearlStack;
    private ItemStack enderChestStack;
    
	@Override
	public int getSizeInventory()
	{
		return INV_SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
        if(slot == Const.enderPearlSlot){return enderPearlStack;}
        if(slot == Const.enderChestSlot){return enderChestStack;} 
      
		return inventory[slot];
	}

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
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
    	else 
    	{
			int p_70298_1_ = index;
			int p_70298_2_ = count;
			
			ItemStack[] aitemstack = this.inventory;
			
			if (aitemstack[p_70298_1_] != null)
			{
			
			    if (aitemstack[p_70298_1_].stackSize <= p_70298_2_)
			    {
			        itemstack = aitemstack[p_70298_1_];
			        aitemstack[p_70298_1_] = null;
			        return itemstack;
			    }
			    else
			    {
			        itemstack = aitemstack[p_70298_1_].splitStack(p_70298_2_);
			
			        if (aitemstack[p_70298_1_].stackSize == 0)
			        {
			            aitemstack[p_70298_1_] = null;
			        }
			
			        return itemstack;
			    }
			}
			else
			{
			    return null;
			}
    	}
    }

	private void onInventoryChanged()
	{
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0)
				this.setInventorySlotContents(i, null);
		}
		
		this.markDirty();
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			setInventorySlotContents(slot, null);
		}

		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if(slot == Const.enderPearlSlot)
		{
			enderPearlStack = stack;  
		}
		else if(slot == Const.enderChestSlot)
		{
			enderChestStack = stack;  
		}
		else
			this.inventory[slot] = stack;
	
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public int getInventoryStackLimit()
	{

		return 64;
	}

	@Override
	public void markDirty()
	{
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{ 
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{ 
		return true;
	}

	public void writeToNBT(NBTTagCompound tags)
	{
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagCompound tagcompound;
	
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			if (this.getStackInSlot(i) != null)
			{
				tagcompound = new NBTTagCompound();
				tagcompound.setInteger(tagSlot,  i);
			
				this.getStackInSlot(i).writeToNBT(tagcompound);
			
				nbttaglist.appendTag(tagcompound);
			}
		}

        if(this.enderChestStack != null)
        {
        	tagcompound = new NBTTagCompound();
			tagcompound.setInteger(tagSlot,  Const.enderChestSlot);
       
            this.enderChestStack.writeToNBT(tagcompound);
            nbttaglist.appendTag(tagcompound);
        }
        if(this.enderPearlStack != null)
        {
        	tagcompound = new NBTTagCompound();
        	tagcompound.setInteger(tagSlot,  Const.enderPearlSlot);  
            this.enderPearlStack.writeToNBT(tagcompound);
            nbttaglist.appendTag(tagcompound);
        }
		tags.setTag(tagName, nbttaglist);
	}

	public void readFromNBT(NBTTagCompound tagcompound)
	{
		NBTTagList nbttaglist = tagcompound.getTagList(tagName,Constants.NBT.TAG_COMPOUND);
		ItemStack itemstack;
    	//System.out.println("READ  COUNT = "+nbttaglist.tagCount());
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound tags = nbttaglist.getCompoundTagAt(i);//tagAt
		
			int b = tags.getInteger(tagSlot);

        	//System.out.println("READ b = "+b+" and i = "+i);//what the -47 ??
			itemstack = ItemStack.loadItemStackFromNBT(tags);
			if (b >= 0 && b < this.getSizeInventory())
			{
				this.setInventorySlotContents(b, itemstack);
			}

			else if (itemstack != null)
            {
            	if(b == Const.enderPearlSlot)
            	{
            		enderPearlStack = itemstack;
            	}
            	if(b == Const.enderChestSlot)
                {
                	enderChestStack = itemstack;
                }
            }
		}
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}

	@Override
	public IChatComponent getDisplayName() 
	{
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player) 
	{
	}

	@Override
	public void closeInventory(EntityPlayer player) 
	{
	}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{
	}

	@Override
	public int getFieldCount() 
	{
		return 0;
	}

	@Override
	public void clear() 
	{
	}

	@Override
	public String getCommandSenderName()
	{
		return null;
	}
}
