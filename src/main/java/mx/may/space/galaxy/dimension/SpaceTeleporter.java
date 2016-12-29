package mx.may.space.galaxy.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * Created by Courtney on 20-Nov-16.
 */
public class SpaceTeleporter extends Teleporter
{
	public SpaceTeleporter(WorldServer worldServer)
	{
		super(worldServer);
	}



	@Override
	public void placeInPortal(Entity p_placeInPortal_1_, float p_placeInPortal_2_)
	{
		super.placeInPortal(p_placeInPortal_1_, p_placeInPortal_2_);
	}

	@Override
	public boolean placeInExistingPortal(Entity p_placeInExistingPortal_1_, float p_placeInExistingPortal_2_)
	{
		return false;
	}

	@Override
	public boolean makePortal(Entity p_makePortal_1_)
	{
		return false;
	}

	@Override
	public void removeStalePortalLocations(long p_removeStalePortalLocations_1_)
	{

	}
}
