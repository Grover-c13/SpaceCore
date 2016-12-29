package mx.may.space;

import mx.may.space.galaxy.dimension.SpaceDimension;
import mx.may.space.galaxy.network.UpdateSpaceObjectHandler;
import mx.may.space.galaxy.network.UpdateSpaceObjectPacket;
import mx.may.space.galaxy.server.GalaxyManager;
import mx.may.space.galaxy.server.ServerEventHandler;
import mx.may.space.metal.*;
import mx.may.space.metal.block.BlockMetalOre;
import net.minecraft.item.Item;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;


public class Common
{
    public static ItemMetalIngot metalIngot;
    public static ItemMetalOreBlock metalOreBlock;
    public static ItemMetalPickaxe metalPickaxe;
	public final static SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("space");
	public final static GalaxyManager GALAXY_MAN = new GalaxyManager();

	public static BlockMetalOre blockMetalOre;

    public void preInit()
    {

		NETWORK.registerMessage(UpdateSpaceObjectHandler.class, UpdateSpaceObjectPacket.class, UpdateSpaceObjectPacket.PACKET_ID, Side.CLIENT);


		blockMetalOre = new BlockMetalOre();
		blockMetalOre.setRegistryName("metal_ore");
		GameRegistry.register(blockMetalOre);

        metalIngot = (ItemMetalIngot) new ItemMetalIngot().setUnlocalizedName("ingot");
        metalIngot.setRegistryName("ingot");
        GameRegistry.register(metalIngot);

		metalOreBlock = (ItemMetalOreBlock) new ItemMetalOreBlock(blockMetalOre).setUnlocalizedName("Metal Ore");
		metalOreBlock.setRegistryName("metal_ore");
		GameRegistry.register(metalOreBlock );

		metalPickaxe = (ItemMetalPickaxe) new ItemMetalPickaxe().setUnlocalizedName("pickaxe");
		metalPickaxe.setRegistryName("pickaxe");
		GameRegistry.register(metalPickaxe);



		Item stick = Item.getByNameOrId("minecraft:stick");
		System.out.println(stick.getUnlocalizedName());
		Item[] metalPickaxeRecipe = {	metalIngot, metalIngot, metalIngot,
										null, stick, null,
										null, stick, null};



		GameRegistry.addRecipe(new MetalRecipe(metalPickaxeRecipe, metalPickaxe, true));

		DimensionType.register("SPACE", "SPACE", 100, SpaceDimension.class, true);
		DimensionManager.registerDimension(100, DimensionType.getById(100));
		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }

    public void postInit()
    {


    }




}
