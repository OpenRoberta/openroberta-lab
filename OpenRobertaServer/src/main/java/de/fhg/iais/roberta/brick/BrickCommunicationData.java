package de.fhg.iais.roberta.brick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Clock;

/**
 * This class is responsible for the synchronisation between the one browser client and one brick. The synchronisation is based on a agreed upon token.
 * This token is stored redundantly in objects of this class.<br>
 * As no push technology to the brick is available, the brick issues a download request command, the thread acting upon this request is frozen and waits
 * until the user has issued a run command in the browser. This unfreezes the thread and the already generated jar is copied to the brick.<br>
 * <br>
 * TODO: This implementation is resource intensive as it freezes an expensive resource, namely a thread. It should be replaced later by async technology.
 *
 * @author rbudde
 */
public class BrickCommunicationData {
    private static final Logger LOG = LoggerFactory.getLogger(BrickCommunicationData.class);
    private static final int TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE = 300000;
    private static final int WAIT_FOR_A_BRICK_PUSH_COMMAND = 1000;

    private final String token;
    private final String robotIdentificator;
    private final String robotName;
    private final String menuversion;
    private final String lejosversion;

    private Clock clock;
    private State state;

    private String battery;

    private String command;
    private String programName;

    public BrickCommunicationData(String token, String robotIdentificator, String robotName, String battery, String menuversion, String lejosversion) {
        this.token = token;
        this.robotIdentificator = robotIdentificator;
        this.robotName = robotName;
        this.battery = battery;
        this.menuversion = menuversion;
        this.lejosversion = lejosversion;

        this.clock = Clock.start();
        this.state = State.WAIT_FOR_TOKENAPPROVAL_FROM_USER;
    }

    /**
     * method called from a thread, which is triggered by a BRICK request. This method blocks until the user has approved the brick token or a timeout occurs.
     *
     * @return true, if user approved the token; false otherwise
     */
    public synchronized boolean brickTokenAgreementRequest() {
        LOG.info("BRICK " + this.robotName + " [" + this.robotIdentificator + "] starts waiting for the client to approve a token");
        this.state = State.WAIT_FOR_TOKENAPPROVAL_FROM_USER;
        this.clock = Clock.start();
        while ( this.clock.elapsedMsec() < TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE && this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            try {
                wait(TIMEOUT_UNTIL_TOKEN_EXPIRES_WHEN_USER_DOESNT_APPROVE);
            } catch ( InterruptedException e ) {
                // try again
            }
        }
        boolean success = this.state == State.TOKEN_APPROVED;
        if ( success ) {
            this.state = State.WAIT_FOR_PUSH_CMD_FROM_BRICK;
            LOG.info("Robot request for token approval terminated SUCCESSFULLY. Time elapsed: " + this.clock.elapsedMsec());
        } else {
            this.state = State.GARBAGE;
            LOG.info("Robot request for token approval FAILED. Time elapsed: " + this.clock.elapsedMsec());
        }
        return success;
    }

    /**
     * method called from a server thread. This method terminates immediately and wakes up the thread, which runs on behalf of a token approval request
     * from the robot.
     */
    public synchronized void userApprovedTheBrickToken() {
        if ( this.state == State.WAIT_FOR_TOKENAPPROVAL_FROM_USER ) {
            LOG.info("user approved the token. The approval request was scheduled " + this.clock.elapsedSecFormatted() + " ago");
            this.state = State.TOKEN_APPROVED;
            this.clock = Clock.start();
            notifyAll();
        } else {
            LOG.info("user approval lost. Nobody is waiting. The approval request was scheduled " + this.clock.elapsedSecFormatted() + " ago");
        }
    }

    /**
     * method called from a thread, which is triggered by a BRICK push command request. This method blocks until either the server issues a push command or
     * a timer thread triggers a timeout.
     *
     * @return true, if user approved the token; false otherwise
     */
    public synchronized void brickHasSentAPushRequest() {
        if ( this.state != State.WAIT_FOR_PUSH_CMD_FROM_BRICK && this.state != State.BRICK_IS_BUSY ) {
            LOG.error("Brick has sent a push request not awaited for. Programming error: Logic or Time race? The request is ACCEPTED. State is "
                + this.state
                + ". The state setting request was scheduled "
                + this.clock.elapsedSecFormatted()
                + " ago. ");
        }
        this.state = State.BRICK_WAITING_FOR_PUSH_FROM_SERVER;
        this.clock = Clock.start();
        while ( this.state == State.BRICK_WAITING_FOR_PUSH_FROM_SERVER ) {
            try {
                wait();
            } catch ( InterruptedException e ) {
                // try again
            }
        }
        LOG.debug("BRICK push request terminated.");
    }

    /**
     * this object is outdated. This method is called to abort an eventually pending request from a robot should be aborted.
     * The notifyAll is for that. This object will be removed from
     * the map holding all valid robot-server connection. The state is set to GARBAGE to express that.
     */
    public synchronized void abortPush() {
        this.state = State.GARBAGE;
        notifyAll();
    }

    /**
     * method called from a timer thread. This method terminates immediately an wakes up a waiting thread, which runs on behalf of a push command from the
     * brick.
     */
    public synchronized void terminatePushAndRequestNextPush() {
        if ( this.state == State.BRICK_WAITING_FOR_PUSH_FROM_SERVER ) {
            this.state = State.WAIT_FOR_PUSH_CMD_FROM_BRICK;
            this.command = "repeat";
            this.clock = Clock.start();
            notifyAll();
        }
    }

    /**
     * method called from a server thread. This method terminates immediately (if the brick waits for a push command) or after 1 sec (if we expect a push
     * command in the very near future. It wakes up the thread, which runs on behalf of a push command request from the brick.
     *
     * @return true, if the robot was waiting for a "run" command, false otherwise
     */
    public synchronized boolean runButtonPressed(String programName) {
        if ( !isBrickWaitingForPushCommand() ) {
            LOG.error("RUN button pressed, but robot is not waiting for that event. Bad luck!");
            return false;
        } else {
            LOG.info("RUN button pressed and robot is waiting for that event. Wait state entered " + this.clock.elapsedSecFormatted() + " ago");
            this.command = "download";
            this.programName = programName;
            this.clock = Clock.start();
            this.state = State.BRICK_IS_BUSY;
            notifyAll();
            return true;
        }
    }

    /**
     * method called from a server thread. This method terminates immediately (if the brick waits for a push command) or after 1 sec (if we expect a push
     * command in the very near future. It wakes up the thread, which runs on behalf of a push command request from the brick.
     *
     * @return the state of the brick
     */
    public synchronized boolean firmwareUpdate() {
        if ( !isBrickWaitingForPushCommand() ) {
            LOG.error("UPDATE button pressed, but brick is not waiting. Bad luck!");
            return false;
        } else {
            LOG.debug("UPDATE button pressed. Wait state entered " + this.clock.elapsedSecFormatted() + " ago");
            this.command = "update";
            this.clock = Clock.start();
            this.state = State.BRICK_IS_BUSY;
            notifyAll();
            return true;
        }
    }

    private boolean isBrickWaitingForPushCommand() {
        if ( this.state == State.WAIT_FOR_PUSH_CMD_FROM_BRICK ) {
            try {
                Thread.sleep(WAIT_FOR_A_BRICK_PUSH_COMMAND);
            } catch ( InterruptedException e ) {
                // ok
            }
        }
        return this.state == State.BRICK_WAITING_FOR_PUSH_FROM_SERVER;
    }

    public String getToken() {
        return this.token;
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

    public String getCommand() {
        return this.command;
    }

    public String getProgramName() {
        return this.programName;
    }

    public long getElapsedMsecOfStartOfLastRequest() {
        return this.clock.elapsedMsec();
    }

    public State getState() {
        return this.state;
    }

    /**
     * the states of communication between the brick and the browser client.
     */
    public enum State {
        WAIT_FOR_TOKENAPPROVAL_FROM_USER, TOKEN_APPROVED, WAIT_FOR_PUSH_CMD_FROM_BRICK, BRICK_WAITING_FOR_PUSH_FROM_SERVER, BRICK_IS_BUSY, GARBAGE;
    }
}