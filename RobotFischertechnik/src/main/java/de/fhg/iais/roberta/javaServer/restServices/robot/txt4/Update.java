package de.fhg.iais.roberta.javaServer.restServices.robot.txt4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.factory.RobotFactory;

/*
 * REST service for fetching txt4 python package with HAL
 */
@Path("/update/txt4")
public class Update {

    private final String robotUpdateResourcesDir;

    @Inject
    public Update(@Named("robotPluginMap") Map<String, RobotFactory> robotPluginMap) {
        this.robotUpdateResourcesDir = robotPluginMap.get("txt4").getPluginProperties().getUpdateDir();
    }

    @GET
    @Path("{version}/hal")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHal(@PathParam("version") String version) throws FileNotFoundException {
        File hal = new File(this.robotUpdateResourcesDir + "/" + version + "/roberta.zip");
        return Response
            .ok(hal, MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=\"" + hal.getName() + "\"")
            .header("Filename", hal.getName())
            .build();
    }

    @GET
    @Path("{version}/hal/checksum")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getChecksum(@PathParam("version") String version) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        java.nio.file.Path path = Paths.get(this.robotUpdateResourcesDir + "/" + version + "/roberta.zip");
        byte[] bytes = Files.readAllBytes(path);
        digest.update(bytes);
        byte[] result = digest.digest();
        return Response.ok(Base64.getEncoder().encodeToString(result), MediaType.TEXT_PLAIN).header("Content-Type", "text/plain").build();
    }
}
