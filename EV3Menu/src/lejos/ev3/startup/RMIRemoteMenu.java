package lejos.ev3.startup;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.remote.ev3.Menu;
import lejos.remote.ev3.RMIMenu;

public class RMIRemoteMenu extends UnicastRemoteObject implements RMIMenu {
	private Menu menu;
	
	private static final long serialVersionUID = 9132686914626791288L;

	protected RMIRemoteMenu(Menu menu) throws RemoteException {
		super(0);
		this.menu = menu;
	}

	@Override
	public void runProgram(String programName) throws RemoteException {
		menu.runProgram(programName);
	}

	@Override
	public boolean deleteFile(String fileName) throws RemoteException {
		return menu.deleteFile(fileName);
	}

	@Override
	public String[] getProgramNames() throws RemoteException {
		return menu.getProgramNames();
	}

	@Override
	public void debugProgram(String programName) throws RemoteException {
		menu.debugProgram(programName);
	}

	@Override
	public void runSample(String programName) throws RemoteException {
		menu.runSample(programName);
	}

	@Override
	public String[] getSampleNames() throws RemoteException {
		return menu.getSampleNames();
	}
	
	public long getFileSize(String filename) {
		return menu.getFileSize(filename);
	}

	@Override
	public boolean uploadFile(String fileName, byte[] contents)
			throws RemoteException {
		return menu.uploadFile(fileName, contents);
	}

	@Override
	public byte[] fetchFile(String fileName) throws RemoteException {
		return menu.fetchFile(fileName);
	}

	@Override
	public String getSetting(String setting) throws RemoteException {
		return menu.getSetting(setting);
	}

	@Override
	public void setSetting(String setting, String value)
			throws RemoteException {
		menu.setSetting(setting, value);
	}

	@Override
	public void deleteAllPrograms() throws RemoteException {
		menu.deleteAllPrograms();
	}

	@Override
	public String getVersion() throws RemoteException {
		return menu.getVersion();
	}

	@Override
	public String getMenuVersion() throws RemoteException {
		return menu.getMenuVersion();
	}

	@Override
	public String getName() throws RemoteException {
		return menu.getName();
	}

	@Override
	public void setName(String name) throws RemoteException {
		menu.setName(name);
	}

	@Override
	public void configureWifi(String ssid, String pwd) throws RemoteException {
		WPASupplicant.writeConfiguration("wpa_supplicant.txt",  "wpa_supplicant.conf", ssid, pwd);	
	}

	@Override
	public void stopProgram() throws RemoteException {
		menu.stopProgram();
	}

	@Override
	public String getExecutingProgramName() throws RemoteException {
		return menu.getExecutingProgramName();
	}

	@Override
	public void shutdown() throws RemoteException {
		menu.shutdown();
	}

	@Override
	public void suspend() throws RemoteException {
		menu.suspend();		
	}

	@Override
	public void resume() throws RemoteException {
		menu.resume();	
	}
}
