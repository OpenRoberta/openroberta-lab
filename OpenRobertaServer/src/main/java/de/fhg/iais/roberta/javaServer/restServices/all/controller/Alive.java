package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.AliveData;

/**
 * for compatability: external services check the availability of the server calling this REST service. Migrated (duplicated :-<) to class ServerData
 */
@Path("/alive")
public class Alive {
    private static final Logger LOG = LoggerFactory.getLogger(Alive.class);
    private static final int EVERY_REQUEST = 100; // after arrival of EVERY_REQUEST many /alive requests, a log entry is written
    private static final AtomicLong aliveRequestCounterForLogging = new AtomicLong(0);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tellTheState() throws Exception {
        JSONObject answer = AliveData.getAndUpdateAliveState();
        long counter = aliveRequestCounterForLogging.incrementAndGet();
        boolean logAlive = counter % EVERY_REQUEST == 0;
        if ( logAlive ) {
            LOG.info("the response to the the " + EVERY_REQUEST + ". /alive request is: " + answer.toString());
        }
        return Response.ok(answer.toString()).build();
    }
}