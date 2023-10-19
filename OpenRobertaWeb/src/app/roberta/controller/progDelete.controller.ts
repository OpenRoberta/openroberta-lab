import * as LOG from 'log';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as PROGRAM from 'program.model';
import * as $ from 'jquery';
import 'bootstrap-table';

export function init(): void {
    //        initView();
    initEvents();
}

function initView(): void {}

function initEvents(): void {
    /**
     * Delete the programs that were selected in program list
     */
    $('#doDeleteProgram').onWrap('click', function() {
        let programs: any[] = $('#confirmDeleteProgram').data('programs');
        for (let i = 0; i < programs.length; i++) {
            let prog = programs[i];
            let progName: string = prog[0];
            let progOwner: string = prog[1];
            let progRight = prog[2];
            let author = prog[3];
            if (progRight.sharedFrom) {
                PROGRAM.deleteShare(progName, progOwner, author, function(result, progName): void {
                    UTIL.response(result);
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, 'MESSAGE_PROGRAM_DELETED', result.message, progName);
                        $('#progList').find('button[name="refresh"]').clickWrap();
                        LOG.info('remove shared program "' + progName + '"form List');
                    }
                });
            } else {
                PROGRAM.deleteProgramFromListing(progName, author, function(result, progName): void {
                    UTIL.response(result);
                    if (result.rc === 'ok') {
                        MSG.displayInformation(result, 'MESSAGE_PROGRAM_DELETED', result.message, progName);
                        $('#progList').find('button[name="refresh"]').clickWrap();
                        LOG.info('delete program "' + progName);
                    }
                });
            }
        }
        $('.modal').modal('hide');
    }),
        'delete programs clicked';
}
