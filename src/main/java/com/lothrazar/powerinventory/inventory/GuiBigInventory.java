package com.lothrazar.powerinventory.inventory;

import com.lothrazar.powerinventory.Const; 
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.UtilTextureRender;
import com.lothrazar.powerinventory.inventory.client.*;
import com.lothrazar.powerinventory.inventory.slot.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiBigInventory extends GuiInventory
{
	public static String backgroundTexture;

	private BigContainerPlayer container;

	private GuiButton btnEnder;
	private GuiButton btnExp;
	private GuiButton btnUncraft;

	public static int texture_width;
	public static int texture_height;
	
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
			int width = 26;
			final int widthlrg = 58;
			final int padding = 6;
			//final int tiny = 12;
			int button_id = 99;
			
			this.buttonList.add(new GuiButtonPotions(button_id++,
					this.guiLeft  - width - padding, 
					this.guiTop - height - padding,
					width,height));

			 
			if(ModConfig.showMergeDeposit)
			{
				//we could refactor more, but this is good enough
				if(ModConfig.smallerMergeDep)
				{
					this.buttonList.add(new GuiButtonDump(button_id++,
							this.guiLeft + this.xSize - width - padding, 
							this.guiTop + padding,
							width,height,StatCollector.translateToLocal("button.dump_s")));
		
					this.buttonList.add(new GuiButtonFilter(button_id++,
							this.guiLeft + this.xSize - 2*padding - 2*width, 
							this.guiTop + padding,
							width,height, StatCollector.translateToLocal("button.filter_s")));
					
				}
				else // full size
				{
					this.buttonList.add(new GuiButtonDump(button_id++,
							this.guiLeft + this.xSize - widthlrg - padding, 
							this.guiTop + padding,
							widthlrg,height,StatCollector.translateToLocal("button.dump")));
		
					this.buttonList.add(new GuiButtonFilter(button_id++,
							this.guiLeft + this.xSize - 2*padding - 2*widthlrg, 
							this.guiTop + padding,
							widthlrg,height, StatCollector.translateToLocal("button.filter")));
				}
			}

			if(ModConfig.enableUncrafting)
		    {
				btnUncraft = new GuiButtonUnc(button_id++, 
						this.guiLeft + Const.uncraftX - 51 ,
						this.guiTop + Const.uncraftY - 1,
						width + 20,height);
				this.buttonList.add(btnUncraft); 
				btnUncraft.enabled = false;// turn it on based on ender chest present or not
				btnUncraft.visible = btnUncraft.enabled;
		    }
			
			btnEnder = new GuiButtonOpenInventory(button_id++, 
					this.guiLeft + SlotEnderChest.posX + 19, 
					this.guiTop + SlotEnderChest.posY - 1,
					12,height, "I",Const.INV_ENDER); 
			this.buttonList.add(btnEnder); 
			btnEnder.enabled = false;// turn it on based on ender chest present or not
			btnEnder.visible = btnEnder.enabled;
			
			if(ModConfig.enableEnchantBottles)
		    {
				btnExp = new GuiButtonExp(button_id++, 
						this.guiLeft + Const.bottleX - width - padding+1, 
						this.guiTop + Const.bottleY-2,
						width,height);
				this.buttonList.add(btnExp);
				
				btnExp.enabled = false;
				btnExp.visible = btnExp.enabled;
		    }
		 
			if(ModConfig.showSortButtons)
			{  
				width = 18;
				int x_spacing = width + padding/2;
				int x = guiLeft + this.xSize - 5*x_spacing - padding+1;
				int y = guiTop + this.ySize - height - padding;
				 
				GuiButton btn;
				 
				btn = new GuiButtonSort(this.mc.thePlayer,button_id++, x, y ,width,height, Const.SORT_LEFTALL,"<<",false);
				this.buttonList.add(btn);

				x += x_spacing;
			 
				btn = new GuiButtonSort(this.mc.thePlayer,button_id++, x, y ,width,height, Const.SORT_LEFT,"<",false);
				this.buttonList.add(btn);

				x += x_spacing;
			 
				btn = new GuiButtonSort(this.mc.thePlayer,button_id++, x, y ,width,height, Const.SORT_SMART,StatCollector.translateToLocal("button.sort"),false);
				this.buttonList.add(btn);
				
				x += x_spacing;

				btn = new GuiButtonSort(this.mc.thePlayer,button_id++, x, y ,width,height, Const.SORT_RIGHT,">",false);
				this.buttonList.add(btn);
				  
				x += x_spacing;
				
				btn = new GuiButtonSort(this.mc.thePlayer,button_id++, x, y ,width,height, Const.SORT_RIGHTALL,">>",false);
				this.buttonList.add(btn);
			}
		}
    }
	
	private void checkSlotsEmpty()
	{
		//TODO: interface-ey stuff to share code more
		final int s = 16;
 
		if(container.invo.getStackInSlot(Const.enderChestSlot) == null)
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
			if(container.invo.getStackInSlot(Const.uncraftSlot) == null)
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
			if(container.invo.getStackInSlot(Const.bottleSlot) == null || 
			   container.invo.getStackInSlot(Const.bottleSlot).getItem() == Items.experience_bottle	)
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

		if(container.invo.getStackInSlot(Const.enderPearlSlot) == null)
		{  
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotEnderPearl.background,SlotEnderPearl.posX, SlotEnderPearl.posY,s,s);
		}

		if(container.invo.getStackInSlot(Const.compassSlot) == null)
		{ 
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotCompass.background,SlotCompass.posX, SlotCompass.posY,s,s);
		}

		if(container.invo.getStackInSlot(Const.clockSlot) == null)
		{  
			if(ModConfig.enableSlotOutlines)
				UtilTextureRender.drawTextureSimple(SlotClock.background,SlotClock.posX, SlotClock.posY,s,s);
		}
		//now do all armor

		if(ModConfig.enableSlotOutlines)
		{
			int armorLeft =  Const.padding+2;
			int armorTop = Const.padding+2;
	//		
			
			int sq = s + 2;//sep btw armor slots
			
			
			if(container.invo.armorInventory[0] == null)
			{
				UtilTextureRender.drawTextureSimple("textures/items/empty_armor_slot_boots.png",armorLeft, armorTop+3*sq,s,s);
			}
			if(container.invo.armorInventory[1] == null)
			{
				UtilTextureRender.drawTextureSimple("textures/items/empty_armor_slot_leggings.png",armorLeft, armorTop+2*sq,s,s);
			}
			if(container.invo.armorInventory[2] == null)
			{
				UtilTextureRender.drawTextureSimple("textures/items/empty_armor_slot_chestplate.png",armorLeft, armorTop+1*sq,s,s);
			}
			if(container.invo.armorInventory[3] == null)
			{
				UtilTextureRender.drawTextureSimple("textures/items/empty_armor_slot_helmet.png",armorLeft, armorTop+0*sq,s,s);				
			}
		}
	}
	 
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{ 
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);//so it does not change scale
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, backgroundTexture));

        UtilTextureRender.drawTexturedQuadFit(this.guiLeft, this.guiTop,texture_width,texture_height);
 
        if(ModConfig.showCharacter)//drawEntityOnScreen
        	drawEntityOnScreen(this.guiLeft + 51, this.guiTop + 75, 30, (float)(this.guiLeft + 51) - (float)mouseX, (float)(this.guiTop + 75 - 50) - (float)mouseY, this.mc.thePlayer);
	
        //if feature is enabled, draw these
        if(ModConfig.enableEnchantBottles)
        	drawSlotAt(Const.bottleX, Const.bottleY);

        if(ModConfig.enableUncrafting)
        	drawSlotAt(Const.uncraftX, Const.uncraftY);
	}

	private void drawSlotAt(int x, int y)
	{
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, "textures/gui/inventory_slot.png"));
         
        //was this.drawTexturedModalRect
        UtilTextureRender.drawTexturedQuadFit(this.guiLeft+ x -1, this.guiTop+ y -1,  Const.SQ, Const.SQ);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{ 
		this.checkSlotsEmpty();
		 
		//this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 87, 32, 4210752);
		
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
