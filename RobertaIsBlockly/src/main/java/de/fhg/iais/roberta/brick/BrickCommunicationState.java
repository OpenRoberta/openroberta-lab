package de.fhg.iais.roberta.brick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.util.Clock;

public class BrickCommunicationState {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicationState.class);

    private Clock lastRequestClock = Clock.start();
    private BrickCommunicationRequest lastRequest = BrickCommunicationRequest.NOTHING_TO_DO;
    private String programName;

    public synchronized String iAmABrickAndWantToWaitForARunButtonPress() {
        if ( this.lastRequest == BrickCommunicationRequest.NOTHING_TO_DO || this.lastRequest == BrickCommunicationRequest.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED ) {
            LOG.debug("BRICK will wait for the run button to be pressed. State: " + this.lastRequest);
            this.lastRequest = BrickCommunicationRequest.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED;
            this.lastRequestClock = Clock.start();
            Clock waitTime = Clock.start();
            while ( this.lastRequest == BrickCommunicationRequest.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED ) {
                try {
                    wait();
                } catch ( InterruptedException e ) {
                    // try again
                }
            }
            notify();
            LOG.debug("BRICK detects that the run button was pressed. Waited " + waitTime.elapsedSecFormatted());
            return this.programName;
        } else if ( this.lastRequest == BrickCommunicationRequest.RUN_BUTTON_OF_WEB_APP_WAS_PRESSED ) {
            LOG.debug("BRICK asks for a download. The run button was pressed " + this.lastRequestClock.elapsedSecFormatted() + " ago");
            this.lastRequest = BrickCommunicationRequest.NOTHING_TO_DO;
            return this.programName;
        } else {
            throw new DbcException("Found an invalid BrickCommunicationRequest: " + this.lastRequest);
        }
    }

    public synchronized String theRunButtonWasPressed(String programName) {
        String commResult =
            this.lastRequest == BrickCommunicationRequest.DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED
                ? "brick seems to wait to download a program"
                : "brick is not waiting to download a program";
        LOG.debug("RUN button was pressed. Old state: " + this.lastRequest + " set " + this.lastRequestClock.elapsedSecFormatted() + " ago - " + commResult);
        this.programName = programName;
        this.lastRequestClock = Clock.start();
        this.lastRequest = BrickCommunicationRequest.RUN_BUTTON_OF_WEB_APP_WAS_PRESSED;
        notify();
        return commResult;
    }
}
