package de.fhg.iais.roberta.javaServer.restServices.ev3;

import java.io.File;
import java.io.FileInputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CommunicationData;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * REST service for downloading user program
 */
@Path("/download")
public class Ev3DownloadJar {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3DownloadJar.class);

    private final Ev3Communicator brickCommunicator;
    private final String pathToCrosscompilerBaseDir;

    @Inject
    public Ev3DownloadJar(Ev3Communicator brickCommunicator, @Named("crosscompiler.basedir") String pathToCrosscompilerBaseDir) {
        this.brickCommunicator = brickCommunicator;
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response handle(JSONObject requestEntity) throws JSONException {
        AliveData.rememberRobotCall();
        try {
            String token = requestEntity.getString("token");
            LOG.info("/download - request for token " + token);
            Ev3CommunicationData state = this.brickCommunicator.getState(token);
            String programName = state.getProgramName();
            String fileName = programName + ".jar";
            File jarDir = new File(this.pathToCrosscompilerBaseDir + token + "/target");
            String message = "unknown";
            if ( jarDir.isDirectory() ) {
                File jarFile = new File(jarDir, fileName);
                if ( jarFile.isFile() ) {
                    ResponseBuilder response = Response.ok(new FileInputStream(jarFile), MediaType.APPLICATION_OCTET_STREAM);
                    response.header("Content-Disposition", "attachment; filename=" + fileName);
                    response.header("Filename", fileName);
                    return response.build();
                } else {
                    message = "jar to upload to robot not found";
                }
            } else {
                message = "directory containg jar to upload to robot not found";
            }
            LOG.error("jar could not be uploaded to robot: " + message);
            return Response.serverError().build();
        } catch ( Exception e ) {
            LOG.error("exception caught and rethrown", e);
            throw new DbcException("exception caught and rethrown", e);
        }
    }
}