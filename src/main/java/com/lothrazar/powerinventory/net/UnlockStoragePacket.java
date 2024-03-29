package com.lothrazar.powerinventory.net;
import com.lothrazar.powerinventory.CapabilityRegistry;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;

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
    EntityPlayer p = ctx.getServerHandler().player;
    int numberPastFirst = (message.tags.getInteger("i") - 1);
    //first one adds zero increments, and more and more
    int expCost = ModConfig.expCostStorage_start + ModConfig.expCostStorage_inc * numberPastFirst;
    if (UtilExperience.getExpTotal(p) >= expCost) {
      IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(p);
      UtilExperience.drainExp(p, expCost);
      prop.setStorageCount(prop.getStorageCount() + 1);// (true, message.tags.getInteger("i"));
      CapabilityRegistry.syncServerDataToClient(ctx.getServerHandler().player);
      p.closeScreen();
      ModInv.playSound(p, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE);
    }
    else {
      ModInv.addChatMessage(p, "gui.craftexp");
    }
    return null;
  }
}
