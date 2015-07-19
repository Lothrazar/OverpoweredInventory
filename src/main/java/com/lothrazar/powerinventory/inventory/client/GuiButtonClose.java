package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.proxy.EnderButtonPacket;

import com.lothrazar.powerinventory.ModInv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonClose extends GuiButton 
{
    public GuiButtonClose(int buttonId, int x, int y, int w,int h)
    {
    	super(buttonId, x, y, w,h, "X");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		mc.thePlayer.closeScreen();
    	}
    	
    	return pressed;
    }
}

