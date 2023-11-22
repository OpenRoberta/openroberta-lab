/**
 * @fileOverview Provides the view/tab for all programs shared with the gallery.
 *               Programs are shown in a bootstrap table with card view. From
 *               this view one can select (double click) a program to load it
 *               into the program (blockly) view to execute or copy it.
 *
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
import * as UTIL from 'util';
import * as MSG from 'message';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGLIST from 'progList.model';
import * as PROGRAM from 'program.model';
import * as PROGRAM_C from 'program.controller';
import * as Blockly from 'blockly';
import { CardView, CommonTable } from 'table';
import * as $ from 'jquery';
import 'bootstrap-table';
import 'bootstrap-tagsinput';

var BACKGROUND_COLORS = ['#33B8CA', '#EBC300', '#005A94', '#179C7D', '#F29400', '#E2001A', '#EB6A0A', '#8FA402', '#BACC1E', '#9085BA', '#FF69B4', '#DF01D7'];
var currentColorIndex;
var currentViewMode = 'gallery';
var allRows = [];
/**
 * Initialize table of programs
 */
function init() {
    initGalleryList();
    initGalleryToolbar();
    initGalleryListEvents();
}

export function switchLanguage() {
    $('#galleryTable').bootstrapTable('destroy');
    init();
    if (GUISTATE_C.getView() === 'tabGalleryList') {
        showView();
    }
}

function initGalleryToolbar() {
    var $selectRobot = $('<select id="filterRobot" class="filter form-select"></select>');
    $('#galleryList .search').prepend($selectRobot);
    var groups = getRobotGroups();
    var $filterField = $('#filterRobot');
    for (var group in groups) {
        $filterField.append(new Option(groups[group], group));
    }
    $filterField.append(new Option('All robots', 'all', true, true));

    var $selectOrderBy = $(
        '<label>Order by:</label> <select class="filter form-select" id="fieldOrderBy">' +
            '<option selected="" value="4:desc">Newest</option>' +
            '<option value="4:asc">Oldest</option>' +
            '<option value="1:asc">Program name</option>' +
            '<option value="0:asc">Robot</option>' +
            '</select>'
    );
    $('#galleryList .search').append($selectOrderBy);
}

function initGalleryList() {
    const myLang = GUISTATE_C.getLanguage();
    const myOptions = {
        columns: [
            {
                sortable: true,
                title: '',
                formatter: CardView.robot,
            },
            {
                title: '',
                sortable: true,
                formatter: CardView.name,
            },
            {
                title: '',
                sortable: true,
                formatter: CardView.programDescription,
            },
            {
                title: '',
                sortable: true,
                formatter: function (goal) {
                    return CardView.titleLabel(goal, 'GALLERY_BY', 'cardViewInfo');
                },
            },
            {
                sortable: true,
                title: '',
                formatter: function (date) {
                    return CardView.titleLabel(UTIL.formatDate(date.replace(/\s/, 'T')), 'GALLERY_DATE', 'cardViewInfo');
                },
            },
            {
                title: '',
                formatter: function (num) {
                    return CardView.titleTypcn(num, 'eye-outline');
                },
                sortable: true,
            },
            {
                title: '',
                formatter: function (num) {
                    return CardView.titleTypcn(num, 'heart-full-outline');
                },
                sortable: true,
            },
            {
                title: '',
                sortable: true,
                formatter: function (value, row) {
                    return CardView.programTags(row[2]);
                },
            },
            {
                title: '',
                events: eventsLike,
                formatter: formatLike,
            },
        ],
        filterControl: true,
        locale: myLang,
        rowAttributes: rowAttributes,
        toolbar: '#galleryListToolbar',
    };
    const options = { ...CommonTable.options, ...CardView.options, ...myOptions };
    $('#galleryTable').bootstrapTable(options);
    $('#galleryTable').bootstrapTable('togglePagination');
}

function initGalleryListEvents() {
    $('#tabGalleryList').onWrap(
        'shown.bs.tab',
        function () {
            guiStateController.setView('tabGalleryList');
            showView();
            return false;
        },
        'gallery clicked'
    );

    $(window).resize(function () {
        $('#galleryTable').bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
    });

    $('#tabGalleryList').onWrap(
        'shown.bs.tab',
        function (e) {
            $(window).trigger('resize');
        },
        'shown gallery list'
    );

    $('#galleryTable').onWrap(
        'post-body.bs.table',
        function (e) {
            configureTagsInput();
        },
        'page-change gallery list'
    );

    $('#galleryList')
        .find('button[name="refresh"]')
        .onWrap(
            'click',
            function () {
                loadTableData();
                return false;
            },
            'refresh gallery list clicked'
        );

    $('#galleryTable').onWrap(
        'click-row.bs.table',
        function ($element, row) {
            PROGRAM_C.loadFromGallery(row);
        },
        'Load program from gallery double clicked'
    );

    $('#backGalleryList').onWrap(
        'click',
        function () {
            $('#tabProgram').clickWrap();
            return false;
        },
        'back to program view'
    );

    $('#galleryTable').on('shown.bs.collapse hidden.bs.collapse', function (e) {
        $('#galleryTable').bootstrapTable('resetWidth');
    });

    $('#filterRobot').onWrap('change', loadTableData, 'gallery filter changed');

    $('#fieldOrderBy').change(function (e) {
        var fieldData = e.target.value.split(':');
        var row = parseInt(fieldData[0]);
        $('#galleryTable').bootstrapTable('sortBy', {
            field: row,
            sortOrder: fieldData[1],
        });
        configureTagsInput();
    });
}

function loadTableData() {
    $('#galleryTable').bootstrapTable('showLoading');
    var params = {};
    var group = $('#filterRobot').val();
    if (group !== 'all') {
        params['group'] = group;
    }
    PROGLIST.loadGalleryList(updateTable, params);
}

function updateTable(result) {
    UTIL.response(result);
    if (result.rc === 'ok') {
        allRows = result.programNames;
        $('#galleryTable').bootstrapTable('load', result.programNames);
        configureTagsInput();
    }
    $('#galleryTable').bootstrapTable('hideLoading');
    $('#galleryTable').bootstrapTable('resetView', {
        height: UTIL.calcDataTableHeight(),
    });
}

function updateLike(value, index, row) {
    let myIndex = allRows.indexOf(row);
    row[6] += value;
    row[8] = value > 0 ? true : false;
    $('#galleryTable').bootstrapTable('updateRow', {
        index: myIndex,
        row: row,
    });
    configureTagsInput();
}

function showView() {
    $('#filterRobot').val(GUISTATE_C.getRobotGroup());
    loadTableData();
}
//TODO: Robot group names exists in plugin properties
function getRobotGroups() {
    var robots = GUISTATE_C.getRobots();
    var groups = {};

    var coerceName = function (name, group) {
        if (group === 'arduino') return 'Nepo4Arduino';
        if (group === 'ev3') return 'Ev3';
        return GUISTATE_C.getMenuRobotRealName(name);
    };

    for (var propt in robots) {
        var group = robots[propt].group;
        var name = robots[propt].name;
        if (group && !groups[group]) {
            groups[group] = coerceName(name, group);
        }
    }
    return groups;
}

var eventsLike = {
    'click .like': function (e, value, row, index) {
        e.stopPropagation();
        if ($(e.target).data('blocked') == 1) return;
        $(e.target).data('blocked', 1);
        PROGRAM.likeProgram(true, row[1], row[3], row[0], function (result) {
            if (result.rc == 'ok') {
                updateLike(1, index, row);
                $(e.target).data('blocked', 0);
            } else {
                $(e.target).data('blocked', 0);
            }
            MSG.displayInformation(result, result.message, result.message, row[1]);
        });
        return false;
    },
    'click .dislike': function (e, value, row, index) {
        e.stopPropagation();
        if ($(e.target).data('blocked') == 1) return;
        $(e.target).data('blocked', 1);
        PROGRAM.likeProgram(false, row[1], row[3], row[0], function (result) {
            if (result.rc == 'ok') {
                updateLike(-1, index, row);
                $(e.target).data('blocked', 0);
            } else {
                $(e.target).data('blocked', 0);
            }
            MSG.displayInformation(result, result.message, result.message, row[1]);
        });
        return false;
    },
};

// TODO extend this, if more customization features are available, eg. robot graphics, uploaded images.
var rowAttributes = function (row, index) {
    var hash = UTIL.getHashFrom(row[0] + row[1] + row[3]);
    currentColorIndex = hash % BACKGROUND_COLORS.length;
    return {
        style: 'background-color :' + BACKGROUND_COLORS[currentColorIndex] + ';' + 'cursor: pointer;  z-index: 1;',
    };
};

var titleNumberOfViews = '<span class="galleryIcon typcn typcn-eye-outline" />';

var titleLikes = '<span class="galleryIcon typcn typcn-heart-full-outline" />';

export { init, rowAttributes, titleNumberOfViews, titleLikes };

var formatLike = function (value, row, index) {
    if (GUISTATE_C.isUserLoggedIn()) {
        if (value) {
            return (
                '<div class="galleryLike"><button href="#" class="dislike galleryLike btn"><span lkey="Blockly.Msg.GALLERY_DISLIKE">' +
                (Blockly.Msg.GALLERY_DISLIKE || 'GALLERY_DISLIKE') +
                '</span></button></div>'
            );
        } else {
            return (
                '<div class="galleryLike"><button href="#" class="like galleryLike btn"><span lkey="Blockly.Msg.GALLERY_LIKE">' +
                (Blockly.Msg.GALLERY_LIKE || 'GALLERY_LIKE') +
                '</span></button></div>'
            );
        }
    } else {
        return '<div style="display:none;" />'; // like is only for logged in users allowed
    }
};

function configureTagsInput() {
    $('.infoTags').tagsinput();
    $('#galleryTable .bootstrap-tagsinput').addClass('galleryTags');
    $('#galleryList').find('.galleryTags>input').attr('readonly', 'true');
    $('#galleryList').find('span[data-role=remove]').addClass('hidden');
}
