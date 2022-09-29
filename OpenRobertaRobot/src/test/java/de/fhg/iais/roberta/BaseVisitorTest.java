package de.fhg.iais.roberta;


import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Actor;
import de.fhg.iais.roberta.syntax.OtherSensor;
import de.fhg.iais.roberta.syntax.Sensor;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.VisitorForBaseVisitorTest;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BaseVisitorTest {

    @BeforeClass
    public static void setupPhrases() {
        AstFactory.add(Sensor.class);
        AstFactory.add(OtherSensor.class);
        AstFactory.add(Actor.class);
    }

    private VisitorForBaseVisitorTest visitor;

    @Before
    public void setUp() throws Exception {
        visitor = spy(new VisitorForBaseVisitorTest());
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
            .hasMessageContaining("not found for phrase")
            .hasMessageContaining("OtherSensor");
    }

    @Test
    public void throwsWhenMessageNotThereTest() {
        Actor actor = new Actor();
        Assertions.assertThatThrownBy(() -> visitor.visit(actor))
            .isInstanceOf(DbcException.class)
            .hasCauseInstanceOf(NoSuchMethodException.class)
            .hasMessageContaining("not found for phrase")
            .hasMessageContaining("Actor");
    }

    @Test
    public void passesThroughExceptionTest() {
        Sensor sensor = new Sensor();
        DbcException exception = new DbcException("unsupported Configuration");
        doThrow(exception).when(visitor).visitSensor(sensor);

        Assertions.assertThatThrownBy(() -> visitor.visit(sensor))
            .isEqualTo(exception);
    }

}