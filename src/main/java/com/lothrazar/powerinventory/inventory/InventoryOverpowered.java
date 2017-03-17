package com.lothrazar.powerinventory.inventory;
import java.lang.ref.WeakReference;
import com.lothrazar.powerinventory.Const;
import com.lothrazar.powerinventory.ModInv;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.net.PacketSyncExtendedInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class InventoryOverpowered implements IInventory {
  public static int INV_SIZE;
  public ItemStack[] inventory;
  // thanks for
  // http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571597-forge-1-6-4-1-8-custom-inventories-in-items-and
  private final String tagName = "opinvtags";
  private final String tagSlot = "Slot";
  public ItemStack enderPearlStack;
  public ItemStack enderChestStack;
  public WeakReference<EntityPlayer> player;
  public InventoryOverpowered(EntityPlayer player) {
    // always 2 hotbars. the number of sections depends on config (ignoring
    // locked or not per player)
    INV_SIZE = 2 * Const.HOTBAR_SIZE + Const.V_INVO_SIZE * ModConfig.getMaxSections();
    inventory = new ItemStack[INV_SIZE];
    this.player = new WeakReference<EntityPlayer>(player);
  }
  @Override
  public int getSizeInventory() {
    return INV_SIZE;
  }
  @Override
  public ItemStack getStackInSlot(int slot) {
    if (slot == Const.SLOT_EPEARL) { return enderPearlStack; }
    if (slot == Const.SLOT_ECHEST) { return enderChestStack; }
    if (slot >= inventory.length) { return null; }
    return inventory[slot];
  }
  public void dropStackInSlot(EntityPlayer p, int slot) {
    ItemStack itemstack = getStackInSlot(slot);
    if (itemstack != null) {
      p.dropItem(itemstack, false);
    }
    syncSlotToClients(slot);
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack itemstack;
    // TODO: these ifelse brnaches are almost all identical. find a way to
    // share code? make function?
    if (index == Const.SLOT_ECHEST) {
      itemstack = this.enderChestStack;
      this.enderChestStack = null;
      syncSlotToClients(index);
      return itemstack;
    }
    else if (index == Const.SLOT_EPEARL) {
      if (this.enderPearlStack.getCount() <= count) {
        itemstack = this.enderPearlStack;
        this.enderPearlStack =  ItemStack.EMPTY;
        syncSlotToClients(index);
        return itemstack;
      }
      else {
        itemstack = this.enderPearlStack.splitStack(count);
        if (this.enderPearlStack.getCount() == 0) {
          this.enderPearlStack =  ItemStack.EMPTY;
        }
        syncSlotToClients(index);
        return itemstack;
      }
    }
    else {
      int indexCopy = index;
      int countCopy = count;
      ItemStack[] aitemstack = this.inventory;
      if (aitemstack[indexCopy] !=  ItemStack.EMPTY) {
        if (aitemstack[indexCopy].getCount() <= countCopy) {
          itemstack = aitemstack[indexCopy];
          aitemstack[indexCopy] =  ItemStack.EMPTY;
          syncSlotToClients(index);
          return itemstack;
        }
        else {
          itemstack = aitemstack[indexCopy].splitStack(countCopy);
          if (aitemstack[indexCopy].getCount() == 0) {
            aitemstack[indexCopy] =  ItemStack.EMPTY;
          }
          syncSlotToClients(index);
          return itemstack;
        }
      }
      else {
        return null;
      }
    }
  }
  //  private void onInventoryChanged() {
  //    for (int i = 0; i < this.getSizeInventory(); ++i) {
  //      if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0)
  //        this.setInventorySlotContents(i, null);
  //    }
  //    this.markDirty();
  //  }
  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    if (slot == Const.SLOT_EPEARL) {
      enderPearlStack = stack;
    }
    else if (slot == Const.SLOT_ECHEST) {
      enderChestStack = stack;
    }
    else {
      this.inventory[slot] = stack;
    }
    if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
      stack.setCount(this.getInventoryStackLimit());
    }
    syncSlotToClients(slot);
    //    this.onInventoryChanged();
  }
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public void markDirty() {
  }
  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
    return true;
  }
  public void writeToNBT(NBTTagCompound tags) {
    NBTTagList nbttaglist = new NBTTagList();
    NBTTagCompound tagcompound;
    for (int i = 0; i < this.getSizeInventory(); ++i) {
      if (this.getStackInSlot(i) != null) {
        tagcompound = new NBTTagCompound();
        tagcompound.setInteger(tagSlot, i);
        this.getStackInSlot(i).writeToNBT(tagcompound);
        nbttaglist.appendTag(tagcompound);
      }
    }
    if (this.enderChestStack != null) {
      tagcompound = new NBTTagCompound();
      tagcompound.setInteger(tagSlot, Const.SLOT_ECHEST);
      this.enderChestStack.writeToNBT(tagcompound);
      nbttaglist.appendTag(tagcompound);
    }
    if (this.enderPearlStack != null) {
      tagcompound = new NBTTagCompound();
      tagcompound.setInteger(tagSlot, Const.SLOT_EPEARL);
      this.enderPearlStack.writeToNBT(tagcompound);
      nbttaglist.appendTag(tagcompound);
    }
    tags.setTag(tagName, nbttaglist);
  }
  public void readFromNBT(NBTTagCompound tagcompound) {
    NBTTagList nbttaglist = tagcompound.getTagList(tagName, Constants.NBT.TAG_COMPOUND);
    ItemStack itemstack;
    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
      NBTTagCompound tags = nbttaglist.getCompoundTagAt(i);// tagAt
      int b = tags.getInteger(tagSlot);
      itemstack =new ItemStack(tags);// ItemStack.loadItemStackFromNBT(tags);
      if (b >= 0 && b < this.getSizeInventory()) {
        this.setInventorySlotContents(b, itemstack);
      }
      else if (itemstack != null) {
        if (b == Const.SLOT_EPEARL) {
          enderPearlStack = itemstack;
        }
        if (b == Const.SLOT_ECHEST) {
          enderChestStack = itemstack;
        }
      }
    }
  }
  @Override
  public boolean hasCustomName() {
    return false;
  }
  @Override
  public void openInventory(EntityPlayer player) {
  }
  @Override
  public void closeInventory(EntityPlayer player) {
  }
  @Override
  public int getField(int id) {
    return 0;
  }
  @Override
  public void setField(int id, int value) {
  }
  @Override
  public int getFieldCount() {
    return 0;
  }
  @Override
  public void clear() {
  }
  @Override
  public String getName() {
    return null;
  }
  @Override
  public ItemStack removeStackFromSlot(int slot) {
    // was getStackInSlotOnClosing
    ItemStack stack = getStackInSlot(slot);
    if (stack.isEmpty()) {
      setInventorySlotContents(slot, ItemStack.EMPTY);
    }
    return stack;
  }
  @Override
  public ITextComponent getDisplayName() {
    // TODO Auto-generated method stub
    return null;
  }
  public void syncSlotToClients(int slot) {
    try {
      if (ModInv.proxy.getClientWorld() == null) {
        ModInv.instance.network.sendToAll(new PacketSyncExtendedInventory(player.get(), slot));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }
  @Override
  public boolean isUsableByPlayer(EntityPlayer player) {
    // TODO Auto-generated method stub
    return true;
  }
}
