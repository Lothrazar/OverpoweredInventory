package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class UnlockStoragePacket implements IMessage, IMessageHandler<UnlockStoragePacket, IMessage> {
	NBTTagCompound tags = new NBTTagCompound();

	public UnlockStoragePacket() {
	}

	public UnlockStoragePacket(NBTTagCompound ptags) {
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
	public IMessage onMessage(UnlockStoragePacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		if (UtilExperience.getExpTotal(p) >= ModConfig.expCostStorage) {

			PlayerPersistProperty prop = PlayerPersistProperty.get(p);

			UtilExperience.drainExp(p, ModConfig.expCostStorage);

			prop.incrementStorage();// (true, message.tags.getInteger("i"));

			p.closeScreen();

			p.worldObj.playSoundAtEntity(p, "mob.zombie.unfect", 1.4F, 1F);
		} else {

			p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("gui.craftexp")));
		}

		return null;
	}
}
