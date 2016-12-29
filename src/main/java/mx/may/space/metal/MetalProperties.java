package mx.may.space.metal;


import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * Created by Courtney on 14-Nov-16.
 */
public class MetalProperties
{
	public static final int MAX_COLOR = 16777215;
	private static Random rand = new Random();
    private int seed;
	private int color;
	private int durability;
	private int strength;
	private int conductivity;


	public static int readNBT(ItemStack item)
	{
		int seed;
		if (item.hasTagCompound())
		{
			if (item.getTagCompound().hasKey("metal"))
			{
				seed = item.getTagCompound().getInteger("metal");
			}
			else
			{
				throw new IllegalArgumentException("Cant create metal properties as item missing metal NBT tag");
			}
		}
		else
		{
			throw new IllegalArgumentException("Cant create metal properties as no NBT");
		}

		return seed;
	}

	public MetalProperties(ItemStack item)
	{
		this(readNBT(item));
	}

    public MetalProperties(int seed)
    {
        this.seed = seed;
		rand.setSeed(seed);
		this.color = rand.nextInt(MAX_COLOR);
		durability = getLevels();
		strength = getLevels();
		conductivity = getLevels();
    }


	public int getColor()
	{
		return color;
	}

	public int getDurability()
	{
		return durability;
	}

	public int getStrength()
	{
		return strength;
	}

	public int getConductivity()
	{
		return conductivity;
	}

	private int getLevels()
	{

		int levels = Math.max(1, rand.nextInt(5));


		// 50% chance for 0-2 more levels
		if (rand.nextBoolean())
		{
			levels += rand.nextInt(2);
		}


		// 25% for 1 more levels
		if (rand.nextInt(4) == 2)
		{
			levels += 1;
		}

		// 10% for 1 more level
		if (rand.nextInt(10) == 1)
		{
			levels += 1;
		}

		// 5% for 1 more level
		if (rand.nextInt(20) == 1)
		{
			levels += 1;
		}

		return levels;

	}

}
