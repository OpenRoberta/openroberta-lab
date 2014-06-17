package de.fhg.iais.roberta.guice;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.javaServer.resources.Blocks;
import de.fhg.iais.roberta.javaServer.resources.DownloadleJOSJar;

public class RobertaGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(Blocks.class);
        bind(DownloadleJOSJar.class);
    }
}
