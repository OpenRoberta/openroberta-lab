package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.ProjectNativeResponse;
import de.fhg.iais.roberta.generated.restEntities.ProjectNepoResponse;
import de.fhg.iais.roberta.generated.restEntities.ProjectSourceResponse;
import de.fhg.iais.roberta.generated.restEntities.ProjectSourceSimulationResponse;
import de.fhg.iais.roberta.generated.restEntities.ProjectWorkflowRequest;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;

@Path("/projectWorkflow")
public class ProjectWorkflowRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectWorkflowRestController.class);

    private final RobotCommunicator robotCommunicator;

    @Inject
    public ProjectWorkflowRestController(RobotCommunicator robotCommunicator) {
        this.robotCommunicator = robotCommunicator;
    }

    @POST
    @Path("/source")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSourceCode(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectWorkflowRequest wfRequest = ProjectWorkflowRequest.make(fullRequest.getData());
            ProjectSourceResponse response = ProjectSourceResponse.make();
            response.setProgXML(wfRequest.getProgXML()); // always return the program, even if the workflow fails
            Project project = request2project(wfRequest, dbSession, httpSessionState, this.robotCommunicator, true, false);
            ProjectService.executeWorkflow("showsource", project);
            // To make this compatible with old frontend we will have to use the old names...
            response.setCmd("showSourceP");
            response.setSourceCode(project.getSourceCode().toString());
            response.setProgXML(project.getAnnotatedProgramAsXml());
            response.setConfAnnos(project.getConfAnnotationList());
            addProjectResultToResponse(response, project);
            Statistics.info("ProgramSource", "success", project.hasSucceeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.error("getSourceCode failed", e);
            Statistics.info("ProgramSource", "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/sourceSimulation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationVMCode(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectWorkflowRequest wfRequest = ProjectWorkflowRequest.make(fullRequest.getData());
            ProjectSourceSimulationResponse response = ProjectSourceSimulationResponse.make();
            response.setProgXML(wfRequest.getProgXML()); // always return the program, even if the workflow fails
            Project project = request2project(wfRequest, dbSession, httpSessionState, this.robotCommunicator, true, false);
            ProjectService.executeWorkflow("getsimulationcode", project);
            // To make this compatible with old frontend we will have to use the old names...
            response.setCmd("runPSim");
            response.setJavaScriptProgram(project.getSourceCode().toString());
            response.setFileExtension(project.getSourceCodeFileExtension());
            response.setProgXML(project.getAnnotatedProgramAsXml());
            response.setConfAnnos(project.getConfAnnotationList());
            response.setConfiguration(project.getConfigurationJSON());
            response.setProgramName(project.getProgramName());
            addProjectResultToResponse(response, project);
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("getSimulationVMCode failed", e);
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", true);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/run")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runProgram(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectWorkflowRequest wfRequest = ProjectWorkflowRequest.make(fullRequest.getData());
            ProjectNepoResponse response = ProjectNepoResponse.make();
            response.setProgXML(wfRequest.getProgXML()); // always return the program, even if the workflow fails
            Project project = request2project(wfRequest, dbSession, httpSessionState, this.robotCommunicator, true, false);
            ProjectService.executeWorkflow("run", project);
            response.setCmd("runPBack");
            response.setConfAnnos(project.getConfAnnotationList());
            response.setCompiledCode(project.getCompiledHex());
            response.setConfiguration(project.getConfigurationJSON());
            // TODO auto connection robots return COMPILERWORKFLOW_SUCCESS or COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS
            // TODO which is not mapped to anything in the frontend, ROBOT_PUSH_RUN is mapped to the message that was used before workflows
            if ( project.getResult() == Key.COMPILERWORKFLOW_SUCCESS || project.getResult() == Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS ) {
                project.setResult(Key.ROBOT_PUSH_RUN);
            }
            addProjectResultToResponse(response, project);
            final int programLength = StringUtils.countMatches(project.getAnnotatedProgramAsXml(), "<block ");
            Statistics.info("ProgramRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded(), "programLength", programLength);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("runProgram failed", e);
            Statistics.info("ProgramRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/stop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopProgram(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectNepoResponse response = ProjectNepoResponse.make();
            response.setCmd("stop");

            String token = httpSessionState.getToken();
            if ( token != null ) {
                // everything is fine
                boolean isPossible = this.robotCommunicator.stop(token, httpSessionState.getRobotName());
                if ( isPossible ) {
                    UtilForREST.addSuccessInfo(response, Key.ROBOT_PUSH_STOP_SUCCESS);
                } else {
                    return UtilForREST.makeBaseResponseForError(Key.ROBOT_PUSH_STOP_ERROR, httpSessionState, null);
                }
            } else {
                return UtilForREST.makeBaseResponseForError(Key.ROBOT_NOT_CONNECTED, httpSessionState, null);
            }

            Statistics.info("ProgramStop", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", true);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("stopProgram failed", e);
            Statistics.info("ProgramStop", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * seems to be only used by import program
     *
     * @param fullRequest
     * @return
     */
    @POST
    @Path("/compileProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileProgram(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectWorkflowRequest wfRequest = ProjectWorkflowRequest.make(fullRequest.getData());
            ProjectNepoResponse response = ProjectNepoResponse.make();
            response.setProgXML(wfRequest.getProgXML()); // always return the program, even if the workflow fails
            Project project = request2project(wfRequest, dbSession, httpSessionState, this.robotCommunicator, true, true);
            ProjectService.executeWorkflow("compile", project);
            response.setCmd("compileP");
            response.setProgXML(project.getAnnotatedProgramAsXml());
            response.setCompiledCode(project.getCompiledHex());
            addProjectResultToResponse(response, project);
            final int programLength = StringUtils.countMatches(project.getAnnotatedProgramAsXml(), "<block ");
            Statistics.info("ProgramCompile", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded(), "programLength", programLength);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("compileProgram failed", e);
            Statistics.info("ProgramCompile", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/runNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runNative(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectWorkflowRequest wfRequest = ProjectWorkflowRequest.make(fullRequest.getData());
            ProjectNativeResponse response = ProjectNativeResponse.make();
            Project project = request2project(wfRequest, dbSession, httpSessionState, this.robotCommunicator, false, false);
            ProjectService.executeWorkflow("runnative", project);
            response.setCmd("runNative");
            response.setCompiledCode(project.getCompiledHex());
            addProjectResultToResponse(response, project);
            Statistics.info("ProgramRunNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("runNative failed", e);
            Statistics.info("ProgramRunNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/compileNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileNative(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ProjectWorkflowRequest wfRequest = ProjectWorkflowRequest.make(fullRequest.getData());
            ProjectNativeResponse response = ProjectNativeResponse.make();
            Project project = request2project(wfRequest, dbSession, httpSessionState, this.robotCommunicator, false, false);
            ProjectService.executeWorkflow("compilenative", project);
            response.setCmd("runNative");
            response.setCompiledCode(project.getCompiledHex());
            addProjectResultToResponse(response, project);
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("compileNative failed", e);
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            ProjectNativeResponse response = ProjectNativeResponse.make();
            Project project =
                new Project.Builder()
                    .setCompiledProgramPath(httpSessionState.getRobotFactory().getFirmwareDefaultProgramName())
                    .setToken(httpSessionState.getToken())
                    .setRobot(httpSessionState.getRobotName())
                    .setFactory(httpSessionState.getRobotFactory())
                    .setRobotCommunicator(this.robotCommunicator)
                    .build();
            ProjectService.executeWorkflow("reset", project);
            response.setCmd("reset");
            response.setProgramName(project.getProgramName());
            response.setCompiledCode(project.getCompiledHex());
            addProjectResultToResponse(response, project);
            Statistics.info("ProgramReset", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.robotCommunicator);
        } catch ( Exception e ) {
            LOG.info("program reset failed", e);
            Statistics.info("ProgramReset", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.robotCommunicator);
        }
    }

    private static <T extends BaseResponse> void addProjectResultToResponse(T response, Project project) {
        response.setRc(project.hasSucceeded() ? "ok" : "error");
        response.setMessage(project.getResult().getKey());
        response.setCause(project.getResult().getKey());
        response.setParameters(new JSONObject(project.getResultParams()));
    }

    private static Project request2project(
        ProjectWorkflowRequest wfRequest,
        DbSession dbSession,
        HttpSessionState httpSessionState,
        RobotCommunicator robotCommunicator,
        boolean isNepo,
        boolean isExportXml) {
        final String robot = wfRequest.getRobot() == null ? httpSessionState.getRobotName() : wfRequest.getRobot();
        Project.Builder project =
            new Project.Builder()
                .setProgramName(wfRequest.getProgramName())
                .setSSID(wfRequest.getSSID())
                .setPassword(wfRequest.getPassword())
                .setLanguage(Language.findByAbbr(wfRequest.getLanguage()))
                .setToken(httpSessionState.getToken())
                .setRobot(robot)
                .setFactory(httpSessionState.getRobotFactory())
                .setRobotCommunicator(robotCommunicator);
        String progXml;
        String confXml;
        if ( isExportXml ) {
            Pair<String, String> progConfPair = ProjectWorkflowRestController.splitExportXML(wfRequest.getProgXML());
            progXml = progConfPair.getFirst();
            confXml = progConfPair.getSecond();
        } else {
            progXml = wfRequest.getProgXML();
            if ( wfRequest.getConfigurationName() != null ) {
                ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState.getUserId());
                confXml =
                    cp.getConfigurationText(wfRequest.getConfigurationName(), httpSessionState.getUserId(), httpSessionState.getRobotFactory().getGroup());
                if ( !cp.succeeded() ) {
                    throw new DbcException("invalid configuration request for name " + wfRequest.getConfigurationName() + ". Front end error.");
                }
            } else {
                confXml = wfRequest.getConfXML() == null ? httpSessionState.getRobotFactory().getConfigurationDefault() : wfRequest.getConfXML();
            }
        }
        if ( isNepo ) {
            project.setProgramXml(progXml);
            project.setConfigurationXml(confXml);
        } else {
            project.setProgramNativeSource(progXml);
        }
        return project.build();
    }

    public static Pair<String, String> splitExportXML(String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        return Pair.of(program, configuration);
    }
}