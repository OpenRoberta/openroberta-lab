package de.fhg.iais.roberta.javaServer.provider;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcKeyException;

@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Provider
public class DbcKeyExceptionMapper implements ExceptionMapper<DbcKeyException> {
    private static final Logger LOG = LoggerFactory.getLogger(DbcKeyExceptionMapper.class);
    private static final String SERVER_ERROR = Key.SERVER_ERROR.getKey();
    static final String ERROR_IN_ERROR = "{\"rc\":\"error\",\"message\":\"" + SERVER_ERROR + "\",\"rc\":\"" + SERVER_ERROR + "\",}";

    @Override
    public Response toResponse(DbcKeyException e) {
        LOG.error("exception was caught at system border", e);
        try {
            final String errorKey = e.getKey().getKey();
            final JSONObject response = new JSONObject();
            response.put("rc", "error");
            response.put("message", errorKey);
            response.put("cause", errorKey);
            response.put("parameters", e.getParameter());
            Util.addFrontendInfo(response, null, null);
            return Response.ok(response).build();
        } catch ( Exception eInE ) {
            LOG.error("server error - exception in exception processor", eInE);
            return Response.ok(ERROR_IN_ERROR).build();
        }
    }
}