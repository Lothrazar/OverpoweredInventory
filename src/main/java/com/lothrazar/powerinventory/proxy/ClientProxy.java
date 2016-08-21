package com.lothrazar.powerinventory.proxy;
import org.lwjgl.input.Keyboard;
import com.lothrazar.powerinventory.CapabilityRegistry;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
  public static KeyBinding keyEnderpearl;
  public static KeyBinding keyEnderchest;
  public static KeyBinding keyHotbar;
  public static KeyBinding keyInventory;
  public static final String keyCategory = "key.categories.inventory";
  @Override
  public boolean isClient() {
    return true;
  }
  @Override
  public World getClientWorld() {
    return FMLClientHandler.instance().getClient().theWorld;
  }
  @Override
  public void registerHandlers() {
    super.registerHandlers();
    keyEnderpearl = new KeyBinding("key.enderpearl", Keyboard.KEY_Z, keyCategory);
    ClientRegistry.registerKeyBinding(ClientProxy.keyEnderpearl);
    keyEnderchest = new KeyBinding("key.enderchest", Keyboard.KEY_I, keyCategory);
    ClientRegistry.registerKeyBinding(ClientProxy.keyEnderchest);
    keyHotbar = new KeyBinding("key.hotbar", Keyboard.KEY_H, keyCategory);
    ClientRegistry.registerKeyBinding(ClientProxy.keyHotbar);
    keyInventory = new KeyBinding("key.opinventory", Keyboard.KEY_B, keyCategory);
    ClientRegistry.registerKeyBinding(ClientProxy.keyInventory);
  }
  //https://github.com/coolAlias/Tutorial-Demo/blob/e8fa9c94949e0b1659dc0a711674074f8752d80e/src/main/java/tutorial/ClientProxy.java
  @Override
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
  }
  @Override
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
    // your packets will not work as expected because you will be getting a
    // client player even when you are on the server!
    // Sounds absurd, but it's true.
    //https://github.com/coolAlias/Tutorial-Demo/blob/e8fa9c94949e0b1659dc0a711674074f8752d80e/src/main/java/tutorial/ClientProxy.java
    // Solution is to double-check side before returning the player:
    return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
  }
  @SideOnly(Side.CLIENT)
  public void setClientPlayerData(MessageContext ctx, NBTTagCompound tags) {
    EntityPlayer p = this.getPlayerEntity(ctx); //Minecraft.getMinecraft().thePlayer;
    if (p != null) {
      IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(Minecraft.getMinecraft().thePlayer);
      if (props != null) {
        props.setDataFromNBT(tags);
      }
    }
  }
}
