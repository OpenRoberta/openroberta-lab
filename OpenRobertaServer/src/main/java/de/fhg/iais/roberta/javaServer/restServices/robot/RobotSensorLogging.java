package de.fhg.iais.roberta.javaServer.restServices.robot;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

/**
 * REST service for downloading user program
 */
@Path("/sensorlogging")
public class RobotSensorLogging {
    private static final Logger LOG = LoggerFactory.getLogger(RobotSensorLogging.class);

    private final RobotCommunicator brickCommunicator;

    @Inject
    public RobotSensorLogging(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response handle(JSONObject requestEntity) {
        String token = (String) requestEntity.remove("token");
        RobotCommunicationData state = this.brickCommunicator.getState(token);
        state.setSensorValues(requestEntity);
        LOG.info(requestEntity.toString());
        return Response.ok().build();
    }
}