package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockStorage;

public class InventoryRenderer
{

	public static final int topspace=12+Const.SQ;//space used by top half not including slots
	private static final int centerHoriz = Const.SLOTS_WIDTH/2 - GuiButtonUnlockStorage.width/2;
	private static final int centerVert = topspace - Const.SLOTS_HEIGHT/2 - GuiButtonUnlockStorage.height/2;
	
	private static final int left=7,pad=4;//pad is middle padding. left is left edge padding. for slot areas
	
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
	// 1 2 11
	// 3 4 12
	// 5 6 13
	// 7 8 14
	// 9 10 15
	//OR.. they just unlock which ever one they click on again? no we cant go back to that. can we?
	//it is only 8 bits...
	
	public static int xPosBtn(int segment){

		if(segment <= 8){//stay in the first two columns, both small and large screens
			if(segment % 2 == 0) //is even so 2,4,6
				return Const.SLOTS_WIDTH + centerHoriz;
			else  // is odd so 1,3,5
				return centerHoriz;
		}
		else{
			return 2*Const.SLOTS_WIDTH + centerHoriz;//always third column
		}
	}
	
	public static int yPosBtn(int segment){

		int segsLeft = 10;
		if(segment <= segsLeft){//small or large, we move down 2 by 2 with the rows
			
			int row = (int)Math.ceil(((double)segment)/2);//integer division -> rounds off so both 3,4 become 2
			
			return row*Const.SLOTS_HEIGHT + centerVert;
		}
		else{
			//so at 11 it should be the upper right
			//iterate down the third column, one by one
			return (segment - segsLeft) * Const.SLOTS_HEIGHT + centerVert; 
		}
	}
	
	public static int xPosTexture(int segment){
		//TODO switch is for temp
		
		switch(segment){
		case 1:
			return left;
		case 2:
			return pad + left + Const.SLOTS_WIDTH;
		case 3:
			return left;
		case 4:
			return pad+left+Const.SLOTS_WIDTH;
		case 5:
			return left;
		case 6:
			return pad+left+Const.SLOTS_WIDTH;
		}
		
		return 0;
	}
	
	public static int yPosTexture(int segment){
		//TODO switch is for temp

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
