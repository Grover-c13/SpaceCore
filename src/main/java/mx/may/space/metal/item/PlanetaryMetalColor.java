package mx.may.space.metal.item;


import mx.may.space.Common;
import mx.may.space.metal.MetalProperties;
import mx.may.space.metal.block.BlockMetalOre;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class PlanetaryMetalColor implements IItemColor {

    @Override
    public int getColorFromItemstack(ItemStack itemStack, int i) {
        int color = Color.WHITE.getRGB();

        if (i == 1)
        {

            int index = itemStack.getItemDamage();

           // if (Common.earth.hasMetal(index))
           // {
           //     color = Common.earth.getMetals()[index].getColor();
           // }
        }


        return color;
    }
}
