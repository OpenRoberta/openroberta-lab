package lejos.ev3.startup;

import java.net.URL;

import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class BackgroundTasks implements KeyListener, Runnable {

    private final RobertaTokenRegister rtr;
    private final RobertaDownloader rd;

    private Thread tokenThread;
    private Thread downloadThread;

    public BackgroundTasks(URL serverTokenRessource, URL serverDownloadRessource, String token) {
        this.rtr = new RobertaTokenRegister(serverTokenRessource, token);
        this.rd = new RobertaDownloader(serverDownloadRessource, token);
    }

    public void startRegisteringThread() {
        this.rtr.setErrorInfo(false);
        this.rtr.setRegisteredInfo(false);
        this.rtr.setTimeOutInfo(false);
        this.tokenThread = new Thread(this.rtr);
        this.tokenThread.start();
    }

    public void startDownloadThread() {
        this.downloadThread = new Thread(this.rd);
        this.downloadThread.start();
    }

    public void stopDownload() {
        this.rd.getHttpConnection().disconnect();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(Key arg0) {
        setRegInterruptInfo(true);
    }

    @Override
    public void keyReleased(Key arg0) {
        //
    }

    public boolean getTimeOutInfo() {
        return this.rtr.getTimeOutInfo();
    }

    public boolean getRegisteredInfo() {
        return this.rtr.getRegisteredInfo();
    }

    public boolean getErrorInfo() {
        return this.rtr.getErrorInfo();
    }

    public boolean getRegInterruptInfo() {
        return this.rtr.getRegInterruptInfo();
    }

    public void setRegInterruptInfo(boolean bool) {
        this.rtr.setRegInterruptInfo(bool);
    }

}
