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
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
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
    V visitSetMode(SetMode mode);

    /**
     * visit a {@link ApplyPosture}.
     *
     * @param apply posture phrase to be visited
     */
    V visitApplyPosture(ApplyPosture applyPosture);

    /**
     * visit a {@link SetStiffness}.
     *
     * @param setStiffness on phrase to be visited
     */
    V visitSetStiffness(SetStiffness setStiffness);

    /**
     * visit a {@link Hand}.
     *
     * @param Hand on phrase to be visited
     */
    V visitHand(Hand hand);

    /**
     * visit a {@link MoveJoint}.
     *
     * @param ElectricCurrentSensor on phrase to be visited
     */
    V visitMoveJoint(MoveJoint moveJoint);

    /**
     * visit a {@link WalkDistance}.
     *
     * @param walkDistance phrase to be visited
     */
    V visitWalkDistance(WalkDistance walkDistance);

    /**
     * visit a {@link TurnDegrees}.
     *
     * @param turnDegrees phrase to be visited
     */
    V visitTurnDegrees(TurnDegrees turnDegrees);

    /**
     * visit a {@link WalkTo}.
     *
     * @param walkTo phrase to be visited
     */
    V visitWalkTo(WalkTo walkTo);

    /**
     * visit a {@link WalkAsync}.
     *
     * @param walkAsync phrase to be visited
     */
    V visitWalkAsync(WalkAsync walkAsync);

    /**
     * visit a {@link Stop}.
     *
     * @param stop phrase to be visited
     */
    V visitStop(Stop stop);

    /**
     * visit a {@link MoveJoint}.
     *
     * @param ElectricCurrentSensor on phrase to be visited
     */
    V visitAnimation(Animation animation);

    /**
     * visit a {@link PointLookAt}.
     *
     * @param point look at phrase to be visited
     */
    V visitPointLookAt(PointLookAt pointLookAt);

    /**
     * visit a {@link setVolume}.
     *
     * @param set volume on phrase to be visited
     */
    V visitSetVolume(SetVolume setVolume);

    /**
     * visit a {@link GetVolume}.
     *
     * @param getVolume phrase to be visited
     */
    V visitGetVolume(GetVolume getVolume);

    /**
     * visit a {@link GetLanguage}.
     *
     * @param getLanguage phrase to be visited
     */
    V visitGetLanguage(GetLanguage getLanguage);

    /**
     * visit a {@link PlayFile}.
     *
     * @param playFile phrase to be visited
     */
    V visitPlayFile(PlayFile playFile);

    /**
     * visit a {@link SetLeds}.
     *
     * @param set leds phrase to be visited
     */
    V visitSetLeds(SetLeds setLeds);

    /**
     * visit a {@link ledOff}.
     *
     * @param led off phrase to be visited
     */
    V visitLedOff(LedOff ledOff);

    /**
     * visit a {@link ledReset}.
     *
     * @param led reset phrase to be visited
     */
    V visitLedReset(LedReset ledReset);

    /**
     * visit a {@link RandomEyesDuration}.
     *
     * @param random eyes duration phrase to be visited
     */
    V visitRandomEyesDuration(RandomEyesDuration randomEyesDuration);

    /**
     * visit a {@link RastaDuration}.
     *
     * @param rast duration phrase to be visited
     */
    V visitRastaDuration(RastaDuration rastaDuration);

    /**
     * visit a {@link FsrSensor}.
     *
     * @param force sensor phrase to be visited
     */
    V visitFsrSensor(FsrSensor forceSensor);

    /**
     * visit a {@link DetectMarkSensor}.
     *
     * @param DetectMarkMode on phrase to be visited
     */
    V visitDetectMarkSensor(DetectMarkSensor naoMark);

    /**
     * visit a {@link DetectMarkSensor}.
     *
     * @param DetectMarkMode on phrase to be visited
     */
    V visitTakePicture(TakePicture takePicture);

    /**
     * visit a {@link DetectMarkSensor}.
     *
     * @param DetectMarkMode on phrase to be visited
     */
    V visitRecordVideo(RecordVideo recordVideo);

    /**
     * visit a {@link LearnFace}.
     *
     * @param LearnFace on phrase to be visited
     */
    V visitLearnFace(LearnFace learnFace);

    /**
     * visit a {@link ForgetFace}.
     *
     * @param ForgetFace on phrase to be visited
     */
    V visitForgetFace(ForgetFace forgetFace);

    /**
     * visit a {@link DetectFaceSensor}.
     *
     * @param DetectFaceSensor on phrase to be visited
     */
    V visitDetectFaceSensor(DetectFaceSensor detectFace);

    V visitElectricCurrentSensor(ElectricCurrentSensor electricCurrent);

    V visitSetIntensity(SetIntensity setIntensity);

    V visitAutonomous(Autonomous autonomous);

    V visitRecognizeWord(RecognizeWord recognizeWord);

    /**
     * visit a {@link NaoMarkInformation}.
     *
     * @param naoMarkInformation on phrase to be visited
     */
    V visitNaoMarkInformation(NaoMarkInformation naoMarkInformation);

    /**
     * visit a {@link DetectedFaceInformation}.
     *
     * @param detectedFaceInformation on phrase to be visited
     */
    V visitDetectedFaceInformation(DetectedFaceInformation detectedFaceInformation);
}