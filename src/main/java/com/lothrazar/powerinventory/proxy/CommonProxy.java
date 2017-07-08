package com.lothrazar.powerinventory.proxy;
import com.lothrazar.powerinventory.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
  public boolean isClient() {
    return false;
  }
  public void registerHandlers() {
    EventHandler handler = new EventHandler();
    MinecraftForge.EVENT_BUS.register(handler);
    MinecraftForge.EVENT_BUS.register(new EventExtendedInventory());
  }
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return ctx.getServerHandler().player.getServer();
  }
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return ctx.getServerHandler().player;
  }
  public void setClientPlayerData(MessageContext ctx, NBTTagCompound tags) {
    //client side only
  }
  public World getClientWorld() {
    // TODO Auto-generated method stub
    return null;
  }
}
