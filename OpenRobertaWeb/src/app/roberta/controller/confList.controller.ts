import * as UTIL from 'util.roberta';
import * as CONFLIST from 'confList.model';
//@ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'bootstrap-table';
import * as GUISTATE_C from 'guiState.controller';
import * as CONFIGURATION_C from 'configuration.controller';

/**
 * Initialize table of configurations
 */
export function init(): void {
    initConfList();
    initConfListEvents();
}

export function switchLanguage(): void {
    $('#confNameTable').bootstrapTable('destroy');
    initConfList();
    CONFLIST.loadConfList(update);
}
function initConfList(): void {
    $('#confNameTable').bootstrapTable({
        locale: GUISTATE_C.getLanguage(),
        theadClasses: 'table-dark',
        pageList: '[ 10, 25, All ]',
        toolbar: '#confListToolbar',
        showRefresh: 'true',
        showPaginationSwitch: 'true',
        pagination: 'true',
        buttonsAlign: 'right',
        resizable: 'true',
        iconsPrefix: 'typcn',
        icons: {
            paginationSwitchDown: 'typcn-document-text',
            paginationSwitchUp: 'typcn-book',
            refresh: 'typcn-refresh',
        },
        columns: [
            {
                title:
                    "<span lkey='Blockly.Msg.DATATABLE_CONFIGURATION_NAME'>" +
                    (Blockly.Msg.DATATABLE_CONFIGURATION_NAME || 'Name der Configuration') +
                    '</span>',
                sortable: true,
                field: '0',
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || 'Erzeugt von') + '</span>',
                sortable: true,
                field: '1',
            },
            {
                visible: false,
                field: '2',
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>" + (Blockly.Msg.DATATABLE_ACTUALIZATION || 'Letzte Aktualisierung') + '</span>',
                sortable: true,
                field: '3',
                formatter: UTIL.formatDate,
            },
            {
                field: '4',
                title: '<input name="btSelectAll" type="checkbox">',
                valign: 'middle',
                halign: 'center',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<input type="checkbox" name="btSelectItem" data-index="' + index + '">';
                },
            },
            {
                field: '5',
                events: eventsDeleteLoad,
                title: titleActions,
                align: 'left',
                valign: 'top',
                formatter: formatDeleteLoad,
                width: '89px',
            },
        ],
    });
    $('#confList>.bootstrap-table')
        .find('button[name="paginationSwitch"]')
        .attr('title', '')
        .attr('rel', 'tooltip')
        //@ts-ignore
        .attr('data-bs-original-title', $('#confNameTable').bootstrapTable.locales[GUISTATE_C.getLanguage()].formatPaginationSwitch())
        .tooltip({ trigger: 'hover' });
    $('#confList>.bootstrap-table')
        .find('button[name="refresh"]')
        .attr('title', '')
        .attr('rel', 'tooltip')
        //@ts-ignore
        .attr('data-bs-original-title', $('#confNameTable').bootstrapTable.locales[GUISTATE_C.getLanguage()].formatRefresh())
        .tooltip({ trigger: 'hover' });
    $('#confNameTable').bootstrapTable('togglePagination');
}

function initConfListEvents(): void {
    let $tabConfList: JQuery<HTMLElement> = $('#tabConfList');
    let $confNameTable: JQuery<HTMLElement> = $('#confNameTable');
    $tabConfList.onWrap('shown.bs.tab', function () {
        GUISTATE_C.setView('tabConfList');
        CONFLIST.loadConfList(update);
    });

    $('#confList')
        .find('button[name="refresh"]')
        .onWrap(
            'click',
            function (): boolean {
                CONFLIST.loadConfList(update);
                return false;
            },
            'refresh configuration list clicked'
        );

    $confNameTable.onWrap(
        'click-row.bs.table',
        function ($element, row): void {
            CONFIGURATION_C.loadFromListing(row);
        },
        'Load configuration from listing clicked'
    );

    $confNameTable.onWrap(
        'check-all.bs.table check.bs.table',
        function (): void {
            $('#deleteSomeConfHeader').removeClass('disabled');
            $('#deleteSomeConfHeader').tooltip({ trigger: 'hover' });
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        },
        'check all configurations'
    );

    $confNameTable.onWrap(
        'uncheck-all.bs.table',
        function ($element, rows): void {
            $('#deleteSomeConfHeader').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.load').removeClass('disabled');
        },
        'uncheck all configurations'
    );

    $confNameTable.onWrap(
        'uncheck.bs.table',
        function (): void {
            var selectedRows = $confNameTable.bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('#deleteSomeConfHeader').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        },
        'uncheck one configuration'
    );

    $('#backConfList').onWrap(
        'click',
        function (): boolean {
            //@ts-ignore
            $('#tabConfiguration').tabWrapShow();
            return false;
        },
        'back to configuration view'
    );

    $(document).onWrap(
        'click',
        '.deleteSomeConf',
        function (): boolean {
            let configurations = $confNameTable.bootstrapTable('getSelections', {});
            let names: string = '';
            for (let i: number = 0; i < configurations.length; i++) {
                names += configurations[i][0];
                names += '<br>';
            }
            $('#confirmDeleteConfName').html(names);
            $('#confirmDeleteConfiguration').oneWrap('hide.bs.modal', function () {
                CONFLIST.loadConfList(update);
            });
            $('#confirmDeleteConfiguration').data('configurations', configurations);
            $('#confirmDeleteConfiguration').modal('show');
            return false;
        },
        //@ts-ignore
        //helper.d.ts only got 3 params while the function in warp.ts got 4
        'delete configurations'
    );

    $confNameTable.on('shown.bs.collapse hidden.bs.collapse', function (): void {
        $confNameTable.bootstrapTable('resetWidth');
    });

    function resizeTable(): void {
        $confNameTable.bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
    }
    $(window).resize(function (): void {
        resizeTable();
    });
}

function update(result): void {
    let $confNameTable: JQuery<HTMLElement> = $('#confNameTable');
    UTIL.response(result);
    if (result.rc === 'ok') {
        $confNameTable.bootstrapTable('load', result.configurationNames);
    }
    $('#deleteSomeConf').attr('data-bs-original-title', Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP || 'Click here to delete all selected robot configurations.');
    $confNameTable.find('.delete').attr('data-bs-original-title', Blockly.Msg.CONFLIST_DELETE_TOOLTIP || 'Click here to delete your robot configuration.');
    $confNameTable
        .find('.load')
        .attr('data-bs-original-title', Blockly.Msg.CONFLIST_LOAD_TOOLTIP || 'Click here to load your robot configuration in the configuration environment.');
    $confNameTable.find('[rel="tooltip"]').tooltip({
        trigger: 'hover',
    });
}

let eventsDeleteLoad = {
    'click .delete': function (e: Event, value, row): boolean {
        //var deleted = false;
        e.stopPropagation();
        let selectedRows: any[] = [row];
        let names: string = '';
        for (let i: number = 0; i < selectedRows.length; i++) {
            names += selectedRows[i][0];
            names += '<br>';
        }
        $('#confirmDeleteConfName').html(names);
        $('#confirmDeleteConfiguration').data('configurations', selectedRows);
        $('#confirmDeleteConfiguration').modal('show');
        return false;
    },
    'click .load': function (e, value, row): void {
        CONFIGURATION_C.loadFromListing(row);
    },
};

let formatDeleteLoad = function (value, row) {
    let result: string = '';
    result +=
        '<a href="#" class="delete" rel="tooltip" lkey="Blockly.Msg.CONFLIST_DELETE_TOOLTIP" data-bs-original-title="" title=""><span class="typcn typcn-delete"></span></a>';
    result +=
        '<a href="#" class="load" rel="tooltip" lkey="Blockly.Msg.CONFLIST_LOAD_TOOLTIP" data-bs-original-title="" title=""><span class="typcn typcn-document"></span></a>';
    return result;
};

let titleActions: string =
    '<a href="#" id="deleteSomeConfHeader" class="deleteSomeConf disabled" rel="tooltip" lkey="Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP" data-bs-original-title="' +
    Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP +
    '" title="">' +
    '<span class="typcn typcn-delete"></span></a>';
