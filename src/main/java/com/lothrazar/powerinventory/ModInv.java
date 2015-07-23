package com.lothrazar.powerinventory;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.lothrazar.powerinventory.proxy.CommonProxy; 
import com.lothrazar.powerinventory.proxy.EnderChestPacket;
import com.lothrazar.powerinventory.proxy.FilterButtonPacket;
import com.lothrazar.powerinventory.proxy.EnderPearlPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket; 

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
@Mod(modid = Const.MODID, useMetadata=true)
public class ModInv
{

    //My fork of this mod was created on July 17, 2015 at https://github.com/PrinceOfAmber/InfiniteInvo
    //original mod source was https://github.com/Funwayguy/InfiniteInvo
	
	@Instance(Const.MODID)
	public static ModInv instance;
	
	@SidedProxy(clientSide = "com.lothrazar.powerinventory.proxy.ClientProxy", serverSide = "com.lothrazar.powerinventory.proxy.CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network ;
	public static Logger logger;
	public static Configuration config;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    	
    	int packetID = 0;
    	network.registerMessage(EnderChestPacket.class, EnderChestPacket.class, packetID++, Side.SERVER);
    	network.registerMessage(SortButtonPacket.class, SortButtonPacket.class, packetID++, Side.SERVER);
    	network.registerMessage(FilterButtonPacket.class, FilterButtonPacket.class, packetID++, Side.SERVER);
    	network.registerMessage(EnderPearlPacket.class, EnderPearlPacket.class, packetID++, Side.SERVER);
    	
    	//TODO: fix these one day...
    	//network.registerMessage(DepositButtonPacket.class, DepositButtonPacket.class, DepositButtonPacket.ID, Side.SERVER);
    	//network.registerMessage(WithdrawButtonPacket.class, WithdrawButtonPacket.class, WithdrawButtonPacket.ID, Side.SERVER);

		config = new Configuration(event.getSuggestedConfigurationFile(), true);
    	loadConfig(event);
		
    	proxy.registerHandlers();
    }
    
	private void loadConfig(FMLPreInitializationEvent event) 
	{
    	config.load();
    	 
    	String category = Configuration.CATEGORY_GENERAL;
		
    	ModConfig.filterRange = config.getInt("button_filter_range", category, 12, 1, 32, "Range of the filter button to reach nearby chests");
		ModConfig.showText = config.getBoolean("show_text",category,false,"Show or hide the 'Crafting' text in the inventory");
		ModConfig.showCharacter = config.getBoolean("show_character",category,true,"Show or hide the animated character text in the inventory");
		//ModConfig.showEnderButton = config.getBoolean("button_ender_chest",category,true,"Show or hide the ender chest button");
		ModConfig.showSortButtons = config.getBoolean("button_sort",category,true,"Show or hide the ender chest button");
		ModConfig.showFilterButton = config.getBoolean("button_filter",category,true,"Show or hide the filter button");
		
		config.save();
	}
}
