package mx.may.space.metal;


import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMetalOreBlock extends ItemBlock
{
    public ItemMetalOreBlock(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(64);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int metadata = stack.getMetadata();

        return "space.ingot";
    }


    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public int getMetadata(ItemStack item) {
        return item.getItemDamage();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return "Metal";
    }





}
