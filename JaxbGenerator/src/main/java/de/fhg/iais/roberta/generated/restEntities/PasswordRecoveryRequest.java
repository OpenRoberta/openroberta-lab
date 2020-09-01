/*
 * This is a class GENERATED by the TransportGenerator maven plugin. DON'T MODIFY IT.
 * IF you modify it, your work may be lost: the class will be overwritten automatically
 * when the maven plugin is re-executed for any reasons.
 */
package de.fhg.iais.roberta.generated.restEntities;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * the request description for the /resetPassword REST request<br><br>
 * Version: 1<br>
 * Datum: 2020-06-15
 */
public class PasswordRecoveryRequest extends BaseRequest {
    protected String lostEmail;
    protected String language;
    
    /**
     * the request description for the /resetPassword REST request
     */
    public static PasswordRecoveryRequest make() {
        return new PasswordRecoveryRequest();
    }
    
    /**
     * the request description for the /resetPassword REST request
     */
    public static PasswordRecoveryRequest makeFromString(String jsonS) {
        try {
            JSONObject jsonO = new JSONObject(jsonS);
            return make(jsonO);
        } catch (JSONException e) {
            throw new RuntimeException("JSON parse error when parsing: " + jsonS, e);
        }
    }
    
    /**
     * the request description for the /resetPassword REST request
     */
    public static PasswordRecoveryRequest makeFromProperties(String cmd,String lostEmail,String language) {
        PasswordRecoveryRequest entity = new PasswordRecoveryRequest();
        entity.setCmd(cmd);
        entity.setLostEmail(lostEmail);
        entity.setLanguage(language);
        entity.immutable();
        return entity;
    }
    
    /**
     * the request description for the /resetPassword REST request
     */
    public static PasswordRecoveryRequest make(JSONObject jsonO) {
        return make().merge(jsonO).immutable();
    }
    
    /**
     * merge the properties of a JSON-object into this bean. The bean must be "under construction".
     * The keys of the JSON-Object must be valid. The bean remains "under construction".<br>
     * Throws a runtime exception if inconsistencies are detected.
     */
    public PasswordRecoveryRequest merge(JSONObject jsonO) {
        try {
            for (String key : JSONObject.getNames(jsonO)) {
                if ("_version".equals(key)) {
                } else if ("cmd".equals(key)) {
                    setCmd(jsonO.optString(key));
                } else if ("lostEmail".equals(key)) {
                    setLostEmail(jsonO.getString(key));
                } else if ("language".equals(key)) {
                    setLanguage(jsonO.getString(key));
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
    public PasswordRecoveryRequest immutable() {
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
    private PasswordRecoveryRequest validate() {
        String _message = null;
        if ( !this.immutable ) {
            _message = "PasswordRecoveryRequest-object is already immutable: " + toString();
        }
        if ( lostEmail == null) {
            _message = "required property lostEmail of PasswordRecoveryRequest-object is not set: " + toString();
        }
        if ( language == null) {
            _message = "required property language of PasswordRecoveryRequest-object is not set: " + toString();
        }
        if ( _message != null ) {
            this.immutable = false;
            throw new RuntimeException(_message);
        }
        return this;
    }
    
    /**
     * GET lostEmail. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getLostEmail() {
        if (!this.immutable) {
            throw new RuntimeException("no lostEmail from an object under construction: " + toString());
        }
        return this.lostEmail;
    }
    
    /**
     * SET lostEmail. Object must be mutable.
     */
    public PasswordRecoveryRequest setLostEmail(String lostEmail) {
        if (this.immutable) {
            throw new RuntimeException("lostEmail assigned to an immutable object: " + toString());
        }
        this.lostEmail = lostEmail;
        return this;
    }
    
    /**
     * GET language. Object must be immutable. Never return null or an undefined/default value.
     */
    public String getLanguage() {
        if (!this.immutable) {
            throw new RuntimeException("no language from an object under construction: " + toString());
        }
        return this.language;
    }
    
    /**
     * SET language. Object must be mutable.
     */
    public PasswordRecoveryRequest setLanguage(String language) {
        if (this.immutable) {
            throw new RuntimeException("language assigned to an immutable object: " + toString());
        }
        this.language = language;
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
            jsonO.put("lostEmail", this.lostEmail);
            jsonO.put("language", this.language);
        } catch (JSONException e) {
            throw new RuntimeException("JSON unparse error when unparsing: " + this, e);
        }
        return jsonO;
    }
    
    @Override
    public String toString() {
        return "PasswordRecoveryRequest [immutable=" + this.immutable + ", cmd=" + this.cmd + ", lostEmail=" + this.lostEmail + ", language=" + this.language + " ]";
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
