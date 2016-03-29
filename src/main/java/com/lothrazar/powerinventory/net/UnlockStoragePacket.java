package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;
import com.lothrazar.powerinventory.util.UtilSound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

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

		int numberPastFirst = (message.tags.getInteger("i") - 1);
		//first one adds zero increments, and more and more
		int expCost = ModConfig.expCostStorage_start + ModConfig.expCostStorage_inc * numberPastFirst;
		
		if (UtilExperience.getExpTotal(p) >= expCost) {

			PlayerPersistProperty prop = PlayerPersistProperty.get(p);

			UtilExperience.drainExp(p, expCost);

			prop.incrementStorage();// (true, message.tags.getInteger("i"));

			p.closeScreen();

			//p.worldObj.playSoundAtEntity(p, "mob.zombie.unfect", 1.4F, 1F);
			UtilSound.playSound(p, SoundEvents.entity_zombie_villager_converted, SoundCategory.PLAYERS);
		} else {

			p.addChatMessage(new TextComponentTranslation(I18n.translateToLocal("gui.craftexp")));
		}

		return null;
	}
}
