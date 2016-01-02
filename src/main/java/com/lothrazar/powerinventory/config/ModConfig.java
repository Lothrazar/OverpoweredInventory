package com.lothrazar.powerinventory.config;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {
	public static boolean alwaysShowHungerbar;
	public static boolean enderPearl64;
	public static boolean minecart64;
	public static boolean boat64;
	public static boolean doors64;
	public static boolean snowballs64;
	public static boolean food64;
	public static boolean bucket64;
	public static int expCostStorage_start;
	public static int expCostStorage_inc;
	public static int expCostPearl;
	public static int expCostEChest;
	public static Configuration config;
	public static boolean persistUnlocksOnDeath = true;
	public static final String categoryHighlander = "overpowered_inventory";

	private static int TEXTURE_WIDTH = 342 + 162 + 4;// 508
	private static int TEXTURE_HEIGHT = 230 + 54 + 54 + 4 + 4;// 346

	private static boolean isLarge = true;
	public static int filterRange = 32;//TODO: not linked

	public static boolean isLargeScreen() {
		return isLarge;
	}

	public static int getInvoWidth() {
		return TEXTURE_WIDTH;
	}

	public static int getInvoHeight() {
		return TEXTURE_HEIGHT;
	}

	public static int getMaxSections() {
		return (isLarge) ? 15 : 6;
	}

	public static void loadConfig(Configuration c) {
		config = c;
		config.load();
		syncConfig();
	}

	public static void syncConfig() {
		// decide which ones can be altered in game - there can be only one
		String category = categoryHighlander;

		ModConfig.alwaysShowHungerbar = config.getBoolean("always_show_hunger", category, true, "Always show hunger bar - even while mounted.  Horse health will show above the hunger bar");

		ModConfig.expCostPearl = config.getInt("exp_cost_pearl_slot", categoryHighlander, 900, 1, 9999, "Experience points needed to add the ender pearl");
		ModConfig.expCostEChest = config.getInt("exp_cost_enderchest_slot", categoryHighlander, 950, 1, 9999, "Experience points needed to add the ender chest");
		ModConfig.expCostStorage_start = config.getInt("exp_cost_storage_start", categoryHighlander, 50, 1, 9999, "Experience points needed to unlock the first storage area");
		ModConfig.expCostStorage_inc = config.getInt("exp_cost_storage_increment", categoryHighlander, 100, 1, 9999, "Increment of experience points needed to unlock each successive storage area (adds up each time)");
		
		
		ModConfig.persistUnlocksOnDeath = config.getBoolean("persist_unlocks_death", categoryHighlander, true, "All EXP unlocks such as crafting slots will be saved and remembered through death.  If this is false, all unlocks reset on death and become locked (modpack makers: feel free to reduce costs if you set this false)");

		category = "stack_to_64";

		// these dont seem to work without restarting the game
		ModConfig.enderPearl64 = config.get(category, "ender_pearl", true).getBoolean();
		ModConfig.minecart64 = config.get(category, "minecarts", true).getBoolean();
		ModConfig.boat64 = config.get(category, "boats", true).getBoolean();
		ModConfig.doors64 = config.get(category, "doors", true).getBoolean();
		ModConfig.snowballs64 = config.get(category, "snowballs", true).getBoolean();
		ModConfig.food64 = config.get(category, "allfood_cake_eggs_stew", true).getBoolean();// cookie,
																								// stews,
																								// cakes
		ModConfig.bucket64 = config.get(category, "empty_bucket", true).getBoolean();

		category = "resolution_size_setting";
		config.addCustomCategoryComment(category, "The small size is for use with smaller screen resolutions, or with GUI Scale Small or Auto.  For GUI Scale Normal and above, the large setting should work fine." + "  WARNING: empty your inventory before you change this from large to small");

		ModConfig.isLarge = config.getBoolean("is_large", category, true, "Large is the default size.  Or you can change this to false for the small version (which still has six extra 3x9 inventory sections)");
		// currently the 'normal' from old version is out
		if (isLarge) {

			TEXTURE_WIDTH = 342 + 162 + 4;// 508
			TEXTURE_HEIGHT = 230 + 54 + 54 + 4 + 4;// 346
		} else {

			TEXTURE_WIDTH = 342;
			TEXTURE_HEIGHT = 230;
		}

		if (config.hasChanged()) {
			config.save();
		}
	}
}
