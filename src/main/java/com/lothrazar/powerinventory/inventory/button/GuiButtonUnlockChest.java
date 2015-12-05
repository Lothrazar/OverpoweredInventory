package com.lothrazar.powerinventory.inventory.button;

import com.lothrazar.powerinventory.net.UnlockChestPacket;
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
public class GuiButtonUnlockChest extends GuiButton implements IGuiTooltip 
{
	final static int height = 20;
	final static int width = 70;
	private String tooltip;
    public GuiButtonUnlockChest(int buttonId, int x, int y, String label)
    {
    	super(buttonId, x, y, width,height, label);
    	this.setTooltip(StatCollector.translateToLocal("tooltip.craftexp"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		NBTTagCompound tags = new NBTTagCompound();
			ModInv.instance.network.sendToServer(new UnlockChestPacket(tags));
    	}
    	
    	return pressed;
    }

	@Override
	public String getTooltip()
	{
		return tooltip;
	}

	@Override
	public void setTooltip(String s)
	{
		tooltip = s;
	}
}

