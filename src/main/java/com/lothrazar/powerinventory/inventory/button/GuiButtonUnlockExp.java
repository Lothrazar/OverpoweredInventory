package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.util.UtilExperience;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

public class GuiButtonUnlockExp extends GuiButton {

	public GuiButtonUnlockExp(int buttonId, int x, int y, int w, int h, EntityPlayer player, int cost) {
		super(buttonId, x, y, w, h, "");

		this.setExpCost((int) UtilExperience.getExpTotal(player), cost);
	}

	/**
	 * Also sets the title, and whether or not its disabled
	 * 
	 * @param playerCurrent
	 * @param cost
	 */
	public void setExpCost(int playerCurrent, int cost) {
		if (playerCurrent < cost) {
			this.enabled = false;
			this.displayString = playerCurrent + "/" + cost + " XP";
		}
		else {
			this.displayString = cost + " XP";
		}
	}
}
