package com.lothrazar.powerinventory;

import java.util.Arrays;
import java.util.List;

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
	private static final int offset = 21;
	//dang max is 31
	//TODO: use bitwise operations to save space
	public static final int STORAGE_1_WATCHER = offset + Const.STORAGE_1;
	public static final int STORAGE_2_WATCHER = offset + Const.STORAGE_2;
	public static final int STORAGE_3_WATCHER = offset + Const.STORAGE_3;
	public static final int STORAGE_4_WATCHER = offset + Const.STORAGE_4;
	public static final int STORAGE_5_WATCHER = offset + Const.STORAGE_5;
	public static List<Integer> allWatchers = Arrays.asList(EPEARL_WATCHER,ECHEST_WATCHER,STORAGE_1_WATCHER,STORAGE_2_WATCHER,STORAGE_3_WATCHER,STORAGE_4_WATCHER,STORAGE_5_WATCHER);
	private int epearlOpen = 0;
	private int echestOpen = 0;
	//todo we could array these
	private int storage1 = 0;
	private int storage2 = 0;
	private int storage3 = 0;
	private int storage4 = 0;
	private int storage5 = 0;
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
		this.player.getDataWatcher().addObject(STORAGE_4_WATCHER, this.storage4);
		this.player.getDataWatcher().addObject(STORAGE_5_WATCHER, this.storage5);
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
		int w = (type < offset) ? offset+type : type;
		player.getDataWatcher().updateObject(w,val);
	}
	public boolean getStorage(int type){
		int w = (type < offset) ? offset+type : type;
		return player.getDataWatcher().getWatchableObjectInt(w)==1;
	}
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		player.getDataWatcher().updateObject(EPEARL_WATCHER,    compound.getInteger(ID+EPEARL_WATCHER));
		player.getDataWatcher().updateObject(ECHEST_WATCHER,    compound.getInteger(ID+ECHEST_WATCHER));
		player.getDataWatcher().updateObject(STORAGE_1_WATCHER, compound.getInteger(ID+STORAGE_1_WATCHER));
		player.getDataWatcher().updateObject(STORAGE_2_WATCHER, compound.getInteger(ID+STORAGE_2_WATCHER));
		player.getDataWatcher().updateObject(STORAGE_3_WATCHER, compound.getInteger(ID+STORAGE_3_WATCHER));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		this.inventory.writeToNBT(compound);
		compound.setInteger(ID+EPEARL_WATCHER,     player.getDataWatcher().getWatchableObjectInt(EPEARL_WATCHER));
		compound.setInteger(ID+ECHEST_WATCHER,     player.getDataWatcher().getWatchableObjectInt(ECHEST_WATCHER));
		compound.setInteger(ID+STORAGE_1_WATCHER,  player.getDataWatcher().getWatchableObjectInt(STORAGE_1_WATCHER));
		compound.setInteger(ID+STORAGE_2_WATCHER,  player.getDataWatcher().getWatchableObjectInt(STORAGE_2_WATCHER));
		compound.setInteger(ID+STORAGE_3_WATCHER,  player.getDataWatcher().getWatchableObjectInt(STORAGE_3_WATCHER));
		compound.setInteger(ID+STORAGE_4_WATCHER,  player.getDataWatcher().getWatchableObjectInt(STORAGE_4_WATCHER));
		compound.setInteger(ID+STORAGE_5_WATCHER,  player.getDataWatcher().getWatchableObjectInt(STORAGE_5_WATCHER));
	}

	public static void clonePlayerData(EntityPlayer original, EntityPlayer newPlayer)
	{
		PlayerPersistProperty.get(newPlayer).setInvoEChest(PlayerPersistProperty.get(original).getInvoEChest());
		PlayerPersistProperty.get(newPlayer).setInvoEPearl(PlayerPersistProperty.get(original).getInvoEPearl());

		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_1),Const.STORAGE_1);
		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_2),Const.STORAGE_2);
		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_3),Const.STORAGE_3);
		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_4),Const.STORAGE_4);
		PlayerPersistProperty.get(newPlayer).setStorage(PlayerPersistProperty.get(original).getStorage(Const.STORAGE_5),Const.STORAGE_5);
	}
}
