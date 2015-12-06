package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.InventoryOverpowered;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class PlayerPersistProperty implements IExtendedEntityProperties
{
	// ref: https://github.com/coolAlias/Tutorial-Demo/blob/eee34169652aaace077b6bf0348f44cbb3ddbd9b/src/main/java/tutorial/entity/ExtendedPlayer.java
	public static final String ID = "OPI";
	public static final String NBT_PEARL = "pearl";
	public static final String NBT_CHEST = "chest";
	public static final String NBT_STORAGE1 = "storage1";
	public static final String NBT_STORAGE2 = "storage2";
	public static final String NBT_STORAGE3 = "storage3";
	public static final int EPEARL_WATCHER = 21;
	public static final int ECHEST_WATCHER = 22;
	public static final int STORAGE_1_WATCHER = 24;
	public static final int STORAGE_2_WATCHER = 25;
	public static final int STORAGE_3_WATCHER = 26;
	private int epearlOpen = 0;
	private int echestOpen = 0;
	private int storage1 = 0;
	private int storage2 = 0;
	private int storage3 = 0;
	private EntityPlayer player;
	public final InventoryOverpowered inventory;
	
	public PlayerPersistProperty(EntityPlayer p)
	{
		this.player = p;
		inventory = new InventoryOverpowered(this.player);
		this.player.getDataWatcher().addObject(EPEARL_WATCHER, this.epearlOpen);
		this.player.getDataWatcher().addObject(ECHEST_WATCHER, this.echestOpen);
		this.player.getDataWatcher().addObject(STORAGE_1_WATCHER, this.storage1);
		this.player.getDataWatcher().addObject(STORAGE_2_WATCHER, this.storage2);
		this.player.getDataWatcher().addObject(STORAGE_3_WATCHER, this.storage3);
	}

	public static void register(EntityPlayer player)
	{
		player.registerExtendedProperties(ID, new PlayerPersistProperty(player));
	}
	
	public static PlayerPersistProperty get(EntityPlayer player)
	{
		IExtendedEntityProperties property = player.getExtendedProperties(ID);
		
		if(property != null && property instanceof PlayerPersistProperty)
		{
			return (PlayerPersistProperty)property;
		} 
		else
		{
			return null;
		}
	}
	
	@Override
	public void init(Entity entity, World world)
	{
		if(entity instanceof EntityPlayer)
		{
			this.player = (EntityPlayer)entity;
		}
	}

	public void setInvoEPearl(boolean c){
		int val = c?1:0;
		player.getDataWatcher().updateObject(EPEARL_WATCHER,val);
	}
	public boolean getInvoEPearl(){
		return player.getDataWatcher().getWatchableObjectInt(EPEARL_WATCHER)==1;
	}
	public void setInvoEChest(boolean c){
		int val = c?1:0;
		player.getDataWatcher().updateObject(ECHEST_WATCHER,val);
	}
	public boolean getInvoEChest(){
		return player.getDataWatcher().getWatchableObjectInt(ECHEST_WATCHER)==1;
	}
	public void setStorage(boolean c, int type){
		int val = c?1:0;
		int w = 0;
		switch(type){
			case Const.STORAGE_1TOPRIGHT: w = STORAGE_1_WATCHER; break;
			case Const.STORAGE_2BOTLEFT: w =  STORAGE_2_WATCHER; break;
			case Const.STORAGE_3BOTRIGHT: w = STORAGE_3_WATCHER; break;
		}
		
		player.getDataWatcher().updateObject(w,val);
	}
	public boolean getStorage(int type){
		int w = 0;
		switch(type){
			case Const.STORAGE_1TOPRIGHT: w = STORAGE_1_WATCHER; break;
			case Const.STORAGE_2BOTLEFT: w =  STORAGE_2_WATCHER; break;
			case Const.STORAGE_3BOTRIGHT: w = STORAGE_3_WATCHER; break;
		}
		
		return player.getDataWatcher().getWatchableObjectInt(w)==1;
	}/*
	public int getStorageCount(){

		//todo: a loop or data struct somehow?
		return player.getDataWatcher().getWatchableObjectInt(STORAGE_1_WATCHER) 
				+ player.getDataWatcher().getWatchableObjectInt(STORAGE_2_WATCHER) 
				+ player.getDataWatcher().getWatchableObjectInt(STORAGE_3_WATCHER);
	}*/
	public boolean getDualHotbarUnlocked(){
		return true;//TODO! THIS
	}
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		player.getDataWatcher().updateObject(EPEARL_WATCHER, compound.getInteger(NBT_PEARL));
		player.getDataWatcher().updateObject(ECHEST_WATCHER, compound.getInteger(NBT_CHEST));
		player.getDataWatcher().updateObject(STORAGE_1_WATCHER, compound.getInteger(NBT_STORAGE1));
		player.getDataWatcher().updateObject(STORAGE_2_WATCHER, compound.getInteger(NBT_STORAGE2));
		player.getDataWatcher().updateObject(STORAGE_3_WATCHER, compound.getInteger(NBT_STORAGE3));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		this.inventory.writeToNBT(compound);
		compound.setInteger(NBT_PEARL,  player.getDataWatcher().getWatchableObjectInt(EPEARL_WATCHER));
		compound.setInteger(NBT_CHEST,  player.getDataWatcher().getWatchableObjectInt(ECHEST_WATCHER));
		compound.setInteger(NBT_STORAGE1,  player.getDataWatcher().getWatchableObjectInt(STORAGE_1_WATCHER));
		compound.setInteger(NBT_STORAGE2,  player.getDataWatcher().getWatchableObjectInt(STORAGE_2_WATCHER));
		compound.setInteger(NBT_STORAGE3,  player.getDataWatcher().getWatchableObjectInt(STORAGE_3_WATCHER));
	}

	public static void clonePlayerData(EntityPlayer original, EntityPlayer newPlayer)
	{
		PlayerPersistProperty.get(newPlayer).setInvoEChest(PlayerPersistProperty.get(original).getInvoEChest());
		PlayerPersistProperty.get(newPlayer).setInvoEPearl(PlayerPersistProperty.get(original).getInvoEPearl());

		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_1TOPRIGHT),Const.STORAGE_1TOPRIGHT);
		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_2BOTLEFT),Const.STORAGE_2BOTLEFT);
		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_3BOTRIGHT),Const.STORAGE_3BOTRIGHT);
	}
}
