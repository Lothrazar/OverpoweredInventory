package com.lothrazar.powerinventory;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
//import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.logging.log4j.Level;

import com.lothrazar.powerinventory.inventory.InventoryPersistProperty;
import com.lothrazar.powerinventory.inventory.client.GuiBigInventory;
import com.lothrazar.powerinventory.inventory.client.GuiButtonClose; 
import com.lothrazar.powerinventory.inventory.client.GuiButtonOpenInventory; 
import com.lothrazar.powerinventory.inventory.client.GuiButtonSort;
import com.lothrazar.powerinventory.proxy.ClientProxy;
import com.lothrazar.powerinventory.proxy.EnderChestPacket;
import com.lothrazar.powerinventory.proxy.EnderPearlPacket; 

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class EventHandler
{
	public static File worldDir;
	public static HashMap<String, Integer> unlockCache = new HashMap<String, Integer>();
	public static HashMap<String, Container> lastOpened = new HashMap<String, Container>();
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) 
    {   
	
        if(ClientProxy.keyEnderpearl.isPressed() )
        { 	     
        	 ModInv.instance.network.sendToServer( new EnderPearlPacket());   
        }  
        if(ClientProxy.keyEnderchest.isPressed())
        { 	     
        	
        	 ModInv.instance.network.sendToServer( new EnderChestPacket());   
        }  
    }
	
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
		}
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event)
	{
		if(event.gui == null){return;}//probably doesnt ever happen

		if(ModConfig.showCornerButtons)
		{
			if(event.gui instanceof net.minecraft.client.gui.inventory.GuiChest || 
			   event.gui instanceof net.minecraft.client.gui.inventory.GuiDispenser || 
			   event.gui instanceof net.minecraft.client.gui.inventory.GuiBrewingStand || 
			   event.gui instanceof net.minecraft.client.gui.inventory.GuiBeacon || 
			   event.gui instanceof net.minecraft.client.gui.inventory.GuiCrafting || 
			   event.gui instanceof net.minecraft.client.gui.inventory.GuiFurnace || 
			   event.gui instanceof net.minecraft.client.gui.inventory.GuiScreenHorseInventory
			   )
			{
				int button_id = 256;
				//trapped, regular chests, minecart chests, and enderchest all use this class
				//which extends  GuiContainer
	
				int padding = 10;
				
				int x,y = padding,w = 20,h = w;
				
				x = Minecraft.getMinecraft().displayWidth/2 - w - padding;//align to right side
				
				event.buttonList.add(new GuiButtonClose(button_id++, x,y,w,h));
				
				x = x - padding - w;
				event.buttonList.add(new GuiButtonOpenInventory(button_id++, x,y,w,h,"E",Const.INV_PLAYER));
				
				x = Minecraft.getMinecraft().displayWidth/2 - w - padding;//align to right side
				
				y += h + padding;
				
				event.buttonList.add(new GuiButtonSort(Minecraft.getMinecraft().thePlayer,button_id++, x, y ,w,h, Const.SORT_RIGHT,">",true));
				x = x - padding - w;
				
				event.buttonList.add(new GuiButtonSort(Minecraft.getMinecraft().thePlayer,button_id++, x, y ,w,h, Const.SORT_LEFT,"<",true));
				
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(!event.world.isRemote && worldDir == null && MinecraftServer.getServer().isServerRunning())
		{
			MinecraftServer server = MinecraftServer.getServer();
			
			if(ModInv.proxy.isClient())
			{
				worldDir = server.getFile("saves/" + server.getFolderName());
			} 
			else
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
		} 
		catch(Exception e)
		{
			ModInv.logger.log(Level.ERROR, "Failed to save slot unlock cache", e);
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
			ModInv.logger.log(Level.ERROR, "Failed to load slot unlock cache", e);
		}
	}
  
	//below was imported from my PowerApples mod
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTextOverlay(RenderGameOverlayEvent.Text event)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;  
	
		if(player.isSneaking() && 
			player.worldObj.isRemote == true)//client side only -> possibly redundant because of SideOnly
		{
			int size = 16;
			
			int xLeft = 20;
			int xRight = Minecraft.getMinecraft().displayWidth/2 - size*2;
			int yBottom = Minecraft.getMinecraft().displayHeight/2 - size*2;

			if(player.inventory.getStackInSlot(Const.clockSlot) != null)
				renderItemAt(new ItemStack(Items.clock),xLeft,yBottom,size);
			
			if(player.inventory.getStackInSlot(Const.compassSlot) != null)
				renderItemAt(new ItemStack(Items.compass),xRight,yBottom,size);
 
		}
	}
	  static ResourceLocation getResourceLocation(final String blockTexture) {
		    String domain = "minecraft";
		    String path = blockTexture;
		    final int domainSeparator = blockTexture.indexOf(':');

		    if (domainSeparator >= 0) {
		      path = blockTexture.substring(domainSeparator + 1);

		      if (domainSeparator > 1) {
		        domain = blockTexture.substring(0, domainSeparator);
		      }
		    }
		    final String resourcePath = "textures/items/" + path + ".png";  // base path and PNG are hardcoded in Minecraft
		    return new ResourceLocation(domain.toLowerCase(), resourcePath);
	  }
	@SideOnly(Side.CLIENT)
	private static void renderItemAt(ItemStack stack, int x, int y, int dim)
	{
	
		//1.7 help thanks to https://github.com/Zyin055/zyinhud/blob/26f52ca29894447bca4378ef30f551b397ab7a29/src/main/java/com/zyin/zyinhud/mods/DurabilityInfo.java
		//Minecraft.getMinecraft().getTextureManager
		//@SuppressWarnings("deprecation")
		//IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
	
		//Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureManager().getResourceLocation(
		//stack.getItemSpriteNumber()));
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);
		
		// System.out.println(stack.getItemSpriteNumber());
 System.out.println(stack.getIconIndex().getIconName()+stack.getItemSpriteNumber());//this is just like "clock" or "compass"
 //http://www.minecraftforge.net/forum/index.php?topic=24313.0
// ResourceLocation resourceLocation = getResourceLocation(stack.getIconIndex().getIconName());
 
 
 
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(
				 stack.getItem().getIconFromDamage(0).getIconName()
				// resourceLocation.getResourcePath()
				);//iBakedModel.getTexture().getIconName()
		
		renderTexture( (TextureAtlasSprite)stack.getIconIndex() , x, y, dim);
		 
		
	}
	@SideOnly(Side.CLIENT)
	public static void renderTexture( TextureAtlasSprite textureAtlasSprite , int x, int y, int dim)
	{	
		//special thanks to http://www.minecraftforge.net/forum/index.php?topic=26613.0
		
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        
		Tessellator tessellator = Tessellator.instance;//.getInstance();
	 
		int height = dim, width = dim;
		//WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(x),          (double)(y + height),  0.0, (double)textureAtlasSprite.getMinU(), (double)textureAtlasSprite.getMaxV());
		tessellator.addVertexWithUV((double)(x + width),  (double)(y + height),  0.0, (double)textureAtlasSprite.getMaxU(), (double)textureAtlasSprite.getMaxV());
		tessellator.addVertexWithUV((double)(x + width),  (double)(y),           0.0, (double)textureAtlasSprite.getMaxU(), (double)textureAtlasSprite.getMinV());
		tessellator.addVertexWithUV((double)(x),          (double)(y),           0.0, (double)textureAtlasSprite.getMinU(), (double)textureAtlasSprite.getMinV());
		tessellator.draw();
	}
}
