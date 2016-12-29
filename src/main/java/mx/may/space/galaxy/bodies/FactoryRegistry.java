package mx.may.space.galaxy.bodies;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;

import java.util.HashMap;

/**
 * Created by Courtney on 7/12/2016.
 */
public class FactoryRegistry
{
	private static HashMap<Integer, BodyFactory> factories = new HashMap<Integer, BodyFactory>();

	public static void registerFactory(Integer id, BodyFactory factory)
	{
		if (!factories.containsKey(id))
		{
			factories.put(id, factory);
		}
		else
		{
			throw new IllegalArgumentException("Trying to add factory with taken id");
		}
	}

	public static BodyFactory getFactory(Integer id)
	{
		return factories.get(id);
	}
}
