package mx.may.space.galaxy.bodies;

import io.netty.buffer.ByteBuf;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Quaternion;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Courtney on 28/11/2016.
 */
public class RotationalState
{
	private static float MAX_ROTATION_VELOCITY = 0.1f;
	private SpaceBody object;
	private float torqueX;
	private float torqueY;
	private float torqueZ;
	private float inertia;
	private float rotAccelX;
	private float rotAccelY;
	private float rotAccelZ;
	private float rotVelocityX;
	private float rotVelocityY;
	private float rotVelocityZ;

	private Quaternion orientation;

	private ConcurrentHashMap<BlockPos, Vec3d> torques;
	private ConcurrentHashMap<BlockPos, Vec3d> rs;


	public RotationalState(SpaceBody object)
	{
		this.object = object;
		torqueX = 0;
		torqueY = 0;
		torqueZ = 0;



		rotAccelX = 0;
		rotAccelY = 0;
		rotAccelZ = 0;

		rotVelocityX = 0;
		rotVelocityY = 0;
		rotVelocityZ = 0;


		torques = new ConcurrentHashMap<BlockPos, Vec3d>();
		rs = new ConcurrentHashMap<BlockPos, Vec3d>();


		orientation = new Quaternion();
		orientation.set(0, 0, 0, 1);

	}


	public void update(float deltaTime)
	{

		rotVelocityX += rotAccelX * deltaTime * 0.5;
		rotVelocityY += rotAccelY * deltaTime * 0.5;
		rotVelocityZ += rotAccelZ * deltaTime * 0.5;


		if (rotVelocityX > MAX_ROTATION_VELOCITY) rotVelocityX = MAX_ROTATION_VELOCITY;
		if (rotVelocityY > MAX_ROTATION_VELOCITY) rotVelocityY = MAX_ROTATION_VELOCITY;
		if (rotVelocityZ > MAX_ROTATION_VELOCITY) rotVelocityZ = MAX_ROTATION_VELOCITY;

		if (rotVelocityX < -MAX_ROTATION_VELOCITY) rotVelocityX = -MAX_ROTATION_VELOCITY;
		if (rotVelocityY < -MAX_ROTATION_VELOCITY) rotVelocityY = -MAX_ROTATION_VELOCITY;
		if (rotVelocityZ < -MAX_ROTATION_VELOCITY) rotVelocityZ = -MAX_ROTATION_VELOCITY;

		/*
		rotOrientationX += rotVelocityX * deltaTime + ((1 / 2) * rotAccelX * deltaTime * deltaTime);
		rotOrientationY += rotVelocityY * deltaTime + ((1 / 2) * rotAccelY * deltaTime * deltaTime);
		rotOrientationZ += rotVelocityZ * deltaTime + ((1 / 2) * rotAccelZ * deltaTime * deltaTime);
		*/


		Quaternion spin = new Quaternion();
		Quaternion velocityQ = new Quaternion(rotVelocityX, rotVelocityY, rotVelocityZ, 0);

		Quaternion.mul(orientation, velocityQ, spin);
		orientation.w += spin.w;
		orientation.x += spin.x;
		orientation.y += spin.y;
		orientation.z += spin.z;

		if (orientation.length() != 0)
		{
			orientation.normalise();
		}

	}


	public void calculateTorques()
	{
		torques.clear();
		rs.clear();
		torqueX = 0;
		torqueY = 0;
		torqueZ = 0;
		Vec3d local;
		for (Map.Entry<BlockPos, Vec3d> entry: object.getForces().entrySet())
		{
			local = calculateTorqueForVector(entry.getValue(), entry.getKey());
			torques.put(entry.getKey(), local);
			torqueX += local.xCoord;
			torqueY += local.yCoord;
			torqueZ += local.zCoord;
		}

		torqueX /= 8;
		torqueY /= 8;
		torqueZ /= 8;

		inertia = object.calculateMomentOfInertia();
		calculateAngularAcceleration();

	}



	private void calculateAngularAcceleration()
	{
		if (inertia != 0)
		{
			rotAccelX = torqueX/inertia;
			rotAccelY = torqueY/inertia;
			rotAccelZ = torqueZ/inertia;
		}
		else
		{
			rotAccelX = 0;
			rotAccelY = 0;
			rotAccelZ = 0;
		}

	}

	private Vec3d calculateTorqueForVector(Vec3d force, BlockPos location) {
		Vec3d loc = new Vec3d( (location.getX()+0.5), (location.getY()+0.5), (location.getZ()+0.5));
		Vec3d r = object.getCog().subtract(loc);
		rs.put(location, r);
		return r.crossProduct(force);
	}




	public void removeTorque(BlockPos pos)
	{
		if (torques.containsKey(pos)) {
			torques.remove(pos);
		}

		if (rs.containsKey(pos)) {
			rs.remove(pos);
		}

		calculateTorques();
	}


	public Vec3d getTorque() {
		return new Vec3d(torqueX, torqueY, torqueZ);
	}

	public Vec3d getAngularAcceleration() {
		return new Vec3d(rotAccelX, rotAccelY, rotAccelZ);
	}

	public Map<BlockPos, Vec3d> getRs()
	{
		return  rs;
	}

	public Map<BlockPos, Vec3d> getTorques() {
		return torques;
	}



	public void clearAcceleration()
	{
		rotAccelX = 0;
		rotAccelY = 0;
		rotAccelZ = 0;
	}

	public Quaternion getOrientation()
	{
		return  orientation;
	}

	public Quaternion getReverseOrientation()
	{
		return 	new Quaternion(-getOrientation().x,-getOrientation().y,-getOrientation().z, getOrientation().w);
	}

	public void resetOrientation()
	{
		this.orientation.setIdentity();
	}

	public void setOrientation(Quaternion newOrienation)
	{
		this.orientation = new Quaternion(newOrienation.x, newOrienation.y, newOrienation.z, newOrienation.w);
	}


	public void updateFromServer(ByteBuf buffer)
	{
		torqueX = buffer.readFloat();
		torqueY = buffer.readFloat();
		torqueZ = buffer.readFloat();
		inertia = buffer.readFloat();
		rotAccelX = buffer.readFloat();
		rotAccelY = buffer.readFloat();
		rotAccelZ = buffer.readFloat();
		rotVelocityX = buffer.readFloat();
		rotVelocityY = buffer.readFloat();
		rotVelocityZ = buffer.readFloat();
		orientation.x = buffer.readFloat();
		orientation.y = buffer.readFloat();
		orientation.z = buffer.readFloat();
		orientation.w = buffer.readFloat();
		calculateTorques();
	}

	public void updateToClient(ByteBuf buffer)
	{
		buffer.writeFloat(torqueX);
		buffer.writeFloat(torqueY);
		buffer.writeFloat(torqueZ);
		buffer.writeFloat(inertia);
		buffer.writeFloat(rotAccelX);
		buffer.writeFloat(rotAccelY);
		buffer.writeFloat(rotAccelZ);
		buffer.writeFloat(rotVelocityX);
		buffer.writeFloat(rotVelocityY);
		buffer.writeFloat(rotVelocityZ);
		buffer.writeFloat(orientation.x);
		buffer.writeFloat(orientation.y);
		buffer.writeFloat(orientation.z);
		buffer.writeFloat(orientation.w);
	}


	public Matrix4d getRotatinMatrix()
	{
		Matrix3d rotation = QuaternionUtils.toMatrix(orientation);
		Matrix4d homoMatrix = new Matrix4d();
		homoMatrix.set(rotation);
		return homoMatrix;
	}
}
