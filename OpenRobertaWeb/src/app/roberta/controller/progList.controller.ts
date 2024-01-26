import * as LOG from 'log';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as PROGLIST from 'progList.model';
import * as USERGROUP from 'userGroup.model';
import * as PROGRAM from 'program.model';
import * as CONFIGURATION_C from 'configuration.controller';
import * as PROGRAM_C from 'program.controller';
import * as GUISTATE_C from 'guiState.controller';
import * as $ from 'jquery';
import 'bootstrap-table';
import { CommonTable } from 'table';
// @ts-ignore
import * as Blockly from 'blockly';

enum View {
    Examples = 'Examples',
    Programs = 'Programs',
}

let view: View;
let $progList: JQuery<HTMLElement>;
let $programNameTable: JQuery<HTMLElement>;
let $userGroupOptGroup: JQuery<HTMLElement>;
let $userGroupSelect: JQuery<HTMLElement>;

/**
 * Initialize table of programs
 */
export function init() {
    initProgList();
    initProgListViewEvents();
}

export function switchLanguage() {
    $('#programNameTable').bootstrapTable('destroy');
    initProgList();
    showView();
}

function initProgList() {
    $programNameTable = $('#programNameTable');
    const myLang = GUISTATE_C.getLanguage();
    const myOptions = {
        locale: myLang,
        theadClasses: 'table-dark',
        pageList: '[ 10, 25, All ]',
        toolbar: '#progListToolbar',
        showRefresh: 'true',
        showPaginationSwitch: 'true',
        columns: [
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>" + (Blockly.Msg.DATATABLE_PROGRAM_NAME || 'Name des Programms') + '</span>',
                sortable: true,
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || 'Erzeugt von') + '</span>',
                sortable: true,
            },
            {
                events: eventsRelations,
                title: "<span class='typcn typcn-flow-merge'></span>",
                sortable: true,
                sorter: sortRelations,
                formatter: formatRelations,
                align: 'left',
                valign: 'middle',
            },
            {
                visible: false,
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || 'Erzeugt am') + '</span>',
                sortable: true,
                formatter: UTIL.formatDate,
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>" + (Blockly.Msg.DATATABLE_ACTUALIZATION || 'Letzte Aktualisierung') + '</span>',
                sortable: true,
                formatter: UTIL.formatDate,
            },
            {
                title: '<input name="btSelectAll" type="checkbox">',
                formatter: function (value, row, index) {
                    if (GUISTATE_C.isUserMemberOfUserGroup() && row[1] === GUISTATE_C.getUserUserGroupOwner()) {
                        return '<input type="checkbox" name="btSelectItem" data-index="' + index + '" disabled>';
                    }
                    return '<input type="checkbox" name="btSelectItem" data-index="' + index + '">';
                },
                valign: 'middle',
                halign: 'center',
                align: 'center',
            },
            {
                events: eventsDeleteShareLoad,
                title: titleActions,
                align: 'left',
                valign: 'top',
                formatter: formatDeleteShareLoad,
                width: '117px',
            },
        ],
    };
    const options = { ...CommonTable.options, ...myOptions };
    $programNameTable.bootstrapTable(options);
    $programNameTable.bootstrapTable('togglePagination');
}

function initProgListViewEvents() {
    let $tabProgList = $('#tabProgList');
    $tabProgList.onWrap('show.bs.tab', function () {
        GUISTATE_C.setView('tabProgList');
        view = View[$tabProgList.data('type')];
        showView();
    });
    $('#backProgList').onWrap(
        'click',
        function () {
            // @ts-ignore
            $('#tabProgram').tabWrapShow();
            return false;
        },
        'back to program view'
    );
    $programNameTable.onWrap('toggle-pagination.bs.table', function () {
        resizeTable();
    });
    $(window).onWrap('resize', function () {
        resizeTable();
    });
    function resizeTable() {
        $programNameTable.bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
    }
}

function showView() {
    if (view === View.Programs) {
        showViewUserProgram();
    } else {
        showViewExampleProgram();
    }
    initProgListEvents();
}

function initProgListEvents() {
    $progList = $('#progList');
    $userGroupOptGroup = $('#progListUserGroupScope');
    $userGroupSelect = $userGroupOptGroup.closest('select');

    $progList.find('button[name="refresh"]').onWrap(
        'click',
        function () {
            switch (view) {
                case View.Programs:
                    $userGroupSelect.trigger('change');
                    break;
                case View.Examples:
                default:
                    $('#programNameTable').bootstrapTable('showLoading');
                    PROGLIST.loadExampleList(updateExamplePrograms);
            }
            return false;
        },
        'refresh prog list'
    );

    $userGroupSelect.change(function () {
        if (view == View.Examples) {
            return;
        }
        var selectVal = $userGroupSelect.val();
        $('#programNameTable').bootstrapTable('showLoading');
        if (selectVal === 'userProgram') {
            PROGLIST.loadProgList(update);
        } else {
            PROGLIST.loadProgListFromUserGroupMembers(selectVal, update);
        }
    });

    $programNameTable.onWrap(
        'click-row.bs.table',
        function ($element, row) {
            loadFromListing(row);
        },
        'Load program from listing clicked'
    );

    $programNameTable.onWrap(
        'check-all.bs.table check.bs.table',
        function () {
            $('#deleteSomeProgHeader').removeClass('disabled');
            $('#deleteSomeProgHeader').tooltip({ trigger: 'hover' });
            $programNameTable.find('#shareSome').removeClass('disabled');
            $programNameTable.find('.delete, .share, .gallery, .load').addClass('disabled');
        },
        'check all programs'
    );

    $programNameTable.onWrap(
        'uncheck-all.bs.table',
        function () {
            $('#deleteSomeProgHeader').addClass('disabled');
            $programNameTable.find('#shareSome').addClass('disabled');
            $programNameTable.find('.delete, .share, .gallery, .load').filter(':not([data-status="disabled"])').removeClass('disabled');
        },
        'uncheck all programs'
    );

    $programNameTable.onWrap(
        'uncheck.bs.table',
        function () {
            var selectedRows = $programNameTable.bootstrapTable('getSelections');
            if (!selectedRows || selectedRows.length === 0) {
                $('#deleteSomeProgHeader').addClass('disabled');
                $programNameTable.find('#shareSome').addClass('disabled');
                $programNameTable.find('.delete, .share, .gallery, .load').filter(':not([data-status="disabled"])').removeClass('disabled');
            }
        },
        'uncheck one program'
    );

    $programNameTable.on('shown.bs.collapse hidden.bs.collapse', function () {
        $programNameTable.bootstrapTable('resetWidth');
    });

    $('#deleteSomeProgHeader').onWrap('click', function () {
        var programs = $programNameTable.bootstrapTable('getSelections', {});
        var names = '<br>';
        for (var i = 0; i < programs.length; i++) {
            names += programs[i][0];
            names += '<br>';
        }
        $('#confirmDeleteProgramName').html(names);
        let $confirmDeleteProgram = $('#confirmDeleteProgram');
        $confirmDeleteProgram.oneWrap('hide.bs.modal', function () {
            $('#programNameTable').bootstrapTable('showLoading');
            PROGLIST.loadProgList(update);
        });
        $confirmDeleteProgram.data('programs', programs);
        $confirmDeleteProgram.modal('show');
        return false;
    });
}

function showViewUserProgram() {
    $('#programNameTable').bootstrapTable('showLoading');
    $programNameTable.bootstrapTable('showColumn', [2, 6]);
    $programNameTable.bootstrapTable('refreshOptions', {
        sortName: 4,
        sortOrder: 'desc',
        search: true,
        filterControl: true,
    });
    var $selectProg = $(
        "<select class='form-select filter' id='progListScopeSelect'>" +
            "<option data-translation-targets='html' value='userProgram'>" +
            Blockly.Msg.MENU_LIST_PROG +
            '</option>' +
            "<optGroup data-translation-targets='label' id='progListUserGroupScope' label=" +
            Blockly.Msg.DATATABLE_USERGROUPS +
            '></optGroup>' +
            '</select>'
    );
    $('#progList .search').prepend($selectProg);
    if (!GUISTATE_C.isUserMemberOfUserGroup()) {
        USERGROUP.loadUserGroupList(function (result) {
            if (result.rc == 'ok' && result.userGroups.length > 0) {
                result.userGroups.forEach(function (userGroup) {
                    $('#progListUserGroupScope').append('<option value="' + userGroup.name + '">' + userGroup.name + '</option>');
                });
            }
        });
    }
    PROGLIST.loadProgList(update);
}

function showViewExampleProgram() {
    $('#programNameTable').bootstrapTable('showLoading');
    $programNameTable.bootstrapTable('hideColumn', [2, 6]);
    $programNameTable.bootstrapTable('refreshOptions', {
        sortName: 0,
        sortOrder: 'asc',
        search: false,
        filterControl: false,
    });
    PROGLIST.loadExampleList(updateExamplePrograms);
}

function updateTooltips() {
    $('#progList>.bootstrap-table')
        .find('button[name="paginationSwitch"]')
        .attr('title', '')
        .attr('rel', 'tooltip')
        .attr('data-bs-original-title', $('#programNameTable').bootstrapTable['locales'][GUISTATE_C.getLanguage()].formatPaginationSwitch())
        .tooltip({ trigger: 'hover' });
    $('#progList>.bootstrap-table')
        .find('button[name="refresh"]')
        .attr('title', '')
        .attr('rel', 'tooltip')
        .attr('data-bs-original-title', $('#programNameTable').bootstrapTable['locales'][GUISTATE_C.getLanguage()].formatRefresh())
        .tooltip({ trigger: 'hover' });
    $programNameTable.find('[rel="tooltip"]').tooltip({ trigger: 'hover' });
}
function update(result) {
    UTIL.response(result);
    if (result.rc === 'ok') {
        $programNameTable.bootstrapTable('load', result.programNames);
        $('#progList .search').show();
    } else {
        if (result.cmd === 'loadPN') {
            $('#backProgList').clickWrap();
        }
    }
    $programNameTable.bootstrapTable('hideLoading');
    $('#deleteSomeProgHeader').removeClass('invisible');
    updateTooltips();
}

function updateExamplePrograms(result) {
    UTIL.response(result);
    if (result.rc === 'ok') {
        $('#programNameTable').bootstrapTable('load', result.programNames);
    } else {
        if (result.cmd === 'loadPN') {
            $('#backProgList').clickWrap();
        }
    }
    $programNameTable.bootstrapTable('hideLoading');
    $('#deleteSomeProgHeader').addClass('invisible');
    updateTooltips();
}

var eventsRelations = {
    'click .showRelations': function (e, value, row, index) {
        e.stopPropagation();
        var collapseName = '.relation' + index;
        $(collapseName).collapse('toggle');
    },
};

var eventsDeleteShareLoad = {
    'click .delete': function (e, value, row) {
        e.stopPropagation();
        var selectedRows = [row];
        var names = '<br>';
        for (var i = 0; i < selectedRows.length; i++) {
            names += selectedRows[i][0];
            names += '<br>';
        }
        $('#confirmDeleteProgramName').html(names);
        let $confirmDeleteProgram = $('#confirmDeleteProgram');
        $confirmDeleteProgram.data('programs', selectedRows);
        $confirmDeleteProgram.oneWrap('hidden.bs.modal', function () {});
        $confirmDeleteProgram.modal('show');
        return false;
    },
    'click .share': function (e, value, row) {
        e.stopPropagation();
        if (!row[2].sharedFrom) {
            $('#show-relations').trigger('updateAndShow', [row]);
        }
        return false;
    },
    'click .gallery': function (e, value, row) {
        e.stopPropagation();
        if (!row[2].sharedFrom && !GUISTATE_C.isUserMemberOfUserGroup()) {
            $('#share-with-gallery').trigger('updateAndShow', [row]);
        }
        return false;
    },
    'click .load': function (e, value, row) {
        e.stopPropagation();
        loadFromListing(row);
    },
};

var formatRelations = function (value, row, index) {
    if ($.isEmptyObject(value)) {
        return '<span class="typcn typcn-minus"></span>';
    }
    if (value.sharedFrom === 'READ') {
        return '<span class="typcn typcn-eye"></span>';
    }
    if (value.sharedFrom === 'WRITE') {
        return '<span class="typcn typcn-pencil"></span>';
    }
    if (value.sharedFrom === 'X_WRITE') {
        return '<span class="typcn typcn-key"></span>';
    }
    if (value.sharedWith && value.sharedWith.length == 1) {
        var result = '';
        $.each(value.sharedWith, function (i, obj) {
            result += '<span>';
            if (obj.type === 'User' && obj.label === 'Roberta') {
                // should not happen
            } else if (obj.right === 'READ') {
                result += '<span class="typcn typcn-eye"></span>';
            } else if (obj.right === 'X_WRITE') {
                result += '<span class="typcn typcn-key"></span>';
            } else if (obj.right === 'WRITE') {
                result += '<span class="typcn typcn-pencil"></span>';
            }

            if (obj.type === 'User') {
                result += '&nbsp;<span class="typcn typcn-user"></span>';
            } else if (obj.type === 'UserGroup') {
                result += '&nbsp;<span class="typcn typcn-group"></span>';
            }

            result += '&nbsp;';

            result += '<span class="value">';
            result += obj.label;
            result += '</span>';
            result += '</span>';
        });
        return result;
    }
    if (value.sharedWith && Object.keys(value.sharedWith).length > 1) {
        let result: string[] = [];
        $.each(value.sharedWith, function (i, obj) {
            var typeLabel = '';

            if (obj.type === 'User') {
                typeLabel = '&nbsp;<span class="typcn typcn-user"></span>';
            } else if (obj.type === 'UserGroup') {
                typeLabel = '&nbsp;<span class="typcn typcn-group"></span>';
            }

            if (obj.type === 'User' && obj.label === 'Roberta') {
                //Do nothing
            } else if (obj.right === 'READ') {
                result.push('<span class="typcn typcn-eye"></span>' + typeLabel + '&nbsp;<span class="value">' + obj.label + '</span>');
            } else if (obj.right === 'WRITE') {
                result.push('<span class="typcn typcn-pencil"></span>' + typeLabel + '&nbsp;<span class="value">' + obj.label + '</span>');
            }
        });
        var resultString = '<div style="white-space:nowrap;"><span style="float:left;">';
        resultString += result[0];
        resultString +=
            '</span><a class="collapsed showRelations" href="#" style=" float:right;"' +
            ' href="#" data-bs-toggle="collapse" data-bs-target=".relation' +
            index +
            '"></a></div>';
        for (var i = 1; i < result.length; i++) {
            resultString += '<div style="clear:both;" class="collapse relation' + index + '">';
            resultString += result[i];
            resultString += '</div>';
        }
        return resultString;
    }
};

var formatDeleteShareLoad = function (value, row) {
    // TODO check here and on the server, if this user is allowed to share programs
    var result = '';
    if (view === View.Programs) {
        if (!GUISTATE_C.isUserMemberOfUserGroup() || GUISTATE_C.getUserUserGroupOwner() !== row[1]) {
            result +=
                '<a href="#" class="delete" rel="tooltip" lkey="Blockly.Msg.PROGLIST_DELETE_TOOLTIP" data-bs-original-title="' +
                Blockly.Msg.PROGLIST_DELETE_TOOLTIP +
                '" title=""><span class="typcn typcn-delete"></span></a>';
        } else {
            result += '<a href="#" class="delete disabled" data-status="disabled"><span class="typcn typcn-delete"></span></a>';
        }
        if (row[2].sharedFrom) {
            result += '<a href="#" class="share disabled" data-status="disabled"><span class="typcn typcn-flow-merge"></span></a>';
            if (!GUISTATE_C.isUserMemberOfUserGroup()) {
                result += '<a href="#" class="gallery disabled" data-status="disabled"><span class="typcn typcn-th-large-outline"></span></a>';
            }
        } else {
            result +=
                '<a href="#" class="share" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_TOOLTIP" data-bs-original-title="' +
                Blockly.Msg.PROGLIST_SHARE_TOOLTIP +
                '" title=""><span class="typcn typcn-flow-merge"></span></a>';
            if (!GUISTATE_C.isUserMemberOfUserGroup()) {
                result +=
                    '<a href="#" class="gallery" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP" data-bs-original-title="' +
                    Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP +
                    '" title=""><span class="typcn typcn-th-large-outline"></span></a>';
            }
        }
    }
    result +=
        '<a href="#" class="load" rel="tooltip" lkey="Blockly.Msg.PROGLIST_LOAD_TOOLTIP" data-bs-original-title="' +
        Blockly.Msg.PROGLIST_LOAD_TOOLTIP +
        '" title=""><span class="typcn typcn-document"></span></a>';
    return result;
};

var sortRelations = function (a, b) {
    if ($.isEmptyObject(a) && $.isEmptyObject(b)) {
        return 0;
    }
    if (a.sharedFrom && b.sharedFrom) {
        if (a.sharedFrom === 'WRITE' && b.sharedFrom === 'WRITE') return 0;
        if (a.sharedFrom === 'WRITE') return 1;
        else return -1;
    }
    if (a.sharedWith && b.sharedWith) {
        var value = {
            a: a.sharedWith[0].right,
            b: b.sharedWith[0].right,
        };
        if (value.a === value.b) return 0;
        if (value.a === 'WRITE') return 1;
        else return -1;
    }
    if ($.isEmptyObject(a)) {
        return -1;
    }
    if ($.isEmptyObject(b)) {
        return 1;
    }
    if (a.sharedWith) {
        return 1;
    }
    return -1;
};
var titleActions =
    '<a href="#" id="deleteSomeProgHeader" class="deleteSomeProg disabled" rel="tooltip" lkey="Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP" data-bs-original-title="' +
    Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP +
    '"title=""><span class="typcn typcn-delete"></span></a>';

/**
 * Load the program and configuration that was selected in program list
 */
function loadFromListing(program) {
    LOG.info('loadFromList ' + program[0]);
    PROGRAM.loadProgramFromListing(program[0], program[1], program[3], function (result) {
        if (result.rc === 'ok') {
            result.programShared = false;
            var alien = program[1] === GUISTATE_C.getUserAccountName() ? null : program[1];
            if (alien) {
                result.programShared = 'READ';
            }
            if (program[2].sharedFrom) {
                result.programShared = program[2].sharedFrom;
            }
            result.name = program[0];
            GUISTATE_C.setProgram(result, alien);
            GUISTATE_C.setProgramXML(result.progXML);

            if (result.configName === undefined) {
                if (result.confXML === undefined) {
                    // Set default configuration
                    GUISTATE_C.setConfigurationNameDefault();
                    GUISTATE_C.setConfigurationXML(GUISTATE_C.getConfigurationConf());
                } else {
                    // Set anonymous configuration
                    GUISTATE_C.setConfigurationName('');
                    GUISTATE_C.setConfigurationXML(result.confXML);
                }
            } else {
                // Set named configuration
                GUISTATE_C.setConfigurationName(result.configName);
                GUISTATE_C.setConfigurationXML(result.confXML);
            }
            $('#tabProgram').oneWrap('shown.bs.tab', function () {
                CONFIGURATION_C.reloadConf();
                PROGRAM_C.reloadProgram();
            });
            // @ts-ignore
            $('#tabProgram').tabWrapShow();
        }
        MSG.displayInformation(result, '', result.message, '', '');
    });
}
