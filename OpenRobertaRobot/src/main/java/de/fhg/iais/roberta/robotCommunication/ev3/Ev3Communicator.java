package de.fhg.iais.roberta.robotCommunication.ev3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CommunicationData.State;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * class, that synchronizes the communication between the bricks and the web-app. Thread-safe. See class {@link Ev3CommunicationData} for
 * further explanations.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author rbudde
 */
public class Ev3Communicator {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3Communicator.class);
    private static final long PUSH_TIMER_INTERVALL = 2000;
    private static final long PUSH_TIMEOUT_INTERVALL = 10000;

    private final Map<String, Ev3CommunicationData> allStates = new ConcurrentHashMap<>();

    public Ev3Communicator() {
        Runnable pushTimerThread = new Runnable() {
            @Override
            public void run() {
                pushTimerRunner();
            }
        };
        new Thread(null, pushTimerThread, "PushTimer").start();
        LOG.info("timer thread created");
    }

    public boolean brickWantsTokenToBeApproved(Ev3CommunicationData registration) {
        String token = registration.getToken();
        String robotIdentificator = registration.getRobotIdentificator();
        Assert.isTrue(token != null && robotIdentificator != null);
        Assert.isTrue(this.allStates.get(token) == null, "token already used. New token required.");
        for ( String storedToken : this.allStates.keySet() ) {
            Ev3CommunicationData storedState = this.allStates.get(storedToken);
            if ( robotIdentificator.equals(storedState.getRobotIdentificator()) && !robotIdentificator.equals("usb") ) {
                LOG.error("Token approval request for robotId " + robotIdentificator + ", but an old request is pending. Old request aborted.");
                storedState.abortPush(); // notifyAll() executed
                this.allStates.remove(storedToken);
            }
        }
        this.allStates.put(token, registration);
        return registration.brickTokenAgreementRequest(); // this will freeze the request until another issues a notifyAll()
    }

    /**
     * called by the robot to inform the server about the fact, that the robot is still connected and ready to get a command pushed to it
     * 
     * @param token identifying the robot
     * @param batteryvoltage the only information from the robot, which changes over time
     * @return a legal command for the robot (in 99% a "repeat" :)
     */
    public String brickWaitsForAServerPush(String token, String batteryvoltage) {
        Ev3CommunicationData state = getState(token);
        if ( state != null ) {
            state.setBattery(batteryvoltage);
            state.brickHasSentAPushRequest();
            return state.getCommand();
        } else {
            LOG.error("a push request from a brick arrived, but no matching state was found in the server - we provoke a server error");
            return null;
        }
    }

    public boolean aTokenAgreementWasSent(String token) {
        Ev3CommunicationData state = this.allStates.get(token);
        if ( state == null ) {
            LOG.info("token " + token + " is not waited for. Typing error of the user?");
            return false;
        } else {
            // todo: version check!
            state.userApprovedTheBrickToken();
            LOG.info("token " + token + " is approved by a user.");
            return true;
        }
    }

    public boolean theRunButtonWasPressed(String token, String programName) {
        Ev3CommunicationData state = getState(token);
        return state.runButtonPressed(programName);
    }

    public boolean firmwareUpdateRequested(String token) {
        Ev3CommunicationData state = getState(token);
        return state.firmwareUpdate();
    }

    public Ev3CommunicationData getState(String token) {
        Ev3CommunicationData state = this.allStates.get(token);
        //        if ( state == null ) {
        //            LOG.error("a communication state for token " + token + " is created. THIS IS A TEMPORARY FIX"); // TODO: make brick comm more robust
        //            state = new BrickCommunicationData(token, "robot-id-generated-" + new Date().getTime(), "robot-name-generated", null);
        //            this.allStates.put(token, state);
        //        }
        return state;
    }

    private void pushTimerRunner() {
        while ( true ) {
            try {
                Thread.sleep(PUSH_TIMER_INTERVALL);
            } catch ( InterruptedException e ) {
                // OK
            }
            for ( Ev3CommunicationData state : this.allStates.values() ) {
                if ( state.getState() == State.BRICK_WAITING_FOR_PUSH_FROM_SERVER && state.getElapsedMsecOfStartOfLastRequest() > PUSH_TIMEOUT_INTERVALL ) {
                    state.terminatePushAndRequestNextPush();
                }
            }
        }
    }

}
