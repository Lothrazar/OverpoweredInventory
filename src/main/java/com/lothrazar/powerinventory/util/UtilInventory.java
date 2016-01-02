package com.lothrazar.powerinventory.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.PlayerPersistProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
		// ALWAYS loop on players base invnetory, so 9 to 27+9
		// then we offset by 18 becuase custom invo has 2x hotbars in front

		for (int i = Const.HOTBAR_SIZE; i < Const.HOTBAR_SIZE + Const.V_INVO_SIZE; i++) {
			int second = i + (invoGroup - 1) * Const.V_INVO_SIZE + Const.HOTBAR_SIZE;

			// offset: since there is no second hotbar in player inventory
			ItemStack barStack = p.inventory.getStackInSlot(i);
			ItemStack secondStack = prop.inventory.getStackInSlot(second);

			// the players real hotbar
			p.inventory.setInventorySlotContents(i, secondStack);

			// that other invo
			prop.inventory.setInventorySlotContents(second, barStack);
		}
	}

	final static String NBT_SORT = Const.MODID + "_sort";
	final static int SORT_ALPH = 0;
	final static int SORT_ALPHI = 1;

	private static int getNextSort(EntityPlayer p) {
		int prev = p.getEntityData().getInteger(NBT_SORT);

		int n = prev + 1;

		if (n >= 2)
			n = 0;

		p.getEntityData().setInteger(NBT_SORT, n);

		return n;
	}

	public static void doSort(EntityPlayer p) {
		PlayerPersistProperty prop = PlayerPersistProperty.get(p);

		IInventory invo = prop.inventory;

		int sortType = getNextSort(p);

		Map<String, SortGroup> unames = new HashMap<String, SortGroup>();

		ItemStack item = null;
		SortGroup temp;
		String key = "";
		int iSize = invo.getSizeInventory();
		for (int i = 2*Const.HOTBAR_SIZE; i < iSize; i++) {
			item = invo.getStackInSlot(i);
			if (item == null) {
				continue;
			}

			if (sortType == SORT_ALPH)
				key = item.getUnlocalizedName() + item.getItemDamage();
			else if (sortType == SORT_ALPHI)
				key = item.getItem().getClass().getName() + item.getUnlocalizedName() + item.getItemDamage();

			temp = unames.get(key);
			if (temp == null) {
				temp = new SortGroup(key);
			}

			if (temp.stacks.size() > 0) {
				// try to merge with top
				ItemStack top = temp.stacks.remove(temp.stacks.size() - 1);

				int room = top.getMaxStackSize() - top.stackSize;

				if (room > 0) {
					int moveover = Math.min(item.stackSize, room);

					top.stackSize += moveover;

					item.stackSize -= moveover;

					if (item.stackSize == 0) {
						item = null;
						invo.setInventorySlotContents(i, item);
					}
				}

				temp.stacks.add(top);
			}

			if (item != null)
				temp.add(item);

			unames.put(key, temp);
		}

		// http://stackoverflow.com/questions/780541/how-to-sort-a-hashmap-in-java

		ArrayList<SortGroup> sorted = new ArrayList<SortGroup>(unames.values());
		Collections.sort(sorted, new Comparator<SortGroup>() {
			public int compare(SortGroup o1, SortGroup o2) {
				return o1.key.compareTo(o2.key);
			}
		});

		int k = 2*Const.HOTBAR_SIZE;
		for (SortGroup sg : sorted) {
			//System.out.println(sg.key+" _ "+k);

			for (int i = 0; i < sg.stacks.size(); i++) {
				
				invo.setInventorySlotContents(k, null);
				invo.setInventorySlotContents(k, sg.stacks.get(i));
				k++;
			}
		}

		for (int j = k; j < iSize; j++) {
			// System.out.println(j + "Empty Out ");
			invo.setInventorySlotContents(j, null);
		}

		// alternately loop by rows
		// so we start at k again, add Const.ALL_COLS to go down one row

	}
}
