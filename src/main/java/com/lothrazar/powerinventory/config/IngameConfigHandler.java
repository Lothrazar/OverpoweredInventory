package com.lothrazar.powerinventory.config;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class IngameConfigHandler implements IModGuiFactory {
  @Override
  public void initialize(Minecraft mc) {
  }

  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }

  @Override
  public boolean hasConfigGui() {
    return false;
  }
  @Override
  public GuiScreen createConfigGui(GuiScreen parentScreen) {
    return null;
  }
}
