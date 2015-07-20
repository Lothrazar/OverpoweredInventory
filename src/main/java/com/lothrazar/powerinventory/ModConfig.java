package com.lothrazar.powerinventory;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class ModConfig
{
	public static NBTTagCompound cachedSettings = new NBTTagCompound();

    public static boolean showEnderButton;
	public static boolean showText;
	public static boolean showCharacter;
	public static boolean showSortButtons;
	public static boolean showFilterButton;
	public static int filterRange;

	public static void SaveToCache()
	{
		//TODO: is this needed even?
		cachedSettings = new NBTTagCompound();
		cachedSettings.setInteger("invoSize", Const.invoSize);
	}
	
	public static void LoadFromCache()
	{
		LoadFromTags(cachedSettings);
	}
	
	public static void LoadFromTags(NBTTagCompound tags)
	{
		//invoSize = tags.getInteger("invoSize");
	}
}
