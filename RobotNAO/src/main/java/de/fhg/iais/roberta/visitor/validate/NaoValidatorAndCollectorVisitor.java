package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
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
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.INaoVisitor;

public class NaoValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements INaoVisitor<Void> {

    public NaoValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        requiredComponentVisited(setIntensity, setIntensity.Intensity);
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor<Void> forceSensor) {
        return null;
    }

    @Override
    public Void visitHand(Hand<Void> hand) {
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        requiredComponentVisited(moveJoint, moveJoint.degrees);
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        requiredComponentVisited(walkDistance, walkDistance.distanceToWalk);
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        requiredComponentVisited(turnDegrees, turnDegrees.degreesToTurn);
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        requiredComponentVisited(setLeds, setLeds.Color);
        return null;
    }

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        requiredComponentVisited(walkAsync, walkAsync.XSpeed, walkAsync.YSpeed, walkAsync.ZSpeed);
        return null;
    }

    @Override
    public Void visitSetMode(SetMode<Void> setMode) {
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous<Void> autonomous) {
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        requiredComponentVisited(walkTo, walkTo.walkToX, walkTo.walkToY, walkTo.walkToTheta);
        return null;
    }

    @Override
    public Void visitStop(Stop<Void> stop) {
        return null;
    }

    @Override
    public Void visitAnimation(Animation<Void> animation) {
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        requiredComponentVisited(pointLookAt, pointLookAt.pointX, pointLookAt.pointY, pointLookAt.pointZ, pointLookAt.speed);
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        requiredComponentVisited(setVolume, setVolume.volume);
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        requiredComponentVisited(sayTextAction, sayTextAction.msg);
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction<Void> sayTextAction) {
        requiredComponentVisited(sayTextAction, sayTextAction.msg);
        requiredComponentVisited(sayTextAction, sayTextAction.pitch);
        requiredComponentVisited(sayTextAction, sayTextAction.speed);
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        requiredComponentVisited(playFile, playFile.msg);
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        requiredComponentVisited(randomEyesDuration, randomEyesDuration.duration);
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        requiredComponentVisited(rastaDuration, rastaDuration.duration);
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor<Void> detectedMark) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.DETECT_MARK, null));
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        requiredComponentVisited(takePicture, takePicture.pictureName);
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        requiredComponentVisited(recordVideo, recordVideo.videoName, recordVideo.duration);
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        requiredComponentVisited(learnFace, learnFace.faceName);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        requiredComponentVisited(forgetFace, forgetFace.faceName);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitDetectFaceSensor(DetectFaceSensor<Void> detectFace) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitElectricCurrentSensor(ElectricCurrentSensor<Void> electricCurrent) {
        return null;
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        requiredComponentVisited(recognizeWord, recognizeWord.vocabulary);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.NAO_SPEECH, null));
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation<Void> naoMarkInformation) {
        requiredComponentVisited(naoMarkInformation, naoMarkInformation.naoMarkId);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.DETECT_MARK, null));
        return null;
    }

    @Override
    public Void visitDetectedFaceInformation(DetectedFaceInformation<Void> detectedFaceInformation) {
        requiredComponentVisited(detectedFaceInformation, detectedFaceInformation.faceName);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        addWarningToPhrase(timerSensor, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        addWarningToPhrase(mathCastStringFunct, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        addWarningToPhrase(mathCastCharFunct, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        addWarningToPhrase(textStringCastNumberFunct, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        addWarningToPhrase(textCharCastNumberFunct, "BLOCK_NOT_SUPPORTED");
        return null;
    }
}
