package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.ConfRequest;
import de.fhg.iais.roberta.generated.restEntities.ConfResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.SaveConfRequest;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;

@Path("/conf")
public class ClientConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);

    private final RobotCommunicator brickCommunicator;

    @Inject
    public ClientConfiguration(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveC")
    public Response saveConfig(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            int userId = httpSessionState.getUserId();
            String robotGroup = httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
            final String robotName = robotGroup != "" ? robotGroup : httpSessionState.getRobotName();
            SaveConfRequest request = SaveConfRequest.make(fullRequest.getData());
            String cmd = "saveC";
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState);
            String configurationName = request.getName();
            String configurationXml = request.getConfiguration();
            cp.updateConfiguration(configurationName, userId, robotName, configurationXml, true);
            UtilForREST.addResultInfo(response, cp);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveAsC")
    public Response saveAsConfig(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            int userId = httpSessionState.getUserId();
            String robotGroup = httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
            final String robotName = robotGroup != "" ? robotGroup : httpSessionState.getRobotName();
            SaveConfRequest request = SaveConfRequest.make(fullRequest.getData());
            String cmd = "saveAsC";
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState);

            String configurationName = request.getName();
            String configurationXml = request.getConfiguration();
            cp.updateConfiguration(configurationName, userId, robotName, configurationXml, false);
            UtilForREST.addResultInfo(response, cp);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loadC")
    public Response loadConfig(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            ConfResponse response = ConfResponse.make();
            int userId = httpSessionState.getUserId();
            String robotGroup = httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
            final String robotName = robotGroup != "" ? robotGroup : httpSessionState.getRobotName();
            ConfRequest request = ConfRequest.make(fullRequest.getData());
            String cmd = "loadC";
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);

            String configurationName = request.getName();
            String ownerName = request.getOwner().trim();
            if ( !ownerName.isEmpty() ) {
                User user = up.getUser(ownerName);
                if ( user != null ) {
                    userId = user.getId();
                }
            }
            String configurationText = cp.getConfigurationText(configurationName, userId, robotName);
            response.setConfXML(configurationText);
            UtilForREST.addResultInfo(response, cp);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteC")
    public Response deleteConfig(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            int userId = httpSessionState.getUserId();
            String robotGroup = httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
            final String robotName = robotGroup != "" ? robotGroup : httpSessionState.getRobotName();
            ConfRequest request = ConfRequest.make(fullRequest.getData());
            String cmd = "deleteC";
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState);

            String configurationName = request.getName();
            cp.deleteByName(configurationName, userId, robotName);
            UtilForREST.addResultInfo(response, cp);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loadCN")
    public Response loadConfigNames(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            ConfResponse response = ConfResponse.make();
            int userId = httpSessionState.getUserId();
            String robotGroup = httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
            final String robotName = robotGroup != "" ? robotGroup : httpSessionState.getRobotName();
            String cmd = "loadCN";
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            ConfigurationProcessor cp = new ConfigurationProcessor(dbSession, httpSessionState);

            JSONArray configurationInfo = cp.getConfigurationInfo(userId, robotName);
            response.setConfigurationNames(configurationInfo);
            UtilForREST.addResultInfo(response, cp);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }
}