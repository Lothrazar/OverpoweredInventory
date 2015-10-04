package com.lothrazar.powerinventory.net;

import com.lothrazar.powerinventory.*;
import com.lothrazar.powerinventory.inventory.ContainerCustomPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class ExpButtonPacket implements IMessage , IMessageHandler<ExpButtonPacket, IMessage>
{
	public ExpButtonPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public ExpButtonPacket(NBTTagCompound ptags)
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
	public IMessage onMessage(ExpButtonPacket message, MessageContext ctx)
	{
		EntityPlayer player = ctx.getServerHandler().playerEntity;
	 
		IInventory invo;
		if(player.openContainer instanceof ContainerCustomPlayer)
		{
			ContainerCustomPlayer c = (ContainerCustomPlayer)player.openContainer ;
			invo = c.invo;
		}
		else
		{
			invo = player.inventory;
		}
		
		ItemStack bottles = invo.getStackInSlot(Const.bottleSlot);
		//in the game, they drop between 3 and 11 experience //src http://minecraft.gamepedia.com/Bottle_o'_Enchanting
	 
		if(bottles != null && bottles.getItem() == Items.glass_bottle)
		{
			double current = UtilExperience.getExpTotal(player);
			
			//so how many times can we subtract ModConfig.expPerBottle from current?
			//if i have 100 exp, and each bottle costs 5, then i can fill  100/5 = 20 bottles
			
			int bottlesToDrain = MathHelper.floor_double(current / ModConfig.expPerBottle);
			
			//but wait, how many physical bottles are present? we may not have enough for all the exp
			//if we can fill 17, but ony have 16, just do the whole stack
			if(bottlesToDrain >= bottles.stackSize)
			{
				bottlesToDrain = bottles.stackSize;   //just do the whole thing
			}
	  
			if(bottlesToDrain > 0)
			{ 
				UtilExperience.drainExp(player, bottlesToDrain * ModConfig.expPerBottle);
				
				invo.setInventorySlotContents(Const.bottleSlot, new ItemStack(Items.experience_bottle,bottlesToDrain));
			
			
				if(bottlesToDrain < bottles.stackSize) 
				{	
					//what iff:
					//we cannot set the whole stack, the whole slot because that would waste empties
					//drop the empty ones in the world to pick up 
					int emptyBottlesLeft = bottles.stackSize - bottlesToDrain;
					
					player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj,
		 					player.posX,player.posY,player.posZ,
		 					new ItemStack(Items.glass_bottle,emptyBottlesLeft)));
				}
			} 
		}
		
		return null; 
	}
}
