package de.fhg.iais.roberta.visitor.vorwerk;

import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.BrushOn;
import de.fhg.iais.roberta.syntax.action.vorwerk.SideBrush;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOff;
import de.fhg.iais.roberta.syntax.action.vorwerk.VacuumOn;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface VorwerkAstVisitor<V> extends AstVisitor<V>, AstSensorsVisitor<V> {

    public V visitDropOffSensor(DropOffSensor<V> dropOffSensor);

    public V visitWallSensor(WallSensor<V> wallSensor);

    public V visitBrushOn(BrushOn<V> brushOn);

    public V visitBrushOff(BrushOff<V> brushOff);

    public V visitSideBrush(SideBrush<V> sideBrush);

    public V visitVacuumOn(VacuumOn<V> vacuumOn);

    public V visitVacuumOff(VacuumOff<V> vacuumOff);

}