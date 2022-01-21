package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IRaspberryPiVisitor<V>  {
    // TODO
    V visitTimerSensor(TimerSensor<V> timerSensor);
}