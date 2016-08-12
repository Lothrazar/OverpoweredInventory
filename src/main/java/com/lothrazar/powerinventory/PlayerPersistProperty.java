package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.InventoryOverpowered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class PlayerPersistProperty  {
	// ref:
	// https://github.com/coolAlias/Tutorial-Demo/blob/eee34169652aaace077b6bf0348f44cbb3ddbd9b/src/main/java/tutorial/entity/ExtendedPlayer.java
	public static final String ID = "OPI";
	public static final int EPEARL_WATCHER = 20;
	public static final int ECHEST_WATCHER = 21;
	// dang max is 31
	// TODO: use bitwise operations to save space
	public static final int STORAGE_COUNT = 22;
	private int epearlOpen = 0;
	private int echestOpen = 0;
	// todo we could array these
	private int storageCount = 0;
	private EntityPlayer player;
	public final InventoryOverpowered inventory;

	public PlayerPersistProperty(EntityPlayer p) {
		this.player = p;
		inventory = new InventoryOverpowered(this.player);

	}



  public static PlayerPersistProperty get(EntityPlayer player) {
    return new PlayerPersistProperty(player);
  }



	public void setEPearlUnlocked(boolean c) {
		int val = c ? 1 : 0;
//		player.getDataWatcher().updateObject(EPEARL_WATCHER, val);
	}

	public boolean isEPearlUnlocked() {
//		return player.getDataWatcher().getWatchableObjectInt(EPEARL_WATCHER) == 1;
	  return true;
	}

	public void setEChestUnlocked(boolean c) {
		int val = c ? 1 : 0;
//		player.getDataWatcher().updateObject(ECHEST_WATCHER, val);
	}

	public boolean isEChestUnlocked() {
//		return player.getDataWatcher().getWatchableObjectInt(ECHEST_WATCHER) == 1;
    return true;
	}

	public void incrementStorage() {
//		player.getDataWatcher().updateObject(STORAGE_COUNT, this.getStorageCount() + 1);
	}

	public void setStorageCount(int c) {
//		player.getDataWatcher().updateObject(STORAGE_COUNT, c);
	}
//
//	public int getStorageCount() {
////		return player.getDataWatcher().getWatchableObjectInt(STORAGE_COUNT);
//    return true;
//	}
//
//	public boolean hasStorage(int count) {
////		return player.getDataWatcher().getWatchableObjectInt(STORAGE_COUNT) >= count;
//	}

 
	public static void clonePlayerData(EntityPlayer original, EntityPlayer newPlayer) {
//		PlayerPersistProperty propsNew = PlayerPersistProperty.get(newPlayer);
//		PlayerPersistProperty propsOriginal = PlayerPersistProperty.get(original);
//		
//		propsNew.setEChestUnlocked(propsOriginal.isEChestUnlocked());
//		propsNew.setEPearlUnlocked(propsOriginal.isEPearlUnlocked());
////		propsNew.setStorageCount(propsOriginal.getStorageCount());
//		
//		for(int i = 0; i < propsOriginal.inventory.getSizeInventory(); i++){
//			propsNew.inventory.setInventorySlotContents(i,propsOriginal.inventory.getStackInSlot(i));
//			
//			propsOriginal.inventory.setInventorySlotContents(i,null);
//		}
	}



  public boolean hasStorage(int i) {
    return true;
  }



  public static void register(EntityPlayer player2) {
    // TODO Auto-generated method stub
    
  }


}
