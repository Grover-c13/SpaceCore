package mx.may.space.galaxy;

import mx.may.space.Common;
import mx.may.space.galaxy.player.SpacePlayer;
import mx.may.space.galaxy.server.GalaxyManager;
import mx.may.space.galaxy.structure.BlockStructure;
import mx.may.space.galaxy.bodies.RotationalState;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.util.vector.Quaternion;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Courtney on 19-Nov-16.
 */
public class PlayerSpaceZone
{

	// linear
	private double spaceX;
	private double spaceY;
	private double spaceZ;
	BlockStructure primaryStructure;
	BlockStructure activeStructure;
	ConcurrentHashMap<Vec3d, BlockStructure> structures;

	private boolean objectMode; // object mode means everything is objects, no world interaction etc



	public PlayerSpaceZone(SpacePlayer player) {
		spaceX = 0;
		spaceY = 80;
		spaceZ = 0;
		primaryStructure = new BlockStructure(spaceX, spaceY, spaceZ);
		primaryStructure.addPlayer(player);
		activeStructure = primaryStructure;
		primaryStructure.setIgnoreCollisions(true);
		Common.GALAXY_MAN.addObject(primaryStructure);
		structures = new ConcurrentHashMap<Vec3d, BlockStructure>();
		objectMode = false;
	}



	public void addBlock(BlockPos pos, short health, short mass, IBlockState state)
	{
		primaryStructure.addBlockState(pos, health, mass, state);
	}

	public void removeBlock(BlockPos pos)
	{
		primaryStructure.removeBlockState(pos);
	}




	public BlockStructure getPrimaryStructure()
	{
		return primaryStructure;
	}

	public BlockStructure getActiveStructure()
	{
		return activeStructure;
	}


	public void test() {

		Vec3d loc = new Vec3d(10, 80, 10);
		Vec3d loc2 = new Vec3d(12, 80.5, 10);
		BlockStructure asteroid = new BlockStructure(loc.xCoord, loc.yCoord, loc.zCoord);
		BlockStructure asteroid2 = new BlockStructure(loc2.xCoord, loc2.yCoord, loc2.zCoord);




		for (int x = 0; x < 1; x++)
		{
			for (int y = 0; y < 1; y++)
			{
				for (int z = 0; z < 1; z++)
				{
					asteroid.addBlockState(new BlockPos(loc.xCoord+x,loc.yCoord+y,loc.zCoord+z), (short) 100, (short) 10000, Blocks.STONE.getDefaultState());
					asteroid2.addBlockState(new BlockPos(loc2.xCoord+x,loc2.yCoord+y,loc2.zCoord+z), (short) 100, (short) 10000, Blocks.STONE.getDefaultState());
				}
			}
		}

		Random rand = new Random();
		Quaternion randQ = new Quaternion(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		randQ.normalise();
		asteroid.getRotationalState().setOrientation(randQ);

		Common.GALAXY_MAN.addObject(asteroid);
		Common.GALAXY_MAN.addObject(asteroid2);

		Vec3d force =  new Vec3d(-1,0,0);
		asteroid2.addForce(new BlockPos(loc2.xCoord+4, loc2.yCoord+4, loc2.zCoord+4), force);
		/*
		Random rand = new Random();

		int asteriods = 30;// rand.nextInt(10);

		for (int count = 0; count < asteriods; count++)
		{


			Vec3d loc = new Vec3d(40-rand.nextInt(80), 100-rand.nextInt(50), 40-rand.nextInt(80));
			BlockStructure asteroid = new BlockStructure(loc.xCoord, loc.yCoord, loc.zCoord);

			int tx = 3+rand.nextInt(5);
			int ty = 3+rand.nextInt(5);
			int tz = 3+rand.nextInt(5);




			for (int x = 0; x < tx; x++)
			{
				for (int y = 0; y < ty; y++)
				{
					for (int z = 0; z < tz; z++)
					{
						asteroid.addBlockState(new BlockPos(loc.xCoord+x,loc.yCoord+y,loc.zCoord+z), (short) 100, (short) 100, Blocks.STONE.getDefaultState());
					}
				}
			}

			Vec3d force =  new Vec3d(-rand.nextInt(2),-rand.nextInt(2),-rand.nextInt(2));

			asteroid.addForce(new BlockPos(loc.xCoord+tx,loc.yCoord+ty,loc.zCoord+tz), force);
			//asteroid.getRotationalState().calculateTorques();
			//asteroid.clearForces();
			//asteroid.getRotationalState().update(50);
			//asteroid.getRotationalState().clearAcceleration();
			Common.GALAXY_MAN.addObject(asteroid);
		}

		*/

	}

	public Vec3d getCenterOfGravity()
	{
		return primaryStructure.getCenterOfGravityAbsolute();
	}



	public Vec3d getSpaceCoord()
	{
		return new Vec3d(spaceX, spaceY, spaceZ);
	}

	public boolean isEntOnStructure(Entity ent) {
		boolean onStructure = false;
		if (activeStructure != null)
		{
			onStructure = activeStructure.entOnWorldStructure(ent);

		}

		return onStructure;
	}


	public boolean isOnPrimary()
	{
		return activeStructure == primaryStructure;
	}

	public boolean isObjectMode()
	{
		return objectMode;
	}

	public void setObjectMode(boolean objectMode)
	{
		this.objectMode = objectMode;
	}
}
