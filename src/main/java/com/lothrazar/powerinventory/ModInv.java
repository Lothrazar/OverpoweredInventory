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
 * @author Initially Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 * before later being merged into my main project
 */
@Mod(modid = Const.MODID, useMetadata=true)
public class ModInv
{
//TASKS BEFORE RELEASE:
	
	//TODO 1: lang file support for key.ender, and for ALL button text -- on hold till features all in and finalized
	//fixed_ TODO 2: 'g' key overlaps with my 'push' key from the other mod, use Z default
	//fixed_ - same code as for 7 also fixed this TODO 3: bug where left/right inv buttons are ignoring the final bottom-right slot
	//fixed_TODO 4: test keepInventory gamerule ----Passed!!
	//TODO 5: pearl keybind: play the throw sound effect
	//TODO 6: on item pickup (pearl/chest) put it in the special slot by default-if possible
	//fixed_ TODO 7: lower right slots and boots overlap - I thought i fixed this already?
	//TODO 8: implement the enderpearls stacking to 64
	//TODO 9: implement the enderchest  stacking to only 1 
	//TODO 10: left/right buttons could merge stacks ? OR add a middle button that does some sort of merge/sort?
	
	
	//... find something useful to put in the top area up there
	//idea: compass/clock slots.
	
	//idea: liquid storage? bucket slot, fillButton, drainButton, and a # showing whats stored (leaves empty behind)
	//but only one type of lq at a time, and have a max /64
	
	//idea: exp numbers? such as  450/5200 = for next level (90453= total)  
	//and EXP bottle filling slot similar to liquid, empty bottles slot where you can drain from player into bottle
	//so if you put in 20 bottles, only eenough xp to fill 10, then the ten go out as full and the rest stay
	 
	
	
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
