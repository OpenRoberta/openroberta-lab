package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Bob3Configuration;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.check.program.Bob3CodePreprocessVisitor;
import de.fhg.iais.roberta.syntax.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.sensor.bob3.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.Bob3AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a
 * StringBuilder. <b>This representation is correct C code for Arduino.</b> <br>
 */
public class Ast2Bob3Visitor extends Ast2ArduVisitor implements Bob3AstVisitor<Void>, AstActorLightVisitor<Void> {
    private boolean isTimerSensorUsed;

    //private Bob3Configuration boardConfiguration;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Ast2Bob3Visitor(Bob3Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(phrases, indentation);
        //this.boardConfiguration = brickConfiguration;
        Bob3CodePreprocessVisitor codePreprocessVisitor = new Bob3CodePreprocessVisitor(phrases, brickConfiguration);
        this.usedSensors = codePreprocessVisitor.getUsedSensors();
        this.usedActors = codePreprocessVisitor.getUsedActors();
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(Bob3Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        Ast2Bob3Visitor astVisitor = new Ast2Bob3Visitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("bob3.getIRLight()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("bob3.getTemperature()");
        return null;
    }

    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("T.ShowSeconds()");
                break;
            case RESET:
                this.sb.append("T.ResetTimer();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        ITouchSensorMode arm = touchSensor.getMode();
        System.out.println(arm.toString());
        this.sb.append("bob3.getArmPair(" + touchSensor.getArmSide() + ", " + touchSensor.getArmPart() + ")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        incrIndentation();
        generateUserDefinedMethods();
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");

        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.Timer();");
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        String methodName = indexOfFunct.getLocation() == IndexLocation.LAST ? "rob.arrFindLast(" : "rob.arrFindFirst(";
        this.sb.append(methodName);
        arrayLen((Var<Void>) indexOfFunct.getParam().get(0));
        this.sb.append(", ");
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("NULL");
            return null;
        }
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            this.sb.append("(");
            arrayLen((Var<Void>) lengthOfIsEmptyFunct.getParam().get(0));
            this.sb.append(" == 0)");
        } else {
            arrayLen((Var<Void>) lengthOfIsEmptyFunct.getParam().get(0));
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case FROM_START:
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("rob.randomIntegerInRange(0, ");
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            return null;
        }
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case FROM_START:
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("rob.randomIntegerInRange(0, ");
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        this.sb.append(" = ");
        listSetIndex.getParam().get(1).visit(this);
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("rob.clamp(");
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
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) == 0");
                break;
            case ODD:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) != 0");
                break;
            case PRIME:
                this.sb.append("rob.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case WHOLE:
                this.sb.append("rob.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(",");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("rob.arrSum(");
                break;
            case MIN:
                this.sb.append("rob.arrMin(");
                break;
            case MAX:
                this.sb.append("rob.arrMax(");
                break;
            case AVERAGE:
                this.sb.append("rob.arrMean(");
                break;
            case MEDIAN:
                this.sb.append("rob.arrMedian(");
                break;
            case STD_DEV:
                this.sb.append("rob.arrStandardDeviatioin(");
                break;
            case RANDOM:
                this.sb.append("rob.arrRand(");
                break;
            case MODE:
                this.sb.append("rob.arrMode(");
                break;
            default:
                break;
        }
        arrayLen((Var<Void>) mathOnListFunct.getParam().get(0));
        this.sb.append(", ");
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("rob.randomFloat()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("rob.randomIntegerInRange(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("#include <math.h> \n");
        this.sb.append("#include <BOB3.h> \n");
        this.sb.append("#include <Wire.h>\n");
        this.sb.append("#include <SoftwareSerial.h>\n");
        this.sb.append("#include <CountUpDownTimer.h>\n");
        this.sb.append("#include <RobertaFunctions.h>\n");

        if ( this.isTimerSensorUsed ) {
            this.sb.append("#include <CountUpDown.h>\n\n");
            this.sb.append("CountUpDownTimer T(UP, HIGH);\n");
        }

        this.sb.append("RobertaFunctions rob;\n");
        this.sb.append("Bob3 bob3;\n");
        this.sb.append("\nvoid setup() \n");
        this.sb.append("{");
        nlIndent();
        this.sb.append("Serial.begin(9600);");
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.StartTimer();");
        }
        this.sb.append("\n}");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("\n}\n");
        }
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("bob3.getIRLight()");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("bob3.setWhiteLeds(WHITE, WHITE)");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("bob3.setLed(2, OFF)");
        this.sb.append("bob3.setLed(1, OFF)");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("bob3.setEyes(WHITE, WHITE)");
        return null;
    }
}
