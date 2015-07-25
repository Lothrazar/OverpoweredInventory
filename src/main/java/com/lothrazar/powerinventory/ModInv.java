package com.lothrazar.powerinventory;

import net.minecraft.init.Items;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.lothrazar.powerinventory.proxy.CommonProxy; 
import com.lothrazar.powerinventory.proxy.EnderChestPacket;
import com.lothrazar.powerinventory.proxy.ExpButtonPacket;
import com.lothrazar.powerinventory.proxy.FilterButtonPacket;
import com.lothrazar.powerinventory.proxy.EnderPearlPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket; 

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
 * @author Initially Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 * before later being merged into my main project
 */
@Mod(modid = Const.MODID, useMetadata=true)
public class ModInv
{
 
	//TODO 
	
	// lang file support for key.ender, and for ALL button text -- on hold till features all in and finalized
	// implement shift clicking with exp bottle slot

	
	//??POSSIBLE additions? 
	// on item pickup (pearl/chest) put it in the special slot by default-if possible
	//shift click out of hotbar should go directly to special slots, work same way as armor
	// left/right buttons could merge stacks ? OR add a middle button that does some sort of merge/sort?
	
	 
	//idea: liquid storage? bucket slot, fillButton, drainButton, and a # showing whats stored (leaves empty behind)
		//but only one type of lq at a time, and have a max /64
	
	//Display exact exp numbers? such as  450/5200 = for next level (90453= total)  
 
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
    	network.registerMessage(ExpButtonPacket.class, ExpButtonPacket.class, packetID++, Side.SERVER);
    	 
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
		ModConfig.showSortButtons = config.getBoolean("move_inventory_buttons",category,true,"Show or hide the inventory shifting buttons << >>");
		ModConfig.enderPearl64 = config.getBoolean("ender_pearl_64", category, true, "Stack to 64 instead of 16");
		
		ModConfig.expPerBottle = config.getInt("exp_per_bottle", category, 10, 1, 11, "The exp cost of filling a single bottle.  Remember, the Bottle 'o Enchanting gives 3-11 experience when used, so it is never an exact two-way conversion.  ");
		  
		if(config.hasChanged()){config.save();}
	}
    
    @EventHandler
    public void preInit(FMLInitializationEvent event)
    {
    	if(ModConfig.enderPearl64)
    	{
    		Items.ender_pearl.setMaxStackSize(64);
    	}
    }
}
