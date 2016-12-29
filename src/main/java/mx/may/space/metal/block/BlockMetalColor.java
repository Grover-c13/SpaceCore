package mx.may.space.metal.block;


import mx.may.space.Common;
import mx.may.space.metal.block.BlockMetalOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.property.ExtendedBlockState;

import javax.annotation.Nullable;
import java.awt.*;

public class BlockMetalColor implements IBlockColor {



    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess iBlockAccess, @Nullable BlockPos blockPos, int i)
    {
        int color = Color.WHITE.getRGB();

        if (i == 1)
        {

            int index = state.getValue(BlockMetalOre.SEED);

           // if (Common.earth.hasMetal(index))
            //{
               // color = Common.earth.getMetals()[index].getColor();
            //}
        }


        return color;
}
}
