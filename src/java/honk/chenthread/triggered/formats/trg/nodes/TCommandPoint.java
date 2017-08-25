// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG node type: CommandPoint
*/

public class TCommandPoint extends TCommandNode
{
	public short[] ref_list;
	public int unk1;

	public TCommandPoint(ByteBuffer fp)
	{
		short ref_count = fp.getShort();
		this.ref_list = new short[ref_count];
		for(int iref = 0; iref < ref_count; iref++) {
			this.ref_list[iref] = fp.getShort();
		}

		pad32(fp);
		this.unk1 = fp.getInt();
		parseCommands(fp);
	}

	public String getParamString()
	{
		return String.format("%s, 0x%08X, %s"
			, makeNodeString(this.ref_list)
			, this.unk1
			, getCommandString()
		);
	}
}





