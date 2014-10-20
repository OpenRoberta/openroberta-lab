package de.fhg.iais.roberta.guice;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class RobertaGuiceServletConfig extends GuiceServletContextListener {
    Injector injector;

    @Override
    protected Injector getInjector() {
        JerseyServletModule jerseyServletModule = new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                // configure at least one JAX-RS resource or the server won't start.
                install(new RobertaGuiceModule());
                Map<String, String> initParams = new HashMap<String, String>();
                // initParams.put("com.sun.jersey.config.feature.Trace", "true");
                initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                initParams.put("com.sun.jersey.config.property.packages", "de.fhg.iais.roberta.javaServer.resources,de.fhg.iais.roberta.javaServer.provider");
                serve("/*").with(GuiceContainer.class, initParams);
            }
        };
        this.injector = Guice.createInjector(jerseyServletModule);
        return this.injector;
    }

    public Injector getCreatedInjector() {
        return this.injector;
    }
}
