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
    public static Pair<String, String> transformBetweenVersions(RobotFactory robotFactory, String programText, String configText) {
        AliveData.transformerDatabaseLoadsTotal.incrementAndGet();
        if ( robotFactory.hasWorkflow("transform") ) {
            Assert.isTrue(robotFactory.getConfigurationType().equals("new"));
            AliveData.transformerExecutedTransformations.incrementAndGet();
            if ( configText == null ) {
                // programs that do not have any configuration modifications are saved into the database without an associated configuration
                // when loaded, the default configuration should be used
                configText = robotFactory.getConfigurationDefault();
            }
            Project project = new Project.Builder().setFactory(robotFactory).setProgramXml(programText).setConfigurationXml(configText).build();
            ProjectService.executeWorkflow("transform", project);
            return Pair.of(project.getAnnotatedProgramAsXml(), configText == null ? null : project.getAnnotatedConfigurationAsXml());
        } else {
            return null;
        }
    }
}
