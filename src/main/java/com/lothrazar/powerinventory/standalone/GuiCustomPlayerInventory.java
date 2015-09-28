package com.lothrazar.powerinventory.standalone;

import org.lwjgl.opengl.GL11;

import com.lothrazar.powerinventory.Const;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiCustomPlayerInventory extends GuiContainer
{
	/** x size of the inventory window in pixels. Defined as float, passed as int */
	//private float xSize_lo;
	
	/** y size of the inventory window in pixels. Defined as float, passed as int. */
	//private float ySize_lo;
	ResourceLocation res = new ResourceLocation(Const.MODID, Const.INVENTORY_TEXTURE);
	private final InventoryCustomPlayer inventory;
	private final EntityPlayer thePlayer;
	
	
	public GuiCustomPlayerInventory(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		super(new ContainerCustomPlayer(player, inventoryPlayer, inventoryCustom));
		inventory = inventoryCustom;
		thePlayer = player;

	}
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
	
		//this.xSize_lo = (float)par1;
	
		//this.ySize_lo = (float)par2;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(	int p_146976_2_, int p_146976_3_)
	{ 
		//drawing text and such on screen
		
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,	int p_146976_2_, int p_146976_3_)
	{ 
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);//so it does not change scale
		this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID,  "textures/gui/inventory.png"));
		
		drawTexturedQuadFit(this.guiLeft, this.guiTop,this.xSize,this.ySize,0);
 
	}
	
	public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel)
	{
		//because the vanilla code REQUIRES textures to be powers of two AND are force dto be max of 256??? WHAT?
		//so this one actually works
		//THANKS hydroflame  ON FORUMS 
		//http://www.minecraftforge.net/forum/index.php/topic,11229.msg57594.html#msg57594
		
		Tessellator tessellator = Tessellator.instance;
  
		//WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		tessellator.startDrawingQuads();
        
		tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0,1);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
		tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1,0);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
	}
}
