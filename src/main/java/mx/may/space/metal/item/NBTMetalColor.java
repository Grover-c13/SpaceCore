package mx.may.space.metal.item;


import mx.may.space.metal.MetalProperties;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class NBTMetalColor implements IItemColor {

    @Override
    public int getColorFromItemstack(ItemStack itemStack, int i) {
        int color = Color.WHITE.getRGB();

        if (i == 1)
        {
            if (itemStack.hasTagCompound())
            {
                if (itemStack.getTagCompound().hasKey("metal"))
                {
                    MetalProperties prop = new MetalProperties(itemStack.getTagCompound().getInteger("metal"));
                    color = prop.getColor();
                }
            }
        }

        return color;
    }
}