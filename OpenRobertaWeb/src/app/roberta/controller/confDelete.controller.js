import * as LOG from 'log';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as CONFIGURATION from 'configuration.model';
import * as $ from 'jquery';
import 'bootstrap-table';

function init() {
    //        initView();
    initEvents();
}
export { init };

function initView() {}

function initEvents() {
    /**
     * Delete the configurations that were selected in configuration list
     */
    $('#doDeleteConfiguration').onWrap('click', function() {
        var configurations = $('#confirmDeleteConfiguration').data('configurations');
        for (var i = 0; i < configurations.length; i++) {
            var conf = configurations[i];
            var confName = conf[0];
            CONFIGURATION.deleteConfigurationFromListing(
                confName,
                function(result, confName) {
                    UTIL.response(result);
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, 'MESSAGE_CONFIGURATION_DELETED', result.message, confName);
                        $('#confList').find('button[name="refresh"]').clickWrap();
                        LOG.info('delete configuration "' + confName);
                    }
                },
                'delete configuration'
            );
        }
        $('.modal').modal('hide');
    }),
        'doDeleteConfigurations clicked';
}
