package com.lothrazar.powerinventory;
 
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraftforge.common.config.Configuration;
 
public class ModConfig
{  
	public static boolean alwaysShowHungerbar;
	public static boolean enderPearl64;
	public static boolean minecart64;
	public static boolean boat64;
	public static boolean doors64;
	public static boolean snowballs64;
	public static boolean food64;
	public static boolean bucket64;
	
	public static Configuration config;
	public static final String categoryHighlander = "can_change_ingame";

	public static void loadConfig(Configuration c) 
	{
		config = c;
    	config.load();
    	syncConfig();
	}
	
	public static void syncConfig()
	{
    	// decide which ones can be altered in game - there can be only one
    	String category = categoryHighlander;

		ModConfig.alwaysShowHungerbar = config.getBoolean("always_show_hunger",category,true,"Always show hunger bar - even while mounted.  Horse health will show above the hunger bar.");

		
		category = "stack_to_64";

		//these dont seem to work without restarting the game
		ModConfig.enderPearl64 = config.get( category,"ender_pearl", true).getBoolean();
		ModConfig.minecart64 = config.get(category,"minecarts",  true).getBoolean();
		ModConfig.boat64 = config.get(category,"boats",  true).getBoolean();
		ModConfig.doors64 = config.get(category,"doors",  true).getBoolean();
		ModConfig.snowballs64 = config.get(category,"snowballs",  true).getBoolean();
		ModConfig.food64 = config.get(category,"allfood_cake_eggs_stew",  true).getBoolean();//cookie, stews, cakes
		ModConfig.bucket64 = config.get(category,"empty_bucket",  true).getBoolean();

		if(config.hasChanged()){config.save();}
		
		
		
		//TODO: this doesnt depend on config so remove this to somewhere else?
		SlotEnderPearl.posX = Const.paddingLrg; 
		SlotEnderPearl.posY = Const.paddingLrg; 	
		
		SlotEnderChest.posY = Const.paddingLrg + 3 * Const.SQ;  
		SlotEnderChest.posX = Const.paddingLrg; 
	}
}
