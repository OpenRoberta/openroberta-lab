package de.fhg.iais.roberta.visitors.arduino;

import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.RgbColor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface ArduinoAstVisitor<V>
    extends AstSensorsVisitor<V>, AstActorDisplayVisitor<V>, AstActorMotorVisitor<V>, AstActorLightVisitor<V>, AstActorSoundVisitor<V> {

    /**
     * visit a {@link LedOnAction}.
     *
     * @param ledOnAction phrase to be visited
     */
    V visitLedOnAction(LedOnAction<V> ledOnAction);

    V visitLedOffAction(LedOffAction<V> ledOffAction);

    V visitExternalLedOnAction(ExternalLedOnAction<V> externalLedOnAction);

    V visitExternalLedOffAction(ExternalLedOffAction<V> externalLedOffAction);

    V visitRgbColor(RgbColor<V> rgbColor);
}
