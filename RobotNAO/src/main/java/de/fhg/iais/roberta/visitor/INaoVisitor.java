package de.fhg.iais.roberta.visitor;

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
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
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

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface INaoVisitor<V> extends IVisitor<V> {

    V visitTouchSensor(TouchSensor touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitAccelerometerSensor(AccelerometerSensor accelerometerSensor);

    V visitSetIntensity(SetIntensity setIntensity);

    V visitFsrSensor(FsrSensor forceSensor);

    V visitHand(Hand hand);

    V visitMoveJoint(MoveJoint moveJoint);

    V visitWalkDistance(WalkDistance walkDistance);

    V visitTurnDegrees(TurnDegrees turnDegrees);

    V visitSetLeds(SetLeds setLeds);

    V visitLedOff(LedOff ledOff);

    V visitLedReset(LedReset ledReset);

    V visitWalkAsync(WalkAsync walkAsync);

    V visitSetMode(SetMode setMode);

    V visitApplyPosture(ApplyPosture applyPosture);

    V visitSetStiffness(SetStiffness setStiffness);

    V visitAutonomous(Autonomous autonomous);

    V visitWalkTo(WalkTo walkTo);

    V visitStop(Stop stop);

    V visitAnimation(Animation animation);

    V visitPointLookAt(PointLookAt pointLookAt);

    V visitSetVolume(SetVolume setVolume);

    V visitGetVolume(GetVolume getVolume);

    V visitSetLanguageAction(SetLanguageAction setLanguageAction);

    V visitGetLanguage(GetLanguage getLanguage);

    V visitSayTextAction(SayTextAction sayTextAction);

    V visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction);

    V visitPlayFile(PlayFile playFile);

    V visitRandomEyesDuration(RandomEyesDuration randomEyesDuration);

    V visitRastaDuration(RastaDuration rastaDuration);

    V visitDetectMarkSensor(DetectMarkSensor detectedMark);

    V visitTakePicture(TakePicture takePicture);

    V visitRecordVideo(RecordVideo recordVideo);

    V visitLearnFace(LearnFace learnFace);

    V visitForgetFace(ForgetFace forgetFace);

    V visitDetectFaceSensor(DetectFaceSensor detectFace);

    V visitElectricCurrentSensor(ElectricCurrentSensor electricCurrent);

    V visitRecognizeWord(RecognizeWord recognizeWord);

    V visitNaoMarkInformation(NaoMarkInformation naoMarkInformation);

    V visitDetectedFaceInformation(DetectedFaceInformation detectedFaceInformation);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTimerReset(TimerReset timerReset);

    V visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct);

    V visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct);

    V visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct);

    V visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct);

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }
}