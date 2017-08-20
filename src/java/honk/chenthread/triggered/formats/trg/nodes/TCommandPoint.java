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
	public TCommandPoint(ByteBuffer fp)
	{
		parseCommands(fp);
	}

	public String getParamString()
	{
		return String.format("!!! %s"
			, getCommandString()
		);
	}
}





