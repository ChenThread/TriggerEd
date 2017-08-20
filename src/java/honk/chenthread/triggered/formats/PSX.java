// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* PSX file format
*/
public class PSX
{
	public static class PObject
	{
		public int flags1;
		public int px, py, pz;
		public int unk2;
		public short unk3, unk4, unk5, unk6;
		public int unk7;
		public int palptr;

		public PObject() {}
	}

	public static class PModel
	{
		public static class PMVertex
		{
			public short vx, vy, vz, vp;
			public PMVertex() {}
		}

		public static class PMPlane
		{
			public short px, py, pz, pp;
			public PMPlane() {}
		}

		public static class PMFace
		{
			public short typ, len;
			public byte[] vlist = new byte[4];
			public byte[] color = new byte[4];
			public short pln_idx;
			public short surf_flags;
			public int tex_idx;
			public byte[] tex_coords = new byte[8];
			public PMFace() {}
		}

		public int flags1;
		public int vtx_count;
		public int pln_count;
		public int fac_count;
		public int unk1;
		public short xmin, xmax;
		public short ymin, ymax;
		public short zmin, zmax;
		public int unk2;

		public HashMap<Integer,PMVertex> vtx_list = new HashMap<Integer,PMVertex>();
		public HashMap<Integer,PMPlane> pln_list = new HashMap<Integer,PMPlane>();
		public HashMap<Integer,PMFace> fac_list = new HashMap<Integer,PMFace>();

		public PModel() {}
	}

	public static class PTexture
	{
		public int unk1;
		public int palsize;
		public int palname;
		public int texidx;
		public int tw;
		public int th;
		public byte[] data;

		public PTexture() {}

		public int getHStride()
		{
			if(this.palsize == 16) {
				return ((this.tw+0x3)&~0x3)>>1;
			} else if(this.palsize == 256) {
				return ((this.tw)+0x3)&~0x3;
			} else {
				throw new RuntimeException("unsupported palette size");
			}
		}

		public int getVStride()
		{
			return this.th;
		}

		public int getBufSize()
		{
			return ((getHStride()*getVStride())+0x3)&~0x3;

		}
	}

	private ByteBuffer fp;
	public HashMap<Integer,PObject> map_obj = new HashMap<Integer,PObject>();
	public HashMap<Integer,PModel> map_mdl = new HashMap<Integer,PModel>();
	public HashMap<Integer,Integer> map_texref = new HashMap<Integer,Integer>();
	public HashMap<Integer,short[]> map_pal4 = new HashMap<Integer,short[]>();
	public HashMap<Integer,short[]> map_pal8 = new HashMap<Integer,short[]>();
	public HashMap<Integer,PTexture> map_tex = new HashMap<Integer,PTexture>();

	public PSX(ByteBuffer filebuf)
	{
		this.fp = filebuf;
		this.fp.order(ByteOrder.LITTLE_ENDIAN);
		this.fp.position(0);
		assertAlways(this.fp.get() == 0x04);
		assertAlways(this.fp.get() == 0x00);
		assertAlways(this.fp.get() == 0x02);
		assertAlways(this.fp.get() == 0x00);

		int meta_ptr = this.fp.getInt();
		int obj_count = this.fp.getInt();
		System.out.printf("  - Objects: %d\n", obj_count);
		for(int iobj = 0; iobj < obj_count; iobj++) {
			PObject po = new PObject();
			po.flags1 = this.fp.getInt();
			po.px = this.fp.getInt();
			po.py = this.fp.getInt();
			po.pz = this.fp.getInt();
			po.unk2 = this.fp.getInt();
			po.unk3 = this.fp.getShort();
			po.unk4 = this.fp.getShort();
			po.unk5 = this.fp.getShort();
			po.unk6 = this.fp.getShort();
			po.unk7 = this.fp.getInt();
			po.palptr = this.fp.getInt();
			this.map_obj.put(iobj, po);
			if(false) {
				System.out.printf("    - %4d: %08X %7d.%03X %7d.%03X %7d.%03X %08X %04X %04X %04X %04X %08X %08X\n"
					, iobj
					, po.flags1
					, po.px>>12, po.px&0xFFF
					, po.py>>12, po.py&0xFFF
					, po.pz>>12, po.pz&0xFFF
					, po.unk2
					, 0xFFFF&(int)po.unk3
					, 0xFFFF&(int)po.unk4
					, 0xFFFF&(int)po.unk5
					, 0xFFFF&(int)po.unk6
					, po.unk7
					, po.palptr
				);
			}
		}
		int mdl_count = this.fp.getInt();
		System.out.printf("  - Models: %d\n", mdl_count);
		int[] mdl_ptrs = new int[mdl_count];
		for(int imdl = 0; imdl < mdl_count; imdl++) {
			mdl_ptrs[imdl] = this.fp.getInt();
		}

		for(int imdl = 0; imdl < mdl_count; imdl++) {
			this.fp.position(mdl_ptrs[imdl]);
			PModel pm = new PModel();
			pm.flags1 = this.fp.getShort();
			pm.vtx_count = this.fp.getShort();
			pm.pln_count = this.fp.getShort();
			pm.fac_count = this.fp.getShort();
			pm.unk1 = this.fp.getInt();
			pm.xmax = this.fp.getShort();
			pm.xmin = this.fp.getShort();
			pm.ymax = this.fp.getShort();
			pm.ymin = this.fp.getShort();
			pm.zmax = this.fp.getShort();
			pm.zmin = this.fp.getShort();
			pm.unk2 = this.fp.getInt();
			this.map_mdl.put(imdl, pm);
			if(false) {
				System.out.printf("    - %4d: %04X %3d %3d %3d %08X %6d %6d %6d %6d %6d %6d %08X\n"
					, imdl
					, pm.flags1
					, pm.vtx_count, pm.pln_count, pm.fac_count
					, pm.unk1
					, pm.xmax
					, pm.xmin
					, pm.ymax
					, pm.ymin
					, pm.zmax
					, pm.zmin
					, pm.unk2
				);
			}

			for(int ivtx = 0; ivtx < pm.vtx_count; ivtx++) {
				PModel.PMVertex pmv = new PModel.PMVertex();
				pmv.vx = this.fp.getShort();
				pmv.vy = this.fp.getShort();
				pmv.vz = this.fp.getShort();
				pmv.vp = this.fp.getShort();
				pm.vtx_list.put(ivtx, pmv);
				if(false) {
					System.out.printf("      - VTX %4d: %6d %6d %6d %04X\n"
						, ivtx
						, pmv.vx
						, pmv.vy
						, pmv.vz
						, 0xFFFF&(int)pmv.vp
					);
				}
			}

			for(int ipln = 0; ipln < pm.pln_count; ipln++) {
				PModel.PMPlane pmp = new PModel.PMPlane();
				pmp.px = this.fp.getShort();
				pmp.py = this.fp.getShort();
				pmp.pz = this.fp.getShort();
				pmp.pp = this.fp.getShort();
				pm.pln_list.put(ipln, pmp);
				if(false) {
					System.out.printf("      - PLN %4d: %6d %6d %6d %04X\n"
						, ipln
						, pmp.px
						, pmp.py
						, pmp.pz
						, 0xFFFF&(int)pmp.pp
					);
				}
			}

			for(int ifac = 0; ifac < pm.fac_count; ifac++) {
				PModel.PMFace pmf = new PModel.PMFace();
				pmf.typ = this.fp.getShort();
				pmf.len = this.fp.getShort();
				assertAlways((pmf.len&0x3) == 0);
				int next_ptr = this.fp.position() + pmf.len - 4;
				assertAlways(pmf.len >= 0x10);
				this.fp.get(pmf.vlist);
				this.fp.get(pmf.color);
				pmf.pln_idx = this.fp.getShort();
				pmf.surf_flags = this.fp.getShort();

				boolean has_tex = ((pmf.typ & 0x0003) != 0x0000);
				pmf.tex_idx = (has_tex ? this.fp.getInt() : 0);
				if(has_tex) { this.fp.get(pmf.tex_coords); }

				pm.fac_list.put(ifac, pmf);

				if(false) {
					System.out.printf("      - FAC %4d: %04X %04X (%3d,%3d,%3d,%3d) %02X%02X%02X%02X %4d %04X"
						, ifac
						, 0xFFFF&(int)pmf.typ
						, 0xFFFF&(int)pmf.len
						, pmf.vlist[3], pmf.vlist[2], pmf.vlist[1], pmf.vlist[0]
						, pmf.color[3], pmf.color[2], pmf.color[1], pmf.color[0]
						, pmf.pln_idx
						, pmf.surf_flags
					);

					if(has_tex) {
						System.out.printf(" %4d (%3d,%3d)(%3d,%3d)(%3d,%3d)(%3d,%3d)"
							, pmf.tex_idx
							, 0xFF&(int)pmf.tex_coords[2*0+0]
							, 0xFF&(int)pmf.tex_coords[2*0+1]
							, 0xFF&(int)pmf.tex_coords[2*1+0]
							, 0xFF&(int)pmf.tex_coords[2*1+1]
							, 0xFF&(int)pmf.tex_coords[2*2+0]
							, 0xFF&(int)pmf.tex_coords[2*2+1]
							, 0xFF&(int)pmf.tex_coords[2*3+0]
							, 0xFF&(int)pmf.tex_coords[2*3+1]
						);
					}
					System.out.printf("\n");
				}
				this.fp.position(next_ptr);
			}
		}

		// If this is an empty model file, do not attempt to read further
		this.fp.position(meta_ptr);
		if(meta_ptr == this.fp.limit()) {
			return;
		}

		// Scan for metatags
		while(true) {
			int tag_typ = this.fp.getInt();
			if(tag_typ == -1) { break; } // Termination condition for this list
			int tag_len = this.fp.getInt();
			//System.out.printf("%08X %08X\n", tag_typ, tag_len);
			int next_ptr = this.fp.position() + tag_len;
			assertAlways((next_ptr&0x3) == 0);
			this.fp.position(next_ptr);
		}

		// Read model pointers
		for(int imdl = 0; imdl < mdl_count; imdl++) {
			this.fp.getInt();
		}

		// Read texture stuff
		int texref_count = this.fp.getInt();
		System.out.printf("  - Texref count: %d\n", texref_count);
		for(int itexref = 0; itexref < texref_count; itexref++) {
			map_texref.put(itexref, this.fp.getInt());
		}

		int pal4_count = this.fp.getInt();
		System.out.printf("  - Pal4 count: %d\n", pal4_count);
		for(int ipal4 = 0; ipal4 < pal4_count; ipal4++) {
			int namehash = this.fp.getInt();
			short[] p4 = new short[16];
			for(int ipalent = 0; ipalent < 16; ipalent++) {
				p4[ipalent] = this.fp.getShort();
			}
			this.map_pal4.put(namehash, p4);
		}

		int pal8_count = this.fp.getInt();
		System.out.printf("  - Pal8 count: %d\n", pal8_count);
		for(int ipal8 = 0; ipal8 < pal8_count; ipal8++) {
			int namehash = this.fp.getInt();
			short[] p8 = new short[256];
			for(int ipalent = 0; ipalent < 256; ipalent++) {
				p8[ipalent] = this.fp.getShort();
			}
			this.map_pal8.put(namehash, p8);
		}

		// Read texture pointers
		int tex_count = this.fp.getInt();
		int[] tex_ptrs = new int[tex_count];
		System.out.printf("  - Texture count: %d\n", tex_count);
		for(int itex = 0; itex < tex_count; itex++) {
			tex_ptrs[itex] = this.fp.getInt();
		}

		// Now read actual textures
		for(int itex = 0; itex < tex_count; itex++) {
			this.fp.position(tex_ptrs[itex]);
			PTexture pt = new PTexture();
			pt.unk1 = this.fp.getInt();
			pt.palsize = this.fp.getInt();
			pt.palname = this.fp.getInt();
			pt.texidx = this.fp.getInt();
			pt.tw = this.fp.getShort();
			pt.th = this.fp.getShort();
			pt.data = new byte[pt.getBufSize()];
			this.fp.get(pt.data);
			if(false) {
				System.out.printf("    - %4d: %08X %3d %08X %3d %3d %3d\n"
					, itex
					, pt.unk1
					, pt.palsize
					, pt.palname
					, pt.texidx
					, pt.tw, pt.th
				);
			}
			int exp_next_ptr = (
				itex == tex_count-1
				? this.fp.limit()
				: tex_ptrs[itex+1]
			);
			//System.out.printf("%08X %08X\n", this.fp.position(), exp_next_ptr);
			assertAlways(this.fp.position() == exp_next_ptr);
			this.map_tex.put(itex, pt);
			
		}
		//if(tex_ptrs.length >= 1) { System.out.printf("%08X %08X\n", tex_ptrs[0], this.fp.position()); }
	}

	private void assertAlways(boolean whyAmICodingInJava)
	{
		if(!whyAmICodingInJava) {
			throw new RuntimeException("Assertion Failed!");
		}
	}

}



