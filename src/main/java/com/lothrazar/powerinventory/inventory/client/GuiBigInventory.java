package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.BigContainerPlayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
 
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

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

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
    { 
		super.initGui();
		
		if(this.container != null && this.mc.playerController.isInCreativeMode() == false)
		{
			int height = 20;
			int STARTX = this.guiLeft + 240;
			int STARTY = this.guiTop + height/2;
			int enderWidth = 90;
			int sortWidth = enderWidth/2 - 5;
			int x = STARTX;
			int y = STARTY;
			int x_spacing = enderWidth/2 + 5;
			int button_id = 99;
			
			if(ModConfig.showEnderButton)
			{
				btnEnder = new GuiButtonInventory(button_id++, x, y ,enderWidth,height,StatCollector.translateToLocal("tile.enderChest.name"),Const.INV_ENDER);
				this.buttonList.add(btnEnder);
				
				btnEnder.enabled = false;//TODO: turn it on based on ender chest present or not
			}
			if(ModConfig.showSortButtons)
			{
				y += height+1;
				GuiButton sortButton;

				sortButton = new GuiButtonSort(button_id++, x, y ,sortWidth,height, Const.SORT_LEFT,"<");
				this.buttonList.add(sortButton);

				x += x_spacing;

				sortButton = new GuiButtonSort(button_id++, x, y ,sortWidth,height, Const.SORT_RIGHT,">");
				this.buttonList.add(sortButton);
				
				x = STARTX;
				y += height+1;

				sortButton = new GuiButtonSort(button_id++, x, y ,sortWidth,height, Const.SORT_LEFTALL,"<<");
				this.buttonList.add(sortButton);

				x += x_spacing;
				
				sortButton = new GuiButtonSort(button_id++, x, y ,sortWidth,height, Const.SORT_RIGHTALL,">>");
				this.buttonList.add(sortButton);
				
			}
			
			if(ModConfig.showFilterButton)
			{
				x = STARTX + enderWidth + 5;
				y = STARTY;

				this.buttonList.add(new GuiButtonFilter(button_id++, x, y ,enderWidth,height));
			}
		}
    }
	private void checkButtons()
	{
		btnEnder.enabled = (container.invo.getStackInSlot(Const.enderChestSlot) != null);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{ 
		this.checkButtons();
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(ModInv.MODID, ModInv.INVENTORY_TEXTURE));
        int gLeft = this.guiLeft;
        int gTop = this.guiTop;
        final int square = Const.square;
        this.drawTexturedModalRect(gLeft, gTop, 0, 0, xStart, yStart);
        
        
        for(int i = 0; i < Const.MORE_COLS; i++)
        {
            this.drawTexturedModalRect(gLeft + xStart + (i * square), gTop, xStart, 0, square, yStart);
        }
        
        for(int i = 0; i < Const.MORE_ROWS; i++)
        {
            this.drawTexturedModalRect(gLeft, gTop + yStart + (i * square), 0, 119, xStart, square);
        }
        
        for(int i = 0; i < Const.MORE_COLS; i++)
        {
        	for(int j = 0; j < Const.MORE_ROWS; j++)
        	{
                this.drawTexturedModalRect(gLeft + xStart + (i * square), gTop + yStart + (j * square), 7, 83, square, square);
        	}
        }
        
        //the ender slot 
        this.drawTexturedModalRect(gLeft-1 + container.pearlX, gTop-1 + container.pearlY, 7, 83, square, square);
        this.drawTexturedModalRect(gLeft-1 + container.echestX, gTop-1 + container.echestY, 7, 83, square, square);
        
        
        int barW = 8;
        //int barW = Const.ALL_COLS * Const.ALL_ROWS < Const.invoSize? 0 : 8;

        this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square), gTop, 187, 0, 2, 119); // Scroll top
        this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square) + 2, gTop, 189 + barW, 0, 13 - barW, 119); // Scroll top
        
        for(int i = 0; i < Const.MORE_ROWS; i++)
        {
            this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square), gTop + 119 + (i * square), 187, 101, 2, square); // Scroll middle
            this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square) + 2, gTop + 119 + (i * square), 189 + barW, 101, 13 - barW, square); // Scroll middle
        }
        
        this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square), gTop + 119 + (Const.MORE_ROWS * square), 187, 119, 2, square); // Scroll bottom
        this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square) + 2, gTop + 119 + (Const.MORE_ROWS * square), 189 + barW, 119, 13 - barW, square); // Scroll bottom
        
        this.drawTexturedModalRect(gLeft, gTop + yStart + (Const.MORE_ROWS * square), 0, yStart, xStart, 29);
        
        for(int i = 0; i < Const.MORE_COLS; i++)
        {
            this.drawTexturedModalRect(gLeft + xStart + (i * square), gTop + yStart + (Const.MORE_ROWS * square), xStart, yStart, square, 29);
        }
        

        
        
        this.drawTexturedModalRect(gLeft + xStart + (Const.MORE_COLS * square), gTop + yStart + (Const.MORE_ROWS * square), 187 + barW, yStart, 16 - barW, 29);

        if(ModConfig.showCharacter)
        	drawEntityOnScreen(gLeft + 51, gTop + 75, 30, (float)(gLeft + 51) - (float)mouseX, (float)(gTop + 75 - 50) - (float)mouseY, this.mc.thePlayer);
     
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{ 
		if(ModConfig.showText)
			this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 87, 32, 4210752);
		
		if(container != null)
		{
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(new ResourceLocation(ModInv.MODID, ModInv.INVENTORY_TEXTURE));
	
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
