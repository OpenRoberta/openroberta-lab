package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.generated.restEntities.InitRequest;
import de.fhg.iais.roberta.generated.restEntities.InitResponse;
import de.fhg.iais.roberta.main.IIpToCountry;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;
import eu.bitwalker.useragentutils.UserAgent;

@Path("/init")
public class ClientInit {
    private static final Logger LOG = LoggerFactory.getLogger(ClientInit.class);

    private final Map<String, IRobotFactory> robotPluginMap;
    private final RobotCommunicator brickCommunicator;
    private final ServerProperties serverProperties;
    private final IIpToCountry ipToCountry;

    @Inject
    public ClientInit(
        @Named("robotPluginMap") Map<String, IRobotFactory> robotPluginMap,
        RobotCommunicator brickCommunicator,
        ServerProperties serverProperties,
        IIpToCountry ipToCountry) //
    {
        this.robotPluginMap = robotPluginMap;
        this.brickCommunicator = brickCommunicator;
        this.serverProperties = serverProperties;
        this.ipToCountry = ipToCountry;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(InitRequest fullRequest, @Context HttpServletRequest httpRequest) throws JSONException //
    {
        HttpSessionState httpSessionState = null;
        try {
            InitResponse response = InitResponse.make();
            response.setCmd("init");
            AliveData.rememberClientCall();
            new ClientLogger().log(ClientInit.LOG, fullRequest.getLog());
            ClientInit.LOG.info("INIT command. Trying to build a new HttpSessionState");
            httpSessionState = HttpSessionState.init(this.robotPluginMap, this.serverProperties, getCountryCode(httpRequest, this.ipToCountry));
            MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
            MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
            MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
            String userAgentString = httpRequest.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
            Statistics.infoUserAgent("Initialization", userAgent, httpSessionState.getCountryCode(), fullRequest.getData());

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
                    String robotInfoDE = httpSessionState.getRobotFactory(robot).getInfoDE();
                    String robotInfoEN = httpSessionState.getRobotFactory(robot).getInfoEN();
                    robotDescription.put("infoDE", robotInfoDE);
                    robotDescription.put("infoEN", robotInfoEN);
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
            response.setServer(server);
            UtilForREST.addSuccessInfo(response, Key.INIT_SUCCESS);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, brickCommunicator);
        } catch ( Exception e ) {
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        }
    }

    private static String getCountryCode(HttpServletRequest servletRequest, IIpToCountry ipToCountry) {
        String remoteAddr = "";
        if ( servletRequest != null ) {
            remoteAddr = servletRequest.getHeader("X-FORWARDED-FOR");
            if ( remoteAddr == null || "".equals(remoteAddr) ) {
                remoteAddr = servletRequest.getRemoteAddr();
            }
        }
        InetAddress addrAsIp;
        String countryCode = "..";
        try {
            Clock getByNameTime = Clock.start();
            addrAsIp = InetAddress.getByName(remoteAddr);
            long elapsed = getByNameTime.elapsedMsec();
            if ( elapsed > 1000 ) {
                LOG.error("InetAddress.getByName(" + remoteAddr + ") + getCountryCode took " + elapsed + "msec");
            }
            countryCode = ipToCountry.getCountryCode(addrAsIp);
        } catch ( IOException e ) {
            LOG.info("Could not evaluate the actual ip as a country code. Likely a problem with the IpToCountry file.");
        }
        return countryCode;
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
                jsonObjRepresentingTheDirectory.put(file.getName().replaceFirst("[.][^.]+$", "").toLowerCase().replaceAll("\\s", ""), jsonObjInDirectory);
            } catch ( Exception e ) {
                // no problem, we simply ignore files without valid json data
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