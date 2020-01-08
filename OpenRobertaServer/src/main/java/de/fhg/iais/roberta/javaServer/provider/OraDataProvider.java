package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

@Provider
public class OraDataProvider implements InjectableProvider<OraData, Parameter> {
    private static final Logger LOG = LoggerFactory.getLogger(OraDataProvider.class);
    public static final String OPEN_ROBERTA_STATE = "openRobertaState";

    public OraDataProvider() {
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Inject
    private SessionFactoryWrapper sessionFactoryWrapper;

    @Override
    public Injectable<?> getInjectable(ComponentContext ic, OraData a, Parameter p) {
        if ( DbSession.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectableDbSessionState();
        } else {
            return null;
        }
    }

    private Injectable<DbSession> getInjectableDbSessionState() {
        return () -> {
            return OraDataProvider.this.sessionFactoryWrapper.getSession();
        };
    }
}