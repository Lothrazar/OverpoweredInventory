package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.InventoryPersistProperty;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
public class UnlockCraftPacket implements IMessage , IMessageHandler<UnlockCraftPacket, IMessage>
{
	public UnlockCraftPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public UnlockCraftPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(UnlockCraftPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		InventoryPersistProperty prop = InventoryPersistProperty.get(p);
		
		//TESTING ONLY: TODO drain exp or some other cost
		prop.setInvoCrafting( true  );
		
		p.closeScreen();
		
		return null;
	}
}