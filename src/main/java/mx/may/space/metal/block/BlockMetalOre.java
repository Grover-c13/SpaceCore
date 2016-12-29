package mx.may.space.metal.block;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockOre;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by Courtney on 17-Nov-16.
 */
public class BlockMetalOre extends BlockOre
{
	public static final PropertyInteger SEED = PropertyInteger.create("metal_seed", 0, 7);

	public BlockMetalOre()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(SEED, 0));
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {SEED});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		meta = meta % 7; // saftey to to stop crashes if someone gives them selves a block
		return this.getDefaultState().withProperty(SEED, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SEED);
	}


	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
