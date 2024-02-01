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
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ServoOnForAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
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
    private String drive;

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

    private ConfigurationComponent getDifferentialDrive() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.DIFFERENTIALDRIVE) ) {
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
    public Void visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            generateOmniDriveMethod(motorOmniDiffOnAction);
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVE));
            this.src.add("(");
            if ( motorOmniDiffOnAction.direction.equals("BACKWARD") ) {
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
            } else {
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
            }
            this.src.add(")");
        }
        return null;
    }

    private void generateOmniDriveMethod(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVE));
        this.src.add("(");

        switch ( motorOmniDiffOnAction.direction ) {
            case "BACKWARD": //backward
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "FORWARD": //forward
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "RIGHT": //right
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "LEFT": //left
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "BACKWARDRIGHT": //backward right
                this.src.add("0, -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0");
                break;
            case "FORWARDLEFT": //forward left
                this.src.add("0, ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0");
                break;
            case "BACKWARDLEFT": //backward left
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0, 0, -");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "FORWARDRIGHT": //forward right
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0, 0, ");
                motorOmniDiffOnAction.power.accept(this);
                break;
            default:
                break;
        }
        this.src.add(")");
    }

    @Override
    public Void visitMotorOmniDiffOnForAction(MotorOmniDiffOnForAction motorOmniDiffOnForAction) {
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            generateOmniDriveForMethod(motorOmniDiffOnForAction);
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVEDISTANCE));
            this.src.add("(");
            motorOmniDiffOnForAction.distance.accept(this);
            this.src.add(", ");
            if ( motorOmniDiffOnForAction.direction.equals("BACKWARD") ) {
                this.src.add("-");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
            } else {
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
            }
            this.src.add(")");
        }


        return null;
    }

    private void generateOmniDriveForMethod(MotorOmniDiffOnForAction motorOmniDiffOnForAction) {
        switch ( motorOmniDiffOnForAction.direction ) {
            case "BACKWARD": //backward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "FORWARD": //forward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "RIGHT": //right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "LEFT": //left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "BACKWARDRIGHT": //backward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "FORWARDLEFT": //forward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "BACKWARDLEFT": //backward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "FORWARDRIGHT": //forward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            default:
                break;
        }
        this.src.add(")");
    }

    @Override
    public Void visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction) {
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add("frontLeftMotor.stop_sync(frontRightMotor, rearLeftMotor, rearRightMotor)");
        } else {
            this.src.add("leftMotor.stop_sync(rightMotor)");
        }
        return null;
    }

    @Override
    public Void visitMotorOmniDiffTurnAction(MotorOmniDiffTurnAction motorOmniDiffTurnAction) {
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVETURN));
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFDRIVETURN));
        }
        this.src.add("(");
        if ( motorOmniDiffTurnAction.direction.equals("LEFT") ) {
            this.src.add("-");
        }
        motorOmniDiffTurnAction.power.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMotorOmniDiffTurnForAction(MotorOmniDiffTurnForAction motorOmniDiffTurnForAction) {
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVETURNDEGREES));
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFDRIVETURNDEGREES));
        }
        this.src.add("(");
        if ( motorOmniDiffTurnForAction.direction.equals("LEFT") ) {
            this.src.add("-");
        }
        motorOmniDiffTurnForAction.power.accept(this);
        this.src.add(", ");
        motorOmniDiffTurnForAction.degrees.accept(this);
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
        ConfigurationComponent configurationComponent = this.configurationAst.getConfigurationComponent(encoderSensor.getUserDefinedPort());
        String port = configurationComponent.getProperty("SENSOR_COUNTER");
        if ( encoderSensor.getMode().equals("ROTATION") ) {
            this.src.add("int(TXT_M_", port, "_motor_step_counter.get_count() // STEPS_PER_ROTATION)");
        } else {
            this.src.add("int(TXT_M_", port, "_motor_step_counter.get_count() / STEPS_PER_ROTATION * 360)");
        }
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        ConfigurationComponent configurationComponent = this.configurationAst.getConfigurationComponent(encoderReset.sensorPort);
        String port = configurationComponent.getProperty("SENSOR_COUNTER");
        this.src.add("TXT_M_", port, "_motor_step_counter.reset()");
        return null;
    }

    @Override
    public Void visitServoOnForAction(ServoOnForAction servoOnForAction) {
        String servoPort = getPortFromConfig(servoOnForAction.port);
        this.src.add("TXT_M_", servoPort, "_servomotor.set_position(int((");
        servoOnForAction.value.accept(this);
        this.src.add(" / 180) * 512))");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String port = getPortFromConfig(ultrasonicSensor.getUserDefinedPort());
        this.src.add("TXT_M_", port, "_ultrasonic_distance_meter.get_distance()");
        return null;
    }

    @Override
    public Void visitGetLineSensor(GetLineSensor getLineSensor) {
        ConfigurationComponent configurationComponent = this.configurationAst.getConfigurationComponent(getLineSensor.getUserDefinedPort());
        String port = "";
        if ( getLineSensor.getSlot().equals(SC.LEFT) ) {
            port = configurationComponent.getProperty("PORTL");
        } else {
            port = configurationComponent.getProperty("PORTR");
        }
        this.src.add("TXT_M_", port, "_trail_follower.get_state()");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String port = getPortFromConfig(keysSensor.getUserDefinedPort());
        this.src.add("TXT_M_", port, "_mini_switch.get_state()");
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
        if ( usedHardwareBean.isActorUsed(SC.ENCODER) ) {
            this.src.add("txt_factory.init_counter_factory()").nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.SERVOMOTOR) ) {
            this.src.add("txt_factory.init_servomotor_factory()").nlI();
        }
        this.src.add("TXT_M = txt_factory.controller_factory.create_graphical_controller()").nlI();

        if ( !usedHardwareBean.getUsedActors().isEmpty() && !usedHardwareBean.getUsedSensors().isEmpty() ) {
            nlIndent();
        }
        initMotors(usedHardwareBean);
        initSensors(usedHardwareBean);
        generateVariables(usedHardwareBean);
        this.src.add("txt_factory.initialized()");

    }

    private void initMotors(UsedHardwareBean usedHardwareBean) {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.MOTOR) && usedHardwareBean.isActorUsed(SC.MOTOR) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_M", port, "_motor = txt_factory.motor_factory.create_motor(TXT_M, ", port, ")").nlI();
            }
            if ( component.componentType.equals(SC.ENCODER) && usedHardwareBean.isActorUsed(SC.ENCODER) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_M", port, "_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, ", port, ")").nlI();
                String counterPort = component.getOptProperty("SENSOR_COUNTER").substring(1);
                this.src.add("TXT_M_C", counterPort, "_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, ", counterPort, ")").nlI();
                this.src.add("TXT_M_C", counterPort, "_motor_step_counter.set_motor(TXT_M_M", port, "_motor)").nlI();
            }
            if ( component.componentType.equals(SC.SERVOMOTOR) && usedHardwareBean.isActorUsed(SC.SERVOMOTOR) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_S", port, "_servomotor = txt_factory.servomotor_factory.create_servomotor(TXT_M, ", port, ")").nlI();
            }
        }
    }

    private void initSensors(UsedHardwareBean usedHardwareBean) {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.KEY) && usedHardwareBean.isSensorUsed(SC.KEY) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_I", port, "_mini_switch = txt_factory.input_factory.create_mini_switch(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals(SC.ULTRASONIC) && usedHardwareBean.isSensorUsed(SC.ULTRASONIC) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_I", port, "_ultrasonic_distance_meter = txt_factory.input_factory.create_ultrasonic_distance_meter(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals("LINE") && usedHardwareBean.isSensorUsed(SC.INFRARED) ) {
                String portLeft = component.getOptProperty("PORTL").substring(1);
                String portRight = component.getOptProperty("PORTR").substring(1);
                this.src.add("TXT_M_I", portLeft, "_trail_follower = txt_factory.input_factory.create_trail_follower(TXT_M, ", portLeft, ")").nlI();
                this.src.add("TXT_M_I", portRight, "_trail_follower = txt_factory.input_factory.create_trail_follower(TXT_M, ", portRight, ")").nlI();
            }
        }
    }

    private void generateVariables(UsedHardwareBean usedHardwareBean) {
        if ( usedHardwareBean.isActorUsed(FischertechnikConstants.OMNIDRIVE) ) {
            ConfigurationComponent omniDrive = getOmniDrive();
            if ( omniDrive != null ) {
                this.src.add("#init omnidrive").nlI();
                this.src.add("frontLeftMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_FL") + "_motor").nlI();
                this.src.add("frontRightMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_FR") + "_motor").nlI();
                this.src.add("rearLeftMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_RL") + "_motor").nlI();
                this.src.add("rearRightMotor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_RR") + "_motor").nlI();
                this.src.add("WHEEL_DIAMETER = ", omniDrive.getOptProperty("BRICK_WHEEL_DIAMETER")).nlI();
                this.src.add("TRACK_WIDTH = ", omniDrive.getOptProperty("BRICK_TRACK_WIDTH")).nlI();
                this.src.add("WHEEL_BASE = ", omniDrive.getOptProperty("WHEEL_BASE")).nlI();
                this.drive = FischertechnikConstants.OMNIDRIVE;
            }
        } else if ( usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            ConfigurationComponent diffDrive = getDifferentialDrive();
            if ( diffDrive != null ) {
                drive = SC.DIFFERENTIAL_DRIVE;
                this.src.add("#init differentialDrive").nlI();
                this.src.add("leftMotor = ", "TXT_M_" + diffDrive.getOptProperty("MOTOR_L") + "_motor").nlI();
                this.src.add("rightMotor = ", "TXT_M_" + diffDrive.getOptProperty("MOTOR_R") + "_motor").nlI();
                this.src.add("WHEEL_DIAMETER = ", diffDrive.getOptProperty("BRICK_WHEEL_DIAMETER")).nlI();
                this.src.add("TRACK_WIDTH = ", diffDrive.getOptProperty("BRICK_TRACK_WIDTH")).nlI();
            }
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