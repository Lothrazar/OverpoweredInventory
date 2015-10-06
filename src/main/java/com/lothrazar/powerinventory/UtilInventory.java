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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
 
import net.minecraft.world.World;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class UtilInventory 
{
	public static ArrayList<IInventory> findTileEntityInventories(EntityPlayer player, int RADIUS ) 
	{        
		//function imported https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Commands/src/main/java/com/lothrazar/samscommands/ModCommands.java#L193
		ArrayList<IInventory> found = new ArrayList<IInventory>();
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
					if(player.worldObj.getTileEntity(xLoop, yLoop, zLoop) instanceof IInventory)
					{ 
						found.add((IInventory)player.worldObj.getTileEntity(xLoop, yLoop, zLoop));
					} 
				}
			}
		}
		
		return found; 
	}
	public static void dumpFromPlayerToIInventory(World world, IInventory inventory, EntityPlayer player)
  	{ 
		ItemStack chestItem;
		ItemStack invItem;
	 
		int start = 0; 
		int end =  inventory.getSizeInventory();//START_CHEST + 3*9; 
		
		
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int slot = start; slot < end; slot++)
		{ 
			chestItem = inventory.getStackInSlot(slot);
		
			if(chestItem != null) {   continue; }//   slot not empty, skip over it
	 
			for(int islotInv = Const.hotbarSize; islotInv < player.inventory.getSizeInventory() - Const.armorSize; islotInv++)
			{
				invItem = player.inventory.getStackInSlot(islotInv);
				
				if(invItem == null)  {continue;}//empty inventory slot
				  
				inventory.setInventorySlotContents(slot, invItem);
 
  				player.inventory.setInventorySlotContents(islotInv,null); 
  				break;
  			}//close loop on player inventory items 
		}//close loop on chest items
  	}

	public static void sortFromPlayerToInventory(World world, IInventory chest, EntityPlayer player)
  	{ 
		//source: https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84
		
		ItemStack chestItem;
		ItemStack invItem;
		int room;
		int toDeposit;
		int chestMax;
		
		//player inventory and the small chest have the same dimensions 
		
		int START_CHEST = 0; 
		int END_CHEST =  chest.getSizeInventory(); 
		
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
	/*
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
	}*/
	public static void shiftRightOne(InventoryPlayer invo) 
	{ 
		Map<Integer,ItemStack> newLocations = new HashMap<Integer,ItemStack>();	
		
		int iNew;
 
		int END = invo.getSizeInventory() - Const.armorSize;
		for(int i = Const.hotbarSize; i < END;i++)
		{ 
			 
			if(i == END-1) iNew = Const.hotbarSize;
			else iNew = i + 1;
			
			newLocations.put((Integer)iNew, invo.getStackInSlot(i));
		 
		}
		
		for (Map.Entry<Integer,ItemStack> entry : newLocations.entrySet()) 
		{ 
			invo.setInventorySlotContents(entry.getKey().intValue(),entry.getValue());
		}
		
	}
	
	public static void shiftLeftOne(InventoryPlayer invo) 
	{
		//int iEmpty = -1;
		//ItemStack item = null;
		
		Map<Integer,ItemStack> newLocations = new HashMap<Integer,ItemStack>();	
		
		int iNew;
 
		int END = invo.getSizeInventory() - Const.armorSize;
		for(int i = Const.hotbarSize; i < END;i++)
		{ 
			 
			if(i == Const.hotbarSize) iNew = END-1;
			else iNew = i - 1;
			
			newLocations.put((Integer)iNew, invo.getStackInSlot(i));
			
			//newLocations.set(iNew, invo.getStackInSlot(i));
			
			/*
			item = invo.getStackInSlot(i);
			
			if(item == null)
			{
				iEmpty = i;
			}
			else if(iEmpty > 0)
			{ 
				moveFromTo(invo,i,iEmpty);
				
				iEmpty = i;		
			}*/
		}
		
		for (Map.Entry<Integer,ItemStack> entry : newLocations.entrySet()) 
		{
		  //  System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
			invo.setInventorySlotContents(entry.getKey().intValue(),entry.getValue());
		}
	}
	
	
	
final static String NBT_SORT = Const.MODID+"_sort";
final static int SORT_ALPH = 0;
final static int SORT_ALPHI = 1;

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
		int sortType = getNextSort(invo.player);

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
				key = item.getUnlocalizedName() + item.getItemDamage();
			else if(sortType == SORT_ALPHI)			
				key = item.getItem().getClass().getName() + item.getUnlocalizedName()+ item.getItemDamage();
			//else if(sortType == SORT_CLASS)
			//	key = item.getItem().getClass().getName()+ item.getItemDamage();
				
		 
			temp = unames.get(key);
			if(temp == null) {temp = new SortGroup(key);}
			
			if(temp.stacks.size() > 0)
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
		
		
		//alternately loop by rows
		//so we start at k again, add Const.ALL_COLS to go down one row
		
		
		
	}

	public static void doSort(EntityPlayer p,int sortType)
	{
		InventoryPlayer invo = p.inventory;
		
		switch(sortType)
		{
		case Const.SORT_LEFT:
			UtilInventory.shiftLeftOne(invo);
			break;
		case Const.SORT_RIGHT:
			UtilInventory.shiftRightOne(invo);
			break;
		case Const.SORT_LEFTALL:
			UtilInventory.shiftLeftAll(invo);
			break;
		case Const.SORT_RIGHTALL:
			UtilInventory.shiftRightAll(invo);
			break;
		case Const.SORT_SMART:
			UtilInventory.sort(invo);
			break;
		}
 
		return ;
	}
}
