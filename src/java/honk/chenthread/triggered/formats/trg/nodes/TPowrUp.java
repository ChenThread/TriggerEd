// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes;

import honk.chenthread.triggered.formats.trg.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG node type: PowrUp
*/

public class TPowrUp extends TCommandNode
{
	public short typ;
	public short[] ref_list;
	public int px, py, pz;
	public int unk1;

	public static final String[] POWRUP_NAMES = {
		null,
		"WavyLaserPickup",
		"ConstantLaserPickup",
		"RapidLaserPickup",
		"KPickup", "SPickup", "APickup",
		"FlamethrowerPickup",
		null,
		null,
		"EPickup",
		null,
		null,
		null,
		"SmallHealth",
		"TPickup",
		"TapePickup",
		"ExtraLife",
		"GotATapeTape",
		"InvisibilityShield", "InvulnerabilityShield",
		"BonusPickup100", "BonusPickup200", "BonusPickup500",
		"MoneyPickup250", "MoneyPickup50", "MoneyPickup100",
		"WheelsPickup", "HeadPickup", "ShoesPickup", "TorsoPickup", "PantsPickup", "BoardPickup",
		"LevelPickup",
	};

	public TPowrUp(ByteBuffer fp)
	{
		this.typ = fp.getShort();
		short ref_count = fp.getShort();
		this.ref_list = new short[ref_count];
		for(int iref = 0; iref < ref_count; iref++) {
			this.ref_list[iref] = fp.getShort();
		}

		pad32(fp);
		this.px = fp.getInt();
		this.py = fp.getInt();
		this.pz = fp.getInt();
		this.unk1 = fp.getInt();
		parseCommands(fp);
	}

	public String getParamString()
	{
		return String.format("%s, %s, <%d, %d, %d>, 0x%08X, %s"
			, (this.typ >= 0 && this.typ < POWRUP_NAMES.length
				&& POWRUP_NAMES[this.typ] != null
				? POWRUP_NAMES[this.typ]
				: String.format("???(%d)", this.typ)
			)
			, makeNodeString(this.ref_list)
			, this.px
			, this.py
			, this.pz
			, this.unk1
			, getCommandString()
		);
	}
}





