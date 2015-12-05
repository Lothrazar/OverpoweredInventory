package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.PlayerPersistProperty;
import com.lothrazar.powerinventory.config.ModConfig;
import com.lothrazar.powerinventory.util.UtilExperience;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
 
public class UnlockPearlPacket implements IMessage , IMessageHandler<UnlockPearlPacket, IMessage>
{
	public UnlockPearlPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public UnlockPearlPacket(NBTTagCompound ptags)
	{
		tags = ptags;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.tags);
	}

	@Override
	public IMessage onMessage(UnlockPearlPacket message, MessageContext ctx)
	{
		EntityPlayer p = ctx.getServerHandler().playerEntity;

		PlayerPersistProperty prop = PlayerPersistProperty.get(p);
		
		if(UtilExperience.getExpTotal(p) >= ModConfig.expCostPearl){
			
			UtilExperience.drainExp(p, ModConfig.expCostPearl);
			
			prop.setInvoEPearl( true  );
			
			p.closeScreen();
			
			p.worldObj.playSoundAtEntity(p,"mob.zombie.unfect", 1.4F, 1F);
		}
		else{

			p.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("gui.craftexp")));
		}
		
		return null;
	}
}