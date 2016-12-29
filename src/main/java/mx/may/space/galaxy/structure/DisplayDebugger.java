package mx.may.space.galaxy.structure;

import com.github.quickhull3d.Point3d;
import mx.may.space.galaxy.bodies.RotationalState;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Map;

/**
 * Created by Courtney on 2/12/2016.
 */
public class DisplayDebugger
{

	public static void drawAll(BlockStructure structure)
	{
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );

		drawCog(structure);
		drawOrigin();
		drawTorque(structure);
		drawForces(structure);
		drawVerticies(structure);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void drawAfter(BlockStructure structure, double px, double py, double pz) {
		RotationalState rotation = structure.getRotationalState();
		FloatBuffer fbuffer = QuaternionUtils.toFloatBuffer(rotation.getOrientation());
		drawRotationQ(structure, px, py, pz, fbuffer);
		drawRotationE(structure, px, py, pz);
	}

	public static void drawRotationQ(BlockStructure structure, double px, double py, double pz, FloatBuffer rotation) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);


		GL11.glLineWidth(2);
		GlStateManager.translate(-(px-structure.getX()), -(py-structure.getY()), -(pz-structure.getZ()));
		GL11.glMultMatrix(rotation);
		GL11.glColor3ub((byte) 255, (byte) 255,(byte) 255);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3d(1, 1, 1);
		GL11.glVertex3d(1, 0, 1);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

	}

	public static void drawRotationE(BlockStructure structure, double px, double py, double pz)
	{
		RotationalState rotation = structure.getRotationalState();
		Vec3d rotationVec = QuaternionUtils.toEuluer(rotation.getOrientation());

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);


		GL11.glLineWidth(2);
		GlStateManager.translate(-(px-structure.getX()), -(py-structure.getY()), -(pz-structure.getZ()));
		GlStateManager.rotate((float) -Math.toDegrees(rotationVec.xCoord), 1, 0, 0);
		GlStateManager.rotate((float) -Math.toDegrees(rotationVec.yCoord), 0, 1, 0);
		GlStateManager.rotate((float) -Math.toDegrees(rotationVec.zCoord), 0, 0, 1);
		GL11.glColor3ub((byte) 0, (byte) 0,(byte) 255);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3d(0.5, 1, 0.5);
		GL11.glVertex3d(0.5, 0, 0.5);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

	}

	public static void drawOrigin()
	{
		GL11.glColor3ub((byte)0,(byte)100,(byte)100);

			GL11.glLineWidth(1);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(0.5f, 0, 0);
			GL11.glVertex3f(-0.5f, 0, 0);
			GL11.glVertex3f(0, 0.5f, 0);
			GL11.glVertex3f(0, -0.5f, 0);
			GL11.glVertex3f(0, 0, 0.5f);
			GL11.glVertex3f(0, 0, -0.5f);


		GL11.glEnd();



	}

	public static void drawCog(BlockStructure structure)
	{
		Vec3d cog = structure.getCog();

		GL11.glColor3ub((byte)253,(byte)0,(byte)0);

		GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex3f((float) (cog.xCoord+0.5f), (float) cog.yCoord, (float) cog.zCoord);
			GL11.glVertex3f((float) (cog.xCoord-0.5f),  (float) cog.yCoord, (float) cog.zCoord);

			GL11.glVertex3f((float) (cog.xCoord), (float) (cog.yCoord+0.5f), (float) cog.zCoord);
			GL11.glVertex3f((float) (cog.xCoord), (float) (cog.yCoord-0.5f), (float) cog.zCoord);

			GL11.glVertex3f((float) (cog.xCoord), (float) cog.yCoord, (float) (cog.zCoord+0.5f));
			GL11.glVertex3f((float) (cog.xCoord), (float) cog.yCoord, (float) (cog.zCoord+-0.5f));

		GL11.glEnd();

	}


	public static void drawTorque(BlockStructure structure)
	{
		Vec3d torque = structure.getRotationalState().getTorque();
		Vec3d cog = structure.getCog();

		GL11.glLineWidth(2);
		GL11.glColor3ub((byte) 0, (byte) 255,(byte) 255);

		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3d(cog.xCoord+torque.xCoord, cog.yCoord+torque.yCoord, cog.zCoord+torque.zCoord);
			GL11.glVertex3d(cog.xCoord, cog.yCoord, cog.zCoord);
		GL11.glEnd();
	}


	public static void drawForces(BlockStructure structure)
	{
		for (Map.Entry<BlockPos, Vec3d> entry : structure.getForces().entrySet())
		{
			drawForce(entry.getKey(), entry.getValue());
		}

	}


	public static void drawForce(BlockPos pos, Vec3d force)
	{
		GL11.glLineWidth(3);
		GL11.glColor3ub((byte) 0, (byte) 255,(byte) 0);

		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(pos.getX()+force.xCoord,pos.getY()+force.yCoord, pos.getZ()+force.zCoord);
		GL11.glVertex3d(pos.getX(), pos.getY(), pos.getZ());
		GL11.glEnd();
	}


	public static void drawVerticies(SpaceBody body)
	{
		GL11.glColor3ub((byte) 255, (byte) 0,(byte) 0);
		GL11.glPointSize(5);
		GL11.glBegin(GL11.GL_POINTS);
			for (Point3d point : body.getClientHull())
			{
				GL11.glVertex3d(point.x, point.y, point.z);
			}
		GL11.glEnd();
	}
}
