package com.lothrazar.powerinventory.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class UtilTextureRender {
	/*
	@SideOnly(Side.CLIENT)
	public static void renderItemAt(ItemStack stack, int x, int y, int dim) {
		// first get texture from item stack
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

		if (Minecraft.getMinecraft().currentScreen != null)
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, textureAtlasSprite, dim, dim);
	}
*/
	public static void drawTextureSimple(ResourceLocation res, int x, int y, int w, int h) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		//Gui.drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, w, h, w, h);
		drawTexturedQuadFit(x,y,w,h);
	}
	
	@SideOnly(Side.CLIENT)	
	public static void drawTexturedQuadFit(double x, double y, double width, double height)
	{
		double zLevel = 0;
		//because the vanilla code REQUIRES textures to be powers of two AND are force dto be max of 256??? WHAT?
		//so this one actually works
		//THANKS hydroflame  ON FORUMS 
		//http://www.minecraftforge.net/forum/index.php/topic,11229.msg57594.html#msg57594
		//https://github.com/LothrazarMinecraftMods/UncraftingTable/blob/backport1710/src/main/java/com/lothrazar/uncraftingtable/GuiUncrafting.java
		
		Tessellator tessellator = Tessellator.instance;
  
		tessellator.startDrawingQuads();
        
		tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
		tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
	}
}
