package de.fhg.iais.roberta.javaServer.restServices.all;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.jaxb.ConfigurationHelper;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Option;
import de.fhg.iais.roberta.util.Util;

@Path("/conf")
public class ClientConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);

    private final Ev3Communicator brickCommunicator;

    @Inject
    public ClientConfiguration(Ev3Communicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, @OraData DbSession dbSession, JSONObject fullRequest) throws Exception {
        AliveData.rememberClientCall();
        new ClientLogger().log(LOG, fullRequest);
        final int userId = httpSessionState.getUserId();
        JSONObject response = new JSONObject();
        try {
            JSONObject request = fullRequest.getJSONObject("data");
            String cmd = request.getString("cmd");
            LOG.info("command is: " + cmd);
            response.put("cmd", cmd);
            ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState);
            if ( cmd.equals("saveC") ) {
                String configurationName = request.getString("name");
                String configurationXml = request.getString("configuration");
                String configurationText = ConfigurationHelper.xmlString2textString(configurationName, configurationXml);
                cp.updateConfiguration(configurationName, userId, configurationText, true);
                Util.addResultInfo(response, cp);

            } else if ( cmd.equals("saveAsC") ) {
                String configurationName = request.getString("name");
                String configurationXml = request.getString("configuration");
                String configurationText = ConfigurationHelper.xmlString2textString(configurationName, configurationXml);
                cp.updateConfiguration(configurationName, userId, configurationText, false);
                Util.addResultInfo(response, cp);

            } else if ( cmd.equals("loadC") && httpSessionState.isUserLoggedIn() ) {
                String configurationName = request.getString("name");
                Configuration configuration = cp.getConfiguration(configurationName, userId);
                if ( configuration != null ) {
                    Option<String> msgConfigurationXml = ConfigurationHelper.textString2xmlString(configuration.getConfigurationText());
                    if ( msgConfigurationXml.isSet() ) {
                        response.put("data", msgConfigurationXml.getVal());
                    } else {
                        LOG.error("Configuration: " + configurationName + " parse error: " + msgConfigurationXml.getMessage());
                        Util.addErrorInfo(response, Key.SERVER_ERROR);
                    }
                }
                Util.addResultInfo(response, cp);

            } else if ( cmd.equals("deleteC") && httpSessionState.isUserLoggedIn() ) {
                String configurationName = request.getString("name");
                cp.deleteByName(configurationName, userId);
                Util.addResultInfo(response, cp);

            } else if ( cmd.equals("loadCN") && httpSessionState.isUserLoggedIn() ) {
                JSONArray configurationInfo = cp.getConfigurationInfo(userId);
                response.put("configurationNames", configurationInfo);
                Util.addResultInfo(response, cp);

            } else {
                LOG.error("Invalid command: " + cmd);
                Util.addErrorInfo(response, Key.COMMAND_INVALID);
            }
            dbSession.commit();
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        Util.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
        return Response.ok(response).build();
    }
}