package com.lothrazar.powerinventory.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
 
public class ClientProxy extends CommonProxy
{
	public static KeyBinding keyEnder;  
	public static final String keyEnderName = "key.ender";
	public static final String keyCategory = "key.categories.inventory";

	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public void registerHandlers()
	{
		super.registerHandlers();
		
		keyEnder = new KeyBinding(keyEnderName, Keyboard.KEY_Z, keyCategory); 
        ClientRegistry.registerKeyBinding(ClientProxy.keyEnder);
	}
}
