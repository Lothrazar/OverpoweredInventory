package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockStorage;

public class InventoryRenderer
{
	private static final int topspace=12+Const.SQ;//space used by top half not including slots
	private static final int left=7,pad=4;//pad is middle padding. left is left edge padding. for slot areas
	private static final int segsLeft = 10;//segments on the left two columns
	private static final int centerHoriz = Const.SLOTS_WIDTH/2 - GuiButtonUnlockStorage.width/2;
	private static final int centerVert = topspace - Const.SLOTS_HEIGHT/2 - GuiButtonUnlockStorage.height/2;
	
	
	private static int rowFromSegment(int segment){
		 
		//so 1,2 are in row 1. but also 11 is in row 1 so we split it off
		if(segment <= segsLeft)
			return (int)Math.ceil(((double)segment)/2);//integer division -> rounds off so both 3,4 become 2;
		else
			return segment - segsLeft;//so 1 -> 5
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
	
	public static int xPosBtn(int segment){
		int col = colFromSegment(segment);
		
		return (col-1)*Const.SLOTS_WIDTH + centerHoriz;
	}
	
	public static int yPosBtn(int segment){

		int row = rowFromSegment(segment);
		//one of the only ones not row-1
		return row*Const.SLOTS_HEIGHT + centerVert;
	}
	
	public static int xPosTexture(int segment){

		int col = colFromSegment(segment);
		//so column 1 is set only by the padding
		return left + (col-1)*(pad + Const.SLOTS_WIDTH);
	}
	
	public static int yPosTexture(int segment){

		int row = rowFromSegment(segment);
		return topspace + (row-1)*(pad + Const.SLOTS_HEIGHT);
	}

	public static int xPosSlotsStart(int segment){
		int col = colFromSegment(segment);
		
		return 2*pad + (col-1)*(Const.SLOTS_WIDTH + pad);
	}

	public static int yPosSlotsStart(int segment){
		int row = rowFromSegment(segment);
		int topLimit = topspace+1;//13+Const.SQ;//DIFFERENT than topSpace - this is for data slot not images
		
		return topLimit + (row-1)*(Const.SLOTS_HEIGHT + pad);
	}
}
