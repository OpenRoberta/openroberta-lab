package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

public class FestobionicflowerUsedMethodCollectorVisitor extends ArduinoUsedMethodCollectorVisitor implements IFestobionicflowerVisitor<Void> {
	public FestobionicflowerUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
		super(builder);
	}

	@Override
	public Void visit(Phrase<Void> visitable) {
		return null;
	}

	@Override
	public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
		return null;
	}

	@Override
	public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
		return null;
	}

	@Override
	public Void visitLightSensor(LightSensor<Void> lightSensor) {
		return null;
	}

	@Override
	public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
		return null;
	}

	@Override
	public Void visitTouchSensor(TouchSensor<Void> keysSensor) {
		return null;
	}
}
