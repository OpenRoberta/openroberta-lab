define(["require", "exports", "log", "util.roberta", "message", "configuration.model", "jquery", "bootstrap-table"], function (require, exports, LOG, UTIL, MSG, CONFIGURATION, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = void 0;
    function init() {
        //        initView();
        initEvents();
    }
    exports.init = init;
    function initEvents() {
        $('#doDeleteConfiguration').onWrap('click', function () {
            var configurations = $('#confirmDeleteConfiguration').data('configurations');
            for (var i = 0; i < configurations.length; i++) {
                var conf = configurations[i]; //TODO create class
                var confName = conf[0];
                CONFIGURATION.deleteConfigurationFromListing(confName, function (result, confName) {
                    // @ts-ignore
                    UTIL.response(result); // response doesnt exist?
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, 'MESSAGE_CONFIGURATION_DELETED', result.message, confName, null);
                        $('#confList').find('button[name="refresh"]').clickWrap();
                        LOG.info('delete configuration "' + confName);
                    }
                });
            }
            $('.modal').modal('hide');
        }),
            'doDeleteConfigurations clicked';
    }
});
