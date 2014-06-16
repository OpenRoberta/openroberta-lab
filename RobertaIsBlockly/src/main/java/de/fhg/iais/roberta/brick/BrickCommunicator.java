package de.fhg.iais.roberta.brick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

/**
 * class, that synchronizes the communication between the bricks and the web-app. All methods are thread-safe. <br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 * 
 * @author rbudde
 */
@Singleton
public class BrickCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicator.class);

    public BrickCommunicator() {
        LOG.info("created");
    }
}
