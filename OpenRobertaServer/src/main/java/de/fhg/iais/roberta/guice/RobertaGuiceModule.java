package de.fhg.iais.roberta.guice;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.fhg.iais.roberta.javaServer.restServices.all.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientConfiguration;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientPing;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientProgram;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientToolbox;
import de.fhg.iais.roberta.javaServer.restServices.all.ClientUser;
import de.fhg.iais.roberta.javaServer.restServices.all.RestExample;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3Command;
import de.fhg.iais.roberta.javaServer.restServices.ev3.Ev3DownloadJar;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CompilerWorkflow;

public class RobertaGuiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(RobertaGuiceModule.class);
    private final Properties openRobertaProperties;

    public RobertaGuiceModule(Properties openRobertaProperties) {
        this.openRobertaProperties = openRobertaProperties;
    }

    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(ClientAdmin.class);
        bind(ClientConfiguration.class);
        bind(ClientToolbox.class);
        bind(ClientProgram.class);
        bind(ClientUser.class);
        bind(Ev3DownloadJar.class);
        bind(Ev3Command.class);
        bind(RestExample.class);
        bind(ClientPing.class);

        bind(SessionFactoryWrapper.class).in(Singleton.class);
        bind(Ev3Communicator.class).in(Singleton.class);
        bind(Ev3CompilerWorkflow.class).in(Singleton.class);

        bind(String.class).annotatedWith(Names.named("hibernate.config.xml")).toInstance("hibernate-cfg.xml");

        try {
            Names.bindProperties(binder(), this.openRobertaProperties);
        } catch ( Exception e ) {
            LOG.error("Could not load properties", e);
        }
    }
}
