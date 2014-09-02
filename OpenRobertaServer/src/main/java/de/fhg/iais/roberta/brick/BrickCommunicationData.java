package de.fhg.iais.roberta.brick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.Pair;

public class BrickCommunicationData {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicationData.class);

    private Clock lastRequestClock = Clock.start();
    private BrickCommunicationState lastRequest = BrickCommunicationState.NOTHING_TO_DO;
    private final String token;
    private String programName;
    private String brickConfiguration;

    public BrickCommunicationData(String token) {
        this.token = token;
    }

    public synchronized Pair<String, String> brickDownloadRequest() {
        BrickCommunicationState download = BrickCommunicationState.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED;
        BrickCommunicationState nothingToDo = BrickCommunicationState.NOTHING_TO_DO;
        if ( this.lastRequest == nothingToDo || this.lastRequest == download ) {
            LOG.debug("BRICK starts waiting for run button. " + this.lastRequest + " -> " + download);
            this.lastRequest = download;
            this.lastRequestClock = Clock.start();
            Clock waitTime = Clock.start();
            while ( this.lastRequest == download ) {
                try {
                    wait();
                } catch ( InterruptedException e ) {
                    // try again
                }
            }
            LOG.debug("Waiting BRICK got run button after " + waitTime.elapsedSecFormatted() + ". " + this.lastRequest + " -> " + nothingToDo);
            this.lastRequest = nothingToDo;
            return Pair.of(this.token, this.programName);
        } else if ( this.lastRequest == BrickCommunicationState.RUN_BUTTON_WAS_PRESSED ) {
            LOG.debug("BRICK detects run button press " + this.lastRequestClock.elapsedSecFormatted() + " ago. " + this.lastRequest + " -> " + nothingToDo);
            this.lastRequest = nothingToDo;
            return Pair.of(this.token, this.programName);
        } else {
            throw new DbcException("Found an invalid BrickCommunicationRequest: " + this.lastRequest);
        }
    }

    public synchronized String runButtonPressed(String programName, String brickConfiguration) {
        String commResult = this.lastRequest == BrickCommunicationState.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED ? "Brick is waiting" : "Brick is not waiting";
        LOG.debug("RUN button pressed. " + commResult + ". state " + this.lastRequest + " entered " + this.lastRequestClock.elapsedSecFormatted() + " ago");
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.lastRequestClock = Clock.start();
        this.lastRequest = BrickCommunicationState.RUN_BUTTON_WAS_PRESSED;
        notify();
        return commResult;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getBrickConfiguration() {
        return this.brickConfiguration;
    }
}