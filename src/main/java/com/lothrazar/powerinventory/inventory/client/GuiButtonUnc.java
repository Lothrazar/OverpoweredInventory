package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.proxy.ExpButtonPacket;
import com.lothrazar.powerinventory.proxy.SortButtonPacket;
import com.lothrazar.powerinventory.proxy.UncButtonPacket;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonUnc extends GuiButton 
{  
    public GuiButtonUnc(int buttonId, int x, int y, int w,int h,  String text)
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
    	//TODO	if(player.inventory.getStackInSlot(Const.uncraftSlot) != null)
    			ModInv.instance.network.sendToServer(new UncButtonPacket(new NBTTagCompound()));
    	}
    	
    	return pressed;
    }
}
