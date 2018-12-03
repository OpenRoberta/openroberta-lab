package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.Set;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.GetSampleSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.collect.Bob3UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IBob3Visitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class Bob3CppVisitor extends AbstractCommonArduinoCppVisitor implements IBob3Visitor<Void> {
    private final boolean isTimerSensorUsed;
    private boolean isListUsed;
    private final Set<UsedSensor> usedTimer;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Bob3CppVisitor(ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(new Configuration.Builder().build(), phrases, indentation);
        Bob3UsedHardwareCollectorVisitor codePreprocessVisitor = new Bob3UsedHardwareCollectorVisitor(phrases);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.usedTimer = codePreprocessVisitor.getTimer();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Bob3CppVisitor astVisitor = new Bob3CppVisitor(programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append(colorConst.getColor().getFirst());
        return null;
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case ANY:
            case COMPARABLE:
            case ADDABLE:
            case NULL:
            case REF:
            case PRIM:
            case NOTHING:
            case CAPTURED_TYPE:
            case R:
            case S:
            case T:
                return "";
            case ARRAY:
                return "std::list<double>";
            case ARRAY_NUMBER:
                return "std::list<double>";
            case ARRAY_STRING:
                return "std::list<String>";
            case ARRAY_BOOLEAN:
                return "std::list<bool>";
            case ARRAY_COLOUR:
                return "std::list<Bob3Color>";
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "double";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "String";
            case VOID:
                return "void";
            case COLOR:
                return "Bob3Color";
            case CONNECTION:
                return "int";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        if ( infraredSensor.getMode().equals("REFLEXION") ) {
            this.sb.append("myBob.getIRSensor()");
        } else {
            this.sb.append("myBob.getIRLight()");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("myBob.getTemperature()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        nlIndent();
        if ( this.isTimerSensorUsed || this.usedTimer.toString().contains("TIMER") ) {
            this.sb.append("unsigned long __time = millis();");
            nlIndent();
        }
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("void setup()");
        nlIndent();
        incrIndentation();
        this.sb.append("{");
        nlIndent();
        generateUsedVars();
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        for ( VarDeclaration<Void> var : this.usedVars ) {
            if ( var.getVarType().toString().contains("ARRAY") ) {
                this.isListUsed = true;
            }
        }
        if ( this.isListUsed ) {
            this.sb.append("#include <ArduinoSTL.h>\n");
            this.sb.append("#include <list>\n");
        }
        this.sb.append("#include <math.h> \n");
        this.sb.append("#include <BOB3.h> \n");
        this.sb.append("#include <NEPODefs.h>\n");
        this.sb.append("Bob3 myBob;\n");

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        //        if ( withWrapping ) {
        //            this.sb.append("\n}\n");
        //        }
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("myBob.setWhiteLeds(WHITE, WHITE);");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("myBob.setLed(2, OFF);");
        this.sb.append("myBob.setLed(1, OFF);");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("(");
        rgbColor.getR().visit(this);
        this.sb.append(")");
        this.sb.append("*256*256 + ");
        this.sb.append("(");
        rgbColor.getG().visit(this);
        this.sb.append(")");
        this.sb.append("*256 + ");
        this.sb.append("(");
        rgbColor.getB().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("myBob.getIRSensor()");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        if ( pinTouchSensor.getSlot().equals("0") ) {
            this.sb.append("( myBob.getArm(" + pinTouchSensor.getPort() + ") > " + pinTouchSensor.getSlot() + " )");
        } else {
            this.sb.append("( myBob.getArm(" + pinTouchSensor.getPort() + ") == " + pinTouchSensor.getSlot() + " )");
        }
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        this.sb.append("myBob.setLed(");
        if ( ledOnAction.getSide().equals("Left") ) {
            this.sb.append("EYE_2, ");
        } else {
            this.sb.append("EYE_1, ");
        }
        ledOnAction.getLedColor().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        this.sb.append("myBob.setLed(");
        if ( ledOffAction.getSide().equals("Left") ) {
            this.sb.append("EYE_2, OFF);");
        } else {
            this.sb.append("EYE_1, OFF);");
        }
        return null;
    }

    @Override
    public Void visitBodyLEDAction(BodyLEDAction<Void> bodyLEDAction) {
        this.sb.append("myBob.setLed(");
        this.sb.append(bodyLEDAction.getSide() + ", ");
        this.sb.append(bodyLEDAction.getledState() + ");");
        return null;
    }

    @Override
    public Void visitBob3CodePadSensor(CodePadSensor<Void> codePadSensor) {
        this.sb.append("myBob.getID()");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.sb.append("myBob.transmitIRCode(");
        sendIRAction.getCode().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.sb.append("myBob.receiveIRCode(500)");
        return null;
    }

    @Override
    public Void visitBob3GetSampleSensor(GetSampleSensor<Void> getSampleSensor) {
        getSampleSensor.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitRememberAction(RememberAction<Void> rememberAction) {
        this.sb.append("remember((int)(");
        rememberAction.getCode().visit(this);
        this.sb.append("));");
        return null;
    }

    @Override
    public Void visitRecallAction(RecallAction<Void> recallAction) {
        this.sb.append("recall()");
        return null;
    }

}
