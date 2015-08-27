package com.lothrazar.powerinventory.proxy;

import java.util.LinkedList;
import java.util.Queue;
import com.lothrazar.powerinventory.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
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

		int sortType = message.tags.getInteger(NBT_SORT);
		UtilInventory.doSort( p,sortType);
 
		return null;
	}

	
}
