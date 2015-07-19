package com.lothrazar.powerinventory.proxy;

import com.lothrazar.powerinventory.ModMutatedInventory;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public void registerHandlers()
	{
		super.registerHandlers();
    	
    	ModMutatedInventory.instance.network.registerMessage(InvoPacket.HandleClient.class, InvoPacket.class, InvoPacket.ID, Side.CLIENT);
	}
}
