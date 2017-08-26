// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* Base class for all TRG nodes.
*/
public abstract class TNode
{
	public String toString()
	{
		String[] cnamea1 = getClass().getName().split("\\$");
		String[] cnamea2 = cnamea1[cnamea1.length-1].split("\\.");
		return String.format("%s(%s)"
			, cnamea2[cnamea2.length-1]
			, this.getParamString()
		);
	}

	public String getParamString()
	{
		return " ? ";
	}

	//
	// Helpers
	//
	public static void pad16(ByteBuffer fp)
	{
		fp.position((fp.position()+0x1)&~0x1);
	}

	public static void pad32(ByteBuffer fp)
	{
		fp.position((fp.position()+0x3)&~0x3);
	}

	public static void padWrite16(ByteBuffer fp)
	{
		// TODO!
		fp.position((fp.position()+0x1)&~0x1);
	}

	public static void padWrite32(ByteBuffer fp)
	{
		// TODO!
		fp.position((fp.position()+0x3)&~0x3);
	}

	public static String makeNodeString(short[] ref_list)
	{
		String s = "";
		s += "[";
		for(int iref = 0; iref < ref_list.length; iref++) {
			if(iref != 0) {
				s += ", ";
			}
			s += String.format("%d", ref_list[iref]);
		}
		s += "]";
		return s;
	}

	public static String readString(ByteBuffer fp)
	{
		String s = "";

		while(true) {
			int c = 0xFF&(int)fp.get();
			if(c == 0) {
				break;
			}
			s += (char)c;
		}

		return s;
	}

	public static void writeString(ByteBuffer fp, String s)
	{
		for(int i = 0; i < s.length(); i++) {
			fp.put((byte)s.charAt(i));
		}
		fp.put((byte)0);
	}
}

