package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.SwapInvoPacket;
import com.lothrazar.powerinventory.ModInv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonRotate extends GuiButton {
	private int invoGroup;

	public GuiButtonRotate(int buttonId, int x, int y, int width, int height, String lbl,int ig) {
		super(buttonId, x, y, width, height, lbl);// ig for test
		invoGroup = ig;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			NBTTagCompound tags = new NBTTagCompound();
			tags.setInteger("i", invoGroup);
			ModInv.instance.network.sendToServer(new SwapInvoPacket(tags));
		}

		return pressed;
	}
}
