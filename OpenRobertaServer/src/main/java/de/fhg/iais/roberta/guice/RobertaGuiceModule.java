package de.fhg.iais.roberta.guice;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.javaServer.resources.Blocks;
import de.fhg.iais.roberta.javaServer.resources.DownloadJar;
import de.fhg.iais.roberta.javaServer.resources.TokenReceiver;
import de.fhg.iais.roberta.javaServer.resources.HelloWorld;
import de.fhg.iais.roberta.javaServer.resources.Ping;

public class RobertaGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(Blocks.class);
        bind(DownloadJar.class);
        bind(TokenReceiver.class);
        bind(HelloWorld.class);
        bind(Ping.class);
    }
}
