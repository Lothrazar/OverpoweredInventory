package com.lothrazar.powerinventory;
 
/** 
 * @author Sam Bassett aka Lothrazar
 */
public class Const
{ 
	public static final int square = 18;
	public static final String NBT_SLOT = "Slot";

	public final static int hotbarSize = 9;
	public final static int armorSize = 4;
 
	public final static int MORE_ROWS = 12;
	public final static int MORE_COLS = 16;
	public final static int ALL_COLS = 9 + MORE_COLS;
	public final static int ALL_ROWS = 3 + MORE_ROWS;
	public final static int invoSize  = ALL_COLS * ALL_ROWS;

    public static final int enderPearlSlot = 388;//388 = ModSettings.invoSize+hotbarSize+armorSize
    public static final int enderChestSlot = enderPearlSlot+1;
     
	public final static int INV_ENDER = 1;
	public final static int INV_PLAYER = 2;
	
	public final static int SORT_LEFT = 1;
	public final static int SORT_RIGHT = 2;
	public final static int SORT_LEFTALL = -1;
	public final static int SORT_RIGHTALL = -2;
}
