package mx.may.space.galaxy.player;

import mx.may.space.galaxy.bodies.BodyFactory;
import mx.may.space.galaxy.bodies.SpaceBody;

/**
 * Created by Courtney on 7/12/2016.
 */
public class SpacePlayerBodyFactory extends BodyFactory
{
	public static final int FACTORY_ID = 1;
	@Override
	public SpaceBody createBody(double x, double y, double z)
	{
		return new SpacePlayer(x, y, z);
	}
}
