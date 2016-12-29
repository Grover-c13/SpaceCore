package mx.may.space.galaxy.network;

import io.netty.buffer.ByteBuf;
import mx.may.space.Client;
import mx.may.space.galaxy.bodies.BodyFactory;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.player.SpacePlayer;
import net.minecraft.client.Minecraft;

import java.util.UUID;

/**
 * Created by Courtney on 7/12/2016.
 */
public class UpdateSpaceObjectTask implements Runnable
	{
	private int id;
	private BodyFactory factory;
	private ByteBuf buffer;

	public UpdateSpaceObjectTask(int id, BodyFactory factory, ByteBuf buffer)
	{
		this.id = id;
		this.factory = factory;
		this.buffer = buffer;
	}

	@Override
	public void run()
	{
		SpaceBody object;
		if (Client.OBJECT_STORE.shouldCreate(id))
		{
			object = factory.createBody(0, 0, 0); // location shouldnt matter
			object.setId(id);
			Client.OBJECT_STORE.addObject(object);
		}
		else
		{
			object = Client.OBJECT_STORE.getObject(id);
		}

		object.updateFromServer(buffer);

		if (object instanceof SpacePlayer)
		{
			SpacePlayer splayer = (SpacePlayer) object;
			if (splayer.getPlayer().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()))
			{
				Client.OBJECT_STORE.setPlayer(splayer);
			}
		}

		if (object.isDestroyed())
		{
			Client.OBJECT_STORE.removeBody(object);
		}


	}
}
