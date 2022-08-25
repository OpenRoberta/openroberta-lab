package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.RobotFactory;
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
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.UtilForXmlTransformation;
import de.fhg.iais.roberta.util.XsltTransformer;
import de.fhg.iais.roberta.util.archiver.UserProgramsArchiver;
import de.fhg.iais.roberta.util.basic.Pair;
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            SaveResponse response = SaveResponse.make();
            SaveRequest saveRequest = SaveRequest.make(request.getData());
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
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

            response.setLastChanged(program != null ? program.getLastChanged().getTime() : -1);
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            BaseResponse response = BaseResponse.make();
            JSONObject dataPart = request.getData();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            ListingResponse response = ListingResponse.make();
            JSONObject dataPart = request.getData();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
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
                    Pair<String, String> progConfPair = UtilForXmlTransformation.transformBetweenVersions(httpSessionState.getRobotFactory(), transformedXml, configText);
                    String configName = program.getConfigName();
                    String configXML = progConfPair.getSecond();
                    response.setProgXML(progConfPair.getFirst());
                    // check and set config name for default config.
                    if ( configName == null && configText == null ) {
                        configName = robot.toUpperCase() + "basis";
                    }
                    response.setConfigName(configName); // may be null, if an anonymous configuration is used
                    response.setConfXML(configXML); // may be null, if the default configuration is used
                    response.setProgramName((program.getName()));
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            EntityResponse response = EntityResponse.make();
            JSONObject dataPart = request.getData();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.info("Unauthorized entity request");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String robot = getRobot(httpSessionState);
                String programName = dataPart.getString("programName");
                String ownerName = dataPart.getString("owner");
                String author = dataPart.getString("author");
                User owner = up.getStandardUser(ownerName);
                int ownerID = owner.getId();
                int authorId = up.getStandardUser(author).getId();
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            UserGroupProgramListRequest request = UserGroupProgramListRequest.make(fullRequest.getData());

            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);
            String robot = getRobot(httpSessionState);

            User user = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( user == null ) {
                LOG.error("Unauthorized");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            }

            String groupName = request.getGroupName();
            UserGroup userGroup = userGroupProcessor.getGroup(groupName, user, false);
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
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
        String errorDescription = "";
        try {
            ImportRequest importRequest = ImportRequest.make(request.getData());
            String robot = getRobot(httpSessionState);
            String xmlText = importRequest.getProgXML();
            xmlText = UtilForHtmlXml.checkProgramTextForXSS(xmlText);
            String programName = importRequest.getProgramName();
            if ( !Util.isValidJavaIdentifier(programName) ) {
                programName = "NEPOprog";
            }
            String transformedXml = null;
            Export jaxbImportExport = null;
            try {
                transformedXml = xsltTransformer.transform(xmlText);
            } catch ( Exception e ) {
                errorDescription = "xslt transform";
            }
            if ( transformedXml != null ) {
                try {
                    jaxbImportExport = JaxbHelper.xml2Element(transformedXml, Export.class);
                } catch ( Exception e ) {
                    errorDescription = "xml2jaxb";
                }
            }
            if ( jaxbImportExport != null ) {
                String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                if ( robotType1.equals(robot) && robotType2.equals(robot) ) {
                    ImportResponse response = ImportResponse.make();
                    response.setProgramName(programName);
                    Pair<String, String> progConfPair =
                        UtilForXmlTransformation.transformBetweenVersions(
                            httpSessionState.getRobotFactory(),
                            JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet()),
                            JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet()));
                    response.setProgXML(progConfPair.getFirst());
                    response.setConfXML(progConfPair.getSecond());
                    UtilForREST.addSuccessInfo(response, Key.PROGRAM_IMPORT_SUCCESS);
                    Statistics.info("ProgramImport", "success", true);
                    return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
                } else {
                    List<RobotFactory> members = httpSessionState.getRobotFactoriesOfGroup(robotType1);
                    List<String> realNames = members.stream().map(RobotFactory::getRealName).collect(Collectors.toList());
                    Statistics.info("ProgramImport", "success", false);
                    ImportErrorResponse error = ImportErrorResponse.make();
                    error.setRobotTypes(String.join(", ", realNames));
                    UtilForREST.addErrorInfo(error, Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE);
                    return UtilForREST.responseWithFrontendInfo(error, httpSessionState, null);
                }
            }
        } catch ( Exception e ) { // JaxbHelper methods throw Exception
            errorDescription = "back transform";
        }
        LOG.error("program import failed (" + errorDescription + "). Probably no or user-edited XML");
        ImportResponse response = ImportResponse.make();
            UtilForREST.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR);
            return UtilForREST.makeBaseResponseForError(Key.PROGRAM_IMPORT_ERROR, httpSessionState, null);
    }

    /**
     * used to export all Programs of every robot of the current user.
     * To get give appropriate feedback logincheck from ClientUser.java should be called before this.
     *
     * @param initToken requires an initToken to creates a valid httpSessionState
     * @return zip file sorted in an directory structure including all Programs by the user as an xml file
     */
    @GET
    @Path("/exportAllPrograms")
    public Response exportAllProgrammsOfUser(@OraData DbSession dbSession, @QueryParam("initToken") String initToken) throws IOException {
        HttpSessionState httpSessionState;
        try {
            httpSessionState = UtilForREST.validateInitToken(initToken);
        } catch ( Exception e ) {
            if ( dbSession != null ) {
                dbSession.close();
            }
            throw e;
        }
        try {
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized export request");
                return null;
            }

            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState.getUserId());
            InputStream zip = new UserProgramsArchiver(httpSessionState, programProcessor).getArchive();
            ResponseBuilder response = Response.ok(zip, "application/zip");
            zip.close();
            return response.header("Content-Disposition", "attachment; filename=\"NEPO_Programs.zip\"").build();
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception in ExportAll. Error ticket: {}", errorTicketId, e);
            return null;
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }

    }


    @POST
    @Path("/share")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareProgram(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            ShareResponse response = ShareResponse.make();
            ShareRequest shareRequest = ShareRequest.make(request.getData());
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            ProgramShareProcessor programShareProcessor = new ProgramShareProcessor(dbSession, httpSessionState.getUserId());
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            BaseResponse response = BaseResponse.make();
            LikeRequest likeRequest = LikeRequest.make(request.getData());
            LikeProcessor lp = new LikeProcessor(dbSession, httpSessionState.getUserId());
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            SaveResponse response = SaveResponse.make();
            ShareCreateRequest shareCreateRequest = ShareCreateRequest.make(request.getData());

            int userId = httpSessionState.getUserId();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, userId);
            ProgramShareProcessor accessRightProcessor = new ProgramShareProcessor(dbSession, userId);
            UserProcessor userProcessor = new UserProcessor(dbSession, userId);
            ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(dbSession, userId);

            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            } else {
                String programName = shareCreateRequest.getProgramName();
                User galleryUser = userProcessor.getStandardUser("Gallery");
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            BaseResponse response = BaseResponse.make();
            ShareDeleteRequest shareDeleteRequest = ShareDeleteRequest.make(request.getData());

            int userId = httpSessionState.getUserId();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, userId);
            ProgramShareProcessor accessRightProcessor = new ProgramShareProcessor(dbSession, userId);
            UserProcessor userProcessor = new UserProcessor(dbSession, userId);

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
                    int galleryId = userProcessor.getStandardUser(owner).getId();
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            ListingNamesResponse response = ListingNamesResponse.make();

            int userId = httpSessionState.getUserId();
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, userId);

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
        return httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup().isEmpty()
            ? httpSessionState.getRobotName()
            : httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
    }
}
