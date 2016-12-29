package mx.may.space.galaxy.utils;

import net.minecraft.util.math.BlockPos;

import javax.vecmath.Matrix3d;

/**
 * Created by Courtney on 5/12/2016.
 */
public class MatrixUtils
{

	public static BlockPos rotateBlockPos(Matrix3d matrix, BlockPos pos)
	{
		int x = (int) (pos.getX()*matrix.m00+pos.getY()*matrix.m01+pos.getZ()*matrix.m02);
		int y = (int) (pos.getX()*matrix.m10+pos.getY()*matrix.m11+pos.getZ()*matrix.m12);
		int z = (int) (pos.getX()*matrix.m20+pos.getY()*matrix.m21+pos.getZ()*matrix.m22);

		return new BlockPos(x, y, z);
	}
}
