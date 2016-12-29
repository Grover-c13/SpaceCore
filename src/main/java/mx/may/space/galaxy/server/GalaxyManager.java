package mx.may.space.galaxy.server;


import javafx.util.Pair;
import mx.may.space.Common;
import mx.may.space.galaxy.PlayerSpaceZone;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.dimension.SpaceTeleporter;
import mx.may.space.galaxy.network.UpdateSpaceObjectPacket;
import mx.may.space.galaxy.player.SpacePlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import vclip.ClosestFeaturesHT;
import vclip.ClosestPointPair;
import vclip.DistanceReport;
import vclip.PolyTree;

import javax.vecmath.Matrix4d;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Courtney on 17-Nov-16.
 */
public class GalaxyManager
{
	private HashMap<EntityPlayer, PlayerSpaceZone> spaceAreas;
	private HashMap<UUID, SpacePlayer> playerObjects;
	private ConcurrentHashMap<Integer, SpaceBody> objects;
	private int id_count;
	private boolean restarting;


	public GalaxyManager()
	{
		spaceAreas = new HashMap<EntityPlayer, PlayerSpaceZone>();
		playerObjects = new HashMap<UUID, SpacePlayer>();
		objects = new ConcurrentHashMap<Integer, SpaceBody>();
		id_count = 0;
		restarting = false;
	}

	public PlayerSpaceZone createSpaceZone(EntityPlayer player)
	{
		PlayerSpaceZone zone = new PlayerSpaceZone(getSpacePlayer(player));
		spaceAreas.put(player, zone);
		return zone;
	}

	public PlayerSpaceZone getZone(EntityPlayer player)
	{
		if (spaceAreas.containsKey(player)) {
			return spaceAreas.get(player);
		}

		return null;
	}


	public SpacePlayer getSpacePlayer(EntityPlayer player)
	{
		SpacePlayer splayer;
		if ( playerObjects.containsKey(player.getUniqueID())) {
			splayer = playerObjects.get(player.getUniqueID());
		}
		else
		{
			splayer = new SpacePlayer(player.getUniqueID(), 0, 0, 0);
			playerObjects.put(player.getUniqueID(), splayer);
			this.addObject(splayer);
		}

		return splayer;
	}

	public void addObject(SpaceBody object)
	{
		object.setId(id_count);
		objects.put(id_count, object);
		id_count++;
	}


	public void update(float deltaTime)
	{
		HashSet<SpaceBody> done = new HashSet<SpaceBody>();
		for (SpaceBody body : objects.values())
		{
			if (!body.isDestroyed())
			{
				body.update(deltaTime);
			}

			for (EntityPlayerMP player : DimensionManager.getWorld(100).getMinecraftServer().getPlayerList().getPlayerList())
			{
				Common.NETWORK.sendTo(new UpdateSpaceObjectPacket(body, player), player);
			}


			if (body.isDestroyed())
			{
				objects.remove(body.getId());
			}

			body.donePlayerUpdates();


			// no collision checks
			if (restarting)
			{
				continue;
			}
			ClosestPointPair pair = new ClosestPointPair();
			PolyTree tree;


			if (!body.isIgnoreCollisions())
			{



				// collisions
				for (SpaceBody secBody : objects.values())
				{
					boolean isDone = done.contains(secBody);


					boolean sameBody = secBody.equals(body);
					if (!secBody.isIgnoreCollisions() && !sameBody && !isDone)
					{
						DistanceReport dr = new DistanceReport();
						ClosestFeaturesHT closestFeaturesHT = new ClosestFeaturesHT();
						Matrix4d trans = body.getReferenceFrameMatrix(secBody);
						body.getPolyTree().vclip(dr, secBody.getPolyTree(), trans, 100, closestFeaturesHT);
						if (dr.getClosestDistance() != Double.POSITIVE_INFINITY && dr.getClosestDistance() != Double.NEGATIVE_INFINITY)
						{
							if (dr.getClosestDistance() <= 0.001 && dr.getClosestDistance() >= -0.001)
							{
								System.out.println("COLLISION");
								body.clearForces();
								secBody.clearForces();
								body.setVelocity(new Vec3d(0, 0, 0));
								secBody.setVelocity(new Vec3d(0, 0, 0));
								body.setAcceleration(new Vec3d(0, 0, 0));
								secBody.setAcceleration(new Vec3d(0, 0, 0));
							}
							System.out.println(dr.getClosestDistance());
						}


					}
				}

				done.add(body);
			}
		}

		if (restarting)
		{
			restarting = false;
		}

	}


	public void reset()
	{
		restarting = true;
		for (SpaceBody body : objects.values())
		{
			body.destroy();
		}
		playerObjects.clear();
	}

	public void putPlayerInSpace(EntityPlayer player, double x, double y, double z)
	{
		PlayerSpaceZone zone = this.createSpaceZone(player);


		zone.addBlock(new BlockPos(0, 80, 0), (short) 100, (short) 1, Blocks.STONE.getDefaultState());
		zone.test();

		SpacePlayer splayer = getSpacePlayer(player);
		splayer.setInSpace(true);
		splayer.setX(x);
		splayer.setY(y);
		splayer.setZ(z);

		World space = DimensionManager.getWorld(100);
		space.setBlockState(new BlockPos(0, 80, 0), Blocks.STONE.getDefaultState());

		Teleporter spaceTeleporter = new SpaceTeleporter(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(100));

		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().transferPlayerToDimension((EntityPlayerMP) player, 100, spaceTeleporter);
		player.setPositionAndUpdate(0, 81, 0);


	}




}
