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
public class GuiButtonInventory extends GuiButton 
{
	private int invType;
    public GuiButtonInventory(int buttonId, int x, int y, int w,int h, String text,int type)
    {
    	super(buttonId, x, y, w,h, text);
    	invType = type;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		switch(invType)
    		{
    		case ModInv.INV_PLAYER:

        		//some GUI's open on client side to initiate, and propogate to server internally
    			Minecraft.getMinecraft().displayGuiScreen(new GuiBigInventory(mc.thePlayer));
    			break;
    		case ModInv.INV_ENDER:
    			//other GUI's have to be hit from server side first to open
        		//send packet to server from client (this) makes sense
        		NBTTagCompound tags = new NBTTagCompound();
        		tags.setInteger("i", invType);
    			ModInv.instance.network.sendToServer(new EnderButtonPacket(tags));
    		break;
    		} 
    	}
    	
    	return pressed;
    }
}

