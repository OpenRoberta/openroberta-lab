package de.fhg.iais.roberta.javaServer.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import de.fhg.iais.roberta.javaServer.resources.OpenRobertaSessionState;

@Provider
public class OpenRobertaSessionStateProvider implements InjectableProvider<OraSessionState, Parameter> {
    private static final String OPEN_ROBERTA_STATE = "openRobertaState";

    public OpenRobertaSessionStateProvider() {
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public Injectable<OpenRobertaSessionState> getInjectable(ComponentContext ic, OraSessionState a, Parameter p) {
        if ( OpenRobertaSessionState.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectable();
        } else {
            return null;
        }
    }

    private Injectable<OpenRobertaSessionState> getInjectable() {
        return new Injectable<OpenRobertaSessionState>() {
            @Override
            public OpenRobertaSessionState getValue() {
                HttpSession httpSession = OpenRobertaSessionStateProvider.this.servletRequest.getSession(true);
                OpenRobertaSessionState httpSessionState = (OpenRobertaSessionState) httpSession.getAttribute(OPEN_ROBERTA_STATE);
                if ( httpSessionState == null ) {
                    httpSessionState = OpenRobertaSessionState.init();
                    httpSession.setAttribute(OPEN_ROBERTA_STATE, httpSessionState);
                }
                return httpSessionState;
            }
        };
    }
}