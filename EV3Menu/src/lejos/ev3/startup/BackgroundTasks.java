package lejos.ev3.startup;

import java.net.URL;

import lejos.ev3.startup.GraphicStartup.IndicatorThread;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class BackgroundTasks implements KeyListener {

    private final RobertaTokenRegister rtr;
    private final RobertaDownloader rd;

    private RobertaLauncher rl;

    private Thread tokenThread;
    private Thread downloadThread;
    private Thread launcherThread;

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

    public void startLauncherThread(IndicatorThread ind, EchoThread echoIn, EchoThread echoErr) {
        this.rl = new RobertaLauncher(ind, echoIn, echoErr);
        this.launcherThread = new Thread(this.rl);
        this.launcherThread.start();
    }

    public void stopDownload() {
        this.rd.getHttpConnection().disconnect();
    }

    @Override
    public void keyPressed(Key arg0) {
        if ( GraphicStartup.isRobertaRegistered == false ) {
            this.rtr.getHttpConnection().disconnect();
        }
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
}
