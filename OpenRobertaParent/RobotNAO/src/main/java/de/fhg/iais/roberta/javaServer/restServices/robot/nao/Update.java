package de.fhg.iais.roberta.javaServer.restServices.robot.nao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/*
 * REST service for fetching NAO python package with HAL
 */
@Path("/update/nao/v2-1-4-3")
public class Update {
    private static final Logger LOG = LoggerFactory.getLogger(Update.class);
    private final String robotUpdateResourcesDir;

    @Inject
    public Update(@Named("robot.plugin.5.updateResources.dir") String robotUpdateResourcesDir) {
        this.robotUpdateResourcesDir = robotUpdateResourcesDir;
    }

    @GET
    @Path("/hal")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHal() throws FileNotFoundException {
        File hal = new File(this.robotUpdateResourcesDir + "/roberta.zip");
        return Response
            .ok(hal, MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=\"" + hal.getName() + "\"")
            .header("Filename", hal.getName())
            .build();
    }

    @GET
    @Path("/hal/checksum")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getChecksum() throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        java.nio.file.Path path = Paths.get(this.robotUpdateResourcesDir + "/roberta.zip");
        byte[] bytes = Files.readAllBytes(path);
        digest.update(bytes);
        byte[] result = digest.digest();
        return Response.ok(Base64.getEncoder().encodeToString(result), MediaType.TEXT_PLAIN).header("Content-Type", "text/plain").build();
    }
}
