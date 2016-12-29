package mx.may.space.galaxy.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * Created by Courtney on 17-Nov-16.
 */
public class SpaceDimension extends WorldProvider
{


	public SpaceDimension()
	{
		setCloudRenderer(new SpaceCloudRenderer());
		setSkyRenderer(new SpaceSkyRenderer());
		setWeatherRenderer(new SpaceWeatherRenderer());
	}


	@Override
	public DimensionType getDimensionType()
	{
		return DimensionType.getById(100);
	}


	@Override
	protected void generateLightBrightnessTable()
	{
		super.generateLightBrightnessTable();
	}

	@Override
	protected void createBiomeProvider()
	{
		super.createBiomeProvider();
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new SpaceChunkGenerator(this.worldObj);
	}

	@Override
	public boolean canCoordinateBeSpawn(int p_canCoordinateBeSpawn_1_, int p_canCoordinateBeSpawn_2_)
	{
		return true;
	}

	@Override
	public float calculateCelestialAngle(long p_calculateCelestialAngle_1_, float p_calculateCelestialAngle_3_)
	{
		return super.calculateCelestialAngle(p_calculateCelestialAngle_1_, p_calculateCelestialAngle_3_);
	}

	@Override
	public int getMoonPhase(long p_getMoonPhase_1_)
	{
		return 0;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return super.isSurfaceWorld();
	}

	@Nullable
	@Override
	public float[] calcSunriseSunsetColors(float p_calcSunriseSunsetColors_1_, float p_calcSunriseSunsetColors_2_)
	{
		return super.calcSunriseSunsetColors(p_calcSunriseSunsetColors_1_, p_calcSunriseSunsetColors_2_);
	}

	@Override
	public Vec3d getFogColor(float p_getFogColor_1_, float p_getFogColor_2_)
	{
		return new Vec3d(10, 10, 10);
	}

	@Override
	public boolean canRespawnHere()
	{
		return super.canRespawnHere();
	}

	@Override
	public float getCloudHeight()
	{
		return 99999999;
	}

	@Override
	public boolean isSkyColored()
	{
		return super.isSkyColored();
	}

	@Override
	public BlockPos getSpawnCoordinate()
	{
		return super.getSpawnCoordinate();
	}

	@Override
	public int getAverageGroundLevel()
	{
		return super.getAverageGroundLevel();
	}

	@Override
	public double getVoidFogYFactor()
	{
		return 0;
	}

	@Override
	public boolean doesXZShowFog(int p_doesXZShowFog_1_, int p_doesXZShowFog_2_)
	{
		return false;
	}

	@Override
	public BiomeProvider getBiomeProvider()
	{
		return super.getBiomeProvider();
	}

	@Override
	public boolean doesWaterVaporize()
	{
		return super.doesWaterVaporize();
	}

	@Override
	public boolean getHasNoSky()
	{
		return false;
	}

	@Override
	public float[] getLightBrightnessTable()
	{
		return super.getLightBrightnessTable();
	}

	@Override
	public WorldBorder createWorldBorder()
	{
		return super.createWorldBorder();
	}

	@Override
	public void setDimension(int p_setDimension_1_)
	{
		super.setDimension(p_setDimension_1_);
	}

	@Override
	public int getDimension()
	{
		return super.getDimension();
	}

	@Override
	public String getSaveFolder()
	{
		return super.getSaveFolder();
	}

	@Override
	public String getWelcomeMessage()
	{
		return super.getWelcomeMessage();
	}

	@Override
	public String getDepartMessage()
	{
		return super.getDepartMessage();
	}

	@Override
	public double getMovementFactor()
	{
		return super.getMovementFactor();
	}



	@Override
	public BlockPos getRandomizedSpawnPoint()
	{
		return super.getRandomizedSpawnPoint();
	}

	@Override
	public boolean shouldMapSpin(String p_shouldMapSpin_1_, double p_shouldMapSpin_2_, double p_shouldMapSpin_4_, double p_shouldMapSpin_4_2)
	{
		return super.shouldMapSpin(p_shouldMapSpin_1_, p_shouldMapSpin_2_, p_shouldMapSpin_4_, p_shouldMapSpin_4_2);
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP p_getRespawnDimension_1_)
	{
		return super.getRespawnDimension(p_getRespawnDimension_1_);
	}

	@Override
	public ICapabilityProvider initCapabilities()
	{
		return super.initCapabilities();
	}

	@Override
	public Biome getBiomeForCoords(BlockPos p_getBiomeForCoords_1_)
	{
		return super.getBiomeForCoords(p_getBiomeForCoords_1_);
	}

	@Override
	public boolean isDaytime()
	{
		return false;
	}

	@Override
	public float getSunBrightnessFactor(float p_getSunBrightnessFactor_1_)
	{
		return 0;
	}

	@Override
	public float getCurrentMoonPhaseFactor()
	{
		return super.getCurrentMoonPhaseFactor();
	}

	@Override
	public Vec3d getSkyColor(Entity p_getSkyColor_1_, float p_getSkyColor_2_)
	{
		return new Vec3d(0, 0, 0);
	}

	@Override
	public Vec3d getCloudColor(float p_getCloudColor_1_)
	{
		return super.getCloudColor(p_getCloudColor_1_);
	}

	@Override
	public float getSunBrightness(float p_getSunBrightness_1_)
	{
		return 1;
	}

	@Override
	public float getStarBrightness(float p_getStarBrightness_1_)
	{
		return 1;
	}

	@Override
	public void setAllowedSpawnTypes(boolean p_setAllowedSpawnTypes_1_, boolean p_setAllowedSpawnTypes_2_)
	{
		super.setAllowedSpawnTypes(p_setAllowedSpawnTypes_1_, p_setAllowedSpawnTypes_2_);
	}

	@Override
	public void calculateInitialWeather()
	{

	}

	@Override
	public void updateWeather()
	{

	}

	@Override
	public boolean canBlockFreeze(BlockPos p_canBlockFreeze_1_, boolean p_canBlockFreeze_2_)
	{
		return super.canBlockFreeze(p_canBlockFreeze_1_, p_canBlockFreeze_2_);
	}

	@Override
	public boolean canSnowAt(BlockPos p_canSnowAt_1_, boolean p_canSnowAt_2_)
	{
		return false;
	}

	@Override
	public void setWorldTime(long p_setWorldTime_1_)
	{
		super.setWorldTime(p_setWorldTime_1_);
	}

	@Override
	public long getSeed()
	{
		return super.getSeed();
	}

	@Override
	public long getWorldTime()
	{
		return super.getWorldTime();
	}

	@Override
	public BlockPos getSpawnPoint()
	{
		return super.getSpawnPoint();
	}

	@Override
	public void setSpawnPoint(BlockPos p_setSpawnPoint_1_)
	{
		super.setSpawnPoint(p_setSpawnPoint_1_);
	}

	@Override
	public boolean canMineBlock(EntityPlayer p_canMineBlock_1_, BlockPos p_canMineBlock_2_)
	{
		return super.canMineBlock(p_canMineBlock_1_, p_canMineBlock_2_);
	}

	@Override
	public boolean isBlockHighHumidity(BlockPos p_isBlockHighHumidity_1_)
	{
		return super.isBlockHighHumidity(p_isBlockHighHumidity_1_);
	}

	@Override
	public int getHeight()
	{
		return super.getHeight();
	}

	@Override
	public int getActualHeight()
	{
		return super.getActualHeight();
	}

	@Override
	public double getHorizon()
	{
		return super.getHorizon();
	}

	@Override
	public void resetRainAndThunder()
	{
		super.resetRainAndThunder();
	}

	@Override
	public boolean canDoLightning(Chunk p_canDoLightning_1_)
	{
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(Chunk p_canDoRainSnowIce_1_)
	{
		return false;
	}

	@Override
	public void onPlayerAdded(EntityPlayerMP p_onPlayerAdded_1_)
	{
		super.onPlayerAdded(p_onPlayerAdded_1_);
	}

	@Override
	public void onPlayerRemoved(EntityPlayerMP p_onPlayerRemoved_1_)
	{
		super.onPlayerRemoved(p_onPlayerRemoved_1_);
	}

	@Override
	public void onWorldSave()
	{
		super.onWorldSave();
	}

	@Override
	public void onWorldUpdateEntities()
	{
		super.onWorldUpdateEntities();
	}

	@Override
	public boolean canDropChunk(int p_canDropChunk_1_, int p_canDropChunk_2_)
	{
		return super.canDropChunk(p_canDropChunk_1_, p_canDropChunk_2_);
	}
}
