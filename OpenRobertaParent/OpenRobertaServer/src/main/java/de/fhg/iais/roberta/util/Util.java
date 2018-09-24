package de.fhg.iais.roberta.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData.State;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static String serverVersion;

    private Util() {
        // no objects
    }

    /**
     * TODO: remove this global setting. Injected from the <b>ServerStarter</b> to add version info to all frontend JSON objects. But not nice ... .
     *
     * @param serverVersion the version to set.
     */
    public static void setServerVersion(String serverVersion) {
        Util.serverVersion = serverVersion;
    }

    public static void addResultInfo(JSONObject response, AbstractProcessor processor) throws JSONException {
        String realKey = processor.getMessage().getKey();
        response.put("rc", processor.getRC());
        response.put("message", realKey);
        response.put("cause", realKey);
        response.put("parameter", processor.getParameter()); // if getParameters returns null, nothing bad happens :-)
    }

    public static JSONObject addSuccessInfo(JSONObject response, Key key) throws JSONException {
        Util.addResultInfo(response, "ok", key);
        return response;
    }

    public static JSONObject addErrorInfo(JSONObject response, Key key) throws JSONException {
        Util.addResultInfo(response, "error", key);
        return response;
    }

    private static void addResultInfo(JSONObject response, String rc, Key key) throws JSONException {
        String realKey = key.getKey();
        response.put("rc", rc);
        response.put("message", realKey);
        response.put("cause", realKey);
    }

    /**
     * add information for the Javascript client to the result json, especially about the state of the robot.<br>
     * This method must be <b>total</b>, i.e. must <b>never</b> throw exceptions.
     *
     * @param response the response object to enrich with data
     * @param httpSessionState needed to access the token
     * @param brickCommunicator needed to access the robot's state
     */
    public static void addFrontendInfo(JSONObject response, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) {
        try {
            response.put("serverTime", new Date());
            response.put("server.version", Util.serverVersion);
            if ( httpSessionState != null ) {
                String token = httpSessionState.getToken();
                if ( token != null ) {
                    if ( token.equals(ClientAdmin.NO_CONNECT) ) {
                        response.put("robot.state", "wait");
                    } else {
                        RobotCommunicationData state = brickCommunicator.getState(token);
                        if ( state != null ) {
                            response.put("robot.wait", state.getRobotConnectionTime());
                            response.put("robot.battery", state.getBattery());
                            response.put("robot.name", state.getRobotName());
                            response.put("robot.version", state.getMenuVersion());
                            response.put("robot.firmwareName", state.getFirmwareName());
                            response.put("robot.sensorvalues", state.getSensorValues());
                            response.put("robot.nepoexitvalue", state.getNepoExitValue());
                            State communicationState = state.getState();
                            String infoAboutState;
                            if ( communicationState == State.ROBOT_IS_BUSY ) {
                                infoAboutState = "busy";
                            } else if ( state.isRobotProbablyDisconnected() || communicationState == State.GARBAGE ) {
                                infoAboutState = "disconnected";
                            } else {
                                infoAboutState = "wait"; // is there a need to distinguish the communication state more detailed?
                            }
                            response.put("robot.state", infoAboutState);
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            Util.LOG.error("when adding info for the client, an unexpected exception occurred. Some info for the client may be missing", e);
        }
    }

    /**
     * Compares two version strings.
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a positive integer if str1 is _numerically_ greater than
     *         str2. The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;

        while ( i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]) ) {
            i++;
        }

        if ( i < vals1.length && i < vals2.length ) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }

    /**
     * Look up file names with specific file extensions in a specific directory.
     *
     * @param path The path to the directory where to look for the files.
     * @param extension The file extension(s).
     * @return a list of files names or an empty list.
     */
    public static List<String> getListOfFileNames(String path, String... extensions) {
        File dir = new File(path);
        List<File> listOfFiles = (List<File>) FileUtils.listFiles(dir, extensions, true);
        List<String> listOfFileNames = new ArrayList<>();
        for ( File file : listOfFiles ) {
            listOfFileNames.add(file.getName());
        }
        return listOfFileNames;
    }

    /**
     * Remove unwanted tags and tag/attribute combinations from a string to prevent XSS
     *
     * @param input XML code containing description attribute that contains code with unwanted tags
     * @return output XML code without unwanted tags in description attribute
     */
    public static String removeUnwantedDescriptionHTMLTags(String input, String... args) {
        String[] tagWhiteList =
            {
                "b",
                "i",
                "u",
                "strike",
                "div",
                "font",
                "br",
                "ul",
                "ol",
                "li"
            };
        String[] attributeWhiteList =
            {
                "size",
                "style",
                "color"
            };
        String DESCRIPTION = "description=\".*?\"";
        Pattern pattern = Pattern.compile(DESCRIPTION);
        Matcher matcher = pattern.matcher(input);
        String description;
        try {
            matcher.find();
            description = matcher.group();
        } catch ( IllegalStateException e ) {
            LOG.warn("No description found for the program");
            return input;
        }
        String newDescription = description.split("description=\"")[1];
        newDescription = newDescription.substring(0, newDescription.length() - 1);
        newDescription = StringEscapeUtils.unescapeHtml4(newDescription);

        PolicyFactory policy = new HtmlPolicyBuilder().allowElements(tagWhiteList).allowAttributes(attributeWhiteList).onElements(tagWhiteList).toFactory();
        String safeHTML = policy.sanitize(newDescription);
        if ( !newDescription.equals(safeHTML) ) {
            String additionalInfo = "Info: ";
            if ( args.length > 0 ) {
                for ( String arg : args ) {
                    additionalInfo += arg;
                    additionalInfo += " ";
                }
            } else {
                additionalInfo += "no info available / user not logged in";
            }
            LOG.error("Possible XSS: program description does not match sanitised value. " + additionalInfo);
        }
        return input.replace(description, "description=\"" + StringEscapeUtils.escapeHtml4(safeHTML) + "\"");
    }
}
