package lejos.remote.nxt;

/**
 * Structure that gives information about a leJOS NXJ file.
 *
 */
public class FileInfo {
	
	/**
	 * The name of the file - up to 20 characters.
	 */
	public String fileName;
	
	/**
	 * The handle for accessing the file.
	 */
	public byte fileHandle;
	
	/**
	 * The size of the file in bytes.
	 */
	public int fileSize;
	
	public FileInfo(String fileName) {
		this.fileName = fileName;
	}	
}
