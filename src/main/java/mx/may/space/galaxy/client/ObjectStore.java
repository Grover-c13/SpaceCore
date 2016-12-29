package mx.may.space.galaxy.client;

import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.network.UpdateSpaceObjectTask;
import mx.may.space.galaxy.player.SpacePlayer;
import mx.may.space.galaxy.structure.BlockStructure;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Courtney on 7/12/2016.
 */
public class ObjectStore
{
	private HashMap<Integer, SpaceBody> objects;
	private SpacePlayer player;
	private ConcurrentLinkedQueue<UpdateSpaceObjectTask> updates;

	public ObjectStore()
	{
		objects = new HashMap<Integer, SpaceBody>();
		player = new SpacePlayer(UUID.randomUUID(), 0, 0, 0);
		updates = new  ConcurrentLinkedQueue<UpdateSpaceObjectTask>();
	}


	public void addObject(SpaceBody object)
	{
		objects.put(object.getId(), object);


	}

	public SpaceBody getObject(int id)
	{
		if (objects.containsKey(id))
		{
			return objects.get(id);
		}

		throw new IllegalArgumentException("Trying to get local object that does not exist, has it been sent?");
	}

	public boolean shouldCreate(int id)
	{
		return !objects.containsKey(id);
	}

	public SpacePlayer getPlayer()
	{
		return player;
	}

	public void setPlayer(SpacePlayer player)
	{
		this.player = player;
	}


	public void updateAndRender(float deltatime, double px, double py, double pz)
	{
		for (UpdateSpaceObjectTask task : updates)
		{
			task.run();
		}

		clearUpdates();

		GlStateManager.pushMatrix();
		FloatBuffer reverse_player = QuaternionUtils.toFloatBuffer(player.getRotationalState().getReverseOrientation());
		GlStateManager.multMatrix(reverse_player);
		for (SpaceBody body : objects.values())
		{
			body.render(player, px, py , pz);
		}

		GlStateManager.popMatrix();
	}

	public synchronized void addUpdate(UpdateSpaceObjectTask task)
	{
		updates.add(task);
	}

	public synchronized void clearUpdates()
	{
		updates.clear();
	}

	public void removeBody(SpaceBody body)
	{
		this.objects.remove(body.getId());
	}
}
