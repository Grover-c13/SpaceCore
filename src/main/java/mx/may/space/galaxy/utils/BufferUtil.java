package mx.may.space.galaxy.utils;

import io.netty.buffer.ByteBuf;

/**
 * Created by Courtney on 9/12/2016.
 */
public class BufferUtil
{
	public static String readString(ByteBuf buf)
	{
		int length = buf.readInt();
		char[] letters = new char[length];

		for (int i = 0; i < length; i++)
		{
			letters[i] = buf.readChar();
		}

		return new String(letters);

	}

	public static void writeString(ByteBuf buf, String in)
	{
		int length = in.length();

		buf.writeInt(length);
		for (char c : in.toCharArray())
		{
			buf.writeChar(c);
		}


	}
}
