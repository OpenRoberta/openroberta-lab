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
import de.fhg.iais.roberta.syntax.check.program.LoopsCounterVisitor;
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

public class NaoLoopsCounterVisitor extends LoopsCounterVisitor implements NaoAstVisitor<Void> {

    public NaoLoopsCounterVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        super(phrasesSet);

    }

    @Override
    public Void visitSetMode(SetMode<Void> mode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitHand(Hand<Void> hand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStop(Stop<Void> stop) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitAnimation(Animation<Void> animation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSetLanguage(SetLanguage<Void> setLanguage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitTouchsensors(Touchsensors<Void> touchsensors) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSonar(Sonar<Void> sonar) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitGyrometer(Gyrometer<Void> gyrometer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitForceSensor(ForceSensor<Void> forceSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitNaoMark(NaoMark<Void> naoMark) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitNaoGetSampleSensor(NaoGetSampleSensor<Void> naoGetSampleSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrent<Void> electricCurrent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDialog(Dialog<Void> dialog) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Void visitRecognizedWord(RecognizedWord<Void> recognizedWord) {
		// TODO Auto-generated method stub
		return null;
	}

}
