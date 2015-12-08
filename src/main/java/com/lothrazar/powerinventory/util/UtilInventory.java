package com.lothrazar.powerinventory.util;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class UtilInventory {
	public static void swapHotbars(EntityPlayer p) {
		PlayerPersistProperty prop = PlayerPersistProperty.get(p);

		for (int bar = 0; bar < Const.HOTBAR_SIZE; bar++) {
			int second = bar + Const.HOTBAR_SIZE;
			
			ItemStack barStack = p.inventory.getStackInSlot(bar);
			ItemStack secondStack = prop.inventory.getStackInSlot(second);

			// the players real hotbar
			p.inventory.setInventorySlotContents(bar, secondStack);

			// that other invo
			prop.inventory.setInventorySlotContents(second, barStack);
		}
	}

	public static void swapInventoryGroup(EntityPlayer p, int invoGroup) {
		PlayerPersistProperty prop = PlayerPersistProperty.get(p);
		//ALWAYS loop on players base invnetory, so 9 to 27+9 
		//then we offset by 18 becuase  custom invo has 2x hotbars in front

		for (int i = Const.HOTBAR_SIZE; i < Const.HOTBAR_SIZE + Const.V_INVO_SIZE; i++) {
			int second = i + (invoGroup-1) * Const.V_INVO_SIZE + Const.HOTBAR_SIZE;

			//offset: since there is no second hotbar in player inventory
			ItemStack barStack = p.inventory.getStackInSlot(i); 
			ItemStack secondStack = prop.inventory.getStackInSlot(second);

			// the players real hotbar
			p.inventory.setInventorySlotContents(i, secondStack);

			// that other invo
			prop.inventory.setInventorySlotContents(second, barStack);
		}
	}
}
