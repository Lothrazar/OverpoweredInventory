package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;
import com.lothrazar.powerinventory.util.UtilSound;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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

			UtilSound.playSound(p, SoundEvents.entity_zombie_villager_converted, SoundCategory.PLAYERS);
		} else {

			p.addChatMessage(new TextComponentTranslation(net.minecraft.util.text.translation.I18n.translateToLocal("gui.craftexp")));
		}

		return null;
	}
}