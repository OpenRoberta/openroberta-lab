package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;

public enum ActorPort implements IActorPort {
    A( "MA" ), B( "MB" ), C( "MC" ), D( "MD" );

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