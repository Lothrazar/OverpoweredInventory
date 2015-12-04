package com.lothrazar.powerinventory;

import net.minecraft.inventory.IInventory;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class UtilInventory 
{
	  
	/**
	 * WARNING: it assumes that 'to' is already empty, and overwrites it.  sets 'from' to empty for you
	 * @param invo
	 * @param from
	 * @param to
	 */
	public static void moveFromTo(IInventory invo,int from, int to)
	{
		invo.setInventorySlotContents(to, invo.getStackInSlot(from));
		invo.setInventorySlotContents(from, null);
	}
}
