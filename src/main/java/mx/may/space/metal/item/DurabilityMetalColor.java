package mx.may.space.metal.item;


import mx.may.space.metal.MetalProperties;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class DurabilityMetalColor implements IItemColor {

    @Override
    public int getColorFromItemstack(ItemStack itemStack, int i) {
        MetalProperties prop = new MetalProperties(itemStack.getItemDamage());
        return prop.getColor();
    }
}
