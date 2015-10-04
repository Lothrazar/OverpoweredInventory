package com.lothrazar.powerinventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class UtilTextureRender
{

	@SideOnly(Side.CLIENT)
	public static void renderItemAt(ItemStack stack, int x, int y, int dim)
	{
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());

		renderTexture(textureAtlasSprite, x, y, dim);
	}

	@SideOnly(Side.CLIENT)
	public static void renderTexture(TextureAtlasSprite textureAtlasSprite, int x, int y, int dim)
	{
		// special thanks to
		// http://www.minecraftforge.net/forum/index.php?topic=26613.0
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tessellator = Tessellator.getInstance();

		int height = dim, width = dim;
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV((double) (x), (double) (y + height), 0.0, (double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMaxV());
		worldrenderer.addVertexWithUV((double) (x + width), (double) (y + height), 0.0, (double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMaxV());
		worldrenderer.addVertexWithUV((double) (x + width), (double) (y), 0.0, (double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMinV());
		worldrenderer.addVertexWithUV((double) (x), (double) (y), 0.0, (double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMinV());
		tessellator.draw();
	}
	
	public static void drawTextureSimple(String texture,double x, double y, double width, double height)
	{
		//wrapper for drawTexturedQuadFit
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Const.MODID, texture)); 
		drawTexturedQuadFit(x,y,width,height);
	}
	
	public static void drawTextureSimple(TextureManager tm, String texture,double x, double y, double width, double height)
	{
		//wrapper for drawTexturedQuadFit
		tm.bindTexture(new ResourceLocation(Const.MODID, texture)); 
		drawTexturedQuadFit(x,y,width,height);
	}
	
	public static void drawTexturedQuadFit(double x, double y, double width, double height)
	{
		drawTexturedQuadFit(x,y,width,height,0);
	}
	
	public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel)
	{
		//because the vanilla code REQUIRES textures to be powers of two AND are force dto be max of 256??? WHAT?
		//so this one actually works
		//THANKS hydroflame  ON FORUMS 
		//http://www.minecraftforge.net/forum/index.php/topic,11229.msg57594.html#msg57594
		
		Tessellator tessellator = Tessellator.getInstance();
  
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        
        worldrenderer.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
        worldrenderer.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        worldrenderer.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
        worldrenderer.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
	}

	
}
