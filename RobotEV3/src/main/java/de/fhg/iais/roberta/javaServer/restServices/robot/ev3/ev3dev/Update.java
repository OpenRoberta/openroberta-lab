package de.fhg.iais.roberta.javaServer.restServices.robot.ev3.ev3dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.AliveData;

/**
 * REST service for updating brick libraries and menu.<br>
 */
@Path("/update/ev3dev")
public class Update {
    private static final Logger LOG = LoggerFactory.getLogger(Update.class);

    private final String robotUpdateResourcesDir;

    @Inject
    public Update(@Named("robotPluginMap") Map<String, IRobotFactory> robotPluginMap) {
        this.robotUpdateResourcesDir = robotPluginMap.get("ev3dev").getPluginProperties().getUpdateDir();
    }

    @GET
    @Path("/runtime")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getRuntime() throws FileNotFoundException {
        AliveData.rememberRobotCall(-1);
        LOG.info("/update/ev3dev/runtime called");
        File bin = new File(this.robotUpdateResourcesDir + "roberta.zip");
        Response.ResponseBuilder response = Response.ok(new FileInputStream(bin));
        response.header("Content-Disposition", "attachment; filename=roberta.zip");
        response.header("Filename", "roberta.zip");
        return response.build();
    }
}
