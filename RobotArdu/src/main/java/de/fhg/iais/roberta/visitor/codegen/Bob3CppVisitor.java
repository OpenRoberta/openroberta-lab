package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IBob3Visitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class Bob3CppVisitor extends AbstractCommonArduinoCppVisitor implements IBob3Visitor<Void> {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public Bob3CppVisitor(List<List<Phrase<Void>>> phrases, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, new ConfigurationAst.Builder().build(), beans);
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
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "double";
            case NUMBER_INT:
                return "int";
            case VOID:
                return "void";
            case COLOR:
                return "unsigned int";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        if ( infraredSensor.getMode().equals("REFLEXION") ) {
            this.sb.append("rob.getIRSensor()");
        } else {
            this.sb.append("rob.getIRLight()");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("rob.getTemperature()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().accept(this);
        nlIndent();
        generateTimerVariables();
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
        this.sb.append("#include \"bob3.h\" \n");
        this.sb.append("Bob3 rob;\n");

        super.generateProgramPrefix(withWrapping);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        if ( pinTouchSensor.getSlot().equals("0") ) {
            this.sb.append("( rob.getArm(" + pinTouchSensor.getPort() + ") > " + pinTouchSensor.getSlot() + " )");
        } else {
            this.sb.append("( rob.getArm(" + pinTouchSensor.getPort() + ") == " + pinTouchSensor.getSlot() + " )");
        }
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        this.sb.append("rob.setLed(");
        if ( ledOnAction.getSide().equals("Left") ) {
            this.sb.append("EYE_2, ");
        } else {
            this.sb.append("EYE_1, ");
        }
        ledOnAction.getLedColor().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        this.sb.append("rob.setLed(");
        if ( ledOffAction.getSide().equals("Left") ) {
            this.sb.append("EYE_2, OFF);");
        } else {
            this.sb.append("EYE_1, OFF);");
        }
        return null;
    }

    @Override
    public Void visitBodyLEDAction(BodyLEDAction<Void> bodyLEDAction) {
        this.sb.append("rob.setLed(");
        this.sb.append(bodyLEDAction.getSide() + ", ");
        this.sb.append(bodyLEDAction.getledState() + ");");
        return null;
    }

    @Override
    public Void visitBob3CodePadSensor(CodePadSensor<Void> codePadSensor) {
        this.sb.append("rob.getID()");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.sb.append("rob.transmitIRCode(");
        sendIRAction.getCode().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.sb.append("rob.receiveIRCode(500)");
        return null;
    }

    @Override
    public Void visitRememberAction(RememberAction<Void> rememberAction) {
        this.sb.append("remember((int)(");
        rememberAction.getCode().accept(this);
        this.sb.append("));");
        return null;
    }

    @Override
    public Void visitRecallAction(RecallAction<Void> recallAction) {
        this.sb.append("recall()");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("rob.setWhiteLeds(WHITE, WHITE);");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("rob.setLed(2, OFF);");
        this.sb.append("rob.setLed(1, OFF);");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        if ( lightSensor.getMode().equals("REFLEXION") ) {
            this.sb.append("rob.getIRSensor()");
        } else {
            this.sb.append("rob.getIRLight()");
        }
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("_CLAMP(");
        mathConstrainFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).accept(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        // not to block generated test programs, we do nothing
        // throw new DbcException("DebugAction is not supported so far! ");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt<Void> assertStmt) {
        // not to block generated test programs, we do nothing
        // throw new DbcException("AssertStmt is not supported so far! ");
        return null;
    }

}
