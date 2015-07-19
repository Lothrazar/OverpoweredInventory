package com.lothrazar.powerinventory.proxy;

import com.lothrazar.powerinventory.*;
import com.lothrazar.powerinventory.inventory.BigInventoryPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class InvoPacket implements IMessage
{
	public static final int ID = 0;
	NBTTagCompound tags = new NBTTagCompound();
	
	public InvoPacket()
	{
	}
	
	public InvoPacket(NBTTagCompound tags)
	{
		this.tags = tags;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, tags);
	}
	
	public static class HandleServer implements IMessageHandler<InvoPacket,IMessage>
	{
		//@SuppressWarnings("unchecked")
		@Override
		public IMessage onMessage(InvoPacket message, MessageContext ctx)
		{
			if(message.tags.hasKey(ModInv.NBT_ID))
			{
				if(message.tags.getInteger(ModInv.NBT_ID) == 0)
				{
					WorldServer world = MinecraftServer.getServer().worldServerForDimension(message.tags.getInteger("World"));
					
					if(world == null)
					{
						return null;
					}
					
					EntityPlayer player = world.getPlayerEntityByName(message.tags.getString("Player"));
					
					if(player == null)
					{
						return null;
					}

					int unlocked = player.getEntityData().getInteger("INFINITE_INVO_UNLOCKED");
		
		 
					unlocked++;
					player.getEntityData().setInteger("INFINITE_INVO_UNLOCKED", unlocked);
					
					EventHandler.unlockCache.put(player.getName(), unlocked);//player.getCommandSenderName()
			
					NBTTagCompound replyTags = new NBTTagCompound();
					replyTags.setInteger(ModInv.NBT_ID, 0);
					replyTags.setString("Player", player.getName());//player.getCommandSenderName()
					replyTags.setInteger("Unlocked", unlocked);
					return new InvoPacket(replyTags);
				 
				} 
				else if(message.tags.getInteger(ModInv.NBT_ID) == 1) // Requesting what slots have been unlocked and the server side settings
				{
					WorldServer world = MinecraftServer.getServer().worldServerForDimension(message.tags.getInteger("World"));
					
					if(world == null)
					{
						ModInv.logger.log(Level.WARN, "Unlock Sync Failed! Unabled to locate dimension " + message.tags.getInteger("World"));
						return null;
					}
					
					EntityPlayer player = world.getPlayerEntityByName(message.tags.getString("Player"));
					
					if(player == null || player.getEntityData() == null)
					{
						ModInv.logger.log(Level.WARN, "Unlock Sync Failed! Unabled to get data for player '" + message.tags.getString("Player") + "'");
						return null;
					}
					
					int unlocked = 0;
					
					if(!player.getEntityData().hasKey("INFINITE_INVO_UNLOCKED") && (EventHandler.unlockCache.containsKey(player.getName()) || EventHandler.unlockCache.containsKey(player.getUniqueID().toString())))
					{
						unlocked = EventHandler.unlockCache.containsKey(player.getName())? EventHandler.unlockCache.get(player.getName()) : EventHandler.unlockCache.get(player.getUniqueID().toString());
						if(EventHandler.unlockCache.containsKey(player.getName()))
						{
							EventHandler.unlockCache.put(player.getUniqueID().toString(), unlocked);
							EventHandler.unlockCache.remove(player.getName());
						}
						player.getEntityData().setInteger("INFINITE_INVO_UNLOCKED", unlocked);
					} else
					{
						unlocked = player.getEntityData().getInteger("INFINITE_INVO_UNLOCKED");
						EventHandler.unlockCache.put(player.getUniqueID().toString(), unlocked);
					}
					
		
					NBTTagCompound cachedSettings = new NBTTagCompound();
					cachedSettings.setInteger("invoSize", ModSettings.invoSize);
					NBTTagCompound reply = new NBTTagCompound();
					reply.setInteger(ModInv.NBT_ID, 0);
					reply.setString("Player", player.getName());
					reply.setInteger("Unlocked", unlocked);
					reply.setTag("Settings", cachedSettings);
					
					return new InvoPacket(reply); 
				} else if(message.tags.getInteger(ModInv.NBT_ID) == 2) // Experimental
				{
					WorldServer world = MinecraftServer.getServer().worldServerForDimension(message.tags.getInteger("World"));
					
					if(world == null)
					{
						ModInv.logger.log(Level.ERROR, "Inventory Sync Failed! Unabled to locate dimension " + message.tags.getInteger("World"));
						return null;
					}
					
					EntityPlayer player = world.getPlayerEntityByName(message.tags.getString("Player"));
					int scrollPos = message.tags.getInteger("Scroll");
					int[] indexes = message.tags.getIntArray("Indexes");
					int[] numbers = message.tags.getIntArray("Numbers");
					int conID = message.tags.getInteger("Container ID");
					
					if(player == null || player.getEntityData() == null)
					{
						ModInv.logger.log(Level.ERROR, "Inventory Sync Failed! Unabled to get data for player '" + message.tags.getString("Player") + "'");
						return null;
					}
					
					Container container = player.openContainer;
					
					if(container == null)
					{
						ModInv.logger.log(Level.ERROR, "Inventory Sync Failed! No container open on server!");
						return null;
					} else if(container.windowId != conID)
					{
						ModInv.logger.log(Level.ERROR, "Inventory Sync Failed! Container ID mismatch (Client: " + conID + ", Server: " + container.windowId + ")");
						return null;
					}
					
					if(container.inventorySlots.size() < numbers.length)
					{
						ModInv.logger.log(Level.ERROR, "Inventory Sync Failed! Only found " + container.inventorySlots.size() + " / " + numbers.length + " requested slots");
						return null;
					}
					
					boolean flag = true;
					
					for(int i = 0; i < numbers.length; i++)
					{
						int sNum = numbers[i];
						int sInx = indexes[i];
						
						Slot s = (Slot)container.inventorySlots.get(sNum);
						
						if(s.inventory instanceof BigInventoryPlayer) // Not 100% necessary anymore but here as a fail safe
						{
							/*
							if(s.getClass() != Slot.class && s.getClass() != SlotLockable.class)
							{
								InfiniteInvo.logger.log(Level.WARN, "Container " + container.getClass().getSimpleName() + " is not supported by InfiniteInvo! Reason: Custom Slots (" + s.getClass().getSimpleName() + ") are being used!");
								return null;
							} else if(!(s instanceof SlotLockable))
							{
								Slot r = new SlotLockable(s.inventory, sInx + (scrollPos * 9), s.xDisplayPosition, s.yDisplayPosition);
								
								// Replace the local slot with our own tweaked one so that locked slots are handled properly
								container.inventorySlots.set(sNum, r);
								r.slotNumber = s.slotNumber;
								s = r;
								// Update the item stack listing.
								container.inventoryItemStacks.set(sNum, r.getStack());
								r.onSlotChanged();
							} else
							{
								((SlotLockable)s).slotIndex = sInx + (scrollPos * 9);
							}*/
							//instead aof above do this
							s.slotNumber = sInx + (scrollPos * 9);
							s.onSlotChanged();
							//s.putStack(new ItemStack(Blocks.stone, s.getSlotIndex())); // Debugging to visualise the location and slot indexes serverside
							
							if(flag && container.getSlotFromInventory(player.inventory, player.inventory.currentItem) == null)
							{
								flag = false;
								ModInv.logger.log(Level.WARN, "Slot broke at index " + s.getSlotIndex() + "(Scroll: " + scrollPos + ", Pass: " + i + "/" + numbers.length + ")");
							}
						}
					}
					
					container.detectAndSendChanges();
				}
			}
			return null;
		}
	}
	

	public static class HandleClient implements IMessageHandler<InvoPacket,IMessage>
	{
		@Override
		public IMessage onMessage(InvoPacket message, MessageContext ctx)
		{
			if(message.tags.hasKey(ModInv.NBT_ID))
			{
				if(message.tags.getInteger(ModInv.NBT_ID) == 0)
				{
					EntityPlayer player = Minecraft.getMinecraft().thePlayer;
					
					if(!message.tags.hasKey(ModInv.NBT_PLAYER) || !message.tags.getString(ModInv.NBT_PLAYER).equals(player.getName()))
					{
						ModInv.logger.log(Level.ERROR, "Server sent packet to the wrong player! Intended target: " + message.tags.getString(ModInv.NBT_PLAYER) + ", Recipient: " + player.getName());
						return null;
					}
					
					if(message.tags.hasKey(ModInv.NBT_Unlocked))
					{
						ModInv.logger.log(Level.INFO, "Loading serverside unlocks...");
						player.getEntityData().setInteger("INFINITE_INVO_UNLOCKED", message.tags.getInteger(ModInv.NBT_Unlocked));
					}
					
					if(message.tags.hasKey(ModInv.NBT_Settings))
					{
						ModInv.logger.log(Level.INFO, "Loading serverside settings...");
						ModSettings.LoadFromTags(message.tags.getCompoundTag(ModInv.NBT_Settings));
					}
				}
			}
			return null;
		}
		
	}
}
