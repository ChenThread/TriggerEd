// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;
import honk.chenthread.triggered.formats.trg.nodes.commands.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG node class: Command Node
*/

public abstract class TCommandNode extends TNode
{
	public ArrayList<TCommand> command_list = new ArrayList<TCommand>();

	public void parseCommands(ByteBuffer fp)
	{
		// TODO!
	}
}

