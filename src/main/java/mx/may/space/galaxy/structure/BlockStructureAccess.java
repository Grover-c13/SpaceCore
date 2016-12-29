package mx.may.space.galaxy.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;

import javax.annotation.Nullable;

/**
 * Created by Courtney on 29/11/2016.
 */
public class BlockStructureAccess implements IBlockAccess
{
	private BlockStructure structure;

	public BlockStructureAccess(BlockStructure structure) {
		this.structure = structure;
	}

	@Nullable
	@Override
	public TileEntity getTileEntity(BlockPos blockPos)
	{
		return null;
	}

	@Override
	public int getCombinedLight(BlockPos blockPos, int i)
	{
		return  0xFFFFFF;
	}

	@Override
	public IBlockState getBlockState(BlockPos blockPos)
	{
		if (structure.getBlockStates().containsKey(blockPos))
		{
			return structure.getBlockStates().get(blockPos).getState();
		}

		return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean isAirBlock(BlockPos blockPos)
	{
		return !structure.getBlockStates().containsKey(blockPos);
	}

	@Override
	public Biome getBiome(BlockPos blockPos)
	{
		return Biome.getBiome(0);
	}

	@Override
	public int getStrongPower(BlockPos blockPos, EnumFacing enumFacing)
	{
		return 0;
	}

	@Override
	public WorldType getWorldType()
	{
		return WorldType.DEFAULT;
	}

	@Override
	public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing, boolean b)
	{
		return this.getBlockState(blockPos).isSideSolid(this,blockPos, enumFacing);
	}
}
