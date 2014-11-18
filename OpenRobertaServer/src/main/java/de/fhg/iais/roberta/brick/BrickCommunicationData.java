package de.fhg.iais.roberta.brick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.Pair;

/**
 * This class is responsible for the synchronisation between the one browser client and one brick. The synchronisation is based on a agreed upon token.
 * This token is stored redundantly in objects of this class.<br>
 * As no push technology to the brick is available, the brick issues a download request command, the thread acting upon this request is frozen and waits
 * until the user has issued a run command in the browser. This unfreezes the thread and the already generated jar is copied to the brick.<br>
 * <br>
 * TODO: This implementation is resource intensive as it freezes an expensive resource, namely a thread. It should be replaced later by async technology.
 *
 * @author rbudde
 */
public class BrickCommunicationData {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicationData.class);

    private Clock lastRequestClock = Clock.start();
    private State lastRequest = State.NOTHING_TO_DO;
    private final String token;
    private String programName;
    private String brickConfigurationName;

    public BrickCommunicationData(String token) {
        this.token = token;
    }

    public synchronized Pair<String, String> brickDownloadRequest() {
        if ( this.lastRequest == State.RUN_BUTTON_WAS_PRESSED ) {
            LOG.debug("Found an outdated BrickCommunicationRequest: " + this.lastRequest + ". Reset to " + State.NOTHING_TO_DO);
            this.lastRequest = State.NOTHING_TO_DO;
        }
        LOG.debug("BRICK starts waiting for run button. " + this.lastRequest + " -> " + State.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED);
        this.lastRequest = State.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED;
        this.lastRequestClock = Clock.start();
        Clock waitTime = Clock.start();
        while ( this.lastRequest == State.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED ) {
            try {
                wait();
            } catch ( InterruptedException e ) {
                // try again
            }
        }
        LOG.debug("Waiting BRICK got answer after " + waitTime.elapsedSecFormatted() + ". " + this.lastRequest + " -> " + State.NOTHING_TO_DO);
        this.lastRequest = State.NOTHING_TO_DO;
        return Pair.of(this.token, this.programName);
    }

    public synchronized String runButtonPressed(String programName, String brickConfigurationName) {
        String commResult = this.lastRequest == State.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED ? "Brick is waiting" : "Brick is not waiting";
        LOG.debug("RUN button pressed. " + commResult + ". state " + this.lastRequest + " entered " + this.lastRequestClock.elapsedSecFormatted() + " ago");
        this.programName = programName;
        this.brickConfigurationName = brickConfigurationName;
        this.lastRequestClock = Clock.start();
        this.lastRequest = State.RUN_BUTTON_WAS_PRESSED;
        notifyAll();
        return commResult;
    }

    public synchronized void abortBrickDownloadRequest() {
        if ( this.lastRequest == State.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED ) {
            this.lastRequest = State.ABORT_DOWNLOAD_REQUEST;
            notifyAll();
        }
    }

    public String getProgramName() {
        return this.programName;
    }

    public Pair<Clock, State> getInfoAboutLastRequest() {
        return Pair.of(this.lastRequestClock, this.lastRequest);
    }

    public String getBrickConfigurationName() {
        return this.brickConfigurationName;
    }

    /**
     * the three states of communication between the brick (requesting a download or not) and the browser client (run button pressed).
     */
    public enum State {
        RUN_BUTTON_WAS_PRESSED, DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED, ABORT_DOWNLOAD_REQUEST, NOTHING_TO_DO;
    }
}