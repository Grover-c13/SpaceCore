package mx.may.space;


import mx.may.space.galaxy.bodies.FactoryRegistry;
import mx.may.space.galaxy.client.ObjectStore;
import mx.may.space.galaxy.client.ClientEventHandler;
import mx.may.space.galaxy.player.SpacePlayerBodyFactory;
import mx.may.space.galaxy.structure.BlockStructureBodyFactory;
import mx.may.space.metal.block.BlockMetalColor;
import mx.may.space.metal.item.DurabilityMetalColor;
import mx.may.space.metal.item.NBTMetalColor;
import mx.may.space.metal.item.PlanetaryMetalColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;


public class Client extends Common
{
	public static ObjectStore OBJECT_STORE = new ObjectStore();

	public void preInit()
	{
		super.preInit();
		registerMetalItem(Common.metalIngot, "space:ingot");
		registerMetalItem(Common.metalPickaxe, "space:pickaxe");
		registerMetalItem(Common.metalOreBlock, "space:metal_ore");

		FactoryRegistry.registerFactory(BlockStructureBodyFactory.FACTORY_ID, new BlockStructureBodyFactory());
		FactoryRegistry.registerFactory(SpacePlayerBodyFactory.FACTORY_ID, new SpacePlayerBodyFactory());

		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	public void registerMetalItem(Item item, String id)
	{
		final int DEFAULT_ITEM_SUBTYPE = 0;
		final ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(id, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition()
		{
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return itemModelResourceLocation;
			}
		});
	}



	public void postInit()
	{
		super.postInit();
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new DurabilityMetalColor(), Common.metalIngot);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new PlanetaryMetalColor(), Common.metalOreBlock);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new NBTMetalColor(), Common.metalPickaxe);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new BlockMetalColor(), Common.blockMetalOre);
	}


}