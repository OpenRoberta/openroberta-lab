package de.fhg.iais.roberta.visitor;

import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class ArduinoValidatorAndCollectorWorkflowTest extends WorkflowTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
        setupRobotFactory("uno");
    }

    @Test
    public void visitKeysSensor_withoutPort() {
        KeysSensor<Void> keysSensor = new KeysSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(keysSensor);

        executeWorkflow("showsource");

        assertPhraseNepoInfo(keysSensor, NepoInfo.Severity.ERROR, "CONFIGURATION_ERROR_SENSOR_MISSING");
    }

    @Test
    public void visitKeysSensor_withPort() {
        configurationComponents.add(new ConfigurationComponent(SC.KEY, false, "P1", "P1", new HashMap<>()));

        KeysSensor<Void> keysSensor = new KeysSensor<>(new SensorMetaDataBean("P1", "", "", null));
        phrases.add(keysSensor);

        executeWorkflow("showsource");

        assertHasNoNepoInfo(keysSensor);
    }

    private void assertHasNoNepoInfo(Phrase<Void> phrase) {
        Assertions.assertThat(phrase.getInfos().getInfos()).isEmpty();
    }

    private void assertPhraseNepoInfo(Phrase<Void> phrase, NepoInfo.Severity severity, String message) {
        Assertions.assertThat(phrase.getInfos().getInfos())
            .anySatisfy((nepoInfo) -> {
                Assertions.assertThat(nepoInfo.getMessage()).isEqualTo(message);
                Assertions.assertThat(nepoInfo.getSeverity()).isEqualTo(severity);
            });
    }
}