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
	public static final int EPEARL_WATCHER = 20;
	public static final int ECHEST_WATCHER = 21;
	//dang max is 31
	//TODO: use bitwise operations to save space
	public static final int STORAGE_COUNT = 22;
	private int epearlOpen = 0;
	private int echestOpen = 0;
	//todo we could array these
	private int storageCount = 0;
	private EntityPlayer player;
	public final InventoryOverpowered inventory;
	
	public PlayerPersistProperty(EntityPlayer p)
	{
		this.player = p;
		inventory = new InventoryOverpowered(this.player);
		this.player.getDataWatcher().addObject(EPEARL_WATCHER, this.epearlOpen);
		this.player.getDataWatcher().addObject(ECHEST_WATCHER, this.echestOpen);
		this.player.getDataWatcher().addObject(STORAGE_COUNT, this.storageCount);                         
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
	public void incrementStorage(){
		player.getDataWatcher().updateObject(STORAGE_COUNT,this.getStorageCount() + 1);
	}
	public void setStorageCount(int c){
		player.getDataWatcher().updateObject(STORAGE_COUNT,c);
	}
	public int getStorageCount(){
		return player.getDataWatcher().getWatchableObjectInt(STORAGE_COUNT);
	}
	public boolean getStorage(int count){
		return player.getDataWatcher().getWatchableObjectInt(STORAGE_COUNT) >= count;
	}
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		player.getDataWatcher().updateObject(EPEARL_WATCHER,    compound.getInteger(ID+EPEARL_WATCHER));
		player.getDataWatcher().updateObject(ECHEST_WATCHER,    compound.getInteger(ID+ECHEST_WATCHER));
		player.getDataWatcher().updateObject(STORAGE_COUNT, compound.getInteger(ID+STORAGE_COUNT));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		this.inventory.writeToNBT(compound);
		compound.setInteger(ID+EPEARL_WATCHER,     player.getDataWatcher().getWatchableObjectInt(EPEARL_WATCHER));
		compound.setInteger(ID+ECHEST_WATCHER,     player.getDataWatcher().getWatchableObjectInt(ECHEST_WATCHER));
		compound.setInteger(ID+STORAGE_COUNT,  player.getDataWatcher().getWatchableObjectInt(STORAGE_COUNT));                                      
	}

	public static void clonePlayerData(EntityPlayer original, EntityPlayer newPlayer)
	{
		PlayerPersistProperty.get(newPlayer).setInvoEChest(PlayerPersistProperty.get(original).getInvoEChest());
		PlayerPersistProperty.get(newPlayer).setInvoEPearl(PlayerPersistProperty.get(original).getInvoEPearl());

		PlayerPersistProperty.get(newPlayer).setStorageCount(PlayerPersistProperty.get(original).getStorageCount());
	}
}
