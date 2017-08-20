// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG node type: AutoExec
*/

public class TAutoExec extends TCommandNode
{
	public TAutoExec(ByteBuffer fp)
	{
		parseCommands(fp);
	}

	public String getParamString()
	{
		return String.format("!!!"
		);
	}
}




