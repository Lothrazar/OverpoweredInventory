package com.lothrazar.powerinventory.net;
import com.lothrazar.powerinventory.*;
import com.lothrazar.powerinventory.util.UtilPlayerInventoryFilestorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EnderPearlPacket implements IMessage, IMessageHandler<EnderPearlPacket, IMessage> {
  public EnderPearlPacket() {
  }
  NBTTagCompound tags = new NBTTagCompound();
  public EnderPearlPacket(NBTTagCompound ptags) {
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
  public IMessage onMessage(EnderPearlPacket message, MessageContext ctx) {
    EntityPlayer p = ctx.getServerHandler().playerEntity;
    ItemStack pearls = UtilPlayerInventoryFilestorage.getPlayerInventory(p).getStackInSlot(Const.SLOT_EPEARL);
    if (pearls != null) {
      World world = p.worldObj;
      EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, p);
      entityenderpearl.setHeadingFromThrower(p, p.rotationPitch, p.rotationYaw, 0.0F, 1.5F, 1.0F);
      world.spawnEntityInWorld(entityenderpearl);
      ModInv.playSound(p, SoundEvents.ENTITY_ARROW_SHOOT);
      if (p.capabilities.isCreativeMode == false) {
        UtilPlayerInventoryFilestorage.getPlayerInventory(p).decrStackSize(Const.SLOT_EPEARL, 1);
      }
    }
    return null;
  }
}
