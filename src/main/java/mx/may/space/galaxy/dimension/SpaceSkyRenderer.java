package mx.may.space.galaxy.dimension;

import mx.may.space.Client;
import mx.may.space.Common;
import mx.may.space.galaxy.PlayerSpaceZone;
import mx.may.space.galaxy.player.SpacePlayer;
import mx.may.space.galaxy.bodies.RotationalState;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Created by Courtney on 19-Nov-16.
 */
public class SpaceSkyRenderer extends IRenderHandler
{
	private TextureManager renderEngine;
	private VertexBuffer starVBO;
	private final VertexFormat vertexBufferFormat;
	private int starGLCallList;
	private boolean vboEnabled;

	private Quaternion lastRotation;

	public SpaceSkyRenderer()
	{
		this.renderEngine = Minecraft.getMinecraft().getTextureManager();
		this.vboEnabled = OpenGlHelper.useVbo();
		this.vertexBufferFormat = new VertexFormat();
		this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
		lastRotation = new Quaternion();
	}





	@SideOnly(Side.CLIENT)
	@Override
	public void render(float ticks, WorldClient world, Minecraft mc)
	{
		SpacePlayer spacePlayer = Client.OBJECT_STORE.getPlayer();
		RotationalState rotation = null;
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);


		if (spacePlayer.isInSpace())
		{
			rotation = spacePlayer.getRotationalState();

			//rotation.getOrientation().OBJECT_STORE(fbuffer);
			//rotation.getOrientation().setW(-rotation.getOrientation().getW());
			//rotation.getOrientation().OBJECT_STORE(fbuffer);
			/*if (zone.isOnPrimary())
			{
				lastRotation.x = rotation.getOrientation().x;
				lastRotation.y = rotation.getOrientation().y;
				lastRotation.z = rotation.getOrientation().z;
				lastRotation.w = rotation.getOrientation().w;
			}*/
			buffer = QuaternionUtils.toFloatBuffer(rotation.getReverseOrientation());
			//fbuffer.flip();
		}

		GlStateManager.pushMatrix();

		Tessellator tessellator = Tessellator.getInstance();
		net.minecraft.client.renderer.VertexBuffer vertexbuffer = tessellator.getBuffer();
		if(this.starVBO != null) {
			this.starVBO.deleteGlBuffers();
		}

		if(this.starGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(this.starGLCallList);
			this.starGLCallList = -1;
		}

		if(this.vboEnabled) {
			this.starVBO = new VertexBuffer(this.vertexBufferFormat);
			this.renderStars(vertexbuffer);

			if (spacePlayer.isInSpace())
			{
					GL11.glMultMatrix(buffer);
			}

			vertexbuffer.finishDrawing();
			vertexbuffer.reset();
			this.starVBO.bufferData(vertexbuffer.getByteBuffer());
		} else {
			this.starGLCallList = GLAllocation.generateDisplayLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(this.starGLCallList, 4864);
			this.renderStars(vertexbuffer);
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}


		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if(this.vboEnabled) {
			this.starVBO.bindBuffer();
			GlStateManager.glEnableClientState('聴');
			GlStateManager.glVertexPointer(3, 5126, 12, 0);
			this.starVBO.drawArrays(7);
			this.starVBO.unbindBuffer();
			GlStateManager.glDisableClientState('聴');
		} else {

			if (spacePlayer.isInSpace())
			{
				GL11.glMultMatrix(buffer);
			}


			GlStateManager.callList(this.starGLCallList);
		}




		GlStateManager.popMatrix();

	}







	private void renderStars(net.minecraft.client.renderer.VertexBuffer p_renderStars_1_) {
		Random random = new Random(10842L);
		p_renderStars_1_.begin(7, DefaultVertexFormats.POSITION);

		for(int i = 0; i < 1500; ++i) {
			double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;
			if(d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d0 *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * 3.141592653589793D * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for(int j = 0; j < 4; ++j) {
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					p_renderStars_1_.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}

	}
}

