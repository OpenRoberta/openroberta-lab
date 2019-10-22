package de.fhg.iais.roberta.visitor.collect;

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
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * Collector for the NAO.
 * Adds the blocks missing from the defaults of {@link ICollectorVisitor}.
 * Defines the specific parent implementation to use (the one of the collector) due to unrelated defaults.
 */
public interface INaoCollectorVisitor extends ICollectorVisitor, INaoVisitor<Void> {

    @Override
    default Void visitSetMode(SetMode<Void> mode) {
        return null;
    }

    @Override
    default Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        return null;
    }

    @Override
    default Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        return null;
    }

    @Override
    default Void visitHand(Hand<Void> hand) {
        return null;
    }

    @Override
    default Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        moveJoint.getDegrees().accept(this);
        return null;
    }

    @Override
    default Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        walkDistance.getDistanceToWalk().accept(this);
        return null;
    }

    @Override
    default Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        turnDegrees.getDegreesToTurn().accept(this);
        return null;
    }

    @Override
    default Void visitWalkTo(WalkTo<Void> walkTo) {
        walkTo.getWalkToTheta().accept(this);
        walkTo.getWalkToX().accept(this);
        walkTo.getWalkToY().accept(this);
        return null;
    }

    @Override
    default Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        walkAsync.getXSpeed().accept(this);
        walkAsync.getYSpeed().accept(this);
        walkAsync.getZSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitStop(Stop<Void> stop) {
        return null;
    }

    @Override
    default Void visitAnimation(Animation<Void> animation) {
        return null;
    }

    @Override
    default Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        pointLookAt.getpointX().accept(this);
        pointLookAt.getpointY().accept(this);
        pointLookAt.getpointZ().accept(this);
        pointLookAt.getSpeed().accept(this);
        return null;
    }

    @Override
    default Void visitSetVolume(SetVolume<Void> setVolume) {
        setVolume.getVolume().accept(this);
        return null;
    }

    @Override
    default Void visitGetVolume(GetVolume<Void> getVolume) {
        return null;
    }

    @Override
    default Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        return null;
    }

    @Override
    default Void visitPlayFile(PlayFile<Void> playFile) {
        playFile.getMsg().accept(this);
        return null;
    }

    @Override
    default Void visitSetLeds(SetLeds<Void> setLeds) {
        setLeds.getColor().accept(this);
        return null;
    }

    @Override
    default Void visitLedOff(LedOff<Void> ledOff) {
        return null;
    }

    @Override
    default Void visitLedReset(LedReset<Void> ledReset) {
        return null;
    }

    @Override
    default Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        randomEyesDuration.getDuration().accept(this);
        return null;
    }

    @Override
    default Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        rastaDuration.getDuration().accept(this);
        return null;
    }

    @Override
    default Void visitFsrSensor(FsrSensor<Void> forceSensor) {
        return null;
    }

    @Override
    default Void visitNaoMark(DetectMarkSensor<Void> naoMark) {
        return null;
    }

    @Override
    default Void visitTakePicture(TakePicture<Void> takePicture) {
        takePicture.getPictureName().accept(this);
        return null;
    }

    @Override
    default Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        recordVideo.getDuration().accept(this);
        recordVideo.getVideoName().accept(this);
        return null;
    }

    @Override
    default Void visitLearnFace(LearnFace<Void> learnFace) {
        learnFace.getFaceName().accept(this);
        return null;
    }

    @Override
    default Void visitForgetFace(ForgetFace<Void> forgetFace) {
        forgetFace.getFaceName().accept(this);
        return null;
    }

    @Override
    default Void visitDetectFace(DetectFaceSensor<Void> detectFace) {
        return null;
    }

    @Override
    default Void visitElectricCurrent(ElectricCurrentSensor<Void> electricCurrent) {
        return null;
    }

    @Override
    default Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        setIntensity.getIntensity().accept(this);
        return null;
    }

    @Override
    default Void visitAutonomous(Autonomous<Void> autonomous) {
        return null;
    }

    @Override
    default Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        recognizeWord.getVocabulary().accept(this);
        return null;
    }

    @Override
    default Void visitNaoMarkInformation(NaoMarkInformation<Void> naoMarkInformation) {
        naoMarkInformation.getNaoMarkId().accept(this);
        return null;
    }

    @Override
    default Void visitDetecedFaceInformation(DetectedFaceInformation<Void> detectedFaceInformation) {
        detectedFaceInformation.getFaceName().accept(this);
        return null;
    }

    // following methods are used to specify unrelated defaults
}
