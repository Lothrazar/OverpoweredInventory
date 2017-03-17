package com.lothrazar.powerinventory.inventory;
import java.util.Arrays;
import com.lothrazar.powerinventory.CapabilityRegistry;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.InventoryRenderer;
import com.lothrazar.powerinventory.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.inventory.button.*;
import com.lothrazar.powerinventory.inventory.slot.*;
import com.lothrazar.powerinventory.util.UtilTextureRender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiOverpowered extends GuiContainer {
  private ResourceLocation bkg = new ResourceLocation(Const.MODID, "textures/gui/inventory.png");
  private ResourceLocation bkg_large = new ResourceLocation(Const.MODID, "textures/gui/inventory_large.png");
  private ResourceLocation bkg_3x9 = new ResourceLocation(Const.MODID, "textures/gui/slots3x9.png");
  public static ResourceLocation slot = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot.png");
  public static boolean SHOW_DEBUG_NUMS = false;
  //	private final InventoryOverpowered inventory;
  private ContainerOverpowered container;
  final int padding = 6;// on the far outer sizes
  final EntityPlayer thePlayer;
  public GuiOverpowered(EntityPlayer player, InventoryPlayer inventoryPlayer) {
    // the player.inventory gets passed in here
    super(new ContainerOverpowered(player, inventoryPlayer));
    container = (ContainerOverpowered) this.inventorySlots;
    //		inventory = container.invo;
    thePlayer = player;
    // fixed numbers from the .png resource size
    this.xSize = ModConfig.getInvoWidth();
    this.ySize = ModConfig.getInvoHeight();
  }
  @Override
  public void initGui() {
    super.initGui();
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(thePlayer);
    int h = 20;
    int w = 20;// default button dims
    int button_id = 99;
    if (prop.isEPearlUnlocked() == false) {
      GuiButtonUnlockPearl b = new GuiButtonUnlockPearl(button_id++,
          this.guiLeft + padding,
          this.guiTop + padding, thePlayer, ModConfig.expCostPearl);
      this.buttonList.add(b);
    }
    if (ModConfig.getMaxSections() > 1) {
      int wid = 50;
      int localStart = this.guiLeft + 70 + 2 * padding;
      // 70
      GuiButtonSort sb = new GuiButtonSort(button_id++,
          localStart, //pearl button has 70 w 
          this.guiTop + padding, wid);
      this.buttonList.add(sb);
      GuiButtonFilter fb = new GuiButtonFilter(button_id++,
          localStart + wid + padding,
          this.guiTop + padding, wid);
      this.buttonList.add(fb);
      GuiButtonDump db = new GuiButtonDump(button_id++,
          localStart + 2 * (wid + padding),
          this.guiTop + padding, wid);
      this.buttonList.add(db);
    }
    if (prop.isEChestUnlocked() == false) {
      GuiButtonUnlockChest b = new GuiButtonUnlockChest(button_id++,
          this.guiLeft + ModConfig.getInvoWidth() - padding - GuiButtonUnlockChest.width,
          this.guiTop + padding,
          thePlayer, ModConfig.expCostEChest);
      this.buttonList.add(b);
    }
    // draw only one single button then stop
    int expCost = ModConfig.expCostStorage_start;
    for (int i = 1; i <= ModConfig.getMaxSections(); i++) {
      if (prop.hasStorage(i) == false) {
        GuiButtonUnlockStorage b = new GuiButtonUnlockStorage(button_id++,
            this.guiLeft + InventoryRenderer.xPosBtn(i),
            this.guiTop + InventoryRenderer.yPosBtn(i), thePlayer, expCost, i);
        this.buttonList.add(b);
        break;
      }
      expCost += ModConfig.expCostStorage_inc;
    }
    w = 6;
    h = 8;
    for (int i = 1; i <= ModConfig.getMaxSections(); i++) {
      if (prop.hasStorage(i))
        this.buttonList.add(new GuiButtonRotate(button_id++,
            this.guiLeft + InventoryRenderer.xPosSwap(i),
            this.guiTop + InventoryRenderer.yPosSwap(i),
            w, h, "", i));
    }
  }
  @Override
  public void drawScreen(int x, int y, float par3) {
    super.drawScreen(x, y, par3);
    GuiButton btn;
    for (int i = 0; i < buttonList.size(); i++) {
      btn = buttonList.get(i);
      if (btn instanceof IGuiTooltip && btn.isMouseOver()) {
        String tooltip = ((IGuiTooltip) btn).getTooltip();
        if (tooltip != null) {
          // it takes a list, one on each line. but we use single line
          // tooltips
          drawHoveringText(Arrays.asList(new String[] { tooltip }), x, y, fontRendererObj);
        }
      }
    }
    if (SHOW_DEBUG_NUMS) {
      for (Slot s : this.container.inventorySlots) {
        // each slot has two different numbers. the slotNumber is
        // UNIQUE, the index is not
        this.drawString(this.fontRendererObj, "" + s.getSlotIndex(), this.guiLeft + s.xPos, this.guiTop + s.yPos + 4, 16777120);// font
        // color
      }
    }
  }
  final int s = 16;
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(thePlayer);
    if (prop.isEPearlUnlocked() && container.invo.getStackInSlot(Const.SLOT_EPEARL) ==  ItemStack.EMPTY) {
      UtilTextureRender.drawTextureSimple(SlotEnderPearl.background, SlotEnderPearl.posX, SlotEnderPearl.posY, s, s);
    }
    if (prop.isEChestUnlocked() && container.invo.getStackInSlot(Const.SLOT_ECHEST) ==  ItemStack.EMPTY) {
      UtilTextureRender.drawTextureSimple(SlotEnderChest.background, SlotEnderChest.posX, SlotEnderChest.posY, s, s);
    }
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    if (ModConfig.isLargeScreen())
      UtilTextureRender.drawTextureSimple(bkg_large, this.guiLeft, this.guiTop, this.xSize, this.ySize);
    else
      UtilTextureRender.drawTextureSimple(bkg, this.guiLeft, this.guiTop, this.xSize, this.ySize);
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(thePlayer);
    for (int i = 1; i <= ModConfig.getMaxSections(); i++) {
      if (prop.hasStorage(i))
        drawSlotSectionAt(this.guiLeft + InventoryRenderer.xPosTexture(i), this.guiTop + InventoryRenderer.yPosTexture(i));
    }
    if (prop.isEChestUnlocked()) {
      drawSlotAt(SlotEnderChest.posX, SlotEnderChest.posY);
    }
    if (prop.isEPearlUnlocked()) {
      drawSlotAt(SlotEnderPearl.posX, SlotEnderPearl.posY);
    }
  }
  private void drawSlotSectionAt(int x, int y) {
    UtilTextureRender.drawTextureSimple(bkg_3x9, x, y, Const.SLOTS_WIDTH, Const.SLOTS_HEIGHT);
  }
  private void drawSlotAt(int x, int y) {
    UtilTextureRender.drawTextureSimple(slot, this.guiLeft + x - 1, this.guiTop + y - 1, Const.SQ, Const.SQ);
  }
}
