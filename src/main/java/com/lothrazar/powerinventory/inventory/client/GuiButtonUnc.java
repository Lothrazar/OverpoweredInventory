package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.net.UncButtonPacket;
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
public class GuiButtonUnc extends GuiButton 
{  
    public GuiButtonUnc(int buttonId, int x, int y, int w,int h)
    {
    	super(buttonId, x, y, w,h, StatCollector.translateToLocal("button.unc") );  
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{   
    		//TODO: save inventory as part of button
    		//send item as NBT data in the packet
    	//TODO	if(player.inventory.getStackInSlot(Const.uncraftSlot) != null)
    			ModInv.instance.network.sendToServer(new UncButtonPacket(new NBTTagCompound()));
    	}
    	
    	return pressed;
    }
}

