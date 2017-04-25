package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SayText;
import de.fhg.iais.roberta.syntax.action.nao.SetIntensity;
import de.fhg.iais.roberta.syntax.action.nao.SetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.SetLeds;
import de.fhg.iais.roberta.syntax.action.nao.SetMode;
import de.fhg.iais.roberta.syntax.action.nao.SetStiffness;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TakePicture;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
//import de.fhg.iais.roberta.syntax.expr.nao.LedColor;
import de.fhg.iais.roberta.syntax.sensor.nao.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.nao.Dialog;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrent;
import de.fhg.iais.roberta.syntax.sensor.nao.ForceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.sensor.nao.Gyrometer;
import de.fhg.iais.roberta.syntax.sensor.nao.LearnFace;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMark;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizedWord;
import de.fhg.iais.roberta.syntax.sensor.nao.Sonar;
import de.fhg.iais.roberta.syntax.sensor.nao.Touchsensors;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface NaoAstVisitor<V> extends AstLanguageVisitor<V> {
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
     * @param ElectricCurrent on phrase to be visited
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
     * visit a {@link Stop}.
     *
     * @param stop phrase to be visited
     */
    V visitStop(Stop<V> stop);

    /**
     * visit a {@link MoveJoint}.
     *
     * @param ElectricCurrent on phrase to be visited
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
     * visit a {@link SetLanguage}.
     *
     * @param set language phrase to be visited
     */
    V visitSetLanguage(SetLanguage<V> setLanguage);

    /**
     * visit a {@link GetLanguage}.
     *
     * @param getLanguage phrase to be visited
     */
    V visitGetLanguage(GetLanguage<V> getLanguage);

    /**
     * visit a {@link SayText}.
     *
     * @param sayText phrase to be visited
     */
    V visitSayText(SayText<V> sayText);

    /**
     * visit a {@link PlayFile}.
     *
     * @param playFile phrase to be visited
     */
    V visitPlayFile(PlayFile<V> playFile);

    /**
     * visit a {@link Dialog}.
     *
     * @param dialog phrase to be visited
     */
    V visitDialog(Dialog<V> dialog);

    /**
     * visit a {@link RecognizedWord}.
     *
     * @param recognized word phrase to be visited
     */
    V visitRecognizedWord(RecognizedWord<V> recognizedWord);

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
     * visit a {@link Touchsensors}.
     *
     * @param rast duration phrase to be visited
     */
    V visitTouchsensors(Touchsensors<V> touchsensors);

    /**
     * visit a {@link Sonar}.
     *
     * @param sonar phrase to be visited
     */
    V visitSonar(Sonar<V> sonar);

    /**
     * visit a {@link Gyrometer}.
     *
     * @param gyrometer phrase to be visited
     */
    V visitGyrometer(Gyrometer<V> gyrometer);

    /**
     * visit a {@link Accelerometer}.
     *
     * @param accelerometer phrase to be visited
     */
    V visitAccelerometer(Accelerometer<V> accelerometer);

    /**
     * visit a {@link ForceSensor}.
     *
     * @param force sensor phrase to be visited
     */
    V visitForceSensor(ForceSensor<V> forceSensor);

    /**
     * visit a {@link NaoMark}.
     *
     * @param NaoMark on phrase to be visited
     */
    V visitNaoMark(NaoMark<V> naoMark);

    /**
     * visit a {@link NaoMark}.
     *
     * @param NaoMark on phrase to be visited
     */
    V visitTakePicture(TakePicture<V> takePicture);

    /**
     * visit a {@link NaoMark}.
     *
     * @param NaoMark on phrase to be visited
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
     * visit a {@link DetectFace}.
     *
     * @param DetectFace on phrase to be visited
     */
    V visitDetectFace(DetectFace<V> detectFace);

    V visitNaoGetSampleSensor(NaoGetSampleSensor<V> naoGetSampleSensor);

    V visitElectricCurrent(ElectricCurrent<V> electricCurrent);

    //V visitLedColor(LedColor<V> ledColor);

    V visitSetIntensity(SetIntensity<V> setIntensity);
}