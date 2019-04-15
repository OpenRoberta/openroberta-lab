package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.Unmarshaller;

/**
 * Extension of the extension interface {@link ContextResolver}. Provides a unmarshaller to resource classes. Uses the singleton class
 * {@link JAXBProviderFactory} to create this context.
 * 
 * @author rbudde
 */
@Provider
public class JAXBUnmarshallerProvider implements ContextResolver<Unmarshaller> {
    @Override
    public Unmarshaller getContext(Class<?> type) {
        return JAXBProviderFactory.getInstance().getUnmarshaller(type);
    }
}