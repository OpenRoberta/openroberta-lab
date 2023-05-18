package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ServerProperties;

/**
 * for compatability: external services check the availability of the server calling this REST service. Migrated (duplicated :-<) to class ServerData
 */
@Path("/alive")
public class Alive {
    private static final Logger LOG = LoggerFactory.getLogger(Alive.class);
    private static final int EVERY_REQUEST = 100; // after arrival of EVERY_REQUEST many /alive requests, a log entry is written
    private static final AtomicLong aliveRequestCounterForLogging = new AtomicLong(0);

    private final ServerProperties serverProperties;

    @Inject
    public Alive(ServerProperties robotProperties) {
        this.serverProperties = robotProperties;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tellTheState() throws Exception {
        JSONObject answer = AliveData.getAndUpdateAliveState();
        answer.put("version", serverProperties.getStringProperty("openRobertaServer.version"));
        long counter = aliveRequestCounterForLogging.incrementAndGet();
        boolean logAlive = counter % EVERY_REQUEST == 0;
        if ( logAlive ) {
            LOG.info("the response to the the " + EVERY_REQUEST + ". /alive request is: " + answer.toString());
        }
        return Response.ok(answer.toString()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/text")
    public Response tellTheStateAsText() throws Exception {
        JSONObject answer = AliveData.getAndUpdateAliveState();
        answer.put("version", serverProperties.getStringProperty("openRobertaServer.version"));
        StringBuilder sb = new StringBuilder();
        String[] answerKeys = answer.keySet().toArray(new String[0]);
        Arrays.sort(answerKeys);
        for ( String key : answerKeys ) {
            sb.append(key).append(" : ").append(answer.get(key)).append("\n");
        }
        return Response.ok(sb.toString()).build();
    }
}