package de.fhg.iais.roberta.persistence.util;

import java.io.Serializable;
import java.util.Map;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

public class HttpSessionState implements Serializable {
    private static final long serialVersionUID = 5423413372044585392L;

    public final static int NO_USER = -1;

    private int userId = HttpSessionState.NO_USER;
    private String robotName;
    private String token = "1Q2W3E4R";
    private String programName;
    private String program;
    private String configurationName;
    private String configuration;
    private String toolboxName;
    private String toolbox;
    private long sessionNumber;
    private Map<String, IRobotFactory> robotPluginMap;

    public HttpSessionState(
        RobotCommunicator robotCommunicator,
        Map<String, IRobotFactory> robotPluginMap,
        RobertaProperties robertaProperties,
        long sessionNumber) //
    {
        this.robotPluginMap = robotPluginMap;
        this.robotName = robertaProperties.getDefaultRobot();
        this.sessionNumber = sessionNumber;

    }

    public static HttpSessionState init(
        RobotCommunicator robotCommunicator,
        Map<String, IRobotFactory> robotPluginMap,
        RobertaProperties robertaProperties,
        long sessionNumber) //
    {
        return new HttpSessionState(robotCommunicator, robotPluginMap, robertaProperties, sessionNumber);
    }

    public int getUserId() {
        return this.userId;
    }

    public boolean isUserLoggedIn() {
        return this.userId >= 1;
    }

    public void setUserClearDataKeepTokenAndRobotId(int userId) {
        Assert.isTrue(userId >= 1 || userId == HttpSessionState.NO_USER);
        // token is not cleared. This would annoy the user.
        // robotId is not cleared. This would annoy the user.
        this.userId = userId;
        this.programName = null;
        this.program = null;
        this.configurationName = null;
        this.configuration = null;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public void setRobotName(String robotName) {
        Assert.notNull(robotName);
        this.robotName = robotName;
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

    public String getConfigurationName() {
        return this.configurationName;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public void setConfigurationNameAndConfiguration(String configurationName, String configuration) {
        this.configurationName = configurationName;
        this.configuration = configuration;
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

    public long getSessionNumber() {
        return this.sessionNumber;
    }
}