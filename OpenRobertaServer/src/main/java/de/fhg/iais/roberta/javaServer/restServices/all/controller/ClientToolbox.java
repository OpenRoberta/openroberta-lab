package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.ToolboxRequest;
import de.fhg.iais.roberta.generated.restEntities.ToolboxResponse;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.ToolboxProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.Toolbox;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;

@Path("/toolbox")
public class ClientToolbox {
    private static final Logger LOG = LoggerFactory.getLogger(ClientToolbox.class);

    private final RobotCommunicator brickCommunicator;

    @Inject
    public ClientToolbox(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            ToolboxResponse response = ToolboxResponse.make();
            ToolboxRequest request = ToolboxRequest.make(fullRequest.getData());
            String cmd = request.getCmd();
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            ToolboxProcessor tp = new ToolboxProcessor(dbSession, httpSessionState);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            if ( cmd.equals("loadT") ) {
                String toolboxName = request.getName();
                String ownerName = request.getOwner().trim();
                int userId = 0;
                if ( !ownerName.isEmpty() ) {
                    User user = up.getUser(ownerName);
                    if ( user != null ) {
                        userId = user.getId();
                    }
                }
                Toolbox toolbox = tp.getToolbox(toolboxName, userId, httpSessionState.getRobotName());
                if ( toolbox != null ) {
                    response.setData(toolbox.getToolboxText());
                }
                UtilForREST.addResultInfo(response, tp);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            } else {
                LOG.error("Invalid command: " + cmd);
                return UtilForREST.makeBaseResponseForError(Key.COMMAND_INVALID, httpSessionState, null);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }
}