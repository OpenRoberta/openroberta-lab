package de.fhg.iais.roberta.robotCommunication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData.State;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * class, that synchronizes the communication between the bricks and the web-app. Thread-safe. See class {@link RobotCommunicationData} for further
 * explanations.<br>
 * <br>
 * The class <b>must</b> be used as a singleton. Use <b>GUICE</b> to enforce that.
 *
 * @author rbudde
 */
public class RobotCommunicator {
    private static final Logger LOG = LoggerFactory.getLogger(RobotCommunicator.class);

    private final Map<String, RobotCommunicationData> allStates = new ConcurrentHashMap<>();
    private String subtype = ""; // The robot subtype, currently used for Arduino type differentiation

    public RobotCommunicator() {
    }

    /**
     * check the new registration ticket. only used by brickWantsTokenToBeApproved(), extracted for testing
     *
     * @throws assertions for various types of issues
     * @return true if the ticket has been accepted
     */
    public boolean addNewRegistration(RobotCommunicationData newRobotCommunicationData) {
        String token = newRobotCommunicationData.getToken();
        String newIdentificator = newRobotCommunicationData.getRobotIdentificator();
        Assert.isTrue(token != null && newIdentificator != null);
        RobotCommunicationData existingRobotCommunicationData = this.allStates.get(token);
        if ( existingRobotCommunicationData != null ) {
            String existingIdentificator = existingRobotCommunicationData.getRobotIdentificator();
            if ( existingIdentificator == null
                || !existingIdentificator.equals(newIdentificator)
                || existingIdentificator.equals("usb")
                || existingIdentificator.equals("unknown") ) {
                LOG.info("ROBOT_RC: token already used. New token required");
                return false;
            }
        }
        for ( String storedToken : this.allStates.keySet() ) {
            RobotCommunicationData storedState = this.allStates.get(storedToken);
            if ( newIdentificator.equals(storedState.getRobotIdentificator()) && !newIdentificator.equals("usb") && !newIdentificator.equals("unknown") ) {
                LOG.info("ROBOT_RC: token approval request for robot [" + newIdentificator + "], but an old request is pending. Start abort old request");
                this.allStates.remove(storedToken);
                storedState.abort(); // notifyAll() executed
                LOG.info("ROBOT_RC: token approval request for robot [" + newIdentificator + "], but an old request is pending. End abort old request.");
            }
        }
        this.allStates.put(token, newRobotCommunicationData);
        return true;
    }

    public boolean brickWantsTokenToBeApproved(RobotCommunicationData newRobotCommunicationData) {
        if ( addNewRegistration(newRobotCommunicationData) ) {
            return newRobotCommunicationData.robotTokenAgreementRequest(); // this will freeze the request until another thread issues a notifyAll()
        } else {
            return false;
        }
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
        RobotCommunicationData state = getState(token);
        if ( state != null ) {
            state.setBattery(batteryvoltage);
            state.setNepoExitValue(nepoExitValue);
            state.robotHasSentAPushRequest();
            return state.getCommand();
        } else {
            LOG.error("ROBOT_RC: /pushcmd from a robot arrived, no matching state was found - we return a server error");
            return null;
        }
    }

    public Key aTokenAgreementWasSent(String token, String robot) {
        RobotCommunicationData state = this.allStates.get(token);

        if ( state == null ) {
            LOG.info("ROBOT_RC: token " + token + " is not waiting for. Typing error of the user?");
            return Key.TOKEN_SET_ERROR_NO_ROBOT_WAITING;
        } else if ( !checkRobotMatchesClient(robot, state) ) {
            LOG
                .info(
                    "ROBOT_RC: token "
                        + token
                        + " belongs to a robot of type "
                        + state.getRobot()
                        + "/"
                        + state.getFirmwareName()
                        + ", but client is "
                        + robot);
            return Key.TOKEN_SET_ERROR_WRONG_ROBOTTYPE;
        } else {
            state.userApprovedTheRobotToken();
            LOG.info("ROBOT_RC: token " + token + " is approved by a user.");
            return Key.TOKEN_SET_SUCCESS;
        }
    }

    public void disconnect(String token) {
        if ( token == null ) {
            return; // was not connected
        } else {
            RobotCommunicationData state = this.allStates.get(token);
            if ( state == null ) {
                LOG.info("ROBOT_RC: token " + token + " is not waited for. Ok.");
            } else {
                LOG.info("ROBOT_RC: Robot [" + state.getRobotIdentificator() + "] with token " + token + " start disconnect");
                this.allStates.remove(token);
                state.abort(); // notifyAll() executed
                LOG.info("ROBOT_RC: Robot [" + state.getRobotIdentificator() + "] with token " + token + " end disconnect");
            }
        }
    }

    private boolean theRunButtonWasPressed(String token, String programName) {
        RobotCommunicationData state = getState(token);
        return state.runButtonPressed(programName);
    }

    public boolean firmwareUpdateRequested(String token) {
        RobotCommunicationData state = getState(token);
        return state.firmwareUpdate();
    }

    public RobotCommunicationData getState(String token) {
        return this.allStates.get(token);
    }

    public String getSubtype() {
        return this.subtype;
    }

    public int getRobotCommunicationDataSize() {
        return allStates.size();
    }

    /**
     * return an overview about the states of the robots connected to the lab<br>
     * <b>This method must NEVER throw an exception</b>
     */
    public String getSummaryOfRobotCommunicator() {
        try {
            Map<State, Integer> rcdMap = new HashMap<>();
            for ( State s : State.values() ) {
                rcdMap.put(s, 0);
            }
            long robotLongestWaitingTime = 0;
            for ( RobotCommunicationData rcd : allStates.values() ) {
                final State state = rcd.getState();
                robotLongestWaitingTime = Math.max(robotLongestWaitingTime, rcd.getElapsedMsecOfStartOfLastRequest());
                int old = rcdMap.getOrDefault(state, 0);
                rcdMap.put(state, old + 1);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("number of robots connected: ").append(allStates.size()).append("\nmax time a robot is waiting: ");
            sb.append(robotLongestWaitingTime).append("\nstate distribution:\n");
            for ( Entry<State, Integer> entry : rcdMap.entrySet() ) {
                sb.append(String.format("%-40s : %d\n", entry.getKey(), entry.getValue()));
            }
            return sb.toString();
        } catch ( Exception e ) {
            String summary = "unexpected, dangerous exception";
            LOG.error(summary, e);
            return summary;
        }
    }

    /**
     * return details of the robots connections<br>
     * <b>This method must NEVER throw an exception</b>
     */
    public String getDetailsOfRobotConnections() {
        try {
            List<String> connectionDetails = new ArrayList<>();
            for ( RobotCommunicationData rcd : allStates.values() ) {
                String token = sanitize(rcd.getToken());
                long waitTime = sanitize(rcd.getElapsedMsecOfStartOfLastRequest());
                long approvalTime = sanitize(rcd.getElapsedMsecOfStartApproval());
                String ident = sanitize(rcd.getRobotIdentificator());
                String robotName = sanitize(rcd.getRobotName());
                String connectionDetail =
                    String.format("tk:%-10s st:%-40s wt:%6d at:%6d mc:%-20s nm: %-40s", token, rcd.getState(), waitTime, approvalTime, ident, robotName);
                connectionDetails.add(connectionDetail);
            }
            return connectionDetails.stream().collect(Collectors.joining("\n"));
        } catch ( Exception e ) {
            String error = "unexpected, dangerous exception";
            LOG.error(error, e);
            return error;
        }

    }

    // TODO: when can this fail?
    private boolean checkRobotMatchesClient(String robot, RobotCommunicationData state) {
        //TODO: it is a hot fix for the release on 6.7.17, later we need to change the state robot name from ardu to botnroll
        if ( robot.equals("botnroll") || robot.equals("arduino") ) {
            robot = "ardu";
        }

        //TODO: this re-writes for old robots, that do not know, that there are V1 and V0 of lejos. 20.02.2019, Artem Vinokurov.
        if ( robot.equals("ev3lejosv0") ) {
            robot = "lejos";
        }

        LOG.info("client:" + robot + ", robot: " + state.getRobot() + ", firmware: " + state.getFirmwareName());

        if ( state.getRobot().equals(robot) ) {
            return true;
        }
        if ( state.getFirmwareName().equals(robot) ) {
            return true;
        }
        // TODO: workarounds for sloppy protocol definition, check for which robots they trigger and fix
        if ( (state.getRobot() + state.getFirmwareName()).equals(robot) ) {
            LOG.warn("checking robot+firmware");
            return true;
        }
        return false;
    }

    private long sanitize(long val) {
        return val;
    }

    private String sanitize(String val) {
        return val == null ? "???" : val;
    }

    public Key run(String token, String robotName, String programName) {
        if ( this.getState(token) != null ) {
            this.subtype = robotName;
            boolean wasRobotWaiting = this.theRunButtonWasPressed(token, programName);
            if ( wasRobotWaiting ) {
                return Key.ROBOT_PUSH_RUN;
            } else {
                return Key.ROBOT_NOT_WAITING;
            }
        }
        return Key.ROBOT_NOT_CONNECTED;
    }
}
