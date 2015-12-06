package com.lothrazar.powerinventory.inventory;

import java.util.Arrays;

import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.inventory.button.GuiButtonRotate;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockChest;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlock3x3Crafting;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockPearl;
import com.lothrazar.powerinventory.inventory.button.GuiButtonUnlockStorage;
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
	final int w = 20;//default button dims
	final int topspace=83;//space used by top half not including slots
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
					ystart,w,h, Const.STORAGE_1TOPRIGHT));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
					xstart - w - padding, //bottom left
					ystart + padding+h, w,h,Const.STORAGE_2BOTLEFT));
		
		this.buttonList.add(new GuiButtonRotate(button_id++,
					xstart, //bottom right
					ystart+padding+h, w,h,Const.STORAGE_3BOTRIGHT));

		if(container.epearlSlotEnabled == false){

			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostPearl;
			
			b = new GuiButtonUnlockPearl(button_id++,
					this.guiLeft+padding,  
					this.guiTop+padding,label);
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostPearl);
		}
		if(container.echestSlotEnabled == false){

			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostEChest;
			
			b = new GuiButtonUnlockChest(button_id++,
					this.guiLeft+padding,  
					this.guiTop+padding + 3*Const.SQ,label);
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
		
		int centerHorizCol = SLOTS_WIDTH/2 - GuiButtonUnlockStorage.width/2;
		int centerVert = topspace - SLOTS_HEIGHT/2 - GuiButtonUnlockStorage.height/2;
		
		if(container.storageEnabled[Const.STORAGE_1TOPRIGHT] == false)
		{
			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostStorage + " XP";
			
			b = new GuiButtonUnlockStorage(button_id++,
					this.guiLeft + SLOTS_WIDTH + centerHorizCol, 
					this.guiTop+ SLOTS_HEIGHT+centerVert, label,Const.STORAGE_1TOPRIGHT);
			
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostStorage);
		}
		if(container.storageEnabled[Const.STORAGE_2BOTLEFT] == false)
		{
			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostStorage + " XP";
			
			b = new GuiButtonUnlockStorage(button_id++,
					this.guiLeft+ centerHorizCol, 
					this.guiTop+ 2*SLOTS_HEIGHT + centerVert, label,Const.STORAGE_2BOTLEFT);
			
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostStorage);
		}
		if(container.storageEnabled[Const.STORAGE_3BOTRIGHT] == false)
		{
			int current = (int)UtilExperience.getExpTotal(thePlayer);
			label = current + "/" + ModConfig.expCostStorage + " XP";
			
			b = new GuiButtonUnlockStorage(button_id++,
					this.guiLeft + SLOTS_WIDTH + centerHorizCol, 
					this.guiTop+ 2*SLOTS_HEIGHT + centerVert, label,Const.STORAGE_3BOTRIGHT);
			
			this.buttonList.add(b);
			
			b.enabled = (current >= ModConfig.expCostStorage);
		}
    }
	
	@Override
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

		if(container.echestSlotEnabled && inventory.getStackInSlot(Const.SLOT_ECHEST) == null){  
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
		//TODO: these will be exp unlocks also
		int left=7,pad=4;
		//topright is 1
		if(container.storageEnabled[Const.STORAGE_1TOPRIGHT])
			UtilTextureRender.drawTextureSimple(bkg_3x9, 
					this.guiLeft+pad+left+SLOTS_WIDTH, 
					this.guiTop+topspace, SLOTS_WIDTH, SLOTS_HEIGHT);
		//lower left is 2
		if(container.storageEnabled[Const.STORAGE_2BOTLEFT])
			UtilTextureRender.drawTextureSimple(bkg_3x9, 
					this.guiLeft+left, 
					this.guiTop+topspace+pad+SLOTS_HEIGHT, SLOTS_WIDTH, SLOTS_HEIGHT);
		//lower right is 3
		if(container.storageEnabled[Const.STORAGE_3BOTRIGHT])
			UtilTextureRender.drawTextureSimple(bkg_3x9, 
					this.guiLeft+pad+left+SLOTS_WIDTH, 
					this.guiTop+topspace+pad+SLOTS_HEIGHT, SLOTS_WIDTH, SLOTS_HEIGHT);
		
        if(container.echestSlotEnabled){drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);}
    	if(container.epearlSlotEnabled){drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);}
	}
	
	private void drawSlotAt(int x, int y)
	{
        UtilTextureRender.drawTextureSimple(slot,this.guiLeft + x - 1, this.guiTop + y - 1, Const.SQ, Const.SQ);
	}
}
