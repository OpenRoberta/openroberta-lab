package de.fhg.iais.roberta.javaServer.restServices.ev3;

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
@Path("/update")
public class Ev3Update {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3Update.class);

    private final String robotUpdateResourcesDir;

    @Inject
    public Ev3Update(@Named("robot.updateResources.dir") String robotUpdateResourcesDir) {
        this.robotUpdateResourcesDir = robotUpdateResourcesDir;
    }

    @GET
    @Path("/runtime")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getRuntime() throws FileNotFoundException {
        AliveData.rememberRobotCall();
        LOG.info("/update/runtime called");
        File jar = new File(this.robotUpdateResourcesDir + "/OpenRobertaRuntime.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=OpenRobertaRuntime.jar");
        return response.build();
    }

    @GET
    @Path("/shared")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getShared() throws FileNotFoundException {
        AliveData.rememberRobotCall();
        LOG.info("/update/shared called");
        File jar = new File(this.robotUpdateResourcesDir + "/OpenRobertaShared.jar");
        ResponseBuilder response = Response.ok(new FileInputStream(jar));
        response.header("Content-Disposition", "attachment; filename=OpenRobertaShared.jar");
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
        return response.build();
    }

}