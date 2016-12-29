package mx.may.space.metal;


import mx.may.space.Common;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMetalIngot extends Item
{
    public ItemMetalIngot()
    {
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
    public String getItemStackDisplayName(ItemStack stack)
    {
        return "Metal";
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack p_onItemRightClick_1_, World world, EntityPlayer p, EnumHand p_onItemRightClick_4_)
    {
		if (!world.isRemote)
		{
		    Common.GALAXY_MAN.reset();
			Common.GALAXY_MAN.putPlayerInSpace(p, 0, 0, 0 );
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, p_onItemRightClick_1_);
    }

}
