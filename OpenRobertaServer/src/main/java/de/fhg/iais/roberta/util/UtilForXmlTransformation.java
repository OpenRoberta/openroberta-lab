package de.fhg.iais.roberta.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class UtilForXmlTransformation {
    private static final Logger LOG = LoggerFactory.getLogger(UtilForXmlTransformation.class);

    // Transform programs with old xml versions to new xml versions
    public static Pair<String, String> transformBetweenVersions(RobotFactory robotFactory, String programText, String configText) {
        if ( robotFactory.hasWorkflow("transform") ) {
            if ( configText == null ) {
                // programs that do not have any configuration modifications are saved into the database without an associated configuration
                // when loaded, the default configuration should be used
                configText = robotFactory.getConfigurationDefault();
            }
            Project project = new Project.Builder().setFactory(robotFactory).setProgramXml(programText).setConfigurationXml(configText).build();
            ProjectService.executeWorkflow("transform", project);
            if ( configText != null ) {
                if ( project.getRobotFactory().getConfigurationType().equals("new") ) {
                    return Pair.of(project.getAnnotatedProgramAsXml(), project.getAnnotatedConfigurationAsXml());
                } else {
                    // old style configurations do not implement a correct backtransformation, return the input instead
                    // however the version needs to be updated anyway
                    // TODO replace all old configurations with new ones
                    if ( configText.contains("xmlversion=\"2.0\"") ) {
                        configText = configText.replace("xmlversion=\"2.0\"", "xmlversion=\"3.0\"");
                    }
                    return Pair.of(project.getAnnotatedProgramAsXml(), configText);
                }
            } else {
                return Pair.of(project.getAnnotatedProgramAsXml(), null);
            }
        } else {
            throw new DbcException("Every robot needs a transform workflow!");
        }
    }
}
