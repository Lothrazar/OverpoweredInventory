package com.lothrazar.powerinventory.util;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.inventory.InventoryOverpowered;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class UtilInventory {
	public static void swapHotbars(EntityPlayer p) {
		PlayerPersistProperty prop = PlayerPersistProperty.get(p);

		for (int bar = 0; bar < Const.HOTBAR_SIZE; bar++) {
			int second = bar + InventoryOverpowered.INV_SIZE - Const.HOTBAR_SIZE;

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
		// if ivg == 1; we go from 9 to 27+9
		// ivg == 2 means .. wel the first block is still 9 to 27+9 but we SWAP
		// it with range a full blocku p
		for (int i = Const.HOTBAR_SIZE; i < Const.HOTBAR_SIZE + Const.V_INVO_SIZE; i++) {
			int second = i + invoGroup * Const.V_INVO_SIZE;

			ItemStack barStack = p.inventory.getStackInSlot(i);// oob 4??
			ItemStack secondStack = prop.inventory.getStackInSlot(second);

			// the players real hotbar
			p.inventory.setInventorySlotContents(i, secondStack);

			// that other invo
			prop.inventory.setInventorySlotContents(second, barStack);
		}
	}
}
