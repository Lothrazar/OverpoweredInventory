package com.lothrazar.powerinventory;

import net.minecraftforge.common.config.Configuration;
 
public class ModConfig
{  
	public static boolean showSortButtons;
	public static boolean showCornerButtons;
	public static boolean showCharacter;
	public static boolean showMergeDeposit;
	public static boolean enderPearl64;
	

	public static String smallMedLarge;

	
	public static int filterRange; 
	public static int expPerBottle;
	

	public static boolean enableUncrafting;
	public static boolean enableEnchantBottles;
	
	public static boolean enableCompatMode;
	public static boolean blockVersionChecker;
	

	private static Configuration config;

	public static void loadConfig(Configuration c) 
	{
		config = c;
    	config.load();
    	 
    	String category = Configuration.CATEGORY_GENERAL;
		
    	ModConfig.filterRange = config.getInt("button_filter_range", category, 12, 1, 32, "Range of the filter button to reach nearby chests");
		ModConfig.showCharacter = config.getBoolean("show_character",category,true,"Show or hide the animated character text in the inventory");
		ModConfig.showSortButtons = config.getBoolean("move_inventory_buttons",category,true,"Show or hide the inventory shifting buttons << >>");
		ModConfig.showCornerButtons = config.getBoolean("show_corner_buttons",category,true,"Show or hide the corner inventory buttons in other GUI's");
		ModConfig.enderPearl64 = config.getBoolean("ender_pearl_64", category, true, "Stack to 64 instead of 16");
		
		ModConfig.showMergeDeposit = config.getBoolean("merge_deposit_buttons", category, true, "Show or hide the merge deposit buttons in upper right corner.");
		ModConfig.expPerBottle = config.getInt("exp_per_bottle", category, 10, 1, 11, "The exp cost of filling a single bottle.  Remember, the Bottle 'o Enchanting gives 3-11 experience when used, so it is never an exact two-way conversion.  ");
		
		
		category = "warning_advanced";
		
		config.addCustomCategoryComment(category, "This section is for disabling and changes major features.  Always empty your inventory before changing these.");
		
		
		ModConfig.enableUncrafting = config.getBoolean("enable_uncrafting",category,true,"Lets you disable the uncrafting slot and button");
		ModConfig.enableEnchantBottles =  config.getBoolean("enable_enchantbottles",category,true,"Lets you disable the enchanting bottle filling slot and button");
		
		ModConfig.smallMedLarge = config.getString("main_size", category, "normal", "Valid values are only exactly 'normal', 'small', 'large'.    Changes your inventory size, for use if your GUI Scale requirements are different.  normal = regular 15x25 inventory size, small = 6x18.  WARNING: EMPTY YOUR PLAYERS INVENTORY IN A CHEST before changing this.  And to be safe, BACKUP YOUR WORLD!");

		ModConfig.blockVersionChecker = config.getBoolean("block_versionchecker",category,false,"By default, this checks once on game startup for a new mod update version.  Set as true to block this check and notice.  Config entry added for modpack creators.");

		category = "warning_compatibility";
		config.addCustomCategoryComment(category, "Compatibility mode is intended for advanced users and modpack creators. "
				+ "It is intended to be turned on if you are using other mods that crash the game or conflict with this mod in some way.  For example, mods that alter the vanilla inventory using ASM techniques, or mods that add tabs (such as Tinkers construct or Custom NPCs).");
		
		
		ModConfig.enableCompatMode =  config.getBoolean("compatibility_mode",category,false,"False is the regular mod with everything normal.  "
				+ "True will give you the regular vanilla inventory, not replaced or changed in any way for compatibility reasons.  Instead, push the upper right button to use the mini version.");
		
		
	
		
		
		if(ModConfig.smallMedLarge.equalsIgnoreCase("normal"))
		{

			Const.MORE_ROWS = 12;//texture 15x25
		 
			Const.MORE_COLS = 16;

			Const.texture_width = 464;
			Const.texture_height = 382;
		    Const.INVENTORY_TEXTURE = "textures/gui/inventory_15x25.png";//375 total
		}
		else if(ModConfig.smallMedLarge.equalsIgnoreCase("large"))
		{
			//might as well add more rows too. work in progress
			Const.MORE_ROWS = 13; 
			 
			Const.MORE_COLS = 18;

			Const.texture_width = 500;
			Const.texture_height = 400;
		    Const.INVENTORY_TEXTURE = "textures/gui/inventory_15x28.png";
		    
		    int offset = 18*2;
		    Const.bottleX += offset; 
			Const.uncraftX += offset; 
		}

		else//assume its small
		{
			//config.setString("normal_small","small");
			ModConfig.smallMedLarge = "small";
			Const.MORE_ROWS = 3;
		 
			Const.MORE_COLS = 9;

			Const.texture_width = 338;
			Const.texture_height = 221;

		    int offset = 18*7;
		    Const.bottleX -= offset; 
			Const.uncraftX -= offset; 
			
		    Const.INVENTORY_TEXTURE = "textures/gui/inventory_6x18.png";//6*18 is 108..so yeah?
		}

		Const.ALL_COLS = 9 + Const.MORE_COLS;
		Const.ALL_ROWS = 3 + Const.MORE_ROWS;
		Const.INVOSIZE  = Const.ALL_COLS * Const.ALL_ROWS;
		
		
		if(ModConfig.enableCompatMode)
		{
			Const.texture_width = 176;
			Const.texture_height = 166;
			
			int charSpace = 54 + 18;// moving stuff left
			
			//TODO: these get set twice, or more, we should fix this whole setup but for now just get it working
			Const.compassX -= charSpace;
			Const.clockX -= charSpace;
			Const.pearlX -= charSpace;
			Const.echestX -= charSpace;
			
			Const.bottleX = Const.texture_width - Const.square - Const.padding - 1;
			 
			Const.uncraftX = Const.bottleX;
		}
		
		
		
		
		if(config.hasChanged()){config.save();}
	}
    
	//2: add in/fix the armor background images
	
	
	//3: option to turn off background images (including armor)
	
	
	//4 more 64 stacking
	
	
	
	
	
	//turn onn/off the uncrafting and ehcnat bottling
	
	
	//turn off version checker 
	
	
	
	// exp cost for uncrafting added (default zero)  
	
	


	//?? enable/disable 3x3 crafting
	// ???: make the left 9 columns actually go vertically down so it matches player invo
}
