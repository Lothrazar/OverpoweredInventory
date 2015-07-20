package com.lothrazar.powerinventory;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.lothrazar.powerinventory.proxy.CommonProxy;
import com.lothrazar.powerinventory.proxy.DepositButtonPacket;
import com.lothrazar.powerinventory.proxy.EnderButtonPacket;
import com.lothrazar.powerinventory.proxy.FilterButtonPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket;
import com.lothrazar.powerinventory.proxy.WithdrawButtonPacket;

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
@Mod(modid = ModInv.MODID, useMetadata=true)
public class ModInv
{
    public static final String MODID = "powerinventory";
    public static final String INVENTORY_TEXTURE = "textures/gui/inventory_gui_3.png";
	public static final String NBT_PLAYER = "Player";
	public static final String NBT_WORLD = "World";
	public static final String NBT_ID = "ID";
	public static final String NBT_Settings = "Settings";
	public static final String NBT_Unlocked = "Unlocked";
	public static final String NBT_INVENTORY = "Inventory";
	public static final String NBT_INVOSIZE = "invoSize";

	public final static int INV_ENDER = 1;
	public final static int INV_PLAYER = 2;
	
	public final static int SORT_LEFT = 1;
	public final static int SORT_RIGHT = 2;
	public final static int SORT_LEFTALL = -1;
	public final static int SORT_RIGHTALL = -2;
    //My fork of this mod was created on July 17, 2015 at https://github.com/PrinceOfAmber/InfiniteInvo
    //original mod source was https://github.com/Funwayguy/InfiniteInvo
	
	@Instance(MODID)
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
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    	network.registerMessage(EnderButtonPacket.class, EnderButtonPacket.class, EnderButtonPacket.ID, Side.SERVER);
    	network.registerMessage(SortButtonPacket.class, SortButtonPacket.class, SortButtonPacket.ID, Side.SERVER);
    	network.registerMessage(FilterButtonPacket.class, FilterButtonPacket.class, FilterButtonPacket.ID, Side.SERVER);
    	//network.registerMessage(DepositButtonPacket.class, DepositButtonPacket.class, DepositButtonPacket.ID, Side.SERVER);
    	//network.registerMessage(WithdrawButtonPacket.class, WithdrawButtonPacket.class, WithdrawButtonPacket.ID, Side.SERVER);
    	
    	config = new Configuration(event.getSuggestedConfigurationFile(), true);
    	config.load();
    	 
    	String category = Configuration.CATEGORY_GENERAL;
		ModSettings.MORE_ROWS = config.getInt("extra_rows", category, 12, 0, 20, "How many extra rows are displayed in the inventory screen");
		ModSettings.MORE_COLS = config.getInt("extra_columns", category, 16, 0, 20, "How many extra columns are displayed in the inventory screen");
		ModSettings.filterRange = config.getInt("button_filter_range", category, 12, 1, 32, "Range of the filter button to reach nearby chests");
		
		ModSettings.ALL_COLS = 9 + ModSettings.MORE_COLS;
		ModSettings.ALL_ROWS = 3 + ModSettings.MORE_ROWS;
		ModSettings.invoSize  = ModSettings.ALL_COLS * ModSettings.ALL_ROWS;
		
		//(String name, String category, String defaultValue, String comment)
		ModSettings.showText = config.getBoolean("show_text",category,false,"Show or hide the 'Crafting' text in the inventory");
		ModSettings.showCharacter = config.getBoolean("show_character",category,true,"Show or hide the animated character text in the inventory");
		ModSettings.showEnderButton = config.getBoolean("button_ender_chest",category,true,"Show or hide the ender chest button");
		ModSettings.showSortButtons = config.getBoolean("button_sort",category,true,"Show or hide the ender chest button");
		ModSettings.showFilterButton = config.getBoolean("button_filter",category,true,"Show or hide the filter button");
		
		config.save();
		
		ModSettings.SaveToCache();
		
    	
    	proxy.registerHandlers();
    }
    
    

	
    
}
