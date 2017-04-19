package de.fhg.iais.roberta.syntax.check;

import java.util.ArrayList;

import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.check.program.PythonGlobalVariableCheck;
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
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

public class NaoPythonGlobalVariableCheck extends PythonGlobalVariableCheck implements NaoAstVisitor<Void> {

    public NaoPythonGlobalVariableCheck(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        super(phrasesSet);
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

        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {

        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {

        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {

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

        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {

        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {

        return null;
    }

    @Override
    public Void visitSetLanguage(SetLanguage<Void> setLanguage) {

        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {

        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {

        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {

        return null;
    }

    @Override
    public Void visitDialog(Dialog<Void> dialog) {

        return null;
    }

    @Override
    public Void visitRecognizedWord(RecognizedWord<Void> recognizedWord) {

        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {

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

        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {

        return null;
    }

    @Override
    public Void visitTouchsensors(Touchsensors<Void> touchsensors) {

        return null;
    }

    @Override
    public Void visitSonar(Sonar<Void> sonar) {

        return null;
    }

    @Override
    public Void visitGyrometer(Gyrometer<Void> gyrometer) {

        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {

        return null;
    }

    @Override
    public Void visitForceSensor(ForceSensor<Void> forceSensor) {

        return null;
    }

    @Override
    public Void visitNaoMark(NaoMark<Void> naoMark) {

        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {

        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {

        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {

        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {

        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {

        return null;
    }

    @Override
    public Void visitNaoGetSampleSensor(NaoGetSampleSensor<Void> naoGetSampleSensor) {

        return null;
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrent<Void> electricCurrent) {

        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {

        return null;
    }

}
