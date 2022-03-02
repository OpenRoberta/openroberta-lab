package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.actors.raspberrypi.Dummy;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateLeft;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateRight;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepForward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.TCollectColor;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.TStepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.TStepForward;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IVolksbotVisitor<V> extends IVisitor<V> {

    V visitStepForward(StepForward<V> stepForward);

    V visitStepBackward(StepBackward<V> stepBackward);

    V visitTStepForward(TStepForward<V> tStepForward);

    V visitTStepBackward(TStepBackward<V> tStepBackward);

    V visitRotateRight(RotateRight<V> rotateRight);

    V visitRotateLeft(RotateLeft<V> rotateLeft);

    V visitTCollectColor(TCollectColor<V> tCollectColor);

    V visitMainTaskSimple(MainTaskSimple<V> mainTaskSimple);

    V visitDummy(Dummy<V> dummy);
}