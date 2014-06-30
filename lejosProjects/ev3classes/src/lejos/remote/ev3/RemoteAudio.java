package lejos.remote.ev3;

import java.io.File;
import java.rmi.RemoteException;

import lejos.hardware.Audio;
import lejos.hardware.port.PortException;

public class RemoteAudio implements Audio {
	private RMIAudio audio;
	
	public RemoteAudio(RMIAudio audio) {
		this.audio=audio;
	}

	@Override
	public void systemSound(int aCode) {
		try {
			audio.systemSound(aCode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void playTone(int aFrequency, int aDuration, int aVolume) {
		try {
			audio.playTone(aFrequency, aDuration, aVolume);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void playTone(int freq, int duration) {
		try {
			audio.playTone(freq, duration);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int playSample(File file, int vol) {
		try {
			return audio.playSample(file, vol);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int playSample(File file) {
		try {
			return audio.playSample(file);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int playSample(byte[] data, int offset, int len, int freq, int vol) {
		try {
			return audio.playSample(data, offset, len, freq, vol);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void playNote(int[] inst, int freq, int len) {
		try {
			audio.playNote(inst, freq, len);
		} catch (RemoteException e) {
			throw new PortException(e);
		}	
	}

	@Override
	public void setVolume(int vol) {
		try {
			audio.setVolume(vol);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public int getVolume() {
		try {
			return audio.getVolume();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public void loadSettings() {
		try {
			audio.loadSettings();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
}
