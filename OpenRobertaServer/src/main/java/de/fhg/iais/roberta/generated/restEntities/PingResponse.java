/*
 * This is a class GENERATED by the TransportGenerator maven plugin. DON'T MODIFY IT.
 * IF you modify it, your work may be lost: the class will be overwritten automatically
 * when the maven plugin is re-executed for any reasons.
 */
package de.fhg.iais.roberta.generated.restEntities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * the response for the /ping REST request. TODO: refactor! Duplicate names!<br><br>
 * Version: 1<br>
 * Datum: 2020-06-15
 */
public class PingResponse extends BaseResponse {
    protected String version;
    protected long date;
    protected boolean dateDefined = false;
    protected String dateAsString;
    
    /**
     * the response for the /ping REST request. TODO: refactor! Duplicate names!
     */
    public static PingResponse make() {
        return new PingResponse();
    }
    
    /**
     * the response for the /ping REST request. TODO: refactor! Duplicate names!
     */
    public static PingResponse makeFromString(String jsonS) {
        try {
            JSONObject jsonO = new JSONObject(jsonS);
            return make(jsonO);
        } catch (JSONException e) {
            throw new RuntimeException("JSON parse error when parsing: " + jsonS, e);
        }
    }
    
    /**
     * the response for the /ping REST request. TODO: refactor! Duplicate names!
     */
    public static PingResponse makeFromProperties(String cmd,String rc,String message,String cause,JSONObject parameters,String initToken,long serverTime,String serverVersion,long robotWait,String robotBattery,String robotName,String robotVersion,String robotFirmwareName,JSONObject robotSensorvalues,int robotNepoexitvalue,String robotState,boolean notificationsAvailable,String version,long date,String dateAsString) {
        PingResponse entity = new PingResponse();
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
        entity.setNotificationsAvailable(notificationsAvailable);
        entity.setVersion(version);
        entity.setDate(date);
        entity.setDateAsString(dateAsString);
        entity.immutable();
        return entity;
    }
    
    /**
     * the response for the /ping REST request. TODO: refactor! Duplicate names!
     */
    public static PingResponse make(JSONObject jsonO) {
        return make().merge(jsonO).immutable();
    }
    
    /**
     * merge the properties of a JSON-object into this bean. The bean must be "under construction".
     * The keys of the JSON-Object must be valid. The bean remains "under construction".<br>
     * Throws a runtime exception if inconsistencies are detected.
     */
    public PingResponse merge(JSONObject jsonO) {
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
                } else if ("notifications.available".equals(key)) {
                    setNotificationsAvailable(jsonO.optBoolean(key));
                } else if ("version".equals(key)) {
                    setVersion(jsonO.getString(key));
                } else if ("date".equals(key)) {
                    setDate(jsonO.getLong(key));
                } else if ("dateAsString".equals(key)) {
                    setDateAsString(jsonO.getString(key));
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
    public PingResponse immutable() {
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
    private PingResponse validate() {
        String _message = null;
        if ( !this.immutable ) {
            _message = "PingResponse-object is already immutable: " + toString();
        }
        if ( rc == null) {
            _message = "required property rc of PingResponse-object is not set: " + toString();
        }
        if ( initToken == null) {
            _message = "required property initToken of PingResponse-object is not set: " + toString();
        }
        if ( !serverTimeDefined) {
            _message = "required property serverTime of PingResponse-object is not set: " + toString();
        }
        if ( serverVersion == null) {
            _message = "required property serverVersion of PingResponse-object is not set: " + toString();
        }
        if ( version == null) {
            _message = "required property version of PingResponse-object is not set: " + toString();
        }
        if ( !dateDefined) {
            _message = "required property date of PingResponse-object is not set: " + toString();
        }
        if ( dateAsString == null) {
            _message = "required property dateAsString of PingResponse-object is not set: " + toString();
        }
        if ( _message != null ) {
            this.immutable = false;
            throw new RuntimeException(_message);
        }
        return this;
    }
    
    /**
     * GET version. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getVersion() {
        if (!this.immutable) {
            throw new RuntimeException("no version from an object under construction: " + toString());
        }
        return this.version;
    }
    
    /**
     * SET version. Object must be mutable.
     */
    public PingResponse setVersion(String version) {
        if (this.immutable) {
            throw new RuntimeException("version assigned to an immutable object: " + toString());
        }
        this.version = version;
        return this;
    }
    
    /**
     * GET date. Object must be immutable. Never return null or an undefined/default value.
     */
    public long getDate() {
        if (!this.immutable) {
            throw new RuntimeException("no date from an object under construction: " + toString());
        }
        return this.date;
    }
    
    /**
     * SET date. Object must be mutable.
     */
    public PingResponse setDate(long date) {
        if (this.immutable) {
            throw new RuntimeException("date assigned to an immutable object: " + toString());
        }
        this.date = date;
        this.dateDefined = true;
        return this;
    }
    
    /**
     * GET dateAsString. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getDateAsString() {
        if (!this.immutable) {
            throw new RuntimeException("no dateAsString from an object under construction: " + toString());
        }
        return this.dateAsString;
    }
    
    /**
     * SET dateAsString. Object must be mutable.
     */
    public PingResponse setDateAsString(String dateAsString) {
        if (this.immutable) {
            throw new RuntimeException("dateAsString assigned to an immutable object: " + toString());
        }
        this.dateAsString = dateAsString;
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
            if (this.notificationsAvailableDefined) {
                jsonO.put("notifications.available", this.notificationsAvailable);
            }
            jsonO.put("version", this.version);
            jsonO.put("date", this.date);
            jsonO.put("dateAsString", this.dateAsString);
        } catch (JSONException e) {
            throw new RuntimeException("JSON unparse error when unparsing: " + this, e);
        }
        return jsonO;
    }
    
    @Override
    public String toString() {
        return "PingResponse [immutable=" + this.immutable + ", cmd=" + this.cmd + ", rc=" + this.rc + ", message=" + this.message + ", cause=" + this.cause + ", parameters=" + this.parameters + ", initToken=" + this.initToken + ", serverTime=" + this.serverTime + ", serverVersion=" + this.serverVersion + ", robotWait=" + this.robotWait + ", robotBattery=" + this.robotBattery + ", robotName=" + this.robotName + ", robotVersion=" + this.robotVersion + ", robotFirmwareName=" + this.robotFirmwareName + ", robotSensorvalues=" + this.robotSensorvalues + ", robotNepoexitvalue=" + this.robotNepoexitvalue + ", robotState=" + this.robotState + ", notificationsAvailable=" + this.notificationsAvailable + ", version=" + this.version + ", date=" + this.date + ", dateAsString=" + this.dateAsString + " ]";
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
