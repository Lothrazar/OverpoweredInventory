package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockStorage;

public class InventoryRenderer
{

	public static final int topspace=12+Const.SQ;//space used by top half not including slots
	static final int centerHoriz = Const.SLOTS_WIDTH/2 - GuiButtonUnlockStorage.width/2;
	static final int centerVert = topspace - Const.SLOTS_HEIGHT/2 - GuiButtonUnlockStorage.height/2;
	//small: segments are [1,6]
	//large: segments are [1,15]
	
	public static int xPosBtn(int segment){
		//TODO: switch statements are temporary, to ensure copy paste accuracy. finding algo later
		switch(segment){
		case 1:	return Const.SLOTS_WIDTH + centerHoriz;
		case 2: return centerHoriz;
		case 3: return Const.SLOTS_WIDTH + centerHoriz;
		case 4: return centerHoriz;
		case 5: return Const.SLOTS_WIDTH + centerHoriz;
		default:
			return 0;
		}
	}
	public static int yPosBtn(int segment){

		//TODO: switch statements are temporary, to ensure copy paste accuracy. finding algo later
		switch(segment){
		case 1: return 1*Const.SLOTS_HEIGHT + centerVert;
		case 2: return 2*Const.SLOTS_HEIGHT + centerVert;
		case 3: return 2*Const.SLOTS_HEIGHT + centerVert;
		case 4: return 3*Const.SLOTS_HEIGHT + centerVert;
		case 5: return 3*Const.SLOTS_HEIGHT + centerVert;
		default:
			return 0;
		}
		//if segment == 1
	}
	
	public static int xPosSlots(int segment){

		return 0;//TODO
	}
	
	public static int yPosSlots(int segment){
		
		return 0;//TODO
	}
}
