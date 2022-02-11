package de.fhg.iais.roberta.visitor;

import org.assertj.core.api.Assertions;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class AbstractValidatorAndCollectorWorkflowTest extends WorkflowTest {
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
