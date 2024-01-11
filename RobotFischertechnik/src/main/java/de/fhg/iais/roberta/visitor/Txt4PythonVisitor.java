package de.fhg.iais.roberta.visitor;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.constants.FischertechnikConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MotorOmniOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class Txt4PythonVisitor extends AbstractPythonVisitor implements ITxt4Visitor<Void> {

    private final ConfigurationAst configurationAst;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public Txt4PythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
    }
    private ConfigurationComponent getOmniDrive() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(FischertechnikConstants.OMNIDRIVE) ) {
                return component;
            }
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        if ( !variables.get().isEmpty() ) {
            variables.accept(this);
            nlIndent();
        }
        if ( this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
            .count() > 0 ) {
            generateUserDefinedMethods();
        }
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this.getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.src.add(helperMethodImpls);
        }
        nlIndent();
        this.src.add("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.MOTORSTART));
        String motorPort = getPortFromConfig(motorOnAction.port);
        this.src.add("(TXT_M_", motorPort, "_motor, ");
        motorOnAction.power.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String motorPort = getPortFromConfig(motorStopAction.port);
        this.src.add("TXT_M_", motorPort, "_motor.stop()");
        return null;
    }

    @Override
    public Void visitMotorOmniOnAction(MotorOmniOnAction motorOmniOnAction) {
        String speedMultiplier = "";
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        switch ( motorOmniOnAction.direction ) {
            case "HEART_SMALL": //backward
                speedMultiplier = "-";
            case "HEART": //forward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEVERTICAL));
                break;
            case "SMILE": //right
                speedMultiplier = "-";
            case "HAPPY": //left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEHORIZONTAL));
                break;
            case "SURPRISED": //backward right
                speedMultiplier = "-";
            case "CONFUSED": //forward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTL));
                break;
            case "ANGRY": //backward left
                speedMultiplier = "-";
            case "ASLEEP": //forward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTR));
                break;
            default:
                break;
        }
        this.src.add("(", speedMultiplier);
        motorOmniOnAction.power.accept(this);
        this.src.add(")");


        return null;
    }

    @Override
    public Void visitMotorOmniOnForAction(MotorOmniOnForAction motorOmniOnForAction) {
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        switch ( motorOmniOnForAction.direction ) {
            case "HEART_SMALL": //backward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                break;
            case "HEART": //forward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                break;
            case "SMILE": //right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                break;
            case "HAPPY": //left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                break;
            case "SURPRISED": //backward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                break;
            case "CONFUSED": //forward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                break;
            case "ANGRY": //backward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniOnForAction.power.accept(this);
                break;
            case "ASLEEP": //forward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE));
                this.src.add("(");
                motorOmniOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniOnForAction.power.accept(this);
                break;
            default:
                break;
        }
        this.src.add(")");


        return null;
    }

    @Override
    public Void visitMotorOmniStopAction(MotorOmniStopAction motorOmniStopAction) {
        this.src.add("frontLeftMotor.stop_sync(frontRightMotor, rearLeftMotor, rearRightMotor)");
        return null;
    }

    @Override
    public Void visitMotorOmniTurnAction(MotorOmniTurnAction motorOmniTurnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVETURN));
        this.src.add("(");
        if ( motorOmniTurnAction.direction.equals("LEFT") ) {
            this.src.add("-");
        }
        motorOmniTurnAction.power.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMotorOmniTurnForAction(MotorOmniTurnForAction motorOmniTurnForAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVETURNDEGREES));
        this.src.add("(");
        if ( motorOmniTurnForAction.direction.equals("LEFT") ) {
            this.src.add("-");
        }
        motorOmniTurnForAction.power.accept(this);
        this.src.add(", ");
        motorOmniTurnForAction.degrees.accept(this);
        this.src.add(")");


        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.MOTORSTARTFOR));
        String motorPort = getPortFromConfig(motorOnForAction.port);
        this.src.add("(TXT_M_", motorPort, "_motor, ");
        motorOnForAction.power.accept(this);
        this.src.add(", ");
        if ( motorOnForAction.unit.equals("ROTATIONS") ) {
            this.src.add("360 * ");
        }
        motorOnForAction.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        ConfigurationComponent block = configurationAst.getConfigurationComponent(keysSensor.getUserDefinedPort());
        String pin = block.getComponentProperties().get("PIN1");
        this.src.add("TXT_M_I", pin, "_mini_switch.get_state()");
        return null;
    }
    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("(");
        rgbColor.R.accept(this);
        this.src.add(", ");
        rgbColor.G.accept(this);
        this.src.add(", ");
        rgbColor.B.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.add("timer.reset()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("timer.now() * 1000");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color = "";
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#E701A7":
                color = "'pink'";
                break;
            case "#571CC1":
                color = "'violet'";
                break;
            case "#3590F5":
                color = "'blue'";
                break;
            case "#77E7FF":
                color = "'azure'";
                break;
            case "#0FCB54":
                color = "'cyan'";
                break;
            case "#0BA845":
                color = "'green'";
                break;
            case "#F7F700":
                color = "'yellow'";
                break;
            case "#FAAC01":
                color = "'orange'";
                break;
            case "#FA010C":
                color = "'red'";
                break;
            case "#000000":
                color = "'black'";
                break;
            case "#FFFFFF":
                color = "'white'";
                break;
            case "#EBC300":
                color = "None";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        this.src.add(color);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("time.sleep(");
        waitTimeStmt.time.accept(this);
        this.src.add("/1000)");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        UsedHardwareBean usedHardwareBean = this.getBean(UsedHardwareBean.class);
        this.src.add("import fischertechnik.factories as txt_factory").nlI();
        this.src.add("from lib.controller import *").nlI();

        //only motor
        if ( usedHardwareBean.isActorUsed(SC.MOTOR) || usedHardwareBean.isActorUsed(SC.ENCODER) ) {
            this.src.add("from fischertechnik.controller.Motor import Motor").nlI();
        }
        if ( usedHardwareBean.isActorUsed(C.RANDOM) || usedHardwareBean.isActorUsed(C.RANDOM_DOUBLE) ) {
            this.src.add("import random").nlI();
        }
        this.src.add("import math").nlI();
        this.src.add("import time").nlI().nlI();


        this.src.add("txt_factory.init()").nlI();
        this.src.add("txt_factory.init_input_factory()").nlI();
        if ( usedHardwareBean.isActorUsed(SC.MOTOR) || usedHardwareBean.isActorUsed(SC.ENCODER) ) {
            this.src.add("txt_factory.init_motor_factory()").nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.SERVOMOTOR) ) {
            this.src.add("txt_factory.init_servomotor_factory()").nlI();
        }
        this.src.add("TXT_M = txt_factory.controller_factory.create_graphical_controller()").nlI();

        if ( !usedHardwareBean.getUsedActors().isEmpty() && !usedHardwareBean.getUsedSensors().isEmpty() ) {
            nlIndent();
        }

        initPeripherals();
        generateVariables(usedHardwareBean);
        this.src.add("txt_factory.initialized()");

    }

    private void initPeripherals() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.MOTOR) ) {
                String port = component.getOptProperty("PORT");
                port = port.substring(1);
                this.src.add("TXT_M_M", port, "_motor = txt_factory.motor_factory.create_motor(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals(SC.ENCODER) ) {
                String port = component.getOptProperty("PORT");
                port = port.substring(1);
                this.src.add("TXT_M_M", port, "_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals(SC.SERVOMOTOR) ) {
                String port = component.getOptProperty("PORT");
                port = port.substring(1);
                this.src.add("TXT_M_S", port, "_servomotor = txt_factory.motor_factory.create_servomotor(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals(SC.KEY) ) {
                String port = component.getOptProperty("PIN1");
                this.src.add("TXT_M_I", port, "_mini_switch = txt_factory.input_factory.create_mini_switch(TXT_M,", port, ")").nlI();
            }
        }
    }

    private void generateVariables(UsedHardwareBean usedHardwareBean) {
        if ( usedHardwareBean.isActorUsed(FischertechnikConstants.OMNIDRIVE) ) {
            ConfigurationComponent omniDrive = getOmniDrive();
            this.src.add("#init omnidrive").nlI();
            this.src.add("frontLeftMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_FL") + "_motor").nlI();
            this.src.add("frontRightMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_FR") + "_motor").nlI();
            this.src.add("rearLeftMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_RL") + "_motor").nlI();
            this.src.add("rearRightMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_RR") + "_motor").nlI();
            this.src.add("WHEEL_DIAMETER = ", omniDrive.getOptProperty("BRICK_WHEEL_DIAMETER")).nlI();
            this.src.add("TRACK_WIDTH = ", omniDrive.getOptProperty("BRICK_TRACK_WIDTH")).nlI();
            this.src.add("WHEEL_BASE = ", omniDrive.getOptProperty("WHEEL_BASE")).nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.ENCODER) ) {
            this.src.add("STEPS_PER_ROTATION = 128").nlI();
        }
    }
    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.src.add("def main():");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        this.src.add("run()");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();

        this.src.add("main()");
    }

    private String getPortFromConfig(String name) {
        ConfigurationComponent block = configurationAst.getConfigurationComponent(name);
        return block.getComponentProperties().get("PORT");
    }
}