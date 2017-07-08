package com.lothrazar.powerinventory.net;
import com.lothrazar.powerinventory.util.UtilInventory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SwapInvoPacket implements IMessage, IMessageHandler<SwapInvoPacket, IMessage> {
  NBTTagCompound tags = new NBTTagCompound();
  public SwapInvoPacket() {
  }
  public SwapInvoPacket(NBTTagCompound ptags) {
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
  public IMessage onMessage(SwapInvoPacket message, MessageContext ctx) {
    EntityPlayer p = ctx.getServerHandler().player;
    int invoGroup = message.tags.getInteger("i");
    UtilInventory.swapInventoryGroup(p, invoGroup);
    return null;
  }
}
