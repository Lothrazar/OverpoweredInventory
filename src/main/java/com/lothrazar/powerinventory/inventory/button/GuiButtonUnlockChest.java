package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.UnlockChestPacket;
import com.lothrazar.powerinventory.ModInv;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonUnlockChest extends GuiButtonUnlockExp implements IGuiTooltip {
	final static int height = 20;
	public final static int width = 70;
	private String tooltip;

	public GuiButtonUnlockChest(int buttonId, int x, int y, EntityPlayer player, int cost) {
		super(buttonId, x, y, width, height, player, cost);
		this.setTooltip(I18n.translateToLocal("tooltip.ender_chest"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			NBTTagCompound tags = new NBTTagCompound();
			ModInv.instance.network.sendToServer(new UnlockChestPacket(tags));
		}

		return pressed;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public void setTooltip(String s) {
		tooltip = s;
	}
}
