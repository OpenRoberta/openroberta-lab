package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import com.google.inject.Inject;
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

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;

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
    public Response getSourceCode(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            response.put("progXML", dataPart.getString("progXML")); // always return the program, even if the workflow fails
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("progXML"))
                    .setConfigurationXml(dataPart.optString("confXML", configurationText))
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
            response.put("progXML", project.getAnnotatedProgramAsXml());
            response.put("confAnnos", project.getConfAnnotationList());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            Statistics.info("ProgramSource", "success", project.hasSucceeded());
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("getSourceCode failed", e);
            Statistics.info("ProgramSource", "success", false);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    @POST
    @Path("/sourceSimulation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationVMCode(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            response.put("progXML", dataPart.getString("progXML")); // always return the program, even if the workflow fails
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();

            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("progXML"))
                    .setConfigurationXml(dataPart.optString("confXML", configurationText))
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
            response.put("fileExtension", project.getSourceCodeFileExtension());
            response.put("progXML", project.getAnnotatedProgramAsXml());
            response.put("confAnno", project.getConfAnnotationList());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            response.put("javaScriptConfiguration", project.getSimSensorConfigurationJSON());
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("getSimulationVMCode failed", e);
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", true);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    @POST
    @Path("/run")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runProgram(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            response.put("progXML", dataPart.getString("progXML")); // always return the program, even if the workflow fails
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("progXML"))
                    .setConfigurationXml(dataPart.optString("confXML", configurationText))
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
            response.put("progAnno", project.getConfAnnotationList());
            response.put("errorCounter", project.getErrorCounter());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            // TODO auto connection robots return COMPILERWORKFLOW_SUCCESS or COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS
            // TODO which is not mapped to anything in the frontend, ROBOT_PUSH_RUN is mapped to the message that was used before workflows
            if ( project.getResult() == Key.COMPILERWORKFLOW_SUCCESS || project.getResult() == Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS ) {
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
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("runProgram failed", e);
            Statistics.info("ProgramRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    @POST
    @Path("/runNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runNative(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramNativeSource(dataPart.getString("progXML"))
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
            Statistics.info("ProgramRunNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("runNative failed", e);
            Statistics.info("ProgramRunNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    @POST
    @Path("/compileProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileProgram(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            response.put("progXML", dataPart.getString("progXML")); // always return the program, even if the workflow fails
            Project project =
                ProjectWorkflowRestController
                    .setupWithExportXML(httpSessionState.getRobotFactory(), dataPart.getString("progXML"))
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
            response.put("progXML", project.getAnnotatedProgramAsXml());
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
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("compileProgram failed", e);
            Statistics.info("ProgramCompile", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    @POST
    @Path("/compileNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileNative(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramNativeSource(dataPart.getString("progXML"))
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
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("compileNative failed", e);
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    @POST
    @Path("/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            Project project =
                new Project.Builder()
                    .setCompiledProgramPath(httpSessionState.getRobotFactory().getFirmwareDefaultProgramName())
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .setRobotCommunicator(this.brickCommunicator)
                    .build();
            ProjectService.executeWorkflow("reset", httpSessionState.getRobotFactory(), project);
            response.put("cmd", "reset");
            response.put("programName", project.getProgramName());
            response.put("errorCounter", project.getErrorCounter());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            Statistics.info("ProgramReset", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            return Response.ok(response).build();
        } catch ( Exception e ) {
            LOG.info("program reset failed", e);
            Statistics.info("ProgramReset", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return createErrorResponse(response, httpSessionState, this.brickCommunicator);
        }
    }

    private static Response createErrorResponse(JSONObject response, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) {
        try {
            UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", Util.getErrorTicketId());
        } catch ( JSONException ex ) {
            LOG.error("Could not add error info to response", ex);
        }
        UtilForREST.responseWithFrontendInfo(response, httpSessionState, brickCommunicator);
        return Response.ok(response).build();
    }

    private static Project.Builder setupWithExportXML(IRobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        return new Project.Builder().setConfigurationXml(configuration).setProgramXml(program).setFactory(factory);
    }
}