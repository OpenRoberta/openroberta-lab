package de.fhg.iais.roberta.util;

public enum Key {
    SERVER_ERROR(),
    COMMAND_INVALID(),
    TOKEN_SET_SUCCESS(),
    TOKEN_SET_ERROR_NO_ROBOT_WAITING(),
    TOOLBOX_LOAD_ERROR_NOT_FOUND(),
    TOOLBOX_LOAD_SUCCESS(),
    ROBOT_PUSH_RUN(),
    ROBOT_NOT_WAITING(),
    ROBOT_FIRMWAREUPDATE_POSSIBLE(),
    ROBOT_FIRMWAREUPDATE_IMPOSSIBLE(),
    ROBOT_NOT_CONNECTED(),
    CONFIGURATION_ERROR_ID_INVALID(),
    CONFIGURATION_GET_ONE_ERROR_NOT_FOUND(),
    CONFIGURATION_GET_ONE_SUCCESS(),
    CONFIGURATION_SAVE_ERROR(),
    CONFIGURATION_SAVE_SUCCESS(),
    CONFIGURATION_GET_ALL_SUCCESS(),
    CONFIGURATION_DELETE_SUCCESS(),
    CONFIGURATION_DELETE_ERROR(),
    CONFIGURATION_SAVE_ERROR_NOT_SAVED_TO_DB(),
    PROGRAM_ERROR_ID_INVALID(),
    PROGRAM_GET_ONE_SUCCESS(),
    PROGRAM_GET_ONE_ERROR_NOT_FOUND,
    PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN,
    PROGRAM_SAVE_ERROR_NOT_SAVED_TO_DB,
    PROGRAM_SAVE_SUCCESS,
    PROGRAM_GET_ALL_SUCCESS,
    PROGRAM_DELETE_SUCCESS,
    PROGRAM_DELETE_ERROR,
    USER_GET_ONE_SUCCESS,
    USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG,
    USER_CREATE_SUCCESS,
    USER_CREATE_ERROR_MISSING_REQ_FIELDS,
    USER_CREATE_ERROR_NOT_SAVED_TO_DB,
    USER_DELETE_SUCCESS,
    USER_DELETE_ERROR_NOT_DELETED_IN_DB,
    USER_DELETE_ERROR_ID_NOT_FOUND,
    USER_GET_ALL_SUCCESS,
    OWNER_DOES_NOT_EXIST,
    PROGRAM_TO_SHARE_DOES_NOT_EXIST,
    USER_TO_SHARE_DOES_NOT_EXIST,
    COMPILERWORKFLOW_ERROR_PROGRAM_NOT_FOUND,
    COMPILERWORKFLOW_ERROR_CONFIGURATION_NOT_FOUND,
    COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED,
    COMPILERWORKFLOW_ERROR_CONFIGURATION_TRANSFORM_FAILED,
    COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED,
    COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;

    private Key() {
        this.key = "ORA_" + this.toString();
    }

    private Key(String key) {
        this.key = key;
    }

    private final String key;

    /**
     * get the key string. This string is looked up in a message table supplied by blockly. Blockly is running in the frontend. Thus, the typical use is:<br>
     * - return from somewhere in the server an enum object<br>
     * - the REST services (or their helper methods) take the key string and embed it into a JSON object returned to the (javascript) client<br>
     * - the client uses a language specific map to get the real message
     */
    public String getKey() {
        return this.key;
    }
}
