package com.lothrazar.powerinventory;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class UtilChestInventory 
{
	
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

	public static void depositPlayerToContainer(EntityPlayer player, Container container)
	{ 
		ItemStack stackFrom;
		
		for(int i = 0; i < container.getInventory().size(); i++)
		{
			for(int ip = ModSettings.hotbarSize; ip < player.inventory.getSizeInventory() - ModSettings.armorSize; ip++)
			{
				stackFrom = player.inventory.getStackInSlot(ip);
				if(stackFrom == null){continue;}
			
				if(container.getSlot(i).getHasStack() == false) //its empty, dump away
				{ 
					container.getSlot(i).putStack(stackFrom);
					
					player.inventory.setInventorySlotContents(ip, null);//and remove it from player inventory
				}
				else
				{ 
					if(stackFrom.stackSize == stackFrom.getMaxStackSize()) {continue;}
					
					ItemStack stackTo = container.getSlot(i).getStack();
					 
					if(stackFrom.isItemEqual(stackTo))//here.getItem() == splayer.getItem() && here.getMetadata() == splayer.getMetadata())
					{
						//so i have a non-empty, non-full stack, and a matching stack in player inventory
						
						int room = stackTo.getMaxStackSize() - stackTo.stackSize;
						if(room > 0)
						{
							int toDeposit = Math.min(room, stackFrom.stackSize);
				
							//so if they have room for 52, then room for 12, and we have 55, 
							//so toDeposit is only 12 and we take that off the 55 in player invo
					 
					 		stackTo.stackSize += toDeposit;
							container.getSlot(i).putStack(stackTo);
					 		//
	
							if(stackFrom.stackSize - toDeposit == 0)
								player.inventory.setInventorySlotContents(ip, null);
							else
							{
								stackFrom.stackSize -= toDeposit;
								player.inventory.setInventorySlotContents(ip, stackFrom);
							}
						}
					} 
				}
			}
		}	
	}
	
	public static void sortFromPlayerToChestEntity(World world, TileEntityChest chest, EntityPlayer player)
  	{ 
		//source: https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84
		
  	//	int totalItemsMoved = 0; 
  	//	int totalSlotsFreed = 0;
  		 
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
		
			if(chestItem == null) {   continue; }//  empty chest slot
			 
			//ItemStack item;
			
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

  					//totalItemsMoved += toDeposit;
  					//totalTypesMoved++;
  					
  					if(invItem.stackSize <= 0)//because of calculations above, should not be below zero
  					{
  						//item stacks with zero count do not destroy themselves, they show up and have unexpected behavior in game so set to empty
  						player.inventory.setInventorySlotContents(islotInv,null); 
  						
  					//	totalSlotsFreed++;
  					}
  					else
  					{
  						//set to new quantity
  	  					player.inventory.setInventorySlotContents(islotInv, invItem); 
  					} 
  				}//end if items match   
  			}//close loop on player inventory items 
		}//close loop on chest items
 
		//System.out.println("yes"+totalSlotsFreed);
	 
  	}
}
