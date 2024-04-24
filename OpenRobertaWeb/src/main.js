require.config({
    baseUrl: '.',
    paths: {
        codeflask: 'libs/codeflask/codeflask.min',
        blockly: 'blockly/blockly_compressed',
        bootstrap: 'libs/bootstrap/bootstrap.bundle.min',
        'bootstrap-table': 'libs/bootstrap/bootstrap-table-1.22.1-dist/js/bootstrap-table.min',
        'bootstrap-table-locals': 'libs/bootstrap/bootstrap-table-1.22.1-dist/js/bootstrap-table-locale-all.min',
        'bootstrap-tagsinput': 'libs/bootstrap/bootstrap-tagsinput.min',
        'bootstrap.wysiwyg': 'libs/bootstrap/bootstrap-wysiwyg.min',
        enjoyHint: 'libs/enjoyHint/enjoyhint.min',
        huebee: 'libs/huebee/huebee.min',
        jquery: 'libs/jquery/jquery.min',
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
        dapjs: 'libs/dapjs/dap.umd',

        'startView.controller': 'js/app/roberta/controller/startView.controller',
        'confDelete.controller': 'js/app/roberta/controller/confDelete.controller',
        'configuration.controller': 'js/app/roberta/controller/configuration.controller',
        'configuration.model': 'js/app/roberta/models/configuration.model',
        'confList.controller': 'js/app/roberta/controller/confList.controller',
        'confList.model': 'js/app/roberta/models/confList.model',
        'galleryList.controller': 'js/app/roberta/controller/galleryList.controller',
        'tutorialList.controller': 'js/app/roberta/controller/tutorialList.controller',
        'guiState.controller': 'js/app/roberta/controller/guiState.controller',
        'guiState.model': 'js/app/roberta/models/guiState.model',
        'import.controller': 'js/app/roberta/controller/import.controller',
        'language.controller': 'js/app/roberta/controller/language.controller',
        'legal.controller': 'js/app/roberta/controller/legal.controller',
        'logList.controller': 'js/app/roberta/controller/logList.controller',
        'logList.model': 'js/app/roberta/models/logList.model',
        'menu.controller': 'js/app/roberta/controller/menu.controller',
        'notification.controller': 'js/app/roberta/controller/notification.controller',
        'notification.model': 'js/app/roberta/models/notification.model',
        'nn.controller': 'js/app/roberta/controller/nn.controller',
        'progCode.controller': 'js/app/roberta/controller/progCode.controller',
        'progDelete.controller': 'js/app/roberta/controller/progDelete.controller',
        'progHelp.controller': 'js/app/roberta/controller/progHelp.controller',
        'progInfo.controller': 'js/app/roberta/controller/progInfo.controller',
        'progSim.controller': 'js/app/roberta/controller/progSim.controller',
        'progRun.controller': 'js/app/roberta/controller/progRun.controller',
        'progList.controller': 'js/app/roberta/controller/progList.controller',
        'progList.model': 'js/app/roberta/models/progList.model',
        'program.controller': 'js/app/roberta/controller/program.controller',
        'program.model': 'js/app/roberta/models/program.model',
        'progTutorial.controller': 'js/app/roberta/controller/progTutorial.controller',
        'progShare.controller': 'js/app/roberta/controller/progShare.controller',
        'robot.controller': 'js/app/roberta/controller/robot.controller',
        'robot.model': 'js/app/roberta/models/robot.model',
        'tour.controller': 'js/app/roberta/controller/tour.controller',
        'user.controller': 'js/app/roberta/controller/user.controller',
        'userGroup.controller': 'js/app/roberta/controller/userGroup.controller',
        'userGroup.model': 'js/app/roberta/models/userGroup.model',
        'user.model': 'js/app/roberta/models/user.model',
        'rest.robot': 'js/app/roberta/rest/robot',
        'startView.model': 'js/app/roberta/models/startView.model',
        'sourceCodeEditor.controller': 'js/app/roberta/controller/sourceCodeEditor.controller',
        'connection.interface': 'js/app/roberta/controller/connections/connection.interface',
        'abstract.connections': 'js/app/roberta/controller/connections/abstract.connections',
        connections: 'js/app/roberta/controller/connections/connections',
        'connection.controller': 'js/app/roberta/controller/connection.controller',
        'webview.controller': 'js/app/roberta/controller/webview.controller',
        'simulation.constants': 'js/app/simulation/simulationLogic/constants',
        'simulation.math': 'js/app/simulation/simulationLogic/math',
        'robot.calliope': 'js/app/simulation/simulationLogic/robot.calliope',
        'robot.mbot': 'js/app/simulation/simulationLogic/robot.mbot',
        'robot.microbit': 'js/app/simulation/simulationLogic/robot.microbit',
        'robot.microbitv2': 'js/app/simulation/simulationLogic/robot.microbitv2',
        'robot.math': 'js/app/simulation/simulationLogic/robot.math',
        'robot.rob3rta': 'js/app/simulation/simulationLogic/robot.rob3rta',
        'robot.ev3': 'js/app/simulation/simulationLogic/robot.ev3',
        'robot.nxt': 'js/app/simulation/simulationLogic/robot.nxt',
        'robot.xnn': 'js/app/simulation/simulationLogic/robot.xnn',
        'robot.thymio': 'js/app/simulation/simulationLogic/robot.thymio',
        'robot.edison': 'js/app/simulation/simulationLogic/robot.edison',
        'robot.robotino': 'js/app/simulation/simulationLogic/robot.robotino',
        'robot.base.mobile': 'js/app/simulation/simulationLogic/robot.base.mobile',
        'robot.base.stationary': 'js/app/simulation/simulationLogic/robot.base.stationary',
        'robot.base': 'js/app/simulation/simulationLogic/robot.base',
        'simulation.objects': 'js/app/simulation/simulationLogic/simulation.objects',
        'robot.sensors': 'js/app/simulation/simulationLogic/robot.sensors',
        'robot.actuators': 'js/app/simulation/simulationLogic/robot.actuators',
        'simulation.types': 'js/app/simulation/simulationLogic/types',
        'simulation.scene': 'js/app/simulation/simulationLogic/simulation.scene',
        'simulation.roberta': 'js/app/simulation/simulationLogic/simulation.roberta',
        'simulation.webots': 'js/app/simulation/simulationLogic/simulation.webots',
        maze: 'js/app/simulation/simulationLogic/maze',

        comm: 'js/helper/comm',
        log: 'js/helper/log',
        message: 'js/helper/msg',
        'util.roberta': 'js/helper/util',
        wrap: 'js/helper/wrap',
        table: 'js/helper/table',

        'interpreter.constants': 'js/app/nepostackmachine/interpreter.constants',
        'interpreter.interpreter': 'js/app/nepostackmachine/interpreter.interpreter',
        'interpreter.aRobotBehaviour': 'js/app/nepostackmachine/interpreter.aRobotBehaviour',
        'interpreter.robotWeDoBehaviour': 'js/app/nepostackmachine/interpreter.robotWeDoBehaviour',
        'interpreter.robotSimBehaviour': 'js/app/nepostackmachine/interpreter.robotSimBehaviour',
        'interpreter.state': 'js/app/nepostackmachine/interpreter.state',
        'interpreter.util': 'js/app/nepostackmachine/interpreter.util',

        'neuralnetwork.nn': 'js/app/neuralnetwork/neuralnetwork.nn',
        'neuralnetwork.uistate': 'js/app/neuralnetwork/neuralnetwork.uistate',
        'neuralnetwork.ui': 'js/app/neuralnetwork/neuralnetwork.ui',
        'neuralnetwork.linechart': 'js/app/neuralnetwork/neuralnetwork.linechart',
        'neuralnetwork.helper': 'js/app/neuralnetwork/neuralnetwork.helper',
        'neuralnetwork.msg': 'js/app/neuralnetwork/neuralnetwork.msg',

        thymio: 'js/app/roberta/models/thymio',
        thymio_generated: 'js/app/roberta/models/thymio_generated',
        flatbuffers: 'libs/thymio/flatbuffers',
        '@cor3ntin/flexbuffers-wasm': 'libs/thymio/flexbuffers',
        'isomorphic-ws': 'libs/thymio/browser',
        'lodash.isequal': 'libs/thymio/lodash/isEqual',

        confVisualization: 'js/app/configVisualization/confVisualization',
        'const.robots': 'js/app/configVisualization/const.robots',
        port: 'js/app/configVisualization/port',
        robotBlock: 'js/app/configVisualization/robotBlock',
        wires: 'js/app/configVisualization/wires',
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
        'jquery-hotkeys': {
            deps: ['jquery'],
        },
        'bootstrap-table': {
            deps: ['bootstrap'],
        },
        'bootstrap-table-locals': {
            deps: ['bootstrap-table'],
        },
        'bootstrap-tagsinput': {
            deps: ['bootstrap-table'],
        },
        'bootstrap.wysiwyg': {
            deps: ['bootstrap-table'],
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
    'bootstrap-table-locals',
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
    'progTutorial.controller',
    'tutorialList.controller',
    'userGroup.controller',
    'volume-meter',
    'user.model',
    'sourceCodeEditor.controller',
    'codeflask',
    'confVisualization',
    'robotBlock',
    'startView.controller',
    //start connections
    'connection.interface',
    'abstract.connections',
    'connections',
    'connection.controller',
    //end connections
], function (require) {
    //window.Popper = require('popper.js').default;
    window.$ = window.jQuery = require('jquery');
    require('bootstrap');
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
    tutorialController = require('progTutorial.controller');
    userGroupController = require('userGroup.controller');
    webviewController = require('webview.controller');
    sourceCodeEditorController = require('sourceCodeEditor.controller');
    codeflask = require('codeflask');
    confVisualization = require('confVisualization');
    robotBlock = require('robotBlock');
    startViewController = require('startView.controller');
    connectionController = require('connection.controller');

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
            return userController.init();
        })
        .then(function () {
            galleryListController.init();
            tutorialListController.init();
            logListController.init();
            legalController.init();
            sourceCodeEditorController.init();
            tutorialController.init();
            userGroupController.init();
            notificationController.init();
            return startViewController.init(initProgramming);
        })
        .then(function () {
            menuController.init(initProgramming);
            $('#sliderDiv').hide();
            if (!guiStateController.getStartWithoutPopup()) {
                $('#tabStart').tabWrapShow();
                $('.navbar-nav > li > ul > .login').addClass('disabled');
                $('#head-navi-icon-user').addClass('error');
            }
            $('.cover').fadeOut(100);
            $('body>.pace').fadeOut(500);
        });
}

var mainCallbackCalled = false;
function initProgramming(robot, extensions, opt_callback, opt_params) {
    let callback = opt_callback;
    let params = opt_params;
    if (!mainCallbackCalled) {
        mainCallbackCalled = true;
        $.when(robotController.init(robot, extensions)).then(function () {
            $('#tabProgram, #tabConfiguration').parent().removeClass('invisible');
            $('.notStart').removeClass('disabled');
            $('#header').addClass('shadow');
            programController.init();
            configurationController.init();
            progHelpController.init();
            progInfoController.init();
            progCodeController.init();
            progSimController.createProgSimInstance();
            progSimController.createProgSimDebugInstance();
            progSimController.createProgSimMultiInstance();
            progRunController.init();
            nnController.init();
            progListController.init();
            progDeleteController.init();
            confListController.init();
            confDeleteController.init();
            progShareController.init();
            guiStateController.setInitialState();
            connectionController.initConnection(robot);
            $('#tabProgram').oneWrap('shown.bs.tab', function () {
                callback && typeof callback === 'function' && callback(...params);
            });
            $('#tabProgram').tabWrapShow();
        });
    } else {
        robotController.switchRobot(robot, extensions, null, function () {
            $('#tabProgram, #tabConfiguration').parent().removeClass('invisible');
            $('#header').addClass('shadow');
            $('.notStart').removeClass('disabled');
            $('#tabProgram').oneWrap('shown.bs.tab', function () {
                callback && typeof callback === 'function' && callback(...params);
            });
            $('#tabProgram').tabWrapShow();
        });
    }
}

var ALLOWED_PING_NUM = 5;

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
