package mx.may.space.galaxy.bodies;

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.QuickHull3D;
import io.netty.buffer.ByteBuf;
import mx.may.space.galaxy.player.SpacePlayer;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Quaternion;
import vclip.ConvexPolyhedron;
import vclip.PolyTree;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Courtney on 28/11/2016.
 */
public abstract class SpaceBody
{
	public static int UNASSIGNED_OBJECT_ID = -1;
	private int id;
	private double x;
	private double y;
	private double z;


	private Vec3d cog;
	private ConcurrentHashMap<BlockPos, Vec3d> forces;
	private Vec3d force;
	private Vec3d acceleration;
	private Vec3d velocity;


	private RotationalState rotationalState;
	private boolean destroyed;

	private QuickHull3D hull;
	private ArrayList<Point3d> verticies;
	private Point3d[] clientHull;
	private ConvexPolyhedron shape;
	private boolean ignoreCollisions;
	private PolyTree polyTree;

	public abstract int getMass();

	public abstract float calculateMomentOfInertia();

	public abstract int getFactoryId();

	public abstract void donePlayerUpdates();

	public abstract void render(SpacePlayer player, double px, double py, double pz);


	public SpaceBody(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		force = new Vec3d(0, 0, 0);
		acceleration = new Vec3d(0, 0, 0);
		velocity = new Vec3d(0, 0, 0);
		forces = new ConcurrentHashMap<BlockPos, Vec3d>();
		rotationalState = new RotationalState(this);
		id = UNASSIGNED_OBJECT_ID;
		cog = new Vec3d(0, 0, 0);
		destroyed = false;
		hull = new QuickHull3D();
		verticies = new ArrayList<Point3d>();
		clientHull = new Point3d[0];
		ignoreCollisions = false;
		shape = new ConvexPolyhedron();
		polyTree = new PolyTree("spaceobject");
	}


	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public double getZ()
	{
		return z;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public void setZ(double z)
	{
		this.z = z;
	}

	public Vec3d getAcceleration()
	{
		return acceleration;
	}

	public void setAcceleration(Vec3d acceleration)
	{
		this.acceleration = acceleration;
	}


	public Vec3d getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vec3d velocity)
	{
		this.velocity = velocity;
	}

	public Vec3d getForce()
	{
		return force;
	}


	public void setRotationalState(RotationalState rotationalState)
	{
		this.rotationalState = rotationalState;
	}

	public RotationalState getRotationalState()
	{
		return rotationalState;
	}

	public void recreateRotationalStateToCurrentOrientation()
	{
		RotationalState newState = new RotationalState(this);
		newState.setOrientation(this.getRotationalState().getOrientation());
		this.setRotationalState(newState);
	}

	public ConcurrentHashMap<BlockPos, Vec3d> getForces()
	{
		return forces;
	}

	public void removeForce(BlockPos loc)
	{
		if (forces.containsKey(loc))
		{
			force = force.subtract(forces.get(loc));
			forces.remove(loc);
			this.getRotationalState().calculateTorques();
		}
	}

	public void addForce(BlockPos loc, Vec3d force)
	{
		forces.put(loc, force);
		this.force = force.add(force);
		this.getRotationalState().calculateTorques();
	}

	public void clearForces()
	{
		this.forces.clear();
		this.force = new Vec3d(0, 0, 0);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Vec3d getCog()
	{
		return cog;
	}

	public void setCog(Vec3d cog)
	{
		this.cog = cog;
	}

	public Vec3d getCogAbsolute()
	{
		return this.getCog().addVector(this.getX(), this.getY(), this.getZ());
	}

	public void updateFromServer(ByteBuf buffer)
	{
		cog = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		x = buffer.readDouble();
		y = buffer.readDouble();
		z = buffer.readDouble();
		velocity = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		acceleration = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		destroyed = buffer.readBoolean();

		int hullCount = buffer.readInt();
		clientHull = new Point3d[hullCount];
		for (int i = 0; i < hullCount; i++)
		{
			clientHull[i] = new Point3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		}

		int forceCount = buffer.readInt();

		BlockPos loc;
		Vec3d force;
		forces.clear();
		// dont recalculate torques because it should of been done on the server
		for (int i = 0; i < forceCount; i++)
		{
			loc = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			force = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
			forces.put(loc, force);
		}

		rotationalState.updateFromServer(buffer);


	}

	public void updateToClient(ByteBuf buffer, EntityPlayerMP player)
	{
		buffer.writeDouble(cog.xCoord);
		buffer.writeDouble(cog.yCoord);
		buffer.writeDouble(cog.zCoord);
		buffer.writeDouble(x);
		buffer.writeDouble(y);
		buffer.writeDouble(z);
		buffer.writeDouble(velocity.xCoord);
		buffer.writeDouble(velocity.yCoord);
		buffer.writeDouble(velocity.zCoord);
		buffer.writeDouble(acceleration.xCoord);
		buffer.writeDouble(acceleration.yCoord);
		buffer.writeDouble(acceleration.zCoord);
		buffer.writeBoolean(destroyed);

		// hull verticies
		buffer.writeInt(this.hull.getNumVertices());

		for (Point3d point : this.hull.getVertices())
		{
			buffer.writeDouble(point.x);
			buffer.writeDouble(point.y);
			buffer.writeDouble(point.z);
		}

		// forces
		buffer.writeInt(forces.size());

		for (Map.Entry<BlockPos, Vec3d> entry : forces.entrySet())
		{
			buffer.writeInt(entry.getKey().getX());
			buffer.writeInt(entry.getKey().getY());
			buffer.writeInt(entry.getKey().getZ());
			buffer.writeDouble(entry.getValue().xCoord);
			buffer.writeDouble(entry.getValue().yCoord);
			buffer.writeDouble(entry.getValue().zCoord);
		}

		rotationalState.updateToClient(buffer);
	}

	public void update(float deltaTime)
	{

		// rotate force
		Vec3d rotatedForce = QuaternionUtils.rotateVector(getRotationalState().getOrientation(), getForce());
		acceleration = new Vec3d(rotatedForce.xCoord / getMass(), rotatedForce.yCoord / getMass(), rotatedForce.zCoord / getMass());

		// linear
		velocity = velocity.addVector(acceleration.xCoord * deltaTime, acceleration.yCoord * deltaTime, acceleration.zCoord * deltaTime);
		x += velocity.xCoord * deltaTime + ((1 / 2) * acceleration.xCoord * deltaTime * deltaTime);
		y += velocity.yCoord * deltaTime + ((1 / 2) * acceleration.yCoord * deltaTime * deltaTime);
		z += velocity.zCoord * deltaTime + ((1 / 2) * acceleration.zCoord * deltaTime * deltaTime);

		this.getRotationalState().update(deltaTime);

	}

	public void destroy()
	{
		destroyed = true;
	}

	public boolean isDestroyed()
	{
		return destroyed;
	}


	public Vec3d getLocalPosition(SpaceBody body, Vec3d point)
	{
		Vec3d out;
		// rotate point by the in bodys orientation
		out = QuaternionUtils.rotateVector(body.getRotationalState().getOrientation(), point);
		// translate away from the origin of this body
		out = out.addVector(this.getX(), this.getY(), this.getZ());
		// rotate by the reverse of this orientation
		out = QuaternionUtils.rotateVector(this.getRotationalState().getReverseOrientation(), out);

		return out;
	}


	public void addVertex(double x, double y, double z)
	{
		verticies.add(new Point3d(x, y, z));
	}

	public QuickHull3D getHull()
	{
		return hull;
	}

	public Point3d[] getClientHull()
	{
		return clientHull;
	}

	public void buildHull()
	{
		Point3d[] qhPoints = new Point3d[verticies.size()];
		verticies.toArray(qhPoints);
		hull.build(qhPoints);

		javax.vecmath.Point3d[] vcPoints = new javax.vecmath.Point3d[hull.getVertices().length];
		for (int i = 0; i < hull.getVertices().length; i++)
		{
			Point3d point = hull.getVertices()[i];
			vcPoints[i] = new javax.vecmath.Point3d(point.x, point.y, point.z);
		}

		shape = new ConvexPolyhedron(vcPoints, hull.getFaces());
		polyTree = new PolyTree("spaceobject", shape);
	}

	public ConvexPolyhedron getShape()
	{
		return shape;
	}

	public Vec3d getRelative(SpaceBody other)
	{
		return new Vec3d(other.getX()-this.getX(), other.getY()-this.getY(), other.getZ()-this.getZ());
	}


	public Matrix4d getTransformationMatrix()
	{
		Matrix4d translate = new Matrix4d();
		translate.setIdentity();
		translate.setM03(this.getX());
		translate.setM13(this.getY()-80);
		translate.setM23(this.getZ());

		translate.mul(this.getRotationalState().getRotatinMatrix());
		return translate;

	}

	public Matrix4d getInverseTransformationMatrix()
	{
		Matrix4d rot = (Matrix4d) this.getRotationalState().getRotatinMatrix().clone();
		rot.transpose();


		Matrix4d trans = new Matrix4d();
		trans.setIdentity();
		trans.setM03(-this.getX());
		trans.setM13(-this.getY());
		trans.setM23(-this.getZ());

		trans.mul(rot);

		return rot;
	}

	public Matrix4d getReferenceFrameMatrix(SpaceBody b)
	{
		Matrix4d matrix = this.getRotationalState().getRotatinMatrix();
		matrix.invert();
		matrix.mul(b.getRotationalState().getRotatinMatrix());


		Vec3d rpos = b.getRelative(this);
		matrix.setM03(rpos.xCoord);
		matrix.setM03(rpos.yCoord);
		matrix.setM03(rpos.zCoord);

 		return matrix;
	}

	public boolean isIgnoreCollisions()
	{
		return ignoreCollisions;
	}

	public void setIgnoreCollisions(boolean ignoreCollisions)
	{
		this.ignoreCollisions = ignoreCollisions;
	}


	public PolyTree getPolyTree()
	{
		return polyTree;
	}

	public boolean equals(SpaceBody other)
	{
		return (other.getId() == this.getId()) && (this.getX() == other.getX()) && (this.getY() == other.getY()) && (this.getZ() == other.getZ());
	}
}

