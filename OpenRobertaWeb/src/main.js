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
        'thymioSocket.controller': 'app/roberta/controller/thymioSocket.controller',

        'simulation.constants': 'app/simulation/simulationLogic/constants',
        'simulation.math': 'app/simulation/simulationLogic/math',
        'robot.calliope': 'app/simulation/simulationLogic/robot.calliope',
        'robot.mbot': 'app/simulation/simulationLogic/robot.mbot',
        'robot.microbit': 'app/simulation/simulationLogic/robot.microbit',
        'robot.microbitv2': 'app/simulation/simulationLogic/robot.microbitv2',
        'robot.math': 'app/simulation/simulationLogic/robot.math',
        'robot.rob3rta': 'app/simulation/simulationLogic/robot.rob3rta',
        'robot.ev3': 'app/simulation/simulationLogic/robot.ev3',
        'robot.nxt': 'app/simulation/simulationLogic/robot.nxt',
        'robot.xnn': 'app/simulation/simulationLogic/robot.xnn',
        'robot.thymio': 'app/simulation/simulationLogic/robot.thymio',
        'robot.robotino': 'app/simulation/simulationLogic/robot.robotino',
        'robot.base.mobile': 'app/simulation/simulationLogic/robot.base.mobile',
        'robot.base.stationary': 'app/simulation/simulationLogic/robot.base.stationary',
        'robot.base': 'app/simulation/simulationLogic/robot.base',
        'simulation.objects': 'app/simulation/simulationLogic/simulation.objects',
        'robot.sensors': 'app/simulation/simulationLogic/robot.sensors',
        'robot.actuators': 'app/simulation/simulationLogic/robot.actuators',
        'simulation.types': 'app/simulation/simulationLogic/types',
        'simulation.scene': 'app/simulation/simulationLogic/simulation.scene',
        'simulation.roberta': 'app/simulation/simulationLogic/simulation.roberta',
        'simulation.webots': 'app/simulation/simulationLogic/simulation.webots',
        maze: 'app/simulation/simulationLogic/maze',

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

        'neuralnetwork.nn': 'app/neuralnetwork/neuralnetwork.nn',
        'neuralnetwork.uistate': 'app/neuralnetwork/neuralnetwork.uistate',
        'neuralnetwork.ui': 'app/neuralnetwork/neuralnetwork.ui',
        'neuralnetwork.helper': 'app/neuralnetwork/neuralnetwork.helper',
        'neuralnetwork.msg': 'app/neuralnetwork/neuralnetwork.msg',

        thymio: 'app/roberta/models/thymio',
        thymio_generated: 'app/roberta/models/thymio_generated',
        flatbuffers: 'libs/thymio/flatbuffers',
        '@cor3ntin/flexbuffers-wasm': 'libs/thymio/flexbuffers',
        'isomorphic-ws': 'libs/thymio/browser',
        'lodash.isequal': 'libs/thymio/lodash/isEqual',

        confVisualization: 'app/configVisualization/confVisualization',
        'const.robots': 'app/configVisualization/const.robots',
        port: 'app/configVisualization/port',
        robotBlock: 'app/configVisualization/robotBlock',
        wires: 'app/configVisualization/wires',
    },
    shim: {
        webots: {
            deps: ['glm', 'webots.enum', 'webots.wren'],
        },
        '@cor3ntin/flexbuffers-wasm': {
            exports: 'FlexBuffers',
        },
        flatbuffers: {
            exports: 'flatbuffers',
            init: function () {
                return this;
            },
        },
        'lodash.isequal': {
            exports: 'isEqual',
        },
        thymio: {
            deps: ['flatbuffers', '@cor3ntin/flexbuffers-wasm', 'lodash.isequal'],
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
            exports: 'VolumeMeter',
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
            progSimController.createProgSimInstance();
            progSimController.createProgSimDebugInstance();
            progSimController.createProgSimMultiInstance();
            progRunController.init();
            tutorialController.init();
            userGroupController.init();
            notificationController.init();
            nnController.init();
            menuController.init();

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

            $('body>.pace').fadeOut(500);
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
