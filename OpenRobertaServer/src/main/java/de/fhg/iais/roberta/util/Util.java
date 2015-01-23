package de.fhg.iais.roberta.util;

import java.io.FileReader;
import java.sql.Timestamp;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.brick.BrickCommunicationData;
import de.fhg.iais.roberta.brick.BrickCommunicationData.State;
import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.AbstractProcessor;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final String PROPERTY_DEFAULT_PATH = "openRoberta.properties";
    private static final String[] reservedWords = new String[] {
        //  @formatter:off
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
        "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
        "throw", "throws", "transient", "true", "try", "void", "volatile", "while"
        //  @formatter:on
        };

    private static final AtomicInteger errorTicketNumber = new AtomicInteger(0);

    public static final String SERVER_ERROR = "ORA_SERVER_ERROR";
    public static final String COMMAND_INVALID = "ORA_COMMAND_INVALID";
    public static final String TOKEN_SET_SUCCESS = "ORA_TOKEN_SET_SUCCESS";
    public static final String TOKEN_SET_ERROR_NO_ROBOT_WAITING = "ORA_TOKEN_SET_ERROR_NO_ROBOT_WAITING";
    public static final String TOOLBOX_LOAD_ERROR_NOT_FOUND = "ORA_TOOLBOX_LOAD_ERROR_NOT_FOUND";
    public static final String TOOLBOX_LOAD_SUCCESS = "ORA_TOOLBOX_LOAD_SUCCESS";
    public static final String ROBOT_PUSH_RUN = "ORA_ROBOT_PUSH_RUN";
    public static final String ROBOT_NOT_WAITING = "ORA_ROBOT_NOT_WAITING";
    public static final String ROBOT_FIRMWAREUPDATE_POSSIBLE = "ORA_ROBOT_FIRMWAREUPDATE_POSSIBLE";
    public static final String ROBOT_FIRMWAREUPDATE_IMPOSSIBLE = "ORA_ROBOT_FIRMWAREUPDATE_IMPOSSIBLE";
    public static final String ROBOT_NOT_CONNECTED = "ORA_ROBOT_NOT_CONNECTED";
    public static final String CONFIGURATION_ERROR_ID_INVALID = "ORA_CONFIGURATION_ERROR_ID_INVALID";
    public static final String CONFIGURATION_GET_ONE_ERROR_NOT_FOUND = "ORA_CONFIGURATION_GET_ONE_ERROR_NOT_FOUND";
    public static final String CONFIGURATION_GET_ONE_SUCCESS = "ORA_CONFIGURATION_GET_ONE_SUCCESS";
    public static final String CONFIGURATION_SAVE_ERROR = "ORA_CONFIGURATION_SAVE_ERROR";
    public static final String CONFIGURATION_SAVE_SUCCESS = "ORA_CONFIGURATION_SAVE_SUCCESS";
    public static final String CONFIGURATION_GET_ALL_SUCCESS = "ORA_CONFIGURATION_GET_ALL_SUCCESS";
    public static final String CONFIGURATION_DELETE_SUCCESS = "ORA_CONFIGURATION_DELETE_SUCCESS";
    public static final String CONFIGURATION_DELETE_ERROR = "ORA_CONFIGURATION_DELETE_ERROR";
    public static final String CONFIGURATION_SAVE_ERROR_NOT_SAVED_TO_DB = "ORA_CONFIGURATION_SAVE_ERROR_NOT_SAVED_TO_DB";
    public static final String PROGRAM_ERROR_ID_INVALID = "ORA_PROGRAM_ERROR_ID_INVALID";
    public static final String PROGRAM_GET_ONE_SUCCESS = "ORA_PROGRAM_GET_ONE_SUCCESS";
    public static final String PROGRAM_GET_ONE_ERROR_NOT_FOUND = "ORA_PROGRAM_GET_ONE_ERROR_NOT_FOUND";
    public static final String PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN = "ORA_PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN";
    public static final String PROGRAM_SAVE_ERROR_NOT_SAVED_TO_DB = "ORA_PROGRAM_SAVE_ERROR_NOT_SAVED_TO_DB";
    public static final String PROGRAM_SAVE_SUCCESS = "ORA_PROGRAM_SAVE_SUCCESS";
    public static final String PROGRAM_GET_ALL_SUCCESS = "ORA_PROGRAM_GET_ALL_SUCCESS";
    public static final String PROGRAM_DELETE_SUCCESS = "ORA_PROGRAM_DELETE_SUCCESS";
    public static final String PROGRAM_DELETE_ERROR = "ORA_PROGRAM_DELETE_ERROR";
    public static final String USER_GET_ONE_SUCCESS = "ORA_USER_GET_ONE_SUCCESS";
    public static final String USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG = "ORA_USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG";
    public static final String USER_CREATE_SUCCESS = "ORA_USER_CREATE_SUCCESS";
    public static final String USER_CREATE_ERROR_NOT_SAVED_TO_DB = "ORA_USER_CREATE_ERROR_NOT_SAVED_TO_DB";
    public static final String USER_DELETE_SUCCESS = "ORA_USER_DELETE_SUCCESS";
    public static final String USER_DELETE_ERROR_NOT_DELETED_IN_DB = "ORA_USER_DELETE_ERROR_NOT_DELETED_IN_DB";
    public static final String USER_DELETE_ERROR_ID_NOT_FOUND = "ORA_USER_DELETE_ERROR_ID_NOT_FOUND";
    public static final String USER_GET_ALL_SUCCESS = "ORA_USER_GET_ALL_SUCCESS";
    public static final String OWNER_DOES_NOT_EXIST = "ORA_OWNER_DOES_NOT_EXIST";
    public static final String PROGRAM_TO_SHARE_DOES_NOT_EXIST = "ORA_PROGRAM_TO_SHARE_DOES_NOT_EXIST";
    public static final String USER_TO_SHARE_DOES_NOT_EXIST = "ORA_USER_TO_SHARE_DOES_NOT_EXIST";

    private Util() {
        // no objects
    }

    /**
     * load the OpenRoberta properties. The URI of the properties refers either to the file system or to the classpath. To be used for production and test. If
     * the parameters is null,
     * the classpath is searched for a default property file.<br>
     * <br>
     * The URI start with "file:" if a path of the file system should be used or starts with "classpath:" if the properties should be loaded as a resource
     * from the classpath. If <code>null</code>, the resource is loaded from the classpath using the default name "openRoberta.properties".
     *
     * @param propertyURI URI of the property file. May be null
     * @return the properties. Returns null, if errors occur (file not found, ...)
     */
    public static Properties loadProperties(String propertyURI) {
        Properties properties = new Properties();
        try {
            if ( propertyURI == null ) {
                LOG.info("properties from classpath. Using the resource: " + PROPERTY_DEFAULT_PATH);
                properties.load(Util.class.getClassLoader().getResourceAsStream(PROPERTY_DEFAULT_PATH));
            } else if ( propertyURI.startsWith("file:") ) {
                String filesystemPathName = propertyURI.substring(5);
                LOG.info("properties from file system. Path: " + filesystemPathName);
                properties.load(new FileReader(filesystemPathName));
            } else if ( propertyURI.startsWith("classpath:") ) {
                String classPathName = propertyURI.substring(10);
                LOG.info("properties from classpath. Using the resource: " + classPathName);
                properties.load(Util.class.getClassLoader().getResourceAsStream(classPathName));
            } else {
                LOG.error("Could not load properties. Invalid URI: " + propertyURI);
                return null;
            }
            return properties;
        } catch ( Exception e ) {
            LOG.error("Could not load properties. Inspect the stacktrace", e);
            return null;
        }
    }

    /**
     * Check whether a String is a valid Java identifier. It is checked, that no reserved word is used
     *
     * @param s String to check
     * @return <code>true</code> if the given String is a valid Java
     *         identifier; <code>false</code> otherwise.
     */
    public final static boolean isValidJavaIdentifier(String s) {
        if ( s == null || s.length() == 0 ) {
            return false;
        }
        CharacterIterator citer = new StringCharacterIterator(s);
        // first
        char c = citer.first();
        if ( c == CharacterIterator.DONE ) {
            return false;
        }
        if ( !Character.isJavaIdentifierStart(c) && !Character.isIdentifierIgnorable(c) ) {
            return false;
        }
        // remainder
        c = citer.next();
        while ( c != CharacterIterator.DONE ) {
            if ( !Character.isJavaIdentifierPart(c) && !Character.isIdentifierIgnorable(c) ) {
                return false;
            }
            c = citer.next();
        }
        return Arrays.binarySearch(reservedWords, s) < 0;
    }

    /**
     * get the actual date as timestamp
     *
     * @return the actual date as timestamp
     */
    public static Timestamp getNow() {
        return new Timestamp(new Date().getTime());
    }

    public static void logServerError(String detailMessage) {
        LOG.error(SERVER_ERROR + " Detail message: " + detailMessage, new Throwable());
    }

    public static void addResultInfo(JSONObject response, AbstractProcessor processor) throws JSONException {
        response.put("rc", processor.getRC());
        response.put("message", processor.getMessage());
        response.put("cause", processor.getMessage());
        response.put("parameter", processor.getParameter()); // if getParameters returns null, nothing bad happens :-)
    }

    /**
     * add information for the Javascript client to the result json, especially about the state of the robot.<br>
     * This method must be <b>total</b>, i.e. must
     * <b>never</b> throw exceptions.
     *
     * @param response the response object to enrich with data
     * @param httpSessionState needed to access the token
     * @param brickCommunicator needed to access the robot's state
     */
    public static void addFrontendInfo(JSONObject response, HttpSessionState httpSessionState, BrickCommunicator brickCommunicator) {
        try {
            response.put("serverTime", new Date());
            if ( httpSessionState != null ) {
                String token = httpSessionState.getToken();
                if ( token != null ) {
                    BrickCommunicationData state = brickCommunicator.getState(token);
                    if ( state != null ) {
                        response.put("robot.wait", state.getElapsedMsecOfStartOfLastRequest());
                        response.put("robot.battery", state.getBattery());
                        response.put("robot.name", state.getRobotName());
                        response.put("robot.state", state.getState() == State.BRICK_IS_BUSY ? "busy" : "wait");
                    }
                }
            }
        } catch ( Exception e ) {
            LOG.error("when adding info for the client, an unexpected exception occured. Some info for the client may be missing", e);
        }
    }

    public static String getErrorTicketId() {
        return "E-" + errorTicketNumber.incrementAndGet();
    }
}
