package de.fhg.iais.roberta.syntax.codegen.vorwerk;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.check.hardware.vorwerk.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.RobotPythonVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;
import de.fhg.iais.roberta.visitor.vorwerk.VorwerkAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public class PythonVisitor extends RobotPythonVisitor
    implements AstSensorsVisitor<Void>, AstActorMotorVisitor<Void>, AstActorSoundVisitor<Void>, VorwerkAstVisitor<Void> {
    protected final VorwerkConfiguration brickConfiguration;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private PythonVisitor(VorwerkConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation, ILanguage language) {
        super(programPhrases, indentation);

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(programPhrases, null);

        this.brickConfiguration = brickConfiguration;

        this.usedGlobalVarInFunctions = checkVisitor.getMarkedVariablesAsGlobal();
        this.isProgramEmpty = checkVisitor.isProgramEmpty();
        this.loopsLabels = checkVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public static String generate(
        VorwerkConfiguration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> programPhrases,
        boolean withWrapping,
        ILanguage language) {
        Assert.notNull(brickConfiguration);

        PythonVisitor astVisitor = new PythonVisitor(brickConfiguration, programPhrases, 0, language);
        astVisitor.generateCode(withWrapping);

        return astVisitor.sb.toString();
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.wait(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.wait(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                this.sb.append(")");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de";
            case ENGLISH:
                return "en";
            case FRENCH:
                return "fr";
            case SPANISH:
                return "es";
            case ITALIAN:
                return "it";
            case DUTCH:
                return "nl";
            case FINNISH:
                return "fi";
            case POLISH:
                return "pl";
            case RUSSIAN:
                return "ru";
            case TURKISH:
                return "tu";
            case CZECH:
                return "cs";
            case PORTUGUESE:
                return "pt-pt";
            case DANISH:
                return "da";
            default:
                return "en";
        }
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        this.sb.append("hal.setLanguage(\"");
        this.sb.append(this.getLanguageString(setLanguageAction.getLanguage()));
        this.sb.append("\")");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("hal.sayText(");
        if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            sayTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            sayTextAction.getMsg().visit(this);
        }
        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( !(sayTextAction.getSpeed().getKind().equals(emptyBlock) && sayTextAction.getPitch().getKind().equals(emptyBlock)) ) {
            this.sb.append(",");
            sayTextAction.getSpeed().visit(this);
            this.sb.append(",");
            sayTextAction.getPitch().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.play_sound(" + playFileAction.getFileName() + ")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("hal.playTone(float(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append("), float(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("hal." + motorOnAction.getPort().getCodeName() + "_motor_on(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        motorOnAction.getDurationValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {

        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {

        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        this.sb.append("hal." + motorStopAction.getPort().getCodeName() + "_motor_stop()");
        return null;
    }

    @Override
    public Void visitBrushOn(BrushOn<Void> brushOn) {
        this.sb.append("hal.brush_on(");
        brushOn.getSpeed().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBrushOff(BrushOff<Void> brushOff) {
        this.sb.append("hal.brush_off()");
        return null;
    }

    @Override
    public Void visitSideBrush(SideBrush<Void> sideBrush) {
        this.sb.append("hal.side_brush_" + sideBrush.getWorkingState().toString().toLowerCase() + "()");
        return null;
    }

    @Override
    public Void visitVacuumOn(VacuumOn<Void> vacuumOn) {
        this.sb.append("hal.vacuum_on(");
        vacuumOn.getSpeed().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitVacuumOff(VacuumOff<Void> vacuumOff) {
        this.sb.append("hal.vacuum_off()");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.sb.append("hal.drive_distance(" + getEnumCode(driveAction.getDirection()) + ", ");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        driveAction.getParam().getDuration().getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("hal.stop_motors()");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {

        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort().getOraName();
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("hal.getTimerValue(" + timerNumber + ")");
                break;
            case RESET:
                this.sb.append("hal.resetTimer(" + timerNumber + ")");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.sample_touch_sensor(" + getEnumCode(touchSensor.getPort()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(touchSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("hal.sample_ultrasonic_sensor(" + getEnumCode(ultrasonicSensor.getPort()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(ultrasonicSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this.sb.append("hal.sample_accelerometer_sensor(" + getEnumCode(accelerometerSensor.getPort()) + ")");
        return null;
    }

    @Override
    public Void visitDropOffSensor(DropOffSensor<Void> dropOffSensor) {
        this.sb.append("hal.sample_dropoff_sensor(" + getEnumCode(dropOffSensor.getPort()) + ")");
        return null;
    }

    @Override
    public Void visitWallSensor(WallSensor<Void> wallSensor) {
        this.sb.append("hal.sample_wall_sensor()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        generateUserDefinedMethods();
        this.sb.append("\n").append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(getEnumCode(where1));
        if ( (where1 == IndexLocation.FROM_START) || (where1 == IndexLocation.FROM_END) ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(getEnumCode(where2));
        if ( (where2 == IndexLocation.FROM_START) || (where2 == IndexLocation.FROM_END) ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                this.sb.append("BlocklyMethods.length( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("BlocklyMethods.isEmpty( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        this.sb.append("BlocklyMethods.listsGetIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getElementOperation()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        this.sb.append("BlocklyMethods.listsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getElementOperation()));
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("BlocklyMethods.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("BlocklyMethods.textJoin(");
        textJoinFunct.getParam().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python\n\n");
        this.sb.append("from __future__ import absolute_import\n");
        this.sb.append("from roberta import Hal\n");
        this.sb.append("from roberta import BlocklyMethods\n");
        this.sb.append("import math\n\n");

        this.sb.append("class BreakOutOfALoop(Exception): pass\n");
        this.sb.append("class ContinueLoop(Exception): pass\n\n");

        this.sb.append("hal = Hal()");

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(INDENT).append("try:\n");
        this.sb.append(INDENT).append(INDENT).append("run()\n");
        this.sb.append(INDENT).append("except Exception as e:\n");
        this.sb.append(INDENT).append(INDENT).append("print('Fehler im Vorwerk')\n");
        this.sb.append(INDENT).append(INDENT).append("print(e.__class__.__name__)\n");
        // FIXME: we can only print about 30 chars
        this.sb.append(INDENT).append(INDENT).append("print(e)");

        this.sb.append("\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(INDENT).append("main()");
    }

}
