package com.lothrazar.powerinventory;
 
public class Const
{ 
    public static final String MODID = "powerinventory";

	public static final int SQ = 18;
	public static final int HOTBAR_SIZE = 9;
	public static final int ARMOR_SIZE = 4; 
	public static final int ROWS_VANILLA = 3; 
	public static final int COLS_VANILLA = 9; 
	public static final int V_INVO_SIZE = ROWS_VANILLA*COLS_VANILLA; //36
	public static final int CRAFTSIZE = 3;
	public static final int STORAGE_1TOPRIGHT = 1;
	public static final int STORAGE_2BOTLEFT = 2;
	public static final int STORAGE_3BOTRIGHT = 3;
	
	//pixel size of vanilla invo
	public static final int VWIDTH = 176;
	public static final int VHEIGHT = 166;
 
	//these are slot indices. different than slot numbers (important)
    public static final int SLOT_EPEARL = 77777; 
    public static final int SLOT_ECHEST = SLOT_EPEARL+1;
    

    public static final int TEXTURE_WIDTH = 342;
    public static final int TEXTURE_HEIGHT = 225;
    public static final int SLOTS_WIDTH = 162;
    public static final int SLOTS_HEIGHT = 54;// the 3x9 size
}
