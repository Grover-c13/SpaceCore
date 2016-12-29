package mx.may.space.galaxy.player;

import io.netty.buffer.ByteBuf;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.utils.BufferUtil;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

/**
 * Created by Courtney on 6/12/2016.
 */
public class SpacePlayer extends SpaceBody
{
	private boolean inSpace;
	private UUID player;
	private SpaceBody fixedTo;

	public SpacePlayer(double x, double y, double z)
	{
		super(x, y, z);
		inSpace = false;
		player = UUID.randomUUID();
		this.setIgnoreCollisions(true);
	}

	public SpacePlayer(UUID player, double x, double y, double z)
	{
		super(x, y, z);
		inSpace = false;
		this.player = player;
		this.setIgnoreCollisions(true);
	}




	@Override
	public int getMass()
	{
		return 10;
	}

	@Override
	public float calculateMomentOfInertia()
	{
		return 1;
	}


	public boolean isInSpace()
	{
		return inSpace;
	}

	public void setInSpace(boolean inSpace)
	{
		this.inSpace = inSpace;
	}

	@Override
	public int getFactoryId()
	{
		return SpacePlayerBodyFactory.FACTORY_ID;
	}

	@Override
	public void donePlayerUpdates()
	{
		// no op
	}

	@Override
	public void render(SpacePlayer player, double px, double py, double pz)
	{

	}



	@Override
	public void updateToClient(ByteBuf buffer, EntityPlayerMP playerEnt)
	{
		super.updateToClient(buffer, playerEnt);
		BufferUtil.writeString(buffer, player.toString());
		buffer.writeBoolean(inSpace);
	}

	@Override
	public void updateFromServer(ByteBuf buffer)
	{
		super.updateFromServer(buffer);
		player = UUID.fromString(BufferUtil.readString(buffer));
		inSpace = buffer.readBoolean();
	}

	public UUID getPlayer()
	{
		return player;
	}


	public boolean isFixedToBody()
	{
		return fixedTo != null;
	}

}
