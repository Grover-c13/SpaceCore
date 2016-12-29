package mx.may.space.metal.block;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by Courtney on 17-Nov-16.
 */
public class UnlistedPropertyMetalSeed implements IUnlistedProperty<IBlockState>
{
	@Override
	public String getName()
	{
		return "metal_seed";
	}

	@Override
	public boolean isValid(IBlockState iBlockState)
	{
		return true;
	}

	@Override
	public Class<IBlockState> getType()
	{
		return IBlockState.class;
	}

	@Override
	public String valueToString(IBlockState iBlockState)
	{
		return iBlockState.toString();
	}
}
