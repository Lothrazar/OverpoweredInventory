package com.lothrazar.powerinventory.standalone;

import com.lothrazar.powerinventory.inventory.InventoryPersistProperty;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	private static int modGuiIndex = 0;
	public static final int GUI_CUSTOM_INV = modGuiIndex++;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z)
	{ 
		if (ID == GUI_CUSTOM_INV) 
			return new ContainerCustomPlayer(player, player.inventory, InventoryPersistProperty.get(player).inventory);
		else
			return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z)
	{ 
		if (ID == GUI_CUSTOM_INV)
			return new GuiCustomPlayerInventory(player, player.inventory, InventoryPersistProperty.get(player).inventory);
		else
			return null;
	}

}
