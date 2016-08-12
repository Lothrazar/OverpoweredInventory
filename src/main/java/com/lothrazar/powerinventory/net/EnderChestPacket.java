package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.CapabilityRegistry;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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

    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(thePlayer);

		ItemStack chest = prop.inventory.getStackInSlot(Const.SLOT_ECHEST);

		if (chest != null)
			p.displayGUIChest(p.getInventoryEnderChest());
		else
      ModInv.addChatMessage(p,"slot.enderchest");
			//p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("slot.enderchest")));

		return null;
	}
}
