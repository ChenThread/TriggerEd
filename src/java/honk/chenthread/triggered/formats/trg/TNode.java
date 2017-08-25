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
	public void pad16(ByteBuffer fp)
	{
		fp.position((fp.position()+0x1)&~0x1);
	}

	public void pad32(ByteBuffer fp)
	{
		fp.position((fp.position()+0x3)&~0x3);
	}

	public String makeNodeString(short[] ref_list)
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
}

