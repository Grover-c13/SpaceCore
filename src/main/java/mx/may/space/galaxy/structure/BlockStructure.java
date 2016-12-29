package mx.may.space.galaxy.structure;


import io.netty.buffer.ByteBuf;
import mx.may.space.Client;
import mx.may.space.galaxy.bodies.RotationalState;
import mx.may.space.galaxy.bodies.SpaceBody;
import mx.may.space.galaxy.player.SpacePlayer;
import mx.may.space.galaxy.utils.BlockUtils;
import mx.may.space.galaxy.utils.BufferUtil;
import mx.may.space.galaxy.utils.MatrixUtils;
import mx.may.space.galaxy.utils.QuaternionUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;


import javax.vecmath.Matrix3d;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Courtney on 28/11/2016.
 */
public class BlockStructure extends SpaceBody
{
	private BlockStructureAccess access;
	private ConcurrentHashMap<BlockPos, SpaceBlockState> blocks;
	private ConcurrentHashMap<BlockPos, SpaceBlockState> addedBlocks;
	private ConcurrentHashMap<BlockPos, SpaceBlockState> removedBlocks;



	private double massX;
	private double massY;
	private double massZ;
	private int totalMass;

	private boolean rerender;
	private HashSet<SpaceBlockState> renderBlocks;


	private double lastLocalX;
	private double lastLocalY;
	private double lastLocalZ;
	private Quaternion lastPrimaryRotation;
	private Quaternion lastPrimaryReverseRotation;

	private HashSet<SpacePlayer> riding;
	private HashSet<Integer> sentPlayers;



	public BlockStructure(double x, double y, double z)
	{
		super(x, y, z);
		access = new BlockStructureAccess(this);
		blocks = new ConcurrentHashMap<BlockPos, SpaceBlockState>();
		addedBlocks = new ConcurrentHashMap<BlockPos, SpaceBlockState>();
		removedBlocks = new ConcurrentHashMap<BlockPos, SpaceBlockState>();
		massX = 0;
		massY = 0;
		massZ = 0;
		totalMass = 0;
		rerender = true;
		renderBlocks = new HashSet<SpaceBlockState>();
		lastPrimaryRotation = new Quaternion();
		lastPrimaryReverseRotation = new Quaternion();
		riding = new HashSet<SpacePlayer>();
		sentPlayers = new HashSet<Integer>();

	}

	public void addBlockState(BlockPos pos, short health, short mass, IBlockState state)
	{
		BlockPos rpos = getRelativeBlockPos(pos);
		massX += mass*(rpos.getX()+0.5);
		massY += mass*(rpos.getY()+0.5);
		massZ += mass*(rpos.getZ()+0.5);
		totalMass += mass;
		updateCenterOfGravity();
		SpaceBlockState spaceState = new SpaceBlockState(health, mass, state, rpos);
		blocks.put(rpos, spaceState);
		addedBlocks.put(rpos, spaceState);
		this.getRotationalState().calculateTorques();
		rerender = true;

		this.addVertex(rpos.getX(), rpos.getY(), rpos.getZ());
		this.addVertex(rpos.getX()+1, rpos.getY(), rpos.getZ());
		this.addVertex(rpos.getX(), rpos.getY()+1, rpos.getZ());
		this.addVertex(rpos.getX(), rpos.getY(), rpos.getZ()+1);
		this.addVertex(rpos.getX(), rpos.getY()+1, rpos.getZ()+1);
		this.addVertex(rpos.getX()+1, rpos.getY(), rpos.getZ()+1);
		this.addVertex(rpos.getX()+1, rpos.getY()+1, rpos.getZ());
		this.addVertex(rpos.getX()+1, rpos.getY()+1, rpos.getZ()+1);
		this.buildHull();
	}

	@SideOnly(Side.CLIENT)
	public void addLocalBlockState(SpaceBlockState state)
	{
		blocks.put(state.getLocation(), state);
		rerender = true;
	}


	public void removeBlockState(BlockPos pos) {

		BlockPos rpos = getRelativeBlockPos(pos);
		if (blocks.containsKey(rpos)) {
			SpaceBlockState spaceState = blocks.get(rpos);
			massX -= spaceState.getMass()*(rpos.getX()+0.5);
			massY -= spaceState.getMass()*(rpos.getY()+0.5);
			massZ -= spaceState.getMass()*(rpos.getZ()+0.5);
			totalMass -= spaceState.getMass();
			updateCenterOfGravity();
			removedBlocks.put(rpos, spaceState);
			blocks.remove(rpos);
		}

		if (totalMass <= 0) {
			totalMass = 0;
			massX = 0;
			massY = 0;
			massZ = 0;
		}

		this.removeForce(rpos);
		this.getRotationalState().removeTorque(rpos);
		rerender = true;
	}


	public ConcurrentHashMap<BlockPos, SpaceBlockState> getBlockStates()
	{
		return this.blocks;
	}


	public void render(SpacePlayer player, double px, double py, double pz)
	{

		if (isRiding(player))
		{
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.translate(this.getX()-px, this.getY()-py, this.getZ()-pz);

			DisplayDebugger.drawAll(this);
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();

			DisplayDebugger.drawAfter(this, px, py , pz);
			return;
		}



		// get position relative to the space player
		double relX = this.getX()-player.getX();
		double relY = this.getY()-player.getY()+80;
		double relZ = this.getZ()-player.getZ();

		double doubleX = (px-relX);
		double doubleY = (py-relY);
		double doubleZ = (pz-relZ);



		RotationalState rotation = this.getRotationalState();
		FloatBuffer orientation_matrix = QuaternionUtils.toFloatBuffer(rotation.getOrientation());


		Tessellator tess = Tessellator.getInstance();
		VertexBuffer vb = tess.getBuffer();
		BlockRendererDispatcher blockDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		TextureManager renderEngine = Minecraft.getMinecraft().getRenderManager().renderEngine;
		renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate(-doubleX, -doubleY, -doubleZ);
		GlStateManager.multMatrix(orientation_matrix);

		if (rerender)
		{
			renderBlocks.clear();
			vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			for (Map.Entry<BlockPos, SpaceBlockState> entry: blocks.entrySet())
			{

				IBakedModel model = blockDispatcher.getModelForState(entry.getValue().getState());
				//blockDispatcher.renderBlock(set.getValue().getState(), set.getKey(), this, vb);
				boolean rendered = blockDispatcher.getBlockModelRenderer().renderModelSmooth(access, model, entry.getValue().getState(), entry.getKey(), vb, true, 0);
				entry.getValue().setModel(model);


				if (rendered) {
					renderBlocks.add(entry.getValue());
				}
			}
			tess.draw();
			rerender = false;
		}
		else
		{


			vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			for (SpaceBlockState state : renderBlocks)
			{

				blockDispatcher.getBlockModelRenderer().renderModelFlat(access, state.getModel(), state.getState(), state.getLocation(), vb, true, 0);

			}
			tess.draw();
		}


		DisplayDebugger.drawAll(this);
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();

		DisplayDebugger.drawAfter(this, px, py , pz);



	}





	public void updateCenterOfGravity() {
		double cogX = massX / totalMass;
		double cogY = massY / totalMass;
		double cogZ = massZ / totalMass;
		Vec3d pos = new Vec3d(cogX, cogY, cogZ);
		this.setCog(pos);
	}


	public Vec3d getCenterOfGravityAbsolute() {
		return this.getCog().addVector(this.getX(), this.getY(), this.getZ());
	}

	public Vec3d getAbsoluteVec(Vec3d vec) {
		Vec3d newVec = new Vec3d(vec.xCoord, vec.yCoord, vec.zCoord);
		return newVec.addVector(this.getX(), this.getY(), this.getZ());
	}

	public BlockPos getAbsoluteBlockPos(BlockPos pos) {
		BlockPos newPos = new BlockPos(pos.getX()+this.getX(), pos.getY()+this.getY(), pos.getZ()+this.getZ());
		//newPos.add(x, y, z);
		return newPos;
	}

	@Override
	public void addForce(BlockPos pos, Vec3d force)
	{
		BlockPos rpos = getRelativeBlockPos(pos);
		super.addForce(rpos, force);
	}


	public void update(float deltaTime) {

		super.update(deltaTime);
		// update riding players orientation

		for (SpacePlayer player : riding)
		{
			player.setRotationalState(this.getRotationalState());
			player.setAcceleration(this.getAcceleration());
			player.setVelocity(this.getVelocity());
			player.setX(getX());
			player.setY(getY());
			player.setZ(getZ());
		}
	}




	public BlockPos getRelativeBlockPos(BlockPos pos)
	{
		return new BlockPos(pos.getX()-this.getX(), pos.getY()-this.getY(), pos.getZ()-this.getZ());
	}




	public Vec3d getLocalPos(SpacePlayer player)
	{
		RotationalState rotation = player.getRotationalState();


		double relativeX, relativeY, relativeZ;

		relativeX = player.getX()-this.getX();////;
		relativeY = player.getY()-this.getY();//-this.getY();
		relativeZ = player.getZ()-this.getZ();//-this.getZ();

		//return new Vec3d(nx, ny, nz);
		return QuaternionUtils.getLocalPoint(rotation.getOrientation(), this, relativeX, relativeY, relativeZ);

	}

	public int getMass()
	{
		return totalMass;
	}

	public boolean isBlockInStructure(BlockPos pos) {
		BlockPos rpos = getRelativeBlockPos(pos);
		return blocks.containsKey(rpos);
	}




	public float calculateMomentOfInertia()
	{
		float inertia = 0;
		double distance;

		for (Map.Entry<BlockPos, SpaceBlockState> entry : getBlockStates().entrySet())
		{
			distance = BlockUtils.blockPosToVec(entry.getKey()).distanceTo(getCog());
			inertia += distance*distance*entry.getValue().getMass();
		}

		return inertia;
	}

	@Override
	public int getFactoryId()
	{
		return BlockStructureBodyFactory.FACTORY_ID;
	}

	@Override
	public void donePlayerUpdates()
	{
		addedBlocks.clear();
		removedBlocks.clear();
	}

	public void addPlayer(SpacePlayer player)
	{
		riding.add(player);
	}

	public void removePlayer(SpacePlayer player)
	{
		riding.remove(player);
	}

	public boolean isRiding(SpacePlayer player)
	{
		return riding.contains(player);
	}

	public boolean entOnWorldStructure(Entity ent)
	{
		boolean onStructure = false;
		BlockPos loc = this.getRelativeBlockPos(ent.getPosition());
		for (int x = -1; x <= 1; x++)
		{
			for (int y = -1; y <= 1; y++)
			{
				for (int z = -1; z <= 1; z++)
				{
					BlockPos pos = new BlockPos(loc.getX()+x, loc.getY()+y, loc.getZ()+z);
					boolean contains = this.blocks.containsKey(pos);
					onStructure = onStructure || contains;
				}
			}
		}

		return onStructure;
	}


	@Override
	public void updateFromServer(ByteBuf buffer)
	{
		super.updateFromServer(buffer);

		// read block additions
		int numAddBlocks = buffer.readShort();
		for (int i = 0; i < numAddBlocks; i++)
		{
			addLocalBlockState(new SpaceBlockState(buffer));
		}

		// read block removals
		int numRemBlocks = buffer.readShort();
		for (int i = 0; i < numRemBlocks; i++)
		{
			BlockPos loc = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			if (blocks.containsKey(loc)) {
				blocks.remove(loc);
				rerender = true;
			}
		}

		int numRiding = buffer.readShort();
		for (int i = 0; i < numRiding; i++)
		{
			// since this is client side we only actually care if its us as to not
			UUID playerID = UUID.fromString(BufferUtil.readString(buffer));

			if (playerID.equals(Client.OBJECT_STORE.getPlayer().getPlayer()))
			{
				this.riding.add(Client.OBJECT_STORE.getPlayer());
			}
		}
	}

	@Override
	public void updateToClient(ByteBuf buffer, EntityPlayerMP player)
	{
		super.updateToClient(buffer, player);
		HashSet<Integer> newPlayers = new HashSet<Integer>(); // as we go remove players who are no longer getting updates from this structure by adding all current players who are to a new set
		Map<BlockPos, SpaceBlockState> additions;
		if (sentPlayers.contains(player.getEntityId()))
		{
			additions = addedBlocks; // only send new blocks
		}
		else
		{
			// send all blocks otherwise
			additions = blocks;
		}

		newPlayers.add(player.getEntityId());

		// add blocks
		buffer.writeShort(additions.size());
		for (SpaceBlockState state : additions.values())
		{
			state.addToByteBuf(buffer);
		}

		// remove blocks
		// only need to send the location
		buffer.writeShort(removedBlocks.size());
		for (BlockPos location : removedBlocks.keySet())
		{
			 buffer.writeInt(location.getX());
			 buffer.writeInt(location.getY());
			 buffer.writeInt(location.getZ());
		}

		buffer.writeShort(riding.size());
		for (SpacePlayer splayer : riding)
		{
			BufferUtil.writeString(buffer, splayer.getPlayer().toString());
		}


		// set the new list of players
		this.sentPlayers = newPlayers;

	}


}
