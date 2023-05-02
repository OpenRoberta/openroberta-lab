package de.fhg.iais.roberta.visitor;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.LedOffAction;
import de.fhg.iais.roberta.syntax.action.spike.LedOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.spike.Image;
import de.fhg.iais.roberta.syntax.spike.PredefinedImage;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class SpikePythonVisitor extends AbstractPythonVisitor implements ISpike {

    private final ConfigurationAst configurationAst;
    private String rightMotorPort;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public SpikePythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
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
            this.sb.append(helperMethodImpls);
        }
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global ").append(String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction) {
        src.add("diff_drive.move(");
        motorDiffOnForAction.distance.accept(this);
        src.add(", 'cm', 0, ");
        switch ( motorDiffOnForAction.direction ) {
            case "BACKWARD":
                src.add("-(");
                motorDiffOnForAction.power.accept(this);
                src.add(")");
                break;
            case "FORWARD":
                motorDiffOnForAction.power.accept(this);
                break;
            default:
                throw new DbcException("Invalid drive direction: " + motorDiffOnForAction.direction);
        }
        src.add(")");
        return null;
    }

    @Override
    public Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction) {
        src.add("diff_drive.move(");
        motorDiffTurnForAction.degrees.accept(this);
        src.add(" * math.pi / 360 * TRACKWIDTH, 'cm', ");
        switch ( motorDiffTurnForAction.direction ) {
            case "LEFT":
                src.add("-100, ");
                break;
            case "RIGHT":
                src.add("100, ");
                break;
            default:
                throw new DbcException("Invalid turn direction: " + motorDiffTurnForAction.direction);
        }
        motorDiffTurnForAction.power.accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        String end = "";
        if ( motorDiffOnAction.regulation ) {
            src.add("diff_drive.start(0, ");
            end = ")";
        } else {
            src.add("diff_drive.start_at_power(");
            end = ", 0)";
        }
        switch ( motorDiffOnAction.direction ) {
            case "BACKWARD":
                src.add("-(");
                motorDiffOnAction.power.accept(this);
                src.add(")");
                break;
            case "FORWARD":
                motorDiffOnAction.power.accept(this);
                break;
            default:
                throw new DbcException("Invalid drive direction: " + motorDiffOnAction.direction);
        }
        src.add(end);
        return null;
    }

    @Override
    public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        String end = "";
        int turn = 0;
        switch ( motorDiffTurnAction.direction ) {
            case "LEFT":
                turn = -100;
                break;
            case "RIGHT":
                turn = 100;
                break;
            default:
                throw new DbcException("Invalid turn direction: " + motorDiffTurnAction.direction);
        }
        if ( motorDiffTurnAction.regulation ) {
            src.add("diff_drive.start(").add(String.valueOf(turn)).add(", ");
            end = ")";
        } else {
            src.add("diff_drive.start_at_power(");
            end = ", " + String.valueOf(turn) + ")";
        }
        motorDiffTurnAction.power.accept(this);
        src.add(end);
        return null;
    }

    @Override
    public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        src.add("diff_drive.move_tank(");//(amount, unit='cm', left_speed=None, right_speed=None)
        switch ( motorDiffCurveForAction.direction ) {
            case "BACKWARD":
                src.add("-(");
                motorDiffCurveForAction.distance.accept(this);
                src.add(")");
                break;
            case "FORWARD":
                motorDiffCurveForAction.distance.accept(this);
                break;
            default:
                throw new DbcException("Invalid curve direction: " + motorDiffCurveForAction.direction);
        }
        src.add(", 'cm', ");
        motorDiffCurveForAction.powerLeft.accept(this);
        src.add(", ");
        motorDiffCurveForAction.powerRight.accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String sensorPort = ultrasonicSensor.getUserDefinedPort();
        this.src.add("get_sample_ultrasonic(ultrasonic_sensor_").add(sensorPort).add(")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String sensorPort = colorSensor.getUserDefinedPort();
        switch ( colorSensor.getMode() ) {
            case "LIGHT":
                this.src.add("color_sensor_").add(sensorPort).add(".get_reflected_light()");
                break;
            case "AMBIENTLIGHT":
                this.src.add("color_sensor_").add(sensorPort).add(".get_ambient_light()");
                break;
            case "COLOUR":
                this.src.add("color_sensor_").add(sensorPort).add(".get_color()");
                break;
            case "REDCHANNEL":
                this.src.add("color_sensor_").add(sensorPort).add(".get_red() / 10.24");
                break;
            case "GREENCHANNEL":
                this.src.add("color_sensor_").add(sensorPort).add(".get_green() / 10.24");
                break;
            case "BLUECHANNEL":
                this.src.add("color_sensor_").add(sensorPort).add(".get_blue() / 10.24");
                break;
            default:
                throw new DbcException("Invalid color sensor mode: " + colorSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String sensorPort = touchSensor.getUserDefinedPort();
        this.src.add("touch_sensor_").add(sensorPort);
        switch ( touchSensor.getMode() ) {
            case "PRESSED":
                this.src.add(".is_pressed()");
                break;
            case "FORCE":
                this.src.add(".get_force_percentage()");
                break;
            default:
                throw new DbcException("Invalid touch sensor mode: " + touchSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        switch ( gyroSensor.getSlot() ) {
            case "X":
                this.src.add("hub.motion_sensor.get_pitch_angle()");
                break;
            case "Y":
                this.src.add("hub.motion_sensor.get_roll_angle()");
                break;
            case "Z":
                this.src.add("hub.motion_sensor.get_yaw_angle()");
                break;
            default:
                throw new DbcException("Invalid gyro sensor slot: " + gyroSensor.getSlot());
        }
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        switch ( gestureSensor.getMode() ) {
            case "FRONT":
                this.src.add("(hub.motion_sensor.get_orientation() == 'front')");
                break;
            case "BACK":
                this.src.add("(hub.motion_sensor.get_orientation() == 'back')");
                break;
            case "UP":
                this.src.add("(hub.motion_sensor.get_orientation() == 'up')");
                break;
            case "DOWN":
                this.src.add("(hub.motion_sensor.get_orientation() == 'down')");
                break;
            case "LEFT":
                this.src.add("(hub.motion_sensor.get_orientation() == 'rightside')");
                break;
            case "RIGHT":
                this.src.add("(hub.motion_sensor.get_orientation() == 'leftside')");
                break;
            case "TAPPED":
                this.src.add("(hub.motion_sensor.get_gesture() == 'tapped')");
                break;
            case "SHAKE":
                this.src.add("(hub.motion_sensor.get_gesture() == 'shaken')");
                break;
            case "FREEFALL":
                this.src.add("(hub.motion_sensor.get_gesture() == 'falling')");
                break;
            default:
                throw new DbcException("Invalid gyro sensor mode: " + gestureSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String portName = keysSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.configurationAst.getConfigurationComponent(portName);
        String port = configurationComponent.getProperty("PIN1");
        switch ( port ) {
            case "RIGHT":
                this.src.add("hub.right_button.is_pressed()");
                break;
            case "LEFT":
                this.src.add("hub.left_button.is_pressed()");
                break;
            default:
                throw new DbcException("Invalid key sensor port: " + port);
        }
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    @Override
    public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        if ( motorDiffCurveAction.regulation ) {
            src.add("diff_drive.start_tank(");
        } else {
            src.add("diff_drive.start_tank_at_power(");
        }
        switch ( motorDiffCurveAction.direction ) {
            case "BACKWARD":
                src.add("-(");
                motorDiffCurveAction.powerLeft.accept(this);
                src.add("), -(");
                motorDiffCurveAction.powerRight.accept(this);
                src.add(")");
                break;
            case "FORWARD":
                motorDiffCurveAction.powerLeft.accept(this);
                src.add(", ");
                motorDiffCurveAction.powerRight.accept(this);
                break;
            default:
                throw new DbcException("Invalid curve direction: " + motorDiffCurveAction.direction);
        }
        src.add(")");
        return null;
    }

    @Override
    public Void visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction) {
        this.src.add("diff_drive.set_stop_action('");
        switch ( motorDiffStopAction.control ) {
            case "BRAKE":
                this.src.add("brake");
                break;
            case "COAST":
                this.src.add("coast");
                break;
            default:
                throw new DbcException("Invalid stop control: " + motorDiffStopAction.control);
        }
        ;
        this.src.add("')").nlI();
        this.src.add("diff_drive.stop()");
        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        String motorPort = getPortFromConfig(motorOnForAction.port);
        this.src.add("motor").add(motorPort).add(".");
        switch ( motorOnForAction.unit ) {
            case "DEGREES":
                this.src.add("run_for_degrees(");
                break;
            case "ROTATIONS":
                this.src.add("run_for_rotations(");
                break;
            default:
                throw new DbcException("Invalid motor unit: " + motorOnForAction.unit);

        }
        motorOnForAction.value.accept(this);
        this.src.add(", ");
        motorOnForAction.power.accept(this);
        this.src.add(")");
        return null;
    }

    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String motorPort = getPortFromConfig(motorOnAction.port);
        this.src.add("motor").add(motorPort);
        if ( motorOnAction.regulation ) {
            src.add(".start(");
        } else {
            src.add(".start_at_power(");
        }
        motorOnAction.power.accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String motorPort = getPortFromConfig(motorStopAction.port);
        String myMotor = "motor" + motorPort;
        this.src.add(myMotor).add(".set_stop_action('");
        switch ( motorStopAction.control ) {
            case "BRAKE":
                this.src.add("brake");
                break;
            case "COAST":
                this.src.add("coast");
                break;
            default:
                throw new DbcException("Invalid stop control: " + motorStopAction.control);
        }
        ;
        this.src.add("')").nlI();
        this.src.add(myMotor).add(".stop()");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.sb.append("(");
        rgbColor.R.accept(this);
        this.sb.append(", ");
        rgbColor.G.accept(this);
        this.sb.append(", ");
        rgbColor.B.accept(this);
        this.sb.append(")");
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
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        int midi = (int) Math.round(69 + (Math.log(Double.parseDouble(playNoteAction.frequency) / 440) / Math.log(2.0)) * 12);
        double duration = Double.parseDouble(playNoteAction.duration) / 1000.0;
        this.src.add("hub.speaker.beep(").add(String.valueOf(midi)).add(", ").add(String.valueOf(duration)).add(");");
        return null;
    }

    @Override
    public Void visitPlayToneAction(PlayToneAction playToneAction) {
        this.src.add("hub.speaker.beep(").add("get_midi_from(");
        playToneAction.frequency.accept(this);
        this.src.add("), ");
        playToneAction.duration.accept(this);
        this.src.add(" / 1000);");
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction ledOnAction) {
        this.src.add("set_status_light(");
        ledOnAction.colour.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction ledOffAction) {
        this.src.add("hub.status_light.off()");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        switch ( displayImageAction.displayImageMode ) {
            case "ANIMATION":
                this.src.add("display.show(");
                displayImageAction.valuesToDisplay.accept(this);
                this.src.add(")");
                break;
            case "IMAGE":
                this.src.add("display.show([");
                displayImageAction.valuesToDisplay.accept(this);
                this.src.add("])");
                break;
            default:
                throw new DbcException("Invalid display mode: " + displayImageAction.displayImageMode);
        }
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        switch ( displayTextAction.displayTextMode ) {
            case "CHARACTER":
                this.src.add("display.show(str(");
                displayTextAction.textToDisplay.accept(this);
                this.src.add("))");
                break;
            case "TEXT":
                this.src.add("hub.light_matrix.write(");
                displayTextAction.textToDisplay.accept(this);
                this.src.add(")");
                break;
            default:
                throw new DbcException("Invalid display mode: " + displayTextAction.displayTextMode);
        }
        return null;
    }

    @Override
    public Void visitDisplayClearAction(DisplayClearAction displayClearAction) {
        this.src.add("hub.light_matrix.off()");
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        this.src.add("Image('");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                this.sb.append(Integer.parseInt(pixel));
            }
            if ( i < 4 ) {
                this.src.add(":");
            }
        }
        this.src.add("')");
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        this.src.add("Image('" + predefinedImage.getImageName().getImageString() + "')");
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
        this.sb.append(color);
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("wait_for_seconds(");
        waitTimeStmt.time.accept(this);
        this.sb.append("/1000)");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        UsedHardwareBean usedHardwareBean = this.getBean(UsedHardwareBean.class);
        this.src.add("import spike").nlI();
        this.src.add("import math").nlI();
        if ( usedHardwareBean.isActorUsed(C.RANDOM) || usedHardwareBean.isActorUsed(C.RANDOM_DOUBLE) ) {
            this.src.add("import random").nlI();
        }
        this.src.add("from spike.control import wait_for_seconds, wait_until");
        if ( usedHardwareBean.isSensorUsed(SC.TIMER) ) {
            this.src.add(", Timer");
        }
        if ( !usedHardwareBean.getUsedActors().isEmpty() && !usedHardwareBean.getUsedSensors().isEmpty() ) {
            nlIndent();
        }
        if ( usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            ConfigurationComponent diffDrive = this.configurationAst.optConfigurationComponentByType("DIFFERENTIALDRIVE");
            String leftPort = diffDrive.getComponentProperties().get("MOTOR_L");
            String rightPort = diffDrive.getComponentProperties().get("MOTOR_R");
            nlIndent();
            this.src.add("TRACKWIDTH = ").add(diffDrive.getComponentProperties().get("BRICK_TRACK_WIDTH")).nlI();
            this.src.add("diff_drive = spike.MotorPair('").add(leftPort).add("', '").add(rightPort).add("')").nlI();
            this.src.add("diff_drive.set_motor_rotation(").add(diffDrive.getComponentProperties().get("BRICK_WHEEL_DIAMETER")).add(" * math.pi, 'cm')");
        }
        if ( usedHardwareBean.isActorUsed(SC.MOTOR) ) {
            usedHardwareBean.getUsedActors().stream().filter(usedActor -> usedActor.getType().equals("MOTOR")).forEach(motor -> {
                nlIndent();
                this.src.add("motor").add(motor.getPort()).add(" = spike.Motor('").add(motor.getPort()).add("')");
            });
        }
        if ( usedHardwareBean.isSensorUsed(SC.TOUCH) ) {
            usedHardwareBean.getUsedSensors().stream().filter(usedActor -> usedActor.getType().equals("TOUCH")).forEach(sensor -> {
                if ( configurationAst.optConfigurationComponent(sensor.getPort()) != null ) {
                    nlIndent();
                    this.src.add("touch_sensor_").add(sensor.getPort()).add(" = spike.ForceSensor('").add(getPortFromConfig(sensor.getPort())).add("')");
                }
            });
        }
        if ( usedHardwareBean.isSensorUsed(SC.ULTRASONIC) ) {
            usedHardwareBean.getUsedSensors().stream().filter(usedActor -> usedActor.getType().equals("ULTRASONIC")).forEach(sensor -> {
                if ( configurationAst.optConfigurationComponent(sensor.getPort()) != null ) {
                    nlIndent();
                    this.src.add("ultrasonic_sensor_").add(sensor.getPort()).add(" = spike.DistanceSensor('").add(getPortFromConfig(sensor.getPort())).add("')");
                }
            });
        }
        if ( usedHardwareBean.isSensorUsed(SC.COLOR) ) {
            usedHardwareBean.getUsedSensors().stream().filter(usedActor -> usedActor.getType().equals("COLOR")).forEach(sensor -> {
                if ( configurationAst.optConfigurationComponent(sensor.getPort()) != null ) {
                    nlIndent();
                    this.src.add("color_sensor_").add(sensor.getPort()).add(" = spike.ColorSensor('").add(getPortFromConfig(sensor.getPort())).add("')");
                }
            });
        }
        if ( usedHardwareBean.isActorUsed("DISPLAY") ) {
            nlIndent();
            this.src.add("import hub as _hub").nlI();
            this.src.add("display = _hub.display").nlI();
            this.src.add("Image = _hub.Image");
        }
        if ( usedHardwareBean.isSensorUsed(SC.TIMER) ) {
            nlIndent();
            this.src.add("timer = Timer()");
        }
        if ( usedHardwareBean.isActorUsed("HUB") ) {
            nlIndent();
            this.src.add("hub = spike.PrimeHub()");
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
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("run()");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("hub.light_matrix.show_image('SAD')");
        decrIndentation();
        //TODO finally close open ports
        decrIndentation();
        nlIndent();
        nlIndent();

        this.sb.append("main()");
    }

    private String getPortFromConfig(String name) {
        ConfigurationComponent block = configurationAst.getConfigurationComponent(name);
        return block.getComponentProperties().get("PORT");
    }
}