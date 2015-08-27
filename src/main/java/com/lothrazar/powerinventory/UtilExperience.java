package com.lothrazar.powerinventory;

import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author Lothrazar
 * imported from  https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/ModSpells.java
 *
 */
public class UtilExperience 
{ 
	public static double getExpTotal(EntityPlayer player)
	{
		int level = player.experienceLevel;

		//numeric reference: http://minecraft.gamepedia.com/Experience#Leveling_up
		double totalExp = getXpForLevel(level);

		double progress = Math.round(player.xpBarCap() * player.experience);

		totalExp += (int)progress;
		
		return totalExp;
	}

	public static boolean drainExp(EntityPlayer player, float f) 
	{  
		double totalExp = getExpTotal(player);

		if(totalExp - f < 0)
		{
			return false;
		}
		
		setXp(player, (int)(totalExp - f));
		
		return true;
	}
	
	public static int getXpToGainLevel(int level)
	{
		//numeric reference: http://minecraft.gamepedia.com/Experience#Leveling_up
		 //1.7.10
		if (level >= 30) 
		{
		    return 62 + (level - 30) * 7;
		} 
		else if (level >= 15) 
		{
		    return 17 + (level - 15) * 3;
		} else 
		{
		    return 17;
		}
		/* 1.8 numbers
		//so if our current level is 5, we pass in5 here and find out
		//how much exp to get from 5 to 6
		int nextLevelExp = 0;

		if(level <= 15)
			nextLevelExp = 2*level + 7;
		else if(level <= 30)
			nextLevelExp = 5*level - 38;
		else //level >= 31 
			nextLevelExp = 9*level - 158;
		
		return nextLevelExp;*/
	}
	
	public static int getXpForLevel(int level)
	{
		//numeric reference: http://minecraft.gamepedia.com/Experience#Leveling_up
		int totalExp = 0;
		//1.7  http://minecraft.gamepedia.com/Experience#Values_of_1.3.1_-_Before_1.8_.2814w02a.29
		if(level <= 15)
			totalExp = level*17;
		else if(level <= 30)
			totalExp = (int)(1.5*level*level - 29.5*level + 360);
		else //level >= 31 
			totalExp = (int)(3.5*level*level - 151.5*level + 2220);
		/* 1.8:::
		if(level <= 15)
			totalExp = level*level + 6*level;
		else if(level <= 30)
			totalExp = (int)(2.5*level*level - 40.5*level + 360);
		else //level >= 31 
			totalExp = (int)(4.5*level*level - 162.5*level + 2220);//fixed. was +162... by mistake
		*/
		return totalExp;
	}
	
	public static int getLevelForXp(int xp) 
	{
		int lev = 0;
		while (getXpForLevel(lev) < xp) 
		{
			lev++;
		}
		return lev - 1;
	}
	
	public static void setXp(EntityPlayer player, int xp)
	{
		player.experienceTotal = xp;
		player.experienceLevel = getLevelForXp(xp);
		int next = getXpForLevel(player.experienceLevel);
 
		player.experience = (float)(player.experienceTotal - next) / (float)player.xpBarCap(); 
		 
	} 
}
