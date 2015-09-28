package com.lothrazar.powerinventory;
 
/** 
 * @author Sam Bassett aka Lothrazar
 */
public class Const
{ 
    public static final String MODID = "powerinventory";
    public static String INVENTORY_TEXTURE = "textures/gui/inventory_gui.png";
	public static int texture_width = 464;
	public static int texture_height = 382;
    
	public static final String NBT_SLOT = "Slot";
	public static final String NBT_PLAYER = "Player";
	public static final String NBT_WORLD = "World";
	public static final String NBT_ID = "ID";
	public static final String NBT_Settings = "Settings";
	public static final String NBT_Unlocked = "Unlocked";
	public static final String NBT_INVENTORY = "Inventory";
	public static final String NBT_INVOSIZE = "invoSize";

	public static final int square = 18;
	public static final int hotbarSize = 9;
	public static final int armorSize = 4; 
 
	public static int MORE_ROWS;
	public static int MORE_COLS;
	public static int ALL_COLS;
	public static int ALL_ROWS;
	public static int INVOSIZE;

	//these are slot indices. different than slot numbers (important)
    public static final int enderPearlSlot = 77777; 
    public static final int enderChestSlot = enderPearlSlot+1;
    public static final int clockSlot = enderPearlSlot+2;
    public static final int compassSlot = enderPearlSlot+3;
    public static final int bottleSlot = enderPearlSlot+4;
    public static final int uncraftSlot = enderPearlSlot+5;
    
     
	public final static int INV_ENDER = 1;
	public final static int INV_PLAYER = 2;
	public final static int INV_SOLO = 3;
	
	public final static int SORT_LEFT = 1;
	public final static int SORT_RIGHT = 2;
	public final static int SORT_SMART = 7;
	public final static int SORT_LEFTALL = -1;
	public final static int SORT_RIGHTALL = -2;
	
	
	
	
	
	
	

	final static int padding = 6;
	
	
	
	public static final int pearlX = 80; 
	public static final int pearlY = 8; 
	public static final int compassX = pearlX;
	public static final int compassY = pearlY + Const.square;
	public static final int clockX = pearlX;
	public static final int clockY = pearlY + 2*Const.square;
	public static final int echestX = pearlX;
	public static final int echestY = pearlY + 3*Const.square; 
	public static final int bottleX = Const.texture_width - Const.square - padding - 1;
	public static final int bottleY = 20 + 2 * Const.square; 
	public static final int uncraftX = bottleX;
	public static final int uncraftY = bottleY - 24;
	
}
