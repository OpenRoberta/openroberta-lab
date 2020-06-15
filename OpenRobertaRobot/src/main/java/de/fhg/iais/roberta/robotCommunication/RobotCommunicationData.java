package de.fhg.iais.roberta.robotCommunication;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Clock;

/**
 * This class is responsible for the synchronisation between the one browser client and one robot. The synchronisation is based on a agreed upon token. This
 * token is stored redundantly in objects of this class.<br>
 * As no push technology to the robot is available, the robot issues a push request command, the thread acting upon this request is frozen and waits until the
 * user has issued a command in the browser (in most cases a run) or a timer expires . This unfreezes the thread and the responses tells the robot what to do.
 * E.g. if a run command has been issued, the already generated jar is requested to be downloaded to the robot.<br>
 * <br>
 * TODO: This implementation is resource intensive as it freezes an expensive resource, namely a thread. It should be replaced later by async technology.
 *
 * @author rbudde
 */
public class RobotCommunicationData {
    private static final Logger LOG = LoggerFactory.getLogger(RobotCommunicationData.class);
    private static final int TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE = 300000;
    private static final int TIMEOUT_UNTIL_ASSUME_DISCONNECTED_IF_ROBOT_DOESNT_PUSH = 10000;
    private static final int PUSH_TIMEOUT_INTERVALL = 10000;

    private final String token;
    private final String robot;
    private final String robotIdentificator;
    private final String robotName;
    private final String menuversion;
    private final String runtimeversion;
    private final String firmwarename;
    private final String firmwareversion;
    private JSONObject sensorvalues;
    private int nepoExitValue;

    private Clock timerStartedByLastRequest;
    private Clock timerStartedByTokenApproval;
    private State state;

    private String battery;

    private String command;
    private String programName;

    public RobotCommunicationData(
        String token,
        String robot,
        String robotIdentificator,
        String robotName,
        String battery,
        String menuversion,
        String runtimeveriosn,
        String firmwarename,
        String firmwareversion) {
        // the per session token
        this.token = token;
        // the robot group, such as 'ev3' ...
        this.robot = robot;
        this.robotIdentificator = robotIdentificator;
        // the name of the robot, can be changed by the user in some cases
        this.robotName = robotName;
        this.battery = battery;
        this.menuversion = menuversion;
        this.runtimeversion = runtimeveriosn;
        // the robot variant, such as 'ev3dev' or 'lejos'
        this.firmwarename = firmwarename;
        // informal robot firmware version details
        this.firmwareversion = firmwareversion;
        this.sensorvalues = new JSONObject();

        this.timerStartedByLastRequest = Clock.start();
        this.state = State.WAIT_FOR_TOKENAPPROVAL_FROM_USER;
    }

    /**
     * BLOCKING a REQUEST from a ROBOT. Called from a thread, which is triggered by a ROBOT, that presents a token for approval and waits:<br>
     * - for a timeout. Then token is not expected<br>
     * - until the user approved the token or denied it
     *
     * @return true, if user approved the token; false otherwise
     */
    public synchronized boolean robotTokenAgreementRequest() {
        LOG.info("ROBOT_RCD: robot [" + this.robotIdentificator + "] sends token " + this.token + " and waits for the user to approve it");
        this.state = State.WAIT_FOR_TOKENAPPROVAL_FROM_USER;
        this.timerStartedByLastRequest = Clock.start();
        while ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER //
            && this.timerStartedByLastRequest.elapsedMsec() < TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE ) //
        {
            try {
                wait(TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE);
            } catch ( InterruptedException e ) { //NOSONAR : repeat the loop until timer elapses or another thread changed the state
                LOG
                    .info(
                        "ROBOT_RCD: spurious interrupt in robotTokenAgreementRequest. Restarting the wait for "
                            + TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE
                            + " msec");
            }
        }
        if ( this.state == State.WAIT_FOR_PUSH_CMD_FROM_ROBOT ) {
            LOG.info("ROBOT_RCD: robot [" + this.robotIdentificator + "] has sent token " + this.token + ". User agreed on that. SUCCESS");
            return true;
        } else if ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            LOG.info("ROBOT_RCD: robot [" + this.robotIdentificator + "] has sent token " + this.token + ". Timed out!");
            abort();
            return false;
        } else {
            LOG
                .info(
                    "ROBOT_RCD: robot ["
                        + this.robotIdentificator
                        + "] has sent token "
                        + this.token
                        + " "
                        + this.timerStartedByLastRequest.elapsedMsecFormatted()
                        + " ago, but we force the robot to be disconnected, because state "
                        + this.state
                        + " is unexpected");
            abort();
            return false;
        }
    }

    /**
     * NO WAITING: method called from a server thread. This method wakes up the thread serving the robot request for token approval
     */
    public synchronized void userApprovedTheRobotToken() {
        if ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            LOG.info("ROBOT_RCD: user approved the token. The approval request was scheduled " + this.timerStartedByLastRequest.elapsedSecFormatted() + " ago");
            this.state = State.WAIT_FOR_PUSH_CMD_FROM_ROBOT;
            this.timerStartedByLastRequest = Clock.start();
            this.timerStartedByTokenApproval = Clock.start();
            notifyAll();
        } else {
            LOG
                .error(
                    "ROBOT_RCD: user approval arrived, but is not expected. State is: "
                        + this.state
                        + ". Last request was scheduled "
                        + this.timerStartedByLastRequest.elapsedSecFormatted()
                        + " ago");
        }
    }

    /**
     * causes WAITING of a ROBOT. Called from a thread, which is triggered by a ROBOT /pushcmd command request. The thread waits:<br>
     * - for a timeout. Then the robot should repeat the command<br>
     * - for a RUN command from the user
     */
    public synchronized void robotHasSentAPushRequest() {
        if ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            LOG
                .info(
                    "ROBOT_RCD: robot has sent a push request, but the server is waiting for a token approval from the user. The request is ignored. Waiting started "
                        + this.timerStartedByLastRequest.elapsedSecFormatted()
                        + " ago. ");
        } else {
            if ( this.state != State.WAIT_FOR_PUSH_CMD_FROM_ROBOT && this.state != State.ROBOT_IS_BUSY ) {
                LOG
                    .info(
                        "ROBOT_RCD: robot has sent an unexpected push request. Programming error? The request is ACCEPTED. State is "
                            + this.state
                            + ". The last request was scheduled "
                            + this.timerStartedByLastRequest.elapsedSecFormatted()
                            + " ago. ");
            }
            this.state = State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER;
            this.timerStartedByLastRequest = Clock.start();
            while ( this.state == State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER ) {
                try {
                    wait(PUSH_TIMEOUT_INTERVALL);
                    if ( this.state == State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER ) {
                        this.state = State.WAIT_FOR_PUSH_CMD_FROM_ROBOT;
                        this.command = "repeat";
                    }
                    break;
                } catch ( InterruptedException e ) { //NOSONAR : repeat the loop until another thread changed the state
                    LOG.info("ROBOT_RCD: spurious interrupt in robotHasSentAPushRequest. Restarting the wait for " + PUSH_TIMEOUT_INTERVALL + " msec");
                }
            }
        }
    }

    /**
     * NO WAITING: this object is outdated. This method is called to abort an eventually pending request from a robot. The notifyAll is for that. This object
     * will be removed from the map holding all valid robot-server connection.
     */
    public synchronized void abort() {
        this.state = State.GARBAGE;
        notifyAll();
    }

    /**
     * NO WAITING: method called from a server thread. This method terminates immediately (if the robot waits for a push command) or after 1 sec (if we expect a
     * push command in the very near future. It wakes up the thread, which runs on behalf of a push command request from the robot.
     *
     * @return true, if the robot was waiting for a "run" command, false otherwise
     */
    public synchronized boolean runButtonPressed(String programName) {
        if ( !isRobotWaitingForPushCommandNowOrWithinTheNextSecond() ) {
            LOG.info("RUN button pressed, but robot is not waiting for that event. Bad luck!");
            return false;
        } else {
            LOG
                .info(
                    "RUN button pressed and robot is waiting for that event. Wait state entered "
                        + this.timerStartedByLastRequest.elapsedSecFormatted()
                        + " ago");
            this.command = "download";
            this.programName = programName;
            this.timerStartedByLastRequest = Clock.start();
            this.state = State.ROBOT_IS_BUSY;
            notifyAll();
            return true;
        }
    }

    /**
     * method called from a server thread. This method terminates immediately (if the robot waits for a push command) or after 1 sec (if we expect a push
     * command in the very near future. It wakes up the thread, which runs on behalf of a push command request from the robot.
     *
     * @return true, if the update is accepted; false otherwise
     */
    public synchronized boolean firmwareUpdate() {
        if ( !isRobotWaitingForPushCommandNowOrWithinTheNextSecond() ) {
            LOG.info("UPDATE button pressed, but the robot is not waiting. Bad luck!");
            return false;
        } else {
            LOG.info("UPDATE button pressed. Wait state entered " + this.timerStartedByLastRequest.elapsedSecFormatted() + " ago");
            this.command = "update";
            this.timerStartedByLastRequest = Clock.start();

            // the robot is disconnected after firmware update
            abort();
            return true;
        }
    }

    /**
     * is the robot waiting for a push command? In the case, that WE expect the push within the next second, WE will wait a second and check again
     *
     * @return true, if the robot is waiting for a push command
     */
    private boolean isRobotWaitingForPushCommandNowOrWithinTheNextSecond() {
        if ( this.state == State.WAIT_FOR_PUSH_CMD_FROM_ROBOT ) {
            try {
                wait(TIMEOUT_UNTIL_ASSUME_DISCONNECTED_IF_ROBOT_DOESNT_PUSH);
            } catch ( InterruptedException e ) { //NOSONAR : expect, that the robot is waiting for a server push
            }
        }
        return this.state == State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER;
    }

    /**
     * return true, if the robot is probably disconnected. But the robot may reconnect in the future (if USB cable is plugged in, for instance)
     *
     * @return true, if the robot didn't send a push command within a reasonable long interval after being requested to do so
     */
    public boolean isRobotProbablyDisconnected() {
        return this.state == State.WAIT_FOR_PUSH_CMD_FROM_ROBOT
            && getElapsedMsecOfStartOfLastRequest() > TIMEOUT_UNTIL_ASSUME_DISCONNECTED_IF_ROBOT_DOESNT_PUSH;
    }

    public String getToken() {
        return this.token;
    }

    public String getRobot() {
        return this.robot;
    }

    public String getRobotIdentificator() {
        return this.robotIdentificator;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public String getBattery() {
        return this.battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public void setSensorValues(JSONObject sensorvalues) {
        this.sensorvalues = sensorvalues;
    }

    public void setNepoExitValue(int nepoExitValue) {
        this.nepoExitValue = nepoExitValue;
    }

    public String getCommand() {
        return this.command;
    }

    public String getProgramName() {
        return this.programName;
    }

    public long getElapsedMsecOfStartOfLastRequest() {
        return this.timerStartedByLastRequest.elapsedMsec();
    }

    public long getElapsedMsecOfStartApproval() {
        if ( this.timerStartedByTokenApproval == null ) {
            return -1;
        } else {
            return this.timerStartedByTokenApproval.elapsedMsec();
        }
    }

    public String getMenuVersion() {
        return this.menuversion;
    }

    public State getState() {
        return this.state;
    }

    public void setStateBusy() {
        this.state = State.ROBOT_IS_BUSY;
    }

    public String getFirmwareName() {
        return this.firmwarename;
    }

    public String getFirmwareVersion() {
        return this.firmwareversion;
    }

    public String getRuntimeVersion() {
        return this.runtimeversion;
    }

    public JSONObject getSensorValues() {
        return this.sensorvalues;
    }

    public int getNepoExitValue() {
        return this.nepoExitValue;
    }

    /**
     * the states of communication between the robot and the browser client.
     */
    public enum State {
        WAIT_FOR_TOKENAPPROVAL_FROM_USER, WAIT_FOR_PUSH_CMD_FROM_ROBOT, ROBOT_WAITING_FOR_PUSH_FROM_SERVER, ROBOT_IS_BUSY, GARBAGE;
    }
}