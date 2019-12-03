package de.fhg.iais.roberta.javaServer.provider;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.main.IIpToCountry;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.util.Clock;
import de.fhg.iais.roberta.util.ServerProperties;

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

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private IIpToCountry ipToCountry;

    @Inject
    private ServerProperties serverProperties;

    @Inject
    @Named("robotPluginMap")
    private Map<String, IRobotFactory> robotPluginMap;

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
        return () -> {
            return OraDataProvider.this.sessionFactoryWrapper.getSession();
        };
    }

    private Injectable<HttpSessionState> getInjectableHttpSessionState() {
        return () -> {
            HttpSession httpSession = OraDataProvider.this.servletRequest.getSession(true);
            HttpSessionState httpSessionState = (HttpSessionState) httpSession.getAttribute(OPEN_ROBERTA_STATE);
            if ( httpSessionState == null ) {
                httpSessionState = HttpSessionState.init(this.robotPluginMap, this.serverProperties, getCountryCode(servletRequest, ipToCountry));
                httpSession.setAttribute(OPEN_ROBERTA_STATE, httpSessionState);
            }
            return httpSessionState;
        };
    }

    private static String getCountryCode(HttpServletRequest servletRequest, IIpToCountry ipToCountry) {
        String remoteAddr = "";
        if ( servletRequest != null ) {
            remoteAddr = servletRequest.getHeader("X-FORWARDED-FOR");
            if ( remoteAddr == null || "".equals(remoteAddr) ) {
                remoteAddr = servletRequest.getRemoteAddr();
            }
        }
        InetAddress addrAsIp;
        String countryCode = "..";
        try {
            Clock getByNameTime = Clock.start();
            addrAsIp = InetAddress.getByName(remoteAddr);
            long elapsed = getByNameTime.elapsedMsec();
            if ( elapsed > 1000 ) {
                LOG.error("InetAddress.getByName(" + remoteAddr + ") + getCountryCode took " + elapsed + "msec");
            }
            countryCode = ipToCountry.getCountryCode(addrAsIp);
        } catch ( IOException e ) {
            LOG.info("Could not evaluate the actual ip as a country code. Likely a problem with the IpToCountry file.");
        }
        return countryCode;
    }
}