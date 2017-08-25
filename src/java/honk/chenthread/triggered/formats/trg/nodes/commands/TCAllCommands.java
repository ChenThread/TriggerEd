// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.trg.nodes.commands;

import honk.chenthread.triggered.formats.trg.*;
import honk.chenthread.triggered.formats.trg.nodes.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* All commands themselves.
*/

public class TCAllCommands
{
	public static class TCSendPulse extends TCommand {} // 3
	public static class TCSendActivate extends TCommand {} // 4
	public static class TCSendSuspend extends TCommand {} // 5

	public static class TCSendSignal extends TCommand {} // 10
	public static class TCSendKill extends TCommand {} // 11
	public static class TCSendKillLoudly extends TCommand {} // 12

	public static class TCSendVisible extends TCommand {
		public short unk1;

		public TCSendVisible(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 13

	public static class TCSetFoggingParams extends TCommand {
		public short unk1;
		public short unk2;
		public short unk3;

		public TCSetFoggingParams(ByteBuffer fp) {
			this.unk1 = fp.getShort();
			this.unk2 = fp.getShort();
			this.unk3 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
			fp.putShort(this.unk2);
			fp.putShort(this.unk3);
		}
		protected String getNodeArgString() {
			return String.format("%d, %d, %d", unk1, unk2, unk3);
		}
	} // 104

	public static class TCSpoolIn extends TCommand {
		public String fname;

		public TCSpoolIn(ByteBuffer fp) {
			this.fname = this.readString(fp);
		}
		protected void writeArgsTo(ByteBuffer fp) {
			this.writeString(fp, this.fname);
		}
		protected String getNodeArgString() {
			return String.format("\"%s\"", fname);
		}
	} // 126

	public static class TCBackgroundOn extends TCommand {
		public short unk1;

		public TCBackgroundOn(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 131

	public static class TCBackgroundOff extends TCommand {
		public short unk1;

		public TCBackgroundOff(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 132

	public static class TCSetInitialPulses extends TCommand {
		public short unk1;

		public TCSetInitialPulses(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 134

	public static class TCSetRestart extends TCommand {
		public String nodestr;

		public TCSetRestart(ByteBuffer fp) {
			this.nodestr = this.readString(fp);
		}
		protected void writeArgsTo(ByteBuffer fp) {
			this.writeString(fp, this.nodestr);
		}
		protected String getNodeArgString() {
			return String.format("\"%s\"", nodestr);
		}
	} // 140

	public static class TCSetVisibilityInBox extends TCommand {
		public short f1, f2;
		public ArrayList<int[]> boxes = new ArrayList<int[]>();

		public TCSetVisibilityInBox(ByteBuffer fp) {
			this.f1 = fp.getShort();
			this.f2 = fp.getShort();

			// "Support" for THPS1 prototype format
			// I'm just trying to get the damn thing to parse
			// so I can get to the important stuff
			if(f2 != 0 && f2 != 1) {
				fp.position(fp.position()-2);
				f2 = 2;
			}
			pad32(fp);
			while(true) {
				int va = 0xFFFF&(int)fp.getShort();
				if(va == 0x00FF) {
					break;
				}
				int va2 = 0xFFFF&(int)fp.getShort();
				int vx1 = (va2<<16)|va;
				int vy1 = fp.getInt();
				int vz1 = fp.getInt();
				int vx2 = fp.getInt();
				int vy2 = fp.getInt();
				int vz2 = fp.getInt();
				boxes.add(new int[]{
					vx1, vy1, vz1,
					vx2, vy2, vz2,
				});
			}
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.f1);
			fp.putShort(this.f2);
			padWrite32(fp);
			for(int i = 0; i < boxes.size(); i++) {
				int[] box = boxes.get(i);
				for(int j = 0; j < 6; j++) {
					fp.putInt(box[j]);
				}
			}
		}
		protected String getNodeArgString() {
			String ns = "";
			for(int i = 0; i < boxes.size(); i++) {
				if(i != 0) {
					ns += " + ";
				}

				int[] box = boxes.get(i);

				ns += String.format("(<%d, %d, %d>, <%d, %d, %d>)"
					, box[0]
					, box[1]
					, box[2]
					, box[3]
					, box[4]
					, box[5]
				);
			}
			return String.format("%d, %d, [%s]", f1, f2, ns);
		}
	} // 141

	public static class TCSetObjFile extends TCommand {
		public String fname;

		public TCSetObjFile(ByteBuffer fp) {
			this.fname = this.readString(fp);
		}
		protected void writeArgsTo(ByteBuffer fp) {
			this.writeString(fp, this.fname);
		}
		protected String getNodeArgString() {
			return String.format("\"%s\"", fname);
		}
	} // 142

	public static class TCSetGameLevel extends TCommand {
		public short unk1;

		public TCSetGameLevel(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 147

	public static class TCEndif extends TCommand {} // 149

	public static class TCSetDualBufferSize extends TCommand {
		public short unk1;

		public TCSetDualBufferSize(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 151

	public static class TCKillBruce extends TCommand {} // 152

	public static class TCSetReverbType extends TCommand {
		public short unk1;

		public TCSetReverbType(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 157

	public static class TCEndLevel extends TCommand {} // 158

	public static class TCBackgroundCreate extends TCommand {
		public int fhash;
		public short px, py, pz;

		public TCBackgroundCreate(ByteBuffer fp) {
			pad32(fp);
			this.fhash = fp.getInt();
			this.px = fp.getShort();
			this.py = fp.getShort();
			this.pz = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			padWrite32(fp);
			fp.putInt(this.fhash);
			fp.putShort(this.px);
			fp.putShort(this.py);
			fp.putShort(this.pz);
		}
		protected String getNodeArgString() {
			return String.format("0x%08X, %d, %d, %d", fhash, px, py, pz);
		}
	} // 171

	public static class TCSetRestart2 extends TCommand {
		public String nodestr;

		public TCSetRestart2(ByteBuffer fp) {
			this.nodestr = this.readString(fp);
		}
		protected void writeArgsTo(ByteBuffer fp) {
			this.writeString(fp, this.nodestr);
		}
		protected String getNodeArgString() {
			return String.format("\"%s\"", nodestr);
		}
	} // 178

	public static class TCSetFadeColor extends TCommand {
		public short unk1;
		public short unk2;

		public TCSetFadeColor(ByteBuffer fp) {
			this.unk1 = fp.getShort();
			this.unk2 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
			fp.putShort(this.unk2);
		}
		protected String getNodeArgString() {
			return String.format("0x%04X, 0x%04X", unk1, unk2);
		}
	} // 200

	public static class TCGapPolyHit extends TCommand {
		public int nodehash;
		public short unk1;

		public TCGapPolyHit(ByteBuffer fp) {
			pad32(fp);
			this.nodehash = fp.getInt();
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			padWrite32(fp);
			fp.putInt(this.nodehash);
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("0x%08X, %d", nodehash, unk1);
		}
	} // 201

	public static class TCSetSkyColor extends TCommand {
		public short unk1;
		public short unk2;

		public TCSetSkyColor(ByteBuffer fp) {
			this.unk1 = fp.getShort();
			this.unk2 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
			fp.putShort(this.unk2);
		}
		protected String getNodeArgString() {
			return String.format("0x%04X, 0x%04X", unk1, unk2);
		}
	} // 202

	public static class TCSetCareerFlag extends TCommand {
		public short unk1;

		public TCSetCareerFlag(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 203

	public static class TCIfCareerFlag extends TCommand {
		public short unk1;

		public TCIfCareerFlag(ByteBuffer fp) {
			this.unk1 = fp.getShort();
		}
		protected void writeArgsTo(ByteBuffer fp) {
			fp.putShort(this.unk1);
		}
		protected String getNodeArgString() {
			return String.format("%d", unk1);
		}
	} // 204

	public static class TCEndCommandList extends TCommand {}
	//public static class TCEndCommandList extends TCommand {}
}
