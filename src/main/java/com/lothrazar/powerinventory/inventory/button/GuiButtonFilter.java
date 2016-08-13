package com.lothrazar.powerinventory.inventory.button;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.net.FilterButtonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class GuiButtonFilter extends GuiButton implements IGuiTooltip {
  // imported from https://github.com/PrinceOfAmber/SamsPowerups , author
  // Lothrazar aka Sam Bassett
  public GuiButtonFilter(int buttonId, int x, int y, int w) {
    super(buttonId, x, y, w, 20, ModInv.lang("button.filter"));
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModInv.instance.network.sendToServer(new FilterButtonPacket(new NBTTagCompound()));
    }
    return pressed;
  }
  @Override
  public String getTooltip() {
    return ModInv.lang("tooltip.filter");
  }
  @Override
  public void setTooltip(String s) {
  }
}
