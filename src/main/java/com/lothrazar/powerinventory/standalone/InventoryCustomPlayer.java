package com.lothrazar.powerinventory.standalone;

import com.lothrazar.powerinventory.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class InventoryCustomPlayer implements IInventory
{
	public static final int INV_SIZE = 36;
	ItemStack[] inventory = new ItemStack[INV_SIZE];
//thanks for http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571597-forge-1-6-4-1-8-custom-inventories-in-items-and
	private final String tagName = "opinvtags";
	private final String tagSlot = "Slot";
	

    private ItemStack enderPearlStack;
    private ItemStack enderChestStack;
    private ItemStack clockStack;
    private ItemStack compassStack;
    private ItemStack bottleStack;
    private ItemStack uncraftStack;
    
	@Override
	public int getSizeInventory()
	{

		return INV_SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
        if(slot == Const.bottleSlot){return bottleStack;} 
        if(slot == Const.uncraftSlot){return uncraftStack;} 
        if(slot == Const.enderPearlSlot){return enderPearlStack;}
        if(slot == Const.enderChestSlot){return enderChestStack;} 
        if(slot == Const.clockSlot){return clockStack;}
        if(slot == Const.compassSlot){return compassStack;} 
        /*
        if (index >= aitemstack.length)
        {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }
        if(index>=aitemstack.length){return null;}//TODO: is this only from swapping configsizes???
*/
        
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
		{
			if (stack.stackSize > amount)
			{
				stack = stack.splitStack(amount);
			}
			if (stack.stackSize == 0)
			{
				setInventorySlotContents(slot, null);
			}
		}
		else
		{
			setInventorySlotContents(slot, null);
		}

		this.onInventoryChanged();
		return stack;
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
		else if(slot == Const.uncraftSlot)
		{
			this.uncraftStack = stack;  
		}
		else
			this.inventory[slot] = stack;
// 
		
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInventoryName()
	{

		return "opinv.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{

		
		return false;
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
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{ 
		return true;
	}

	public void writeToNBT(NBTTagCompound tagcompound)
	{
		NBTTagList nbttaglist = new NBTTagList();
	
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			if (this.getStackInSlot(i) != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			
				nbttagcompound1.setByte(tagSlot, (byte) i);
			
				this.getStackInSlot(i).writeToNBT(nbttagcompound1);
			
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
	
		tagcompound.setTag(tagName, nbttaglist);

	}

	public void readFromNBT(NBTTagCompound tagcompound)
	{

		NBTTagList nbttaglist = tagcompound.getTagList(tagName,Constants.NBT.TAG_COMPOUND);
	
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound tags = nbttaglist.getCompoundTagAt(i);//tagAt
		
			byte b = tags.getByte(tagSlot);
		
			if (b >= 0 && b < this.getSizeInventory())
			{
				this.setInventorySlotContents(b, ItemStack.loadItemStackFromNBT(tags));
			}
		}
	}
}
