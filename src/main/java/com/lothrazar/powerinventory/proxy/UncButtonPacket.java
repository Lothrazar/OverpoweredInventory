package com.lothrazar.powerinventory.proxy;

import java.util.ArrayList;

import com.lothrazar.powerinventory.*;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
/** 
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class UncButtonPacket implements IMessage , IMessageHandler<UncButtonPacket, IMessage>
{
	public UncButtonPacket() {}
	NBTTagCompound tags = new NBTTagCompound(); 
	
	public UncButtonPacket(NBTTagCompound ptags)
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

	private void dropItems(EntityPlayer player, ItemStack s)
	{ 
		//this fn is null safe, it gets nulls all the time
		if(s == null || s.getItem() == Items.milk_bucket){return;}
		//also, when crafting cake you get the empty bucket back.
		//so dont refund full buckets or else thats free infinite iron
		
		World w = player.worldObj;
		double x = player.posX;
		double y = player.posY;
		double z = player.posZ;

		ItemStack stack = s.copy();
		stack.stackSize = 1;
		//we set to 1 because recipe registry is bugged in some forge versions
		//EXAMPLE: crafting a Hay Bale takes 9 wheat, so 9 stacks of 1
		//but forge tells me its 9 stacks of 9 !?!?
		
		w.spawnEntityInWorld(new EntityItem(w, x,y,z,stack));
	}
	
	@Override
	public IMessage onMessage(UncButtonPacket message, MessageContext ctx)
	{
		EntityPlayer player = ctx.getServerHandler().playerEntity;
  
		ItemStack toUncraft = player.inventory.getStackInSlot(Const.uncraftSlot);
 
		int i;
		Object maybeOres;
		int outsize = 0;

		//outsize is 3 means the recipe makes three items total. so MINUS three
		//from the toUncraft for EACH LOOP
		if(toUncraft != null)
		for(Object next : CraftingManager.getInstance().getRecipeList())
		{
			//check ore dictionary for some
			 
			if(next instanceof ShapedOreRecipe)
			{
				ShapedOreRecipe r = (ShapedOreRecipe) next;
 
				if(r.getRecipeOutput().isItemEqual(toUncraft))
				{
					outsize = r.getRecipeOutput().stackSize;
				
					if(toUncraft.stackSize >= outsize)
					{
						for(i = 0; i < r.getInput().length; i++) 
						{
							maybeOres = r.getInput()[i];

							if(maybeOres instanceof ArrayList && (ArrayList<ItemStack>)maybeOres != null)//<ItemStack>
							{ 
								ArrayList<ItemStack> ores = (ArrayList<ItemStack>)maybeOres;
							
								if(ores.size() == 1)
								{
									//sticks,iron,and so on
									dropItems(player, ores.get(0));
								}
								//else size is > 1 , so its something like wooden planks
								//TODO:maybe with a config file or something, but not for now
							}
							if(maybeOres instanceof ItemStack)//<ItemStack>
							{
								dropItems(player, (ItemStack)maybeOres); 
							} 
						}
					}
					break;
				}
			}
			else if(next instanceof ShapelessOreRecipe)
			{
				ShapelessOreRecipe r = (ShapelessOreRecipe) next;
		
				if(r.getRecipeOutput().isItemEqual(toUncraft))
				{
					outsize = r.getRecipeOutput().stackSize;
					
					if(toUncraft.stackSize >= outsize)
					{
						for(i = 0; i < r.getInput().size(); i++) 
						{
							maybeOres = r.getInput().get(i);

							if(maybeOres instanceof ArrayList && (ArrayList<ItemStack>)maybeOres != null)//<ItemStack>
							{ 
								ArrayList<ItemStack> ores = (ArrayList<ItemStack>)maybeOres;
							
								if(ores.size() == 1)
								{
									dropItems(player, ores.get(0)); 
									//sticks,iron,and so on 
									
								}
								//else size is > 1 , so its something like wooden planks
								//TODO:maybe with a config file or something, but not for now
							}
							if(maybeOres instanceof ItemStack)//<ItemStack>
							{
								dropItems(player, (ItemStack)maybeOres); 
						
							} 
						}
					}
					break;
				} 
			}
			else if(next instanceof ShapedRecipes)
			{
				ShapedRecipes r = (ShapedRecipes) next;
 
				if(r.getRecipeOutput().isItemEqual( toUncraft ) )
				{  
					outsize = r.getRecipeOutput().stackSize;
				  
					if(toUncraft.stackSize >= outsize)
					{
						for(i = 0; i < r.recipeItems.length; i++) 
						{
							dropItems(player, r.recipeItems[i]); 
						}
					}
					break;
				}
			}
			else if(next instanceof ShapelessRecipes)
			{
				ShapelessRecipes r = (ShapelessRecipes) next;

				if(r.getRecipeOutput().isItemEqual( toUncraft))
				{  
					outsize = r.getRecipeOutput().stackSize;
				
					if(toUncraft.stackSize >= outsize)
					{
						for(i = 0; i < r.recipeItems.size(); i++) 
						{
							dropItems(player, (ItemStack)r.recipeItems.get(i));
						}
					}
					break;
				}
			} 
		}
		
		if(outsize > 0)
		{
			player.inventory.decrStackSize(Const.uncraftSlot, outsize); // toUncraft.stackSize -= outsize;
			 
			player.playSound("random.break", 1.0F, 1.0F);//same sound as breaking a too
		}
		
		return null; 
	}
}
