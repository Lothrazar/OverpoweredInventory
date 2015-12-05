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
	public static final String NBT_CRAFT = "crafting";
	public static final String NBT_PEARL = "pearl";
	public static final String NBT_CHEST = "chest";
	public static final int CRAFTING_WATCHER = 20;
	public static final int EPEARL_WATCHER = 21;
	public static final int ECHEST_WATCHER = 22;
	private int craftingOpen = 0;
	private int epearlOpen = 0;
	private int echestOpen = 0;
	private EntityPlayer player;
	public final InventoryOverpowered inventory = new InventoryOverpowered();

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
	
	public PlayerPersistProperty(EntityPlayer player)
	{
		this.player = player;
		this.player.getDataWatcher().addObject(CRAFTING_WATCHER, this.craftingOpen);
		this.player.getDataWatcher().addObject(EPEARL_WATCHER, this.epearlOpen);
		this.player.getDataWatcher().addObject(ECHEST_WATCHER, this.echestOpen);
	}
	
	@Override
	public void init(Entity entity, World world)
	{
		if(entity instanceof EntityPlayer)
		{
			this.player = (EntityPlayer)entity;
		}
	}

	public void setInvoCrafting(boolean c){
		int val = c?1:0;
		player.getDataWatcher().updateObject(CRAFTING_WATCHER,val);
	}
	public boolean getInvoCrafting(){
		return player.getDataWatcher().getWatchableObjectInt(CRAFTING_WATCHER)==1;
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
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		player.getDataWatcher().updateObject(CRAFTING_WATCHER, compound.getInteger(NBT_CRAFT));
		player.getDataWatcher().updateObject(EPEARL_WATCHER, compound.getInteger(NBT_PEARL));
		player.getDataWatcher().updateObject(ECHEST_WATCHER, compound.getInteger(NBT_CHEST));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		this.inventory.writeToNBT(compound);
		compound.setInteger(NBT_CRAFT,  player.getDataWatcher().getWatchableObjectInt(CRAFTING_WATCHER));
		compound.setInteger(NBT_PEARL,  player.getDataWatcher().getWatchableObjectInt(EPEARL_WATCHER));
		compound.setInteger(NBT_CHEST,  player.getDataWatcher().getWatchableObjectInt(ECHEST_WATCHER));
	}

	public static void clonePlayerData(EntityPlayer original, EntityPlayer newPlayer)
	{
		PlayerPersistProperty.get(newPlayer).setInvoCrafting(PlayerPersistProperty.get(original).getInvoCrafting());
		PlayerPersistProperty.get(newPlayer).setInvoEChest(PlayerPersistProperty.get(original).getInvoEChest());
		PlayerPersistProperty.get(newPlayer).setInvoEPearl(PlayerPersistProperty.get(original).getInvoEPearl());
	}
}
