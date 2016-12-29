package mx.may.space.galaxy.dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Courtney on 19-Nov-16.
 */
public class SpaceCloudRenderer extends IRenderHandler
{

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float ticks, WorldClient world, Minecraft mc)
	{
		// do nothing
	}

}

