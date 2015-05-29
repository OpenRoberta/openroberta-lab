package de.fhg.iais.roberta.javaServer.restServices.all;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.AliveData;

@Path("/alive")
public class Alive {
    private static final Logger LOG = LoggerFactory.getLogger(Alive.class);
    private static final int EVERY_REQUEST = 10; // after EVERY_PING many /alive requests have arrived, a log entry is written
    private static final AtomicInteger aliveRequestCounterForLogging = new AtomicInteger(0);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tellTheState() throws Exception {
        JSONObject answer = AliveData.getAndUpdateAliveState();
        int counter = aliveRequestCounterForLogging.incrementAndGet();
        boolean logAlive = counter % EVERY_REQUEST == 0;
        if ( logAlive ) {
            aliveRequestCounterForLogging.set(0);
            LOG.info("server running since " + answer.getString("runningSince"));
        }
        return Response.ok(answer).build();
    }
}