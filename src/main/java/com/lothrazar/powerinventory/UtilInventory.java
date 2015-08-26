package com.lothrazar.powerinventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class UtilInventory 
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
						found.add(posCurrent);
					} 
				}
			}
		}
		
		return found; 
	}
/*
	public static void moveallContainerToPlayer(EntityPlayer player,Container container) //W
	{
		ItemStack source,destination;

		int cont = 0;
		for(int ip = Const.hotbarSize; ip < player.inventory.getSizeInventory() - Const.armorSize; ip++)
		{
			for(int i = cont; i < container.getInventory().size(); i++)
			{
				if(container.getSlot(i).getHasStack() == false || container.getSlot(i).getStack() == null){continue;}
			
				destination = player.inventory.getStackInSlot(ip);
				source = container.getSlot(i).getStack();
				
				if(destination == null)
				{
					player.inventory.setInventorySlotContents(ip, source);
				 
					player.inventoryContainer.getSlot(ip).putStack(source);
					player.inventoryContainer.detectAndSendChanges();
					container.getSlot(i).putStack(null);
					// okay, now we are done with source
					//start at the next one later 
					
				}
				else
				{
					if(destination.stackSize == destination.getMaxStackSize()){break;}//if its full, we are done already, break inner loop only
	 
					if(source.isItemEqual(destination)) //do we match?
					{
			//tried to find a way to share code here between this and the opposite method
						//but not there yet.. copy paste works though
						
						int room = destination.getMaxStackSize() - destination.stackSize;
						
						if(room > 0)
						{
							int toDeposit = Math.min(room, source.stackSize);
				
							//so if they have room for 52, then room for 12, and we have 55, 
							//so toDeposit is only 12 and we take that off the 55 in player invo
					 
							destination.stackSize += toDeposit;
	
							player.inventoryContainer.getSlot(ip).putStack(destination);
							
							//now decrement source
	
							if(source.stackSize - toDeposit == 0)
							{
								container.getSlot(i).putStack(null);
							}
							else
							{
								source.stackSize -= toDeposit;
								container.getSlot(i).putStack(source); 
							}
						} 
					}
				}
			}
		}
	}*/
	
	//TODO: refactor above and below to share code, code reuse, find some generic bits ?
	//they are /almost/ copy-pastes in reverse of each other
	/*
	public static void moveallPlayerToContainer(EntityPlayer player, Container container)//D
	{ 
		ItemStack source,destination;

		for(int i = 0; i < container.getInventory().size(); i++)
		{
			for(int ip = Const.hotbarSize; ip < player.inventory.getSizeInventory() - Const.armorSize; ip++)
			{
				source = player.inventory.getStackInSlot(ip);
				if(source == null){continue;}
			
				if(container.getSlot(i).getHasStack() == false && //its empty, dump away
						container.getSlot(i).isItemValid(source)) //intended to validate furnace/brewing slot rules
				{ 
					container.getSlot(i).putStack(source);


					player.inventory.setInventorySlotContents(ip, null);//and remove it from player inventory
				}
				else
				{ 
					destination = container.getSlot(i).getStack();
					if(destination.stackSize == destination.getMaxStackSize()) {continue;}
					 
					if(source.isItemEqual(destination))//here.getItem() == splayer.getItem() && here.getMetadata() == splayer.getMetadata())
					{
						//so i have a non-empty, non-full stack, and a matching stack in player inventory
						
						int room = destination.getMaxStackSize() - destination.stackSize;
						
						if(room > 0)
						{
							int toDeposit = Math.min(room, source.stackSize);
				
							//so if they have room for 52, then room for 12, and we have 55, 
							//so toDeposit is only 12 and we take that off the 55 in player invo
					 
					 		destination.stackSize += toDeposit;
							container.getSlot(i).putStack(destination);
					 		//
	
							if(source.stackSize - toDeposit == 0)
							{ 
								player.inventory.setInventorySlotContents(ip, null);
							}
							else
							{
								source.stackSize -= toDeposit;
								player.inventory.setInventorySlotContents(ip, source);
							}
						}
					} 
				}
			}
		}	
	}*/
	public static void dumpFromPlayerToChestEntity(World world, TileEntityChest chest, EntityPlayer player)
  	{ 

		ItemStack chestItem;
		ItemStack invItem;
	 
		int START_CHEST = 0; 
		int END_CHEST =  START_CHEST + 3*9; 
		
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++)
		{ 
			chestItem = chest.getStackInSlot(islotChest);
		
			if(chestItem != null) {   continue; }//  chest slot not empty, skip over it
	 
			for(int islotInv = Const.hotbarSize; islotInv < player.inventory.getSizeInventory() - Const.armorSize; islotInv++)
			{
				invItem = player.inventory.getStackInSlot(islotInv);
				
				if(invItem == null)  {continue;}//empty inventory slot
				  
				chest.setInventorySlotContents(islotChest, invItem);
 
  				player.inventory.setInventorySlotContents(islotInv,null); 
  				break;
  			}//close loop on player inventory items 
		}//close loop on chest items
  	}
	
	public static void sortFromPlayerToChestEntity(World world, TileEntityChest chest, EntityPlayer player)
  	{ 
		//source: https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84
		
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
			 
			for(int islotInv = Const.hotbarSize; islotInv < player.inventory.getSizeInventory() - Const.armorSize; islotInv++)
			{
			 
				invItem = player.inventory.getStackInSlot(islotInv);
				
				if(invItem == null)  {continue;}//empty inventory slot
		 
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

  					if(invItem.stackSize <= 0)//because of calculations above, should not be below zero
  					{
  						//item stacks with zero count do not destroy themselves, they show up and have unexpected behavior in game so set to empty
  						player.inventory.setInventorySlotContents(islotInv,null); 
  					}
  					else
  					{
  						//set to new quantity
  	  					player.inventory.setInventorySlotContents(islotInv, invItem); 
  					} 
  				}//end if items match   
  			}//close loop on player inventory items 
		}//close loop on chest items
  	}
	

	public static void shiftRightAll(InventoryPlayer invo)
	{
		Queue<Integer> empty = new LinkedList<Integer>();

		ItemStack item;
		
		for(int i = invo.getSizeInventory() - (Const.armorSize + 1); i >= Const.hotbarSize;i--)
		{
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				empty.add(i);
			}
			else
			{
				//find an empty spot for it
				if(empty.size() > 0 && empty.peek() > i)
				{
					//poll remove it since its not empty anymore
					moveFromTo(invo,i,empty.poll());
					empty.add(i);
				}
			}
		}
	}
	
	public static void shiftLeftAll(InventoryPlayer invo)
	{
		Queue<Integer> empty = new LinkedList<Integer>();

		ItemStack item;
		
		for(int i = Const.hotbarSize; i < invo.getSizeInventory() - Const.armorSize;i++)
		{
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				empty.add(i);
			}
			else  //find an empty spot for it
			{
				if(empty.size() > 0 && empty.peek() < i)
				{
					//poll remove it since its not empty anymore
					moveFromTo(invo,i,empty.poll());
					empty.add(i);
				}
			}
		}
	}
	/**
	 * WARNING: it assumes that 'to' is already empty, and overwrites it.  sets 'from' to empty for you
	 * @param invo
	 * @param from
	 * @param to
	 */
	public static void moveFromTo(InventoryPlayer invo,int from, int to)
	{
		invo.setInventorySlotContents(to, invo.getStackInSlot(from));
		invo.setInventorySlotContents(from, null);
	}
	
	public static void shiftRightOne(InventoryPlayer invo) 
	{
		int iEmpty = -1;
		ItemStack item = null;
		//0 to 8 is crafting
		//armor is 384-387
		for(int i = invo.getSizeInventory() - (Const.armorSize + 1); i >= Const.hotbarSize;i--)//388-4 384
		{
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				iEmpty = i;
			}
			else if(iEmpty > 0) //move i into iEmpty
			{
				moveFromTo(invo,i,iEmpty);
				
				iEmpty = i;					
			 
			}//else keep looking
		}
	}
	
	public static void shiftLeftOne(InventoryPlayer invo) 
	{
		int iEmpty = -1;
		ItemStack item = null;

		for(int i = Const.hotbarSize; i < invo.getSizeInventory() - Const.armorSize;i++)
		{ 
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				iEmpty = i;
			}
			else if(iEmpty > 0)
			{ 
				moveFromTo(invo,i,iEmpty);
				
				iEmpty = i;		
			}
		}
	}
final static String NBT_SORT = Const.MODID+"_sort";
final static int SORT_ALPH = 0;
final static int SORT_CLASS = 1;
	private static int getNextSort(EntityPlayer p)
	{
		int prev = p.getEntityData().getInteger(NBT_SORT);
		
		int n = prev+1;
		
		if(n>=2)n=0;
		
		 p.getEntityData().setInteger(NBT_SORT,n);
		 
		return n;
	}
	public static void sort(InventoryPlayer invo) 
	{
		
		
		//int sortType = invo.player.worldObj.rand.nextInt(2);
		
		int sortType = getNextSort(invo.player);
		System.out.println("sorttype = "+sortType);
		int iSize =  invo.getSizeInventory() - Const.armorSize;

		Map<String,SortGroup> unames = new HashMap<String,SortGroup>();

		ItemStack item = null;
		SortGroup temp;
		String key = "";
		
		for(int i = Const.hotbarSize; i < iSize;i++)
		{ 
			item = invo.getStackInSlot(i);
			if(item == null){continue;}
			
			if(sortType == SORT_ALPH)			
				key = item.getItem().getClass().getName()+item.getUnlocalizedName() + item.getItemDamage();
			else if(sortType == SORT_CLASS)
				key = item.getDisplayName()+ item.getItemDamage();
				
			if(item != null)
			{
				temp = unames.get(key);
				if(temp == null) {temp = new SortGroup(key);}
				
				if(temp.stacks.size()>0)
				{
					//try to merge with top
					ItemStack top = temp.stacks.remove(temp.stacks.size()-1);
			
					int room = top.getMaxStackSize() - top.stackSize;
					
					if(room > 0)
					{
						int moveover = Math.min(item.stackSize,room);
						
						top.stackSize += moveover;
						
						item.stackSize -= moveover;
						
						if(item.stackSize == 0) 
						{
							item = null;
							invo.setInventorySlotContents(i, item);
						}
					}
					
					 temp.stacks.add(top);
					
				}
				
				if(item != null)
					temp.add(item);
				
				unames.put(key,temp);
			}
		}

		//http://stackoverflow.com/questions/780541/how-to-sort-a-hashmap-in-java
	 
		ArrayList<SortGroup> sorted = new ArrayList<SortGroup>(unames.values());
		Collections.sort(sorted, new Comparator<SortGroup>() 
		{
	        public int compare(SortGroup o1, SortGroup o2) 
	        {
	            return o1.key.compareTo(o2.key);
	        }
	    });

		int k = Const.hotbarSize;
		for (SortGroup p : sorted) 
		{
			//System.out.println(p.key+" "+p.stacks.size());
			
			for(int i = 0; i < p.stacks.size(); i++)
			{
				invo.setInventorySlotContents(k, null);
				invo.setInventorySlotContents(k, p.stacks.get(i));
				k++;
			}
		}

		for(int j = k; j < iSize; j++)
		{
			invo.setInventorySlotContents(j, null);
		}
	}
	
}
