package de.fhg.iais.roberta.javaServer.provider;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.dbc.DbcKeyException;

@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Provider
public class DbcKeyExceptionMapper implements ExceptionMapper<DbcKeyException> {
    private static final Logger LOG = LoggerFactory.getLogger(DbcKeyExceptionMapper.class);
    private static final String SERVER_ERROR = Key.SERVER_ERROR.getKey();
    private static final int INIT_FAIL_MOD = 5;
    private static final AtomicInteger numberOfInitFails = new AtomicInteger();
    static final String ERROR_IN_ERROR = "{\"rc\":\"error\",\"message\":\"" + SERVER_ERROR + "\",\"rc\":\"" + SERVER_ERROR + "\",}";

    @Override
    public Response toResponse(DbcKeyException e) {
        final String errorKey = e.getKey().getKey();
        final BaseResponse response = BaseResponse.make();
        if ( errorKey.startsWith("ORA_INIT_FAIL") ) {
            int actualNumberOfInitFails = numberOfInitFails.getAndIncrement();
            if ( actualNumberOfInitFails < INIT_FAIL_MOD ) {
                LOG.error("init DbcKeyException was caught at system border: " + e.getMessage());
            } else if ( actualNumberOfInitFails == INIT_FAIL_MOD ) {
                LOG.error("init DbcKeyException logged every 10th occurences only");
            } else if ( actualNumberOfInitFails % INIT_FAIL_MOD == 0 ) {
                LOG.error(INIT_FAIL_MOD + " init DbcKeyException were caught at system border: " + e.getMessage());
            }
            response.setInitToken("invalid-token");
        } else {
            LOG.error("DbcKeyException was caught at system border", e);
        }
        try {
            response.setRc("error").setMessage(errorKey).setCause(errorKey).setParameters(new JSONObject(e.getParameter()));
            return UtilForREST.responseWithFrontendInfo(response, null, null);
        } catch ( Exception eInE ) {
            LOG.error("server error - exception in exception processor", eInE);
            return Response.ok(ERROR_IN_ERROR).build();
        }
    }
}