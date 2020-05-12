package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import com.google.inject.Inject;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.UnmarshalException;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.AccessRightProcessor;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.LikeProcessor;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForHtmlXml;
import de.fhg.iais.roberta.util.UtilForREST;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

@Path("/program")
public class ClientProgramController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientProgramController.class);

    private final boolean isPublicServer;

    @Inject
    public ClientProgramController(ServerProperties serverProperties) {
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
    }

    private static String getRobot(HttpSessionState httpSessionState) {
        return (httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup().isEmpty())
            ? httpSessionState.getRobotName()
            : httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup();
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveProgram(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            Long timestamp = dataPart.optLong("timestamp");
            Timestamp programTimestamp = new Timestamp(timestamp);
            String programName = dataPart.getString("programName");
            String programText = dataPart.getString("progXML");
            String configName = dataPart.optString("configName", null);
            String configText = dataPart.optString("confXML", null);
            boolean isShared = dataPart.optBoolean("shared", false);
            boolean isSaveCommand = dataPart.getString("cmd").equals("save");
            Program program;
            if ( isSaveCommand ) {
                program =
                    programProcessor.persistProgramText(programName, programText, configName, configText, userId, robot, userId, programTimestamp, !isShared);
            } else {
                program = programProcessor.persistProgramText(programName, programText, configName, configText, userId, robot, userId, null, true);
            }
            if ( programProcessor.succeeded() && (program != null) ) {
                response.put("lastChanged", program.getLastChanged().getTime());
            }
            UtilForREST.addResultInfo(response, programProcessor);
            Statistics.info("ProgramSave", "success", programProcessor.succeeded());
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProgram(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
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
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/listing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgram(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() && !dataPart.getString("owner").equals("Roberta") && !dataPart.getString("owner").equals("Gallery") ) {
                LOG.info("Unauthorized load request");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                String programName = dataPart.getString("programName");
                String ownerName = dataPart.getString("owner");
                String author = dataPart.getString("author");
                String robot = getRobot(httpSessionState);

                Program program = programProcessor.getProgram(programName, ownerName, robot, author);
                if ( program != null ) {
                    response.put("progXML", program.getProgramText());
                    String configText = programProcessor.getProgramsConfig(program);
                    response.put("configName", program.getConfigName()); // may be null, if an anonymous configuration is used
                    response.put("confXML", configText); // may be null, if the default configuration is used
                    response.put("lastChanged", program.getLastChanged().getTime());
                    // count the views if the program is from the gallery!
                    if ( ownerName.equals("Gallery") ) {
                        programProcessor.addOneView(program);
                    }
                }
                UtilForREST.addResultInfo(response, programProcessor);
                Statistics.info("ProgramLoad", "success", programProcessor.succeeded());
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramEntity(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
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
                    response.put("program", program);
                }
                UtilForREST.addResultInfo(response, programProcessor);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/listing/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfosOfProgramsOfLoggedInUser(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                int userId = httpSessionState.getUserId();
                JSONArray programInfo = programProcessor.getProgramInfo(userId, robot, userId);
                response.put("programNames", programInfo);
                UtilForREST.addResultInfo(response, programProcessor);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/examples/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfosOfExamplePrograms(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            String robot = getRobot(httpSessionState);
            int userId = 1;
            JSONArray programInfo = programProcessor.getProgramInfo(userId, robot, userId);
            response.put("programNames", programInfo);
            UtilForREST.addResultInfo(response, programProcessor);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importProgram(JSONObject request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
        JSONObject dataPart = UtilForREST.extractDataPart(request);
        JSONObject response = new JSONObject();
        try {
            String robot = getRobot(httpSessionState);
            String xmlText = dataPart.getString("progXML");
            xmlText = UtilForHtmlXml.checkProgramTextForXSS(xmlText);
            if ( xmlText.contains("robottype=\"ardu\"") ) {
                xmlText = xmlText.replaceAll("robottype=\"ardu\"", "robottype=\"botnroll\"");
                LOG.warn("Ardu to botnroll renaming on import should be removed in future.");
            }
            String programName = dataPart.getString("programName");
            if ( !Util.isValidJavaIdentifier(programName) ) {
                programName = "NEPOprog";
            }

            Export jaxbImportExport;
            try {
                jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
            } catch ( UnmarshalException | org.xml.sax.SAXException e ) {
                jaxbImportExport = null;
            }
            if ( jaxbImportExport != null ) {
                String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                if ( robotType1.equals(robot) && robotType2.equals(robot) ) {
                    response.put("programName", programName);
                    response.put("progXML", JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet()));
                    response.put("confXML", JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet()));
                    UtilForREST.addSuccessInfo(response, Key.PROGRAM_IMPORT_SUCCESS);
                    Statistics.info("ProgramImport", "success", true);
                } else {
                    List<IRobotFactory> members = httpSessionState.getRobotFactoriesOfGroup(robotType1);
                    List<String> realNames = members.stream().map(IRobotFactory::getRealName).collect(Collectors.toList());
                    response.put("robotTypes", String.join(", ", realNames));
                    UtilForREST.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE);
                    Statistics.info("ProgramImport", "success", false);
                }
            } else {
                UtilForREST.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR);
            }
        } catch ( Exception e ) { // JaxbHelper methods throw Exception
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/share")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareProgram(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            AccessRightProcessor accessRightProcessor = new AccessRightProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                User user = userProcessor.getUser(userId);
                if ( !this.isPublicServer || ((user != null) && user.isActivated()) ) {
                    String programName = dataPart.getString("programName");
                    String userToShareName = dataPart.getString("userToShare");
                    String right = dataPart.getString("right");
                    accessRightProcessor.shareToUser(userId, robot, programName, userId, userToShareName, right);
                    UtilForREST.addResultInfo(response, accessRightProcessor);
                    Statistics.info("ProgramShare", "success", accessRightProcessor.succeeded());
                } else {
                    UtilForREST.addErrorInfo(response, Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE);
                }
            }
        } catch ( Exception e ) { // UserProcessor throws Exception
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeProgram(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            LikeProcessor lp = new LikeProcessor(dbSession, httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                Statistics.info("GalleryLike", "success", false);
            } else {
                String programName;
                String robotName;
                String author;
                boolean like;
                programName = dataPart.getString("programName");
                robotName = dataPart.getString("robotName");
                author = dataPart.getString("author");
                like = dataPart.getBoolean("like");
                if ( like ) {
                    lp.createLike(programName, robotName, author);
                    if ( lp.succeeded() ) {
                        // nothing to do
                        // argument: deleted tracks whether a like was set or taken away
                        Statistics.info("GalleryLike", "success", true, "deleted", false);
                    } else {
                        UtilForREST.addErrorInfo(response, Key.LIKE_SAVE_ERROR_EXISTS);
                        Statistics.info("GalleryLike", "success", false);
                    }
                } else {
                    lp.deleteLike(programName, robotName, author);
                    Statistics.info("GalleryLike", "success", true, "deleted", true);
                }
                UtilForREST.addResultInfo(response, lp);
            }
        } catch ( Exception e ) { // LikeProcessor throws Exception
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/share/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareProgramInGallery(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);

            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            AccessRightProcessor accessRightProcessor = new AccessRightProcessor(dbSession, httpSessionState);
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(dbSession, httpSessionState);

            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                String programName = dataPart.getString("programName");
                int galleryId = userProcessor.getUser("Gallery").getId();
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
                            programProcessor.persistProgramText(programName, program.getProgramText(), null, confText, galleryId, robot, userId, null, true);
                        if ( programProcessor.succeeded() ) {
                            if ( programCopy != null ) {
                                response.put("lastChanged", programCopy.getLastChanged().getTime());
                                // share the copy of the program with the origin user
                                accessRightProcessor.shareToUser(galleryId, robot, programName, userId, userAccount, "X_WRITE");
                            } else {
                                LOG.error("TODO: check potential error: the saved program should never be null");
                            }
                            UtilForREST.addSuccessInfo(response, Key.GALLERY_UPLOAD_SUCCESS);
                            Statistics.info("GalleryShare", "success", true);
                        } else {
                            UtilForREST.addErrorInfo(response, Key.GALLERY_UPLOAD_ERROR);
                            Statistics.info("GalleryShare", "success", false);
                        }
                    } else {
                        UtilForREST.addErrorInfo(response, Key.GALLERY_UPLOAD_ERROR);
                        Statistics.info("GalleryShare", "success", false);
                    }
                } else {
                    UtilForREST.addErrorInfo(response, Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE);
                    Statistics.info("GalleryShare", "success", false);
                }
            }
        } catch ( Exception e ) { // UserProcessor throws Exception
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/share/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSharedProgram(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            AccessRightProcessor accessRightProcessor = new AccessRightProcessor(dbSession, httpSessionState);
            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                Statistics.info("ProgramShareDelete", "success", false);
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                String programName = dataPart.getString("programName");
                String owner = dataPart.getString("owner");
                String author = dataPart.getString("author");
                accessRightProcessor.shareDelete(owner, robot, programName, author, userId);
                UtilForREST.addResultInfo(response, accessRightProcessor);
                // if this program was shared from the gallery we need to delete the copy of it as well
                if ( owner.equals("Gallery") ) {
                    int galleryId = userProcessor.getUser(owner).getId();
                    programProcessor.deleteByName(programName, galleryId, robot, userId);
                    Statistics.info("ProgramShareDelete", "success", true);
                    UtilForREST.addResultInfo(response, programProcessor);
                }
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/gallery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramsFromGallery(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            JSONObject data = request.getJSONObject("data");
            String group = "";
            if ( data.has("group") ) {
                group = data.getString("group");
            }
            JSONArray programInfo = programProcessor.getProgramGallery(userId, group);
            response.put("programNames", programInfo);
            UtilForREST.addResultInfo(response, programProcessor);
            Statistics.info("GalleryView", "success", programProcessor.succeeded());
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }

    /**
     * TODO: really needed? No use found in client
     *
     * @param dbSession
     * @param request
     * @return
     */
    @POST
    @Path("/relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public Response getProjectRelations(@OraData DbSession dbSession, JSONObject request) {
        JSONObject response = new JSONObject();
        try {
            HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, request, true);
            JSONObject dataPart = UtilForREST.extractDataPart(request);
            ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            int userId = httpSessionState.getUserId();
            String robot = getRobot(httpSessionState);
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                UtilForREST.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                String programName = dataPart.getString("programName");
                JSONArray relations = programProcessor.getProgramRelations(programName, userId, robot, userId);
                response.put("relations", relations);
                UtilForREST.addResultInfo(response, programProcessor);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            try {
                UtilForREST.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
            } catch ( JSONException ex ) {
                LOG.error("Could not add error info to response!", ex);
            }
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Response.ok(response).build();
    }
}
