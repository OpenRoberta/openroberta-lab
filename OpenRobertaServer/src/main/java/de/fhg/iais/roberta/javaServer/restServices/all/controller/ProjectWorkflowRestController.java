package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

@Path("/projectWorkflow")
public class ProjectWorkflowRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectWorkflowRestController.class);

    private final RobotCommunicator brickCommunicator;

    @Inject
    public ProjectWorkflowRestController(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Path("/source")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSourceCode(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Util.handleRequestInit(httpSessionState, LOG, request);
        JSONObject dataPart = Util.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("programBlockSet"))
                    .setConfigurationXml(dataPart.optString("configurationBlockSet", configurationText))
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            ProjectService.executeWorkflow("showsource", httpSessionState.getRobotFactory(), project);
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "showSourceP");
            response.put("sourceCode", project.getSourceCode());
            response.put("fileExtension", project.getFileExtension());
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("configuration", project.getAnnotatedConfigurationAsXml());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            Statistics.info("ProgramSource", "success", project.hasSucceeded());
            Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("getSourceCode failed", e);
            Statistics.info("ProgramSource", "success", false);
            return createErrorResponse(response, httpSessionState);
        }
    }

    @POST
    @Path("/sourceSimulation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationVMCode(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Util.handleRequestInit(httpSessionState, LOG, request);
        JSONObject dataPart = Util.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("programBlockSet"))
                    .setConfigurationXml(dataPart.optString("configurationBlockSet", configurationText))
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            ProjectService.executeWorkflow("getsimulationcode", httpSessionState.getRobotFactory(), project);
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "runPSim");
            response.put("javaScriptProgram", project.getSourceCode());
            response.put("fileExtension", project.getFileExtension());
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("configuration", project.getAnnotatedConfigurationAsXml());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("getSimulationVMCode failed", e);
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", true);
            return createErrorResponse(response, httpSessionState);
        }
    }

    @POST
    @Path("/run")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Util.handleRequestInit(httpSessionState, LOG, request);
        JSONObject dataPart = Util.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("programBlockSet"))
                    .setConfigurationXml(dataPart.optString("configurationBlockSet", configurationText))
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .setRobotCommunicator(this.brickCommunicator)
                    .build();
            ProjectService.executeWorkflow("run", httpSessionState.getRobotFactory(), project);
            response.put("cmd", "runPBack");
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("errorCounter", project.getErrorCounter());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            // TODO auto connection robots return COMPILERWORKFLOW_SUCCESS or COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS
            // TODO which is not mapped to anything in the frontend, ROBOT_PUSH_RUN is mapped to the message that was used before workflows
            if ( (project.getResult() == Key.COMPILERWORKFLOW_SUCCESS) || (project.getResult() == Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS) ) {
                project.setResult(Key.ROBOT_PUSH_RUN);
            }
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            Statistics
                .info(
                    "ProgramRun",
                    "LoggedIn",
                    httpSessionState.isUserLoggedIn(),
                    "success",
                    project.hasSucceeded(),
                    "programLength",
                    StringUtils.countMatches(project.getAnnotatedProgramAsXml(), "<block "));
            Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("runProgram failed", e);
            Statistics.info("ProgramRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState);
        }
    }

    @POST
    @Path("/runNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runNative(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Util.handleRequestInit(httpSessionState, LOG, request);
        JSONObject dataPart = Util.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramNativeSource(dataPart.getString("programText"))
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .setRobotCommunicator(this.brickCommunicator)
                    .build();
            ProjectService.executeWorkflow("runnative", httpSessionState.getRobotFactory(), project);
            response.put("cmd", "runNative");
            response.put("errorCounter", project.getErrorCounter());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            Statistics.info("ProgramRunNative", "LoggedIn", httpSessionState.isUserLoggedIn(),
                            "success", project.hasSucceeded());
            Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("runNative failed", e);
            Statistics.info("ProgramRunNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState);
        }
    }

    @POST
    @Path("/compileProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Util.handleRequestInit(httpSessionState, LOG, request);
        JSONObject dataPart = Util.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            Project project =
                Util
                    .setupWithExportXML(httpSessionState.getRobotFactory(), dataPart.getString("programBlockSet"))
                    .setProgramName(dataPart.getString("programName"))
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            ProjectService.executeWorkflow("compile", httpSessionState.getRobotFactory(), project);
            response.put("cmd", "compileP");
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("errorCounter", project.getErrorCounter());
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            Statistics
                .info(
                    "ProgramCompile",
                    "LoggedIn",
                    httpSessionState.isUserLoggedIn(),
                    "success",
                    project.hasSucceeded(),
                    "programLength",
                    StringUtils.countMatches(project.getAnnotatedProgramAsXml(), "<block "));
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("compileProgram failed", e);
            Statistics.info("ProgramCompile", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState);
        }
    }

    @POST
    @Path("/compileNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileNative(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Util.handleRequestInit(httpSessionState, LOG, request);
        JSONObject dataPart = Util.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            Project project =
                    new Project.Builder()
                            .setProgramName(dataPart.getString("programName"))
                            .setProgramNativeSource(dataPart.getString("programText"))
                            .setSSID(dataPart.optString("SSID", null))
                            .setPassword(dataPart.optString("password", null))
                            .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                            .setToken(httpSessionState.getToken())
                            .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                            .setFactory(httpSessionState.getRobotFactory())
                            .setRobotCommunicator(this.brickCommunicator)
                            .build();
            ProjectService.executeWorkflow("compilenative", httpSessionState.getRobotFactory(), project);
            response.put("cmd", "runNative");
            response.put("errorCounter", project.getErrorCounter());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("compileNative failed", e);
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState);
        }
    }

    private Response createErrorResponse(JSONObject response, HttpSessionState httpSessionState) {
        try {
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", Util1.getErrorTicketId());
        } catch ( JSONException ex ) {
            LOG.error("Could not add error info to response", ex);
        }
        Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        return Response.ok(response).build();
    }
}
