package mx.may.space.galaxy.structure;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Courtney on 29/11/2016.
 */
public class SpaceBlockState
{
	private short health;
	private short mass;
	private IBlockState state;
	private BlockPos location;
	private IBakedModel model;

	public SpaceBlockState(short health, short mass, IBlockState state, BlockPos location)
	{
		this.health = health;
		this.mass = mass;
		this.state = state;
		this.location = location;
	}

	public SpaceBlockState(ByteBuf buffer)
	{
		this.health = buffer.readShort();
		this.mass = buffer.readShort();
		this.state = Block.REGISTRY.getObjectById(buffer.readInt()).getStateFromMeta(buffer.readInt());
		this.location = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
	}

	public void addToByteBuf(ByteBuf buffer)
	{
		buffer.writeShort(health);
		buffer.writeShort(mass);
		buffer.writeInt(Block.REGISTRY.getIDForObject(this.getState().getBlock()));
		buffer.writeInt(state.getBlock().getMetaFromState(this.getState()));
		buffer.writeInt(location.getX());
		buffer.writeInt(location.getY());
		buffer.writeInt(location.getZ());
	}


	public void setHealth(short health)
	{
		this.health = health;
	}

	public void setMass(short mass)
	{
		this.mass = mass;
	}

	public short getHealth()
	{
		return health;
	}

	public short getMass()
	{
		return mass;
	}

	public IBlockState getState()
	{
		return state;
	}

	public BlockPos getLocation()
	{
		return location;
	}

	public void setModel(IBakedModel model)
	{
		this.model = model;
	}

	public IBakedModel getModel()
	{
		return model;
	}
}