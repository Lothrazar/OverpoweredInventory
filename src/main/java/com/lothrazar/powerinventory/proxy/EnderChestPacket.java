package com.lothrazar.powerinventory.proxy;

import com.lothrazar.powerinventory.Const;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class EnderChestPacket implements IMessage , IMessageHandler<EnderChestPacket, IMessage>
{
	public EnderChestPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public EnderChestPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(EnderChestPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;
		
		if( p.inventory.getStackInSlot(Const.enderChestSlot) != null)
			p.displayGUIChest(p.getInventoryEnderChest());
		/*
		int invType = message.tags.getInteger("i");

		switch(invType)
		{
		case Const.INV_ENDER:
			p.displayGUIChest(p.getInventoryEnderChest());
		break;
		case Const.INV_PLAYER:

			//this packet should not have been sent. but keep empty branch so i remember it
			break;
		}*/

		return null;
	}
}
