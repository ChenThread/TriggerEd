// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;
import honk.chenthread.triggered.formats.trg.nodes.commands.*;
import static honk.chenthread.triggered.formats.trg.nodes.commands.TCAllCommands.*;

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
		while(true)
		{
			TCommand cmd = TCommand.parse(fp);
			command_list.add(cmd);
			if(cmd == null) { break; }
			if(cmd instanceof TCEndCommandList) {
				break;
			}
		}
	}

	public String getCommandString()
	{
		String s = "";
		for(int i = 0; i < this.command_list.size(); i++) {
			if(i != 0) {
				s += ";  ";
			}

			TCommand cmd = this.command_list.get(i);
			if(cmd == null) {
				s += "???";
			} else {
				s += cmd.toString();
			}
		}
		return "["+s+"]";
	}
}

