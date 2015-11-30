package com.lothrazar.powerinventory.inventory;

import org.lwjgl.opengl.GL11;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.UtilTextureRender;
import com.lothrazar.powerinventory.inventory.client.GuiButtonExp;
import com.lothrazar.powerinventory.inventory.client.GuiButtonOpenInventory;
import com.lothrazar.powerinventory.inventory.client.GuiButtonUnc;
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
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
		super(new ContainerCustomPlayer(player, inventoryPlayer, inventoryCustom));
		inventory = inventoryCustom;
		//thePlayer = player;
	}
	
	@SuppressWarnings("unchecked")
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
			//btnUncraft.enabled = false;// turn it on based on ender chest present or not
			//btnUncraft.visible = btnUncraft.enabled;
	    }
		
		btnEnder = new GuiButtonOpenInventory(button_id++, 
				this.guiLeft + SlotEnderChest.posX + 19, 
				this.guiTop + SlotEnderChest.posY - 1,
				12,height, "I",Const.INV_ENDER); 
		this.buttonList.add(btnEnder); 
		//btnEnder.enabled = false;// turn it on based on ender chest present or not
		//btnEnder.visible = btnEnder.enabled;
		
		if(ModConfig.enableEnchantBottles)
	    {
			btnExp = new GuiButtonExp(button_id++, 
					this.guiLeft + Const.bottleX - width - Const.padding+1, 
					this.guiTop + Const.bottleY-2,
					width,height);
			this.buttonList.add(btnExp);
			
			//btnExp.enabled = false;
			//btnExp.visible = btnExp.enabled;
	    }
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
		if(ModConfig.enableUncrafting) 
			if(inventory.getStackInSlot(Const.uncraftSlot) == null)
			{ 
				btnUncraft.enabled = false;
				btnUncraft.visible = btnUncraft.enabled; 
			}
			else 
			{ 
				btnUncraft.enabled = true; 
				btnUncraft.visible = btnUncraft.enabled;
			}

		if(ModConfig.enableEnchantBottles) 
			if(inventory.getStackInSlot(Const.bottleSlot) == null || 
					inventory.getStackInSlot(Const.bottleSlot).getItem() == Items.experience_bottle	)
			{
				btnExp.enabled = false;
				btnExp.visible = btnExp.enabled;

				if(ModConfig.enableSlotOutlines)
					UtilTextureRender.drawTextureSimple(SlotBottle.background,Const.bottleX, Const.bottleY,s,s); 
			}
			else 
			{ 
				btnExp.enabled = true; 
				btnExp.visible = btnExp.enabled;
			}

		if(inventory.getStackInSlot(Const.enderPearlSlot) == null)
		{  
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotEnderPearl.background,SlotEnderPearl.posX, SlotEnderPearl.posY,s,s);
		}

		if(inventory.getStackInSlot(Const.compassSlot) == null)
		{ 
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotCompass.background,SlotCompass.posX, SlotCompass.posY,s,s);
		}

		if(inventory.getStackInSlot(Const.clockSlot) == null)
		{  
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotClock.background,SlotClock.posX, SlotClock.posY,s,s);
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
		 
        //if feature is enabled, draw these
        if(ModConfig.enableEnchantBottles)
        	drawSlotAt(Const.bottleX, Const.bottleY);

        if(ModConfig.enableUncrafting)
        	drawSlotAt(Const.uncraftX, Const.uncraftY);
        

        drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);
    	drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);
    	drawSlotAt(SlotClock.posX, SlotClock.posY);
    	drawSlotAt(SlotCompass.posX, SlotCompass.posY);
	}
	private void drawSlotAt(int x, int y)
	{
        UtilTextureRender.drawTextureSimple(Const.slot,this.guiLeft+ x -1, this.guiTop+ y -1,  Const.SQ, Const.SQ);
	}
}
