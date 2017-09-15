package de.fhg.iais.roberta.syntax.check.hardware.nxt;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import de.fhg.iais.roberta.components.nxt.NxtConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nxt.NxtAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor implements NxtAstVisitor<Void> {

    private boolean isPlayToneUsed = false;
    private boolean isDriveUsed = false;
    private boolean isCurveUsed = false;
    private int tmpArrVarCount = 0;
    private String tmpArrVar = "";

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, NxtConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    public String getTmpArrVar() {
        return this.tmpArrVar;
    }

    public boolean isPlayToneUsed() {
        return this.isPlayToneUsed;
    }

    public boolean isDriveUsed() {
        return this.isDriveUsed;
    }

    public boolean isCurveUsed() {
        return this.isCurveUsed;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        super.visitToneAction(toneAction);
        this.isPlayToneUsed = true;
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.isDriveUsed = true;
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        this.isDriveUsed = true;
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.isDriveUsed = true;
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        this.isDriveUsed = true;
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        this.isDriveUsed = true;
        this.isCurveUsed = true;
        return null;
    }

    //TODO: rewrite it in a nicer way, check why it is not detecting inserted arrays, fix sensors
    public Void generateArrTmpVar(Expr<Void> expr) {
        System.out.println(expr.getVarType().toString());
        String type;
        String defVal;
        switch ( expr.getVarType().toString() ) {
            case "NUMBER":
                type = "float";
                defVal = "0";
                break;
            case "STRING":
                type = "string";
                defVal = "";
                break;
            case "BOOLEAN":
                type = "bool";
                defVal = "true";
                break;
            default:
                type = "int";
                defVal = "0";
                break;
        }

        if ( !expr.getVarType().toString().contains("ARRAY") ) {
            this.tmpArrVarCount += 1;
            String str = expr.toString().replaceAll("defVal=ARRAY", defVal);
            String[] val = StringUtils.substringsBetween(str.substring(18, str.length() - 1), "[", "]");
            String values = "";
            boolean first = true;
            for ( String value : val ) {
                if ( first ) {
                    first = false;
                } else {
                    values += ", ";
                }
                if ( expr.getVarType().toString().contains("STRING") ) {
                    values += "\"" + value + "\"";
                } else {
                    values += value;
                }
            }

            this.tmpArrVar += "\n" + type + " __tmpArr" + this.tmpArrVarCount + "[] = {" + values + "};";
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        generateArrTmpVar(mathOnListFunct.getParam().get(0));
        return null;

    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        generateArrTmpVar(lengthOfIsEmptyFunct.getParam().get(0));
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        generateArrTmpVar(indexOfFunct.getParam().get(0));
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        generateArrTmpVar(listGetIndex.getParam().get(0));
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        generateArrTmpVar(listSetIndex.getParam().get(1));
        return null;
    }

}