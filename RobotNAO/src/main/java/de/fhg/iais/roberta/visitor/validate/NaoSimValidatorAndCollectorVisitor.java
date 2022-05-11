package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.Autonomous;
import de.fhg.iais.roberta.syntax.action.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LearnFace;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SetIntensity;
import de.fhg.iais.roberta.syntax.action.nao.SetLeds;
import de.fhg.iais.roberta.syntax.action.nao.SetMode;
import de.fhg.iais.roberta.syntax.action.nao.SetStiffness;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TakePicture;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkAsync;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.visitor.INaoVisitor;
import de.fhg.iais.roberta.visitor.NaoSimMethods;

public class NaoSimValidatorAndCollectorVisitor extends NaoValidatorAndCollectorVisitor implements INaoVisitor<Void> {

    public static final List<String> VALID_TOUCH_SENSOR_PORTS = Collections.singletonList("BUMPER");
    public static final List<String> VALID_TOUCH_SENSOR_SLOTS = Arrays.asList("LEFT", "RIGHT");

    public NaoSimValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
        usedMethodBuilder.addUsedMethod(NaoSimMethods.WAIT_TIME);
        usedMethodBuilder.addUsedMethod(NaoSimMethods.RESET_POSE);
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        boolean isPortValid = VALID_TOUCH_SENSOR_PORTS.stream().anyMatch(str -> str.equalsIgnoreCase(touchSensor.getUserDefinedPort()));
        boolean isSlotValid = VALID_TOUCH_SENSOR_SLOTS.stream().anyMatch(str -> str.equalsIgnoreCase(touchSensor.getSlot()));

        if ( !isPortValid || !isSlotValid ) {
            addErrorToPhrase(touchSensor, "SIM_BLOCK_NOT_SUPPORTED");
        } else {
            usedMethodBuilder.addUsedMethod(NaoSimMethods.IS_TOUCHED);
        }
        return super.visitTouchSensor(touchSensor);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_ULTRASONIC);
        return super.visitUltrasonicSensor(ultrasonicSensor);
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        switch ( gyroSensor.getSlot().toUpperCase() ) {
            case "X":
                usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_GYRO_X);
                break;
            case "Y":
                usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_GYRO_Y);
                break;
        }
        return super.visitGyroSensor(gyroSensor);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        switch ( accelerometerSensor.getUserDefinedPort().toUpperCase() ) {
            case "X":
                usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_ACCELEROMETER_X);
                break;
            case "Y":
                usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_ACCELEROMETER_Y);
                break;
            case "Z":
                usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_ACCELEROMETER_Z);
                break;
        }
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitSetIntensity(SetIntensity setIntensity) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_LED);
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_INTENSITY);
        return super.visitSetIntensity(setIntensity);
    }

    @Override
    public Void visitFsrSensor(FsrSensor forceSensor) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_FORCE);
        return super.visitFsrSensor(forceSensor);
    }

    @Override
    public Void visitHand(Hand hand) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.MOVE_HAND_JOINT);
        return super.visitHand(hand);
    }

    @Override
    public Void visitMoveJoint(MoveJoint moveJoint) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.MOVE_JOINT);
        return super.visitMoveJoint(moveJoint);
    }

    @Override
    public Void visitWalkDistance(WalkDistance walkDistance) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.WALK_DISTANCE);
        usedMethodBuilder.addUsedMethod(NaoSimMethods.CALC_DIST);
        usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_FORCE);
        return super.visitWalkDistance(walkDistance);
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees turnDegrees) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.TURN);
        return super.visitTurnDegrees(turnDegrees);
    }

    @Override
    public Void visitSetLeds(SetLeds setLeds) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_LED);
        return super.visitSetLeds(setLeds);
    }

    @Override
    public Void visitLedOff(LedOff ledOff) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_LED);
        return super.visitLedOff(ledOff);
    }

    @Override
    public Void visitLedReset(LedReset ledReset) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.LED_OFF);
        return super.visitLedReset(ledReset);
    }

    @Override
    public Void visitWalkAsync(WalkAsync walkAsync) {
        addWarningToPhrase(walkAsync, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitWalkAsync(walkAsync);
    }

    @Override
    public Void visitSetMode(SetMode setMode) {
        addWarningToPhrase(setMode, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitSetMode(setMode);
    }

    @Override
    public Void visitApplyPosture(ApplyPosture applyPosture) {
        switch ( applyPosture.posture ) {
            case "SITRELAX":
            case "SIT":
            case "LYINGBELLY":
            case "LYINGBACK":
                addWarningToPhrase(applyPosture, "SIM_BLOCK_NOT_SUPPORTED");
                break;
            default:
                usedMethodBuilder.addUsedMethod(NaoSimMethods.ANIMATION);
                usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_LED);
                break;
        }
        return super.visitApplyPosture(applyPosture);
    }

    @Override
    public Void visitSetStiffness(SetStiffness setStiffness) {
        addWarningToPhrase(setStiffness, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitSetStiffness(setStiffness);
    }

    @Override
    public Void visitAutonomous(Autonomous autonomous) {
        addWarningToPhrase(autonomous, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitAutonomous(autonomous);
    }

    @Override
    public Void visitWalkTo(WalkTo walkTo) {
        addWarningToPhrase(walkTo, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitWalkTo(walkTo);
    }

    @Override
    public Void visitStop(Stop stop) {
        addWarningToPhrase(stop, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitStop(stop);
    }

    @Override
    public Void visitAnimation(Animation animation) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.ANIMATION);
        if ( Objects.equals(animation.move, "BLINK") ) {
            usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_LED);
        }
        return super.visitAnimation(animation);
    }

    @Override
    public Void visitPointLookAt(PointLookAt pointLookAt) {
        addWarningToPhrase(pointLookAt, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitPointLookAt(pointLookAt);
    }

    @Override
    public Void visitSetVolume(SetVolume setVolume) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_VOLUME);
        return super.visitSetVolume(setVolume);
    }

    @Override
    public Void visitGetVolume(GetVolume getVolume) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_VOLUME);
        return super.visitGetVolume(getVolume);
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SET_LANGUAGE);
        return super.visitSetLanguageAction(setLanguageAction);
    }

    @Override
    public Void visitGetLanguage(GetLanguage getLanguage) {
        addErrorToPhrase(getLanguage, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitGetLanguage(getLanguage);
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SAY_TEXT);
        return super.visitSayTextAction(sayTextAction);
    }

    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.SAY_TEXT);
        return super.visitSayTextWithSpeedAndPitchAction(sayTextAction);
    }

    @Override
    public Void visitPlayFile(PlayFile playFile) {
        addWarningToPhrase(playFile, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitPlayFile(playFile);
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration randomEyesDuration) {
        addWarningToPhrase(randomEyesDuration, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRandomEyesDuration(randomEyesDuration);
    }

    @Override
    public Void visitRastaDuration(RastaDuration rastaDuration) {
        addWarningToPhrase(rastaDuration, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRastaDuration(rastaDuration);
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectedMark) {
        addErrorToPhrase(detectedMark, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitDetectMarkSensor(detectedMark);
    }

    @Override
    public Void visitTakePicture(TakePicture takePicture) {
        addWarningToPhrase(takePicture, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitTakePicture(takePicture);
    }

    @Override
    public Void visitRecordVideo(RecordVideo recordVideo) {
        addWarningToPhrase(recordVideo, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRecordVideo(recordVideo);
    }

    @Override
    public Void visitLearnFace(LearnFace learnFace) {
        addErrorToPhrase(learnFace, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitLearnFace(learnFace);
    }

    @Override
    public Void visitForgetFace(ForgetFace forgetFace) {
        addWarningToPhrase(forgetFace, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitForgetFace(forgetFace);
    }

    @Override
    public Void visitDetectFaceSensor(DetectFaceSensor detectFace) {
        addErrorToPhrase(detectFace, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitDetectFaceSensor(detectFace);
    }

    @Override
    public Void visitElectricCurrentSensor(ElectricCurrentSensor electricCurrent) {
        addErrorToPhrase(electricCurrent, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitElectricCurrentSensor(electricCurrent);
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord recognizeWord) {
        usedMethodBuilder.addUsedMethod(NaoSimMethods.GET_RECOGNIZED_WORD);
        return super.visitRecognizeWord(recognizeWord);
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation naoMarkInformation) {
        addErrorToPhrase(naoMarkInformation, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitNaoMarkInformation(naoMarkInformation);
    }

    @Override
    public Void visitDetectedFaceInformation(DetectedFaceInformation detectedFaceInformation) {
        addErrorToPhrase(detectedFaceInformation, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitDetectedFaceInformation(detectedFaceInformation);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        addErrorToPhrase(timerSensor, "BLOCK_NOT_SUPPORTED");
        return super.visitTimerSensor(timerSensor);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        addErrorToPhrase(timerReset, "BLOCK_NOT_SUPPORTED");
        return super.visitTimerReset(timerReset);
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        addWarningToPhrase(mathCastStringFunct, "BLOCK_NOT_SUPPORTED");
        return super.visitMathCastStringFunct(mathCastStringFunct);
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        addWarningToPhrase(mathCastCharFunct, "BLOCK_NOT_SUPPORTED");
        return super.visitMathCastCharFunct(mathCastCharFunct);
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        addWarningToPhrase(textStringCastNumberFunct, "BLOCK_NOT_SUPPORTED");
        return super.visitTextStringCastNumberFunct(textStringCastNumberFunct);
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        addWarningToPhrase(textCharCastNumberFunct, "BLOCK_NOT_SUPPORTED");
        return super.visitTextCharCastNumberFunct(textCharCastNumberFunct);
    }
}
