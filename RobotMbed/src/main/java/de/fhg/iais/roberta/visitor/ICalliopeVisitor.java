package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface ICalliopeVisitor<V> extends IMbedVisitor<V> {
    V visitLightStatusAction(LightStatusAction lightStatusAction);

    V visitMotorOnAction(MotorOnAction motorOnAction);

    V visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction);

    V visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction);

    V visitMotorStopAction(MotorStopAction motorStopAction);

    V visitSoundSensor(SoundSensor microphoneSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitLedOnAction(LedOnAction ledOnAction);

    V visitLightAction(LightAction lightAction);

    V visitRadioRssiSensor(RadioRssiSensor radioRssiSensor);

    V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction);

    V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction);

    V visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction);

    V visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction);

    V visitLedBarSetAction(LedBarSetAction ledBarSetAction);

    V visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction);

    V visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction);

    V visitHumiditySensor(HumiditySensor humiditySensor);

    V visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction);

    V visitServoSetAction(ServoSetAction servoSetAction);

    V visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction);

    V visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction);

    V visitColorSensor(ColorSensor colorSensor);
}