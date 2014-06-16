package de.fhg.iais.roberta.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import de.fhg.iais.roberta.javaServer.resources.Blocks;

public class RobertaGuiceServletConfig extends GuiceServletContextListener {
    Injector injector;

    @Override
    protected Injector getInjector() {
        JerseyServletModule jerseyServletModule = new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                install(new RobertaGuiceModule());
                // configure at least one JAX-RS resource or the server won't start.
                bind(Blocks.class);
                serve("/*").with(GuiceContainer.class);

            }
        };
        this.injector = Guice.createInjector(jerseyServletModule);
        return this.injector;
    }

    public Injector getCreatedInjector() {
        return this.injector;
    }
}
