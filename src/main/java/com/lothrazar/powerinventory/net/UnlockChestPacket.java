package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class UnlockChestPacket implements IMessage, IMessageHandler<UnlockChestPacket, IMessage> {
	public UnlockChestPacket() {
	}

	NBTTagCompound tags = new NBTTagCompound();

	public UnlockChestPacket(NBTTagCompound ptags) {
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
	public IMessage onMessage(UnlockChestPacket message, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		if (UtilExperience.getExpTotal(p) >= ModConfig.expCostEChest) {

			UtilExperience.drainExp(p, ModConfig.expCostEChest);
			PlayerPersistProperty prop = PlayerPersistProperty.get(p);

			prop.setEChestUnlocked(true);

			p.closeScreen();

			p.worldObj.playSoundAtEntity(p, "mob.zombie.unfect", 1.4F, 1F);
		} else {

			p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("gui.craftexp")));
		}

		return null;
	}
}