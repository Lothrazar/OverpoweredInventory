package com.lothrazar.powerinventory.net;
 
import com.lothrazar.powerinventory.InventoryPersistProperty;
import com.lothrazar.powerinventory.UtilInventory;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class MessageRotateInv implements IMessage, IMessageHandler<MessageRotateInv, IMessage>
{
	NBTTagCompound tags = new NBTTagCompound(); 
	public MessageRotateInv()	{ 	}
	public MessageRotateInv(NBTTagCompound ptags)	
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
	
	@Override
	public IMessage onMessage(MessageRotateInv message, MessageContext ctx)
	{  
		EntityPlayer p = ctx.getServerHandler().playerEntity; 
		
		InventoryPersistProperty prop = InventoryPersistProperty.get(p);
		
		IInventory invo = prop.inventory;

		//top left is 9-35
		
		//top right is 36 - 62
		
		
		
		
		return null;
	}
}
 
