package com.lothrazar.powerinventory.proxy;

import java.util.ArrayList;

import com.lothrazar.powerinventory.*;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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

	@Override
	public IMessage onMessage(UncButtonPacket message, MessageContext ctx)
	{
		EntityPlayer player = ctx.getServerHandler().playerEntity;




		World w = player.worldObj;
		double x = player.posX;
		double y = player.posY;
		double z = player.posZ;
		//in the game, they drop between 3 and 11 experience //src http://minecraft.gamepedia.com/Bottle_o'_Enchanting
		//int e =ModConfig.expPerBottle;
		 
		ItemStack toUncraft = player.inventory.getStackInSlot(Const.uncraftSlot);
 
		int i;
		ItemStack s;
		int outsize = 0;
		
		
		ArrayList<EntityItem> drops = new ArrayList<EntityItem>();
		 
		if(toUncraft != null)
		for(Object next : CraftingManager.getInstance().getRecipeList())
		{
			//check ore dictionary first to avoid Class cast exceptions
			 
			if(next instanceof ShapedOreRecipe)
			{
				ShapedOreRecipe r = (ShapedOreRecipe) next;

				//TODO: requires config file input for custom recipe?
				System.out.println("--------OREshaped "+r.getRecipeOutput().getDisplayName());
				//LADDER is in here, even though it takes sticks. WEIRD right?

				if(r.getRecipeOutput().getItem() == toUncraft.getItem())
				{
					outsize = r.getRecipeOutput().stackSize;
				
					//outsize is 3 means the recipe makes three items total. so MINUS three
					//from the toUncraft for EACH LOOP
					if(toUncraft.stackSize >= outsize)
					{
						player.inventory.decrStackSize(Const.uncraftSlot, outsize); // toUncraft.stackSize -= outsize;
						Object maybeOres;
						for(i = 0; i < r.getInput().length; i++) 
						{
							maybeOres = r.getInput()[i];

							if(maybeOres instanceof ArrayList && (ArrayList<ItemStack>)maybeOres != null)//<ItemStack>
							{ 
								ArrayList<ItemStack> ores = (ArrayList<ItemStack>)maybeOres;
								//System.out.println("ore ARRAYLIST "+ores.size());
								
								if(ores.size() == 1)
								{
									//sticks,iron,and so on
									
									s = ores.get(0);

									if(s != null)
										player.worldObj.spawnEntityInWorld(new EntityItem(w, x,y,z, s.copy()));
									
								} //else size is > 1 , so its something like wooden planks
							}
							if(maybeOres instanceof ItemStack)//<ItemStack>
							{
								System.out.println("ore - item stack");
							}
							//if(s != null)
							//	player.worldObj.spawnEntityInWorld(new EntityItem(w, x,y,z, s.copy()));
						}
					}
					break;
				}
			}
			else if(next instanceof ShapelessOreRecipe)
			{
				ShapelessOreRecipe r = (ShapelessOreRecipe) next;
				//TODO: requires config file input for custom recipe?
				//for example, its not fair to turn a chest into 8 oak planks,
				//since it could have been made with birch or something.
				System.out.println("=====OREshapeless "+r.getRecipeOutput().getDisplayName());

				if(r.getRecipeOutput().getItem() == toUncraft.getItem())
				{
					//r.getInput()//an array of objects
					
				}
			}
			else if(next instanceof ShapedRecipes)
			{
				ShapedRecipes r = (ShapedRecipes) next;

				////System.out.println("___shaped"+r.getRecipeOutput().getDisplayName());
				
				if(r.getRecipeOutput().getItem() == toUncraft.getItem())
				{  
					outsize = r.getRecipeOutput().stackSize;
				 
		 
					//outsize is 3 means the recipe makes three items total. so MINUS three
					//from the toUncraft for EACH LOOP
					if(toUncraft.stackSize >= outsize)
					{
						player.inventory.decrStackSize(Const.uncraftSlot, outsize); // toUncraft.stackSize -= outsize;
						
						for(i = 0; i < r.recipeItems.length; i++) 
						{
							s = r.recipeItems[i];

							if(s != null)
							{
								//drops.add(arg0)
								player.worldObj.spawnEntityInWorld(new EntityItem(w, x,y,z, s.copy()));
							}
						}
					}
					break;
				}
			}
			else if(next instanceof ShapelessRecipes)
			{
				ShapelessRecipes r = (ShapelessRecipes) next;
				//this is almost a copy paste of shaped recipe
				//except recipeItems is a List not an array

//System.out.println("====="+r.getRecipeOutput().getDisplayName());
				
				if(r.getRecipeOutput().getItem() == toUncraft.getItem())
				{  
					outsize = r.getRecipeOutput().stackSize;
				
					if(toUncraft.stackSize >= outsize)
					{
						player.inventory.decrStackSize(Const.uncraftSlot, outsize);  
						
						for(i = 0; i < r.recipeItems.size(); i++) 
						{
							s = (ItemStack)r.recipeItems.get(i);
							
							if(s != null)
								player.worldObj.spawnEntityInWorld(new EntityItem(w, x,y,z, s.copy()));
						}
					}
					break;
				}
			} 
		}
		 
		return null; 
	}
}
