package mx.may.space.galaxy.server;

import mx.may.space.Common;
import mx.may.space.galaxy.PlayerSpaceZone;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Courtney on 8/12/2016.
 */
public class ServerEventHandler
{
	@SubscribeEvent
	public void playerUpdate(LivingEvent.LivingUpdateEvent e)
	{
		if (!e.getEntity().getEntityWorld().isRemote)
		{
			if (e.getEntity() instanceof EntityPlayer)
			{
				PlayerSpaceZone zone = Common.GALAXY_MAN.getZone((EntityPlayer) e.getEntity());
				if (zone != null)
				{
					if (!zone.isEntOnStructure(e.getEntity()) && e.getEntity().getPosition().getY() < 900)
					{
						zone.setObjectMode(true);
						//e.getEntity().setPositionAndUpdate(e.getEntity().posX, e.getEntity().posY + 1000, e.getEntity().posZ);
					}
				}
			}
		}
	}




	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent e)
	{


		for (EntityPlayerMP player: FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList())
		{

			PlayerSpaceZone zone = Common.GALAXY_MAN.getZone(player);

			if (zone != null) {
				//zone.update(1);

			}

			Common.GALAXY_MAN.update(1);


		}



	}

	@SubscribeEvent
	public void blockPlace(BlockEvent.PlaceEvent e)
	{
		PlayerSpaceZone zone = Common.GALAXY_MAN.getZone(e.getPlayer());

		if (zone != null) {

			if (e.getPlacedBlock().getBlock() instanceof BlockObsidian)
			{
				zone.addBlock(e.getPos(), (short) 10000, (short) 1000, e.getState());
			}
			else
			{
				zone.addBlock(e.getPos(), (short) 100, (short) 1, e.getState());
			}

			if (e.getPlacedBlock().getBlock() instanceof BlockDispenser)
			{

				EnumFacing facing = e.getPlacedBlock().getValue(BlockDispenser.FACING).getOpposite();

				Vec3i force = facing.getDirectionVec();
				Vec3d forceD = new Vec3d(force);
				zone.getPrimaryStructure().addForce(e.getPos(), forceD);
			}
		}
	}

	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent e)
	{
		PlayerSpaceZone zone = Common.GALAXY_MAN.getZone(e.getPlayer());

		if (zone != null) {
			zone.removeBlock(e.getPos());
		}
	}


}
