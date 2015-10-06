package com.lothrazar.powerinventory.proxy;

import java.util.ArrayList;

import com.lothrazar.powerinventory.*;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
//import net.minecraft.util.BlockPos;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class DumpButtonPacket implements IMessage , IMessageHandler<DumpButtonPacket, IMessage>
{
	public DumpButtonPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public DumpButtonPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(DumpButtonPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		ArrayList<IInventory> locations = UtilInventory.findTileEntityInventories(p, ModConfig.filterRange);
		

		for(IInventory pos : locations)
		{
 
				//merge first then dump
				//UtilInventory.sortFromPlayerToChestEntity(p.worldObj, (TileEntityChest)p.worldObj.getTileEntity(pos), p);
			UtilInventory.dumpFromPlayerToIInventory(p.worldObj, pos, p);
	 
		}
		
		return null; 
	}
}
