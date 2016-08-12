package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.CapabilityRegistry;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound; 
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
	    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(p);

			prop.setEChestUnlocked(true);

			p.closeScreen();

      ModInv.playSound(p,  SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE);
		} else {

		  ModInv.addChatMessage(p,"gui.craftexp");
		}

		return null;
	}
}