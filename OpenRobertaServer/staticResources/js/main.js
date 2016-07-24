require.config({
    baseUrl : 'js/libs',
    paths : {
        'jquery' : 'jquery/jquery-2.1.4.min',
        'jquery-ui' : 'jquery/jquery-ui-1.10.4.custom.min',
        'jquery-scrollto' : 'jquery/jquery.scrollTo.min',
        'datatables' : 'jquery/jquery.dataTables.min',
        'jquery-validate' : 'jquery/jquery.validate.min',
        'jquery-cookie' : 'jquery/jquery.cookie',
        'bootstrap' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap.min',
        'bootstrap-table' : 'bootstrap/bootstrap-3.3.1-dist/dist/js/bootstrap-table.min',
        'blocks' : '../../blockly/blocks_compressed',
        'blocks-msg' : '../../blockly/msg/js/en',
        'blockly' : '../../blockly/blockly_compressed',
        'prettify' : 'code-prettify/prettify',
        'enjoyHint' : 'enjoyHint/enjoyhint.min',

        'roberta.roberta' : '../app/roberta/roberta',
        'roberta.user-state' : '../app/roberta/roberta.user.state',
        //'roberta.brickly' : '../app/roberta/brickly',
        'roberta.user' : '../app/roberta/roberta.user',
        'roberta.toolbox' : '../app/roberta/roberta.toolbox',
        'roberta.robot' : '../app/roberta/roberta.robot',
        'roberta.program' : '../app/roberta/roberta.program',
        'roberta.program.sharing' : '../app/roberta/roberta.program.sharing',
        'roberta.navigation' : '../app/roberta/roberta.navigation',
        'roberta.language' : '../app/roberta/roberta.language',
        'roberta.brick-configuration' : '../app/roberta/roberta.brick-configuration',
        'roberta.tour' : '../app/roberta/roberta.tour',

        'progDelete.controller' : '../app/roberta/controller/progDelete.controller',
        'confDelete.controller' : '../app/roberta/controller/confDelete.controller',
        'progShare.controller' : '../app/roberta/controller/progShare.controller',
        'progList.controller' : '../app/roberta/controller/progList.controller',
        'confList.controller' : '../app/roberta/controller/confList.controller',
        'logList.controller' : '../app/roberta/controller/logList.controller',
        'language.controller' : '../app/roberta/controller/language.controller',
        'guiState.controller' : '../app/roberta/controller/guiState.controller',
        'user.controller' : '../app/roberta/controller/user.controller',
        'menu.controller' : '../app/roberta/controller/menu.controller',
        'robot.controller' : '../app/roberta/controller/robot.controller',
        'program.controller' : '../app/roberta/controller/program.controller',
        'configuration.controller' : '../app/roberta/controller/configuration.controller',
        'progList.model' : '../app/roberta/models/progList.model',
        'confList.model' : '../app/roberta/models/confList.model',
        'guiState.model' : '../app/roberta/models/guiState.model',
        'user.model' : '../app/roberta/models/user.model',
        'logList.model' : '../app/roberta/models/logList.model',

        'simulation.constants' : '../app/simulation/simulationLogic/constants',
        'simulation.simulation' : '../app/simulation/simulationLogic/simulation',
        'simulation.robot' : '../app/simulation/simulationLogic/robot',
        'simulation.robot.simple' : '../app/simulation/simulationLogic/robot.simple',
        'simulation.robot.draw' : '../app/simulation/simulationLogic/robot.draw',
        'simulation.robot.math' : '../app/simulation/simulationLogic/robot.math',
        'simulation.robot.rescue' : '../app/simulation/simulationLogic/robot.rescue',
        'simulation.robot.roberta' : '../app/simulation/simulationLogic/robot.roberta',
        'simulation.scene' : '../app/simulation/simulationLogic/scene',
        'simulation.math' : '../app/simulation/simulationLogic/math',
        'simulation.program.eval' : '../app/simulation/robertaLogic/program.eval',
        'simulation.program.builder' : '../app/simulation/robertaLogic/program.builder',

        'rest.configuration' : '../app/roberta/rest/configuration',
        'rest.program' : '../app/roberta/rest/program',
        'rest.robot' : '../app/roberta/rest/robot',
        'rest.user' : '../app/roberta/rest/user',

        'log' : '../helper/log',
        'wrap' : '../helper/wrap',
        'comm' : '../helper/comm',
        'util' : '../helper/util',
        'message' : '../helper/msg',

        'robertaLogic.timer' : '../app/simulation/robertaLogic/timer',
        'robertaLogic.actors' : '../app/simulation/robertaLogic/actors',
        'robertaLogic.motor' : '../app/simulation/robertaLogic/motor',
        'robertaLogic.memory' : '../app/simulation/robertaLogic/memory',
        'robertaLogic.program' : '../app/simulation/robertaLogic/program',
        'robertaLogic.constants' : '../app/simulation/robertaLogic/constants',

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
        'jquery-ui' : {
            deps : [ 'jquery' ]
        },
        'jquery-cookie' : {
            deps : [ 'jquery' ]
        },
    }
});

require([ 'require', 'wrap', 'jquery', 'jquery-cookie', 'guiState.controller', 'progList.controller', 'logList.controller', 'confList.controller',
        'progDelete.controller', 'progShare.controller', 'menu.controller', 'user.controller', 'robot.controller', 'program.controller',
        'configuration.controller', 'language.controller' ], function(require) {

    $ = require('jquery', 'jquery-cookie');
    WRAP = require('wrap');
    COMM = require('comm');
    guiStateController = require('guiState.controller');
    progListController = require('progList.controller');
    confListController = require('confList.controller');
    progDeleteController = require('progDelete.controller');
    confDeleteController = require('progDelete.controller');
    progShareController = require('progShare.controller');
    logListController = require('logList.controller');
    menuController = require('menu.controller');
    userController = require('user.controller');
    robotController = require('robot.controller');
    programController = require('program.controller');
    configurationController = require('configuration.controller');
    languageController = require('language.controller');

    $(document).ready(WRAP.fn3(init, 'page init'));
});

/**
 * Initializations
 */
function init() {
    COMM.setErrorFn(handleServerErrors);
    $.when(languageController.init()).then(function(language) {
        return guiStateController.init(language);
    }).then(function() {
        return robotController.init();
    }).then(function() {
        progListController.init();
        progDeleteController.init();
        confListController.init();
        confDeleteController.init();
        progShareController.init();
        logListController.init();
        menuController.init();
        userController.initUserForms();
        configurationController.init();
        programController.init();
        $(".pace").fadeOut(1000, function() {
            $(".cover").fadeOut(500);
            if (guiStateController.noCookie()) {
                $("#show-startup-message").modal("show");
            }
        });
    });

//    // Workaround to set the focus on input fields with attribute 'autofocus'
//    $('.modal').on('shown.bs.modal', function() {
//        $(this).find('[autofocus]').focus();
//    });
//
//    $(window).on('beforeunload', function(e) {
//        //       if (!userState.programSaved || !userState.configurationSaved) {
////            if (userState.id === -1) {
////                // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD']);
////                return Blockly.Msg.POPUP_BEFOREUNLOAD;
////            } else {
////                // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD_LOGGEDIN']);
////                return Blockly.Msg.POPUP_BEFOREUNLOAD_LOGGEDIN;
////            }
//        //       }
//    });

//
//    UTIL.checkVisibility(function() {
//        var visible = UTIL.checkVisibility();
//        LOG.info("this tab visible: " + visible);
//        if (!visible) {
//            SIM.setPause(true);
//            // TODO do more?
//        }
//    });
//
//    var ping = setInterval(function() {
//        pingServer();
//    }, 3000);

}

/**
 * Handle server errors
 */
function handleServerErrors() {
    // TODO more?        
    LOG.info('Server is not available or network is down');
    userState.doPing = false;
    $('#message').attr('lkey', Blockly.Msg.SERVER_NOT_AVAILABLE);
    $('#message').html(Blockly.Msg.SERVER_NOT_AVAILABLE);
    $('#show-message').on('hidden.bs.modal', function(e) {
        $("#show-message").modal("show");
    });
    $("#show-message").modal("show");
}

/**
 * Regularly ping the server to keep status information up-to-date
 */
function pingServer() {
    if (userState.doPing) {
        COMM.ping(function(result) {
            ROBERTA_ROBOT.setState(result);
        });
    }
}
