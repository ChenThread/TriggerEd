// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered;

import honk.chenthread.triggered.formats.*;
import honk.chenthread.triggered.formats.containers.*;
import java.nio.ByteBuffer;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("TriggerEd: Tony Hawk map manipulation and editing tools");
		System.out.println("Copyright (C) Chen Thread, 2017");
		FileContainer fc_pkr = new PKR(RealFileSystem.getImpl().readFile("ALL.PKR"));
		for(String fname : fc_pkr.getFileNames()) {
			if(true && fname.toLowerCase().endsWith(".trg")) {
				ByteBuffer trgbuf = fc_pkr.readFile(fname);
				System.out.printf("- \"%s\" %d\n", fname
					, (trgbuf == null ? -1 : trgbuf.hashCode()));
				TRG trg = new TRG(trgbuf);
			}
			if(false && fname.toLowerCase().endsWith(".psx")) {
				ByteBuffer psxbuf = fc_pkr.readFile(fname);
				System.out.printf("- \"%s\" %d\n", fname
					, (psxbuf == null ? -1 : psxbuf.hashCode()));
				PSX psx = new PSX(psxbuf);
			}
		}
		//for(;;) { try { Thread.sleep(1000); } catch (InterruptedException ex) {} }
	}
}

