package de.fhg.iais.roberta.syntax.check;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;

public class CheckVisitorTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    class TestProgramCheckVisitor extends RobotCommonCheckVisitor {

        public TestProgramCheckVisitor(Configuration brickConfiguration) {
            super(brickConfiguration);
        }

        @Override
        public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
            return null;
        }

        @Override
        public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
            return null;
        }

        @Override
        public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
            return null;
        }

        @Override
        public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
            return null;
        }

        @Override
        public Void visitLightAction(LightAction<Void> lightAction) {
            return null;
        }

        @Override
        public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
            return null;
        }

        @Override
        public Void visitToneAction(ToneAction<Void> toneAction) {
            return null;
        }

        @Override
        public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
            return null;
        }

        @Override
        public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
            return null;
        }

        @Override
        public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
            return null;
        }

        @Override
        public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
            return null;
        }

        @Override
        public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
            return null;
        }

        @Override
        protected void checkSensorPort(ExternalSensor<Void> sensor) {

        }

        @Override
        public Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
            return null;
        }

    }

    @Test
    public void check_noLoops_returnsEmptyMap() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/invalide_use_of_variable.xml");

        TestProgramCheckVisitor checkVisitor = new TestProgramCheckVisitor(null);
        checkVisitor.check(phrases);

        Assert.assertEquals(1, checkVisitor.getErrorCount());
    }

}
