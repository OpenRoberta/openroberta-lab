package de.fhg.iais.roberta.brick;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.fhg.iais.roberta.util.Pair;

/**
 * class, that synchronizes the communication between the bricks and the web-app. Thread-safe. See class {@link BrickCommunicationData} for
 * further explanations.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 * 
 * @author rbudde
 */
@Singleton
public class BrickCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicator.class);
    private final Map<String, BrickCommunicationData> allStates = new ConcurrentHashMap<>();

    public BrickCommunicator() {
        LOG.info("created");
    }

    public Pair<String, String> iAmABrickAndWantToWaitForARunButtonPress(String token) {
        BrickCommunicationData singleState = getSingleState(token);
        return singleState.brickDownloadRequest();
    }

    public String theRunButtonWasPressed(String token, String programName, String brickConfigurationName) {
        BrickCommunicationData singleState = getSingleState(token);
        return singleState.runButtonPressed(programName, brickConfigurationName);
    }

    private BrickCommunicationData getSingleState(String token) {
        BrickCommunicationData singleState = this.allStates.get(token);
        if ( singleState == null ) {
            singleState = new BrickCommunicationData(token);
            this.allStates.put(token, singleState);
        }
        return singleState;
    }
}
