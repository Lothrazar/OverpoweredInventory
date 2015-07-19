package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.proxy.FilterButtonPacket;

import com.lothrazar.powerinventory.ModMutatedInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonFilter extends GuiButton 
{
	//imported from https://github.com/PrinceOfAmber/SamsPowerups , author Lothrazar aka Sam Bassett
	//private EntityPlayer player;
    public GuiButtonFilter(int buttonId, int x, int y, int w,int h)
    {
    	super(buttonId, x, y, w,h, StatCollector.translateToLocal("button.filter"));
    	//this.player = player;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		//do what the button is meant to do
    	
    		//send packet to server from client (this) makes sense
    		NBTTagCompound tags = new NBTTagCompound();

    		ModMutatedInventory.instance.network.sendToServer(new FilterButtonPacket(tags));
    	}
    	
    	return pressed;
    }
}

