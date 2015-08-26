package com.lothrazar.powerinventory;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class SortGroup
{
	public SortGroup(String k)
	{
		stacks = new ArrayList<ItemStack>();
		key = k;
	}
	public void add(ItemStack s)
	{
		stacks.add(s);
	}
	public ArrayList<ItemStack> stacks;
	public String key;
}