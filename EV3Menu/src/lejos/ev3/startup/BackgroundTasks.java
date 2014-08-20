package lejos.ev3.startup;

import java.net.URL;

import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

public class BackgroundTasks implements KeyListener {

    private final TextLCD lcd = LocalEV3.get().getTextLCD();

    private final RobertaTokenRegister rtr;
    private final Thread tokenThread;

    public BackgroundTasks(URL serverTokenRessource, String token) {
        this.rtr = new RobertaTokenRegister(serverTokenRessource, token);
        this.tokenThread = new Thread(this.rtr);
    }

    public void register() {
        this.tokenThread.start();
    }

    @Override
    public void keyPressed(Key arg0) {
        this.tokenThread.interrupt();
    }

    @Override
    public void keyReleased(Key arg0) {
        //
    }

}
