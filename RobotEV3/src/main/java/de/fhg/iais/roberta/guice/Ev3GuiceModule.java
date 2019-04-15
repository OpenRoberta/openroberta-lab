package de.fhg.iais.roberta.guice;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class Ev3GuiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3GuiceModule.class);
    private final Properties ev3Properties;

    public Ev3GuiceModule(Properties ev3Properties) {
        this.ev3Properties = ev3Properties;
    }

    @Override
    protected void configure() {
        try {
            Names.bindProperties(binder(), this.ev3Properties);
        } catch ( Exception e ) {
            LOG.error("Could not bind EV3 properties in guice", e);
        }
    }
}
