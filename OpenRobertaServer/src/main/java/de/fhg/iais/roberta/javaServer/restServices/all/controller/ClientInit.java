package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.Util;
import eu.bitwalker.useragentutils.UserAgent;

@Path("/init")
public class ClientInit {
    private static final Logger LOG = LoggerFactory.getLogger(ClientInit.class);
    private final RobotCommunicator brickCommunicator;
    private final ServerProperties serverProperties;

    @Inject
    public ClientInit(RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.brickCommunicator = brickCommunicator;
        this.serverProperties = serverProperties;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, @OraData DbSession dbSession, JSONObject fullRequest, @Context HttpHeaders httpHeaders)
        throws Exception {

        AliveData.rememberClientCall();
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
        new ClientLogger().log(ClientInit.LOG, fullRequest);
        JSONObject response = new JSONObject();
        try {
            JSONObject request = fullRequest.getJSONObject("data");
            ClientInit.LOG.info("INIT command. Trying to build a new HttpSessionState");
            response.put("cmd", "init");
            if ( httpSessionState.isInitTokenInitialized() ) {
                ClientInit.LOG
                    .error("init-token was found during init: reload/re-use of old session? more than 1 tab? NOTE: the old init-token will be destroyed");
                httpSessionState.reset();
            }
            httpSessionState.setInitToken();
            List<String> userAgentList = httpHeaders.getRequestHeader("User-Agent");
            String userAgentString = "";
            if ( userAgentList != null ) {
                userAgentString = userAgentList.get(0);
            }
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
            Statistics.infoUserAgent("Initialization", userAgent, httpSessionState.getCountryCode(), request);

            JSONObject server = new JSONObject();
            server.put("defaultRobot", this.serverProperties.getDefaultRobot());
            JSONObject robots = new JSONObject();
            Collection<String> availableRobots = this.serverProperties.getRobotWhitelist();
            int i = 0;
            for ( String robot : availableRobots ) {
                JSONObject robotDescription = new JSONObject();
                robotDescription.put("name", robot);
                if ( !ServerProperties.NAME_OF_SIM.equals(robot) ) {
                    robotDescription.put("realName", httpSessionState.getRobotFactory(robot).getRealName());
                    robotDescription.put("info", httpSessionState.getRobotFactory(robot).getInfo());
                    robotDescription.put("beta", httpSessionState.getRobotFactory(robot).isBeta());
                    robotDescription.put("group", httpSessionState.getRobotFactory(robot).getGroup());
                }
                robots.put("" + i, robotDescription);
                i++;
            }
            server.put("isPublic", this.serverProperties.getBooleanProperty("server.public"));
            server.put("robots", robots);
            String staticRecourcesDir = this.serverProperties.getStringProperty("server.staticresources.dir");
            String pathToTutorial = this.serverProperties.getStringProperty("server.admin.dir") + "/tutorial";
            JSONObject tutorial =
                new File(pathToTutorial).isDirectory() //
                    ? ClientInit.getJSONObjectsFromDirectory(pathToTutorial) //
                    : new JSONObject();
            server.put("tutorial", tutorial);

            String pathToLegalTexts = this.serverProperties.getStringProperty("server.admin.dir") + "/legalTexts";
            JSONObject legalTextFiles = ClientInit.getHTMLContentFromDirectory(pathToLegalTexts);

            server.put("legalTexts", legalTextFiles);

            String pathToHelp = staticRecourcesDir + File.separator + "help";
            List<String> help = ClientInit.getListOfFileNamesFromDirectory(pathToHelp, "html");
            server.put("help", help);
            String theme = this.serverProperties.getStringProperty("server.theme");
            server.put("theme", theme);
            response.put("server", server);
            UtilForREST.addSuccessInfo(response, Key.INIT_SUCCESS);
            UtilForREST.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
            dbSession.commit();
        } catch ( Exception e ) {
            dbSession.rollback();
            UtilForREST.addErrorInfo(response, Key.SERVER_ERROR);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        MDC.clear();
        return Response.ok(response).build();
    }

    /**
     * Look up file names with specific file extensions in a specific directory.
     *
     * @param path The path to the directory where to look for the files.
     * @param extension The file extension(s).
     * @return a list of files names or an empty list.
     */
    private static List<File> getListOfFilesFromDirectory(String path, String... extensions) {
        File dir = new File(path);
        try {
            List<File> listOfFiles = (List<File>) FileUtils.listFiles(dir, extensions, true);
            return listOfFiles;
        } catch ( Exception e ) {
            return Collections.<File> emptyList();
        }
    }

    /**
     * Reads all files provided by the list of paths. Assuming that the files content json data with one property "name" this method returns a JSON object
     * containing the json data.
     *
     * @param path to the directory
     * @param extensions of the files
     * @return
     */
    private static JSONObject getJSONObjectsFromDirectory(String path) {
        List<File> files = ClientInit.getListOfFilesFromDirectory(path, "json");
        JSONObject jsonObjRepresentingTheDirectory = new JSONObject();
        for ( File file : files ) {
            try {
                JSONObject jsonObjInDirectory = new JSONObject(Util.readFileContent(file.getAbsolutePath()));
                jsonObjRepresentingTheDirectory.put(jsonObjInDirectory.getString("name").toLowerCase().replaceAll("\\s", ""), jsonObjInDirectory);
            } catch ( Exception e ) {
                // no problem, we simply ignore files without valid json data or without the property "name"
            }
        }
        return jsonObjRepresentingTheDirectory;
    }

    private static JSONObject getHTMLContentFromDirectory(String directoryPath) {

        List<File> files = ClientInit.getListOfFilesFromDirectory(directoryPath, "html");
        JSONObject jsonObjRepresentingTheDirectory = new JSONObject();
        for ( File file : files ) {
            try {
                String fileContent = Util.readFileContent(file.getAbsolutePath());
                if ( !fileContent.trim().isEmpty() ) {
                    jsonObjRepresentingTheDirectory.put(file.getName(), fileContent);
                }
            } catch ( Exception e ) {
                //There should not be a problem in storing a String as property, but yeah, in case it does lets ignore that file
            }
        }
        return jsonObjRepresentingTheDirectory;
    }

    /**
     * Looks for files in a specific directory and returns the names of the files found.
     *
     * @param The path to the directory where to look for the files.
     * @param extension The file extension(s).
     * @return a list of files names or an empty list.
     */
    private static List<String> getListOfFileNamesFromDirectory(String path, String extensions) {
        List<File> files = ClientInit.getListOfFilesFromDirectory(path, extensions);
        List<String> listOfFileNames = new ArrayList<>();
        for ( File file : files ) {
            listOfFileNames.add(file.getName());
        }
        return listOfFileNames;
    }
}