package lejos.remote.nxt;

import java.io.File;
import java.io.IOException;

import lejos.hardware.Audio;
import lejos.hardware.port.PortException;
import lejos.utility.Delay;

public class RemoteNXTAudio implements Audio {
	private NXTCommand nxtCommand;    
	
	public static int C2 = 523;
	
	public RemoteNXTAudio(NXTCommand nxtCommand) {
		this.nxtCommand = nxtCommand;
	}
	
	@Override
    public void systemSound(int aCode)
    {
        if (aCode == 0)
            playTone(600, 200);
        else if (aCode == 1)
        {
            playTone(600, 150);
            pause(200);
            playTone(600, 150);
            pause(150);
        }
        else if (aCode == 2)// C major arpeggio
            for (int i = 4; i < 8; i++)
            {
                playTone(C2 * i / 4, 100);
                pause(100);
            }
        else if (aCode == 3)
            for (int i = 7; i > 3; i--)
            {
                playTone(C2 * i / 4, 100);
                pause(100);
            }
        else if (aCode == 4)
        {
            playTone(100, 500);
            pause(500);
        }
    }
	
	private void pause(int duration) {
		Delay.msDelay(duration);
	}

	@Override
	public void playTone(int frequency, int duration, int aVolume) {
		try {
			nxtCommand.playTone(frequency, duration);
		} catch (IOException ioe) {
			throw new PortException(ioe);
		}
	}

	@Override
	public void playTone(int frequency, int duration) {
		try {
			nxtCommand.playTone(frequency, duration);
		} catch (IOException ioe) {
			throw new PortException(ioe);
		}
	}

	@Override
	public int playSample(File file, int vol) {
		try {
			return nxtCommand.playSoundFile(file.getName(), false);
		} catch (IOException ioe) {
			throw new PortException(ioe);
		}
	}

	@Override
	public int playSample(File file) {
		try {
			return nxtCommand.playSoundFile(file.getName(), false);
		} catch (IOException ioe) {
			throw new PortException(ioe);
		}
	}

	@Override
	public int playSample(byte[] data, int offset, int len, int freq, int vol) {
		throw new UnsupportedOperationException("playSample with data not supported on NXT");
	}

	@Override
	public void playNote(int[] inst, int freq, int len) {
		throw new UnsupportedOperationException("playNote not supported on NXT");
		
	}

	@Override
	public void setVolume(int vol) {
		throw new UnsupportedOperationException("setVolume not supported on NXT");
		
	}

	@Override
	public int getVolume() {
		throw new UnsupportedOperationException("getVolume not supported on NXT");
	}

	@Override
	public void loadSettings() {
		// Do nothing
	}
}
