package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.OpenInventoryPacket;
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
public class GuiButtonOpenInventory extends GuiButton 
{
    public GuiButtonOpenInventory(int buttonId, int x, int y, int w,int h)
    {
    	super(buttonId, x, y, w,h, StatCollector.translateToLocal("button.cornerinvo"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		NBTTagCompound tags = new NBTTagCompound();
			ModInv.instance.network.sendToServer(new OpenInventoryPacket(tags));
    	}
    	
    	return pressed;
    }
}

