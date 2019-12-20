package de.fhg.iais.roberta.javaServer.restServices.robot.ev3.lejos.v0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

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

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.AliveData;

/**
 * REST service for updating brick libraries and menu.<br>
 */
@Path("/update")
public class Update {
    private static final Logger LOG = LoggerFactory.getLogger(Update.class);

    private final String robotUpdateResourcesDir;

    @Inject
    public Update(@Named("robotPluginMap") Map<String, IRobotFactory> robotPluginMap) {
        this.robotUpdateResourcesDir = robotPluginMap.get("ev3lejosv0").getPluginProperties().getUpdateDir();
    }

    @GET
    @Path("/runtime")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getRuntime() throws FileNotFoundException {
        AliveData.rememberRobotCall(-1);
        LOG.info("/update/runtime called");
        File jar = new File(this.robotUpdateResourcesDir + "/EV3Runtime.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=EV3Runtime.jar");
        response.header("Filename", "EV3Runtime.jar");
        return response.build();
    }

    @GET
    @Path("/shared")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Deprecated
    /**
     * After restructuring our projects we do not have any more of the OpenRobertaShared library
     *
     * @return
     * @throws FileNotFoundException
     */
    public Response getShared() throws FileNotFoundException {
        // old versions of the menu will require the OpenRobertaShared.jar
        // since we do not have any more we pass the EV3Runtime twice
        AliveData.rememberRobotCall(-1);
        LOG.info("/update/shared called");
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
        AliveData.rememberRobotCall(-1);
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
        AliveData.rememberRobotCall(-1);
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
        AliveData.rememberRobotCall(-1);
        LOG.info("/update/ev3menu called");
        File jar = new File(this.robotUpdateResourcesDir + "/EV3Menu.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=EV3Menu.jar");
        response.header("Filename", "EV3Menu.jar");
        return response.build();
    }
}