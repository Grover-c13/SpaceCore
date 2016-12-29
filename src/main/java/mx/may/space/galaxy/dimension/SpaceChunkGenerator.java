package mx.may.space.galaxy.dimension;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Courtney on 18-Nov-16.
 */
public class SpaceChunkGenerator implements IChunkGenerator
{
	private World world;
	private ChunkPrimer primer;

	public SpaceChunkGenerator(World world)
	{
		this.world = world;
		this.primer = new ChunkPrimer();

		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{

				for (int y = 0; y < 256; y++)
				{
					primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
				}
			}
		}
	}

	@Override
	public Chunk provideChunk(int x, int y)
	{
		return new Chunk(world, primer , x, y);
	}

	@Override
	public void populate(int i, int i1)
	{

	}

	@Override
	public boolean generateStructures(Chunk chunk, int i, int i1)
	{
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos)
	{
		return null;
	}

	@Nullable
	@Override
	public BlockPos getStrongholdGen(World world, String s, BlockPos blockPos)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunk, int i, int i1)
	{

	}
}
