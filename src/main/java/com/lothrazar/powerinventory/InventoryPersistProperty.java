package com.lothrazar.powerinventory;

import java.util.HashMap;
import java.util.UUID;
 
import com.lothrazar.powerinventory.inventory.InventoryCustomPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class InventoryPersistProperty implements IExtendedEntityProperties
{
	// TODO: fix client server syng https://github.com/coolAlias/Tutorial-Demo/blob/eee34169652aaace077b6bf0348f44cbb3ddbd9b/src/main/java/tutorial/entity/ExtendedPlayer.java
	public static final String ID = "II_BIG_INVO";
	private int craftingOpen = 0;
	public static final int CRAFTING_WATCHER = 20;
	private EntityPlayer player;
	private EntityPlayer prevPlayer;
	public final InventoryCustomPlayer inventory = new InventoryCustomPlayer();

	/**
	 * Keep inventory doesn't work with extended inventories so we store it here upon death to reload later
	 */
	public static HashMap<UUID, NBTTagList> keepInvoCache = new HashMap<UUID, NBTTagList>();
	
	public static void register(EntityPlayer player)
	{
		player.registerExtendedProperties(ID, new InventoryPersistProperty(player));
	}
	
	public static InventoryPersistProperty get(EntityPlayer player)
	{
		IExtendedEntityProperties property = player.getExtendedProperties(ID);
		
		if(property != null && property instanceof InventoryPersistProperty)
		{
			return (InventoryPersistProperty)property;
		} else
		{
			return null;
		}
	}
	
	public InventoryPersistProperty(EntityPlayer player)
	{
		this.player = player;
		this.prevPlayer = null;
		this.player.getDataWatcher().addObject(CRAFTING_WATCHER, this.craftingOpen);
	}
	
	public void onJoinWorld()
	{
		if(prevPlayer != null)
		{
			player.inventory.readFromNBT(prevPlayer.inventory.writeToNBT(new NBTTagList()));
			this.prevPlayer = null;
		}
		
		if(!player.worldObj.isRemote)
		{
			if(player.isEntityAlive() && keepInvoCache.containsKey(player.getUniqueID()))
			{
				player.inventory.readFromNBT(keepInvoCache.get(player.getUniqueID()));
				keepInvoCache.remove(player.getUniqueID());
			} else if(!player.isEntityAlive() && player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory") && !keepInvoCache.containsKey(player.getUniqueID()))
			{
				keepInvoCache.put(player.getUniqueID(), player.inventory.writeToNBT(new NBTTagList()));
			}
		}
	}
	
	@Override
	public void init(Entity entity, World world)
	{
		if(entity instanceof EntityPlayer)
		{
			if(player != null && player != entity) // This will return true if the entity is being cloned
			{
				prevPlayer = player; // Store the previous player for later when the new one is spawned in the world
			}
			
			this.player = (EntityPlayer)entity;
		}
	}

	public void setInvoCrafting(boolean c){
		int val = c?1:0;
		player.getDataWatcher().updateObject(CRAFTING_WATCHER,val);
	}
	public boolean hasInvoCrafting(){
		return player.getDataWatcher().getWatchableObjectInt(CRAFTING_WATCHER)==1;
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		//craftingOpen = compound.getBoolean("crafting");
		player.getDataWatcher().updateObject(CRAFTING_WATCHER, compound.getInteger("crafting"));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		this.inventory.writeToNBT(compound);
		compound.setInteger("crafting",  player.getDataWatcher().getWatchableObjectInt(CRAFTING_WATCHER));
	}
}
