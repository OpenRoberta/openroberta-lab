package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.*;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.functions.*;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

import java.util.ArrayList;

public class Ev3C4ev3Visitor extends AbstractCppVisitor implements IEv3Visitor<Void> {

    private final ILanguage language;

    /**
     * initialize the EV3 c4ev3 code generator visitor.
     *
     * @param programPhrases
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    protected Ev3C4ev3Visitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation, ILanguage language) {
        super(programPhrases, indentation);
        this.language = language;
    }

    /**
     * factory method to generate EV3 c4ev3 code from an AST.<br>
     *
     * @param programName
     * @param brickConfiguration
     * @param phrasesSet
     * @param withWrapping
     * @param language
     * @return
     */
    public static String generate(
        String programName,
        Configuration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        boolean withWrapping,
        ILanguage language) {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);

        Ev3C4ev3Visitor astVisitor = new Ev3C4ev3Visitor(phrasesSet, 0, language);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }


    @Override
    protected void generateCode(boolean withWrapping) {
        // TODO: Implement
        this.sb.append("#include <ev3.h>\nint main () { printf(\"Running\\n\"); InitEV3(); OnFwdReg(OUT_A, 50); Wait(500); OnFwdReg(OUT_A, 0); FreeEV3(); return 0; }");
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
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
    protected void generateProgramPrefix(boolean withWrapping) {

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {

    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        return null;
    }
}
