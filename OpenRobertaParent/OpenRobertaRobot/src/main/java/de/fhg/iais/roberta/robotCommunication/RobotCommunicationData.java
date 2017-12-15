package de.fhg.iais.roberta.robotCommunication;

import org.codehaus.jettison.json.JSONObject;
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
    private static final int WAIT_FOR_A_ROBOT_PUSH_COMMAND = 1000;
    private static final int TIMEOUT_UNTIL_ASSUME_DISCONNECTED_IF_ROBOT_DOESNT_PUSH = 10000;

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
     * method called from a thread, which is triggered by a robot request. This method blocks until the user has approved the robot token or a timeout occurs.
     *
     * @return true, if user approved the token; false otherwise
     */
    public synchronized boolean robotTokenAgreementRequest() {
        LOG.info("Robot [" + this.robotIdentificator + "] token " + this.token + " starts waiting for the client to approve the token");
        this.state = State.WAIT_FOR_TOKENAPPROVAL_FROM_USER;
        this.timerStartedByLastRequest = Clock.start();
        while ( this.timerStartedByLastRequest.elapsedMsec() < TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE
            && this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) //
        {
            try {
                wait(TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE);
            } catch ( InterruptedException e ) { //NOSONAR : repeat the loop until timer elapses or another thread changed the state
            }
        }
        if ( this.state == State.WAIT_FOR_PUSH_CMD_FROM_ROBOT ) {
            LOG.info("Robot [" + this.robotIdentificator + "] token " + this.token + " approval terminated SUCCESSFULLY.");
            return true;
        } else if ( this.state == State.GARBAGE ) {
            LOG.info(
                "Robot ["
                    + this.robotIdentificator
                    + "] token "
                    + this.token
                    + " was disconnected. The request is aborted. Time elapsed: "
                    + this.timerStartedByLastRequest.elapsedMsecFormatted());
            return false;
        } else {
            this.state = State.GARBAGE;
            LOG.info(
                "Robot ["
                    + this.robotIdentificator
                    + "] token "
                    + this.token
                    + " approval FAILED. The robot is disconnected. Time elapsed: "
                    + this.timerStartedByLastRequest.elapsedMsecFormatted());
            return false;
        }
    }

    /**
     * method called from a server thread. This method terminates immediately and wakes up the thread, which runs on behalf of a token approval request from the
     * robot.
     */
    public synchronized void userApprovedTheRobotToken() {
        if ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            LOG.info("user approved the token. The approval request was scheduled " + this.timerStartedByLastRequest.elapsedSecFormatted() + " ago");
            this.state = State.WAIT_FOR_PUSH_CMD_FROM_ROBOT;
            this.timerStartedByLastRequest = Clock.start();
            this.timerStartedByTokenApproval = Clock.start();
            notifyAll();
        } else {
            LOG.info(
                "user approval lost. Nobody is waiting. The approval request was scheduled " + this.timerStartedByLastRequest.elapsedSecFormatted() + " ago");
        }
    }

    /**
     * method called from a thread, which is triggered by a ROBOT push command request. This method blocks until either the server issues a push command or a
     * timer thread triggers a timeout.
     *
     * @return true, if user approved the token; false otherwise
     */
    public synchronized void robotHasSentAPushRequest() {
        if ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            LOG.error(
                "Robot has sent a push request, but the server waits for a token approval by an user. The request ist ignored. "
                    + "Waiting started "
                    + this.timerStartedByLastRequest.elapsedSecFormatted()
                    + " ago. ");
        } else {
            if ( this.state != State.WAIT_FOR_PUSH_CMD_FROM_ROBOT && this.state != State.ROBOT_IS_BUSY ) {
                LOG.error(
                    "Robot has sent a push request not awaited for. Programming error: Logic or Time race? The request is ACCEPTED. State is "
                        + this.state
                        + ". The state setting request was scheduled "
                        + this.timerStartedByLastRequest.elapsedSecFormatted()
                        + " ago. ");
            }
            this.state = State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER;
            this.timerStartedByLastRequest = Clock.start();
            while ( this.state == State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER ) {
                try {
                    wait();
                } catch ( InterruptedException e ) { //NOSONAR : repeat the loop until another thread changed the state
                }
            }
        }
        LOG.debug("ROBOT push request terminated.");
    }

    /**
     * this object is outdated. This method is called to abort an eventually pending request from a robot should be aborted. The notifyAll is for that. This
     * object will be removed from the map holding all valid robot-server connection. The state is set to GARBAGE to express that.
     */
    public synchronized void abortPush() {
        this.state = State.GARBAGE;
        notifyAll();
    }

    /**
     * method called from a timer thread. This method terminates immediately and wakes up a waiting thread, which runs on behalf of a push command from the
     * robot.
     */
    public synchronized void terminatePushAndRequestNextPush() {
        if ( this.state == State.ROBOT_WAITING_FOR_PUSH_FROM_SERVER ) {
            this.state = State.WAIT_FOR_PUSH_CMD_FROM_ROBOT;
            this.command = "repeat";
            this.timerStartedByLastRequest = Clock.start();
            notifyAll();
        }
    }

    /**
     * method called from a server thread. This method terminates immediately (if the robot waits for a push command) or after 1 sec (if we expect a push
     * command in the very near future. It wakes up the thread, which runs on behalf of a push command request from the robot.
     *
     * @return true, if the robot was waiting for a "run" command, false otherwise
     */
    public synchronized boolean runButtonPressed(String programName) {
        if ( !isRobotWaitingForPushCommand() ) {
            LOG.error("RUN button pressed, but robot is not waiting for that event. Bad luck!");
            return false;
        } else {
            LOG.info(
                "RUN button pressed and robot is waiting for that event. Wait state entered " + this.timerStartedByLastRequest.elapsedSecFormatted() + " ago");
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
     * @return the state of the robot
     */
    public synchronized boolean firmwareUpdate() {
        if ( !isRobotWaitingForPushCommand() ) {
            LOG.error("UPDATE button pressed, but the robot is not waiting. Bad luck!");
            return false;
        } else {
            LOG.debug("UPDATE button pressed. Wait state entered " + this.timerStartedByLastRequest.elapsedSecFormatted() + " ago");
            this.command = "update";
            this.timerStartedByLastRequest = Clock.start();

            // the robot is disconnected after firmware update
            abortPush();
            return true;
        }
    }

    private boolean isRobotWaitingForPushCommand() {
        if ( this.state == State.WAIT_FOR_PUSH_CMD_FROM_ROBOT ) {
            try {
                Thread.sleep(WAIT_FOR_A_ROBOT_PUSH_COMMAND);
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

    public long getRobotConnectionTime() {
        return this.timerStartedByTokenApproval.elapsedMsec();
    }

    public String getMenuVersion() {
        return this.menuversion;
    }

    public State getState() {
        return this.state;
    }

    public String getFirmwareName() {
        return this.firmwarename;
    }

    public String getFirmwareVersion() {
        return this.firmwareversion;
    }

    public String getRuntimeVersion() {
        return runtimeversion;
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