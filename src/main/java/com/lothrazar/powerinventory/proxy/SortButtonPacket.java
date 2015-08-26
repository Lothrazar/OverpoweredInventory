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
			UtilInventory.shiftLeftOne(invo);
			break;
		case Const.SORT_RIGHT:
			UtilInventory.shiftRightOne(invo);
			break;
		case Const.SORT_LEFTALL:
			UtilInventory.shiftLeftAll(invo);
			break;
		case Const.SORT_RIGHTALL:
			UtilInventory.shiftRightAll(invo);
		case Const.SORT_SMART:
			System.out.println("new sort");
			
			
			UtilInventory.sort(invo);
			break;
		}
	  
		return null;
	}
	
}
