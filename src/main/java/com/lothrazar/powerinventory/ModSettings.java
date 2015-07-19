package com.lothrazar.powerinventory;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A container for all the configurable settings in the mod
 */
public class ModSettings
{
	public static NBTTagCompound cachedSettings = new NBTTagCompound();
	

	public final static int hotbarSize = 9;
	public final static int armorSize = 4;

	public static int invoSize;
	public static int MORE_ROWS;
	public static int MORE_COLS;
    public static int fullCols;// = 9 + ModSettings.MORE_COLS;
    public static int fullRows;	//3 + ModSettings.MORE_ROWS;
    public static boolean showEnderButton;
	public static boolean showText;
	public static boolean showCharacter;
	public static boolean showSortButtons;
	public static boolean showFilterButton;
	public static int filterRange;

	public static void SaveToCache()
	{
		cachedSettings = new NBTTagCompound();
		cachedSettings.setInteger("invoSize", invoSize);
	}
	
	public static void LoadFromCache()
	{
		LoadFromTags(cachedSettings);
	}
	
	public static void LoadFromTags(NBTTagCompound tags)
	{
		invoSize = tags.getInteger("invoSize");
	}
}
