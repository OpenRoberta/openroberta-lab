package de.fhg.iais.roberta.robotCommunication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.robotCommunication.Ev3CommunicationData.State;
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

    /**
     * check the new registration ticket.
     * only used by brickWantsTokenToBeApproved(), extracted for testing
     *
     * @throws assertions for various types of issues
     * @return true if the ticket has been accepted
     */
    public boolean addNewRegistration(Ev3CommunicationData registration) {
        String token = registration.getToken();
        String robotIdentificator = registration.getRobotIdentificator();
        Assert.isTrue(token != null && robotIdentificator != null);
        Assert.isTrue(this.allStates.get(token) == null, "token already used. New token required.");
        for ( String storedToken : this.allStates.keySet() ) {
            Ev3CommunicationData storedState = this.allStates.get(storedToken);
            if ( robotIdentificator.equals(storedState.getRobotIdentificator())
                && !robotIdentificator.equals("usb")
                && !robotIdentificator.equals("unknown") ) {
                LOG.error("Token approval request for robot [" + robotIdentificator + "], but an old request is pending. Old request aborted.");
                storedState.abortPush(); // notifyAll() executed
                this.allStates.remove(storedToken);
            }
        }
        this.allStates.put(token, registration);
        return true;
    }

    public boolean brickWantsTokenToBeApproved(Ev3CommunicationData registration) {
        addNewRegistration(registration);
        return registration.brickTokenAgreementRequest(); // this will freeze the request until another issues a notifyAll()
    }

    /**
     * called by the robot to inform the server about the fact, that the robot is still connected and ready to get a command pushed to it
     *
     * @param token identifying the robot
     * @param batteryvoltage changes over time
     * @param nepoExitValue the return value of the last user program, that was executed. Is 0 if no exitvalue is provided by the robot system.
     * @return a legal command for the robot (in 99% a "repeat" :)
     */
    public String brickWaitsForAServerPush(String token, String batteryvoltage, int nepoExitValue) {
        Ev3CommunicationData state = getState(token);
        if ( state != null ) {
            state.setBattery(batteryvoltage);
            state.setNepoExitValue(nepoExitValue);
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

    public void disconnect(String token) {
        Ev3CommunicationData state = this.allStates.get(token);
        if ( state == null ) {
            LOG.info("token " + token + " is not waited for. Ok.");
        } else {
            state.abortPush(); // notifyAll() executed
            this.allStates.remove(token);
            LOG.info("Robot [" + state.getRobotIdentificator() + "] token " + token + " disconnected.");
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
