package com.lothrazar.powerinventory.net;

import java.util.ArrayList;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilInventory;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
 //
		ArrayList<IInventory> locations = UtilInventory.findTileEntityInventories(p,ModConfig.filterRange );
		
		for(IInventory inventory : locations)
		{
			UtilInventory.dumpFromPlayerToIInventory(p.worldObj, inventory, p);
		}
		
		return null; 
	}
	
	
}