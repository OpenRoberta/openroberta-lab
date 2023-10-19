import * as LOG from 'log';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as CONFIGURATION from 'configuration.model';
import * as $ from 'jquery';
import 'bootstrap-table';
import { BaseResponse } from '../ts/restEntities';

export function init(): void {
    //        initView();
    initEvents();
}

function initEvents(): void {
    $('#doDeleteConfiguration').onWrap('click', function (): void {
        let configurations: [] = $('#confirmDeleteConfiguration').data('configurations');
        for (let i: number = 0; i < configurations.length; i++) {
            let conf: any[] = configurations[i]; //TODO create class
            let confName: string = conf[0];
            CONFIGURATION.deleteConfigurationFromListing(confName, function (result: BaseResponse, confName: string): void {
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
