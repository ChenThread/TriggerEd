// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
* TRG file format
*/
public class TRG
{
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
		System.out.printf("Nodes: %d\n", node_count);
		int[] node_ptrs = new int[node_count];
		for(int inode = 0; inode < node_count; inode++) {
			node_ptrs[inode] = this.fp.getInt();
		}
	}

	private void assertAlways(boolean whyAmICodingInJava)
	{
		if(!whyAmICodingInJava) {
			throw new RuntimeException("Assertion Failed!");
		}
	}

}


