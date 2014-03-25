package lejos.remote.ev3;

public interface Menu {
	
	public void runProgram(String programName);
	
	public void runSample(String programName);
	
	public void debugProgram(String programName);
	
	public boolean deleteFile(String fileName);
	
	public void stopProgram();
	
	public long getFileSize(String filename);
	
	public String[] getProgramNames();
	
	public String[] getSampleNames();

	public boolean uploadFile(String fileName, byte[] contents);
	
	public byte[] fetchFile(String fileName);
	
	public String getSetting(String setting);
	
	public void setSetting(String setting, String value);
	
	public void deleteAllPrograms();
	
	public String getVersion();
	
	public String getMenuVersion();
	
	public String getName();
	
	public void setName(String name);
	
	public String getExecutingProgramName();
	
	public void shutdown();
}
