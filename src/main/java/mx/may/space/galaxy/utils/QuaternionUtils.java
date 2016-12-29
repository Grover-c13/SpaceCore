package mx.may.space.galaxy.utils;


import mx.may.space.galaxy.structure.BlockStructure;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Quaternion;


import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import java.nio.FloatBuffer;

/**
 * Created by Courtney on 1/12/2016.
 */
public class QuaternionUtils
{
	public static FloatBuffer toFloatBuffer(Quaternion quat)
	{

		FloatBuffer dest = BufferUtils.createFloatBuffer(16);
		Matrix3d matrix = toMatrix(quat);

		dest.put((float) matrix.m00);
		dest.put((float) matrix.m01);
		dest.put((float) matrix.m02);
		dest.put((float) 0);
		dest.put((float) matrix.m10);
		dest.put((float) matrix.m11);
		dest.put((float) matrix.m12);
		dest.put((float) 0);
		dest.put((float) matrix.m20);
		dest.put((float) matrix.m21);
		dest.put((float) matrix.m22);
		dest.put((float) 0);

		dest.put((float) 0);
		dest.put((float) 0);
		dest.put((float) 0);
		dest.put((float) 1);
		dest.rewind();

		return dest;

	}

	public static Vec3d toEuluer(Quaternion quat) {
		Matrix3d matrix = toMatrix(quat);
		double xRot, yRot, zRot;
		if (matrix.m21 != 1 || matrix.m21 != -1)
		{
			xRot = Math.atan2(matrix.m21, matrix.m22);
			yRot = -Math.asin(matrix.m21);
			zRot = Math.atan2(matrix.m10/Math.cos(yRot), matrix.m00/Math.cos(yRot));
		}
		else
		{
			//Rz(φ)Ry(θ)Rx(ψ)
			zRot = 0;
			if (matrix.m21 == -1)
			{
				yRot = Math.PI/2;
				xRot = Math.atan2(matrix.m01, matrix.m02);
			}
			else
			{
				yRot = -Math.PI/2;
				xRot = Math.atan2(-matrix.m01, -matrix.m02);
			}
		}



		return new Vec3d(xRot, yRot, zRot);

	}




	public static Vec3d getLocalPoint(Quaternion rotation, BlockStructure structure, double rx, double ry, double rz)
	{
		Quaternion point = new Quaternion((float)rx, (float)ry, (float)rz, 0);
		Quaternion rotXpoint = new Quaternion(); // hamilton product of the point and rotation
		Quaternion out = new Quaternion();
		Quaternion.mul(rotation, point, rotXpoint);
		Quaternion.mulInverse(rotXpoint, rotation, out);
		return new Vec3d(out.getX(), out.getY(), out.getZ());

	}


	public static Vec3d rotateVector(Quaternion rotation, Vec3d vector) throws IllegalStateException
	{
		Quaternion firstMul = new Quaternion();
		Quaternion secondMul = new Quaternion();
		Quaternion conjugate = new Quaternion();
		Quaternion.negate(rotation, conjugate);
		Quaternion.mul(rotation, new Quaternion((float) vector.xCoord, (float) vector.yCoord, (float) vector.zCoord, 0), firstMul);
		Quaternion.mul(firstMul, conjugate, secondMul);
		return new Vec3d(secondMul.getX(), secondMul.getY(), secondMul.getZ());
	}

	public static Matrix3d toMatrix(Quaternion quat)
	{
		float x = quat.x;
		float y = quat.y;
		float z = quat.z;
		float w = quat.w;

		float x2 = x * x;
		float y2 = y * y;
		float z2 = z * z;
		float xy = x * y;
		float xz = x * z;
		float yz = y * z;
		float wx = w * x;
		float wy = w * y;
		float wz = w * z;

		float m11, m12, m13;
		float m21, m22, m23;
		float m31, m32, m33;

		m11 = 1.0f - 2.0f * (y2 + z2);
		m12 = (2.0f * (xy - wz));
		m13 = (2.0f * (xz + wy));
		m21 = (2.0f * (xy + wz));
		m22 = (1.0f - 2.0f * (x2 + z2));
		m23 = (2.0f * (yz - wx));
		m31 = (2.0f * (xz - wy));
		m32 = (2.0f * (yz + wx));
		m33 = (1.0f - 2.0f * (x2 + y2));

		Matrix3d matrix = new Matrix3d();

		matrix.m00 = m11;
		matrix.m01 = m12;
		matrix.m02 = m13;
		matrix.m10 = m21;
		matrix.m11 = m22;
		matrix.m12 = m23;
		matrix.m20 = m31;
		matrix.m21 = m32;
		matrix.m22 = m33;
		return matrix;

	}





}
