package de.fhg.iais.roberta.guice;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.brick.Templates;
import de.fhg.iais.roberta.javaServer.resources.BrickCommand;
import de.fhg.iais.roberta.javaServer.resources.DownloadJar;
import de.fhg.iais.roberta.javaServer.resources.HelloWorld;
import de.fhg.iais.roberta.javaServer.resources.Ping;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestConfiguration;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

public class RobertaGuiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(RobertaGuiceModule.class);
    private final Properties openRobertaProperties;

    public RobertaGuiceModule(Properties openRobertaProperties) {
        this.openRobertaProperties = openRobertaProperties;
    }

    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(RestBlocks.class);
        bind(RestConfiguration.class);
        bind(RestProgram.class);
        bind(RestUser.class);
        bind(DownloadJar.class);
        bind(BrickCommand.class);
        bind(HelloWorld.class);
        bind(Ping.class);

        bind(SessionFactoryWrapper.class).in(Singleton.class);
        bind(Templates.class).in(Singleton.class);
        bind(BrickCommunicator.class).in(Singleton.class);
        bind(CompilerWorkflow.class).in(Singleton.class);

        bind(String.class).annotatedWith(Names.named("hibernate.config.xml")).toInstance("hibernate-cfg.xml");

        try {
            Names.bindProperties(binder(), this.openRobertaProperties);
        } catch ( Exception e ) {
            LOG.error("Could not load properties", e);
        }
    }
}
