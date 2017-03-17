package com.lothrazar.powerinventory.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import com.google.common.io.Files;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.inventory.InventoryOverpowered;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;

/*Thank you so much for the help azanor
 * for basically writing this class and releasing it open source
 * 
 * https://github.com/Azanor/Baubles
 * 
 * which is under Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) license.
 * so i was able to use parts of that to make this
 * **/
public class UtilPlayerInventoryFilestorage {
  public static final String EXT = "opi";
  public static final String EXTBK = "opibk";
  public static HashSet<Integer> playerEntityIds = new HashSet<Integer>();
  private static HashMap<String, InventoryOverpowered> playerItems = new HashMap<String, InventoryOverpowered>();
  public static void playerSetupOnLoad(PlayerEvent.LoadFromFile event) {
    EntityPlayer player = event.getEntityPlayer();
    clearPlayerInventory(player);
    File playerFile = getPlayerFile(EXT, event.getPlayerDirectory(), event.getEntityPlayer());
    if (!playerFile.exists()) {
      File fileNew = event.getPlayerFile(EXT);
      if (fileNew.exists()) {
        try {
          Files.copy(fileNew, playerFile);
          ModInv.logger.info("Using and converting UUID savefile for " + player.getDisplayNameString());
          fileNew.delete();
          File fb = event.getPlayerFile(EXTBK);
          if (fb.exists())
            fb.delete();
        }
        catch (IOException e) {
        }
      }
    }
    loadPlayerInventory(event.getEntityPlayer(), playerFile, getPlayerFile(EXTBK, event.getPlayerDirectory(), event.getEntityPlayer()));
    playerEntityIds.add(event.getEntityPlayer().getEntityId());
  }
  public static void clearPlayerInventory(EntityPlayer player) {
    playerItems.remove(player.getDisplayNameString());
  }
  public static InventoryOverpowered getPlayerInventory(EntityPlayer player) {
    if (!playerItems.containsKey(player.getDisplayNameString())) {
      InventoryOverpowered inventory = new InventoryOverpowered(player);
      playerItems.put(player.getDisplayNameString(), inventory);
    }
    return playerItems.get(player.getDisplayNameString());
  }
  public static ItemStack getPlayerInventoryStack(EntityPlayer player, int slot) {
    return getPlayerInventory(player).getStackInSlot(slot);
  }
  public static void setPlayerInventoryStack(EntityPlayer player, int slot, ItemStack itemStack) {
    //    UtilPlayerInventoryFilestorage.getPlayerInventory(player).setInventorySlotContents(slot, itemStack);
    if (slot == Const.SLOT_ECHEST) {
      getPlayerInventory(player).enderChestStack = itemStack;
    }
    else if (slot == Const.SLOT_EPEARL) {
      getPlayerInventory(player).enderPearlStack = itemStack;
    }
    else
//      getPlayerInventory(player).inventory[slot] = itemStack;
    getPlayerInventory(player).inventory.set(slot, itemStack);
  }
  public static void setPlayerInventory(EntityPlayer player, InventoryOverpowered inventory) {
    playerItems.put(player.getDisplayNameString(), inventory);
  }
  public static void loadPlayerInventory(EntityPlayer player, File file1, File file2) {
    if (player != null && !player.getEntityWorld().isRemote) {
      try {
        NBTTagCompound data = null;
        boolean save = false;
        if (file1 != null && file1.exists()) {
          try {
            FileInputStream fileinputstream = new FileInputStream(file1);
            data = CompressedStreamTools.readCompressed(fileinputstream);
            fileinputstream.close();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        if (file1 == null || !file1.exists() || data == null || data.hasNoTags()) {
          ModInv.logger.warn("Data not found for " + player.getDisplayNameString() + ". Trying to load backup data.");
          if (file2 != null && file2.exists()) {
            try {
              FileInputStream fileinputstream = new FileInputStream(file2);
              data = CompressedStreamTools.readCompressed(fileinputstream);
              fileinputstream.close();
              save = true;
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        if (data != null) {
          InventoryOverpowered inventory = new InventoryOverpowered(player);
          inventory.readFromNBT(data);
          playerItems.put(player.getDisplayNameString(), inventory);
          if (save)
            savePlayerItems(player, file1, file2);
        }
      }
      catch (Exception e) {
        ModInv.logger.error("Error loading player extended inventory");
        e.printStackTrace();
      }
    }
  }
  public static void savePlayerItems(EntityPlayer player, File file1, File file2) {
    if (player != null && !player.getEntityWorld().isRemote) {
      try {
        if (file1 != null && file1.exists()) {
          try {
            Files.copy(file1, file2);
          }
          catch (Exception e) {
            ModInv.logger.error("Could not backup old file for player " + player.getDisplayNameString());
          }
        }
        try {
          if (file1 != null) {
            InventoryOverpowered inventory = getPlayerInventory(player);
            NBTTagCompound data = new NBTTagCompound();
            inventory.writeToNBT(data);
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            CompressedStreamTools.writeCompressed(data, fileoutputstream);
            fileoutputstream.close();
          }
        }
        catch (Exception e) {
          ModInv.logger.error("Could not save file for player " + player.getDisplayNameString());
          e.printStackTrace();
          if (file1.exists()) {
            try {
              file1.delete();
            }
            catch (Exception e2) {
            }
          }
        }
      }
      catch (Exception exception1) {
        ModInv.logger.error("Error saving inventory");
        exception1.printStackTrace();
      }
    }
  }
  public static File getPlayerFile(String suffix, File playerDirectory, EntityPlayer player) {
    String playername = player.getUniqueID().toString();
    String file =  "_" + playername + "." + suffix;
    ModInv.logger.info("Player File: "+file);
    return new File(playerDirectory, file);
  }
  public static void syncItems(EntityPlayer player) {
    InventoryOverpowered invo = getPlayerInventory(player);
    for (int a = 0; a < invo.getSizeInventory(); a++) {
      getPlayerInventory(player).syncSlotToClients(a);
    }
  }
  public static void putDataIntoInventory(InventoryOverpowered invo, EntityPlayer player) {
    InventoryOverpowered fromStorage = getPlayerInventory(player);
    invo.inventory = fromStorage.inventory;
    invo.enderPearlStack = fromStorage.enderPearlStack;
    invo.enderChestStack = fromStorage.enderChestStack;
  }
}
