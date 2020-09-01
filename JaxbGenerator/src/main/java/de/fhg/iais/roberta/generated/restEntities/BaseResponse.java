/*
 * This is a class GENERATED by the TransportGenerator maven plugin. DON'T MODIFY IT.
 * IF you modify it, your work may be lost: the class will be overwritten automatically
 * when the maven plugin is re-executed for any reasons.
 */
package de.fhg.iais.roberta.generated.restEntities;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * basic response with too many properties :-<, also used to return errors<br><br>
 * Version: 1<br>
 * Datum: 2020-06-15
 */
public class BaseResponse {
    protected boolean immutable = false;
    protected String cmd;
    protected String rc;
    protected String message;
    protected String cause;
    protected JSONObject parameters;
    protected String initToken;
    protected long serverTime;
    protected boolean serverTimeDefined = false;
    protected String serverVersion;
    protected long robotWait;
    protected boolean robotWaitDefined = false;
    protected String robotBattery;
    protected String robotName;
    protected String robotVersion;
    protected String robotFirmwareName;
    protected JSONObject robotSensorvalues;
    protected int robotNepoexitvalue;
    protected boolean robotNepoexitvalueDefined = false;
    protected String robotState;
    
    /**
     * basic response with too many properties :-<, also used to return errors
     */
    public static BaseResponse make() {
        return new BaseResponse();
    }
    
    /**
     * basic response with too many properties :-<, also used to return errors
     */
    public static BaseResponse makeFromString(String jsonS) {
        try {
            JSONObject jsonO = new JSONObject(jsonS);
            return make(jsonO);
        } catch (JSONException e) {
            throw new RuntimeException("JSON parse error when parsing: " + jsonS, e);
        }
    }
    
    /**
     * basic response with too many properties :-<, also used to return errors
     */
    public static BaseResponse makeFromProperties(String cmd,String rc,String message,String cause,JSONObject parameters,String initToken,long serverTime,String serverVersion,long robotWait,String robotBattery,String robotName,String robotVersion,String robotFirmwareName,JSONObject robotSensorvalues,int robotNepoexitvalue,String robotState) {
        BaseResponse entity = new BaseResponse();
        entity.setCmd(cmd);
        entity.setRc(rc);
        entity.setMessage(message);
        entity.setCause(cause);
        entity.setParameters(parameters);
        entity.setInitToken(initToken);
        entity.setServerTime(serverTime);
        entity.setServerVersion(serverVersion);
        entity.setRobotWait(robotWait);
        entity.setRobotBattery(robotBattery);
        entity.setRobotName(robotName);
        entity.setRobotVersion(robotVersion);
        entity.setRobotFirmwareName(robotFirmwareName);
        entity.setRobotSensorvalues(robotSensorvalues);
        entity.setRobotNepoexitvalue(robotNepoexitvalue);
        entity.setRobotState(robotState);
        entity.immutable();
        return entity;
    }
    
    /**
     * basic response with too many properties :-<, also used to return errors
     */
    public static BaseResponse make(JSONObject jsonO) {
        return make().merge(jsonO).immutable();
    }
    
    /**
     * merge the properties of a JSON-object into this bean. The bean must be "under construction".
     * The keys of the JSON-Object must be valid. The bean remains "under construction".<br>
     * Throws a runtime exception if inconsistencies are detected.
     */
    public BaseResponse merge(JSONObject jsonO) {
        try {
            for (String key : JSONObject.getNames(jsonO)) {
                if ("_version".equals(key)) {
                } else if ("cmd".equals(key)) {
                    setCmd(jsonO.optString(key));
                } else if ("rc".equals(key)) {
                    setRc(jsonO.getString(key));
                } else if ("message".equals(key)) {
                    setMessage(jsonO.optString(key));
                } else if ("cause".equals(key)) {
                    setCause(jsonO.optString(key));
                } else if ("parameters".equals(key)) {
                    setParameters(jsonO.optJSONObject(key));
                } else if ("initToken".equals(key)) {
                    setInitToken(jsonO.getString(key));
                } else if ("server.time".equals(key)) {
                    setServerTime(jsonO.getLong(key));
                } else if ("server.version".equals(key)) {
                    setServerVersion(jsonO.getString(key));
                } else if ("robot.wait".equals(key)) {
                    setRobotWait(jsonO.optLong(key));
                } else if ("robot.battery".equals(key)) {
                    setRobotBattery(jsonO.optString(key));
                } else if ("robot.name".equals(key)) {
                    setRobotName(jsonO.optString(key));
                } else if ("robot.version".equals(key)) {
                    setRobotVersion(jsonO.optString(key));
                } else if ("robot.firmwareName".equals(key)) {
                    setRobotFirmwareName(jsonO.optString(key));
                } else if ("robot.sensorvalues".equals(key)) {
                    setRobotSensorvalues(jsonO.optJSONObject(key));
                } else if ("robot.nepoexitvalue".equals(key)) {
                    setRobotNepoexitvalue(jsonO.optInt(key));
                } else if ("robot.state".equals(key)) {
                    setRobotState(jsonO.optString(key));
                } else {
                    throw new RuntimeException("JSON parse error. Found invalid key: " + key + " in " + jsonO);
                }
            }
            return this;
        } catch (Exception e) {
            throw new RuntimeException("JSON parse / casting error when parsing: " + jsonO, e);
        }
    }
    
    /**
     * moves a bean from state "under construction" to state "immutable".<br>
     * Checks whether all required fields are set. All lists are made immutable.<br>
     * Throws a runtime exception if inconsistencies are detected.
     */
    public BaseResponse immutable() {
        if (this.immutable) {
            return this;
        }
        this.immutable = true;
        return validate();
    }
    
    /**
     * Checks whether all required fields are set.<br>
     * Throws a runtime exception if inconsistencies are detected.
     */
    private BaseResponse validate() {
        String _message = null;
        if ( !this.immutable ) {
            _message = "BaseResponse-object is already immutable: " + toString();
        }
        if ( rc == null) {
            _message = "required property rc of BaseResponse-object is not set: " + toString();
        }
        if ( initToken == null) {
            _message = "required property initToken of BaseResponse-object is not set: " + toString();
        }
        if ( !serverTimeDefined) {
            _message = "required property serverTime of BaseResponse-object is not set: " + toString();
        }
        if ( serverVersion == null) {
            _message = "required property serverVersion of BaseResponse-object is not set: " + toString();
        }
        if ( _message != null ) {
            this.immutable = false;
            throw new RuntimeException(_message);
        }
        return this;
    }
    
    /**
     * GET cmd. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getCmd() {
        if (!this.immutable) {
            throw new RuntimeException("no cmd from an object under construction: " + toString());
        }
        return this.cmd;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean cmdDefined() {
        return this.cmd != null;
    }
    
    /**
     * SET cmd. Object must be mutable.
     */
    public BaseResponse setCmd(String cmd) {
        if (this.immutable) {
            throw new RuntimeException("cmd assigned to an immutable object: " + toString());
        }
        this.cmd = cmd;
        return this;
    }
    
    /**
     * GET rc. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getRc() {
        if (!this.immutable) {
            throw new RuntimeException("no rc from an object under construction: " + toString());
        }
        return this.rc;
    }
    
    /**
     * SET rc. Object must be mutable.
     */
    public BaseResponse setRc(String rc) {
        if (this.immutable) {
            throw new RuntimeException("rc assigned to an immutable object: " + toString());
        }
        this.rc = rc;
        return this;
    }
    
    /**
     * GET message. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getMessage() {
        if (!this.immutable) {
            throw new RuntimeException("no message from an object under construction: " + toString());
        }
        return this.message;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean messageDefined() {
        return this.message != null;
    }
    
    /**
     * SET message. Object must be mutable.
     */
    public BaseResponse setMessage(String message) {
        if (this.immutable) {
            throw new RuntimeException("message assigned to an immutable object: " + toString());
        }
        this.message = message;
        return this;
    }
    
    /**
     * GET cause. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getCause() {
        if (!this.immutable) {
            throw new RuntimeException("no cause from an object under construction: " + toString());
        }
        return this.cause;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean causeDefined() {
        return this.cause != null;
    }
    
    /**
     * SET cause. Object must be mutable.
     */
    public BaseResponse setCause(String cause) {
        if (this.immutable) {
            throw new RuntimeException("cause assigned to an immutable object: " + toString());
        }
        this.cause = cause;
        return this;
    }
    
    /**
     * GET parameters. Object must be immutable. Never return null or an undefined/default value.
     */
    public JSONObject getParameters() {
        if (!this.immutable) {
            throw new RuntimeException("no parameters from an object under construction: " + toString());
        }
        return this.parameters;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean parametersDefined() {
        return this.parameters != null;
    }
    
    /**
     * SET parameters. Object must be mutable.
     */
    public BaseResponse setParameters(JSONObject parameters) {
        if (this.immutable) {
            throw new RuntimeException("parameters assigned to an immutable object: " + toString());
        }
        this.parameters = parameters;
        return this;
    }
    
    /**
     * GET initToken. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getInitToken() {
        if (!this.immutable) {
            throw new RuntimeException("no initToken from an object under construction: " + toString());
        }
        return this.initToken;
    }
    
    /**
     * SET initToken. Object must be mutable.
     */
    public BaseResponse setInitToken(String initToken) {
        if (this.immutable) {
            throw new RuntimeException("initToken assigned to an immutable object: " + toString());
        }
        this.initToken = initToken;
        return this;
    }
    
    /**
     * GET serverTime. Object must be immutable. Never return null or an undefined/default value.
     */
    public long getServerTime() {
        if (!this.immutable) {
            throw new RuntimeException("no serverTime from an object under construction: " + toString());
        }
        return this.serverTime;
    }
    
    /**
     * SET serverTime. Object must be mutable.
     */
    public BaseResponse setServerTime(long serverTime) {
        if (this.immutable) {
            throw new RuntimeException("serverTime assigned to an immutable object: " + toString());
        }
        this.serverTime = serverTime;
        this.serverTimeDefined = true;
        return this;
    }
    
    /**
     * GET serverVersion. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getServerVersion() {
        if (!this.immutable) {
            throw new RuntimeException("no serverVersion from an object under construction: " + toString());
        }
        return this.serverVersion;
    }
    
    /**
     * SET serverVersion. Object must be mutable.
     */
    public BaseResponse setServerVersion(String serverVersion) {
        if (this.immutable) {
            throw new RuntimeException("serverVersion assigned to an immutable object: " + toString());
        }
        this.serverVersion = serverVersion;
        return this;
    }
    
    /**
     * GET robotWait. Object must be immutable. Never return null or an undefined/default value.
     */
    public long getRobotWait() {
        if (!this.immutable) {
            throw new RuntimeException("no robotWait from an object under construction: " + toString());
        }
        return this.robotWait;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotWaitDefined() {
        return this.robotWaitDefined;
    }
    
    /**
     * SET robotWait. Object must be mutable.
     */
    public BaseResponse setRobotWait(long robotWait) {
        if (this.immutable) {
            throw new RuntimeException("robotWait assigned to an immutable object: " + toString());
        }
        this.robotWait = robotWait;
        this.robotWaitDefined = true;
        return this;
    }
    
    /**
     * GET robotBattery. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getRobotBattery() {
        if (!this.immutable) {
            throw new RuntimeException("no robotBattery from an object under construction: " + toString());
        }
        return this.robotBattery;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotBatteryDefined() {
        return this.robotBattery != null;
    }
    
    /**
     * SET robotBattery. Object must be mutable.
     */
    public BaseResponse setRobotBattery(String robotBattery) {
        if (this.immutable) {
            throw new RuntimeException("robotBattery assigned to an immutable object: " + toString());
        }
        this.robotBattery = robotBattery;
        return this;
    }
    
    /**
     * GET robotName. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getRobotName() {
        if (!this.immutable) {
            throw new RuntimeException("no robotName from an object under construction: " + toString());
        }
        return this.robotName;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotNameDefined() {
        return this.robotName != null;
    }
    
    /**
     * SET robotName. Object must be mutable.
     */
    public BaseResponse setRobotName(String robotName) {
        if (this.immutable) {
            throw new RuntimeException("robotName assigned to an immutable object: " + toString());
        }
        this.robotName = robotName;
        return this;
    }
    
    /**
     * GET robotVersion. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getRobotVersion() {
        if (!this.immutable) {
            throw new RuntimeException("no robotVersion from an object under construction: " + toString());
        }
        return this.robotVersion;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotVersionDefined() {
        return this.robotVersion != null;
    }
    
    /**
     * SET robotVersion. Object must be mutable.
     */
    public BaseResponse setRobotVersion(String robotVersion) {
        if (this.immutable) {
            throw new RuntimeException("robotVersion assigned to an immutable object: " + toString());
        }
        this.robotVersion = robotVersion;
        return this;
    }
    
    /**
     * GET robotFirmwareName. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getRobotFirmwareName() {
        if (!this.immutable) {
            throw new RuntimeException("no robotFirmwareName from an object under construction: " + toString());
        }
        return this.robotFirmwareName;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotFirmwareNameDefined() {
        return this.robotFirmwareName != null;
    }
    
    /**
     * SET robotFirmwareName. Object must be mutable.
     */
    public BaseResponse setRobotFirmwareName(String robotFirmwareName) {
        if (this.immutable) {
            throw new RuntimeException("robotFirmwareName assigned to an immutable object: " + toString());
        }
        this.robotFirmwareName = robotFirmwareName;
        return this;
    }
    
    /**
     * GET robotSensorvalues. Object must be immutable. Never return null or an undefined/default value.
     */
    public JSONObject getRobotSensorvalues() {
        if (!this.immutable) {
            throw new RuntimeException("no robotSensorvalues from an object under construction: " + toString());
        }
        return this.robotSensorvalues;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotSensorvaluesDefined() {
        return this.robotSensorvalues != null;
    }
    
    /**
     * SET robotSensorvalues. Object must be mutable.
     */
    public BaseResponse setRobotSensorvalues(JSONObject robotSensorvalues) {
        if (this.immutable) {
            throw new RuntimeException("robotSensorvalues assigned to an immutable object: " + toString());
        }
        this.robotSensorvalues = robotSensorvalues;
        return this;
    }
    
    /**
     * GET robotNepoexitvalue. Object must be immutable. Never return null or an undefined/default value.
     */
    public int getRobotNepoexitvalue() {
        if (!this.immutable) {
            throw new RuntimeException("no robotNepoexitvalue from an object under construction: " + toString());
        }
        return this.robotNepoexitvalue;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotNepoexitvalueDefined() {
        return this.robotNepoexitvalueDefined;
    }
    
    /**
     * SET robotNepoexitvalue. Object must be mutable.
     */
    public BaseResponse setRobotNepoexitvalue(int robotNepoexitvalue) {
        if (this.immutable) {
            throw new RuntimeException("robotNepoexitvalue assigned to an immutable object: " + toString());
        }
        this.robotNepoexitvalue = robotNepoexitvalue;
        this.robotNepoexitvalueDefined = true;
        return this;
    }
    
    /**
     * GET robotState. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getRobotState() {
        if (!this.immutable) {
            throw new RuntimeException("no robotState from an object under construction: " + toString());
        }
        return this.robotState;
    }
    
    /**
     * is the property defined? The property maybe undefined as it is not a required property
     *
     * @return true if the property is defined (has been set)
     */
    public boolean robotStateDefined() {
        return this.robotState != null;
    }
    
    /**
     * SET robotState. Object must be mutable.
     */
    public BaseResponse setRobotState(String robotState) {
        if (this.immutable) {
            throw new RuntimeException("robotState assigned to an immutable object: " + toString());
        }
        this.robotState = robotState;
        return this;
    }
    
    /**
     * generates a JSON-object from an immutable bean.<br>
     * Throws a runtime exception if inconsistencies are detected.
     */
    public JSONObject toJson() {
        if (!this.immutable) {
            throw new RuntimeException("no JSON from an object under construction: " + toString());
        }
        JSONObject jsonO = new JSONObject();
        try {
            jsonO.put("_version", "1");
            if (this.cmd != null) {
                jsonO.put("cmd", this.cmd);
            }
            jsonO.put("rc", this.rc);
            if (this.message != null) {
                jsonO.put("message", this.message);
            }
            if (this.cause != null) {
                jsonO.put("cause", this.cause);
            }
            if (this.parameters != null) {
                jsonO.put("parameters", this.parameters);
            }
            jsonO.put("initToken", this.initToken);
            jsonO.put("server.time", this.serverTime);
            jsonO.put("server.version", this.serverVersion);
            if (this.robotWaitDefined) {
                jsonO.put("robot.wait", this.robotWait);
            }
            if (this.robotBattery != null) {
                jsonO.put("robot.battery", this.robotBattery);
            }
            if (this.robotName != null) {
                jsonO.put("robot.name", this.robotName);
            }
            if (this.robotVersion != null) {
                jsonO.put("robot.version", this.robotVersion);
            }
            if (this.robotFirmwareName != null) {
                jsonO.put("robot.firmwareName", this.robotFirmwareName);
            }
            if (this.robotSensorvalues != null) {
                jsonO.put("robot.sensorvalues", this.robotSensorvalues);
            }
            if (this.robotNepoexitvalueDefined) {
                jsonO.put("robot.nepoexitvalue", this.robotNepoexitvalue);
            }
            if (this.robotState != null) {
                jsonO.put("robot.state", this.robotState);
            }
        } catch (JSONException e) {
            throw new RuntimeException("JSON unparse error when unparsing: " + this, e);
        }
        return jsonO;
    }
    
    @Override
    public String toString() {
        return "BaseResponse [immutable=" + this.immutable + ", cmd=" + this.cmd + ", rc=" + this.rc + ", message=" + this.message + ", cause=" + this.cause + ", parameters=" + this.parameters + ", initToken=" + this.initToken + ", serverTime=" + this.serverTime + ", serverVersion=" + this.serverVersion + ", robotWait=" + this.robotWait + ", robotBattery=" + this.robotBattery + ", robotName=" + this.robotName + ", robotVersion=" + this.robotVersion + ", robotFirmwareName=" + this.robotFirmwareName + ", robotSensorvalues=" + this.robotSensorvalues + ", robotNepoexitvalue=" + this.robotNepoexitvalue + ", robotState=" + this.robotState + " ]";
    }
    @Override
    public int hashCode() {
        throw new RuntimeException("no hashCode from transport beans!");
    }
    
    @Override
    public boolean equals(Object obj) {
        throw new RuntimeException("no equals from transport beans!");
    }
    
}
