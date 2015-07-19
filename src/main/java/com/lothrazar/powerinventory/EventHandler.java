package com.lothrazar.powerinventory;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.logging.log4j.Level;

import com.lothrazar.powerinventory.inventory.BigContainerPlayer;
import com.lothrazar.powerinventory.inventory.InventoryPersistProperty;
import com.lothrazar.powerinventory.inventory.client.GuiBigInventory;
import com.lothrazar.powerinventory.proxy.InvoPacket;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler
{
	public static File worldDir;
	public static HashMap<String, Integer> unlockCache = new HashMap<String, Integer>();
	public static HashMap<String, Container> lastOpened = new HashMap<String, Container>();
	
	@SubscribeEvent
	public void onEntityConstruct(EntityConstructing event) // More reliable than on entity join
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			
			if(InventoryPersistProperty.get(player) == null)
			{
				InventoryPersistProperty.register(player);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			
			if(InventoryPersistProperty.get(player) != null)
			{
				InventoryPersistProperty.get(player).onJoinWorld();
			}
			
			if(event.world.isRemote)
			{
				NBTTagCompound requestTags = new NBTTagCompound();
				requestTags.setInteger(ModMutatedInventory.NBT_ID, 1);
				requestTags.setInteger(ModMutatedInventory.NBT_WORLD, event.world.provider.getDimensionId());
				requestTags.setString(ModMutatedInventory.NBT_PLAYER, player.getName());
				ModMutatedInventory.instance.network.sendToServer(new InvoPacket(requestTags));
			} else
			{
				ModSettings.LoadFromCache();
			}
		}
	}
 
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event)
	{
		if(event.entityLiving instanceof EntityPlayer)
		{
	
			if(!event.entityLiving.worldObj.isRemote && event.entityLiving.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
			{
				InventoryPersistProperty.keepInvoCache.put(event.entityLiving.getUniqueID(), ((EntityPlayer)event.entityLiving).inventory.writeToNBT(new NBTTagList()));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		if(event.gui != null && event.gui.getClass() == GuiInventory.class && !(event.gui instanceof GuiBigInventory))
		{
			event.gui = new GuiBigInventory(Minecraft.getMinecraft().thePlayer);
		} else if(event.gui == null && Minecraft.getMinecraft().thePlayer.inventoryContainer instanceof BigContainerPlayer)
		{
			// Reset scroll and inventory slot positioning to make sure it doesn't screw up later
			((BigContainerPlayer)Minecraft.getMinecraft().thePlayer.inventoryContainer).scrollPos = 0;
			((BigContainerPlayer)Minecraft.getMinecraft().thePlayer.inventoryContainer).updateScroll();
		}
	}
	/*
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event)
	{
		if(event.gui instanceof GuiBigInventory)
		{
			((GuiBigInventory)event.gui).redoButtons = true;
		} else if(event.gui instanceof GuiContainer)
		{
			GuiContainer gui = (GuiContainer)event.gui;
			Container container = gui.inventorySlots;
			
			//TODO: add buttons here when ready
			//event.buttonList.add(new xxxxxxxxxxx(256, 0, 0, 1, 1, "", container, gui));
		
		}
	}*/
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(!event.world.isRemote && worldDir == null && MinecraftServer.getServer().isServerRunning())
		{
			MinecraftServer server = MinecraftServer.getServer();
			
			if(ModMutatedInventory.proxy.isClient())
			{
				worldDir = server.getFile("saves/" + server.getFolderName());
			} else
			{
				worldDir = server.getFile(server.getFolderName());
			}

			new File(worldDir, "data/").mkdirs();
			LoadCache(new File(worldDir, "data/SlotUnlockCache"));
		}
	}
	
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event)
	{
		if(!event.world.isRemote && worldDir != null && MinecraftServer.getServer().isServerRunning())
		{
			new File(worldDir, "data/").mkdirs();
			SaveCache(new File(worldDir, "data/SlotUnlockCache"));
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if(!event.world.isRemote && worldDir != null && !MinecraftServer.getServer().isServerRunning())
		{
			new File(worldDir, "data/").mkdirs();
			SaveCache(new File(worldDir, "data/SlotUnlockCache"));
			
			worldDir = null;
			unlockCache.clear();
			InventoryPersistProperty.keepInvoCache.clear();
		}
	}
	
	public static void SaveCache(File file)
	{
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(unlockCache);
			
			oos.close();
			fos.close();
		} catch(Exception e)
		{
			ModMutatedInventory.logger.log(Level.ERROR, "Failed to save slot unlock cache", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void LoadCache(File file)
	{
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileInputStream fis = new FileInputStream(file);
			
			if(fis.available() <= 0)
			{
				fis.close();
				return;
			}
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			unlockCache = (HashMap<String,Integer>)ois.readObject();
			
			ois.close();
			fis.close();
		} catch(Exception e)
		{
			ModMutatedInventory.logger.log(Level.ERROR, "Failed to load slot unlock cache", e);
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equals(ModMutatedInventory.MODID))
		{
			ModMutatedInventory.config.save();
			//ConfigHandler.initConfigs();
		}
	}
}
