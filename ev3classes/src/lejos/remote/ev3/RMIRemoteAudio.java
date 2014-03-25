package lejos.remote.ev3;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.Audio;
import lejos.hardware.ev3.LocalEV3;

public class RMIRemoteAudio extends UnicastRemoteObject implements RMIAudio {
	private Audio audio = LocalEV3.get().getAudio();
	
	private static final long serialVersionUID = -1570513073865401851L;

	protected RMIRemoteAudio() throws RemoteException {
		super(0);
	}

	@Override
	public void systemSound(int aCode) throws RemoteException {
		audio.systemSound(aCode);
	}

	@Override
	public void playTone(int aFrequency, int aDuration, int aVolume)  throws RemoteException {
		audio.playTone(aFrequency, aDuration, aVolume);	
	}

	@Override
	public void playTone(int freq, int duration)  throws RemoteException {
		audio.playTone(freq, duration);
	}

	@Override
	public int playSample(File file, int vol)  throws RemoteException {
		return audio.playSample(file, vol);
	}

	@Override
	public int playSample(File file)  throws RemoteException {
		return audio.playSample(file);
	}

	@Override
	public int playSample(byte[] data, int offset, int len, int freq, int vol)  throws RemoteException{
		return audio.playSample(data, offset, len, freq, vol);
	}

	@Override
	public void playNote(int[] inst, int freq, int len)  throws RemoteException {
		audio.playNote(inst, freq, len);
	}

	@Override
	public void setVolume(int vol)  throws RemoteException {
		audio.setVolume(vol);	
	}

	@Override
	public int getVolume()  throws RemoteException{
		return audio.getVolume();
	}

	@Override
	public void loadSettings()  throws RemoteException {
		audio.loadSettings();
	}
}
