package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;

public class MbedUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IMbedCollectorVisitor {
    public MbedUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        String mode = gestureSensor.getMode();
        if ( mode.equals("SHAKE") ) {
            this.builder.addUsedMethod(CalliopeMethods.IS_GESTURE_SHAKE);
        }
        return super.visitGestureSensor(gestureSensor);
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        sensorGetSample.getSensor().accept(this);
        return super.visitGetSampleSensor(sensorGetSample);
    }

}
