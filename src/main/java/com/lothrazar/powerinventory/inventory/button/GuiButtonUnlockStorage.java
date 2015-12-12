package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.UnlockStoragePacket;
import com.lothrazar.powerinventory.ModInv;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiButtonUnlockStorage extends GuiButtonUnlockExp implements IGuiTooltip {
	public final static int height = 20;
	public final static int width = 70;
	private int invoGroup;
	private String tooltip;

	public GuiButtonUnlockStorage(int buttonId, int x, int y, EntityPlayer player, int cost, int ig) {
		super(buttonId, x, y, width, height, player,cost);
		invoGroup = ig;
		this.setTooltip(StatCollector.translateToLocal("tooltip.storage"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			NBTTagCompound tags = new NBTTagCompound();
			tags.setInteger("i", invoGroup);
			ModInv.instance.network.sendToServer(new UnlockStoragePacket(tags));
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
