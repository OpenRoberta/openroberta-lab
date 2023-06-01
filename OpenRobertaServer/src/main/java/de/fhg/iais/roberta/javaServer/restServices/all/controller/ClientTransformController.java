package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.Map;

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

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.provider.XsltTrans;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.UtilForXmlTransformation;
import de.fhg.iais.roberta.util.XsltTransformer;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

@Path("/transform")
public class ClientTransformController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientTransformController.class);

    private final Map<String, RobotFactory> robotPluginMap;
    private final boolean feature_Toggle_loadProgramTransformSaveProgram;

    @Inject
    public ClientTransformController(ServerProperties serverProperties, @Named("robotPluginMap") Map<String, RobotFactory> robotPluginMap) {
        this.feature_Toggle_loadProgramTransformSaveProgram =
            serverProperties.getBooleanProperty("feature-toggle.load-program-transform-save-program");
        this.robotPluginMap = robotPluginMap;
    }

    @POST
    @Path("/program")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response transform(@XsltTrans XsltTransformer xsltTransformer, String toTransform) {
        String transformed = "";
        try {
            toTransform = UtilForHtmlXml.checkProgramTextForXSS(toTransform);
            toTransform = xsltTransformer.transform(toTransform);
            if ( toTransform != null ) {
                Export jaxbImportExport = JaxbHelper.xml2Element(toTransform, Export.class);
                if ( jaxbImportExport != null ) {
                    BlockSet jaxbProgram = jaxbImportExport.getProgram().getBlockSet();
                    BlockSet jaxbConfig = jaxbImportExport.getConfig().getBlockSet();
                    String robotType1 = jaxbProgram.getRobottype();
                    String robotType2 = jaxbConfig.getRobottype();
                    RobotFactory robotFactory = getArbitraryPluginOfAPluginGroup(robotType1, robotPluginMap);
                    if ( robotFactory != null && robotType1.equals(robotType2) ) {
                        String progXml = JaxbHelper.blockSet2xml(jaxbProgram);
                        String configXml = JaxbHelper.blockSet2xml(jaxbConfig);
                        Pair<String, String> progConfPair = UtilForXmlTransformation.transformBetweenVersions(robotFactory, jaxbProgram.getXmlversion(), progXml, configXml);
                        progXml = progConfPair == null ? progXml : progConfPair.getFirst();
                        configXml = progConfPair == null ? configXml : progConfPair.getSecond();
                        transformed = "<export xmlns=\"http://de.fhg.iais.roberta.blockly\"><program>" + progXml + "</program><config>" + configXml + "</config></export>";
                        Statistics.info("ProgramTransForm", "success", true);
                    }
                }
            }
        } catch ( Exception e ) {
            transformed = "";
            Statistics.info("ProgramTransForm", "success", false);
        }
        return Response.ok(transformed).build();
    }

    @POST
    @Path("/idList")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response transformIdList(@XsltTrans XsltTransformer xsltTransformer, @OraData DbSession dbSession, String idsToTransformAsString) {
        if ( !feature_Toggle_loadProgramTransformSaveProgram ) {
            throw new DbcException("feature toggle, that allows program transformations from the db is disabled");
        }
        RobotFactory robotFactory = getArbitraryPluginOfAPluginGroup("calliope2017NoBlue", robotPluginMap);
        String[] idsToTransform = idsToTransformAsString.split("\\s*,\\s*");
        LOG.error("transforming " + idsToTransform.length + " programs from the database");
        ProgramProcessor programProcessor = new ProgramProcessor(dbSession, 0);
        ProgramDao programDao = new ProgramDao(dbSession);
        String transformMessage = "undefined";
        try {
            for ( String idToTransform : idsToTransform ) {
                LOG.error(idToTransform);
                Program programToTransform = programDao.load(Integer.parseInt(idToTransform));
                if ( programToTransform == null ) {
                    throw new DbcException("program from the db to be transformed was not found. Id: " + idToTransform);
                } else {
                    String configName = programToTransform.getConfigName();
                    String programText = programToTransform.getProgramText();
                    String configText = programProcessor.getProgramsConfig(programToTransform);
                    programText = xsltTransformer.transform(programText);
                    if ( configText != null ) {
                        configText = xsltTransformer.transform(configText);
                    }
                    Pair<String, String> progConfPair = UtilForXmlTransformation.transformBetweenVersions(robotFactory, "transform!", programText, configText);
                    programText = progConfPair == null ? programText : progConfPair.getFirst();
                    configText = progConfPair == null ? configText : configName == null && configText == null ? null : progConfPair.getSecond();
                    programProcessor.replaceTransformedProgram(programToTransform, programText, configName, configText); // save the transformed program text and conf
                    Statistics.info("ProgramTransForm", "message", programProcessor.getMessage().toString());
                }
            }
            transformMessage = "\ntransform seems to have been successful";
        } catch ( Exception e ) {
            transformMessage = "\ntransform crashed with exception: " + e.getMessage();
        }
        return Response.ok(transformMessage).build();
    }

    private RobotFactory getArbitraryPluginOfAPluginGroup(String pluginNameOrPluginGroup, Map<String, RobotFactory> robotPluginMap) {
        for ( RobotFactory robotPlugin : robotPluginMap.values() ) {
            if ( pluginNameOrPluginGroup.equals(robotPlugin.getGroup()) ) {
                return robotPlugin;
            }
        }
        return null;
    }

}
