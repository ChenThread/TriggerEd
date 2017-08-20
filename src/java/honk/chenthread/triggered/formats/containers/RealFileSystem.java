// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.containers;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
* Real file system.
*/
public class RealFileSystem implements FileContainer
{
	private RealFileSystem() {}
	private static RealFileSystem SINGLETON = new RealFileSystem();
	public static RealFileSystem getImpl()
	{
		return SINGLETON;
	}

	/**
	* Loads a read-only file given a filename.
	*
	* @param fname Filename to load from
	* @return Buffer to the given file, or null on failure
	*/
	public ByteBuffer readFile(String fname)
	{
		FileChannel fc = null;

		try {
			// there is a pre-1.7 way of doing this
			// but not a pre-1.4 way
			// but who the hell uses pre-1.4 anyway
			fc = FileChannel.open(new File(fname).toPath(),
				StandardOpenOption.READ);
			ByteBuffer filebuf = fc.map(FileChannel.MapMode.READ_ONLY,
				0, fc.size());
			return filebuf;
		} catch(IOException ex) {
		} catch(InvalidPathException ex) {
		}

		if(fc != null) {
			try {
				fc.close();
			} catch(IOException ex2) {
			}
		}
		return null;
	}

	/**
	* Obtain a list of filenames.
	* @return List of filenames
	*/
	public List<String> getFileNames()
	{
		// TODO!
		return new ArrayList<String>();
	}
}

