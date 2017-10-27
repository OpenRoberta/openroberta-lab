package de.fhg.iais.roberta.javaServer.restServices.robot.ev3.lejos.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.util.AliveData;

/**
 * REST service for updating brick libraries and menu.<br>
 */
@Path("/update/v1")
public class Update {
    private static final Logger LOG = LoggerFactory.getLogger(Update.class);

    private final String robotUpdateResourcesDir;

    @Inject
    public Update(@Named("robot.plugin.11.updateResources.dir") String robotUpdateResourcesDir) {
        this.robotUpdateResourcesDir = robotUpdateResourcesDir;
    }

    @GET
    @Path("/runtime")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getRuntime() throws FileNotFoundException {
        AliveData.rememberRobotCall();
        LOG.info("/update/runtime called");
        File jar = new File(this.robotUpdateResourcesDir + "/EV3Runtime.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=EV3Runtime.jar");
        response.header("Filename", "EV3Runtime.jar");
        return response.build();
    }

    @GET
    @Path("/jsonlib")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getJsonLib() throws FileNotFoundException {
        AliveData.rememberRobotCall();
        LOG.info("/update/jsonlib called");
        File jar = new File(this.robotUpdateResourcesDir + "/json.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=json.jar");
        response.header("Filename", "json.jar");
        return response.build();
    }

    @GET
    @Path("/websocketlib")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getWebSocketLib() throws FileNotFoundException {
        AliveData.rememberRobotCall();
        LOG.info("/update/websocketlib called");
        File jar = new File(this.robotUpdateResourcesDir + "/Java-WebSocket.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=Java-WebSocket.jar");
        response.header("Filename", "Java-WebSocket.jar");
        return response.build();
    }

    @GET
    @Path("/ev3menu")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getMenu() throws FileNotFoundException {
        AliveData.rememberRobotCall();
        LOG.info("/update/ev3menu called");
        File jar = new File(this.robotUpdateResourcesDir + "/EV3Menu.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=EV3Menu.jar");
        response.header("Filename", "EV3Menu.jar");
        return response.build();
    }
}