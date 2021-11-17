require.config({
    baseUrl: 'js',
    paths: {
        codeflask: 'libs/codeflask/codeflask.min',
        blockly: '../blockly/blockly_compressed',
        bootstrap: 'libs/bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap.min',
        'bootstrap-table': 'libs/bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-table.min',
        'bootstrap-tagsinput': 'libs/bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-tagsinput.min',
        'bootstrap.wysiwyg': 'libs/bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-wysiwyg.min',
        enjoyHint: 'libs/enjoyHint/enjoyhint.min',
        huebee: 'libs/huebee/huebee.min',
        jquery: 'libs/jquery/jquery-3.3.1.min',
        'jquery-scrollto': 'libs/jquery/jquery.scrollTo-2.1.2.min',
        'jquery-validate': 'libs/jquery/jquery.validate-1.17.0.min',
        'jquery-hotkeys': 'libs/jquery/jquery.hotkeys-0.2.0',
        slick: 'libs/slick/slick.min',
        'socket.io': 'libs/socket.io/socket.io',
        'volume-meter': 'libs/sound/volume-meter',
        'neuralnetwork-lib': 'libs/neuralnetwork/lib',
        d3: 'libs/neuralnetwork/d3.min',
        webots: 'libs/webots/webots.min',
        glm: 'libs/webots/glm-js.min',
        'webots.enum': 'libs/webots/enum',
        'webots.wren': 'libs/webots/wrenjs',

        'confDelete.controller': 'app/roberta/controller/confDelete.controller',
        'configuration.controller': 'app/roberta/controller/configuration.controller',
        'configuration.model': 'app/roberta/models/configuration.model',
        'confList.controller': 'app/roberta/controller/confList.controller',
        'confList.model': 'app/roberta/models/confList.model',
        'galleryList.controller': 'app/roberta/controller/galleryList.controller',
        'tutorialList.controller': 'app/roberta/controller/tutorialList.controller',
        'guiState.controller': 'app/roberta/controller/guiState.controller',
        'guiState.model': 'app/roberta/models/guiState.model',
        'import.controller': 'app/roberta/controller/import.controller',
        'language.controller': 'app/roberta/controller/language.controller',
        'legal.controller': 'app/roberta/controller/legal.controller',
        'logList.controller': 'app/roberta/controller/logList.controller',
        'logList.model': 'app/roberta/models/logList.model',
        'menu.controller': 'app/roberta/controller/menu.controller',
        'multSim.controller': 'app/roberta/controller/multSim.controller',
        'notification.controller': 'app/roberta/controller/notification.controller',
        'notification.model': 'app/roberta/models/notification.model',
        'nn.controller': 'app/roberta/controller/nn.controller',
        'progCode.controller': 'app/roberta/controller/progCode.controller',
        'progDelete.controller': 'app/roberta/controller/progDelete.controller',
        'progHelp.controller': 'app/roberta/controller/progHelp.controller',
        'progInfo.controller': 'app/roberta/controller/progInfo.controller',
        'progSim.controller': 'app/roberta/controller/progSim.controller',
        'progRun.controller': 'app/roberta/controller/progRun.controller',
        'progList.controller': 'app/roberta/controller/progList.controller',
        'progList.model': 'app/roberta/models/progList.model',
        'program.controller': 'app/roberta/controller/program.controller',
        'program.model': 'app/roberta/models/program.model',
        'progTutorial.controller': 'app/roberta/controller/progTutorial.controller',
        'progShare.controller': 'app/roberta/controller/progShare.controller',
        'progSim.controller': 'app/roberta/controller/progSim.controller',
        'robot.controller': 'app/roberta/controller/robot.controller',
        'robot.model': 'app/roberta/models/robot.model',
        'tour.controller': 'app/roberta/controller/tour.controller',
        'user.controller': 'app/roberta/controller/user.controller',
        'userGroup.controller': 'app/roberta/controller/userGroup.controller',
        'userGroup.model': 'app/roberta/models/userGroup.model',
        'user.model': 'app/roberta/models/user.model',
        'rest.robot': 'app/roberta/rest/robot',
        'socket.controller': 'app/roberta/controller/socket.controller',
        'webview.controller': 'app/roberta/controller/webview.controller',
        'sourceCodeEditor.controller': 'app/roberta/controller/sourceCodeEditor.controller',

        'simulation.constants': 'app/simulation/simulationLogic/constants',
        'simulation.math': 'app/simulation/simulationLogic/math',
        'simulation.robot': 'app/simulation/simulationLogic/robot',
        'simulation.robot.draw': 'app/simulation/simulationLogic/robot.draw',
        'simulation.robot.mbed': 'app/simulation/simulationLogic/robot.mbed',
        'simulation.robot.calliope': 'app/simulation/simulationLogic/robot.calliope',
        'simulation.robot.calliope2016': 'app/simulation/simulationLogic/robot.calliope2016',
        'simulation.robot.calliope2017': 'app/simulation/simulationLogic/robot.calliope2017',
        'simulation.robot.mbot': 'app/simulation/simulationLogic/robot.mbot',
        'simulation.robot.microbit': 'app/simulation/simulationLogic/robot.microbit',
        'simulation.robot.math': 'app/simulation/simulationLogic/robot.math',
        'simulation.robot.rescue': 'app/simulation/simulationLogic/robot.rescue',
        'simulation.robot.roberta': 'app/simulation/simulationLogic/robot.roberta',
        'simulation.robot.simple': 'app/simulation/simulationLogic/robot.simple',
        'simulation.robot.ev3': 'app/simulation/simulationLogic/robot.ev3',
        'simulation.robot.nxt': 'app/simulation/simulationLogic/robot.nxt',
        'simulation.scene': 'app/simulation/simulationLogic/scene',
        'simulation.simulation': 'app/simulation/simulationLogic/simulation',

        comm: 'helper/comm',
        log: 'helper/log',
        message: 'helper/msg',
        util: 'helper/util',
        wrap: 'helper/wrap',

        'interpreter.constants': 'app/nepostackmachine/interpreter.constants',
        'interpreter.interpreter': 'app/nepostackmachine/interpreter.interpreter',
        'interpreter.aRobotBehaviour': 'app/nepostackmachine/interpreter.aRobotBehaviour',
        'interpreter.robotWeDoBehaviour': 'app/nepostackmachine/interpreter.robotWeDoBehaviour',
        'interpreter.robotSimBehaviour': 'app/nepostackmachine/interpreter.robotSimBehaviour',
        'interpreter.state': 'app/nepostackmachine/interpreter.state',
        'interpreter.util': 'app/nepostackmachine/interpreter.util',
        'interpreter.jsHelper': 'app/nepostackmachine/interpreter.jsHelper',

        'neuralnetwork.nn': 'app/neuralnetwork/neuralnetwork.nn',
        'neuralnetwork.state': 'app/neuralnetwork/neuralnetwork.state',
        'neuralnetwork.playground': 'app/neuralnetwork/neuralnetwork.playground',

        confVisualization: 'app/configVisualization/confVisualization',
        'const.robots': 'app/configVisualization/const.robots',
        port: 'app/configVisualization/port',
        robotBlock: 'app/configVisualization/robotBlock',
        wires: 'app/configVisualization/wires',

        'webots.simulation': 'app/webotsSimulation/webots.simulation',
    },
    shim: {
        webots: {
            deps: ['glm', 'webots.enum', 'webots.wren'],
        },
        bootstrap: {
            deps: ['jquery'],
        },
        blockly: {
            exports: 'Blockly',
        },
        confVisualization: {
            deps: ['blockly'],
        },
        robotBlock: {
            deps: ['blockly'],
        },
        port: {
            deps: ['blockly'],
        },
        'volume-meter': {
            exports: 'Volume',
            init: function () {
                return {
                    createAudioMeter: createAudioMeter,
                };
            },
        },
        'jquery-validate': {
            deps: ['jquery'],
        },
    },
});

require([
    'require',
    'huebee',
    'wrap',
    'log',
    'jquery',
    'blockly',
    'guiState.controller',
    'progList.controller',
    'logList.controller',
    'confList.controller',
    'progDelete.controller',
    'confDelete.controller',
    'progShare.controller',
    'menu.controller',
    'multSim.controller',
    'user.controller',
    'nn.controller',
    'robot.controller',
    'program.controller',
    'progSim.controller',
    'notification.controller',
    'progCode.controller',
    'progDelete.controller',
    'progHelp.controller',
    'legal.controller',
    'progInfo.controller',
    'progRun.controller',
    'configuration.controller',
    'language.controller',
    'socket.controller',
    'progTutorial.controller',
    'tutorialList.controller',
    'userGroup.controller',
    'volume-meter',
    'user.model',
    'webview.controller',
    'sourceCodeEditor.controller',
    'codeflask',
    'interpreter.jsHelper',
    'confVisualization',
    'robotBlock',
], function (require) {
    $ = require('jquery');
    WRAP = require('wrap');
    LOG = require('log');
    COMM = require('comm');
    Blockly = require('blockly');
    confDeleteController = require('confDelete.controller');
    configurationController = require('configuration.controller');
    confListController = require('confList.controller');
    guiStateController = require('guiState.controller');
    languageController = require('language.controller');
    logListController = require('logList.controller');
    menuController = require('menu.controller');
    multSimController = require('multSim.controller');
    progDeleteController = require('progDelete.controller');
    progListController = require('progList.controller');
    galleryListController = require('galleryList.controller');
    tutorialListController = require('tutorialList.controller');
    legalController = require('legal.controller');
    programController = require('program.controller');
    progHelpController = require('progHelp.controller');
    progInfoController = require('progInfo.controller');
    notificationController = require('notification.controller');
    progCodeController = require('progCode.controller');
    progSimController = require('progSim.controller');
    progRunController = require('progRun.controller');
    progShareController = require('progShare.controller');
    robotController = require('robot.controller');
    userController = require('user.controller');
    nnController = require('nn.controller');
    userModel = require('user.model');
    socketController = require('socket.controller');
    tutorialController = require('progTutorial.controller');
    tutorialListController = require('tutorialList.controller');
    userGroupController = require('userGroup.controller');
    webviewController = require('webview.controller');
    sourceCodeEditorController = require('sourceCodeEditor.controller');
    codeflask = require('codeflask');
    stackmachineJsHelper = require('interpreter.jsHelper');
    confVisualization = require('confVisualization');
    robotBlock = require('robotBlock');

    $(document).ready(WRAP.wrapTotal(init, 'page init'));
});

/**
 * Initializations
 */
function init() {
    COMM.setErrorFn(handleServerErrors);
    $.when(languageController.init())
        .then(function (language) {
            return webviewController.init(language);
        })
        .then(function (language, opt_data) {
            return guiStateController.init(language, opt_data);
        })
        .then(function () {
            return robotController.init();
        })
        .then(function () {
            return userController.init();
        })
        .then(function () {
            galleryListController.init();
            tutorialListController.init();
            progListController.init();
            progDeleteController.init();
            confListController.init();
            confDeleteController.init();
            progShareController.init();
            logListController.init();
            legalController.init();
            sourceCodeEditorController.init();
            programController.init();
            configurationController.init();
            progHelpController.init();
            progInfoController.init();
            progCodeController.init();
            progSimController.init();
            progRunController.init();
            menuController.init();
            tutorialController.init();
            userGroupController.init();
            notificationController.init();
            // nnController.init();

            $('.cover').fadeOut(100, function () {
                if (guiStateController.getStartWithoutPopup()) {
                    userModel.getStatusText(function (result) {
                        if (result.statustext[0] !== '' && result.statustext[1] !== '') {
                            $('#modal-statustext').modal('show');
                        }
                    });
                } else {
                    $('#show-startup-message').modal('show');
                }
            });

            $('.pace').fadeOut(500);
        });
}

/**
 * Handle server errors
 */
ALLOWED_PING_NUM = 5;

function handleServerErrors(jqXHR) {
    // TODO more?
    LOG.error('Client connection issue: ' + jqXHR.status);
    if (this.url === '/rest/ping') {
        COMM.errorNum += 1;
    }
    // show message, if REST call is no ping or EXACTLY ALLOWED_PING_NUM requests fail (to avoid multiple messages)
    if (this.url !== '/rest/ping' || COMM.errorNum == ALLOWED_PING_NUM) {
        if (jqXHR.status && jqXHR.status < 500) {
            COMM.showServerError('FRONTEND');
        } else {
            COMM.showServerError('CONNECTION');
        }
    }
}
