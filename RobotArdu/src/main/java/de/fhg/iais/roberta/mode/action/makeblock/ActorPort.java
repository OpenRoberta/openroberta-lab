package de.fhg.iais.roberta.mode.action.makeblock;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;

public enum ActorPort implements IActorPort {
    SLOT1( "M1", "motor1", "MB" ), SLOT2( "M2", "motor2", "MC" );

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