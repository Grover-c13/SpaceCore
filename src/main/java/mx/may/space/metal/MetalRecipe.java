package mx.may.space.metal;

import mx.may.space.Common;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class MetalRecipe implements IRecipe
{
	private int seed; // hack for client side colour, should be okay though!
	private Item slots[];
	private Item result;

	private boolean setDurability;
	public MetalRecipe(Item[] slots, Item result, boolean setDurability)
	{
		if (slots.length != 9 || result == null) {
			throw new IllegalArgumentException("Metal recipe is for fixed 3x3 recipes only!");
		}
		this.slots = slots;
		this.result = result;
		this.setDurability = setDurability;
	}


	public boolean slotMatches(ItemStack slotItem, int slot)
	{
		boolean match = false;
		if (slotItem != null)
		{

			if (slotItem.getItem().equals(slots[slot]))
			{
				match = true;
			}
		}
		else
		{
			// if the recipe slot is null its a match
			if (slots[slot] == null) {
				match = true;
			}
		}

		return match;
	}



	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world)
	{
		boolean match = true;
		int firstMetalSeed = -1;


		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
			ItemStack item = inventoryCrafting.getStackInSlot(i);

			if (!slotMatches(item, i))
			{
				System.out.println("Slot " + i + " did not match");
				match = false;
				break;
			}

			// check to see if all ingots are the same
			// record the first seed and then check all others
			if (item != null)
			{
				if (item.getUnlocalizedName().equals("space.ingot"))
				{

					if (firstMetalSeed == -1)
					{
						firstMetalSeed = item.getItemDamage();
						seed = item.getItemDamage();
					} else if (firstMetalSeed != item.getItemDamage())
					{
						match = false;
						break;
					}
				}
			}
		}

		if (match)
		{
			System.out.println("MATCH!");
		}

		return match;
	}


	public int findSeed(InventoryCrafting inventoryCrafting)
	{

		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++)
		{
			ItemStack item = inventoryCrafting.getStackInSlot(i);

			if (item != null)
			{
				if (item.getUnlocalizedName().equals("space.ingot"))
				{
					return item.getItemDamage();
				}
			}
		}

		throw new IllegalArgumentException("trying to get metal seed from recipe with no metal!");
	}

	@Nullable
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
	{
		ItemStack resultStack = new ItemStack(result);
		int metalSeed = findSeed(inventoryCrafting);
		MetalProperties props = new MetalProperties(metalSeed);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("metal", metalSeed);
		resultStack.setTagCompound(nbt);
		resultStack.setStackDisplayName(	"Durability: " + props.getDurability() + "\r" +
											"Strength: " + props.getStrength() + "\r" +
											"Conductivity: " + props.getConductivity() + "\r");
		return resultStack;
	}



	@Override
	public int getRecipeSize()
	{
		return 9;
	}

	@Nullable
	@Override
	public ItemStack getRecipeOutput()
	{
		ItemStack output = new ItemStack(Common.metalPickaxe);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("metal", seed); 	// have to use field since nothing passed to this
										// may cause syncing issues server side
										// (player1 and player2 start crafting a pickaxe at same time, both reading
										//  from this field) may not be an issue since this is only the "preview" of the item
		output.setTagCompound(nbt);
		return output;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv)
	{
		ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);

			if (itemstack != null && itemstack.getItem().hasContainerItem(itemstack))
			{
				aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
			}
		}

		return aitemstack;
	}
}
