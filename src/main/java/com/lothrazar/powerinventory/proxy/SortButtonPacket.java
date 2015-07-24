package com.lothrazar.powerinventory.proxy;

import java.util.LinkedList;
import java.util.Queue;
import com.lothrazar.powerinventory.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class SortButtonPacket implements IMessage , IMessageHandler<SortButtonPacket, IMessage>
{
	public SortButtonPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public SortButtonPacket(NBTTagCompound ptags)
	{
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.tags);
	}

	public static final String NBT_SORT = "sort";

	@Override
	public IMessage onMessage(SortButtonPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;
 
		InventoryPlayer invo = p.inventory;
		int sortType = message.tags.getInteger(NBT_SORT);
		
		switch(sortType)
		{
		case Const.SORT_LEFT:
			shiftLeftOne(invo);
			break;
		case Const.SORT_RIGHT:
			shiftRightOne(invo);
			break;
		case Const.SORT_LEFTALL:
			shiftLeftAll(invo);
			break;
		case Const.SORT_RIGHTALL:
			shiftRightAll(invo);
			break;
		}
	  
		return null;
	}
	
	private void shiftRightAll(InventoryPlayer invo)
	{
		Queue<Integer> empty = new LinkedList<Integer>();

		ItemStack item;
		
		for(int i = invo.getSizeInventory() - (Const.armorSize + 1); i >= Const.hotbarSize;i--)
		{
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				empty.add(i);
			}
			else
			{
				//find an empty spot for it
				if(empty.size() > 0 && empty.peek() > i)
				{
					//poll remove it since its not empty anymore
					moveFromTo(invo,i,empty.poll());
					empty.add(i);
				}
			}
		}
	}
	
	private void shiftLeftAll(InventoryPlayer invo)
	{
		Queue<Integer> empty = new LinkedList<Integer>();

		ItemStack item;
		
		for(int i = Const.hotbarSize; i < invo.getSizeInventory() - Const.armorSize;i++)
		{
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				empty.add(i);
			}
			else  //find an empty spot for it
			{
				if(empty.size() > 0 && empty.peek() < i)
				{
					//poll remove it since its not empty anymore
					moveFromTo(invo,i,empty.poll());
					empty.add(i);
				}
			}
		}
	}
	/**
	 * WARNING: it assumes that 'to' is already empty, and overwrites it.  sets 'from' to empty for you
	 * @param invo
	 * @param from
	 * @param to
	 */
	private static void moveFromTo(InventoryPlayer invo,int from, int to)
	{
		invo.setInventorySlotContents(to, invo.getStackInSlot(from));
		invo.setInventorySlotContents(from, null);
	}
	
	private void shiftRightOne(InventoryPlayer invo) 
	{
		int iEmpty = -1;
		ItemStack item = null;
		//0 to 8 is crafting
		//armor is 384-387
		for(int i = invo.getSizeInventory() - (Const.armorSize + 1); i >= Const.hotbarSize;i--)//388-4 384
		{
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				iEmpty = i;
			}
			else if(iEmpty > 0) //move i into iEmpty
			{
				moveFromTo(invo,i,iEmpty);
				
				iEmpty = i;					
			 
			}//else keep looking
		}
	}
	
	private void shiftLeftOne(InventoryPlayer invo) 
	{
		int iEmpty = -1;
		ItemStack item = null;

		for(int i = Const.hotbarSize; i < invo.getSizeInventory() - Const.armorSize;i++)
		{ 
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				iEmpty = i;
			}
			else if(iEmpty > 0)
			{ 
				moveFromTo(invo,i,iEmpty);
				
				iEmpty = i;		
			}
		}
	}
}
