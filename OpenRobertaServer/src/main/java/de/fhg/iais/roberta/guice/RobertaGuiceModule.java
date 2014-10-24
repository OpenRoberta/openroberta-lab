package de.fhg.iais.roberta.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import de.fhg.iais.roberta.brick.BrickCommunicator;
import de.fhg.iais.roberta.brick.CompilerWorkflow;
import de.fhg.iais.roberta.brick.Templates;
import de.fhg.iais.roberta.javaServer.resources.DownloadJar;
import de.fhg.iais.roberta.javaServer.resources.HelloWorld;
import de.fhg.iais.roberta.javaServer.resources.Ping;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestConfiguration;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.javaServer.resources.TokenReceiver;
import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;

public class RobertaGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(RestBlocks.class);
        bind(RestConfiguration.class);
        bind(RestProgram.class);
        bind(RestUser.class);
        bind(DownloadJar.class);
        bind(TokenReceiver.class);
        bind(HelloWorld.class);
        bind(Ping.class);

        bind(SessionFactoryWrapper.class).in(Singleton.class);
        bind(Templates.class).in(Singleton.class);
        bind(BrickCommunicator.class).in(Singleton.class);
        bind(CompilerWorkflow.class).in(Singleton.class);

        bind(String.class).annotatedWith(Names.named("hibernate-cfg.xml")).toInstance("hibernate-cfg.xml");
        bind(String.class).annotatedWith(Names.named("crosscompiler.basedir")).toInstance("../OpenRobertaRuntime/userProjects/"); // TODO: rm relative path!
        bind(String.class).annotatedWith(Names.named("crosscompiler.build.xml")).toInstance("../OpenRobertaRuntime/build.xml"); // TODO: rm relative path!
    }
}
