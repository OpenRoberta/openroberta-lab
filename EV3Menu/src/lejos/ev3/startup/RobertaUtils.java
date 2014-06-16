package lejos.ev3.startup;

import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages brick<->server communication for file downloading. <br>
 * - establish http connection<br>
 * - send token to server<br>
 * - download and save file<br>
 * 
 * @author dpyka
 */
public class RobertaUtils {

    // fileName of program that was downloaded from RobertaLab
    private String fileName;

    // taken from GraphicStartup.java
    // location where user programs are saved to
    private static final String PROGRAMS_DIRECTORY = "/home/lejos/programs";

    /**
     * Default constructor for RobertaUtils
     */
    public RobertaUtils() {
        //
    }

    /**
     * Returns the name of the last file which was downloaded from the server. Returns null if no file downloaded before
     * 
     * @return fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Saves the name of the downloaded file from http header to instance variable
     * 
     * @param fileName the name of the file + file extension
     */
    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Method to retrieve the program from the server
     * TODO exception handling!
     * 
     * @param serverURL a valid server url
     * @param token brick<->client identification token
     */
    public void getProgram(URL serverURL, String token) {
        RobertaDownloadThread rdt = new RobertaDownloadThread(serverURL, token);
        ScheduledExecutorService sTP = Executors.newScheduledThreadPool(1);
        sTP.scheduleAtFixedRate(rdt, 0, 60, TimeUnit.SECONDS);
        setFileName(rdt.getFileName());

        /*try {
            downloadProgramFromServer(openConnection(serverURL), token);
        } catch ( IOException e ) {
            e.printStackTrace();
        }*/
    }

}
