require.config({
    baseUrl : 'js/libs',
    paths : {
        'codeflask' : 'codeflask/codeflask.min',
        'blockly' : '../../blockly/blockly_compressed',
        'blocks' : '../../blockly/blocks_compressed',
        'blocks-msg' : '../../blockly/msg/js/en',
        'bootstrap' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap.min',
        'bootstrap-table' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-table.min',
        'bootstrap-tagsinput' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-tagsinput.min',
        'bootstrap.wysiwyg' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-wysiwyg.min',
        'enjoyHint' : 'enjoyHint/enjoyhint.min',
        'jquery' : 'jquery/jquery-3.3.1.min',
        'jquery-cookie' : 'jquery/jquery.cookie-1.4.1',
        'jquery-scrollto' : 'jquery/jquery.scrollTo-2.1.2.min',
        'jquery-validate' : 'jquery/jquery.validate-1.17.0.min',
        'jquery-hotkeys' : 'jquery/jquery.hotkeys-0.2.0',
        'slick' : 'slick/slick.min',
        'socket.io' : 'socket.io/socket.io',
        'volume-meter' : 'sound/volume-meter',

        'confDelete.controller' : '../app/roberta/controller/confDelete.controller',
        'configuration.controller' : '../app/roberta/controller/configuration.controller',
        'configuration.model' : '../app/roberta/models/configuration.model',
        'confList.controller' : '../app/roberta/controller/confList.controller',
        'confList.model' : '../app/roberta/models/confList.model',
        'galleryList.controller' : '../app/roberta/controller/galleryList.controller',
        'tutorialList.controller' : '../app/roberta/controller/tutorialList.controller',
        'guiState.controller' : '../app/roberta/controller/guiState.controller',
        'guiState.model' : '../app/roberta/models/guiState.model',
        'import.controller' : '../app/roberta/controller/import.controller',
        'language.controller' : '../app/roberta/controller/language.controller',
        'logList.controller' : '../app/roberta/controller/logList.controller',
        'logList.model' : '../app/roberta/models/logList.model',
        'menu.controller' : '../app/roberta/controller/menu.controller',
        'multSim.controller' : '../app/roberta/controller/multSim.controller',
        'progCode.controller' : '../app/roberta/controller/progCode.controller',
        'progDelete.controller' : '../app/roberta/controller/progDelete.controller',
        'progLegal.controller' : '../app/roberta/controller/progLegal.controller',
        'progHelp.controller' : '../app/roberta/controller/progHelp.controller',
        'progInfo.controller' : '../app/roberta/controller/progInfo.controller',
        'progSim.controller' : '../app/roberta/controller/progSim.controller',
        'progRun.controller' : '../app/roberta/controller/progRun.controller',
        'progList.controller' : '../app/roberta/controller/progList.controller',
        'progList.model' : '../app/roberta/models/progList.model',
        'program.controller' : '../app/roberta/controller/program.controller',
        'program.model' : '../app/roberta/models/program.model',
        'progTutorial.controller' : '../app/roberta/controller/progTutorial.controller',
        'progShare.controller' : '../app/roberta/controller/progShare.controller',
        'progSim.controller' : '../app/roberta/controller/progSim.controller',
        'robot.controller' : '../app/roberta/controller/robot.controller',
        'robot.model' : '../app/roberta/models/robot.model',
        'tour.controller' : '../app/roberta/controller/tour.controller',
        'user.controller' : '../app/roberta/controller/user.controller',
        'user.model' : '../app/roberta/models/user.model',
        'rest.robot' : '../app/roberta/rest/robot',
        'socket.controller' : '../app/roberta/controller/socket.controller',
        'cookieDisclaimer.controller' : '../app/roberta/controller/cookieDisclaimer.controller',
        'webview.controller' : '../app/roberta/controller/webview.controller',
        'wedo.model' : '../app/roberta/models/wedo.model',
        
        'sourceCodeEditor.controller' : '../app/roberta/controller/sourceCodeEditor.controller',

        'simulation.constants' : '../app/simulation/simulationLogic/constants',
        'simulation.math' : '../app/simulation/simulationLogic/math',
        'simulation.robot' : '../app/simulation/simulationLogic/robot',
        'simulation.robot.draw' : '../app/simulation/simulationLogic/robot.draw',
        'simulation.robot.mbed' : '../app/simulation/simulationLogic/robot.mbed',
        'simulation.robot.calliope' : '../app/simulation/simulationLogic/robot.calliope',
        'simulation.robot.calliope2016' : '../app/simulation/simulationLogic/robot.calliope2016',
        'simulation.robot.calliope2017' : '../app/simulation/simulationLogic/robot.calliope2017',
        'simulation.robot.microbit' : '../app/simulation/simulationLogic/robot.microbit',
        'simulation.robot.math' : '../app/simulation/simulationLogic/robot.math',
        'simulation.robot.rescue' : '../app/simulation/simulationLogic/robot.rescue',
        'simulation.robot.roberta' : '../app/simulation/simulationLogic/robot.roberta',
        'simulation.robot.simple' : '../app/simulation/simulationLogic/robot.simple',
        'simulation.robot.ev3' : '../app/simulation/simulationLogic/robot.ev3',
        'simulation.robot.nxt' : '../app/simulation/simulationLogic/robot.nxt',
        'simulation.scene' : '../app/simulation/simulationLogic/scene',
        'simulation.simulation' : '../app/simulation/simulationLogic/simulation',

        'comm' : '../helper/comm',
        'log' : '../helper/log',
        'message' : '../helper/msg',
        'util' : '../helper/util',
        'wrap' : '../helper/wrap',

        'interpreter.constants' : '../app/wedoInterpreter/interpreter.constants',
        'interpreter.interpreter' : '../app/wedoInterpreter/interpreter.interpreter',
        'interpreter.aRobotBehaviour' : '../app/wedoInterpreter/interpreter.aRobotBehaviour',
        'interpreter.robotWeDoBehaviour' : '../app/wedoInterpreter/interpreter.robotWeDoBehaviour',
        'interpreter.robotWeDoBehaviourTest' : '../app/wedoInterpreter/interpreter.robotWeDoBehaviourTest',
        'interpreter.robotMbedBehaviour' : '../app/wedoInterpreter/interpreter.robotMbedBehaviour',
        'interpreter.state' : '../app/wedoInterpreter/interpreter.state',
        'interpreter.util' : '../app/wedoInterpreter/interpreter.util'

    },
    shim : {
        'bootstrap' : {
            deps : [ 'jquery' ]
        },
        'blocks-msg' : {
            deps : [ 'blocks' ],
            exports : 'Blockly'
        },
        'blocks' : {
            deps : [ 'blockly' ],
            exports : 'Blockly'
        },
        'volume-meter' : {
            exports : "Volume",
            init : function() {
                return {
                    createAudioMeter : createAudioMeter
                };
            }
        },
        'jquery-validate' : {
            deps : [ 'jquery' ]
        },
        'jquery-cookie' : {
            deps : [ 'jquery' ]
        },
    }
});

require([ 'require', 'wrap', 'log', 'jquery', 'jquery-cookie', 'guiState.controller', 'progList.controller', 'logList.controller', 'confList.controller',
        'progDelete.controller', 'confDelete.controller', 'progShare.controller', 'cookieDisclaimer.controller', 'menu.controller', 'multSim.controller',
        'user.controller', 'robot.controller', 'program.controller', 'progSim.controller', 'progCode.controller', 'progDelete.controller',
        'progHelp.controller', 'progLegal.controller', 'progInfo.controller', 'progRun.controller', 'configuration.controller', 'language.controller', 'socket.controller',
        'progTutorial.controller', 'tutorialList.controller', 'volume-meter', 'user.model', 'webview.controller', 'sourceCodeEditor.controller', 'codeflask' ], function(require) {
    $ = require('jquery', 'jquery-cookie');
    WRAP = require('wrap');
    LOG = require('log');
    COMM = require('comm');
    confDeleteController = require('confDelete.controller');
    configurationController = require('configuration.controller');
    confListController = require('confList.controller');
    guiStateController = require('guiState.controller');
    languageController = require('language.controller');
    logListController = require('logList.controller');
    cookieDisclaimer = require('cookieDisclaimer.controller');
    menuController = require('menu.controller');
    multSimController = require('multSim.controller');
    progDeleteController = require('progDelete.controller');
    progListController = require('progList.controller');
    galleryListController = require('galleryList.controller');
    tutorialListController = require('tutorialList.controller');
    programController = require('program.controller');
    progHelpController = require('progHelp.controller');
    progLegalController = require('progLegal.controller');
    progInfoController = require('progInfo.controller');
    progCodeController = require('progCode.controller');
    progSimController = require('progSim.controller');
    progRunController = require('progRun.controller');
    progShareController = require('progShare.controller');
    robotController = require('robot.controller');
    userController = require('user.controller');
    userModel = require('user.model');
    socketController = require('socket.controller');
    tutorialController = require('progTutorial.controller');
    tutorialListController = require('tutorialList.controller');
    webviewController = require('webview.controller');
    sourceCodeEditorController = require('sourceCodeEditor.controller');
    codeflask = require('codeflask');

    $(document).ready(WRAP.fn3(init, 'page init'));
});

/**
 * Initializations
 */
function init() {
    COMM.setErrorFn(handleServerErrors);
    $.when(languageController.init()).then(function(language) {
        return webviewController.init(language);
    }).then(function(language, opt_data) {
        return guiStateController.init(language, opt_data);
    }).then(function() {
        return robotController.init();
    }).then(function() {
        return userController.init();
    }).then(function() {
        galleryListController.init();
        tutorialListController.init();
        progListController.init();
        progDeleteController.init();
        confListController.init();
        confDeleteController.init();
        progShareController.init();
        logListController.init();
        
        sourceCodeEditorController.init();

        programController.init();
        configurationController.init();
        progHelpController.init();
        progLegalController.init();
        progInfoController.init();
        progCodeController.init();
        progSimController.init();
        progRunController.init();
        menuController.init();
        tutorialController.init();

        cookieDisclaimer.init();
        $(".cover").fadeOut(100, function() {
            if (!guiStateController.getStartWithoutPopup()) {
                if (guiStateController.noCookie()) {
                    $("#show-startup-message").modal("show");
                } else {
                    userModel.getStatusText(function(result) {
                        if (result.statustext[0] !== "" && result.statustext[1] !== "") {
                            $('#modal-statustext').modal("show");
                        }
                    });
                }
            }
        });

        $(".pace").fadeOut(500);
    });
}

/**
 * Handle server errors
 */
ALLOWED_PING_NUM = 5

function handleServerErrors(jqXHR) {
    // TODO more?  
    LOG.error("Client connection issue: " + jqXHR.status);
    if (this.url === "/rest/ping") {
        COMM.errorNum += 1;
    }
    if (this.url !== "/rest/ping" || COMM.errorNum > ALLOWED_PING_NUM) {
        guiStateController.setPing(false);
        if (jqXHR.status && jqXHR.status < 500) {
            $('#message').attr('lkey', Blockly.Msg.ORA_COMMAND_INVALID);
            $('#message').html(Blockly.Msg.ORA_COMMAND_INVALID);
        } else {
            $('#message').attr('lkey', Blockly.Msg.SERVER_NOT_AVAILABLE);
            $('#message').html(Blockly.Msg.SERVER_NOT_AVAILABLE);
        }
        $('#show-message').modal({
            backdrop : 'static',
            keyboard : false
        })
        $('#show-message :button').hide();
        $('#show-message').one('hidden.bs.modal', function(e) {
            // $("#show-message").modal("show");
            guiStateController.setPing(true);
        });
        $("#show-message").modal("show");
    }
}
