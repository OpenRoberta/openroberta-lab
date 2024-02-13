package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public class MicrobitPythonVisitor extends AbstractPythonVisitor implements IMicrobitVisitor<Void> {
    protected final ConfigurationAst robotConfiguration;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public MicrobitPythonVisitor(List<List<Phrase>> programPhrases, ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.robotConfiguration = robotConfiguration;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        this.src.add("microbit.Image.", predefinedImage.getImageName());
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case PREDEFINED_IMAGE:
                this.src.add("microbit.Image.SILLY");
                break;
            case IMAGE:
                this.src.add("microbit.Image()");
                break;
            default:
                super.visitEmptyExpr(emptyExpr);
                break;
        }
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("microbit.sleep(");
        waitTimeStmt.time.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("microbit.display.clear()");
        return null;
    }

    @Override
    public Void visitImage(Image image) {
        this.src.add("microbit.Image('");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                this.src.add(pixel);
            }
            if ( i < 4 ) {
                this.src.add(":");
            }
        }
        this.src.add("')");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        this.src.add("microbit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !displayTextAction.msg.getKind().hasName("STRING_CONST") ) {
            this.src.add("str(");
            displayTextAction.msg.accept(this);
            this.src.add(")");
        } else {
            displayTextAction.msg.accept(this);
        }
        this.src.add(")");
        return null;
    }

    private void appendTextDisplayType(DisplayTextAction displayTextAction) {
        if ( Objects.equals(displayTextAction.mode, "TEXT") ) {
            this.src.add("scroll(");
        } else {
            this.src.add("show(");
        }
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        this.src.add("microbit.display.show(");
        displayImageAction.valuesToDisplay.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        imageInvertFunction.image.accept(this);
        this.src.add(".invert()");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String port = keysSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.src.add("microbit.button_", pin1.toLowerCase(Locale.ENGLISH), ".is_pressed()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.src.add("microbit.temperature()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("( microbit.running_time() - timer1 )");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.add("timer1 = microbit.running_time()");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        if ( accelerometerSensor.getSlot().equals(SC.STRENGTH) ) {
            this.src.add("math.sqrt(microbit.accelerometer.get_x()**2 + microbit.accelerometer.get_y()**2 + microbit.accelerometer.get_z()**2)");
        } else {
            this.src.add("microbit.accelerometer.get_", accelerometerSensor.getSlot().toLowerCase(Locale.ENGLISH), "()");
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("round(microbit.display.read_light_level() / 2.55)");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        String port = pinValueSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String valueType = pinValueSensor.getMode().toLowerCase(Locale.ENGLISH);
        if ( valueType.equalsIgnoreCase(SC.PULSEHIGH) ) {
            this.src.add("machine.time_pulse_us(microbit.pin");
            this.src.add(pin1);
            this.src.add(", 1)");
        } else if ( valueType.equalsIgnoreCase(SC.PULSELOW) ) {
            this.src.add("machine.time_pulse_us(microbit.pin");
            this.src.add(pin1);
            this.src.add(", 0)");
        } else {
            this.src.add("microbit.pin");
            this.src.add(pin1);
            this.src.add(".read_");
            this.src.add(valueType);
            this.src.add("()");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        this.usedGlobalVarInFunctions.clear();
        this.usedGlobalVarInFunctions.add("timer1");
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        this.src.add("def run():");
        incrIndentation();
        nlIndent();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        }
//        TODO add as soon as microbit runtime is updated
//        if ( this.robotConfiguration.isComponentTypePresent(SC.DIGITAL_PIN) ) {
//            for ( ConfigurationComponent usedConfigurationBlock : this.robotConfiguration.getConfigurationComponentsValues() ) {
//                if ( usedConfigurationBlock.getComponentType().equals(SC.DIGITAL_PIN) ) {
//                    String pin1 = usedConfigurationBlock.getProperty("PIN1");
//                    String mode = usedConfigurationBlock.getProperty("PIN_PULL");
//                    if ( mode.equals("PIN_PULL_UP") ) {
//                        mode = "PULL_UP";
//                    } else if ( mode.equals("PIN_PULL_DOWN") ) {
//                        mode = "PULL_DOWN";
//                    } else {
//                        continue;
//                    }
//                    nlIndent();
//                    this.src.add("microbit.MicroBitDigitalPin.set_pull(microbit.pin", pin1, ".",mode,");");
//                }
//            }
//        }
        return null;
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
        this.src.add("raise");
        decrIndentation();
        decrIndentation();
        nlIndent();

        super.generateProgramSuffix(withWrapping);
    }

    @Override
    protected void addWaitStatementTimeout() {
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        this.src.add("(\"", gestureSensor.getMode().toString().toLowerCase().replace("_", " "), "\" == microbit.accelerometer.current_gesture())");
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        imageShiftFunction.image.accept(this);
        this.src.add(".shift_", imageShiftFunction.shiftDirection.toString().toLowerCase(), "(");
        imageShiftFunction.positions.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        this.src.add("microbit.compass.heading()");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        this.src.add("microbit.pin", pinTouchSensor.getUserDefinedPort(), ".is_touched()");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        this.src.add("radio.config(power=", radioSendAction.power, ")");
        nlIndent();
        this.src.add("radio.send(str(");
        radioSendAction.message.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        switch ( radioReceiveAction.type ) {
            case "Number":
                this.src.add("((lambda x: 0 if x is None else float(x))(radio.receive()))");
                break;
            case "Boolean":
                this.src.add("('True' == radio.receive())");
                break;
            case "String":
                this.src.add("radio.receive()");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        this.src.add("radio.config(group=");
        radioSetChannelAction.channel.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        String port = mbedPinWriteValueAction.port;
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.src.add("microbit.pin", pin1);
        String valueType = mbedPinWriteValueAction.pinValue.equals(SC.DIGITAL) ? "digital(" : "analog(";
        this.src.add(".write_", valueType);
        mbedPinWriteValueAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        this.src.add("microbit.display.set_pixel(");
        displaySetPixelAction.x.accept(this);
        this.src.add(", ");
        displaySetPixelAction.y.accept(this);
        this.src.add(", ");
        displaySetPixelAction.brightness.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        this.src.add("microbit.display.get_pixel(");
        displayGetPixelAction.x.accept(this);
        this.src.add(", ");
        displayGetPixelAction.y.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.src.add("import microbit");
        nlIndent();
        this.src.add("import random");
        nlIndent();
        this.src.add("import math");
        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            this.src.add("import radio");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.MUSIC) ) {
            this.src.add("import music");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.PIN_VALUE) ) {
            this.src.add("import machine");
            nlIndent();
        }
        nlIndent();
        this.src.add("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.src.add("class ContinueLoop(Exception): pass");
        nlIndent();
        nlIndent();
        this.src.add("timer1 = microbit.running_time()");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            nlIndent();
            this.src.add("radio.on()");
        }
        generateNNStuff("python");
    }

    @Override
    public String getEnumCode(IMode value) {
        return value.toString().toLowerCase();
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("music.pitch(");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("music.pitch(", Integer.parseInt(playNoteAction.frequency.split("\\.")[0]), ", ", playNoteAction.duration, ")");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        switch ( playFileAction.fileName ) {
            case "DADADADUM":
            case "ENTERTAINER":
            case "PRELUDE":
            case "ODE":
            case "NYAN":
            case "RINGTONE":
            case "FUNK":
            case "BLUES":
            case "BIRTHDAY":
            case "WEDDING":
            case "FUNERAL":
            case "PUNCHLINE":
            case "PYTHON":
            case "BADDY":
            case "CHASE":
            case "BA_DING":
            case "WAWAWAWAA":
            case "JUMP_UP":
            case "JUMP_DOWN":
            case "POWER_UP":
            case "POWER_DOWN": {
                this.src.add("music.play(music.", playFileAction.fileName, ")");
                break;
            }
            default: {
                throw new DbcException("Invalid play file name: " + playFileAction.fileName);
            }
        }
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        if ( switchLedMatrixAction.activated.equals("ON") ) {
            this.src.add("microbit.display.on()");
        } else {
            this.src.add("microbit.display.off()");
        }
        return null;
    }
}
