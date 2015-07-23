package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.Const; 
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.BigContainerPlayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
 
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
/**
 * @author https://github.com/Funwayguy/InfiniteInvo
 * @author Forked and altered by https://github.com/PrinceOfAmber/InfiniteInvo
 */
public class GuiBigInventory extends GuiInventory
{
	private BigContainerPlayer container;

	public static final int texture_width = 464;
	public static final int texture_height = 382;

	GuiButton btnEnder;
	public GuiBigInventory(EntityPlayer player)
	{
		super(player);
		container = player.inventoryContainer instanceof BigContainerPlayer? (BigContainerPlayer)player.inventoryContainer : null;
		this.xSize = texture_width;
		this.ySize = texture_height;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
    { 
		super.initGui();
		
		if(this.container != null && this.mc.playerController.isInCreativeMode() == false)
		{
			final int height = 20;
			final int width = 30;
			final int widthlrg = 58;
			final int padding = 6;
			int button_id = 99;
			 
			btnEnder = new GuiButtonOpenInventory(button_id++, 
					this.guiLeft + texture_width - width - 64, 
					this.guiTop + texture_height - height - padding,
					width,height, "EC",Const.INV_ENDER);
			this.buttonList.add(btnEnder); 
			btnEnder.enabled = false;// turn it on based on ender chest present or not

			if(ModConfig.showFilterButton)
			{   
				this.buttonList.add(new GuiButtonFilter(button_id++, 
						this.guiLeft + texture_width-widthlrg - padding, 
						this.guiTop + padding,
						widthlrg,height));
			}
			
			if(ModConfig.showSortButtons)
			{  
				int x = guiLeft + 180;
				int y = guiTop + texture_height - width+4;
				int x_spacing = 32;
				 
				GuiButton btn;

				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_LEFTALL,"<<");
				this.buttonList.add(btn);

				x += x_spacing;
			 
				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_LEFT,"<");
				this.buttonList.add(btn);

				x += x_spacing+4;

				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_RIGHT,">");
				this.buttonList.add(btn);
				  
				x += x_spacing;
				
				btn = new GuiButtonSort(button_id++, x, y ,width,height, Const.SORT_RIGHTALL,">>");
				this.buttonList.add(btn);
				
			}
		}
    }
	
	private void checkButtons()
	{
		final int s = 16;
		String st;
		if(container.invo.getStackInSlot(Const.enderChestSlot) == null)
		{
			btnEnder.enabled = false;

			st = "textures/items/empty_enderchest.png";
			this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, st)); 
			drawTexturedQuadFit(container.echestX, container.echestY,s,s,0);
			
		}
		else 
		{ 
			btnEnder.enabled = true; 
		}

		if(container.invo.getStackInSlot(Const.enderPearlSlot) == null)
		{ 
			st = "textures/items/empty_enderpearl.png";
			this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, st)); 
			drawTexturedQuadFit(container.pearlX, container.pearlY,s,s,0);
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, Const.INVENTORY_TEXTURE));

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
/*
		Slot s;
		int show;
		for(Object o : this.container.inventorySlots)
		{
			//vanilla code does not declare ArrayList<Slot>, even though every object in there really is one
			s = (Slot)o;
	 
			//each slot has two different numbers. the slotNumber is UNIQUE, the index is not
			show = s.getSlotIndex();
			//show = s.slotNumber;
			this.drawString(this.fontRendererObj, "" + show, s.xDisplayPosition, s.yDisplayPosition +  4, 16777120);
		}*/

	}
}
