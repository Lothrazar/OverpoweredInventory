package com.lothrazar.powerinventory;

import com.lothrazar.powerinventory.inventory.ContainerOverpowered;
import com.lothrazar.powerinventory.inventory.GuiOverpowered;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	private static int modGuiIndex = 0;
	public static final int GUI_CUSTOM_INV = modGuiIndex++;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z)
	{ 
		if (ID == GUI_CUSTOM_INV) 
			return new ContainerOverpowered(player, player.inventory, PlayerPersistProperty.get(player).inventory);
		else
			return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z)
	{ 
		if (ID == GUI_CUSTOM_INV)
			return new GuiOverpowered(player, player.inventory, PlayerPersistProperty.get(player).inventory);
		else
			return null;
	}
}
