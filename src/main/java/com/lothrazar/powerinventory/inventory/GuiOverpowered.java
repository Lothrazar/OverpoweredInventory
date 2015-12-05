package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.inventory.button.GuiButtonRotate;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockCraft;
import com.lothrazar.powerinventory.inventory.slot.*;
import com.lothrazar.powerinventory.util.UtilTextureRender;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer; 
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiOverpowered extends GuiContainer
{
	private ResourceLocation bkg = new ResourceLocation(Const.MODID,  "textures/gui/inventory.png");
	private ResourceLocation bkg_craft = new ResourceLocation(Const.MODID,  "textures/gui/crafting.png");
	public static ResourceLocation slot = new ResourceLocation(Const.MODID,"textures/gui/inventory_slot.png");
	public static final int craftX = 36;//was 111,17
	public static final int craftY = 14;
	public static boolean SHOW_DEBUG_NUMS = false;
	private final InventoryOverpowered inventory;
	private ContainerOverpowered container;
	final int h = 20;
	final int w = 20;
	final int padding = 6;//on the far outer sizes
	
	public GuiOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryOverpowered inventoryCustom)
	{
		//the player.inventory gets passed in here
		super(new ContainerOverpowered(player, inventoryPlayer, inventoryCustom));
		container = (ContainerOverpowered)this.inventorySlots;
		inventory = inventoryCustom;
		
		
		
		//fixed numbers from the .png resource size
		this.xSize = 342;
		this.ySize = 225;
	}

	@Override
	public void initGui()
    { 
		super.initGui();
		
		int button_id = 99;
		
		int xstart = this.guiLeft + this.xSize - w - padding;
		int ystart = this.guiTop + padding;
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				xstart, //top right
				ystart,w,h, GuiButtonRotate.TOPRIGHT));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				xstart - w - padding, //bottom left
				ystart + padding+h, w,h,GuiButtonRotate.BOTLEFT));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				xstart, //bottom right
				ystart+padding+h, w,h,GuiButtonRotate.BOTRIGHT));
		
		if(container.craftingEnabled == false){

			this.buttonList.add(new GuiButtonUnlockCraft(button_id++,
					this.guiLeft+craftX, 
					this.guiTop+craftY));
		}
    }
	
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		
		if(SHOW_DEBUG_NUMS){ 
			for(Slot s : this.container.inventorySlots)
			{
				//each slot has two different numbers. the slotNumber is UNIQUE, the index is not
				this.drawString(this.fontRendererObj, "" + s.getSlotIndex(), 
						this.guiLeft + s.xDisplayPosition,
						this.guiTop + s.yDisplayPosition +  4, 
						16777120);//font color
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{ 
		this.checkSlotsEmpty();
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	private void checkSlotsEmpty()
	{
		final int s = 16;

		if(inventory.getStackInSlot(Const.enderPearlSlot) == null)
		{  
			UtilTextureRender.drawTextureSimple(SlotEnderPearl.background,SlotEnderPearl.posX, SlotEnderPearl.posY,s,s);
		}

		if(inventory.getStackInSlot(Const.enderChestSlot) == null)
		{  
			UtilTextureRender.drawTextureSimple(SlotEnderChest.background,SlotEnderChest.posX, SlotEnderChest.posY,s,s);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{ 
		UtilTextureRender.drawTextureSimple(bkg, this.guiLeft, this.guiTop,this.xSize,this.ySize);
		
		if( container.craftingEnabled ){

			UtilTextureRender.drawTextureSimple(bkg_craft, 
					this.guiLeft+craftX, 
					this.guiTop+craftY,
					111,57);
		}
	 
        drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);
    	drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);
	}
	
	private void drawSlotAt(int x, int y)
	{
        UtilTextureRender.drawTextureSimple(slot,this.guiLeft + x - 1, this.guiTop + y - 1, Const.SQ, Const.SQ);
	}
}
