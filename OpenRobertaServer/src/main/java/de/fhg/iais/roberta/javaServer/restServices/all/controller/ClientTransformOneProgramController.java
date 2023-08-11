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
import de.fhg.iais.roberta.blockly.generated.Config;
import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.provider.XsltTrans;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.XsltAndJavaTransformer;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

@Path("/transform")
public class ClientTransformOneProgramController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientTransformOneProgramController.class);

    private final Map<String, RobotFactory> robotPluginMap;

    @Inject
    public ClientTransformOneProgramController(@Named("robotPluginMap") Map<String, RobotFactory> robotPluginMap) {
        this.robotPluginMap = robotPluginMap;
    }

    @POST
    @Path("/program")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response transform(@XsltTrans XsltAndJavaTransformer xsltAndJavaTransformer, String exportAsString) {
        try {
            exportAsString = UtilForHtmlXml.checkProgramTextForXSS(exportAsString);
            Export exportAsElement = JaxbHelper.xml2Element(exportAsString, Export.class);
            BlockSet programAsBlockSet = exportAsElement.getProgram().getBlockSet();
            BlockSet configAsBlockSet = exportAsElement.getConfig().getBlockSet();
            String robotType1 = programAsBlockSet.getRobottype();
            String robotType2 = configAsBlockSet.getRobottype();
            RobotFactory robotFactory = getArbitraryPluginOfAPluginGroup(robotType1, robotPluginMap);
            if ( robotFactory != null && robotType1.equals(robotType2) ) {
                String progAsString = JaxbHelper.blockSet2xml(programAsBlockSet);
                String configAsString = JaxbHelper.blockSet2xml(configAsBlockSet);
                Pair<String, String> transformed = xsltAndJavaTransformer.transform(robotFactory, progAsString, configAsString);
                progAsString = transformed.getFirst();
                configAsString = transformed.getSecond();
                de.fhg.iais.roberta.blockly.generated.Program programAsElement = JaxbHelper.xml2Element(progAsString, de.fhg.iais.roberta.blockly.generated.Program.class);
                Config configAsElement = JaxbHelper.xml2Element(configAsString, Config.class);
                exportAsElement = new Export();
                exportAsElement.setProgram(programAsElement);
                exportAsElement.setConfig((configAsElement));
                exportAsString = "<export xmlns=\"http://de.fhg.iais.roberta.blockly\"><program>" + progAsString + "</program><config>" + configAsString + "</config></export>";
                Statistics.info("ProgramTransForm", "success", true);
                return Response.ok().build();
            }
        } catch ( Exception e ) {
            LOG.error("transform failure", e);
            Statistics.info("ProgramTransForm", "success", false);
        }
        return Response.serverError().build();
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
