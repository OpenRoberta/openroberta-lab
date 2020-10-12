package de.fhg.iais.roberta.persistence.util;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Objects of this class store the server-related state of the user's work with the openroberta-lab.
 */
public class HttpSessionState implements Serializable {
    private static final long serialVersionUID = 5423413372044585392L;
    private static final Logger LOG = LoggerFactory.getLogger(HttpSessionState.class);
    public final static int NO_USER = -1;
    public final static long EXPIRATION_TIME_SEC = 10800; // 3 hours = 3*60*60 = 10.800

    private static final AtomicLong SESSION_COUNTER = new AtomicLong();

    private static final Map<String, HttpSessionState> initToken2HttpSessionstate = new ConcurrentHashMap<>();

    /**
     * the REST-service in ClientInit sets the 'initToken'. It is returned with each response and the front end must supply it with each request. The token is
     * needed to detect when it is NOT SET and another function than init is called. It is likely, that then either the server was restarted or a user session
     * moved from one server to another server. In the latter case the session must be migrated to the new server. <i>This is not yet implemented. Actually we
     * abort the request.</i>
     */
    private final String initToken;
    private final long sessionNumber;
    private final Map<String, IRobotFactory> robotPluginMap;
    private final String defaultRobotName;
    private final String countryCode;

    private final long initTime;
    private long lastAccessTime;

    private int userId;
    private String robotName;
    private String token;
    private String programName;
    private String program;
    private String toolboxName;
    private String toolbox;
    private boolean processing;
    private String receivedNotificationsDigest;

    private HttpSessionState(
        String initToken,
        long sessionNumber,
        Map<String, IRobotFactory> robotPluginMap,
        ServerProperties serverProperties,
        String countryCode) //
    {
        this.initToken = initToken;
        this.sessionNumber = sessionNumber;
        this.robotPluginMap = robotPluginMap;
        this.defaultRobotName = serverProperties.getDefaultRobot();
        this.countryCode = countryCode;
        this.initTime = new Date().getTime();
        this.lastAccessTime = 0;

        this.userId = HttpSessionState.NO_USER;
        this.robotName = defaultRobotName;
        this.token = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
        this.programName = null;
        this.program = null;
        this.toolboxName = null;
        this.toolbox = null;
        this.setProcessing(false);

        // now remember the HttpSessionState object in a global map and use the 'initToken' as key
        HttpSessionState previousStateMustNotExist = initToken2HttpSessionstate.put(this.initToken, this);
        if ( previousStateMustNotExist != null && !"".equals(this.initToken) ) {
            LOG.error("FATAL! Key collision! HttpSessionState with session number " + sessionNumber + " and initToken '" + this.initToken + "' found");
        }
    }

    /**
     * factory method to create and initialize a {@linkplain HttpSessionState} object. This object stores the server-related state of the user's work with the
     * lab.
     *
     * @param robotPluginMap
     * @param serverProperties
     * @param sessionNumber
     * @param countryCode
     * @return
     */
    public static HttpSessionState init(Map<String, IRobotFactory> robotPluginMap, ServerProperties serverProperties, String countryCode) //
    {
        String initTokenString = RandomUrlPostfix.generate(12, 12, 3, 3, 3);
        long sessionNumber = SESSION_COUNTER.incrementAndGet();
        HttpSessionState httpSessionState = new HttpSessionState(initTokenString, sessionNumber, robotPluginMap, serverProperties, countryCode);
        LOG.info("session " + sessionNumber + " created");
        return httpSessionState;
    }

    /**
     * factory method to create and initialize a {@linkplain HttpSessionState} object for debugging. Calling this method will disable consistency checks as
     * multiple tabs detection, bypassing the /init REST-call.<br>
     * <b>It must not be used outside of tests</b>
     *
     * @param robotPluginMap
     * @param serverProperties
     * @param sessionNumber
     * @return
     */
    public static HttpSessionState initOnlyLegalForDebugging(
        String initTokenString,
        Map<String, IRobotFactory> robotPluginMap,
        ServerProperties serverProperties,
        long sessionNumber) //
    {
        HttpSessionState state = new HttpSessionState(initTokenString, sessionNumber, robotPluginMap, serverProperties, "..");
        return state;
    }

    public int getUserId() {
        return this.userId;
    }

    /**
     * remember the time of this call<br>
     * Usage: if a http request hits the server, it contains a value for key 'initToken', which is used to retrieve an object of this class. In this context
     * this method is called to remember the time of the http request. The value is used to invalidate this object, if no http request hits the server for a
     * long time.
     */
    private void rememberAccessTime() {
        this.lastAccessTime = new Date().getTime();
    }

    /**
     * return the number of seconds since the last access. The accessor usually is the frontend<br>
     * Usage: if a http request hits the serverthe time of the http request is remembered. This call return the number of seconds between now and the last
     * request. This value in turn is used to invalidate this object, if no http request hits the server for a long time.
     */
    public long getSecondsSinceLastAccess() {
        return (new Date().getTime() - this.lastAccessTime) / 1000;
    }

    public boolean isUserLoggedIn() {
        return this.userId >= 1;
    }

    public void setUserClearDataKeepTokenAndRobotId(int userId) {
        Assert.isTrue(userId >= 1 || userId == HttpSessionState.NO_USER);
        // token is not cleared & robotId is not cleared: this would annoy the user.
        this.userId = userId;
        this.programName = null;
        this.program = null;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public void setRobotName(String robotName) {
        Assert.notNull(robotName);
        this.robotName = robotName;
    }

    public String getInitToken() {
        Assert.notNull(initToken, "init token not initialized. This is a SEVERE error");
        return initToken;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        Assert.notNull(token);
        this.token = token;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgramNameAndProgramText(String programName, String program) {
        this.programName = programName;
        this.program = program;
    }

    public String getToolboxName() {
        return this.toolboxName;
    }

    public String getToolbox() {
        return this.toolbox;
    }

    public void setToolboxNameAndToolbox(String toolboxName, String toolbox) {
        this.toolboxName = toolboxName;
        this.toolbox = toolbox;
    }

    public IRobotFactory getRobotFactory() {
        return this.robotPluginMap.get(this.robotName);
    }

    public IRobotFactory getRobotFactory(String robotName) {
        return this.robotPluginMap.get(robotName);
    }

    /**
     * Returns a list of robot factories associated with the group. In case the robot has no group a one element list the given robot is returned and in case an
     * invalid robot is used an empty list is returned.
     *
     * @param group the robot group
     * @return the list of group member factories
     */
    public List<IRobotFactory> getRobotFactoriesOfGroup(String group) {
        List<IRobotFactory> groupMembers = robotPluginMap.values().stream().filter(factory -> {
            String propertyGroup = factory.getPluginProperties().getStringProperty("robot.plugin.group");
            return (propertyGroup != null) && propertyGroup.equals(group);
        }).collect(Collectors.toList());
        if ( groupMembers.isEmpty() ) {
            if ( !robotPluginMap.containsKey(group) ) {
                LOG.warn("No robot plugin associated with this group or robot name.");
                return Collections.emptyList();
            } else {
                return Collections.singletonList(getRobotFactory(group));
            }
        } else {
            return groupMembers;
        }
    }

    public long getSessionNumber() {
        return this.sessionNumber;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public static HttpSessionState get(String initToken) {
        HttpSessionState httpSessionState = initToken2HttpSessionstate.get(initToken);
        if ( httpSessionState != null ) {
            httpSessionState.rememberAccessTime();
        }
        return initToken2HttpSessionstate.get(initToken);
    }

    public static int getNumberOfHttpSessionStates() {
        return initToken2HttpSessionstate.size();
    }

    public static void removeExpired() {
        boolean somethingExpired = false;
        for ( Entry<String, HttpSessionState> entry : initToken2HttpSessionstate.entrySet() ) {
            if ( entry.getValue().getSecondsSinceLastAccess() > EXPIRATION_TIME_SEC ) {
                String initToken = entry.getKey();
                Statistics.info("SessionExpire", "initToken", initToken, "success", true);
                LOG.info("session with token " + initToken + " expired");
                initToken2HttpSessionstate.remove(initToken);
                somethingExpired = true;
            }
        }
        if ( !somethingExpired ) {
            LOG.info("no sessions expired");
        }
    }

    public String getReceivedNotificationsDigest() {
        return receivedNotificationsDigest;
    }

    public void setReceivedNotificationsDigest(String receivedNotificationsDigest) {
        this.receivedNotificationsDigest = receivedNotificationsDigest;
    }
}