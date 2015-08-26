package com.lothrazar.powerinventory.inventory.client;

import com.lothrazar.powerinventory.proxy.SortButtonPacket;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.UtilInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonSort extends GuiButton 
{ 
	private int sortType;
	private EntityPlayer player;
	private boolean doManualSync = true;
    public GuiButtonSort(EntityPlayer p,int buttonId, int x, int y, int w,int h,  int sort, String text, boolean ms)
    {
    	super(buttonId, x, y, w,h, text ); 
    	sortType = sort;
    	player=p;
    	doManualSync = ms;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{ 
    		NBTTagCompound tags = new NBTTagCompound();
  
    		tags.setInteger(SortButtonPacket.NBT_SORT, sortType);
    		
    		ModInv.instance.network.sendToServer(new SortButtonPacket(tags));//does server
    		
    		//we only NEEDto do this in the upper right outer buttons, not the ones INSDE the player GUIInventory
    		//forum thread old but related http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1429140-forge-pushing-server-side-changes-ontileentity-to
    		
    		if(doManualSync)
    			UtilInventory.doSort( player,sortType);//does client by hand, so it stays aligned with server
    	}
    	
    	return pressed;
    }
}

