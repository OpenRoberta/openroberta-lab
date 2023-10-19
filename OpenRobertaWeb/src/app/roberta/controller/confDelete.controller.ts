import * as LOG from 'log';
import * as UTIL from 'util';
import * as MSG from 'message';
import * as CONFIGURATION from 'configuration.model';
import * as $ from 'jquery';
import 'bootstrap-table';

function init(): void {
    //        initView();
    initEvents();
}
export { init };

class ConfDeleteResult {
    cause: string;
    cmd: string;
    initToken: string;
    message: string;
    parameters: {};
    rc: string;
}

function initView(): void {}

function initEvents(): void {
    /**
     * Delete the configurations that were selected in configuration list
     */
    $('#doDeleteConfiguration').onWrap('click', function (): void {
        let configurations: [] = $('#confirmDeleteConfiguration').data('configurations');
        for (let i: number = 0; i < configurations.length; i++) {
            let conf: any[] = configurations[i]; //TODO create class
            let confName: string = conf[0];
            CONFIGURATION.deleteConfigurationFromListing(confName, function (result: ConfDeleteResult, confName: string): void {
                UTIL.response(result);
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
