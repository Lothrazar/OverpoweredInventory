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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonOpenInventory extends GuiButton{

	public static int width = 28, height = 30;
	public GuiButtonOpenInventory(int buttonId, int x, int y) {
		super(buttonId, x, y, width,height, "");
		//this.setTooltip(StatCollector.translateToLocal("tooltip.open"));
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
	ResourceLocation button_dark = new ResourceLocation(Const.MODID,"textures/gui/tab_button_dark.png");
	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		// override this and draw the texture here, so the vanilla grey square
		// doesnt show up
		if (this.visible) {
			// http://www.minecraftforge.net/forum/index.php?topic=19594.0

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + GuiButtonOpenInventory.width && mouseY < this.yPosition + GuiButtonOpenInventory.height;

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);

		
			if(this.isMouseOver()){
				UtilTextureRender.drawTextureSimple(button, this.xPosition, this.yPosition, GuiButtonOpenInventory.width,GuiButtonOpenInventory.height);

				//drawHoveringText(Arrays.asList(new String[] { tooltip }),  mouseX, mouseY, Minecraft.getMinecraft().fontRendererObj);
			}
			else{
				UtilTextureRender.drawTextureSimple(button_dark, this.xPosition, this.yPosition, GuiButtonOpenInventory.width,GuiButtonOpenInventory.height);
			}

			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
