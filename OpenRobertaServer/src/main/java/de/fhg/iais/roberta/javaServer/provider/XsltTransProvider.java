package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import de.fhg.iais.roberta.util.XsltTransformer;

@Provider
public class XsltTransProvider implements InjectableProvider<XsltTrans, Parameter> {

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<?> getInjectable(ComponentContext ic, XsltTrans a, Parameter p) {
        if ( XsltTransformer.class.isAssignableFrom(p.getParameterClass()) ) {
            return getInjectableXsltTransformer();
        } else {
            return null;
        }
    }

    private Injectable<XsltTransformer> getInjectableXsltTransformer() {
        return () -> new XsltTransformer();
    }
}