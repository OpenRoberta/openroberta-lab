package de.fhg.iais.roberta.visitor.hardware.actor;

public interface IAllActorsVisitor<V>
    extends IBluetoothVisitor<V>, IDifferentialMotorVisitor<V>, IDisplayVisitor<V>, ILightVisitor<V>, ISoundVisitor<V>, ISpeechVisitor<V>, ISerialVisitor<V>,
    IPinVisitor<V> {

}
