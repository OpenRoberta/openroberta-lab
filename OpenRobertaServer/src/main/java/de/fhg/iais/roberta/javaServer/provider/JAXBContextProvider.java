package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

/**
 * Extension of the extension interface {@link ContextResolver}. Provides a JAXB context to resource classes. This object can be used to create marshaller,
 * unmarshaller and namespace mappers. Uses the singleton class {@link JAXBProviderFactory} to create this context.
 * 
 * @author rbudde
 */
@Provider
public class JAXBContextProvider implements ContextResolver<JAXBContext> {
    @Override
    public JAXBContext getContext(Class<?> type) {
        return JAXBProviderFactory.getInstance().getJAXBContext(type);
    }
}