package de.fhg.iais.roberta.factory;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class NAOGuiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(NAOGuiceModule.class);
    private final Properties naoProperties;

    public NAOGuiceModule(Properties naoProperties) {
        this.naoProperties = naoProperties;
    }

    @Override
    protected void configure() {
        try {
            Names.bindProperties(binder(), this.naoProperties);
        } catch ( Exception e ) {
            LOG.error("Could not bind NAO properties in guice", e);
        }
    }
}
