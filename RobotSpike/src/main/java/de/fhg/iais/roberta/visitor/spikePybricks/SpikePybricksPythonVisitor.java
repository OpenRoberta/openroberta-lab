package de.fhg.iais.roberta.visitor.spikePybricks;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.spike.Image;
import de.fhg.iais.roberta.syntax.spike.PredefinedImage;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.AbstractSpikePythonVisitor;

/**
 * This class is extending {@link AbstractSpikePythonVisitor}. <br>All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class SpikePybricksPythonVisitor extends AbstractSpikePythonVisitor {

    private UsedHardwareBean usedHardwareBean = null;

    public SpikePybricksPythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans, configurationAst);
    }

    @Override
    protected void addExceptionSadFaceToCode() {
        src.add("while True:").nlI();
        src.add("    hub.display.icon(Matrix([[0, 0, 0, 0, 0], [0, 100, 0, 100, 0], [0, 0, 0, 0, 0], [0, 100, 100, 100, 0], [100, 0, 0, 0, 100]]))");
        decrIndentation();
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        src.add("wait(");
        waitTimeStmt.time.accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction) {
        src.add("drive_straight(");
        motorDiffOnForAction.power.accept(this);
        src.add(",");
        motorDiffOnForAction.distance.accept(this);
        switch ( motorDiffOnForAction.direction ) {
            case "BACKWARD":
                src.add("* -1");
                break;
            case "FORWARD":
                break;
            default:
                throw new DbcException("Invalid direction: " + motorDiffOnForAction.direction);
        }
        src.add(")");

        return null;
    }

    @Override
    public Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction) {
        src.add("turn_for(");
        motorDiffTurnForAction.power.accept(this);
        src.add(",");
        motorDiffTurnForAction.degrees.accept(this);
        src.add(",");
        switch ( motorDiffTurnForAction.direction ) {
            case "RIGHT":
                src.add("1");
                break;
            case "LEFT":
                src.add("-1");
                break;
            default:
                throw new DbcException("Invalid turn direction: " + motorDiffTurnForAction.direction);
        }
        src.add(")");
        return null;
    }

    @Override
    public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        if ( motorDiffOnAction.regulation ) {

            src.add("drive_base.drive(");
            switch ( motorDiffOnAction.direction ) {
                case "BACKWARD":
                    src.add("-(").add("deg_sec_to_mm_sec(get_deg_sec_from_percent").add("(");
                    motorDiffOnAction.power.accept(this);
                    src.add(")))");
                    break;
                case "FORWARD":
                    src.add("deg_sec_to_mm_sec(get_deg_sec_from_percent").add("(");
                    motorDiffOnAction.power.accept(this);
                    src.add("))");
                    break;
                default:
                    throw new DbcException("Invalid direction: " + motorDiffOnAction.direction);
            }
            src.add(" , 0)");

        } else {

            src.add("diff_drive(");
            switch ( motorDiffOnAction.direction ) {
                case "BACKWARD":
                    src.add("-(");
                    motorDiffOnAction.power.accept(this);
                    src.add("),-(");
                    motorDiffOnAction.power.accept(this);
                    src.add(")");
                    break;
                case "FORWARD":
                    motorDiffOnAction.power.accept(this);
                    src.add(", ");
                    motorDiffOnAction.power.accept(this);
                    break;
                default:
                    throw new DbcException("Invalid direction: " + motorDiffOnAction.direction);
            }
            src.add(", False)");

        }
        return null;
    }

    @Override
    public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        if ( motorDiffTurnAction.regulation ) {

            src.add("drive_base.drive(0,");
            switch ( motorDiffTurnAction.direction ) {
                case "RIGHT":
                    src.add(" get_deg_sec_from_percent").add("(");
                    motorDiffTurnAction.power.accept(this);
                    src.add(") * (WHEEL_DIAMETER * math.pi) / (TRACKWIDTH * math.pi)");
                    break;
                case "LEFT":
                    src.add(" -( get_deg_sec_from_percent").add("(");
                    motorDiffTurnAction.power.accept(this);
                    src.add(")) * (WHEEL_DIAMETER * math.pi) / (TRACKWIDTH * math.pi)");
                    break;
                default:
                    throw new DbcException("Invalid turn direction: " + motorDiffTurnAction.direction);
            }
            src.add(")");

        } else {

            src.add("diff_drive(");
            switch ( motorDiffTurnAction.direction ) {
                case "RIGHT":
                    motorDiffTurnAction.power.accept(this);
                    src.add(", -(");
                    motorDiffTurnAction.power.accept(this);
                    src.add(")");
                    break;
                case "LEFT":
                    src.add("-(");
                    motorDiffTurnAction.power.accept(this);
                    src.add("), ");
                    motorDiffTurnAction.power.accept(this);
                    break;
                default:
                    throw new DbcException("Invalid turn direction: " + motorDiffTurnAction.direction);
            }
            src.add(", False)");

        }
        return null;
    }

    @Override
    public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        src.add("tank_drive_dist(");
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
        src.add(", ");
        motorDiffCurveForAction.powerLeft.accept(this);
        src.add(", ");
        motorDiffCurveForAction.powerRight.accept(this);
        src.add(")");

        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String sensorPort = ultrasonicSensor.getUserDefinedPort();
        src.add("get_dist(ultrasonic_sensor_").add(sensorPort).add(")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String sensorPort = colorSensor.getUserDefinedPort();
        switch ( colorSensor.getMode() ) {
            case "LIGHT":
                src.add("color_sensor_").add(sensorPort).add(".reflection()");
                break;
            case "AMBIENTLIGHT":
                src.add("int(color_sensor_").add(sensorPort).add(".ambient())");
                break;
            case "COLOUR":
                src.add("get_color(color_sensor_").add(sensorPort).add(")");
                break;
            case "REDCHANNEL":
                src.add("int(hsv2rgb(");
                src.add("color_sensor_").add(sensorPort).add(".hsv()");
                src.add(")[0]/2.55)");
                break;
            case "GREENCHANNEL":
                src.add("int(hsv2rgb(");
                src.add("color_sensor_").add(sensorPort).add(".hsv()");
                src.add(")[1]/2.55)");
                break;
            case "BLUECHANNEL":
                src.add("int(hsv2rgb(");
                src.add("color_sensor_").add(sensorPort).add(".hsv()");
                src.add(")[2]/2.55)");
                break;
            default:
                throw new DbcException("Invalid color sensor mode: " + colorSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String sensorPort = touchSensor.getUserDefinedPort();
        switch ( touchSensor.getMode() ) {
            case "PRESSED":
                src.add("touch_sensor_").add(sensorPort);
                src.add(".pressed()");
                break;
            case "FORCE":
                src.add("int(min(touch_sensor_").add(sensorPort);
                src.add(".force() * 10.0 , 100.0))");
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
                src.add("hub.imu.tilt()[0]");
                break;
            case "Y":
                src.add("hub.imu.tilt()[1]");
                break;
            case "Z":
                src.add("int(hub.imu.heading()%180 if abs(hub.imu.heading()%360) < 180 else -abs(hub.imu.heading()%360 - 360))");
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
                src.add("(hub.imu.up() == Side.TOP)");
                break;
            case "BACK":
                src.add("(hub.imu.up() == Side.BOTTOM)");
                break;
            case "UP":
                src.add("(hub.imu.up() == Side.FRONT)");
                break;
            case "DOWN":
                src.add("(hub.imu.up() == Side.BACK)");
                break;
            //TODO this is swaped becaues lego spike has this the wrong way
            case "LEFT":
                src.add("(hub.imu.up() == Side.RIGHT)");
                break;
            case "RIGHT":
                src.add("(hub.imu.up() == Side.LEFT)");
                break;
            case "TAPPED":
                src.add("is_tapped()");
                break;
            case "SHAKE":
                src.add("is_shaken()");
                break;
            case "FREEFALL":
                src.add("is_free_fall()");
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
                src.add("(Button.RIGHT in hub.buttons.pressed())");
                break;
            case "LEFT":
                src.add("(Button.LEFT in hub.buttons.pressed())");
                break;
            default:
                throw new DbcException("Invalid key sensor port: " + port);
        }

        return null;
    }

    @Override
    public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        String regulated = motorDiffCurveAction.regulation.toString().substring(0, 1).toUpperCase() + motorDiffCurveAction.regulation.toString().substring(1);

        src.add("diff_drive(");
        switch ( motorDiffCurveAction.direction ) {
            case "BACKWARD":
                src.add("-((");
                motorDiffCurveAction.powerLeft.accept(this);
                src.add(")), -((");
                motorDiffCurveAction.powerRight.accept(this);
                src.add("))");
                break;
            case "FORWARD":
                src.add("(");
                motorDiffCurveAction.powerLeft.accept(this);
                src.add("), (");
                motorDiffCurveAction.powerRight.accept(this);
                src.add(")");
                break;
            default:
                throw new DbcException("Invalid curve direction: " + motorDiffCurveAction.direction);
        }
        src.add(", ").add(regulated).add(")");

        return null;
    }

    @Override
    public Void visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction) {
        src.add("drive_base");

        switch ( motorDiffStopAction.control ) {
            case "BRAKE":
                //making the motor drive at speed 0 is the same as breaking
                src.add(".drive(0,0)");
                break;
            case "COAST":
                //default option stop is coast
                src.add(".stop()");
                break;
            default:
                throw new DbcException("Invalid stop control: " + motorDiffStopAction.control);
        }

        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        String motorPort = getPortFromConfig(motorOnForAction.port);

        src.add("motor").add(motorPort).add(".");
        switch ( motorOnForAction.unit ) {
            case "DEGREES":
                src.add("run_angle(rotation_angle = ");
                break;
            case "ROTATIONS":
                src.add("run_angle(rotation_angle = 360 * ");
                break;
            default:
                throw new DbcException("Invalid motor unit: " + motorOnForAction.unit);
        }
        motorOnForAction.value.accept(this);
        src.add(", speed = get_deg_sec_from_percent(");
        motorOnForAction.power.accept(this);
        src.add("))");

        return null;
    }

    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String motorPort = getPortFromConfig(motorOnAction.port);

        src.add("motor").add(motorPort);
        if ( motorOnAction.regulation ) {
            src.add(".run(get_deg_sec_from_percent(");
            motorOnAction.power.accept(this);
            src.add("))");
        } else {
            src.add(".dc(");
            motorOnAction.power.accept(this);
            src.add(")");
        }

        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String motorPort = getPortFromConfig(motorStopAction.port);

        src.add("motor").add(motorPort);
        switch ( motorStopAction.control ) {
            case "BRAKE":
                src.add(".brake()");
                break;
            case "COAST":
                src.add(".stop()");
                break;
            default:
                throw new DbcException("Invalid stop control: " + motorStopAction.control);
        }

        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        src.add("stopWatch.reset()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        src.add("stopWatch.time()");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        src.add("hub.speaker.beep(").add(String.valueOf(playNoteAction.frequency)).add(", ").add(String.valueOf(playNoteAction.duration)).add(");");
        return null;
    }

    @Override
    public Void visitPlayToneAction(PlayToneAction playToneAction) {
        src.add("hub.speaker.beep(");
        playToneAction.frequency.accept(this);
        src.add(", ");
        playToneAction.duration.accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        src.add("hub_light_on(");
        rgbLedOnHiddenAction.colour.accept(this);
        src.add(")");
        return null;
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        src.add("hub.light.off()");
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        switch ( displayImageAction.displayImageMode ) {
            case "ANIMATION":
                src.add("show_animation(list(");
                displayImageAction.valuesToDisplay.accept(this);
                src.add("))");
                break;
            case "IMAGE":
                src.add("hub.display.icon(");
                displayImageAction.valuesToDisplay.accept(this);
                src.add(")");
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
                src.add("display_text(str(");
                displayTextAction.textToDisplay.accept(this);
                src.add("))");
                break;
            case "TEXT":
                //TODO implement scrolling text ?
                src.add("display_text(str(");
                displayTextAction.textToDisplay.accept(this);
                src.add("))");
                break;
            default:
                throw new DbcException("Invalid display mode: " + displayTextAction.displayTextMode);
        }
        return null;
    }

    @Override
    public Void visitDisplayClearAction(DisplayClearAction displayClearAction) {
        src.add("hub.display.off()");
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        src.add("Matrix([");
        for ( int i = 0; i < 5; i++ ) {
            src.add("[");
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "100";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                src.add(Integer.parseInt(pixel));
                if ( j != 4 )
                    src.add(", ");
            }
            src.add("]");
            if ( i != 4 )
                src.add(", ");
        }
        src.add("])");

        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        src.add(predefinedImage.getImageName().getMatrixString());
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color = "";

        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#E701A7":
                color = "Color.MAGENTA";
                break;
            case "#571CC1":
                color = "Color.VIOLET";
                break;
            case "#3590F5":
                color = "Color.BLUE";
                break;
            case "#77E7FF":
                color = "Color.AZURE";
                break;
            case "#0FCB54":
                color = "Color.CYAN";
                break;
            case "#0BA845":
                color = "Color.GREEN";
                break;
            case "#F7F700":
                color = "Color.YELLOW";
                break;
            case "#FAAC01":
                color = "Color.ORANGE";
                break;
            case "#FA010C":
                color = "Color.RED";
                break;
            case "#000000":
                color = "Color.BLACK";
                break;
            case "#FFFFFF":
                color = "Color.WHITE";
                break;
            case "#EBC300":
                color = "Color.NONE";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }

        src.add(color);
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case SQUARE:
                mathSingleFunct.param.get(0).accept(this);
                src.add(" * ");
                mathSingleFunct.param.get(0).accept(this);
                break;
            case ABS:
                src.add("abs(");
                mathSingleFunct.param.get(0).accept(this);
                src.add(")");
                break;
            case LOG10:
                src.add("math.log(");
                mathSingleFunct.param.get(0).accept(this);
                src.add(")/math.log(10)");
                break;
            case POW10:
                src.add("10 **");
                mathSingleFunct.param.get(0).accept(this);
                break;
            case ROUND:
                this.src.add("round(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case EXP:
                this.src.add("math.e**");
                mathSingleFunct.param.get(0).accept(this);
                break;
            default:
                super.visitMathSingleFunct(mathSingleFunct);
                break;
        }
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        usedHardwareBean = this.getBean(UsedHardwareBean.class);

        src.add("from pybricks.hubs import PrimeHub").nlI();
        importPubDevices();
        importParameters();
        importTools();
        importMathFunctions();
        importRobotics();
        changeColors();
        instantiateComponents();
        src.add("hub = PrimeHub()").nlI();
        prepareComponents();

        generateNNStuff("python");
    }

    /**
     * these are sensor and motor classes
     */
    private void importPubDevices() {
        String pubDevicesImportString = "";

        if ( !(usedHardwareBean.isActorUsed(SC.MOTOR) ||
            usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) ||
            usedHardwareBean.isSensorUsed(SC.COLOR) ||
            usedHardwareBean.isSensorUsed(SC.ULTRASONIC) ||
            usedHardwareBean.isSensorUsed(SC.TOUCH))
        ) {
            return;
        }

        if ( usedHardwareBean.isActorUsed(SC.MOTOR) || usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) )
            pubDevicesImportString += ", Motor";

        if ( usedHardwareBean.isSensorUsed(SC.COLOR) )
            pubDevicesImportString += ", ColorSensor";

        if ( usedHardwareBean.isSensorUsed(SC.ULTRASONIC) )
            pubDevicesImportString += ", UltrasonicSensor";

        if ( usedHardwareBean.isSensorUsed(SC.TOUCH) )
            pubDevicesImportString += ", ForceSensor";

        //substring to remove first ","
        pubDevicesImportString = "from pybricks.pupdevices import" + pubDevicesImportString.substring(1);

        src.add(pubDevicesImportString);
        src.nlI();
    }

    private void importParameters() {
        String parameterImportString = "";
        if ( !((usedHardwareBean.isImportUsed(SC.PORT) ||
            usedHardwareBean.isImportUsed(SC.COlOR_IMPORT) ||
            usedHardwareBean.isSensorUsed(SC.BUTTON) ||
            usedHardwareBean.isActorUsed(SC.MOTOR)) ||
            usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) ||
            usedHardwareBean.isSensorUsed(SC.GYRO))
        ) {
            return;
        }

        if ( usedHardwareBean.isImportUsed(SC.PORT) )
            parameterImportString += ", Port";

        if ( usedHardwareBean.isImportUsed(SC.COlOR_IMPORT) )
            parameterImportString += ", Color";

        if ( usedHardwareBean.isSensorUsed(SC.BUTTON) )
            parameterImportString += ", Button";

        if ( usedHardwareBean.isActorUsed(SC.MOTOR) || usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) )
            parameterImportString += ", Direction";

        if ( usedHardwareBean.isSensorUsed(SC.GYRO) )
            parameterImportString += ", Side";

        //substring to remove first ","
        parameterImportString = "from pybricks.parameters import" + parameterImportString.substring(1);

        src.add(parameterImportString);
        src.nlI();
    }

    private void importTools() {
        String toolsImportString = "";

        if ( usedHardwareBean.isImportUsed(SC.WAIT) )
            toolsImportString += ", wait";

        if ( usedHardwareBean.isSensorUsed(SC.TIMER) )
            toolsImportString += ", StopWatch";

        if ( usedHardwareBean.isSensorUsed(SC.GYRO) )
            toolsImportString += ", vector";

        //Matrix always has to be imported, we need this for the exception sad face
        toolsImportString = "from pybricks.tools import Matrix" + toolsImportString;

        src.add(toolsImportString);
        src.nlI();
    }

    private void importRobotics() {
        if ( usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) )
            src.add("from pybricks.robotics import DriveBase").nlI();
    }

    /**
     * these are pybricks specific umath functions
     * python import math doesn't exist in pybricks context
     */
    private void importMathFunctions() {
        src.add("import umath as math").nlI();
        src.add("import urandom as random").nlI();
    }

    private void changeColors() {
        if ( usedHardwareBean.isImportUsed(SC.COlOR_IMPORT) ){
            nlIndent();
            src.add("Color.MAGENTA = Color(315,50,15)").nlI();
            src.add("Color.BLUE = Color(225,20,20)").nlI();
            src.add("Color.AZURE = Color(200,20,20)").nlI();
            src.add("Color.CYAN = Color(150,20,20)").nlI();
            src.add("Color.YELLOW = Color(55,35,35)").nlI();
            src.add("Color.RED = Color(350,35,35)").nlI();
            src.add("Color.BLACK = Color(0,10,10)").nlI();
            src.add("Color.WHITE = Color(0,0,70)").nlI();
            //Color.NONE
        }
    }

    private void instantiateComponents() {
        nlIndent();

        instantiateDifferentialDrive();
        instantiateMotors();
        instantiateTouchSensor();
        instantiateUltrasonicSensor();
        instantiateColorSensor();
        instantiateTimer();

        nlIndent();
    }

    private void instantiateDifferentialDrive() {
        if ( usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            ConfigurationComponent diffDrive = this.configurationAst.optConfigurationComponentByType("DIFFERENTIALDRIVE");
            String leftPort = diffDrive.getComponentProperties().get("MOTOR_L"); //Port
            String rightPort = diffDrive.getComponentProperties().get("MOTOR_R"); // Port

            src.add("left_motor = Motor(Port." + leftPort + ", Direction.COUNTERCLOCKWISE)").nlI();
            src.add("right_motor = Motor(Port." + rightPort + ")").nlI();

            src.add("TRACKWIDTH = ").add(diffDrive.getComponentProperties().get("BRICK_TRACK_WIDTH")).add(" * 10").nlI();
            src.add("WHEEL_DIAMETER = 56").nlI();
            src.add("drive_base = DriveBase(left_motor, right_motor, wheel_diameter=WHEEL_DIAMETER, axle_track=TRACKWIDTH)").nlI();
            //accelerate to max speed within 0.1 seconds, this value is subject to change
            src.add("drive_base.settings(straight_acceleration=810*10, turn_acceleration=810*10)").nlI();
        }
    }

    private void instantiateMotors() {
        if ( usedHardwareBean.isActorUsed(SC.MOTOR) ) {
            usedHardwareBean.getUsedActors().stream().filter(usedActor -> usedActor.getType().equals("MOTOR")).forEach(motor -> {
                AtomicBoolean componentLocked = new AtomicBoolean(false);
                usedHardwareBean.getLockedComponent().forEach(
                    (component, port) -> {
                        if((motor.getPort()).equals(port)) {
                            src.add("motor").add(motor.getPort()).add(" = left_motor").nlI();
                            componentLocked.set(true);
                        }
                    }
                );
                if(!componentLocked.get()){
                    src.add("motor").add(motor.getPort()).add(" = Motor(Port.").add(motor.getPort()).add(")").nlI();
                }
            });
        }
    }

    private void instantiateTouchSensor() {
        if ( usedHardwareBean.isSensorUsed(SC.TOUCH) ) {
            usedHardwareBean.getUsedSensors().stream().filter(usedActor -> usedActor.getType().equals("TOUCH")).forEach(sensor -> {
                if ( configurationAst.optConfigurationComponent(sensor.getPort()) != null ) {
                    nlIndent();
                    src.add("touch_sensor_").add(sensor.getPort()).add(" = ForceSensor(Port.").add(getPortFromConfig(sensor.getPort())).add(")");
                }
            });
        }
    }

    private void instantiateUltrasonicSensor() {
        if ( usedHardwareBean.isSensorUsed(SC.ULTRASONIC) ) {
            usedHardwareBean.getUsedSensors().stream().filter(usedActor -> usedActor.getType().equals("ULTRASONIC")).forEach(sensor -> {
                if ( configurationAst.optConfigurationComponent(sensor.getPort()) != null ) {
                    nlIndent();
                    src.add("ultrasonic_sensor_").add(sensor.getPort()).add(" = UltrasonicSensor(Port.").add(getPortFromConfig(sensor.getPort())).add(")");
                }
            });
        }
    }

    private void instantiateColorSensor() {
        if ( usedHardwareBean.isSensorUsed(SC.COLOR) ) {
            usedHardwareBean.getUsedSensors().stream().filter(usedActor -> usedActor.getType().equals("COLOR")).forEach(sensor -> {
                if ( configurationAst.optConfigurationComponent(sensor.getPort()) != null ) {
                    nlIndent();
                    src.add("color_sensor_").add(sensor.getPort()).add(" = ColorSensor(Port.").add(getPortFromConfig(sensor.getPort())).add(")").nlI();
                    src.add("color_sensor_").add(sensor.getPort()).add(".detectable_colors([Color.MAGENTA, Color.BLUE, Color.AZURE, Color.CYAN, Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE, Color.NONE])");
                }
            });
        }
    }

    private void instantiateTimer() {
        if ( usedHardwareBean.isSensorUsed(SC.TIMER) ) {
            nlIndent();
            src.add("stopWatch = StopWatch()");
        }
    }

    private void prepareComponents() {
        if ( usedHardwareBean.isSensorUsed(SC.GYRO) ) {
            src.add("hub.imu.reset_heading(0)").nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.SPEAKER)){
            //TODO make volume be settings controlled
            src.add("hub.speaker.volume(15)").nlI();
        }
    }
}