package com.lothrazar.powerinventory.proxy;

import com.lothrazar.powerinventory.*;
import com.lothrazar.powerinventory.inventory.BigContainerPlayer;

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
public class DepositButtonPacket implements IMessage , IMessageHandler<DepositButtonPacket, IMessage>
{
	public DepositButtonPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public DepositButtonPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(DepositButtonPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		 if(p.openContainer != null)
		 { 
			 UtilInventory.moveallPlayerToContainer(p, p.openContainer);
			 p.inventoryContainer.detectAndSendChanges();
			 p.openContainer.detectAndSendChanges(); 
			 
			 p.inventory.markDirty();
			 /*
			 if( p.inventoryContainer instanceof BigContainerPlayer)
			 {
				 ((BigContainerPlayer)p.inventoryContainer ).updateScroll();
			 }*/
		 }
		 
		return null;
	}
}
