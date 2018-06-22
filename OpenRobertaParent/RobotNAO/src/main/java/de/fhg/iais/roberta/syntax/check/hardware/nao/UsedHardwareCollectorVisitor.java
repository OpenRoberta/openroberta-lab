package de.fhg.iais.roberta.syntax.check.hardware.nao;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.nao.SensorType;
import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.nao.ColorHexString;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedMark;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrent;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor implements NaoAstVisitor<Void> {

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    @Override
    public Void visitSetMode(SetMode<Void> mode) {
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
    public Void visitHand(Hand<Void> hand) {
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        moveJoint.getDegrees().visit(this);
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        walkDistance.getDistanceToWalk().visit(this);
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        turnDegrees.getDegreesToTurn().visit(this);
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        walkTo.getWalkToTheta().visit(this);
        walkTo.getWalkToX().visit(this);
        walkTo.getWalkToY().visit(this);
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
        pointLookAt.getpointX().visit(this);
        pointLookAt.getpointY().visit(this);
        pointLookAt.getpointZ().visit(this);
        pointLookAt.getSpeed().visit(this);
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        setVolume.getVolume().visit(this);
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguage) {
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        sayTextAction.getMsg().visit(this);
        sayTextAction.getPitch().visit(this);
        sayTextAction.getPitch().visit(this);
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        playFile.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        setLeds.getColor().visit(this);
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
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        randomEyesDuration.getDuration().visit(this);
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        rastaDuration.getDuration().visit(this);
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor<Void> forceSensor) {
        return null;
    }

    @Override
    public Void visitNaoMark(DetectedMark<Void> naoMark) {
        this.usedSensors.add(new UsedSensor(null, SensorType.DETECT_MARK, null));
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        takePicture.getPictureName().visit(this);
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        recordVideo.getDuration().visit(this);
        recordVideo.getVideoName().visit(this);
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        learnFace.getFaceName().visit(this);
        this.usedSensors.add(new UsedSensor(null, SensorType.NAOFACE, null));
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        forgetFace.getFaceName().visit(this);
        this.usedSensors.add(new UsedSensor(null, SensorType.NAOFACE, null));
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {
        this.usedSensors.add(new UsedSensor(null, SensorType.NAOFACE, null));
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrent<Void> electricCurrent) {
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        setIntensity.getIntensity().visit(this);
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        walkAsync.getXSpeed().visit(this);
        walkAsync.getYSpeed().visit(this);
        walkAsync.getZSpeed().visit(this);
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous<Void> autonomous) {
        return null;
    }

    @Override
    public Void visitColorHexString(ColorHexString<Void> colorHexString) {
        return null;
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        recognizeWord.getVocabulary().visit(this);
        this.usedSensors.add(new UsedSensor(null, SensorType.NAOSPEECH, null));
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation<Void> naoMarkInformation) {
        naoMarkInformation.getNaoMarkId().visit(this);
        this.usedSensors.add(new UsedSensor(null, SensorType.DETECT_MARK, null));
        return null;
    }

    @Override
    public Void visitDetecedFaceInformation(DetectedFaceInformation<Void> detectedFaceInformation) {
        detectedFaceInformation.getFaceName().visit(this);
        this.usedSensors.add(new UsedSensor(null, SensorType.NAOFACE, null));
        return null;
    }
}
