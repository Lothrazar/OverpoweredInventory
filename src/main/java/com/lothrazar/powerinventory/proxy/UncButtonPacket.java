package com.lothrazar.powerinventory.proxy;

import java.util.ArrayList;

import com.lothrazar.powerinventory.*;
import com.lothrazar.powerinventory.standalone.ContainerCustomPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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
	
	//todo: put in a blacklist from config file
	private ArrayList<EntityItem> drops;
	private void addDrop(EntityPlayer player, ItemStack s)
	{ 
		//this fn is null safe, it gets nulls all the time
		if(s == null || s.getItem() == Items.milk_bucket){return;}
		//also, when crafting cake you get the empty bucket back.
		//so dont refund full buckets or else thats free infinite iron
		

		
		ItemStack stack = s.copy();
		stack.stackSize = 1;
		//bugged out wooden planks from something like a note block or chest
		//where , there are a whole bunch of wooden plank types it COULD be but no way to know for sure
		//by default (if checking Only number) this blocks all oak/quartz
		if(stack.getItemDamage() == 32767 )
		{
			if("tile.wood.oak".equals( stack.getUnlocalizedName()))
			{
				return;
			}
			
			stack.setItemDamage(0);//do not make invalid quartz
		}
				
		//we set to 1 because recipe registry is bugged in some forge versions
		//EXAMPLE: crafting a Hay Bale takes 9 wheat, so 9 stacks of 1
		//but forge tells me its 9 stacks of 9 !?!?

		World w = player.worldObj;
		double x = player.posX;
		double y = player.posY;
		double z = player.posZ;
		drops.add(new EntityItem(w, x,y,z,stack));
	}
	//TODO: in future versions we may add a blacklist to uncrafting
	//TODO: also an option: allow Custom reverses
	//such as: currently Crafting Table et all do nothing
	//but we could allow a config file to say "crafting table -> 8 sticks or whatever?
	
	private static final ArrayList<Item> blacklist = new ArrayList<Item>(){{
		add(Items.chainmail_boots);
		add(Items.chainmail_chestplate);
		add(Items.chainmail_helmet);
		add(Items.chainmail_leggings);
	}};
	
	@Override
	public IMessage onMessage(UncButtonPacket message, MessageContext ctx)
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
		
		ItemStack toUncraft = invo.getStackInSlot(Const.uncraftSlot);
		
		if(toUncraft == null){return null;}
		
		if(blacklist.contains(toUncraft.getItem())){return null;}
		
		drops = new ArrayList<EntityItem>();
		int i;
		Object maybeOres;
		int outsize = 0;
		
		//TODO: need a blacklist. for example: chain armor

		//outsize is 3 means the recipe makes three items total. so MINUS three
		//from the toUncraft for EACH LOOP
		
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
									addDrop(player, ores.get(0));
								}
								//else size is > 1 , so its something like wooden planks
								//TODO:maybe with a config file or something, but not for now
							}
							if(maybeOres instanceof ItemStack)//<ItemStack>
							{
								addDrop(player, (ItemStack)maybeOres); 
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
									addDrop(player, ores.get(0)); 
									//sticks,iron,and so on 
								}
								//else size is > 1 , so its something like wooden planks
								//TODO:maybe with a config file or something, but not for now
							}
							if(maybeOres instanceof ItemStack)//<ItemStack>
							{
								addDrop(player, (ItemStack)maybeOres); 
						
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
							addDrop(player, r.recipeItems[i]); 
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
							addDrop(player, (ItemStack)r.recipeItems.get(i));
						}
					}
					break;
				}
			} 
		}
		
		if(drops.size() > 0)  //if(outsize > 0)
		{
			for(EntityItem ei : drops)
			{
				player.worldObj.spawnEntityInWorld(ei); 
			}
			invo.decrStackSize(Const.uncraftSlot, outsize); // toUncraft.stackSize -= outsize;
			 
			player.playSound("random.break", 1.0F, 1.0F);//same sound as breaking a too
		}
		
		return null; 
	}
}
