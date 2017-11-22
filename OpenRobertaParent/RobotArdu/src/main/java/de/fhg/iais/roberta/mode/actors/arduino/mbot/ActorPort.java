package de.fhg.iais.roberta.mode.actors.arduino.mbot;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;

public enum ActorPort implements IActorPort {
    NO_PORT, M1( "M1", "motor1", "MB" ), M2( "M2", "motor2", "MC" ), PORT_1( "1", "P1" ), PORT_2( "2", "P2" ), PORT_3( "3", "P3" ), PORT_4( "4", "P4" );

    private final String[] values;

    private ActorPort(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    @Override
    public String getXmlName() {
        return this.values[0];
    }

}