package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.OpenInventoryPacket;
import com.lothrazar.powerinventory.util.UtilTextureRender;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonOpenInventory extends GuiButton implements IGuiTooltip {

	private String tooltip;

	public GuiButtonOpenInventory(int buttonId, int x, int y) {
		super(buttonId, x, y, 20, 20, "");
		this.setTooltip(StatCollector.translateToLocal("tooltip.open"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			NBTTagCompound tags = new NBTTagCompound();
			ModInv.instance.network.sendToServer(new OpenInventoryPacket(tags));
		}

		return pressed;
	}
	ResourceLocation button = new ResourceLocation(Const.MODID,"textures/gui/tab_button.png");
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		// override this and draw the texture here, so the vanilla grey square
		// doesnt show up
		if (this.visible) {
			// http://www.minecraftforge.net/forum/index.php?topic=19594.0

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);

			UtilTextureRender.drawTextureSimple(button, this.xPosition, this.yPosition, this.width,this.height);

			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
//TODO: tab
	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public void setTooltip(String s) {
		tooltip = s;
	}
}
