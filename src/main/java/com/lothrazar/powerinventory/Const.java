package com.lothrazar.powerinventory;
 
public class Const
{ 
    public static final String MODID = "powerinventory";
    //public static String INVENTORY_TEXTURE = "textures/gui/inventory_gui.png";
	//public static int texture_width = 464;
	//public static int texture_height = 382;
    
	public static final String NBT_SLOT = "Slot";
	public static final String NBT_PLAYER = "Player";
	public static final String NBT_WORLD = "World";
	public static final String NBT_ID = "ID";
	public static final String NBT_Settings = "Settings";
	public static final String NBT_Unlocked = "Unlocked";
	public static final String NBT_INVENTORY = "Inventory";
	public static final String NBT_INVOSIZE = "invoSize";

	public static final int SQ = 18;
	public static final int HOTBAR_SIZE = 9;
	public static final int ARMOR_SIZE = 4; 
	public static final int ROWS_VANILLA = 3; 
	public static final int COLS_VANILLA = 9; 
	
	//width of the black box with the character in it (with the armor slots and stuff too)
	public static final int WIDTH_CHARARMOR = 72; 
 
	//public static int INVOSIZE;

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
	
	
	
	
	
	
	

	public final static int padding = 6;
	public final static int paddingLrg = 8;
	 
	public static int bottleX;// = Const.texture_width - Const.square - padding - 1;
	public static int bottleY = 20 + 2 * Const.SQ; 
	public static int uncraftX;// = bottleX;
	public static int uncraftY;// = bottleY - 24;
	
	
}
