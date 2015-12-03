package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.UtilTextureRender;
import com.lothrazar.powerinventory.inventory.client.GuiButtonOpenInventory;
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer; 
import net.minecraft.util.ResourceLocation;

public class GuiCustomPlayerInventory extends GuiContainer
{
	GuiButton btnEnder;
	GuiButton btnExp;
	GuiButton btnUncraft;
	
	ResourceLocation res = new ResourceLocation(Const.MODID, "inventory.png");
	private final InventoryCustomPlayer inventory;
	//private final EntityPlayer thePlayer;
	
	
	public GuiCustomPlayerInventory(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryCustomPlayer inventoryCustom)
	{
		//the player.inventory gets passed in here
		super(new ContainerCustomPlayer(player, inventoryPlayer, inventoryCustom));
		inventory = inventoryCustom;
		//thePlayer = player;
	}
	
	@Override
	public void initGui()
    { 
		super.initGui();

		int button_id = 199;
		int width = 26;
		final int height = 20;
 
		
		btnEnder = new GuiButtonOpenInventory(button_id++, 
				this.guiLeft + SlotEnderChest.posX + 19, 
				this.guiTop + SlotEnderChest.posY - 1,
				12,height, "I",Const.INV_ENDER); 
		this.buttonList.add(btnEnder); 
		//btnEnder.enabled = false;// turn it on based on ender chest present or not
		//btnEnder.visible = btnEnder.enabled;
		
 
    }
	
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
	
	}
	@Override
	protected void drawGuiContainerForegroundLayer(	int p_146976_2_, int p_146976_3_)
	{ 
		//drawing text and such on screen
		this.checkSlotsEmpty();
		
	}
	
	private void checkSlotsEmpty()
	{
		//TODO: interface-ey stuff to share code more
		
		//!!!
		final int s = 16;
 
		if(inventory.getStackInSlot(Const.enderChestSlot) == null)
		{
			btnEnder.enabled = false;
			btnEnder.visible = btnEnder.enabled;

			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotEnderChest.background,SlotEnderChest.posX, SlotEnderChest.posY,s,s); 
		}
		else 
		{ 
			btnEnder.enabled = true; 
			btnEnder.visible = btnEnder.enabled;
		}


		if(inventory.getStackInSlot(Const.enderPearlSlot) == null)
		{  
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotEnderPearl.background,SlotEnderPearl.posX, SlotEnderPearl.posY,s,s);
		}

	}
	private ResourceLocation bkg = new ResourceLocation(Const.MODID,  "textures/gui/inventory.png");
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,	int p_146976_2_, int p_146976_3_)
	{ 
		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//GL11.glScalef(1.0F, 1.0F, 1.0F);//so it does not change scale
		//this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID,  "textures/gui/inventory.png"));
		
		//UtilTextureRender.drawTexturedQuadFit(this.guiLeft, this.guiTop,this.xSize,this.ySize);//,0
		UtilTextureRender.drawTextureSimple(bkg, this.guiLeft, this.guiTop,this.xSize,this.ySize);
	 

        drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);
    	drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);
	}
	private void drawSlotAt(int x, int y)
	{
        UtilTextureRender.drawTextureSimple(Const.slot,this.guiLeft+ x -1, this.guiTop+ y -1,  Const.SQ, Const.SQ);
	}
}
