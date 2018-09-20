package de.fhg.iais.roberta.javaServer.restServices.all;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.AccessRightProcessor;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.DummyProcessor;
import de.fhg.iais.roberta.persistence.LikeProcessor;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformerData;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

@Path("/program")
public class ClientProgram {
    private static final Logger LOG = LoggerFactory.getLogger(ClientProgram.class);

    private final SessionFactoryWrapper sessionFactoryWrapper;
    private final RobotCommunicator brickCommunicator;
    private final boolean isPublicServer;

    @Inject
    public ClientProgram(SessionFactoryWrapper sessionFactoryWrapper, RobotCommunicator brickCommunicator, RobertaProperties robertaProperties) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
        this.brickCommunicator = brickCommunicator;
        this.isPublicServer = robertaProperties.getBooleanProperty("server.public");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, JSONObject fullRequest) throws Exception {
        AliveData.rememberClientCall();
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
        new ClientLogger().log(ClientProgram.LOG, fullRequest);

        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        try {
            final JSONObject request = fullRequest.getJSONObject("data");
            final String cmd = request.getString("cmd");
            ClientProgram.LOG.info("command is: " + cmd + ", userId is " + userId);
            response.put("cmd", cmd);
            final ProgramProcessor pp = new ProgramProcessor(dbSession, httpSessionState);
            final AccessRightProcessor upp = new AccessRightProcessor(dbSession, httpSessionState);
            final UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            final LikeProcessor lp = new LikeProcessor(dbSession, httpSessionState);
            final ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(dbSession, httpSessionState);

            final IRobotFactory robotFactory = httpSessionState.getRobotFactory();

            if ( cmd.equals("saveP") || cmd.equals("saveAsP") ) {
                final String programName = request.getString("programName");
                final String programText = request.getString("programText");
                final String configName = request.optString("configName", null);
                final String configText = request.optString("configText", null);
                Program program;
                if ( cmd.equals("saveP") ) {
                    // update an already existing program
                    final Long timestamp = request.getLong("timestamp");
                    final Timestamp programTimestamp = new Timestamp(timestamp);
                    final boolean isShared = request.optBoolean("shared", false);
                    program = pp.persistProgramText(programName, programText, configName, configText, userId, robot, userId, programTimestamp, !isShared);
                } else {
                    program = pp.persistProgramText(programName, programText, configName, configText, userId, robot, userId, null, true);
                }
                if ( pp.isOk() ) {
                    if ( program != null ) {
                        response.put("lastChanged", program.getLastChanged().getTime());
                    } else {
                        ClientProgram.LOG.error("TODO: check potential error: the saved program should never be null");
                    }
                }
                Util.addResultInfo(response, pp);
                Statistics.info("ProgramSave");

            } else if ( cmd.equals("showSourceP") ) {
                final String token = httpSessionState.getToken();
                final String programName = request.getString("name");
                final String programText = request.getString("programText");
                final String configName = request.optString("configuration", null);
                String configurationText = request.optString("configurationText", null);
                final ILanguage language = Language.findByAbbr(request.optString("language"));
                if ( configName != null ) {
                    configurationText = configurationProcessor.getConfigurationText(configName, userId, robot);
                } else if ( configurationText == null ) {
                    configurationText = robotFactory.getConfigurationDefault();
                }

                final AbstractProcessor forMessages = new DummyProcessor();
                final BlocklyProgramAndConfigTransformer transformer =
                    BlocklyProgramAndConfigTransformer.transform(robotFactory, programText, configurationText);
                transformer.getBrickConfiguration().setRobotName(httpSessionState.getRobotName());
                if ( transformer.getErrorMessage() != null ) {
                    forMessages.setError(transformer.getErrorMessage());
                } else {
                    final RobotCommonCheckVisitor programChecker = robotFactory.getRobotProgramCheckVisitor(transformer.getBrickConfiguration());
                    programConfigurationCompatibilityCheck(response, transformer, programChecker);

                    final String sourceCode = robotFactory.getRobotCompilerWorkflow().generateSourceCode(token, programName, transformer, language);
                    if ( sourceCode == null ) {
                        forMessages.setError(Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED);
                    } else {
                        response.put("sourceCode", sourceCode);
                        response.put("fileExtension", robotFactory.getFileExtension());
                        forMessages.setSuccess(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
                    }
                }
                Util.addResultInfo(response, forMessages);
                Statistics.info("ProgramSource");

            } else if ( cmd.equals("loadP") ) {
                if ( !httpSessionState.isUserLoggedIn() && !request.getString("owner").equals("Roberta") && !request.getString("owner").equals("Gallery") ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("name");
                    final String ownerName = request.getString("owner");
                    final String authorName = request.getString("authorName");

                    final Program program = pp.getProgram(programName, ownerName, robot, authorName);
                    if ( program != null ) {
                        response.put("programText", program.getProgramText());
                        final String configText = pp.getProgramsConfig(program);
                        response.put("configName", program.getConfigName()); // may be null, if an anonymous configuration is used
                        response.put("configText", configText); // may be null, if the default configuration is used
                        response.put("lastChanged", program.getLastChanged().getTime());
                        // count the views if the program is from the gallery!
                        if ( ownerName.equals("Gallery") ) {
                            pp.addOneView(program);
                        }
                    }
                    Util.addResultInfo(response, pp);
                    Statistics.info("ProgramLoad");
                }
            } else if ( cmd.equals("importXML") ) {
                String xmlText = request.getString("program");
                xmlText = Util.removeUnwantedDescriptionHTMLTags(xmlText);
                String programName = request.getString("name");
                if ( !Util1.isValidJavaIdentifier(programName) ) {
                    programName = "NEPOprog";
                }

                Export jaxbImportExport = null;
                try {
                    jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
                } catch ( final UnmarshalException | org.xml.sax.SAXException e ) {
                    jaxbImportExport = null;
                }
                if ( jaxbImportExport != null ) {
                    final String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                    final String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                    if ( robotType1.equals(robot) && robotType2.equals(robot) ) {
                        response.put("programName", programName);
                        response.put("programText", JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet()));
                        response.put("configText", JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet()));
                        Util.addSuccessInfo(response, Key.PROGRAM_IMPORT_SUCCESS);
                        Statistics.info("ProgramImport");
                    } else {
                        Util.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE);
                    }
                } else {
                    Util.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR);
                }
            } else if ( cmd.equals("shareP") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final User user = up.getUser(userId);
                    if ( !this.isPublicServer || user != null && user.isActivated() ) {
                        final String programName = request.getString("programName");
                        final String userToShareName = request.getString("userToShare");
                        final String right = request.getString("right");
                        upp.shareToUser(userId, robot, programName, userId, userToShareName, right);
                        Util.addResultInfo(response, upp);
                        Statistics.info("ProgramShare");
                    } else {
                        Util.addErrorInfo(response, Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE);
                    }
                }

            } else if ( cmd.equals("shareWithGallery") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("programName");
                    final int galleryId = up.getUser("Gallery").getId();
                    // generating a unique name for the program owned by the gallery.
                    final User user = up.getUser(userId);
                    final String userAccount = user.getAccount();
                    if ( !this.isPublicServer || user != null && user.isActivated() ) {
                        // get the program from the origin user to share with the gallery
                        final Program program = pp.getProgram(programName, userAccount, robot, userAccount);

                        String confText;
                        if ( program != null ) {
                            if ( program.getConfigName() == null ) {
                                if ( program.getConfigHash() == null ) {
                                    confText = null;
                                } else {
                                    final ConfigurationDao confDao = new ConfigurationDao(dbSession);
                                    confText = confDao.load(program.getConfigHash()).getConfigurationText();
                                }
                            } else {
                                confText = configurationProcessor.getConfigurationText(program.getConfigName(), userId, robot);
                            }
                            // make a copy of the user program and store it as a gallery owned program
                            final Program programCopy =
                                pp.persistProgramText(programName, program.getProgramText(), null, confText, galleryId, robot, userId, null, true);
                            if ( pp.isOk() ) {
                                if ( programCopy != null ) {
                                    response.put("lastChanged", programCopy.getLastChanged().getTime());
                                    // share the copy of the program with the origin user
                                    upp.shareToUser(galleryId, robot, programName, userId, userAccount, "X_WRITE");
                                } else {
                                    ClientProgram.LOG.error("TODO: check potential error: the saved program should never be null");
                                }
                                Util.addSuccessInfo(response, Key.GALLERY_UPLOAD_SUCCESS);
                                Statistics.info("GalleryShare");
                            } else {
                                Util.addErrorInfo(response, Key.GALLERY_UPLOAD_ERROR);
                            }
                        } else {
                            Util.addErrorInfo(response, Key.GALLERY_UPLOAD_ERROR);
                        }
                    } else {
                        Util.addErrorInfo(response, Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE);
                    }
                }

            } else if ( cmd.equals("likeP") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("programName");
                    final String robotName = request.getString("robotName");
                    final boolean like = request.getBoolean("like");
                    final String authorName = request.getString("authorName");

                    if ( like ) {
                        lp.createLike(programName, robotName, authorName);
                        if ( lp.isOk() ) {
                            // nothing to do
                        } else {
                            Util.addErrorInfo(response, Key.LIKE_SAVE_ERROR_EXISTS);
                        }
                    } else {
                        lp.deleteLike(programName, robotName, authorName);
                    }

                    Util.addResultInfo(response, lp);
                    Statistics.info("GalleryLike");
                }

            } else if ( cmd.equals("shareDelete") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("programName");
                    final String owner = request.getString("owner");
                    final String author = request.getString("author");
                    upp.shareDelete(owner, robot, programName, author, userId);
                    Util.addResultInfo(response, upp);
                    Statistics.info("ProgramShareDelete");
                    // if this program was shared from the gallery we need to delete the copy of it as well
                    if ( owner.equals("Gallery") ) {
                        final int ownerId = up.getUser(owner).getId();
                        pp.deleteByName(programName, ownerId, robot, userId);
                        Util.addResultInfo(response, pp);
                    }
                }

            } else if ( cmd.equals("deleteP") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("name");
                    final String author = request.getString("author");
                    pp.deleteByName(programName, userId, robot, author);
                    Util.addResultInfo(response, pp);
                    Statistics.info("ProgramDelete");
                }
            } else if ( cmd.equals("loadPN") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final JSONArray programInfo = pp.getProgramInfo(userId, robot, userId);
                    response.put("programNames", programInfo);
                    Util.addResultInfo(response, pp);
                }

            } else if ( cmd.equals("loadGallery") ) {
                final JSONArray programInfo = pp.getProgramGallery(userId);
                response.put("programNames", programInfo);
                Util.addResultInfo(response, pp);
                Statistics.info("GalleryView");
            } else if ( cmd.equals("loadProgramEntity") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("name");
                    final String ownerName = request.getString("owner");
                    final String authorName = request.getString("author");
                    final User owner = up.getUser(ownerName);
                    final int ownerID = owner.getId();
                    final int authorId = up.getUser(authorName).getId();
                    final JSONArray program = pp.getProgramEntity(programName, ownerID, robot, authorId);
                    if ( program != null ) {
                        response.put("program", program);
                    }
                    Util.addResultInfo(response, pp);
                }

            } else if ( cmd.equals("loadEN") ) {
                final JSONArray programInfo = pp.getProgramInfo(1, robot, 1);
                response.put("programNames", programInfo);
                Util.addResultInfo(response, pp);

            } else if ( cmd.equals("loadPR") ) {
                if ( !httpSessionState.isUserLoggedIn() ) {
                    ClientProgram.LOG.error("Unauthorized");
                    Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
                } else {
                    final String programName = request.getString("name");
                    final JSONArray relations = pp.getProgramRelations(programName, userId, robot, userId);
                    response.put("relations", relations);
                    Util.addResultInfo(response, pp);
                }

            } else if ( cmd.equals("runP") ) {
                boolean wasRobotWaiting = false;

                final String token = httpSessionState.getToken();
                final String programName = request.getString("name");
                final String programText = request.optString("programText");
                final String configName = request.optString("configuration", null);
                String configurationText = request.optString("configurationText", null);
                final ILanguage language = Language.findByAbbr(request.optString("language"));
                if ( configName != null ) {
                    configurationText = configurationProcessor.getConfigurationText(configName, userId, robot);
                } else if ( configurationText == null ) {
                    configurationText = robotFactory.getConfigurationDefault();
                }

                final BlocklyProgramAndConfigTransformer programAndConfigTransformer =
                    BlocklyProgramAndConfigTransformer.transform(robotFactory, programText, configurationText);
                programAndConfigTransformer.getBrickConfiguration().setRobotName(httpSessionState.getRobotName());
                Key messageKey = programAndConfigTransformer.getErrorMessage();
                if ( messageKey == null ) {
                    final RobotCommonCheckVisitor programChecker =
                        robotFactory.getRobotProgramCheckVisitor(programAndConfigTransformer.getBrickConfiguration());
                    messageKey = programConfigurationCompatibilityCheck(response, programAndConfigTransformer, programChecker);
                    if ( messageKey == null ) {
                        ClientProgram.LOG.info("compiler workflow started for program {}", programName);
                        messageKey =
                            robotFactory.getRobotCompilerWorkflow().generateSourceAndCompile(token, programName, programAndConfigTransformer, language);
                        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS && token != null && !token.equals(ClientAdmin.NO_CONNECT) ) {
                            this.brickCommunicator.setSubtype(httpSessionState.getRobotName());
                            wasRobotWaiting = this.brickCommunicator.theRunButtonWasPressed(token, programName);
                            Statistics.info("ProgramRun", "LoggedIn", String.valueOf(httpSessionState.isUserLoggedIn()));
                        } else {
                            if ( messageKey != null ) {
                                LOG.info(messageKey.toString());
                            }
                            LOG.info("download command for the ev3 skipped, Keep going with push requests");
                        }
                    }
                }
                handleRunProgramError(response, messageKey, token, wasRobotWaiting);
            } else if ( cmd.equals("compileN") ) {
                final String token = httpSessionState.getToken();
                final String programName = request.getString("name");
                final String programText = request.optString("programText");
                final ILanguage language = Language.findByAbbr(request.optString("language"));
                LOG.info("compilation of native source started for program {}", programName);
                final Key messageKey = robotFactory.getRobotCompilerWorkflow().compileSourceCode(token, programName, programText, language, null);
                LOG.info("compile user supplied native program. Result: " + messageKey);
                if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
                    Util.addSuccessInfo(response, Key.COMPILERWORKFLOW_SUCCESS);
                    Statistics.info("ProgramCompileNative");
                } else {
                    Util.addErrorInfo(response, messageKey);
                }
            } else if ( cmd.equals("compileP") ) {
                Key messageKey = null;

                String programName = request.getString("name");
                final String xmlText = request.getString("program");
                final ILanguage language = Language.findByAbbr(request.optString("language"));
                if ( !Util1.isValidJavaIdentifier(programName) ) {
                    programName = "NEPOprog";
                }

                Export jaxbImportExport = null;
                try {
                    jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
                } catch ( final UnmarshalException | org.xml.sax.SAXException e ) {
                    jaxbImportExport = null;
                }
                if ( jaxbImportExport != null ) {
                    final String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                    final String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                    if ( robotType1.equals(robot) && robotType2.equals(robot) ) {
                        final String programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
                        final String configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());
                        final String token = "toknTokn";
                        final BlocklyProgramAndConfigTransformer programAndConfigTransformer =
                            BlocklyProgramAndConfigTransformer.transform(robotFactory, programText, configText);
                        programAndConfigTransformer.getBrickConfiguration().setRobotName(httpSessionState.getRobotName());
                        messageKey = programAndConfigTransformer.getErrorMessage();
                        if ( messageKey == null ) {
                            final RobotCommonCheckVisitor programChecker =
                                robotFactory.getRobotProgramCheckVisitor(programAndConfigTransformer.getBrickConfiguration());
                            messageKey = programConfigurationCompatibilityCheck(response, programAndConfigTransformer, programChecker);
                            if ( messageKey == null ) {
                                ClientProgram.LOG.info("compiler workflow started for program {}", programName);
                                messageKey =
                                    robotFactory.getRobotCompilerWorkflow().generateSourceAndCompile(token, programName, programAndConfigTransformer, language);
                            }
                        } else {
                            messageKey = Key.PROGRAM_IMPORT_ERROR;
                        }
                    } else {
                        messageKey = Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE;
                    }
                    ClientProgram.LOG.info("compileP terminated with " + messageKey);
                    if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
                        Util.addSuccessInfo(response, Key.COMPILERWORKFLOW_SUCCESS);
                        Statistics.info("ProgramCompile");
                    } else {
                        Util.addErrorInfo(response, messageKey);
                    }
                } else {
                    messageKey = Key.PROGRAM_IMPORT_ERROR;
                    Util.addErrorInfo(response, messageKey);
                }
            } else if ( cmd.equals("runPBack") ) {
                Key messageKey = null;
                final String token = httpSessionState.getToken();
                final String programName = request.getString("name");
                final String programText = request.optString("programText");
                final String configName = request.optString("configuration", null);
                String configurationText = request.optString("configurationText", null);
                final ILanguage language = Language.findByAbbr(request.optString("language"));
                if ( configName != null ) {
                    configurationText = configurationProcessor.getConfigurationText(configName, userId, robot);
                } else if ( configurationText == null ) {
                    configurationText = robotFactory.getConfigurationDefault();
                }

                final BlocklyProgramAndConfigTransformer programAndConfigTransformer =
                    BlocklyProgramAndConfigTransformer.transform(robotFactory, programText, configurationText);
                programAndConfigTransformer.getBrickConfiguration().setRobotName(httpSessionState.getRobotName());
                messageKey = programAndConfigTransformer.getErrorMessage();
                if ( messageKey == null ) {
                    final RobotCommonCheckVisitor programChecker =
                        robotFactory.getRobotProgramCheckVisitor(programAndConfigTransformer.getBrickConfiguration());
                    messageKey = programConfigurationCompatibilityCheck(response, programAndConfigTransformer, programChecker);
                    if ( messageKey == null ) {
                        ClientProgram.LOG.info("compiler workflow started for program {}", programName);

                        final ICompilerWorkflow robotCompilerWorkflow = robotFactory.getRobotCompilerWorkflow();
                        messageKey = robotCompilerWorkflow.generateSourceAndCompile(token, programName, programAndConfigTransformer, language);
                        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
                            response.put("compiledCode", robotCompilerWorkflow.getCompiledCode());
                            response.put("rc", "ok");
                            Statistics.info("ProgramRunBack", "LoggedIn", String.valueOf(httpSessionState.isUserLoggedIn()));
                        } else {
                            if ( messageKey != null ) {
                                LOG.info(messageKey.toString());
                                handleRunProgramError(response, messageKey, token, true);
                            }
                        }
                    }
                }
                handleRunProgramError(response, messageKey, token, true);
            } else if ( cmd.equals("runPsim") ) {
                boolean wasRobotWaiting = false;

                final String token = httpSessionState.getToken();
                final String programName = request.getString("name");
                final String programText = request.optString("programText");
                final String configName = request.optString("configuration", null);
                String configurationText = request.optString("configurationText", null);
                final ILanguage language = Language.findByAbbr(request.optString("language"));
                if ( configName != null ) {
                    configurationText = configurationProcessor.getConfigurationText(configName, userId, robot);
                } else if ( configurationText == null ) {
                    configurationText = robotFactory.getConfigurationDefault();
                }

                final BlocklyProgramAndConfigTransformer transformer =
                    BlocklyProgramAndConfigTransformer.transform(robotFactory, programText, configurationText);
                transformer.getBrickConfiguration().setRobotName(httpSessionState.getRobotName());
                Key messageKey = transformer.getErrorMessage();
                if ( messageKey == null ) {
                    final RobotSimulationCheckVisitor programChecker = robotFactory.getSimProgramCheckVisitor(transformer.getBrickConfiguration());
                    messageKey = programConfigurationCompatibilityCheck(response, transformer, programChecker);
                    transformer.getProgramTransformer().getData();
                    if ( messageKey == null ) {
                        ClientProgram.LOG.info("JavaScript code generation started for program {}", programName);
                        final String javaScriptCode = robotFactory.getSimCompilerWorkflow().generateSourceCode(token, programName, transformer, language);
                        // extreme debugging: ClientProgram.LOG.debug("JavaScriptCode \n{}", javaScriptCode);
                        response.put("javaScriptProgram", javaScriptCode);
                        wasRobotWaiting = true;
                        messageKey = Key.COMPILERWORKFLOW_SUCCESS;
                        Statistics.info("SimulationRun", "LoggedIn", String.valueOf(httpSessionState.isUserLoggedIn()));
                    }
                }
                //TODO program checks should be in compiler workflow and should be thoroughly revised
                handleRunProgramError(response, messageKey, token, wasRobotWaiting);

            } else {
                ClientProgram.LOG.error("Invalid command: " + cmd);
                Util.addErrorInfo(response, Key.COMMAND_INVALID);
            }
            dbSession.commit();
        } catch ( final Exception e ) {
            dbSession.rollback();
            final String errorTicketId = Util1.getErrorTicketId();
            ClientProgram.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        Util.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
        MDC.clear();
        return Response.ok(response).build();
    }

    private Key programConfigurationCompatibilityCheck(
        JSONObject response,
        BlocklyProgramAndConfigTransformer programAndConfigTransformer,
        RobotCommonCheckVisitor programChecker)
        throws JSONException,
        JAXBException {
        final Jaxb2AstTransformerData<Void> data = programAndConfigTransformer.getProgramTransformer().getData();
        if ( programChecker == null ) {
            response.put("data", ClientProgram.jaxbToXml(ClientProgram.astToJaxb(programAndConfigTransformer.getProgramTransformer().getTree(), data)));
            return null;
        }
        programChecker.check(programAndConfigTransformer.getTransformedProgram());
        final int errorCounter = programChecker.getErrorCount();
        response.put("data", ClientProgram.jaxbToXml(ClientProgram.astToJaxb(programChecker.getCheckedProgram(), data)));
        response.put("errorCounter", errorCounter);
        if ( errorCounter > 0 ) {
            return Key.PROGRAM_INVALID_STATEMETNS;
        }
        return null;
    }

    private static String jaxbToXml(BlockSet blockSet) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        final Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        final StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

    private static BlockSet astToJaxb(ArrayList<ArrayList<Phrase<Void>>> astProgram, Jaxb2AstTransformerData<Void> data) {
        final BlockSet blockSet = new BlockSet();
        blockSet.setDescription(data.getDescription());
        blockSet.setRobottype(data.getRobotType());
        blockSet.setTags(data.getTags());
        blockSet.setXmlversion(data.getXmlVersion());

        for ( final ArrayList<Phrase<Void>> tree : astProgram ) {
            final Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            for ( final Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        return blockSet;
    }

    private static void handleRunProgramError(JSONObject response, Key messageKey, String token, boolean wasRobotWaiting) throws JSONException {
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            if ( token == null ) {
                Util.addErrorInfo(response, Key.ROBOT_NOT_CONNECTED);
            } else {
                if ( wasRobotWaiting ) {
                    Util.addSuccessInfo(response, Key.ROBOT_PUSH_RUN);
                } else {
                    Util.addErrorInfo(response, Key.ROBOT_NOT_WAITING);
                }
            }
        } else {
            Util.addErrorInfo(response, messageKey);
        }
    }
}