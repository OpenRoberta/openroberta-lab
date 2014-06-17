package de.fhg.iais.roberta.brick;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

/**
 * class, that synchronizes the communication between the bricks and the web-app. Thread-safe. See class {@link BrickCommunicationState} for
 * further explanations.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 * 
 * @author rbudde
 */
@Singleton
public class BrickCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicator.class);
    private final Map<String, BrickCommunicationState> allStates = new ConcurrentHashMap<>();

    public BrickCommunicator() {
        LOG.info("created");
    }

    public String iAmABrickAndWantToWaitForARunButtonPress(String token) {
        BrickCommunicationState singleState = getSingleState(token);
        return singleState.iAmABrickAndWantToWaitForARunButtonPress();
    }

    public String theRunButtonWasPressed(String token, String programName) {
        BrickCommunicationState singleState = getSingleState(token);
        return singleState.theRunButtonWasPressed(programName);
    }

    private BrickCommunicationState getSingleState(String token) {
        BrickCommunicationState singleState = this.allStates.get(token);
        if ( singleState == null ) {
            singleState = new BrickCommunicationState();
            this.allStates.put(token, singleState);
        }
        return singleState;
    }
}
