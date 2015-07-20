package com.lothrazar.powerinventory;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class ModSettings
{
	public static NBTTagCompound cachedSettings = new NBTTagCompound();

	public static final String NBT_SLOT = "Slot";

	public final static int hotbarSize = 9;
	public final static int armorSize = 4;

	public static int invoSize;
	public static int MORE_ROWS;
	public static int MORE_COLS;
    public static int ALL_COLS;// = 9 + ModSettings.MORE_COLS;
    public static int ALL_ROWS;	//3 + ModSettings.MORE_ROWS;
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
