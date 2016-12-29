package mx.may.space.galaxy.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Courtney on 6/12/2016.
 */
public class BlockUtils
{
	public static Vec3d blockPosToVec(BlockPos pos) {
		return new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
	}
}
