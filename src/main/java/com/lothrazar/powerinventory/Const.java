package com.lothrazar.powerinventory;

import net.minecraft.client.renderer.Tessellator;
 
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
	
	
	
	
	
	
	

	public final static int padding = 6;
	
	
	
	public static int pearlX = 80; 
	public static int pearlY = 8; 
	public static int compassX = pearlX;
	public static int compassY = pearlY + Const.square;
	public static int clockX = pearlX;
	public static int clockY = pearlY + 2*Const.square;
	public static int echestX = pearlX;
	public static int echestY = pearlY + 3*Const.square; 
	public static int bottleX = Const.texture_width - Const.square - padding - 1;
	public static int bottleY = 20 + 2 * Const.square; 
	public static int uncraftX = bottleX;
	public static int uncraftY = bottleY - 24;
	
	
	
	public static void drawTexturedQuadFit(double x, double y, double width, double height)
	{
		double zLevel = 0;
		//because the vanilla code REQUIRES textures to be powers of two AND are force dto be max of 256??? WHAT?
		//so this one actually works
		//THANKS hydroflame  ON FORUMS 
		//http://www.minecraftforge.net/forum/index.php/topic,11229.msg57594.html#msg57594
		
		Tessellator tessellator = Tessellator.instance;
  
		//WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		tessellator.startDrawingQuads();
        
		tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
		tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
	}
}
