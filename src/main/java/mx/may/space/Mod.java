package mx.may.space;


import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.lwjgl.opengl.GL11;

@net.minecraftforge.fml.common.Mod(modid = Mod.MODID, version = Mod.VERSION)
public class Mod
{
	public static final String MODID = "space";
	public static final String VERSION = "1.0";


	@SidedProxy(clientSide="mx.may.space.Client", serverSide="mx.may.space.Server")
	public static Common proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}






}
