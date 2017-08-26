// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG node type: Restart
*/

public class TRestart extends TCommandNode
{
	public short[] ref_list;
	public int px, py, pz;
	public short sx, sy, sz;
	public String name;

	public TRestart(ByteBuffer fp)
	{
		short ref_count = fp.getShort();
		this.ref_list = new short[ref_count];
		for(int iref = 0; iref < ref_count; iref++) {
			this.ref_list[iref] = fp.getShort();
		}

		pad32(fp);
		this.px = fp.getInt();
		this.py = fp.getInt();
		this.pz = fp.getInt();
		this.sx = fp.getShort();
		this.sy = fp.getShort();
		this.sz = fp.getShort();
		this.name = readString(fp);
		pad16(fp);
		parseCommands(fp);
	}

	public String getParamString()
	{
		return String.format("%s, <%d, %d, %d>, <%d, %d, %d>, \"%s\", %s"
			, makeNodeString(this.ref_list)
			, this.px , this.py , this.pz
			, this.sx , this.sy , this.sz
			, this.name
			, getCommandString()
		);
	}
}





