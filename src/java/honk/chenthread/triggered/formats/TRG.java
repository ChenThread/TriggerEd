// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats;

import honk.chenthread.triggered.formats.trg.*;
import honk.chenthread.triggered.formats.trg.nodes.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* TRG file format
*/
public class TRG
{
	public static class TSeedableBaddy extends TNode {
		public TSeedableBaddy(ByteBuffer fp) {
		}
	}

	public static class TBarrel extends TNode {
		public TBarrel(ByteBuffer fp) {
		}
	}

	public static class TRailDef extends TNode {
		public TRailDef(ByteBuffer fp) {
		}
	}

	public static class TTrickOb extends TNode {
		public TTrickOb(ByteBuffer fp) {
		}
	}

	public static class TCamPt extends TNode {
		public TCamPt(ByteBuffer fp) {
		}
	}

	public static class TGoalOb extends TNode {
		public TGoalOb(ByteBuffer fp) {
		}
	}

	public static class TTerminator extends TNode {
		public TTerminator(ByteBuffer fp) {
		}
	}

	private HashMap<Integer,TNode> map_node = new HashMap<Integer,TNode>();
	private ByteBuffer fp;

	public TRG(ByteBuffer filebuf)
	{
		this.fp = filebuf;
		this.fp.order(ByteOrder.LITTLE_ENDIAN);
		this.fp.position(0);
		assertAlways(this.fp.get() == '_');
		assertAlways(this.fp.get() == 'T');
		assertAlways(this.fp.get() == 'R');
		assertAlways(this.fp.get() == 'G');
		assertAlways(this.fp.get() == 0x02);
		assertAlways(this.fp.get() == 0x00);
		assertAlways(this.fp.get() <= 0x01);
		assertAlways(this.fp.get() == 0x00);

		int node_count = this.fp.getInt();
		System.out.printf("  - Nodes: %d\n", node_count);
		int[] node_ptrs = new int[node_count];
		for(int inode = 0; inode < node_count; inode++) {
			node_ptrs[inode] = this.fp.getInt();
		}
		for(int inode = 0; inode < node_count; inode++) {
			this.fp.position(node_ptrs[inode]);
			int typ = this.fp.getShort();
			//System.out.printf("Honk: %04X\n", typ);
			TNode tn = null;
			switch(typ) {
				case 1: tn = new TBaddy(this.fp); break;
				case 2: tn = new TCrate(this.fp); break;
				case 3: tn = new TPoint(this.fp); break;
				case 4: tn = new TAutoExec(this.fp); break;
				case 5: tn = new TPowrUp(this.fp); break;
				case 6: tn = new TCommandPoint(this.fp); break;
				case 7: tn = new TSeedableBaddy(this.fp); break;
				case 8: tn = new TRestart(this.fp); break;
				case 9: tn = new TBarrel(this.fp); break;
				case 10: tn = new TRailPoint(this.fp); break;
				case 11: tn = new TRailDef(this.fp); break;
				case 12: tn = new TTrickOb(this.fp); break;
				case 13: tn = new TCamPt(this.fp); break;
				case 14: tn = new TGoalOb(this.fp); break;
				case 15: tn = new TAutoExec2(this.fp); break;
				case 255: tn = new TTerminator(this.fp); break;

				case 501: // I don't know what this is.
				case 1000: // I don't know what this is.
					break;

				default:
					System.err.printf("%d", typ);
					throw new RuntimeException("unhandled mode type");
			}

			System.out.printf("    - %4d: %4d %s\n"
				, inode
				, typ
				, tn
			);
		}
	}

	private void assertAlways(boolean whyAmICodingInJava)
	{
		if(!whyAmICodingInJava) {
			throw new RuntimeException("Assertion Failed!");
		}
	}

}


