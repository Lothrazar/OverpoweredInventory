package com.lothrazar.powerinventory;
import com.lothrazar.powerinventory.inventory.InventoryOverpowered;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityRegistry {
  public static void register() {
    CapabilityManager.INSTANCE.register(IPlayerExtendedProperties.class, new Storage(),
        InstancePlayerExtendedProperties.class);
  }
  public static IPlayerExtendedProperties getPlayerProperties(EntityPlayer player) {
    if (player == null) {
      ModInv.logger.error("Null player, cannot get properties");
      return null;
    }
    IPlayerExtendedProperties props = player.getCapability(ModInv.CAPABILITYSTORAGE, null);
    if(props.getItems() == null){
      props.setItems(new InventoryOverpowered(player));
    }
    return props;
  }
  public interface IPlayerExtendedProperties {
    boolean isEPearlUnlocked();
    void setEPearlUnlocked(boolean value);
    boolean isEChestUnlocked();
    void setEChestUnlocked(boolean value);
    int getStorageCount();
    void setStorageCount(int value);
    InventoryOverpowered getItems();
    void setItems(InventoryOverpowered value);
    NBTTagCompound getDataAsNBT();
    void setDataFromNBT(NBTTagCompound nbt);
    //summary
    boolean hasStorage(int k);
  }
  public static class InstancePlayerExtendedProperties implements IPlayerExtendedProperties {
    private boolean hasEPearl = false;
    private boolean hasEChest = false;
    private int storageCount = 20;
    private InventoryOverpowered invo;
    @Override
    public NBTTagCompound getDataAsNBT() {
      NBTTagCompound tags = new NBTTagCompound();
      tags.setByte("isEPearlUnlocked", (byte) (this.isEPearlUnlocked() ? 1 : 0));
      tags.setByte("isEChestUnlocked", (byte) (this.isEChestUnlocked() ? 1 : 0));
      tags.setInteger("getStorageCount", this.getStorageCount());
      if (invo != null) {
        invo.writeToNBT(tags);
      }
      return tags;
    }
    @Override
    public void setDataFromNBT(NBTTagCompound nbt) {
      NBTTagCompound tags;
      if (nbt instanceof NBTTagCompound == false) {
        tags = new NBTTagCompound();
      }
      else {
        tags = (NBTTagCompound) nbt;
      }
      this.setEPearlUnlocked(tags.getByte("isEPearlUnlocked") == 1);
      this.setEChestUnlocked(tags.getByte("isEChestUnlocked") == 1);
      this.setStorageCount(tags.getInteger("getStorageCount"));
      if(invo != null){
        invo.readFromNBT(tags);
      }
    }
    @Override
    public boolean isEPearlUnlocked() {
      return this.hasEPearl;
    }
    @Override
    public void setEPearlUnlocked(boolean value) {
      this.hasEPearl = value;
    }
    @Override
    public boolean isEChestUnlocked() {
      return this.hasEChest;
    }
    @Override
    public void setEChestUnlocked(boolean value) {
      this.hasEChest = value;
    }
    @Override
    public int getStorageCount() {
      if(this.storageCount == 20){
        this.storageCount-=20;//??HAXS. still not sure about why that happened
      }
      return this.storageCount;
    }
    @Override
    public void setStorageCount(int value) {
      this.storageCount = value;
    }
    @Override
    public boolean hasStorage(int k) {
      return this.getStorageCount() >= k;
    }
    @Override
    public InventoryOverpowered getItems() {
      return invo;
    }
    @Override
    public void setItems(InventoryOverpowered value) {
      invo = value;
    }
  }
  public static class Storage implements IStorage<IPlayerExtendedProperties> {
    @Override
    public NBTTagCompound writeNBT(Capability<IPlayerExtendedProperties> capability, IPlayerExtendedProperties instance, EnumFacing side) {
      return instance.getDataAsNBT();
    }
    @Override
    public void readNBT(Capability<IPlayerExtendedProperties> capability, IPlayerExtendedProperties instance, EnumFacing side, NBTBase nbt) {
      try {
        instance.setDataFromNBT((NBTTagCompound) nbt);
      }
      catch (Exception e) {
        ModInv.logger.error("Invalid NBT compound " );
        e.printStackTrace();
      }
    }
  }
  public static void syncServerDataToClient(EntityPlayerMP p) {
    if (p == null) { return; }
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(p);
    if (props != null) {
      ModInv.instance.network.sendTo(new PacketSyncPlayerData(props.getDataAsNBT()), p);
    }
  }
}
