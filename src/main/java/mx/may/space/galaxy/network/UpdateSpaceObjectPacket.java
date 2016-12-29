package mx.may.space.galaxy.network;

import io.netty.buffer.ByteBuf;
import mx.may.space.Client;
import mx.may.space.galaxy.bodies.BodyFactory;
import mx.may.space.galaxy.bodies.FactoryRegistry;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.player.SpacePlayerBodyFactory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by Courtney on 6/12/2016.
 */
public class UpdateSpaceObjectPacket implements IMessage
{
	public static final int PACKET_ID = 0;
	private SpaceBody body;
	private BodyFactory factory;
	private int id;
	private ByteBuf buffer;
	private EntityPlayerMP player;

	public UpdateSpaceObjectPacket()
	{

	}

	public UpdateSpaceObjectPacket(SpaceBody body, EntityPlayerMP player)
	{
		this.body = body;
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		// id
		id = buffer.readInt();
		int factoryId = buffer.readInt();

		factory = FactoryRegistry.getFactory(factoryId);
		this.buffer = buffer;
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		// id stuff
		buffer.writeInt(body.getId()); // object id
		buffer.writeInt(body.getFactoryId()); // factory id

		body.updateToClient(buffer, this.player);
	}

	public SpaceBody getBody()
	{
		return body;
	}

	public BodyFactory getFactory()
	{
		return factory;
	}

	public int getId()
	{
		return id;
	}

	public ByteBuf getBuffer()
	{
		return buffer;
	}
}
