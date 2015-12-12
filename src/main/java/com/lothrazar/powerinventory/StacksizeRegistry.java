package com.lothrazar.powerinventory;

import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import com.lothrazar.powerinventory.config.ModConfig;

public class StacksizeRegistry {
	public static void registerChanges() {
		ArrayList<Item> to64 = new ArrayList<Item>();
		if (ModConfig.enderPearl64) {
			to64.add(Items.ender_pearl);
		}
		if (ModConfig.minecart64) {
			to64.add(Items.minecart);
			to64.add(Items.tnt_minecart);
			to64.add(Items.chest_minecart);
			to64.add(Items.furnace_minecart);
			to64.add(Items.hopper_minecart);
			to64.add(Items.command_block_minecart);
		}
		if (ModConfig.boat64) {
			to64.add(Items.boat);
		}
		if (ModConfig.doors64) {
			to64.add(Items.iron_door); 
		}
		if (ModConfig.snowballs64) {
			to64.add(Items.snowball);
		}
		if (ModConfig.bucket64) {
			to64.add(Items.bucket);
		}
		if (ModConfig.food64) {
			to64.add(Items.egg);
			to64.add(Items.cake);
			to64.add(Items.cookie);
			to64.add(Items.mushroom_stew); 
		}

		for (Item item : to64) {
			item.setMaxStackSize(64);
		}
	}
}
