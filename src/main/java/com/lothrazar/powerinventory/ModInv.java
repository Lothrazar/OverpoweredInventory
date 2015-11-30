package com.lothrazar.powerinventory;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.lothrazar.powerinventory.proxy.CommonProxy; 
import com.lothrazar.powerinventory.net.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent; 
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Initially Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 * before later being merged into my main project
 */
@Mod(modid = Const.MODID, useMetadata=true, canBeDeactivated=false
		,  guiFactory ="com.lothrazar."+Const.MODID+".IngameConfigHandler")
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
    	network.registerMessage(UncButtonPacket.class,   UncButtonPacket.class,   packetID++, Side.SERVER);
    	network.registerMessage(HotbarSwapPacket.class,  HotbarSwapPacket.class,  packetID++, Side.SERVER);
        
    	proxy.registerHandlers();
    } 
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	if(ModConfig.enableCompatMode)
    	{
   	 		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    	}
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
    		to64.add(Items.spruce_door);
    		to64.add(Items.birch_door);
    		to64.add(Items.jungle_door);
    		to64.add(Items.oak_door);
    		to64.add(Items.dark_oak_door);
    		to64.add(Items.acacia_door);
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
    		to64.add(Items.rabbit_stew);
    	} 
    	
		for(Item item : to64)
		{
			item.setMaxStackSize(64);
		}
    }
}
