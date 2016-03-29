package com.lothrazar.powerinventory.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class UtilSound{

	public static void playSound(World world,BlockPos pos, SoundEvent s, SoundCategory c){
		world.playSound(pos.getX(), pos.getY(), pos.getZ(), s, c, 1.0F, 1.0F, false);
	}
	public static void playSound(Entity entity, SoundEvent s, SoundCategory c){

		playSound(entity.getEntityWorld(),entity.getPosition(),s,c);
	}
}
