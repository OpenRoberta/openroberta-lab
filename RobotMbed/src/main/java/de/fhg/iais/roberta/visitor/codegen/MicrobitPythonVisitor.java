package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class MicrobitPythonVisitor extends AbstractPythonVisitor implements IMbedVisitor<Void> {
    private final ConfigurationAst robotConfiguration;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public MicrobitPythonVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.robotConfiguration = robotConfiguration;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage<Void> predefinedImage) {
        this.sb.append("microbit.Image." + predefinedImage.getImageName());
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case PREDEFINED_IMAGE:
                this.sb.append("microbit.Image.SILLY");
                break;
            case IMAGE:
                this.sb.append("microbit.Image()");
                break;
            default:
                super.visitEmptyExpr(emptyExpr);
                break;
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        decrIndentation();
        //        nlIndent();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("microbit.sleep(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("microbit.display.clear()");
        return null;
    }

    @Override
    public Void visitImage(Image<Void> image) {
        this.sb.append("microbit.Image('");
        for ( int i = 0; i < 5; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                this.sb.append(pixel);
            }
            if ( i < 4 ) {
                this.sb.append(":");
            }
        }
        this.sb.append("')");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        this.sb.append("microbit.display.");
        appendTextDisplayType(displayTextAction);
        if ( !displayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            displayTextAction.getMsg().accept(this);
            this.sb.append(")");
        } else {
            displayTextAction.getMsg().accept(this);
        }
        this.sb.append(")");
        return null;
    }

    private void appendTextDisplayType(DisplayTextAction<Void> displayTextAction) {
        if ( displayTextAction.getMode() == DisplayTextMode.TEXT ) {
            this.sb.append("scroll(");
        } else {
            this.sb.append("show(");
        }
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        this.sb.append("microbit.display.show(");
        displayImageAction.getValuesToDisplay().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction<Void> imageInvertFunction) {
        imageInvertFunction.getImage().accept(this);
        this.sb.append(".invert()");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        String port = keysSensor.getPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.sb.append("microbit.button_").append(pin1.toLowerCase(Locale.ENGLISH)).append(".is_pressed()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("microbit.temperature()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("( microbit.running_time() - timer1 )");
                break;
            case SC.RESET:
                this.sb.append("timer1 = microbit.running_time()");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");

        }

        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        if ( accelerometerSensor.getSlot().equals(SC.STRENGTH) ) {
            this.sb.append("math.sqrt(microbit.accelerometer.get_x()**2 + microbit.accelerometer.get_y()**2 + microbit.accelerometer.get_z()**2)");
        } else {
            this.sb.append("microbit.accelerometer.get_").append(accelerometerSensor.getSlot().toLowerCase(Locale.ENGLISH)).append("()");
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("round(microbit.display.read_light_level() / 2.55)");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinValueSensor) {
        String port = pinValueSensor.getPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        String valueType = pinValueSensor.getMode().toLowerCase(Locale.ENGLISH);
        this.sb.append("microbit.pin");
        this.sb.append(pin1);
        this.sb.append(".read_");
        this.sb.append(valueType);
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        this.usedGlobalVarInFunctions.clear();
        this.usedGlobalVarInFunctions.add("timer1");
        StmtList<Void> variables = mainTask.getVariables();
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        nlIndent();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            this.sb.append("global ").append(String.join(", ", this.usedGlobalVarInFunctions));
        }
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
        this.sb.append("raise");
        decrIndentation();
        decrIndentation();
        nlIndent();

        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        this.sb.append("\"" + gestureSensor.getMode().toString().toLowerCase().replace("_", " ") + "\" == microbit.accelerometer.current_gesture()");
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction<Void> imageShiftFunction) {
        imageShiftFunction.getImage().accept(this);
        this.sb.append(".shift_" + imageShiftFunction.getShiftDirection().toString().toLowerCase() + "(");
        imageShiftFunction.getPositions().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("microbit.compass.heading()");
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        this.sb.append("microbit.pin" + pinTouchSensor.getPort() + ".is_touched()");
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction<Void> radioSendAction) {
        this.sb.append("radio.config(power=" + radioSendAction.getPower() + ")");
        nlIndent();
        this.sb.append("radio.send(str(");
        radioSendAction.getMsg().accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction<Void> radioReceiveAction) {
        switch ( radioReceiveAction.getType() ) {
            case NUMBER:
                this.sb.append("((lambda x: 0 if x is None else float(x))(radio.receive()))");
                break;
            case BOOLEAN:
                this.sb.append("('True' == radio.receive())");
                break;
            case STRING:
                this.sb.append("radio.receive()");
                break;
            default:
                throw new IllegalArgumentException("unhandled type");
        }
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction<Void> radioSetChannelAction) {
        this.sb.append("radio.config(group=");
        radioSetChannelAction.getChannel().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        String port = pinWriteValueAction.getPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        this.sb.append("microbit.pin" + pin1);
        String valueType = pinWriteValueAction.getMode().equals(SC.DIGITAL) ? "digital(" : "analog(";
        this.sb.append(".write_").append(valueType);
        pinWriteValueAction.getValue().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPinSetPullAction(PinSetPullAction<Void> pinSetPullAction) {
        // TODO add as soon as microbit runtime is updated
        //        this.sb.append("microbit.pin" + pinSetPullAction.getPort().getValues()[0] + ".set_pull(");
        //        switch ( pinSetPullAction.getMode() ) {
        //            case SC.UP:
        //                this.sb.append("PULL_UP");
        //                break;
        //            case SC.DOWN:
        //                this.sb.append("PULL_DOWN");
        //                break;
        //            case SC.NONE:
        //            default:
        //                this.sb.append("NO_PULL");
        //                break;
        //        }
        //        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction<Void> displaySetPixelAction) {
        this.sb.append("microbit.display.set_pixel(");
        displaySetPixelAction.getX().accept(this);
        this.sb.append(", ");
        displaySetPixelAction.getY().accept(this);
        this.sb.append(", ");
        displaySetPixelAction.getBrightness().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction<Void> displayGetPixelAction) {
        this.sb.append("microbit.display.get_pixel(");
        displayGetPixelAction.getX().accept(this);
        this.sb.append(", ");
        displayGetPixelAction.getY().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("import microbit");
        nlIndent();
        this.sb.append("import random");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            this.sb.append("import radio");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.MUSIC) ) {
            this.sb.append("import music");
            nlIndent();
        }
        nlIndent();
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
        nlIndent();
        this.sb.append("timer1 = microbit.running_time()");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            nlIndent();
            this.sb.append("radio.on()");
        }
    }

    @Override
    public String getEnumCode(IMode value) {
        return value.toString().toLowerCase();
    }

    @Override
    public Void visitStmtTextComment(StmtTextComment<Void> stmtTextComment) {
        this.sb.append("# " + stmtTextComment.getTextComment());
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        this.sb.append("print(");
        serialWriteAction.getValue().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("music.pitch(");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb
            .append("music.pitch(")
            .append(Integer.parseInt(playNoteAction.getFrequency().split("\\.")[0]))
            .append(", ")
            .append(playNoteAction.getDuration())
            .append(")");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        throw new DbcException("Not supported!");
    }
}