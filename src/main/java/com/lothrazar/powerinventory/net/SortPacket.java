package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.util.UtilInventory;
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
public class SortPacket implements IMessage, IMessageHandler<SortPacket, IMessage> {
	public SortPacket() {
	}

	NBTTagCompound tags = new NBTTagCompound();

	public SortPacket(NBTTagCompound ptags) {
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

	// public static final String NBT_SORT = "sort";

	@Override
	public IMessage onMessage(SortPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		// int sortType = message.tags.getInteger(NBT_SORT);
		UtilInventory.doSort(p);

		return null;
	}

}