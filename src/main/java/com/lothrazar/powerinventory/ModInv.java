package com.lothrazar.powerinventory;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.lothrazar.powerinventory.proxy.CommonProxy; 
import com.lothrazar.powerinventory.proxy.DumpButtonPacket;
import com.lothrazar.powerinventory.proxy.OpenInventoryPacket;
import com.lothrazar.powerinventory.proxy.ExpButtonPacket;
import com.lothrazar.powerinventory.proxy.FilterButtonPacket;
import com.lothrazar.powerinventory.proxy.EnderPearlPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket; 
import com.lothrazar.powerinventory.proxy.UncButtonPacket;
import com.lothrazar.powerinventory.standalone.GuiHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Initially Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 * before later being merged into my main project
 */
@Mod(modid = Const.MODID, useMetadata=true)
public class ModInv
{
	//??POSSIBLE additions? 
	
	//a back button in inventory. shows only IF we use I, then back goes back into where we were??
	
	// on item pickup (pearl/chest) put it in the special slot by default-if possible
	//shift click out of hotbar should go directly to special slots, work same way as armor
	// left/right buttons could merge stacks ? OR add a middle button that does some sort of merge/sort?

	 
	//idea: liquid//potion storage? bucket slot, fillButton, drainButton, and a # showing whats stored (leaves empty behind)
		//but only one type of lq at a time, and have a max /64
	
	//Display exact exp numbers? such as  450/5200 = for next level (90453= total)  
 
	@Instance(Const.MODID)
	public static ModInv instance;
	
	@SidedProxy(clientSide = "com.lothrazar.powerinventory.proxy.ClientProxy", serverSide = "com.lothrazar.powerinventory.proxy.CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network ;
	public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    	 
    	ModConfig.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));

    	
    	int packetID = 0;
    	network.registerMessage(OpenInventoryPacket.class,  OpenInventoryPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(SortButtonPacket.class,  SortButtonPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(FilterButtonPacket.class,FilterButtonPacket.class,packetID++, Side.SERVER);
    	network.registerMessage(EnderPearlPacket.class,  EnderPearlPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(ExpButtonPacket.class,   ExpButtonPacket.class,   packetID++, Side.SERVER);
    	network.registerMessage(DumpButtonPacket.class,  DumpButtonPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(UncButtonPacket.class,   UncButtonPacket.class,  packetID++, Side.SERVER);
    	
    	proxy.registerHandlers();
    }
    
    static boolean sentVersionMessage = false;//only send it once
    static VersionChecker versionChecker ;
    

    @EventHandler
    public void postInit(FMLPostInitializationEvent  event)
    {
    	if(ModConfig.blockVersionChecker == false)
    	{
	    	versionChecker = new VersionChecker();
	    	Thread versionCheckThread = new Thread(versionChecker, "Version Check");
	    	versionCheckThread.start();
    	}
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	//if comp
    	if(ModConfig.enableCompatMode)
   	 		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
   	 	
    	ArrayList<Item> to64 = new ArrayList<Item>();
    	if(ModConfig.enderPearl64)
    	{
    		to64.add(Items.ender_pearl);
    	}
    	if(ModConfig.minecart64)
    	{
    		to64.add(Items.minecart);
    		to64.add(Items.tnt_minecart);
    		to64.add(Items.chest_minecart);
    		to64.add(Items.furnace_minecart);
    		to64.add(Items.hopper_minecart);
    		to64.add(Items.command_block_minecart);
    	}
    	if(ModConfig.boat64)
    	{
    		to64.add(Items.boat);
    	}
    	if(ModConfig.doors64)
    	{
    		to64.add(Items.iron_door);
    		to64.add(Items.wooden_door);
    	}
    	if(ModConfig.snowballs64)
    	{
    		to64.add(Items.snowball);
    	}
    	if(ModConfig.bucket64)
    	{
    		to64.add(Items.bucket);
    	}
    	if(ModConfig.food64)
    	{
    		to64.add(Items.egg);
    		to64.add(Items.cake);
    		to64.add(Items.cookie);
    		to64.add(Items.mushroom_stew);
    	} 
    	
		for(Item item : to64)
		{
			item.setMaxStackSize(64);
		}
    }
}
