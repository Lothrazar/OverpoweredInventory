package com.lothrazar.powerinventory.proxy;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	public static KeyBinding keyEnderpearl;
	public static KeyBinding keyEnderchest;
	public static KeyBinding keyHotbar;
	public static KeyBinding keyInventory;

	public static final String keyCategory = "key.categories.inventory";

	@Override
	public boolean isClient() {
		return true;
	}

	@Override
	public void registerHandlers() {
		super.registerHandlers();

		keyEnderpearl = new KeyBinding("key.enderpearl", Keyboard.KEY_Z, keyCategory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyEnderpearl);

		keyEnderchest = new KeyBinding("key.enderchest", Keyboard.KEY_I, keyCategory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyEnderchest);

		keyHotbar = new KeyBinding("key.hotbar", Keyboard.KEY_H, keyCategory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyHotbar);
		
		keyInventory = new KeyBinding("key.opinventory", Keyboard.KEY_B, keyCategory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyInventory);
	}
}
