package mx.may.space.galaxy.client;

import ibxm.Player;
import mx.may.space.Client;
import mx.may.space.Common;
import mx.may.space.galaxy.PlayerSpaceZone;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * Created by Courtney on 19-Nov-16.
 */
public class ClientEventHandler
{



	@SubscribeEvent
	public void clientTick(TickEvent.RenderTickEvent e)
	{
		//PlayerSpaceZone zone = Common.galaxyManager.getZone(Minecraft.getMinecraft().thePlayer);

		//if (zone != null) {
			//zone.renderObjects(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);

		//}
	}




	@SubscribeEvent
	public void onRenderLiving(RenderLivingEvent event)
	{

	}




	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent e)
	{

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks();
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks();
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks();

		Client.OBJECT_STORE.updateAndRender(1, playerX, playerY, playerZ);

		PlayerSpaceZone zone = Common.GALAXY_MAN.getZone(player);
		if (zone != null) {
			drawCog(zone, player, e.getPartialTicks());


			for (Map.Entry<BlockPos, Vec3d> entries : zone.getPrimaryStructure().getForces().entrySet())
			{
				drawForceArrow(zone, player, zone.getPrimaryStructure().getAbsoluteBlockPos(entries.getKey()), entries.getValue(), e.getPartialTicks(), 0, 255 , 0);
			}

			for (Map.Entry<BlockPos, Vec3d> entries : zone.getPrimaryStructure().getRotationalState().getTorques().entrySet())
			{
				drawForceArrow(zone, player, zone.getPrimaryStructure().getAbsoluteBlockPos(entries.getKey()), entries.getValue(), e.getPartialTicks(), 0, 255 , 255);
			}

			for (Map.Entry<BlockPos, Vec3d> entries : zone.getPrimaryStructure().getRotationalState().getRs().entrySet())
			{
				BlockPos pos = zone.getPrimaryStructure().getAbsoluteBlockPos(entries.getKey());
				drawForceArrow(zone, player, pos, entries.getValue(), e.getPartialTicks(), 255, 0 , 255);
			}


			drawTorque(zone, player,  zone.getPrimaryStructure().getRotationalState().getTorque(), e.getPartialTicks());

		}

	}




	public void drawForceArrow(PlayerSpaceZone zone, EntityPlayer player, BlockPos origin, Vec3d force, float partialTicks, int red, int green, int blue)
	{
		Vec3d originv = new Vec3d(origin.getX()+0.5, origin.getY()+0.5, origin.getZ()+0.5);
		drawLine(zone, player, originv, force, partialTicks, red, green, blue);
	}



	public void drawLine(PlayerSpaceZone zone, EntityPlayer player, Vec3d origin, Vec3d force, float partialTicks, int red, int green, int blue)
	{
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		double doubleX = playerX-origin.xCoord;
		double doubleY = playerY-origin.yCoord;
		double doubleZ = playerZ-origin.zCoord;



		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);


		GL11.glLineWidth(2);
		GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
		GL11.glColor3ub((byte) red, (byte) green,(byte) blue);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3d(force.xCoord, force.yCoord, force.zCoord);
		GL11.glVertex3d(0, 0, 0);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

	}


	public void drawTorque(PlayerSpaceZone zone, EntityPlayer player, Vec3d torque, float partialTicks)
	{
		Vec3d cog = zone.getCenterOfGravity();
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		double doubleX = playerX-cog.xCoord;
		double doubleY = playerY-cog.yCoord;
		double doubleZ = playerZ-cog.zCoord;



		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);


		GL11.glLineWidth(6);
		GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
		GL11.glColor3ub((byte) 0, (byte) 255,(byte) 255);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3d(torque.xCoord, torque.yCoord, torque.zCoord);
		GL11.glVertex3d(0, 0, 0);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

	}



	public void drawCog(PlayerSpaceZone zone, EntityPlayer player, float partialTicks)
	{
		Vec3d cog = zone.getCenterOfGravity();
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		double doubleX = playerX-cog.xCoord;
		double doubleY = playerY-cog.yCoord;
		double doubleZ = playerZ-cog.zCoord;


		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);


		GL11.glLineWidth(6);
		GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
		GL11.glColor3ub((byte)253,(byte)0,(byte)0);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3f(0.5f, 0, 0);
		GL11.glVertex3f(-0.5f, 0, 0);

		GL11.glVertex3f(0, 0.5f, 0);
		GL11.glVertex3f(0, -0.5f, 0);

		GL11.glVertex3f(0, 0, 0.5f);
		GL11.glVertex3f(0, 0, -0.5f);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}
}
