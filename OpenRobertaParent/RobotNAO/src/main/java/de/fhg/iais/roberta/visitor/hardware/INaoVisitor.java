package de.fhg.iais.roberta.visitor.hardware;

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
import de.fhg.iais.roberta.visitor.hardware.actor.ISpeechVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface INaoVisitor<V> extends ISpeechVisitor<V>, ISensorVisitor<V> {

    /**
     * visit a {@link SetMode}.
     *
     * @param mode phrase to be visited
     */
    V visitSetMode(SetMode<V> mode);

    /**
     * visit a {@link ApplyPosture}.
     *
     * @param apply posture phrase to be visited
     */
    V visitApplyPosture(ApplyPosture<V> applyPosture);

    /**
     * visit a {@link SetStiffness}.
     *
     * @param setStiffness on phrase to be visited
     */
    V visitSetStiffness(SetStiffness<V> setStiffness);

    /**
     * visit a {@link Hand}.
     *
     * @param Hand on phrase to be visited
     */
    V visitHand(Hand<V> hand);

    /**
     * visit a {@link MoveJoint}.
     *
     * @param ElectricCurrentSensor on phrase to be visited
     */
    V visitMoveJoint(MoveJoint<V> moveJoint);

    /**
     * visit a {@link WalkDistance}.
     *
     * @param walkDistance phrase to be visited
     */
    V visitWalkDistance(WalkDistance<V> walkDistance);

    /**
     * visit a {@link TurnDegrees}.
     *
     * @param turnDegrees phrase to be visited
     */
    V visitTurnDegrees(TurnDegrees<V> turnDegrees);

    /**
     * visit a {@link WalkTo}.
     *
     * @param walkTo phrase to be visited
     */
    V visitWalkTo(WalkTo<V> walkTo);

    /**
     * visit a {@link WalkAsync}.
     *
     * @param walkAsync phrase to be visited
     */
    V visitWalkAsync(WalkAsync<V> walkAsync);

    /**
     * visit a {@link Stop}.
     *
     * @param stop phrase to be visited
     */
    V visitStop(Stop<V> stop);

    /**
     * visit a {@link MoveJoint}.
     *
     * @param ElectricCurrentSensor on phrase to be visited
     */
    V visitAnimation(Animation<V> animation);

    /**
     * visit a {@link PointLookAt}.
     *
     * @param point look at phrase to be visited
     */
    V visitPointLookAt(PointLookAt<V> pointLookAt);

    /**
     * visit a {@link setVolume}.
     *
     * @param set volume on phrase to be visited
     */
    V visitSetVolume(SetVolume<V> setVolume);

    /**
     * visit a {@link GetVolume}.
     *
     * @param getVolume phrase to be visited
     */
    V visitGetVolume(GetVolume<V> getVolume);

    /**
     * visit a {@link GetLanguage}.
     *
     * @param getLanguage phrase to be visited
     */
    V visitGetLanguage(GetLanguage<V> getLanguage);

    /**
     * visit a {@link PlayFile}.
     *
     * @param playFile phrase to be visited
     */
    V visitPlayFile(PlayFile<V> playFile);

    /**
     * visit a {@link SetLeds}.
     *
     * @param set leds phrase to be visited
     */
    V visitSetLeds(SetLeds<V> setLeds);

    /**
     * visit a {@link ledOff}.
     *
     * @param led off phrase to be visited
     */
    V visitLedOff(LedOff<V> ledOff);

    /**
     * visit a {@link ledReset}.
     *
     * @param led reset phrase to be visited
     */
    V visitLedReset(LedReset<V> ledReset);

    /**
     * visit a {@link RandomEyesDuration}.
     *
     * @param random eyes duration phrase to be visited
     */
    V visitRandomEyesDuration(RandomEyesDuration<V> randomEyesDuration);

    /**
     * visit a {@link RastaDuration}.
     *
     * @param rast duration phrase to be visited
     */
    V visitRastaDuration(RastaDuration<V> rastaDuration);

    /**
     * visit a {@link FsrSensor}.
     *
     * @param force sensor phrase to be visited
     */
    V visitFsrSensor(FsrSensor<V> forceSensor);

    /**
     * visit a {@link DetectMarkSensor}.
     *
     * @param DetectMarkMode on phrase to be visited
     */
    V visitNaoMark(DetectMarkSensor<V> naoMark);

    /**
     * visit a {@link DetectMarkSensor}.
     *
     * @param DetectMarkMode on phrase to be visited
     */
    V visitTakePicture(TakePicture<V> takePicture);

    /**
     * visit a {@link DetectMarkSensor}.
     *
     * @param DetectMarkMode on phrase to be visited
     */
    V visitRecordVideo(RecordVideo<V> recordVideo);

    /**
     * visit a {@link LearnFace}.
     *
     * @param LearnFace on phrase to be visited
     */
    V visitLearnFace(LearnFace<V> learnFace);

    /**
     * visit a {@link ForgetFace}.
     *
     * @param ForgetFace on phrase to be visited
     */
    V visitForgetFace(ForgetFace<V> forgetFace);

    /**
     * visit a {@link DetectFaceSensor}.
     *
     * @param DetectFaceSensor on phrase to be visited
     */
    V visitDetectFace(DetectFaceSensor<V> detectFace);

    V visitElectricCurrent(ElectricCurrentSensor<V> electricCurrent);

    V visitSetIntensity(SetIntensity<V> setIntensity);

    V visitAutonomous(Autonomous<V> autonomous);

    V visitRecognizeWord(RecognizeWord<V> recognizeWord);

    /**
     * visit a {@link NaoMarkInformation}.
     *
     * @param naoMarkInformation on phrase to be visited
     */
    V visitNaoMarkInformation(NaoMarkInformation<V> naoMarkInformation);

    /**
     * visit a {@link DetectedFaceInformation}.
     *
     * @param detectedFaceInformation on phrase to be visited
     */
    V visitDetecedFaceInformation(DetectedFaceInformation<V> detectedFaceInformation);
}