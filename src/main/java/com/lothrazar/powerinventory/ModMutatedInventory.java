package com.lothrazar.powerinventory;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import com.lothrazar.powerinventory.proxy.CommonProxy;
import com.lothrazar.powerinventory.proxy.EnderButtonPacket;
import com.lothrazar.powerinventory.proxy.FilterButtonPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModMutatedInventory.MODID, useMetadata=true)
public class ModMutatedInventory
{
    public static final String MODID = "powerinventory";
	public static final String NBT_PLAYER = "Player";
	public static final String NBT_WORLD = "World";
	public static final String NBT_ID = "ID";
	public static final String NBT_Settings = "Settings";
	public static final String NBT_Unlocked = "Unlocked";

	public final static int SORT_LEFT = 1;
	public final static int SORT_RIGHT = 2;
	public final static int SORT_LEFTALL = -1;
	public final static int SORT_RIGHTALL = -2;
    //My fork of this mod was created on July 17, 2015 at https://github.com/PrinceOfAmber/InfiniteInvo
    //original mod source was https://github.com/Funwayguy/InfiniteInvo
	
	@Instance(MODID)
	public static ModMutatedInventory instance;
	
	@SidedProxy(clientSide = "com.lothrazar.powerinventory.proxy.ClientProxy", serverSide = "com.lothrazar.powerinventory.proxy.CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network ;
	public static Logger logger;
	public static Configuration config;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    	network.registerMessage(EnderButtonPacket.class, EnderButtonPacket.class, EnderButtonPacket.ID, Side.SERVER);
    	network.registerMessage(SortButtonPacket.class, SortButtonPacket.class, SortButtonPacket.ID, Side.SERVER);
    	network.registerMessage(FilterButtonPacket.class, FilterButtonPacket.class, FilterButtonPacket.ID, Side.SERVER);
    	
    	config = new Configuration(event.getSuggestedConfigurationFile(), true);
    	config.load();
    	 
    	String category = Configuration.CATEGORY_GENERAL;
		ModSettings.MORE_ROWS = config.getInt("extra_rows", category, 12, 0, 20, "How many extra rows are displayed in the inventory screen");
		ModSettings.MORE_COLS = config.getInt("extra_columns", category, 16, 0, 20, "How many extra columns are displayed in the inventory screen");
		ModSettings.filterRange = config.getInt("button_filter_range", category, 12, 1, 32, "Range of the filter button to reach nearby chests");
		
		ModSettings.fullCols = 9 + ModSettings.MORE_COLS;
		ModSettings.fullRows = 3 + ModSettings.MORE_ROWS;
		ModSettings.invoSize  = ModSettings.fullCols * ModSettings.fullRows;
		
		//(String name, String category, String defaultValue, String comment)
		ModSettings.showText = config.getBoolean("show_text",category,false,"Show or hide the 'Crafting' text in the inventory");
		ModSettings.showCharacter = config.getBoolean("show_character",category,true,"Show or hide the animated character text in the inventory");
		ModSettings.showEnderButton = config.getBoolean("button_ender_chest",category,true,"Show or hide the ender chest button");
		ModSettings.showSortButtons = config.getBoolean("button_sort",category,true,"Show or hide the ender chest button");
		ModSettings.showFilterButton = config.getBoolean("button_filter",category,true,"Show or hide the filter button");
		
		config.save();
		
		ModSettings.SaveToCache();
		
    	
    	proxy.registerHandlers();
    }
    
    

	public static ArrayList<BlockPos> findBlocks(EntityPlayer player, Block blockHunt, int RADIUS ) 
	{        
		//function imported https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Commands/src/main/java/com/lothrazar/samscommands/ModCommands.java#L193
		ArrayList<BlockPos> found = new ArrayList<BlockPos>();
		int xMin = (int) player.posX - RADIUS;
		int xMax = (int) player.posX + RADIUS;

		int yMin = (int) player.posY - RADIUS;
		int yMax = (int) player.posY + RADIUS;

		int zMin = (int) player.posZ - RADIUS;
		int zMax = (int) player.posZ + RADIUS;
		 
		//int xDistance, zDistance, distance , distanceClosest = RADIUS * RADIUS;
		
		BlockPos posCurrent = null; 
		for (int xLoop = xMin; xLoop <= xMax; xLoop++)
		{
			for (int yLoop = yMin; yLoop <= yMax; yLoop++)
			{
				for (int zLoop = zMin; zLoop <= zMax; zLoop++)
				{  
					posCurrent = new BlockPos(xLoop, yLoop, zLoop);
					if(player.worldObj.getBlockState(posCurrent).getBlock().equals(blockHunt))
					{ 
						//xDistance = (int) Math.abs(xLoop - player.posX );
						//zDistance = (int) Math.abs(zLoop - player.posZ );
						
						//distance = (int) distanceBetweenHorizontal(player.getPosition(), posCurrent);
						
						found.add(posCurrent);
					} 
				}
			}
		}
		
		return found; 
	}

	//source: https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84
	public static void sortFromPlayerToChestEntity(World world, TileEntityChest chest, EntityPlayer player)
  	{ 
  		int totalItemsMoved = 0; 
  		int totalSlotsFreed = 0;
  		 
		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;
		
		//player inventory and the small chest have the same dimensions 
		
		int START_CHEST = 0; 
		int END_CHEST =  START_CHEST + 3*9; 
		
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++)
		{ 
			chestItem = chest.getStackInSlot(islotChest);
		
			if(chestItem == null)
			{  
				continue;
			}//not an error; empty chest slot
			 
			ItemStack item;
			
			for(int islotInv = ModSettings.hotbarSize; islotInv < player.inventory.getSizeInventory() - ModSettings.armorSize; islotInv++)
			{
				//item = invo.getStackInSlot(i);
				 
				invItem = player.inventory.getStackInSlot(islotInv);
				
				if(invItem == null) 
				{ 
					continue;
			    }//empty inventory slot
		 
  				if( invItem.getItem().equals(chestItem.getItem()) && invItem.getItemDamage() ==  chestItem.getItemDamage() )
  				{  
  					//same item, including damage (block state)
  					
  					chestMax = chestItem.getItem().getItemStackLimit(chestItem);
  					room = chestMax - chestItem.stackSize;
  					 
  					if(room <= 0) {continue;} // no room, check the next spot
  			 
  					//so if i have 30 room, and 28 items, i deposit 28.
  					//or if i have 30 room and 38 items, i deposit 30
  					toDeposit = Math.min(invItem.stackSize,room);
 
  					chestItem.stackSize += toDeposit;
  					chest.setInventorySlotContents(islotChest, chestItem);

  					invItem.stackSize -= toDeposit;

  					totalItemsMoved += toDeposit;
  					//totalTypesMoved++;
  					
  					if(invItem.stackSize <= 0)//because of calculations above, should not be below zero
  					{
  						//item stacks with zero count do not destroy themselves, they show up and have unexpected behavior in game so set to empty
  						player.inventory.setInventorySlotContents(islotInv,null); 
  						
  						totalSlotsFreed++;
  					}
  					else
  					{
  						//set to new quantity
  	  					player.inventory.setInventorySlotContents(islotInv, invItem); 
  					} 
  				}//end if items match   
  			}//close loop on player inventory items 
		}//close loop on chest items
 
		System.out.println("yes"+totalSlotsFreed);
		if( totalSlotsFreed > 0) 
		{ 
			 
		//particles dont work, this only happens on server side (remote==false always)
			//SamsUtilities.spawnParticle(world,EnumParticleTypes.SLIME,chest.getPos().up()); 
		}
  	}
    
}
