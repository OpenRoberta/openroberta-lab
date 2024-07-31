package de.fhg.iais.roberta.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

public class UtilForXmlTransformation {
    private static final Logger LOG = LoggerFactory.getLogger(UtilForXmlTransformation.class);

    // Transform programs with old xml versions to new xml versions, currently only mbed systems
    public static Pair<String, String> transformBetweenVersions(RobotFactory robotFactory, String xmlVersion, String programText, String configText) {
        if ( robotFactory.hasWorkflow("transform") && !"3.1".equals(xmlVersion) ) {
            AliveData.transformerExecutedTransformations.incrementAndGet();
            Assert.isTrue(robotFactory.getConfigurationType().equals("new"));
            Project project = new Project.Builder()
                .setFactory(robotFactory)
                .setProgramXml(programText)
                .setConfigurationXml(configText == null ? robotFactory.getConfigurationDefault() : configText)
                .build();
            ProjectService.executeWorkflow("transform", project);
            return Pair.of(project.getProgramAsBlocklyXML(), configText == null ? null : project.getConfigurationAsBlocklyXML());
        } else {
            return null;
        }
    }
}
