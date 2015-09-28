package com.lothrazar.powerinventory.standalone;

import org.lwjgl.opengl.GL11;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.client.GuiButtonExp;
import com.lothrazar.powerinventory.inventory.client.GuiButtonOpenInventory;
import com.lothrazar.powerinventory.inventory.client.GuiButtonUnc;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiCustomPlayerInventory extends GuiContainer
{

	GuiButton btnEnder;
	GuiButton btnExp;
	GuiButton btnUncraft;
	
	ResourceLocation res = new ResourceLocation(Const.MODID, Const.INVENTORY_TEXTURE);
	private final InventoryCustomPlayer inventory;
	private final EntityPlayer thePlayer;
	
	
	public GuiCustomPlayerInventory(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		super(new ContainerCustomPlayer(player, inventoryPlayer, inventoryCustom));
		inventory = inventoryCustom;
		thePlayer = player;

	}
	
	
	
	@Override
	public void initGui()
    { 
		super.initGui();

		int button_id = 199;
		int width = 26;
		final int height = 20;
		
		if(ModConfig.enableUncrafting)
	    {
			btnUncraft = new GuiButtonUnc(button_id++, 
					this.guiLeft + Const.uncraftX - 51 ,
					this.guiTop + Const.uncraftY - 1,
					width + 20,height);
			this.buttonList.add(btnUncraft); 
			btnUncraft.enabled = false;// turn it on based on ender chest present or not
			//btnUncraft.visible = btnUncraft.enabled;
	    }
		
		btnEnder = new GuiButtonOpenInventory(button_id++, 
				this.guiLeft + Const.echestX + 19, 
				this.guiTop + Const.echestY - 1,
				12,height, "I",Const.INV_ENDER); 
		this.buttonList.add(btnEnder); 
		btnEnder.enabled = false;// turn it on based on ender chest present or not
		//btnEnder.visible = btnEnder.enabled;
		
		if(ModConfig.enableEnchantBottles)
	    {
			btnExp = new GuiButtonExp(button_id++, 
					this.guiLeft + Const.bottleX - width - Const.padding+1, 
					this.guiTop + Const.bottleY-2,
					width,height);
			this.buttonList.add(btnExp);
			
			btnExp.enabled = false;
			//btnExp.visible = btnExp.enabled;
	    }
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
		
		Const.drawTexturedQuadFit(this.guiLeft, this.guiTop,this.xSize,this.ySize);//,0
 
        //if feature is enabled, draw these
        if(ModConfig.enableEnchantBottles)
        	drawSlotAt(Const.bottleX, Const.bottleY);

        if(ModConfig.enableUncrafting)
        	drawSlotAt(Const.uncraftX, Const.uncraftY);
        

    	drawSlotAt(Const.echestX, Const.echestY);
    	drawSlotAt(Const.pearlX, Const.pearlY);
    	drawSlotAt(Const.clockX, Const.clockY);
    	drawSlotAt(Const.compassX, Const.compassY);
	}

	private void drawSlotAt(int x, int y)
	{
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, "textures/gui/inventory_slot.png"));
         
        //was this.drawTexturedModalRect
        Const.drawTexturedQuadFit(this.guiLeft+ x -1, this.guiTop+ y -1,  Const.square, Const.square);
	}

}
