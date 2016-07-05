package de.fhg.iais.roberta.factory.action.generic;

import de.fhg.iais.roberta.factory.action.IActorPort;

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