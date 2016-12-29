package mx.may.space.galaxy.network;

import mx.may.space.Client;
import mx.may.space.galaxy.bodies.FactoryRegistry;
import mx.may.space.galaxy.bodies.SpaceBody;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Courtney on 7/12/2016.
 */
public class UpdateSpaceObjectHandler implements IMessageHandler<UpdateSpaceObjectPacket, IMessage>
{
	@Override
	public IMessage onMessage(UpdateSpaceObjectPacket msg, MessageContext messageContext)
	{
		Client.OBJECT_STORE.addUpdate(new UpdateSpaceObjectTask(msg.getId(), msg.getFactory(), msg.getBuffer()));
		return null;
	}
}
