package com.lothrazar.powerinventory;
 
/** 
 * @author Sam Bassett aka Lothrazar
 */
public class Const
{ 
    public static final String MODID = "powerinventory";
	public static final int square = 18;
	public static final String NBT_SLOT = "Slot";
    public static final String INVENTORY_TEXTURE = "textures/gui/inventory_gui.png";
	public static final String NBT_PLAYER = "Player";
	public static final String NBT_WORLD = "World";
	public static final String NBT_ID = "ID";
	public static final String NBT_Settings = "Settings";
	public static final String NBT_Unlocked = "Unlocked";
	public static final String NBT_INVENTORY = "Inventory";
	public static final String NBT_INVOSIZE = "invoSize";

	public final static int hotbarSize = 9;
	public final static int armorSize = 4;
 
	public final static int MORE_ROWS = 12;
	public final static int MORE_COLS = 16;
	public final static int ALL_COLS = 9 + MORE_COLS;
	public final static int ALL_ROWS = 3 + MORE_ROWS;
	public final static int invoSize  = ALL_COLS * ALL_ROWS+1;

    public static final int enderPearlSlot = 777;//388 = ModSettings.invoSize+hotbarSize+armorSize
    public static final int enderChestSlot = enderPearlSlot+1;
     
	public final static int INV_ENDER = 1;
	public final static int INV_PLAYER = 2;
	
	public final static int SORT_LEFT = 1;
	public final static int SORT_RIGHT = 2;
	public final static int SORT_LEFTALL = -1;
	public final static int SORT_RIGHTALL = -2;
}
