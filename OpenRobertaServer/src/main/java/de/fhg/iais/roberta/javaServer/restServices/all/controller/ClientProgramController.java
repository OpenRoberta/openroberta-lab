package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.UnmarshalException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.EntityResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.ImportErrorResponse;
import de.fhg.iais.roberta.generated.restEntities.ImportRequest;
import de.fhg.iais.roberta.generated.restEntities.ImportResponse;
import de.fhg.iais.roberta.generated.restEntities.LikeRequest;
import de.fhg.iais.roberta.generated.restEntities.ListingNamesResponse;
import de.fhg.iais.roberta.generated.restEntities.ListingResponse;
import de.fhg.iais.roberta.generated.restEntities.SaveRequest;
import de.fhg.iais.roberta.generated.restEntities.SaveResponse;
import de.fhg.iais.roberta.generated.restEntities.ShareCreateRequest;
import de.fhg.iais.roberta.generated.restEntities.ShareDeleteRequest;
import de.fhg.iais.roberta.generated.restEntities.ShareRequest;
import de.fhg.iais.roberta.generated.restEntities.ShareResponse;
import de.fhg.iais.roberta.generated.restEntities.UserGroupProgramListRequest;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.provider.XsltTrans;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.LikeProcessor;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.ProgramShareProcessor;
import de.fhg.iais.roberta.persistence.UserGroupProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.ProgramShare;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.XsltTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

@Path("/program")
public class ClientProgramController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientProgramController.class);

    private final boolean isPublicServer;

    @Inject
    public ClientProgramController(ServerProperties serverProperties) {
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveProgram(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            SaveResponse response = SaveResponse.make();
            SaveRequest saveRequest = SaveRequest.make(request.getData());
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            String robot = getRobot(httpSessionState);
            Long timestamp = saveRequest.getTimestamp();
            Timestamp programTimestamp = timestamp == null ? null : new Timestamp(timestamp);
            String programName = saveRequest.getProgramName();
            String programText = saveRequest.getProgXML();
            String configName = saveRequest.getConfigName();
            String configText = saveRequest.getConfXML();
            String ownerName = saveRequest.getOwnerAccount();
            boolean isSaveCommand = saveRequest.getCmd().equals("save");

            if ( !isSaveCommand ) {
                programTimestamp = null;
            }

            Program program = programProcessor.persistProgramText(programName, ownerName, programText, configName, configText, robot, programTimestamp);

            response.setLastChanged((program != null) ? program.getLastChanged().getTime() : -1);
            UtilForREST.addResultInfo(response, programProcessor);
            Statistics.info("ProgramSave", "success", programProcessor.succeeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: setParameters(errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProgram(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            BaseResponse response = BaseResponse.make();
            JSONObject dataPart = request.getData();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                Statistics.info("ProgramDelete", "success", false);
            } else {
                String programName;
                String author;
                programName = dataPart.getString("programName");
                author = dataPart.getString("author");
                programProcessor.deleteByName(programName, userId, robot, author);
                UtilForREST.addResultInfo(response, programProcessor);
                Statistics.info("ProgramDelete", "success", programProcessor.succeeded());
            }
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: setParameters(errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/listing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgram(@OraData DbSession dbSession, @XsltTrans XsltTransformer xsltTransformer, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            ListingResponse response = ListingResponse.make();
            JSONObject dataPart = request.getData();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() && !dataPart.getString("owner").equals("Roberta") && !dataPart.getString("owner").equals("Gallery") ) {
                LOG.info("Unauthorized load request");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String programName = dataPart.getString("programName");
                String ownerName = dataPart.getString("owner");
                String author = dataPart.getString("author");
                String robot = getRobot(httpSessionState);

                Program program = programProcessor.getProgram(programName, ownerName, robot, author);
                if ( program == null ) {
                    return UtilForREST.makeBaseResponseForError(programProcessor.getMessage(), httpSessionState, null);
                } else {
                    String configText = programProcessor.getProgramsConfig(program);
                    String transformedXml = xsltTransformer.transform(program.getProgramText());
                    Pair<String, String> progConfPair = transformBetweenVersions(httpSessionState.getRobotFactory(), transformedXml, configText);
                    response.setProgXML(progConfPair.getFirst());
                    response.setConfigName(program.getConfigName()); // may be null, if an anonymous configuration is used
                    response.setConfXML(progConfPair.getSecond()); // may be null, if the default configuration is used
                    response.setLastChanged(program.getLastChanged().getTime());
                    // count the views if the program is from the gallery!
                    if ( ownerName.equals("Gallery") ) {
                        programProcessor.addOneView(program);
                    }
                    UtilForREST.addResultInfo(response, programProcessor);
                    Statistics.info("ProgramLoad", "success", programProcessor.succeeded());
                    return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
                }
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: setParameters(errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramEntity(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            EntityResponse response = EntityResponse.make();
            JSONObject dataPart = request.getData();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.info("Unauthorized entity request");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String robot = getRobot(httpSessionState);
                String programName = dataPart.getString("programName");
                String ownerName = dataPart.getString("owner");
                String author = dataPart.getString("author");
                User owner = up.getUser(ownerName);
                int ownerID = owner.getId();
                int authorId = up.getUser(author).getId();
                JSONArray program = programProcessor.getProgramEntity(programName, ownerID, robot, authorId);
                if ( program != null ) {
                    response.setProgram(program);
                }
                UtilForREST.addResultInfo(response, programProcessor);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/listing/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfosOfProgramsOfLoggedInUser(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized listing request");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                int userId = httpSessionState.getUserId();
                JSONArray programInfo = programProcessor.getProgramInfoOfProgramsOwnedByOrSharedWithUser(userId, robot);

                if ( !programProcessor.succeeded() ) {
                    if ( programProcessor.getMessage().equals(Key.PROGRAM_GET_ALL_ERROR_USER_NOT_FOUND) ) {
                        return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
                    } else {
                        return UtilForREST.makeBaseResponseForError(programProcessor.getMessage(), httpSessionState, null);
                    }
                }

                response.setProgramNames(programInfo);
                UtilForREST.addResultInfo(response, programProcessor);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/userGroupMembers/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramInfoOfProgramsOwnedByUserGroupMembers(@OraData DbSession dbSession, FullRestRequest fullRequest) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            UserGroupProgramListRequest request = UserGroupProgramListRequest.make(fullRequest.getData());

            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState, isPublicServer);
            String robot = getRobot(httpSessionState);

            User user = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( user == null ) {
                LOG.error("Unauthorized");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            }

            String groupName = request.getGroupName();
            UserGroup userGroup = userGroupProcessor.getGroup(groupName, user);
            if ( userGroup == null ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, null);
            }

            JSONArray programInfo = programProcessor.getProgramInfoOfProgramsOwnedByUserGroupMembers(userGroup, robot);
            if ( programInfo == null ) {
                return UtilForREST.makeBaseResponseForError(programProcessor.getMessage(), httpSessionState, null);
            }

            //Set the result info before you get the shared programs, because if the programInfo can be gathered the call is already successful
            UtilForREST.addResultInfo(response, programProcessor);

            JSONArray sharedPrograms = programProcessor.getProgramsSharedWithUserGroup(userGroup, robot);
            if ( sharedPrograms != null ) {
                for ( int i = 0; i < sharedPrograms.length(); i++ ) {
                    programInfo.put(sharedPrograms.get(i));
                }
            }

            response.setProgramNames(programInfo);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/examples/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfosOfExamplePrograms(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            String robot = getRobot(httpSessionState);
            int userId = 1;
            JSONArray programInfo = programProcessor.getProgramInfoOfProgramsOwnedByOrSharedWithUser(userId, robot);
            response.setProgramNames(programInfo);
            UtilForREST.addResultInfo(response, programProcessor);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importProgram(@XsltTrans XsltTransformer xsltTransformer, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        ImportRequest importRequest = ImportRequest.make(request.getData());
        ImportResponse response = ImportResponse.make();
        try {
            String robot = getRobot(httpSessionState);
            String xmlText = importRequest.getProgXML();
            xmlText = UtilForHtmlXml.checkProgramTextForXSS(xmlText);
            if ( xmlText.contains("robottype=\"ardu\"") ) {
                xmlText = xmlText.replaceAll("robottype=\"ardu\"", "robottype=\"botnroll\"");
                LOG.warn("Ardu to botnroll renaming on import should be removed in future.");
            }
            String programName = importRequest.getProgramName();
            if ( !Util.isValidJavaIdentifier(programName) ) {
                programName = "NEPOprog";
            }
            String transformedXml = xsltTransformer.transform(xmlText);
            Export jaxbImportExport;
            try {
                jaxbImportExport = JaxbHelper.xml2Element(transformedXml, Export.class);
            } catch ( UnmarshalException e ) {
                jaxbImportExport = null;
            }
            if ( jaxbImportExport != null ) {
                String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                if ( robotType1.equals(robot) && robotType2.equals(robot) ) {
                    response.setProgramName(programName);
                    Pair<String, String> progConfPair =
                        transformBetweenVersions(
                            httpSessionState.getRobotFactory(),
                            JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet()),
                            JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet()));
                    response.setProgXML(progConfPair.getFirst());
                    response.setConfXML(progConfPair.getSecond());
                    UtilForREST.addSuccessInfo(response, Key.PROGRAM_IMPORT_SUCCESS);
                    Statistics.info("ProgramImport", "success", true);
                    return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
                } else {
                    List<IRobotFactory> members = httpSessionState.getRobotFactoriesOfGroup(robotType1);
                    List<String> realNames = members.stream().map(IRobotFactory::getRealName).collect(Collectors.toList());
                    Statistics.info("ProgramImport", "success", false);
                    ImportErrorResponse error = ImportErrorResponse.make();
                    error.setRobotTypes(String.join(", ", realNames));
                    UtilForREST.addErrorInfo(error, Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE);
                    return UtilForREST.responseWithFrontendInfo(error, httpSessionState, null);
                }
            } else {
                UtilForREST.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR);
                return UtilForREST.makeBaseResponseForError(Key.PROGRAM_IMPORT_ERROR, httpSessionState, null);
            }
        } catch ( Exception e ) { // JaxbHelper methods throw Exception
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        }
    }

    @POST
    @Path("/share")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareProgram(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            ShareResponse response = ShareResponse.make();
            ShareRequest shareRequest = ShareRequest.make(request.getData());
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            ProgramShareProcessor programShareProcessor = new ProgramShareProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized share request");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                User user = userProcessor.getUser(userId);
                if ( this.isPublicServer ) {
                    if ( user == null ) {
                        return UtilForREST.makeBaseResponseForError(Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE, httpSessionState, null);
                    } else if ( !user.isActivated() && user.getUserGroup() == null ) {
                        return UtilForREST.makeBaseResponseForError(Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE, httpSessionState, null);
                    }
                }
                String programName = shareRequest.getProgramName();
                JSONObject shareData = shareRequest.getShareData();
                String entityLabel = shareData.getString("label");
                String entityType = shareData.getString("type");
                String right = shareData.getString("right");

                Set<ProgramShare> shares = programShareProcessor.shareToEntity(userId, robot, programName, entityLabel, entityType, right);

                JSONArray jsonShares = new JSONArray();
                JSONObject tmpJsonObj;
                for ( ProgramShare share : shares ) {
                    tmpJsonObj = new JSONObject();
                    tmpJsonObj.put("type", share.getEntityType());
                    tmpJsonObj.put("label", share.getEntityLabel());
                    tmpJsonObj.put("right", share.getRelation().toString());
                    jsonShares.put(tmpJsonObj);
                }

                response.setSharedWith(jsonShares);

                UtilForREST.addResultInfo(response, programShareProcessor);
                Statistics.info("ProgramShare", "success", programShareProcessor.succeeded());
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
            }
        } catch ( Exception e ) { // UserProcessor throws Exception
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeProgram(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            BaseResponse response = BaseResponse.make();
            LikeRequest likeRequest = LikeRequest.make(request.getData());
            LikeProcessor lp = new LikeProcessor(dbSession, httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                Statistics.info("GalleryLike", "success", false);
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String programName = likeRequest.getProgramName();
                String robotName = likeRequest.getRobotName();
                String author = likeRequest.getAuthor();
                boolean like = likeRequest.getLike();
                if ( like ) {
                    lp.createLike(programName, robotName, author);
                    if ( lp.succeeded() ) {
                        // nothing to do; argument: deleted tracks whether a like was set or taken away
                        Statistics.info("GalleryLike", "success", true, "deleted", false);
                    } else {
                        Statistics.info("GalleryLike", "success", false);
                        return UtilForREST.makeBaseResponseForError(Key.LIKE_SAVE_ERROR_EXISTS, httpSessionState, null);
                    }
                } else {
                    lp.deleteLike(programName, robotName, author);
                    Statistics.info("GalleryLike", "success", true, "deleted", true);
                }
                UtilForREST.addResultInfo(response, lp);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
            }
        } catch ( Exception e ) { // LikeProcessor throws Exception
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/share/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareProgramInGallery(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            SaveResponse response = SaveResponse.make();
            ShareCreateRequest shareCreateRequest = ShareCreateRequest.make(request.getData());

            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            ProgramShareProcessor accessRightProcessor = new ProgramShareProcessor(dbSession, httpSessionState);
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(dbSession, httpSessionState);

            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String programName = shareCreateRequest.getProgramName();
                User galleryUser = userProcessor.getUser("Gallery");
                // generating a unique name for the program owned by the gallery.
                User user = userProcessor.getUser(userId);
                String userAccount = user.getAccount();
                if ( !this.isPublicServer || user != null && user.isActivated() ) {
                    // get the program from the origin user to share with the gallery
                    Program program = programProcessor.getProgramAndLockTable(programName, userAccount, robot, userAccount);
                    String confText;
                    if ( program != null ) {
                        if ( program.getConfigName() == null ) {
                            if ( program.getConfigHash() == null ) {
                                confText = null;
                            } else {
                                ConfigurationDao confDao = new ConfigurationDao(dbSession);
                                confText = confDao.load(program.getConfigHash()).getConfigurationText();
                            }
                        } else {
                            confText = configurationProcessor.getConfigurationText(program.getConfigName(), userId, robot);
                        }
                        // make a copy of the user program and store it as a gallery owned program
                        Program programCopy =
                            programProcessor.persistProgramText(programName, galleryUser.getAccount(), program.getProgramText(), null, confText, robot, null);
                        if ( programProcessor.succeeded() ) {
                            if ( programCopy != null ) {
                                response.setLastChanged(programCopy.getLastChanged().getTime());
                                // share the copy of the program with the origin user
                                accessRightProcessor.shareToUser(galleryUser.getId(), robot, programName, userId, userAccount, "X_WRITE");
                            } else {
                                LOG.error("TODO: check potential error: the saved program should never be null");
                            }
                            UtilForREST.addSuccessInfo(response, Key.GALLERY_UPLOAD_SUCCESS);
                            Statistics.info("GalleryShare", "success", true);
                            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
                        } else {
                            Statistics.info("GalleryShare", "success", false);
                            return UtilForREST.makeBaseResponseForError(Key.GALLERY_UPLOAD_ERROR, httpSessionState, null);
                        }
                    } else {
                        Statistics.info("GalleryShare", "success", false);
                        return UtilForREST.makeBaseResponseForError(Key.GALLERY_UPLOAD_ERROR, httpSessionState, null);
                    }
                } else {
                    Statistics.info("GalleryShare", "success", false);
                    return UtilForREST.makeBaseResponseForError(Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE, httpSessionState, null);
                }
            }
        } catch ( Exception e ) { // UserProcessor throws Exception
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            Statistics.info("GalleryShare", "success", false);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/share/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSharedProgram(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            BaseResponse response = BaseResponse.make();
            ShareDeleteRequest shareDeleteRequest = ShareDeleteRequest.make(request.getData());
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            ProgramShareProcessor accessRightProcessor = new ProgramShareProcessor(dbSession, httpSessionState);
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                Statistics.info("ProgramShareDelete", "success", false);
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String programName = shareDeleteRequest.getProgramName();
                String owner = shareDeleteRequest.getOwner();
                String author = shareDeleteRequest.getAuthor();
                accessRightProcessor.shareDelete(owner, robot, programName, author, userId);
                UtilForREST.addResultInfo(response, accessRightProcessor);
                // if this program was shared from the gallery, we need to delete the copy of it as well
                if ( owner.equals("Gallery") ) {
                    int galleryId = userProcessor.getUser(owner).getId();
                    programProcessor.deleteByName(programName, galleryId, robot, userId);
                    Statistics.info("ProgramShareDelete", "success", true);
                    UtilForREST.addResultInfo(response, programProcessor);
                }
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Path("/gallery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramsFromGallery(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            JSONObject data = request.getData();
            String group = data.has("group") ? data.getString("group") : "";
            JSONArray programInfo = programProcessor.getProgramGallery(userId, group);
            response.setProgramNames(programInfo);
            UtilForREST.addResultInfo(response, programProcessor);
            Statistics.info("GalleryView", "success", programProcessor.succeeded());
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    private static String getRobot(HttpSessionState httpSessionState) {
        return (httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup().isEmpty())
            ? httpSessionState.getRobotName()
            : httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
    }

    // Transform programs with old xml versions to new xml versions
    private static Pair<String, String> transformBetweenVersions(IRobotFactory robotFactory, String programText, String configText) {
        if ( robotFactory.hasWorkflow("transform") ) {
            if ( configText == null ) {
                // programs that do not have any configuration modifications are saved into the database without an associated configuration
                // when loaded, the default configuration should be used
                configText = robotFactory.getConfigurationDefault();
            }
            Project project = new Project.Builder().setFactory(robotFactory).setProgramXml(programText).setConfigurationXml(configText).build();
            ProjectService.executeWorkflow("transform", project);
            if ( configText != null ) {
                if ( project.getRobotFactory().getConfigurationType().equals("new") ) {
                    return Pair.of(project.getAnnotatedProgramAsXml(), project.getAnnotatedConfigurationAsXml());
                } else {
                    // old style configurations do not implement a correct backtransformation, return the input instead
                    // however the version needs to be updated anyway
                    // TODO replace all old configurations with new ones
                    if ( configText.contains("xmlversion=\"2.0\"") ) {
                        configText = configText.replace("xmlversion=\"2.0\"", "xmlversion=\"3.0\"");
                    }
                    return Pair.of(project.getAnnotatedProgramAsXml(), configText);
                }
            } else {
                return Pair.of(project.getAnnotatedProgramAsXml(), null);
            }
        } else {
            throw new DbcException("Every robot needs a transform workflow!");
        }
    }
}
