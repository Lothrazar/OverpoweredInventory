package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockStorage;

public class InventoryRenderer
{

	public static final int topspace=12+Const.SQ;//space used by top half not including slots
	private static final int centerHoriz = Const.SLOTS_WIDTH/2 - GuiButtonUnlockStorage.width/2;
	private static final int centerVert = topspace - Const.SLOTS_HEIGHT/2 - GuiButtonUnlockStorage.height/2;
	
	private static final int left=7,pad=4;//pad is middle padding. left is left edge padding. for slot areas

	private static final int segsLeft = 10;//segments on the left two columns
	
	private static int rowFromSegment(int segment){
		 
		//so 1,2 are in row 1. but also 11 is in row 1 so we split it off
		if(segment <= segsLeft)
			return (int)Math.ceil(((double)segment)/2);//integer division -> rounds off so both 3,4 become 2;
		else
			return segsLeft - segment;//so 1 -> 5
	}
	private static int colFromSegment(int segment){
		 
		//first chunk alternates btw colum 1,2 back and forth
		if(segment <= segsLeft)
			if(segment % 2 == 0) 
				return 2; //starting with odd row in the top right for segment 1
			else
				return 1;
		else
			return 3;
	}
	
	// 1  2  11
	// 3  4  12
	// 5  6  13
	// 7  8  14
	// 9  10 15
	//small: segments are [1,6]
	//large: segments are [1,15]
	/*
	private static int numColumns(){
		return ModConfig.isLargeScreen() ? 3 : 2;
	}
	private static int numRows(){
		return ModConfig.isLargeScreen() ? 5 : 3;
	}*/
	
	//TODO: loop on ModConfig.getMaxSections()
	//tricky since small means 2 columns and 3 rows totalling 6
	//but large means 3 columns and 5 rows-> 15
	// so need a getRowCol setup so its like
	//OR.. they just unlock which ever one they click on again? no we cant go back to that. can we?
	//it is only 8 bits...
	
	public static int xPosBtn(int segment){
		int col = colFromSegment(segment);
		
		return (col-1)*Const.SLOTS_WIDTH + centerHoriz;
	}
	
	public static int yPosBtn(int segment){

		int row = rowFromSegment(segment);
		
		return row*Const.SLOTS_HEIGHT + centerVert;
	}
	
	public static int xPosTexture(int segment){
		//there are only 3 columns, no need to make constants
		switch(colFromSegment(segment)){
		case 1:
			return left;//column 1, so segment 1,3,5 etc
		case 2:
			return left + pad + Const.SLOTS_WIDTH; 
		case 3:
			return left + 2*(pad + Const.SLOTS_WIDTH);//always third column
		}
		return 0;
	}
	
	public static int yPosTexture(int segment){
		//TODO switch is for temp
		int row = rowFromSegment(segment);
		
		
		switch(segment){
		case 1:
			return topspace;
		case 2:
			return topspace;
		case 3:
			return topspace + pad + Const.SLOTS_HEIGHT;
		case 4:
			return topspace+pad+Const.SLOTS_HEIGHT;
		case 5:
			return topspace+2*(pad+Const.SLOTS_HEIGHT);
		case 6:
			return topspace+2*(pad+Const.SLOTS_HEIGHT);
		}
		return 0;//TODO
	}

	public static int xPosSlotsStart(int segment){
		switch(segment){
		case 1:     
			return 2*pad;
		case 2:    
			return Const.SLOTS_WIDTH + 3*pad;
		case 3:    
			return 2*pad;
		case 4:    
			return Const.SLOTS_WIDTH + 3*pad;
		case 5:    
			return 2*pad;
		case 6:
			return Const.SLOTS_WIDTH + 3*pad;
		}
		return 0;
	}

	public static int yPosSlotsStart(int segment){
		int topLimit = 13+Const.SQ;//DIFFERENT than topSpace - this is for data slot not images
		switch(segment){
		case 1:
			return topLimit;
		case 2:     
			return topLimit; 
		case 3:     
			return topLimit + Const.SLOTS_HEIGHT + pad; 
		case 4:     
			return topLimit + Const.SLOTS_HEIGHT + pad; 
		case 5:     
			return topLimit + 2*(Const.SLOTS_HEIGHT + pad);
		case 6:     
			return topLimit + 2*(Const.SLOTS_HEIGHT + pad);
		}

		return 0;
	}

	
}
