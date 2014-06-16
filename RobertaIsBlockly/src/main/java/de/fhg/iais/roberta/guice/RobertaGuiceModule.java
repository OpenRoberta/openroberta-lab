package de.fhg.iais.roberta.guice;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;

public class RobertaGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BrickCommunicator.class);
        bind(SessionFactoryWrapper.class);
    }
}
