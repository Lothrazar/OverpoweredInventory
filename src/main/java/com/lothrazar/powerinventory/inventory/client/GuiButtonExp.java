package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.proxy.ExpButtonPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket;
import com.lothrazar.powerinventory.ModInv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonExp extends GuiButton 
{  
    public GuiButtonExp(int buttonId, int x, int y, int w,int h,  String text)
    {
    	super(buttonId, x, y, w,h, text );  
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{   
    		ModInv.instance.network.sendToServer(new ExpButtonPacket(new NBTTagCompound()));
    	}
    	
    	return pressed;
    }
}

