package de.fhg.iais.roberta.guice;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.javaServer.resources.Blocks;
import de.fhg.iais.roberta.javaServer.resources.DownloadleJOSJar;
import de.fhg.iais.roberta.javaServer.resources.ErrorMessageReceiver;
import de.fhg.iais.roberta.javaServer.resources.HelloWorld;
import de.fhg.iais.roberta.javaServer.resources.Ping;

public class RobertaGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(Blocks.class);
        bind(DownloadleJOSJar.class);
        bind(ErrorMessageReceiver.class);
        bind(HelloWorld.class);
        bind(Ping.class);
    }
}
