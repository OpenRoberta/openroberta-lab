package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.OtherSensor;
import de.fhg.iais.roberta.syntax.Sensor;

public class VisitorForBaseVisitorTest extends BaseVisitor<Void> {
    public Void visitSensor(Sensor sensor) {
        return null;
    }

    private Void visitOtherSensor(OtherSensor sensor) {
        return null;
    }
}
