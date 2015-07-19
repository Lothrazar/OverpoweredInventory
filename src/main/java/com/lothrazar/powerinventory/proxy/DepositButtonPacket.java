package com.lothrazar.powerinventory.proxy;

import java.util.ArrayList;

import com.lothrazar.powerinventory.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
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
	public static final int ID = 0;
	
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

		 System.out.println("Put into open container");
		 
		 if(p.openContainer != null)	 System.out.println(p.openContainer.getClass().getName());
		 
		// p.openContainer.canMergeSlot(p_94530_1_, p_94530_2_)
		
		return null;
	
	}
}
