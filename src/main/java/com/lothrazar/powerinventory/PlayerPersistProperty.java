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
	public static final int CRAFTING_WATCHER = 20;
	private int craftingOpen = 0;
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
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		//craftingOpen = compound.getBoolean("crafting");
		player.getDataWatcher().updateObject(CRAFTING_WATCHER, compound.getInteger(NBT_CRAFT));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		this.inventory.writeToNBT(compound);
		compound.setInteger(NBT_CRAFT,  player.getDataWatcher().getWatchableObjectInt(CRAFTING_WATCHER));
	}

	public static void clonePlayerData(EntityPlayer original, EntityPlayer newPlayer)
	{
		PlayerPersistProperty.get(newPlayer).setInvoCrafting(PlayerPersistProperty.get(original).getInvoCrafting());
	}
}
