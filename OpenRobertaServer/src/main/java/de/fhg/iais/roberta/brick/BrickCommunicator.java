package de.fhg.iais.roberta.brick;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.util.Pair;

/**
 * class, that synchronizes the communication between the bricks and the web-app. Thread-safe. See class {@link BrickCommunicationData} for
 * further explanations.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author rbudde
 */
public class BrickCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicator.class);
    private final Map<String, BrickCommunicationData> allStates = new ConcurrentHashMap<>();
    private final Map<String, TokenRegistrationData> openTokenAgreementRequest = new ConcurrentHashMap<>();

    public BrickCommunicator() {
        LOG.info("created");
    }

    public boolean iAmABrickAndWantATokenToBeAgreedUpon(String token) {
        synchronized ( BrickCommunicator.class ) {
            Assert.isTrue(this.allStates.get(token) == null, "token already used. New token required.");
        }

        TokenRegistrationData newTokenRegData = new TokenRegistrationData();
        TokenRegistrationData oldTokenRegData = this.openTokenAgreementRequest.put(token, newTokenRegData);
        if ( oldTokenRegData != null ) {
            oldTokenRegData.abortRequest();
        }
        return newTokenRegData.brickTokenAgreementRequest();
    }

    public boolean aTokenAgreementWasSent(String token) {
        TokenRegistrationData tokenRegData = this.openTokenAgreementRequest.remove(token);
        if ( tokenRegData == null ) {
            LOG.info("token " + token + " is not waited for. Typo error of the user?"); // TODO: make brick comm more robust
            return false;
        } else {
            tokenRegData.clientAgreedOnTheBrickToken();
            BrickCommunicationData newSingleState = new BrickCommunicationData(token);
            BrickCommunicationData oldSingleState = this.allStates.put(token, newSingleState);
            if ( oldSingleState != null ) {
                LOG.info("a communication state for token " + token + " exists. THIS IS PROBABLY A PROBLEM"); // TODO: make brick comm more robust
                oldSingleState.abortBrickDownloadRequest();
            }
            LOG.info("token " + token + " is agreed upon.");
        }
        return true;
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
            LOG.info("a communication state for token " + token + " is created. THIS IS A TEMPORARY FIX"); // TODO: make brick comm more robust
            singleState = new BrickCommunicationData(token);
            this.allStates.put(token, singleState);
        }
        return singleState;
    }
}
