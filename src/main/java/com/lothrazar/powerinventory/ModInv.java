package com.lothrazar.powerinventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import com.lothrazar.powerinventory.proxy.CommonProxy;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.net.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Initially Forked and altered by
 *         https://github.com/PrinceOfAmber/InfiniteInvo before later being
 *         merged into my main project
 */
@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/LothrazarMinecraftMods/OverpoweredInventory/master-18/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigHandler")
public class ModInv {
	@Instance(Const.MODID)
	public static ModInv instance;

	@SidedProxy(clientSide = "com.lothrazar.powerinventory.proxy.ClientProxy", serverSide = "com.lothrazar.powerinventory.proxy.CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network;
	public static Logger logger;

  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);

		ModConfig.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));

		int packetID = 0;
		network.registerMessage(OpenInventoryPacket.class, OpenInventoryPacket.class, packetID++, Side.SERVER);
		network.registerMessage(EnderPearlPacket.class, EnderPearlPacket.class, packetID++, Side.SERVER);
		network.registerMessage(EnderChestPacket.class, EnderChestPacket.class, packetID++, Side.SERVER);
		network.registerMessage(SwapInvoPacket.class, SwapInvoPacket.class, packetID++, Side.SERVER);
		network.registerMessage(HotbarSwapPacket.class, HotbarSwapPacket.class, packetID++, Side.SERVER);
		network.registerMessage(UnlockPearlPacket.class, UnlockPearlPacket.class, packetID++, Side.SERVER);
		network.registerMessage(UnlockChestPacket.class, UnlockChestPacket.class, packetID++, Side.SERVER);
		network.registerMessage(UnlockStoragePacket.class, UnlockStoragePacket.class, packetID++, Side.SERVER);
		network.registerMessage(SortPacket.class, SortPacket.class, packetID++, Side.SERVER);
		network.registerMessage(FilterButtonPacket.class, FilterButtonPacket.class, packetID++, Side.SERVER);
		network.registerMessage(DumpButtonPacket.class, DumpButtonPacket.class, packetID++, Side.SERVER);

    network.registerMessage(PacketSyncPlayerData.class, PacketSyncPlayerData.class, PacketSyncPlayerData.ID, Side.CLIENT);
		proxy.registerHandlers();
    CapabilityRegistry.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());


	}

  public static String lang(String string) {
    return I18n.translateToLocal(string);
  }
  public static void addChatMessage(EntityPlayer p, String string) {

    p.addChatMessage(new TextComponentTranslation(lang("gui.craftexp")));
    
  }

  public static void playSound(EntityPlayer player, SoundEvent soundIn) {
    playSound(player,null,soundIn);
  }
  public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent soundIn) {
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    player.worldObj.playSound(player, here, soundIn, SoundCategory.PLAYERS,1,1);
  }
}
