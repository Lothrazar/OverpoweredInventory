package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.net.FilterButtonPacket;
import com.lothrazar.powerinventory.ModInv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonFilter extends GuiButton 
{
	//imported from https://github.com/PrinceOfAmber/SamsPowerups , author Lothrazar aka Sam Bassett
    public GuiButtonFilter(int buttonId, int x, int y, int w,int h,String t)
    {
    	super(buttonId, x, y, w,h,t);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{ 
    		ModInv.instance.network.sendToServer(new FilterButtonPacket(new NBTTagCompound()));
    	}
    	
    	return pressed;
    }
}

