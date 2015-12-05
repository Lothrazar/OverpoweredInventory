package com.lothrazar.powerinventory.inventory;

import java.util.Arrays;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.inventory.button.GuiButtonRotate;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockChest;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlock3x3Crafting;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockPearl;
import com.lothrazar.powerinventory.inventory.button.IGuiTooltip;
import com.lothrazar.powerinventory.inventory.slot.*;
import com.lothrazar.powerinventory.util.UtilExperience;
import com.lothrazar.powerinventory.util.UtilTextureRender;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer; 
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiOverpowered extends GuiContainer
{
	private ResourceLocation bkg = new ResourceLocation(Const.MODID,  "textures/gui/inventory.png");
	private ResourceLocation bkg_craft = new ResourceLocation(Const.MODID,  "textures/gui/crafting.png");
	private ResourceLocation bkg_3x9 = new ResourceLocation(Const.MODID,  "textures/gui/slots3x9.png");
	public static ResourceLocation slot = new ResourceLocation(Const.MODID,"textures/gui/inventory_slot.png");
	public static final int craftX = 56;//was 111,17
	public static final int craftY = 14;
	final int SLOTS_WIDTH = 162;
	final int SLOTS_HEIGHT = 54;// the 3x9 size
	public static boolean SHOW_DEBUG_NUMS = false;
	private final InventoryOverpowered inventory;
	private ContainerOverpowered container;
	final int h = 20;
	final int w = 20;
	final int padding = 6;//on the far outer sizes
	final EntityPlayer thePlayer;
	
	public GuiOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryOverpowered inventoryCustom)
	{
		//the player.inventory gets passed in here
		super(new ContainerOverpowered(player, inventoryPlayer, inventoryCustom));
		container = (ContainerOverpowered)this.inventorySlots;
		inventory = inventoryCustom;
		thePlayer = player;
		
		
		//fixed numbers from the .png resource size
		this.xSize = 342;
		this.ySize = 225;
	}

	@Override
	public void initGui()
    { 
		super.initGui();
		
		int button_id = 99;
		String label;
		int xstart = this.guiLeft + this.xSize - w - padding;
		int ystart = this.guiTop + padding;
		GuiButton b;
		this.buttonList.add(new GuiButtonRotate(button_id++,
				xstart, //top right
				ystart,w,h, GuiButtonRotate.TOPRIGHT));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				xstart - w - padding, //bottom left
				ystart + padding+h, w,h,GuiButtonRotate.BOTLEFT));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
				xstart, //bottom right
				ystart+padding+h, w,h,GuiButtonRotate.BOTRIGHT));

		if(container.epearlSlotEnabled == false){

			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostPearl;
			
			b = new GuiButtonUnlockPearl(button_id++,
				SlotEnderPearl.posX+40,  
				SlotEnderPearl.posY+4,label);
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostPearl);
		}
		if(container.echestSlotEnabled == false){

			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostEChest;
			
			b = new GuiButtonUnlockChest(button_id++,
				SlotEnderChest.posX+40,  
				SlotEnderChest.posY,label);
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostEChest);
		}
		
		if(container.craftingEnabled == false)
		{
			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostCrafting + " XP";
			
			b = new GuiButtonUnlock3x3Crafting(button_id++,
					this.guiLeft + craftX + 24,  // put this not in top left of where crafting image goes, but more centered
					this.guiTop  + craftY + 10, label);
			
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostCrafting);
		}
    }
	
	public void drawScreen(int x, int y, float par3)
	{
		super.drawScreen(x, y, par3);

		GuiButton btn;
		for (int i = 0; i < buttonList.size(); i++) 
		{
			btn = buttonList.get(i);
			if (btn instanceof IGuiTooltip && btn.isMouseOver() ) 
			{
				String tooltip = ((IGuiTooltip)btn).getTooltip();
				if (tooltip != null) 
				{
					//it takes a list, one on each line. but we use single line tooltips
					drawHoveringText(Arrays.asList(new String[]{ tooltip}), x, y, fontRendererObj);
				}
			}
		}
		
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

		if(container.epearlSlotEnabled && inventory.getStackInSlot(Const.SLOT_EPEARL) == null){
			UtilTextureRender.drawTextureSimple(SlotEnderPearl.background,SlotEnderPearl.posX, SlotEnderPearl.posY,s,s);
		}
		else{
			
		}

		if(container.echestSlotEnabled && inventory.getStackInSlot(Const.SLOT_ECHEST) == null){  
			UtilTextureRender.drawTextureSimple(SlotEnderChest.background,SlotEnderChest.posX, SlotEnderChest.posY,s,s);
		}
		else{
			
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
	 
        if(container.echestSlotEnabled){drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);}
    	if(container.epearlSlotEnabled){drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);}
	}
	
	private void drawSlotAt(int x, int y)
	{
        UtilTextureRender.drawTextureSimple(slot,this.guiLeft + x - 1, this.guiTop + y - 1, Const.SQ, Const.SQ);
	}
}
