package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.net.OpenInventoryPacket;
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
public class GuiButtonOpenInventory extends GuiButton 
{
	private int invType;
    public GuiButtonOpenInventory(int buttonId, int x, int y, int w,int h, String text,int type)
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
    	

        		NBTTagCompound tags = new NBTTagCompound();
        		tags.setInteger("i", invType);//TODO: use const.nbt flag
    			ModInv.instance.network.sendToServer(new OpenInventoryPacket(tags));
    	
    	}
    	
    	return pressed;
    }
}

