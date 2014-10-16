package de.fhg.iais.roberta.guice;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.javaServer.resources.DownloadJar;
import de.fhg.iais.roberta.javaServer.resources.HelloWorld;
import de.fhg.iais.roberta.javaServer.resources.Ping;
import de.fhg.iais.roberta.javaServer.resources.RestBlocks;
import de.fhg.iais.roberta.javaServer.resources.RestConfiguration;
import de.fhg.iais.roberta.javaServer.resources.RestProgram;
import de.fhg.iais.roberta.javaServer.resources.RestUser;
import de.fhg.iais.roberta.javaServer.resources.TokenReceiver;

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
    }
}
