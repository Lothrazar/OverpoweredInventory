package com.lothrazar.powerinventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import com.lothrazar.powerinventory.inventory.BigContainerPlayer;
import com.lothrazar.powerinventory.inventory.BigInventoryPlayer;
import com.lothrazar.powerinventory.inventory.InventoryCustomPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class InventoryPersistProperty implements IExtendedEntityProperties
{
	public static final String ID = "II_BIG_INVO";
	
	EntityPlayer player;
	EntityPlayer prevPlayer;

	private static final int WATCHER_POTIONS = 22;
	private static final String NBT_POTIONS = "samSpell"; 
	private ArrayList<NBTTagCompound> potions;
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
	}
	
	public void onJoinWorld()
	{
		if(ModConfig.enableCompatMode == false)
			if(!(player.inventory instanceof BigInventoryPlayer))
			{
				player.inventory = new BigInventoryPlayer(player);
				player.inventoryContainer = new BigContainerPlayer((BigInventoryPlayer)player.inventory, !player.worldObj.isRemote, player);
				player.openContainer = player.inventoryContainer;
			}
		
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
	public void loadNBTData(NBTTagCompound compound)
	{
		this.inventory.readFromNBT(compound);
		
		if(ModConfig.enableCompatMode == false)
			if(!(player.inventory instanceof BigInventoryPlayer))
			{
				player.inventory = new BigInventoryPlayer(player);
				player.inventoryContainer = new BigContainerPlayer((BigInventoryPlayer)player.inventory, !player.worldObj.isRemote, player);
				player.openContainer = player.inventoryContainer;
				((BigInventoryPlayer)player.inventory).readFromNBT(compound.getTagList(Const.NBT_INVENTORY, 10));
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

	public final InventoryCustomPlayer inventory = new InventoryCustomPlayer();
	
	@Override
	public void saveNBTData(NBTTagCompound properties) 
	{
		this.inventory.writeToNBT(properties);
		
	}

	public void savePotionEffects(EntityPlayer p)
	{
		// Collection collection = p.getActivePotionEffects();
		//ArrayList<PotionEffect> active = (ArrayList<PotionEffect>)p.getActivePotionEffects();
        PotionEffect potioneffect;
        Iterator iterator = p.getActivePotionEffects().iterator();
        
       // ArrayList<String> encodedPotions = new ArrayList<String> ();
        //String s;
        
        potions = new ArrayList<NBTTagCompound>();
        NBTTagCompound tags;
        
        while (iterator.hasNext())
        {
            potioneffect = (PotionEffect)iterator.next();
            
            //s = potioneffect.getPotionID() +","+potioneffect.getAmplifier()+","+potioneffect.getDuration();

            tags = new NBTTagCompound();
            potioneffect.writeCustomPotionEffectToNBT(tags);
            
            //PotionEffect.readCustomPotionEffectFromNBT(nbt);
            
           // encodedPotions.add(s);
            
            
            //System.out.println(s);
        }
	}
}
