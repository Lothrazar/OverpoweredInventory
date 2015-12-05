package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.UnlockCraftPacket;
import com.lothrazar.powerinventory.ModInv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonUnlockCraft extends GuiButton 
{
	final static int height = 20;
	final static int width = 70;
	public final static String tooltip = "button.craftexp";
    public GuiButtonUnlockCraft(int buttonId, int x, int y, String label)
    {
    	super(buttonId, x, y, width,height, label);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		NBTTagCompound tags = new NBTTagCompound();
			ModInv.instance.network.sendToServer(new UnlockCraftPacket(tags));
    	}
    	
    	return pressed;
    }
}

