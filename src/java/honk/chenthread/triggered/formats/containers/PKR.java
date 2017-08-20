// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.containers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.UnsupportedEncodingException;

/**
* PKR file container.
*/
public class PKR implements FileContainer
{
	private class FileListEntry
	{
		public String fname;
		public int fflags;
		public int foffs;
		public int flen1;
		public int flen2;
	}

	private ByteBuffer fp;
	private HashMap<String, FileListEntry> filemap;
	private ArrayList<String> filenamelist;

	/**
	* Provides read-only access to a PKR archive.
	*
	* @param filebuf ByteBuffer to use as a file source
	*/
	public PKR(ByteBuffer filebuf)
	{
		this.fp = filebuf;
		this.fp.order(ByteOrder.LITTLE_ENDIAN);

		// Read header
		this.fp.position(0x0000);
		assertAlways(this.fp.get() == 'P');
		assertAlways(this.fp.get() == 'K');
		assertAlways(this.fp.get() == 'R');
		assertAlways(this.fp.get() == '2');
		assertAlways(this.fp.get() == 0x01);
		assertAlways(this.fp.get() == 0x00);
		assertAlways(this.fp.get() == 0x00);
		assertAlways(this.fp.get() == 0x00);
		int dircount = this.fp.getInt();
		int filecount = this.fp.getInt();
		assertAlways(dircount >= 0);
		assertAlways(filecount >= 0);

		this.filemap = new HashMap<String, FileListEntry>();
		this.filenamelist = new ArrayList<String>();

		// Read directory info
		for(int idir = 0; idir < dircount; idir++) {
			this.fp.position(0x0010 + (32+4+4)*idir);
			String dname = this.readstrfinite(fp, 32);
			assertAlways(dname.endsWith("/"));
			int doffs = this.fp.getInt();
			int dlen = this.fp.getInt();
			//System.out.printf("DIR \"%s\" offs=%d len=%d\n", dname, doffs, dlen);

			// Read each filename
			for(int ifile = 0; ifile < dlen; ifile++) {
				this.fp.position(doffs + (32+4+4+4+4)*ifile);
				FileListEntry fle = new FileListEntry();
				String fname_raw = this.readstrfinite(fp, 32);
				fle.fname = dname + fname_raw;
				fle.fflags = this.fp.getInt();
				fle.foffs = this.fp.getInt();
				fle.flen1 = this.fp.getInt();
				fle.flen2 = this.fp.getInt();
				assertAlways(fle.flen1 == fle.flen2);
				filemap.put(fle.fname.toLowerCase(), fle);
				filenamelist.add(fle.fname);
				//System.out.printf("FILE \"%s\" flags=%d offs=%d len=%d\n", fname, fflags, foffs, flen1);
			}
		}
	}

	private String readstrfinite(ByteBuffer fp, int len)
	{
		byte[] s_buf = new byte[len];
		fp.get(s_buf, 0, len);
		int tlen = 0;
		while(tlen < len) {
			if(s_buf[tlen] == 0) {
				break;
			}
			tlen++;
		}

		try {
			return new String(s_buf, 0, tlen, "utf8");
		} catch(UnsupportedEncodingException ex) {
			throw new RuntimeException("WHAT FUCKING OPERATING SYSTEM DOESN'T HAVE UTF-8, WHATEVER THE FUCK YOU'RE RUNNING, THAT'S WHAT, I MEAN EVEN WINDOWS SUPPORTS UTF-8");
		}
	}

	private void assertAlways(boolean whyAmICodingInJava)
	{
		if(!whyAmICodingInJava) {
			throw new RuntimeException("Assertion Failed!");
		}
	}

	/**
	* Loads a read-only file given a filename.
	*
	* @param fname Filename to load from
	* @return Buffer to the given file, or null on failure
	*/
	public ByteBuffer readFile(String fname)
	{
		String key = fname.toLowerCase();
		if(this.filemap.containsKey(key)) {
			FileListEntry fle = this.filemap.get(key);
			this.fp.position(fle.foffs);
			ByteBuffer bbuf = this.fp.slice();
			bbuf.limit(fle.flen1);
			return bbuf;
		} else {
			return null;
		}
	}

	/**
	* Obtain a list of filenames.
	* @return List of filenames
	*/
	public List<String> getFileNames()
	{
		return new ArrayList<String>(this.filenamelist);
	}
}
