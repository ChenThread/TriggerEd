// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG node type: Crate
*/

public class TCrate extends TNode
{
	public int unk1;
	public int namehash;

	public TCrate(ByteBuffer fp)
	{
		this.unk1 = fp.getShort();
		this.pad32(fp);
		this.namehash = fp.getInt();
	}

	public String getParamString()
	{
		return String.format("%d, 0x%08X"
			, this.unk1
			, this.namehash
		);
	}
}


