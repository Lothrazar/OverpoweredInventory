package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.Const; 
import com.lothrazar.powerinventory.InventoryPersistProperty;
import com.lothrazar.powerinventory.inventory.InventoryCustomPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
public class HotbarSwapPacket implements IMessage , IMessageHandler<HotbarSwapPacket, IMessage>
{
	public HotbarSwapPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public HotbarSwapPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(HotbarSwapPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		InventoryPersistProperty prop = InventoryPersistProperty.get(p);
		
		for(int bar = 0; bar < Const.HOTBAR_SIZE; bar++)
		{
			int second = bar +  InventoryCustomPlayer.INV_SIZE - Const.HOTBAR_SIZE;
			//System.out.println("bar:"+ bar+" -> "+second);
			
			ItemStack barStack = p.inventory.getStackInSlot(bar);
			ItemStack secondStack = prop.inventory.getStackInSlot(second);
		
			//the players real hotbar
			p.inventory.setInventorySlotContents(bar, secondStack);
			
			//that other invo 
			prop.inventory.setInventorySlotContents(second, barStack);
		}
 
		return null;
	}
}