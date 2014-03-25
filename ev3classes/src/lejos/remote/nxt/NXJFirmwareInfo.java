package lejos.remote.nxt;

/**
 * Information about leJOS NXJ firmware and menu
 * 
 * @author Lawrie Griffiths
 *
 */
public class NXJFirmwareInfo {
	public byte firmwareMajorVersion;
	public byte firmwareMinorVersion;
	public byte firmwarePatchLevel;
	public int firmwareRevision;
	public byte menuMajorVersion;
	public byte menuMinorVersion;
	public byte menuPatchLevel;
	public int menuRevision;
	
	/**
	 * Get the full firmware version
	 * 
	 * @return the firmware version as a string
	 */
	public String getFirmwareVersion() {
		return firmwareMajorVersion + "." + firmwareMinorVersion + "." + firmwarePatchLevel + "(" + firmwareRevision + ")";
	}
	
	/**
	 * Get the full firmware version
	 * 
	 * @return the firmware version as a string
	 */
	public String getMenuVersion() {
		return menuMajorVersion + "." + menuMinorVersion + "." + menuPatchLevel + "(" + menuRevision + ")";
	}
}
