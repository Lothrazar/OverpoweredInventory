package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.net.MessageRotateInv;
import com.lothrazar.powerinventory.ModInv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonRotate extends GuiButton 
{
	final static int height = 20;
	final static int width = 20;
	private int invoGroup;
    public GuiButtonRotate(int buttonId, int x, int y, int ig)
    {
    	super(buttonId, x, y, width,height, "");
    	invoGroup = ig;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		NBTTagCompound tags = new NBTTagCompound();
    		tags.setInteger("i", invoGroup);
			ModInv.instance.network.sendToServer(new MessageRotateInv(tags));
    	}
    	
    	return pressed;
    }
}

