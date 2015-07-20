package com.lothrazar.powerinventory.proxy;

import com.lothrazar.powerinventory.UtilInventory;

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
public class WithdrawButtonPacket implements IMessage , IMessageHandler<WithdrawButtonPacket, IMessage>
{
	public WithdrawButtonPacket() {}
	NBTTagCompound tags = new NBTTagCompound();
	public static final int ID = 4;
	
	public WithdrawButtonPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(WithdrawButtonPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		 if(p.openContainer != null)
		 {
			 //System.out.println("TODO: WITHDRAW FROM "+p.openContainer.getClass().getName());//net.minecraft.inventory.ContainerDispenser or whatever
		 
			 UtilInventory.moveallContainerToPlayer(p, p.openContainer);
			 p.inventoryContainer.detectAndSendChanges();
			 p.openContainer.detectAndSendChanges();
			 
		 }
		 
		return null;
	}
}
