package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.actor.light.LedsOffAction;
import de.fhg.iais.roberta.syntax.actor.thymio.LedButtonOnAction;
import de.fhg.iais.roberta.syntax.actor.thymio.LedCircleOnAction;
import de.fhg.iais.roberta.syntax.actor.thymio.LedProxHOnAction;
import de.fhg.iais.roberta.syntax.actor.thymio.LedProxVOnAction;
import de.fhg.iais.roberta.syntax.actor.thymio.LedSoundOnAction;
import de.fhg.iais.roberta.syntax.actor.thymio.LedTemperatureOnAction;
import de.fhg.iais.roberta.syntax.actor.thymio.PlayRecordingAction;
import de.fhg.iais.roberta.syntax.actor.thymio.RecordStartAction;
import de.fhg.iais.roberta.syntax.actor.thymio.RecordStopAction;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.thymio.TapSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public interface IThymioVisitor<V> extends IActors4AutonomousDriveRobots<V>, ISensorVisitor<V> {
    V visitPlayRecordingAction(PlayRecordingAction playRecordingAction);

    V visitLedButtonOnAction(LedButtonOnAction ledButtonOnAction);

    V visitLedCircleOnAction(LedCircleOnAction ledCircleOnAction);

    V visitLedSoundOnAction(LedSoundOnAction ledSoundOnAction);

    V visitLedTemperatureOnAction(LedTemperatureOnAction ledTemperatureOnAction);

    V visitLedProxHOnAction(LedProxHOnAction ledProxHOnAction);

    V visitLedProxVOnAction(LedProxVOnAction ledProxVOnAction);

    V visitLedsOffAction(LedsOffAction ledsOffAction);

    V visitTapSensor(TapSensor tapSensor);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitRecordStartAction(RecordStartAction recordStartAction);

    V visitRecordStopAction(RecordStopAction recordStopAction);
}
