package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.IWorker;

public class WorkflowTestHelper {

    private RobotFactory robotFactory;
    protected List<IWorker> workerChain;
    protected List<Phrase<Void>> phrases;
    protected List<ConfigurationComponent> configurationComponents;

    @Before
    public void setUp() throws Exception {
        StmtList<Void> variables = StmtList.make();
        variables.setReadOnly();

        MainTask<Void> mainTask = MainTask.make(variables, "false", BlocklyBlockProperties.make("1", "1"), null);
        phrases = new ArrayList<>(Arrays.asList(new Location<>("0", "0"), mainTask));
        configurationComponents = new ArrayList<>();
    }

    protected void setupRobotFactory(String robotName) {
        robotFactory = Util.configureRobotPlugin(robotName, "", "", Collections.emptyList());
    }

    protected Project executeWorkflow() {
        ProgramAst<Void> programAst = new ProgramAst.Builder<Void>()
            .setRobotType(robotFactory.getGroup())
            .addToTree(phrases)
            .build();

        ConfigurationAst configurationAst = new ConfigurationAst.Builder()
            .setRobotType(robotFactory.getGroup())
            .addComponents(configurationComponents)
            .build();

        configurationAst.setRobotName(robotFactory.getPluginProperties().getRobotName());

        Project project = new Project.Builder()
            .setFactory(robotFactory)
            .setProgramAst(programAst)
            .setConfigurationAst(configurationAst)
            .build();

        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workerChain ) {
                worker.execute(project);
                if ( !project.hasSucceeded() ) {
                    break;
                }
            }
        }
        return project;
    }

    protected Project executeWorkflow(String workflowName) {
        ProgramAst<Void> programAst = new ProgramAst.Builder<Void>()
            .setRobotType(robotFactory.getGroup())
            .addToTree(phrases)
            .build();

        ConfigurationAst configurationAst = new ConfigurationAst.Builder()
            .setRobotType(robotFactory.getGroup())
            .addComponents(configurationComponents)
            .build();

        Project project = new Project.Builder()
            .setFactory(robotFactory)
            .setProgramAst(programAst)
            .setConfigurationAst(configurationAst)
            .build();

        UnitTestHelper.executeWorkflow(workflowName, robotFactory, project);
        return project;
    }

    protected final static BlocklyBlockProperties bp = BlocklyBlockProperties.make("1", "1");

    protected void assertHasUsedSensor(Project project, String userDefinedPort, String type, String mode) {
        UsedHardwareBean usedHardwareBean = getUsedHardwareBean(project);
        assertHasUsedSensor(usedHardwareBean, userDefinedPort, type, mode);
    }

    protected void assertHasNoNepoInfo(Phrase<Void> phrase) {
        Assertions
            .assertThat(phrase
                .getInfos()
                .getInfos())
            .isEmpty();
    }

    protected void assertHasNepoInfo(Phrase<Void> phrase, NepoInfo.Severity severity, String message) {
        Assertions
            .assertThat(phrase
                .getInfos()
                .getInfos())
            .as(String.format("Phrase %s should have NepoInfo %s with message \"%s\"", phrase, severity, message))
            .anySatisfy((nepoInfo) -> {
                Assertions
                    .assertThat(nepoInfo.getMessage())
                    .isEqualTo(message);
                Assertions
                    .assertThat(nepoInfo.getSeverity())
                    .isEqualTo(severity);
            });
    }

    private UsedHardwareBean getUsedHardwareBean(Project project) {
        return project.getWorkerResult(UsedHardwareBean.class);
    }

    private void assertHasUsedSensor(UsedHardwareBean usedHardwareBean, String userDefinedPort, String type, String mode) {
        Assertions
            .assertThat(usedHardwareBean.getUsedSensors())
            .anySatisfy(usedSensor -> {
                Assertions
                    .assertThat(usedSensor.getPort())
                    .isEqualTo(userDefinedPort);
                Assertions
                    .assertThat(usedSensor.getMode())
                    .isEqualTo(mode);
                Assertions
                    .assertThat(usedSensor.getType())
                    .isEqualTo(type);
            });
    }

    private void assertHasUsedActor(Project project, String userDefinedPort, String type) {
        UsedHardwareBean usedHardwareBean = getUsedHardwareBean(project);
        assertHasUsedActor(usedHardwareBean, userDefinedPort, type);
    }

    private void assertHasUsedActor(UsedHardwareBean usedHardwareBean, String userDefinedPort, String type) {
        Assertions
            .assertThat(usedHardwareBean.getUsedActors())
            .anySatisfy(usedActor -> {
                Assertions
                    .assertThat(usedActor.getPort())
                    .isEqualTo(userDefinedPort);
                Assertions
                    .assertThat(usedActor.getType())
                    .isEqualTo(type);
            });
    }

}
