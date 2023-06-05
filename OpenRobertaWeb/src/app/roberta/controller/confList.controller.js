import * as UTIL from 'util.roberta';
import * as CONFLIST from 'confList.model';
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'bootstrap-table';
import * as GUISTATE_C from 'guiState.controller';
import * as CONFIGURATION_C from 'configuration.controller';

/**
 * Initialize table of configurations
 */
export function init() {
    initConfList();
    initConfListEvents();
}

export function switchLanguage() {
    $('#confNameTable').bootstrapTable('destroy');
    initConfList();
    CONFLIST.loadConfList(update);
}
function initConfList() {
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
    $('#confNameTable').bootstrapTable('togglePagination');
}

function initConfListEvents() {
    let $tabConfList = $('#tabConfList');
    let $confNameTable = $('#confNameTable');
    $tabConfList.onWrap('shown.bs.tab', function () {
        GUISTATE_C.setView('tabConfList');
        CONFLIST.loadConfList(update);
    });

    $('#confList')
        .find('button[name="refresh"]')
        .onWrap(
            'click',
            function () {
                CONFLIST.loadConfList(update);
                return false;
            },
            'refresh configuration list clicked'
        );

    $confNameTable.onWrap(
        'click-row.bs.table',
        function ($element, row) {
            CONFIGURATION_C.loadFromListing(row);
        },
        'Load configuration from listing clicked'
    );

    $confNameTable.onWrap(
        'check-all.bs.table',
        function () {
            $('.deleteSomeConf').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        },
        'check all configurations'
    );

    $confNameTable.onWrap(
        'check.bs.table',
        function () {
            $('.deleteSomeConf').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        },
        'check one configuration'
    );

    $confNameTable.onWrap(
        'uncheck-all.bs.table',
        function ($element, rows) {
            $('.deleteSomeConf').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.load').removeClass('disabled');
        },
        'uncheck all configurations'
    );

    $confNameTable.onWrap(
        'uncheck.bs.table',
        function () {
            var selectedRows = $confNameTable.bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('.deleteSomeConf').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        },
        'uncheck one configuration'
    );

    $('#backConfList').onWrap(
        'click',
        function () {
            $('#tabConfiguration').tabWrapShow();
            return false;
        },
        'back to configuration view'
    );

    $(document).onWrap(
        'click',
        '.deleteSomeConf',
        function () {
            var configurations = $confNameTable.bootstrapTable('getSelections', {});
            var names = '';
            for (var i = 0; i < configurations.length; i++) {
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
        'delete configurations'
    );

    $confNameTable.on('shown.bs.collapse hidden.bs.collapse', function () {
        $confNameTable.bootstrapTable('resetWidth');
    });

    function resizeTable() {
        $confNameTable.bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
    }
    $(window).resize(function () {
        resizeTable();
    });
}

function update(result) {
    let $confNameTable = $('#confNameTable');
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

var eventsDeleteLoad = {
    'click .delete': function (e, value, row) {
        //var deleted = false;
        e.stopPropagation();
        var selectedRows = [row];
        var names = '';
        for (var i = 0; i < selectedRows.length; i++) {
            names += selectedRows[i][0];
            names += '<br>';
        }
        $('#confirmDeleteConfName').html(names);
        $('#confirmDeleteConfiguration').data('configurations', selectedRows);
        $('#confirmDeleteConfiguration').modal('show');
        return false;
    },
    'click .load': function (e, value, row) {
        CONFIGURATION_C.loadFromListing(row);
    },
};

var formatDeleteLoad = function (value, row) {
    var result = '';
    result +=
        '<a href="#" class="delete" rel="tooltip" lkey="Blockly.Msg.CONFLIST_DELETE_TOOLTIP" data-bs-original-title="" title=""><span class="typcn typcn-delete"></span></a>';
    result +=
        '<a href="#" class="load" rel="tooltip" lkey="Blockly.Msg.CONFLIST_LOAD_TOOLTIP" data-bs-original-title="" title=""><span class="typcn typcn-document"></span></a>';
    return result;
};

var titleActions =
    '<a href="#" id="deleteSomeConf" class="deleteSomeConf disabled" rel="tooltip" lkey="Blockly.Msg.CONFLIST_DELETE_ALL_TOOLTIP" data-bs-original-title="" data-container="body" title="">' +
    '<span class="typcn typcn-delete"></span></a>';
