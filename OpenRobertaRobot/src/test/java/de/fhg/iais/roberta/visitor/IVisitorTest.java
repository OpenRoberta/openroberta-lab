package de.fhg.iais.roberta.visitor;


import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class IVisitorTest {

    private SpecificVisitor visitor;

    @Before
    public void setUp() throws Exception {
        visitor = spy(new SpecificVisitor());
    }

    @Test
    public void callsMethodTest() {
        Sensor sensor = new Sensor();
        visitor.visit(sensor);
        verify(visitor, times(1)).visitSensor(sensor);
    }

    @Test
    public void throwsWhenMethodPrivateTest() {
        OtherSensor otherSensor = new OtherSensor();
        Assertions.assertThatThrownBy(() -> visitor.visit(otherSensor))
            .isInstanceOf(DbcException.class)
            .hasCauseInstanceOf(NoSuchMethodException.class)
            .hasMessageContaining("not supported")
            .hasMessageContaining("visitOtherSensor");
    }

    @Test
    public void throwsWhenMessageNotThereTest() {
        Actor actor = new Actor();
        Assertions.assertThatThrownBy(() -> visitor.visit(actor))
            .isInstanceOf(DbcException.class)
            .hasCauseInstanceOf(NoSuchMethodException.class)
            .hasMessageContaining("not supported")
            .hasMessageContaining("visitActor");
    }

    @Test
    public void passesThroughExceptionTest() {
        Sensor sensor = new Sensor();
        DbcException exception = new DbcException("unsupported Configuration");
        doThrow(exception).when(visitor).visitSensor(sensor);

        Assertions.assertThatThrownBy(() -> visitor.visit(sensor))
            .isEqualTo(exception);
    }

    public static class SpecificVisitor implements IVisitor<Void> {
        public Void visitSensor(Sensor sensor) {
            return null;
        }

        private Void visitOtherSensor(OtherSensor sensor) {
            return null;
        }
    }

    public static class Sensor extends Phrase<Void> {
        public Sensor() {
            super(null, BlocklyBlockProperties.make("SENSOR", "sensor"), null);
        }
    }

    public static class Actor extends Phrase<Void> {
        public Actor() {
            super(null, BlocklyBlockProperties.make("ACTOR", "actor"), null);
        }
    }

    private static class OtherSensor extends Phrase<Void>{
        public OtherSensor() {
            super(null, BlocklyBlockProperties.make("OTHER_SENSOR", "other_sensor"), null);
        }
    }
}