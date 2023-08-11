package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.provider.XsltTrans;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.XsltAndJavaTransformer;
import de.fhg.iais.roberta.util.basic.Pair;

@Path("/transformBatch")
public class ClientTransformBatchController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientTransformBatchController.class);
    private static final AtomicBoolean ID_TRANSFORMER_IS_RUNNING = new AtomicBoolean(false);

    private final Map<String, RobotFactory> robotPluginMap;
    private final boolean feature_Toggle_loadProgramTransformSaveProgram;

    private XsltAndJavaTransformer xsltAndJavaTransformer = null;
    private ProgramProcessor programProcessor = null;
    private ProgramDao programDao = null;
    private DbSession dbSession;

    private int successfulTransforms = 0;
    private int failingTransforms = 0;
    private int notNeededTransforms = 0;

    @Inject
    public ClientTransformBatchController(ServerProperties serverProperties, @Named("robotPluginMap") Map<String, RobotFactory> robotPluginMap) {
        this.feature_Toggle_loadProgramTransformSaveProgram =
            serverProperties.getBooleanProperty("feature-toggle.load-program-transform-save-program");
        this.robotPluginMap = robotPluginMap;
    }

    @POST
    @Path("/fromTo")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response transformFromTo(@XsltTrans XsltAndJavaTransformer xsltAndJavaTransformer, @OraData DbSession dbSession, String fromTo) {
        if ( !feature_Toggle_loadProgramTransformSaveProgram ) {
            return logAndMakeResponse("feature toggle, that allows program transformations from the db is disabled");
        }
        if ( ID_TRANSFORMER_IS_RUNNING.getAndSet(true) ) {
            return logAndMakeResponse("an idList transformation is running. Please wait until finished");
        }
        try {
            this.xsltAndJavaTransformer = xsltAndJavaTransformer;
            programProcessor = new ProgramProcessor(dbSession, 0);
            programDao = new ProgramDao(dbSession);
            this.dbSession = dbSession;
            String[] fromToArray = fromTo.split("-");
            int from = Integer.parseInt(fromToArray[0]);
            int to = Integer.parseInt(fromToArray[1]);
            if ( from >= to || to - from > 1000 ) {
                return logAndMakeResponse("interval invalid or too large. Max 1000 programs allowed");
            }
            LOG.error("transformation of " + (to - from) + " programs from the database started");
            Thread.sleep(2000); // simple minded way to avoid congestion
            for ( int p = from; p <= to; p++ ) {
                transformProgramWithId(p);
            }
            return logAndMakeResponse(
                "transformation terminated. Transforms: " + successfulTransforms + ", not needed: " + notNeededTransforms + ", failing: " + failingTransforms);
        } catch ( Exception e ) {
            return logAndMakeResponse("exception during transform - should not happen, exception message: " + e.getMessage());
        } finally {
            ID_TRANSFORMER_IS_RUNNING.set(false);
            if ( dbSession != null ) {
                dbSession.close(false);
            }
        }
    }

    @POST
    @Path("/idList")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response transformIdList(@XsltTrans XsltAndJavaTransformer xsltAndJavaTransformer, @OraData DbSession dbSession, String idsToTransformAsString) {
        if ( !feature_Toggle_loadProgramTransformSaveProgram ) {
            return logAndMakeResponse("feature toggle, that allows program transformations from the db is disabled");
        }
        if ( ID_TRANSFORMER_IS_RUNNING.getAndSet(true) ) {
            return logAndMakeResponse("an idList transformation is running. Please wait until finished");
        }
        try {
            this.xsltAndJavaTransformer = xsltAndJavaTransformer;
            programProcessor = new ProgramProcessor(dbSession, 0);
            programDao = new ProgramDao(dbSession);
            this.dbSession = dbSession;
            String[] idsToTransform = idsToTransformAsString.split("\\s*,\\s*");
            if ( idsToTransform.length > 1000 ) {
                return logAndMakeResponse("idList too long. Max 1000 programs allowed");
            }
            LOG.error("transformation of " + idsToTransform.length + " programs from the database started");
            Thread.sleep(2000); // simple minded way to avoid congestion

            for ( String idToTransform : idsToTransform ) {
                transformProgramWithId(Integer.parseInt(idToTransform));
            }
            return logAndMakeResponse(
                "transformation terminated. Transforms: " + successfulTransforms + ", not needed: " + notNeededTransforms + ", failing: " + failingTransforms);
        } catch ( Exception e ) {
            return logAndMakeResponse("exception during transform - should not happen, exception message: " + e.getMessage());
        } finally {
            ID_TRANSFORMER_IS_RUNNING.set(false);
            if ( dbSession != null ) {
                dbSession.close(false);
            }
        }
    }

    private void transformProgramWithId(int idToTransform) {
        Program programToTransform = programDao.get(idToTransform);
        if ( programToTransform == null ) {
            detailedLogging("program with id " + idToTransform + " not found in the data base");
        } else {
            detailedLogging("id of the program to be transformed: " + idToTransform);
            String robotGroupName = programToTransform.getRobot().getName();
            RobotFactory robotFactory = getArbitraryPluginOfAPluginGroup(robotGroupName, robotPluginMap);
            String configName = programToTransform.getConfigName();
            String programText = programToTransform.getProgramText();
            String configText = programProcessor.getProgramsConfig(programToTransform);
            try {
                Pair<String, String> transformed = xsltAndJavaTransformer.transform(robotFactory, programText, configText);
                String programTextTransformed = transformed.getFirst();
                String configTextTransformed = transformed.getSecond();
                boolean programChanged = !programTextTransformed.equals(programText);
                boolean configChanged = configText == null ? configTextTransformed != null : !configText.equals(configTextTransformed);
                if ( programChanged || configChanged ) {
                    programProcessor.replaceTransformedProgram(programToTransform, programTextTransformed, configName, configTextTransformed);
                    detailedLogging("program with id " + idToTransform + " saved into the data base");
                    dbSession.commit();
                    successfulTransforms++;
                } else {
                    detailedLogging("program with id " + idToTransform + " was uptodate and is NOT saved into the data base");
                    notNeededTransforms++;
                }
            } catch ( Exception e ) {
                failingTransforms++;
                // first LOG: only message, second LOG: contains stacktrace (in most cases unnecessary)
                LOG.error("program with id " + idToTransform + " could NOT be transformed and is NOT saved into the data base. Message: " + e.getMessage());
                // LOG.error("program with id " + idToTransform + " could NOT be transformed and is NOT saved into the data base", e);
            }
        }
    }

    private RobotFactory getArbitraryPluginOfAPluginGroup(String pluginNameOrPluginGroup, Map<String, RobotFactory> robotPluginMap) {
        for ( RobotFactory robotPlugin : robotPluginMap.values() ) {
            if ( pluginNameOrPluginGroup.equals(robotPlugin.getGroup()) ) {
                return robotPlugin;
            }
        }
        return null;
    }

    private Response logAndMakeResponse(String message) {
        LOG.error(message);
        return Response.ok(message).build();
    }

    private void detailedLogging(String message) {
        // LOG.error(message);
    }
}
