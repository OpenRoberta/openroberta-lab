package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.IWeDoVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractRegenerateTextlyJavaVisitor;

public class WeDoRegenerateTextlyJavaVisitor extends AbstractRegenerateTextlyJavaVisitor implements IWeDoVisitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(WeDoRegenerateTextlyJavaVisitor.class);

    protected final ConfigurationAst brickConfiguration;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programPhrases the program
     * @param brickConfiguration hardware configuration of the brick
     */
    public WeDoRegenerateTextlyJavaVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst brickConfiguration) {
        super(programPhrases);
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.nlI().add("wedo.clearDisplay();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.nlI().add("wedo.showText(");
        showTextAction.msg.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        this.src.nlI().add("wedo.turnRgbOn(").add(rgbLedOnAction.port).add(",");
        rgbLedOnAction.colour.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        this.src.nlI().add("wedo.turnRgbOff(").add(rgbLedOffAction.port).add(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        this.src.nlI().add("wedo.motor.move(").add(motorOnAction.port).add(",");
        motorOnAction.param.speed.accept(this);
        if ( motorOnAction.param.getDuration() != null ) {
            this.src.add(",");
            motorOnAction.param.getDuration().value.accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        this.src.nlI().add("wedo.motor.stop(").add(motorStopAction.port).add(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.nlI().add("wedo.pitch(").add(toneAction.port).add(",");
        toneAction.frequency.accept(this);
        this.src.add(",");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;

    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.nlI().add("wedo.pitch(").add(playNoteAction.port).add(",").add(playNoteAction.frequency).add(",").add(playNoteAction.duration).add(");");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.src.add("wedo.keysSensor.isPressed(").add(keysSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        this.src.add("wedo.gyroSensor.isTilted(").add(gyroSensor.getUserDefinedPort()).add(",").add(gyroSensor.getSlot().toLowerCase()).add(")");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        this.src.add("wedo.infraredSensor(").add(infraredSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add("#rgb(").add(colorConst.hexValue.replace("#", "")).add(")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("wedo.timerSensor()");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.nlI().add("wedo.timerReset();");
        return null;
    }
}
