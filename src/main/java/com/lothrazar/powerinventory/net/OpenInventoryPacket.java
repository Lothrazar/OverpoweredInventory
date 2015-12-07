package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.GuiHandler;
import com.lothrazar.powerinventory.ModInv;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenInventoryPacket implements IMessage, IMessageHandler<OpenInventoryPacket, IMessage> {
	public OpenInventoryPacket() {
	}

	NBTTagCompound tags = new NBTTagCompound();

	public OpenInventoryPacket(NBTTagCompound ptags) {
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
	public IMessage onMessage(OpenInventoryPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		p.openGui(ModInv.instance, GuiHandler.GUI_CUSTOM_INV, p.worldObj, (int) p.posX, (int) p.posY, (int) p.posZ);

		return null;
	}
}
