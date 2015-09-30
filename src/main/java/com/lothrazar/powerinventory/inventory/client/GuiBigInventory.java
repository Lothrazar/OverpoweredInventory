package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.Const; 
import com.lothrazar.powerinventory.ModConfig;
import com.lothrazar.powerinventory.inventory.BigContainerPlayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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


	GuiButton btnEnder;
	GuiButton btnExp;
	GuiButton btnUncraft;
	public GuiBigInventory(EntityPlayer player)
	{
		super(player);
		container = player.inventoryContainer instanceof BigContainerPlayer? (BigContainerPlayer)player.inventoryContainer : null;
		this.xSize = Const.texture_width;
		this.ySize = Const.texture_height;
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
			 
			if(ModConfig.showMergeDeposit)
			{
				this.buttonList.add(new GuiButtonDump(button_id++,
						this.guiLeft + this.xSize - widthlrg - padding, 
						this.guiTop + padding,
						widthlrg,height));
	
				this.buttonList.add(new GuiButtonFilter(button_id++,
						this.guiLeft + this.xSize - widthlrg - 2*padding - widthlrg, 
						this.guiTop + padding,
						widthlrg,height));
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
					this.guiLeft + Const.echestX + 19, 
					this.guiTop + Const.echestY - 1,
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
 
			drawTextureSimple("textures/items/empty_enderchest.png",Const.echestX, Const.echestY,s,s); 
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
	  
				drawTextureSimple("textures/items/empty_bottle.png",Const.bottleX, Const.bottleY,s,s); 
			}
			else 
			{ 
				btnExp.enabled = true; 
				btnExp.visible = btnExp.enabled;
			}

		if(container.invo.getStackInSlot(Const.enderPearlSlot) == null)
		{  
			drawTextureSimple("textures/items/empty_enderpearl.png",Const.pearlX, Const.pearlY,s,s);
		}

		if(container.invo.getStackInSlot(Const.compassSlot) == null)
		{ 
			drawTextureSimple("textures/items/empty_compass.png",Const.compassX, Const.compassY,s,s);
		}

		if(container.invo.getStackInSlot(Const.clockSlot) == null)
		{  
			drawTextureSimple("textures/items/empty_clock.png",Const.clockX, Const.clockY,s,s);
		}
	}
	 //TODO: this is double din both guis?
	public void drawTextureSimple(String texture,double x, double y, double width, double height)
	{
		//wrapper for drawTexturedQuadFit
		this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, texture)); 
		Const.drawTexturedQuadFit(x,y,width,height);
	}

	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{ 
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);//so it does not change scale
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Const.MODID, Const.INVENTORY_TEXTURE));

        Const.drawTexturedQuadFit(this.guiLeft, this.guiTop,this.xSize,this.ySize);
 
        if(ModConfig.showCharacter)//drawEntityOnScreen
        	func_147046_a(this.guiLeft + 51, this.guiTop + 75, 30, (float)(this.guiLeft + 51) - (float)mouseX, (float)(this.guiTop + 75 - 50) - (float)mouseY, this.mc.thePlayer);
	
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
        Const.drawTexturedQuadFit(this.guiLeft+ x -1, this.guiTop+ y -1,  Const.square, Const.square);
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
