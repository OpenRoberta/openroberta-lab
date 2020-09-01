package de.fhg.iais.roberta.visitor.collect;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
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
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class NaoUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements INaoVisitor<Void> {

    public NaoUsedHardwareCollectorVisitor(
        List<List<Phrase<Void>>> phrasesSet,
        ConfigurationAst configuration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(configuration, beanBuilders);
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
        moveJoint.getDegrees().accept(this);
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        walkDistance.getDistanceToWalk().accept(this);
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        turnDegrees.getDegreesToTurn().accept(this);
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        walkTo.getWalkToTheta().accept(this);
        walkTo.getWalkToX().accept(this);
        walkTo.getWalkToY().accept(this);
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
        pointLookAt.getpointX().accept(this);
        pointLookAt.getpointY().accept(this);
        pointLookAt.getpointZ().accept(this);
        pointLookAt.getSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        setVolume.getVolume().accept(this);
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
        sayTextAction.getMsg().accept(this);
        sayTextAction.getPitch().accept(this);
        sayTextAction.getPitch().accept(this);
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        playFile.getMsg().accept(this);
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        setLeds.getColor().accept(this);
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
        randomEyesDuration.getDuration().accept(this);
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        rastaDuration.getDuration().accept(this);
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor<Void> forceSensor) {
        return null;
    }

    @Override
    public Void visitNaoMark(DetectMarkSensor<Void> naoMark) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.DETECT_MARK, null));
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        takePicture.getPictureName().accept(this);
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        recordVideo.getDuration().accept(this);
        recordVideo.getVideoName().accept(this);
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        learnFace.getFaceName().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        forgetFace.getFaceName().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFaceSensor<Void> detectFace) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().accept(this);
        return null;
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrentSensor<Void> electricCurrent) {
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        setIntensity.getIntensity().accept(this);
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        walkAsync.getXSpeed().accept(this);
        walkAsync.getYSpeed().accept(this);
        walkAsync.getZSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous<Void> autonomous) {
        return null;
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        recognizeWord.getVocabulary().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.NAO_SPEECH, null));
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation<Void> naoMarkInformation) {
        naoMarkInformation.getNaoMarkId().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.DETECT_MARK, null));
        return null;
    }

    @Override
    public Void visitDetecedFaceInformation(DetectedFaceInformation<Void> detectedFaceInformation) {
        detectedFaceInformation.getFaceName().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(null, SC.NAO_FACE, null));
        return null;
    }
}
