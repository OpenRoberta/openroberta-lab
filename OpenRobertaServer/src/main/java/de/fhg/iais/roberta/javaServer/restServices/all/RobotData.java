package de.fhg.iais.roberta.javaServer.restServices.all;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

@Path("/robotdata")
public class RobotData {
    private static final Logger LOG = LoggerFactory.getLogger(RobotData.class);
    private RobotCommunicator robotCommunicator;

    @Inject
    public RobotData(RobotCommunicator robotCommunicator) {
        this.robotCommunicator = robotCommunicator;
    }

    @Path("/summary")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response returnSummary() throws Exception {
        return Response.ok("").build();
    }
}