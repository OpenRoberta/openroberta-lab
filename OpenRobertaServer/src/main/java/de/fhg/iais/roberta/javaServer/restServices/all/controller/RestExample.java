package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.KeyVal;

@Path("/hello")
public class RestExample {
    private static final Logger LOG = LoggerFactory.getLogger(RestExample.class);
    private static AtomicInteger hwCounter = new AtomicInteger(0);

    @Path("/json1")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response handle1() {
        LOG.info("somebody wants to see HW as json - pojo");
        KeyVal answer = new KeyVal("greeting", "Hello World");
        return Response.ok(answer).build();
    }

    @Path("/json2")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response handle2() throws Exception {
        LOG.info("somebody wants to see HW as json - jsonobject");
        JSONObject answer = new JSONObject().put("greeting", "Hello World").put("from", "jersey").put("to", "browser");
        return Response.ok(answer.toString()).build();
    }

    @Path("/txt")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleHw() {
        int cnt = hwCounter.addAndGet(1);
        LOG.info("somebody wants to see HW " + cnt);
        return Response.ok("Hello World " + cnt).build();
    }

}