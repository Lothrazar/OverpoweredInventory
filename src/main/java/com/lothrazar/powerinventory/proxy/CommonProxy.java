package com.lothrazar.powerinventory.proxy;

import com.lothrazar.powerinventory.*;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	public boolean isClient() {
		return false;
	}

	public void registerHandlers() {
		EventHandler handler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
	}
}
