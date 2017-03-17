package com.lothrazar.powerinventory.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.inventory.InventoryOverpowered;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilInventory {
  public static void swapHotbars(EntityPlayer p) {
    InventoryOverpowered extendedInventory = UtilPlayerInventoryFilestorage.getPlayerInventory(p);
    for (int bar = 0; bar < Const.HOTBAR_SIZE; bar++) {
      int second = bar + Const.HOTBAR_SIZE;
      ItemStack barStack = p.inventory.getStackInSlot(bar);
      ItemStack secondStack = extendedInventory.getStackInSlot(second);
      //			// the players real hotbar
      p.inventory.setInventorySlotContents(bar, secondStack);
      //			// that other invo
      extendedInventory.setInventorySlotContents(second, barStack);
    }
  }
  public static void swapInventoryGroup(EntityPlayer p, int invoGroup) {
    InventoryOverpowered extendedInventory = UtilPlayerInventoryFilestorage.getPlayerInventory(p);
    // ALWAYS loop on players base invnetory, so 9 to 27+9
    // then we offset by 18 becuase custom invo has 2x hotbars in front
    for (int i = Const.HOTBAR_SIZE; i < Const.HOTBAR_SIZE + Const.V_INVO_SIZE; i++) {
      int second = i + (invoGroup - 1) * Const.V_INVO_SIZE + Const.HOTBAR_SIZE;
      // offset: since there is no second hotbar in player inventory
      ItemStack barStack = p.inventory.getStackInSlot(i);
      ItemStack secondStack = extendedInventory.getStackInSlot(second);
      //			// the players real hotbar
      p.inventory.setInventorySlotContents(i, secondStack);
      //			// that other invo
      extendedInventory.setInventorySlotContents(second, barStack);
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
    InventoryOverpowered invo = UtilPlayerInventoryFilestorage.getPlayerInventory(p);
    int sortType = getNextSort(p);
    Map<String, SortGroup> unames = new HashMap<String, SortGroup>();
    ItemStack item = null;
    SortGroup temp;
    String key = "";
    int iSize = invo.getSizeInventory();
    for (int i = 2 * Const.HOTBAR_SIZE; i < iSize; i++) {
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
    int k = 2 * Const.HOTBAR_SIZE;
    for (SortGroup sg : sorted) {
      for (int i = 0; i < sg.stacks.size(); i++) {
        invo.setInventorySlotContents(k, null);
        invo.setInventorySlotContents(k, sg.stacks.get(i));
        k++;
      }
    }
    for (int j = k; j < iSize; j++) {
      invo.setInventorySlotContents(j, null);
    }
    // alternately loop by rows
    // so we start at k again, add Const.ALL_COLS to go down one row
    //    prop.setItems(invo);
    UtilPlayerInventoryFilestorage.setPlayerInventory(p, invo);
  }
  public static ArrayList<IInventory> findTileEntityInventories(EntityPlayer player, int RADIUS) {
    // function imported
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Commands/src/main/java/com/lothrazar/samscommands/ModCommands.java#L193
    ArrayList<IInventory> found = new ArrayList<IInventory>();
    int xMin = (int) player.posX - RADIUS;
    int xMax = (int) player.posX + RADIUS;
    int yMin = (int) player.posY - RADIUS;
    int yMax = (int) player.posY + RADIUS;
    int zMin = (int) player.posZ - RADIUS;
    int zMax = (int) player.posZ + RADIUS;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (player.getEntityWorld().getTileEntity(posCurrent) instanceof IInventory) {
            found.add((IInventory) player.getEntityWorld().getTileEntity(posCurrent));
          }
        }
      }
    }
    return found;
  }
  public static void dumpFromPlayerToIInventory(World world, IInventory inventory, EntityPlayer p) {
    ItemStack chestItem;
    ItemStack invItem;
    int start = 0;
    InventoryOverpowered extendedInventory = UtilPlayerInventoryFilestorage.getPlayerInventory(p);
    // inventory and chest has 9 rows by 3 columns, never changes. same as
    // 64 max stack size
    for (int slot = start; slot < inventory.getSizeInventory(); slot++) {
      chestItem = inventory.getStackInSlot(slot);
      if (chestItem != null) {
        continue;
      } // slot not empty, skip over it
      for (int islotInv = 2 * Const.HOTBAR_SIZE; islotInv < extendedInventory.getSizeInventory(); islotInv++) {
        invItem = extendedInventory.getStackInSlot(islotInv);
        if (invItem == null) {
          continue;
        } // empty inventory slot
        inventory.setInventorySlotContents(slot, invItem);
        extendedInventory.setInventorySlotContents(islotInv, null);
        break;
      } // close loop on player inventory items
    } // close loop on chest items
    //    prop.setItems(extendedInventory);
    UtilPlayerInventoryFilestorage.setPlayerInventory(p, extendedInventory);
  }
  public static void sortFromPlayerToInventory(World world, IInventory chest, EntityPlayer p) {
    // source:
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84
    InventoryOverpowered extendedInventory = UtilPlayerInventoryFilestorage.getPlayerInventory(p);
    ItemStack chestItem;
    ItemStack invItem;
    int room;
    int toDeposit;
    int chestMax;
    // player inventory and the small chest have the same dimensions
    int START_CHEST = 0;
    int END_CHEST = chest.getSizeInventory();
    // inventory and chest has 9 rows by 3 columns, never changes. same as
    // 64 max stack size
    for (int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++) {
      chestItem = chest.getStackInSlot(islotChest);
      if (chestItem == null) {
        continue;
      } // empty chest slot
      for (int islotInv = 2 * Const.HOTBAR_SIZE; islotInv < extendedInventory.getSizeInventory(); islotInv++) {
        invItem = extendedInventory.getStackInSlot(islotInv);
        if (invItem == null) {
          continue;
        } // empty inventory slot
        if (invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() == chestItem.getItemDamage()) {
          // same item, including damage (block state)
          chestMax = chestItem.getItem().getItemStackLimit(chestItem);
          room = chestMax - chestItem.stackSize;
          if (room <= 0) {
            continue;
          } // no room, check the next spot
          // so if i have 30 room, and 28 items, i deposit 28.
          // or if i have 30 room and 38 items, i deposit 30
          toDeposit = Math.min(invItem.stackSize, room);
          chestItem.stackSize += toDeposit;
          chest.setInventorySlotContents(islotChest, chestItem);
          invItem.stackSize -= toDeposit;
          if (invItem.stackSize <= 0) {
            // item stacks with zero count do not destroy
            // themselves, they show up and have unexpected behavior
            // in game so set to empty
            extendedInventory.setInventorySlotContents(islotInv, null);
          }
          else {
            // set to new quantity
            extendedInventory.setInventorySlotContents(islotInv, invItem);
          }
        } // end if items match
      } // close loop on player inventory items
    } // close loop on chest items
    //    prop.setItems(extendedInventory);
    UtilPlayerInventoryFilestorage.setPlayerInventory(p, extendedInventory);
  }
}
