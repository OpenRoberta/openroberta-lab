package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.UtilForREST;

@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeExceptionMapper.class);
    private static final String SERVER_ERROR = Key.SERVER_ERROR.getKey();
    static final String ERROR_IN_ERROR = "{\"rc\":\"error\",\"message\":\"" + SERVER_ERROR + "\",\"rc\":\"" + SERVER_ERROR + "\",\"initToken\":\"error\"}";

    @Override
    public Response toResponse(RuntimeException e) {
        try {
            String errorMessage = e.getMessage();
            errorMessage =
                (errorMessage == null) ? "-no error message in exception-" : errorMessage.length() <= 60 ? errorMessage : errorMessage.substring(0, 60);
            LOG.error("server error - exception: " + errorMessage, e);
            final BaseResponse response = BaseResponse.make();
            String keyAsString = Key.SERVER_ERROR.toString();
            response.setRc("error").setMessage(keyAsString).setCause(keyAsString).setInitToken("error");
            return UtilForREST.responseWithFrontendInfo(response, null, null);
        } catch ( Exception eInE ) {
            LOG.error("server error - exception in exception processor", eInE);
            return Response.ok(ERROR_IN_ERROR).build();
        }
    }
}