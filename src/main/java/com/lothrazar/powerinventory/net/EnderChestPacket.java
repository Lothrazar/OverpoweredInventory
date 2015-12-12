package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class EnderChestPacket implements IMessage, IMessageHandler<EnderChestPacket, IMessage> {
	NBTTagCompound tags = new NBTTagCompound();

	public EnderChestPacket() {
	}

	public EnderChestPacket(NBTTagCompound ptags) {
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
	public IMessage onMessage(EnderChestPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		PlayerPersistProperty prop = PlayerPersistProperty.get(p);

		ItemStack chest = prop.inventory.getStackInSlot(Const.SLOT_ECHEST);

		if (chest != null)
			p.displayGUIChest(p.getInventoryEnderChest());
		else
			p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("slot.enderchest")));

		return null;
	}
}
