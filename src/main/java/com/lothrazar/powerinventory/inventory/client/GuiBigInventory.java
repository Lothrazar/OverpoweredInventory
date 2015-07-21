package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.BigContainerPlayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
 
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class GuiBigInventory extends GuiInventory
{
	private BigContainerPlayer container;

	public boolean redoButtons = false;
	int xStart = 169;
	int yStart = 137;
	GuiButton btnEnder;
	public GuiBigInventory(EntityPlayer player)
	{
		super(player);
		container = player.inventoryContainer instanceof BigContainerPlayer? (BigContainerPlayer)player.inventoryContainer : null;
		this.xSize = xStart + (Const.square * Const.MORE_COLS) + 15;
		this.ySize = yStart + (Const.square * Const.MORE_ROWS) + 29;
System.out.println(xSize+"   "+ySize);
System.out.println(xSize+"   "+ySize);
System.out.println(xSize+"   "+ySize);
System.out.println(xSize+"   "+ySize);
System.out.println(xSize+"   "+ySize);//472    382
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
    { 
		super.initGui();
		
		if(this.container != null && this.mc.playerController.isInCreativeMode() == false)
		{
			final int height = 20;
			final int width = 32;
			final int ypadding = 6;
			int button_id = 99;
			 
			btnEnder = new GuiButtonInventory(button_id++, this.guiLeft + 210, this.guiTop + ypadding ,width,height, "EC",Const.INV_ENDER);
			this.buttonList.add(btnEnder); 
			btnEnder.enabled = false;// turn it on based on ender chest present or not
		  
			if(ModConfig.showSortButtons)
			{
				final int STARTX = this.guiLeft + 280;
				final int STARTY = this.guiTop + ypadding;
				int x = STARTX;
				int y = STARTY;
				int x_spacing = 50;
				
				y += height + 1;
				GuiButton btn;

				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_LEFT,"<");
				this.buttonList.add(btn);

				x += x_spacing;

				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_RIGHT,">");
				this.buttonList.add(btn);
				
				x = STARTX;
				y += height+1;

				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_LEFTALL,"<<");
				this.buttonList.add(btn);

				x += x_spacing;
				
				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_RIGHTALL,">>");
				this.buttonList.add(btn);
				
			}
			
			if(ModConfig.showFilterButton)
			{  
				this.buttonList.add(new GuiButtonFilter(button_id++, this.guiLeft + 395, this.guiTop + ypadding,60,height));
			}
		}
    }
	private void checkButtons()
	{
		
		btnEnder.enabled = (container.invo.getStackInSlot(Const.enderChestSlot) != null);
		
		if(btnEnder.enabled == false)
		{
			final int s = Const.square;
	       // GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			String st = "textures/items/empty_enderchest";
			this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, st));
		
			drawTexturedQuadFit(container.echestX, container.echestY,s,s,0);
			
		 	//this.drawTexturedModalRect(container.echestX-1+5, container.echestY-1	, 0, 0, Const.square, Const.square);

			//to use standard cols: http://minecraft.gamepedia.com/Formatting_codes
			//int white = 0xFFFFFF;
			int red = 0x2A0000;
	 
 
			int pad = +1;
			int x = pad+container.echestX;
			int y = pad+container.echestY;
	 
			this.fontRendererObj.drawString( "0", x,y, red);

		
			
			
			//this one below does work, but it goes on outside screen, behind gui
			//this.mc.fontRendererObj.drawStringWithShadow("123123112312312qwadadasdasdfasdf2", 0,this.guiTop, color);
		}
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
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{ 
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);//so it does not change scale
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, ModInv.INVENTORY_TEXTURE));

        drawTexturedQuadFit(this.guiLeft, this.guiTop,this.xSize,this.ySize,0);
      
        if(ModConfig.showCharacter)
        	drawEntityOnScreen(this.guiLeft + 51, this.guiTop + 75, 30, (float)(this.guiLeft + 51) - (float)mouseX, (float)(this.guiTop + 75 - 50) - (float)mouseY, this.mc.thePlayer);
     
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{ 
		this.checkButtons();
		
		if(ModConfig.showText)
			this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 87, 32, 4210752);
		
		if(container != null)
		{
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, ModInv.INVENTORY_TEXTURE));
	
	        // Draw the empty/locked slot icons
	        for(int j = 0; j < Const.ALL_ROWS; j++)
	        {
	        	for(int i = 0; i < Const.ALL_COLS; i++)
	        	{
	        		if(i + (j + container.scrollPos) * Const.ALL_COLS >= Const.invoSize)
	        		{
	        			this.drawTexturedModalRect(7 + i * Const.square, 83 + j * Const.square, 0, 166, Const.square, Const.square);
	        		} 
	        	}
	        }
		}
	}
}
