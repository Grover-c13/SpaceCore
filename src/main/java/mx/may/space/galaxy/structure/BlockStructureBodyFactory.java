package mx.may.space.galaxy.structure;

import mx.may.space.galaxy.bodies.BodyFactory;
import mx.may.space.galaxy.bodies.SpaceBody;

/**
 * Created by Courtney on 7/12/2016.
 */
public class BlockStructureBodyFactory extends BodyFactory
{
	public static final int FACTORY_ID = 0;
	@Override
	public SpaceBody createBody(double x, double y, double z)
	{
		return new BlockStructure(x, y, z);
	}
}
