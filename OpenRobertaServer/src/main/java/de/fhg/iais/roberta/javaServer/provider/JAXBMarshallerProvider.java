package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.Marshaller;

/**
 * Extension of the extension interface {@link ContextResolver}. Provides a marshaller to resource classes. Uses the singleton class {@link JAXBProviderFactory}
 * to create this context.
 * 
 * @author rbudde
 */
@Provider
public class JAXBMarshallerProvider implements ContextResolver<Marshaller> {
    @Override
    public Marshaller getContext(Class<?> type) {
        return JAXBProviderFactory.getInstance().getMarshaller(type);
    }
}