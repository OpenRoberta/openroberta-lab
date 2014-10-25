package de.fhg.iais.roberta.javaServer.provider;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.connector.DbSession;
import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;

@Provider
public class OraDataProvider implements InjectableProvider<OraData, Parameter> {
    private static final Logger LOG = LoggerFactory.getLogger(OraDataProvider.class);
    private static final String OPEN_ROBERTA_STATE = "openRobertaState";
    private static final AtomicLong SESSION_COUNTER = new AtomicLong();

    public OraDataProvider() {
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private SessionFactoryWrapper sessionFactoryWrapper;

    @Override
    public Injectable<?> getInjectable(ComponentContext ic, OraData a, Parameter p) {
        if ( HttpSessionState.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectableHttpSessionState();
        } else if ( DbSession.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectableDbSessionState();
        } else {
            return null;
        }
    }

    private Injectable<DbSession> getInjectableDbSessionState() {
        return new Injectable<DbSession>() {
            @Override
            public DbSession getValue() {
                return OraDataProvider.this.sessionFactoryWrapper.getSession();
            }
        };
    }

    private Injectable<HttpSessionState> getInjectableHttpSessionState() {
        return new Injectable<HttpSessionState>() {
            @Override
            public HttpSessionState getValue() {
                HttpSession httpSession = OraDataProvider.this.servletRequest.getSession(true);
                HttpSessionState httpSessionState = (HttpSessionState) httpSession.getAttribute(OPEN_ROBERTA_STATE);
                if ( httpSessionState == null ) {
                    long sessionNumber = SESSION_COUNTER.incrementAndGet();
                    LOG.info("session #" + sessionNumber + " created");
                    httpSessionState = HttpSessionState.init();
                    httpSession.setAttribute(OPEN_ROBERTA_STATE, httpSessionState);
                }
                return httpSessionState;
            }
        };
    }
}