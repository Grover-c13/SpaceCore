package mx.may.space.metal;

import mx.may.space.Common;
import mx.may.space.galaxy.PlayerSpaceZone;
import mx.may.space.galaxy.dimension.SpaceTeleporter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * Created by Courtney on 14-Nov-16.
 */
public class ItemMetalPickaxe extends ItemPickaxe
{
	public ItemMetalPickaxe()
	{
		super(ToolMaterial.IRON);
	}

	@Override
	public Item getContainerItem()
	{
		return super.getContainerItem();
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		MetalProperties prop = new MetalProperties(stack);
		int dur = 2*prop.getDurability();
		int durCubed = dur*dur*dur;
		return 60+durCubed;
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack p_onItemRightClick_1_, World world, EntityPlayer p, EnumHand p_onItemRightClick_4_)
	{
		System.out.println("CLICK");
		return new ActionResult<ItemStack>(EnumActionResult.PASS, p_onItemRightClick_1_);
	}


}
