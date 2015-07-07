package de.fhg.iais.roberta.persistence.util;

import de.fhg.iais.roberta.util.dbc.Assert;

public class HttpSessionState {
    private int userId = -1;
    private int robotId = 42;
    private String token = "1Q2W3E4R";
    private String programName;
    private String program;
    private String configurationName;
    private String configuration;
    private String toolboxName;
    private String toolbox;

    public HttpSessionState() {
    }

    public static HttpSessionState init() {
        return new HttpSessionState();
    }

    public int getUserId() {
        return this.userId;
    }

    public boolean isUserLoggedIn() {
        return this.userId >= 1;
    }

    public void rememberLogin(int userId) {
        Assert.isTrue(userId >= 1);
        // token is not cleared. This would annoy the user.
        this.userId = userId;
        // robotId is not cleared.
        this.programName = null;
        this.program = null;
        this.configurationName = null;
        this.configuration = null;
    }

    public void rememberLogout() {
        this.userId = -1;
        // token is not cleared. This would annoy the user.
        this.programName = null;
        this.program = null;
        this.configurationName = null;
        this.configuration = null;
    }

    public int getRobotId() {
        return this.robotId;
    }

    public void setRobotId(int robotId) {
        Assert.notNull(robotId);
        this.robotId = robotId;
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
}