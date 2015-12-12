package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class EnderPearlPacket implements IMessage, IMessageHandler<EnderPearlPacket, IMessage> {
	public EnderPearlPacket() {
	}

	NBTTagCompound tags = new NBTTagCompound();

	public EnderPearlPacket(NBTTagCompound ptags) {
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.tags);
	}

	@Override
	public IMessage onMessage(EnderPearlPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		PlayerPersistProperty prop = PlayerPersistProperty.get(p);

		ItemStack pearls = prop.inventory.getStackInSlot(Const.SLOT_EPEARL);

		if (pearls != null) {
			p.worldObj.spawnEntityInWorld(new EntityEnderPearl(p.worldObj, p));

			p.worldObj.playSoundAtEntity(p, "random.bow", 1.0F, 1.0F); 

			if (p.capabilities.isCreativeMode == false)
				prop.inventory.decrStackSize(Const.SLOT_EPEARL, 1);
		}

		return null;
	}
}
