package com.lothrazar.powerinventory.proxy;

import java.util.ArrayList;

import com.lothrazar.powerinventory.*;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
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
		
		//in the game, they drop between 3 and 11 experience //src http://minecraft.gamepedia.com/Bottle_o'_Enchanting
		//int e =ModConfig.expPerBottle;
		
		ItemStack bottles = player.inventory.getStackInSlot(Const.bottleSlot);
		
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
				bottlesToDrain = bottles.stackSize; 
				//just do the whole thing
			}
			else //therefore bottlesToDrain < bottles.stackSize
			{
				//so we can drain them all BUT
				//we cannot set the whole stack, the whole slot because that would waste empties
				//drop the empty ones in the world to pick up
				
				int emptyBottlesLeft = bottles.stackSize - bottlesToDrain;
	 
				if(player.worldObj.isRemote  ==  false)//this always passes because of the network packets, but keep it for consistency
				{	
					player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj,
		 					player.getPosition().getX(),player.getPosition().getY(),player.getPosition().getZ(),
		 					new ItemStack(Items.glass_bottle,emptyBottlesLeft)));
				}
		 
			}

			//now , in either case, we can drain the exp into the bottles
			
			if(bottlesToDrain > 0)
			{ 
				UtilExperience.drainExp(player, bottlesToDrain * ModConfig.expPerBottle);
				
				player.inventory.setInventorySlotContents(Const.bottleSlot, new ItemStack(Items.experience_bottle,bottlesToDrain));
			} 
		}
		
		return null; 
	}
}
