define([ 'require', 'exports', 'log', 'util', 'message', 'comm', 'configuration.model', 'blockly', 'jquery', 'bootstrap-table' ], function(require, exports,
        LOG, UTIL, MSG, COMM, CONFIGURATION, Blockly, $) {

    function init() {
//        initView();
        initEvents();
    }
    exports.init = init;

    function initView() {

    }

    function initEvents() {
        /**
         * Delete the programs that were selected in program list
         */
        $('#doDeleteConfiguration').onWrap('click', function() {
            var configurations = $("#confirmDeleteConfiguration").data('configurations');
            for (var i = 0; i < configurations.length; i++) {
                var conf = configurations[i];
                var confName = conf[0];
                CONFIGURATION.deleteConfigurationFromListing(confName, function(result, confName) {
                    UTIL.response(result);
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, "MESSAGE_CONFIGURATION_DELETED", result.message, confName);
                        $('#confList').find('button[name="refresh"]').trigger('click');
                        LOG.info('delete configuration "' + confName);
                    }
                });
            }
            $('.modal').modal('hide');
        }), 'doDeleteConfigurations clicked';
    }
});
