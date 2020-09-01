package de.fhg.iais.roberta.javaServer.provider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import de.fhg.iais.roberta.generated.restEntities.InitRequest;
import de.fhg.iais.roberta.util.UtilForREST;

@Provider
@Consumes("application/json")
public class InitRequestProvider implements MessageBodyReader<InitRequest> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == InitRequest.class;
    }

    @Override
    public InitRequest readFrom(
        Class<InitRequest> type,
        Type genericType,
        Annotation[] annotations,
        MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders,
        InputStream entityStream)
        throws IOException,
        WebApplicationException {

        String entity = UtilForREST.convertStreamToString(entityStream);
        return InitRequest.makeFromString(entity);
    }
}