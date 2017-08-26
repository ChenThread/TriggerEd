// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes.commands;

import honk.chenthread.triggered.formats.trg.*;
import honk.chenthread.triggered.formats.trg.nodes.*;
import static honk.chenthread.triggered.formats.trg.nodes.commands.TCAllCommands.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* Commands for the non-Baddy TRG node types.
*/

public abstract class TCommand
{
	public short typ;

	public static TCommand parse(ByteBuffer fp)
	{
		pad16(fp);
		short typ = fp.getShort();

		switch(typ)
		{
			case 3: return new TCSendPulse();
			case 4: return new TCSendActivate();
			case 5: return new TCSendSuspend();

			case 10: return new TCSendSignal();
			case 11: return new TCSendKill();
			case 12: return new TCSendKillLoudly();
			case 13: return new TCSendVisible(fp);

			case 104: return new TCSetFoggingParams(fp);

			case 126: return new TCSpoolIn(fp);

			case 128: return new TCSpoolEnv(fp);

			case 131: return new TCBackgroundOn(fp);
			case 132: return new TCBackgroundOff(fp);

			case 134: return new TCSetInitialPulses(fp);

			case 140: return new TCSetRestart(fp);
			case 141: return new TCSetVisibilityInBox(fp);
			case 142: return new TCSetObjFile(fp);

			case 147: return new TCSetGameLevel(fp);

			case 149: return new TCEndif();

			case 151: return new TCSetDualBufferSize(fp);
			case 152: return new TCKillBruce();

			case 157: return new TCSetReverbType(fp);
			case 158: return new TCEndLevel();

			case 166: return new TCSetOTPushback(fp);

			case 169: return new TCSetOTPushback2(fp);

			case 171: return new TCBackgroundCreate(fp);

			case 178: return new TCSetRestart2(fp);

			case 200: return new TCSetFadeColor(fp);
			case 201: return new TCGapPolyHit(fp);
			case 202: return new TCSetSkyColor(fp);

			case 203: return new TCSetCareerFlag(fp);
			case 204: return new TCIfCareerFlag(fp);
			//case 205: return new TCIfCareerGoal(fp);

			case -1: return new TCEndCommandList();

			default:
				System.out.printf("*** %04X %d\n", typ, typ);
				return null;
		}
	}

	protected void writeArgsTo(ByteBuffer fp)
	{
	}

	public void writeTo(ByteBuffer fp)
	{
		this.pad16(fp);
		fp.putShort(this.typ);
		this.writeArgsTo(fp);
	}

	protected String getNodeArgString()
	{
		return "";
	}

	public String toString()
	{
		return String.format("%s(%s)"
			, this.getClass().getSimpleName()
			, this.getNodeArgString()
		);
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


