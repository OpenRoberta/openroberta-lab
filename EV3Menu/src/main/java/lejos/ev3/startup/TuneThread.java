package lejos.ev3.startup;

import lejos.hardware.Sound;

/**
 * Thread to play the tune
 */
public class TuneThread extends Thread
{
	
	@Override
	public void run()
	{
		playTune();
	}
    
    /**
     * Play the leJOS startup tune.
     */
    static void playTune()
    {
        int[] freq = { 523, 784, 659 };
        for (int i = 0; i < 3; i++) {
            Sound.playTone(freq[i], 300);
            //Sound.pause(300);
        }
    }
}
