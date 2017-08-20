// vim: sts=0:ts=8:sw=8:noet:
package honk.chenthread.triggered.formats.containers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
* Interface for reading files from some form of filesystem.
*/
public interface FileContainer
{
	/**
	* Loads a read-only file given a filename.
	*
	* @param fname Filename to load from
	* @return Buffer to the given file, or null on failure
	*/
	public ByteBuffer readFile(String fname);

	/**
	* Obtain a list of filenames.
	* @return List of filenames
	*/
	public List<String> getFileNames();
}

