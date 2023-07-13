package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.BodyLEDAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RecallAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.RememberAction;
import de.fhg.iais.roberta.syntax.actors.arduino.bob3.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.bob3.CodePadSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.hardware.INIBOVisitor;

/**
 * This class is implementing {@link INIBOVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * The Visitor class <b>has to be extended</b> by NIBO robots {@link Bob3CppVisitor} and {@link Rob3rtaCppVisitor}.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public abstract class NIBOCommonCppVisitor extends NepoArduinoCppVisitor implements INIBOVisitor<Void> {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     * @param configurationAst the configuration
     * @param beans a map of available beans
     */
    public NIBOCommonCppVisitor(List<List<Phrase>> phrases, ConfigurationAst configurationAst, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, configurationAst, beans);
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
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        if ( infraredSensor.getMode().equals("REFLEXION") ) {
            this.src.add("rob.getIRSensor()");
        } else {
            this.src.add("rob.getIRLight()");
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.src.add("rob.getTemperature()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        decrIndentation();
        mainTask.variables.accept(this);
        nlIndent();
        generateTimerVariables();
        generateUserDefinedMethods();
        nlIndent();
        this.src.add("void setup() {").INCR().nlI();
        generateUsedVars();
        this.src.DECR().nlI().add("}").nlI();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        super.generateProgramPrefix(withWrapping);
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        this.src.add("rob.setLed(");
        if ( ledOnAction.side.equals("Left") ) {
            this.src.add("EYE_2, ");
        } else {
            this.src.add("EYE_1, ");
        }
        ledOnAction.ledColor.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction ledOffAction) {
        this.src.add("rob.setLed(");
        if ( ledOffAction.side.equals("Left") ) {
            this.src.add("EYE_2, OFF);");
        } else {
            this.src.add("EYE_1, OFF);");
        }
        return null;
    }

    @Override
    public Void visitBodyLEDAction(BodyLEDAction bodyLEDAction) {
        this.src.add("rob.setLed(");
        this.src.add(bodyLEDAction.side, ", ");
        this.src.add(bodyLEDAction.ledState, ");");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        this.src.add("rob.transmitIRCode(");
        sendIRAction.code.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        this.src.add("rob.receiveIRCode(500)");
        return null;
    }

    @Override
    public Void visitRememberAction(RememberAction rememberAction) {
        this.src.add("remember((int)(");
        rememberAction.code.accept(this);
        this.src.add("));");
        return null;
    }

    @Override
    public Void visitRecallAction(RecallAction recallAction) {
        this.src.add("recall()");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        this.src.add("_CLAMP(");
        mathConstrainFunct.value.accept(this);
        this.src.add(", ");
        mathConstrainFunct.lowerBound.accept(this);
        this.src.add(", ");
        mathConstrainFunct.upperBound.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("randomNumber(");
        mathRandomIntFunct.from.accept(this);
        this.src.add(", ");
        mathRandomIntFunct.to.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        // not to block generated test programs, we do nothing
        // throw new DbcException("DebugAction is not supported so far! ");
        return null;
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        // not to block generated test programs, we do nothing
        // throw new DbcException("AssertStmt is not supported so far! ");
        return null;
    }

    @Override
    public Void visitCodePadSensor(CodePadSensor codePadSensor) {
        this.src.add("rob.getID()");
        return null;
    }
}
