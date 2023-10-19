var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
define(["require", "exports", "util.roberta", "message", "guiState.controller", "progList.model", "program.model", "program.controller", "blockly", "table", "jquery", "bootstrap-table", "bootstrap-tagsinput"], function (require, exports, UTIL, MSG, GUISTATE_C, PROGLIST, PROGRAM, PROGRAM_C, Blockly, table_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.formatLike = exports.titleLikes = exports.titleNumberOfViews = exports.rowAttributes = exports.eventsLike = exports.switchLanguage = exports.init = void 0;
    var BACKGROUND_COLORS = [
        '#33B8CA',
        '#EBC300',
        '#005A94',
        '#179C7D',
        '#F29400',
        '#E2001A',
        '#EB6A0A',
        '#8FA402',
        '#BACC1E',
        '#9085BA',
        '#FF69B4',
        '#DF01D7'
    ];
    var currentColorIndex;
    var allRows = [];
    /**
     * Initialize table of programs
     */
    function init() {
        initGalleryList();
        initGalleryToolbar();
        initGalleryListViewEvents();
    }
    exports.init = init;
    function switchLanguage() {
        $('#galleryTable').bootstrapTable('destroy');
        initGalleryList();
        initGalleryToolbar();
        showView();
    }
    exports.switchLanguage = switchLanguage;
    function initGalleryToolbar() {
        var $selectRobot = $('<select id="filterRobot" class="filter form-select"></select>');
        $('#galleryList .search').prepend($selectRobot);
        var groups = UTIL.getRobotGroupsPrettyPrint();
        var $filterField = $('#filterRobot');
        for (var group in groups) {
            $filterField.append(new Option(groups[group], group));
        }
        $filterField.append(new Option(Blockly.Msg.GALLERY_ALL_ROBOTS, 'all', true, true));
        var $selectOrderBy = $('<label lkey="Blockly.Msg.GALLERY_SORT_BY">' +
            Blockly.Msg.GALLERY_SORT_BY +
            ':</label> <select class="filter form-select" id="fieldOrderBy">' +
            '<option selected="" value="4:desc">' +
            Blockly.Msg.GALLERY_NEWEST +
            '</option>' +
            '<option value="4:asc">' +
            Blockly.Msg.GALLERY_OLDEST +
            '</option>' +
            '<option value="1:asc">' +
            Blockly.Msg.GALLERY_PROGRAM_NAME +
            '</option>' +
            '<option value="0:asc">' +
            Blockly.Msg.GALLERY_ROBOT +
            '</option>' +
            '</select>');
        $('#galleryList .search').append($selectOrderBy);
    }
    function initGalleryList() {
        var myLang = GUISTATE_C.getLanguage();
        var myOptions = {
            columns: [
                {
                    sortable: true,
                    title: '',
                    formatter: table_1.CardView.robot
                },
                {
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.name
                },
                {
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.programDescription
                },
                {
                    title: '',
                    sortable: true,
                    formatter: function (goal) {
                        return table_1.CardView.titleLabel(goal, 'GALLERY_BY', 'cardViewInfo');
                    }
                },
                {
                    sortable: true,
                    title: '',
                    formatter: function (date) {
                        return table_1.CardView.titleLabel(UTIL.formatDate(date.replace(/\s/, 'T')), 'GALLERY_DATE', 'cardViewInfo');
                    }
                },
                {
                    title: '',
                    formatter: function (num) {
                        return table_1.CardView.titleTypcn(num, 'eye-outline');
                    },
                    sortable: true
                },
                {
                    title: '',
                    formatter: function (num) {
                        return table_1.CardView.titleTypcn(num, 'heart-full-outline');
                    },
                    sortable: true
                },
                {
                    title: '',
                    sortable: true,
                    formatter: function (value, row) {
                        return table_1.CardView.programTags(row[2]);
                    }
                },
                {
                    title: '',
                    events: exports.eventsLike,
                    formatter: exports.formatLike
                }
            ],
            filterControl: 'true',
            locale: myLang,
            rowAttributes: exports.rowAttributes,
            toolbar: '#galleryListToolbar',
            sortName: 4,
            sortOrder: 'desc',
            height: UTIL.calcDataTableHeight()
        };
        var options = __assign(__assign(__assign({}, table_1.CommonTable.options), table_1.CardView.options), myOptions);
        $('#galleryTable').bootstrapTable(options);
    }
    function initGalleryListViewEvents() {
        $('#tabGalleryList').onWrap('shown.bs.tab', function (e) {
            GUISTATE_C.setView('tabGalleryList');
            showView();
            return false;
        }, 'gallery clicked');
        $('#backGalleryList').onWrap('click', function () {
            //@ts-ignore
            $('#' + GUISTATE_C.getPrevView()).tabWrapShow();
            return false;
        }, 'back to program view');
        $(window).onWrap('resize', function () { return refreshTable(); });
        $('#galleryTable').onWrap('post-body.bs.table', function () {
            refreshTable();
        });
    }
    function refreshTable() {
        $('#galleryTable').bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight()
        });
        $('#galleryList .fixed-table-container.fixed-height').addClass('has-card-view');
        configureTagsInput();
    }
    function initGalleryListEvents() {
        $('#galleryTable').onWrap('search.bs.table', function () {
            refreshTable();
        }, 'Load program from gallery double clicked');
        $('#galleryTable').onWrap('click-row.bs.table', function ($element, row) {
            PROGRAM_C.loadFromGallery(row);
        }, 'Load program from gallery double clicked');
        $('#galleryList')
            .find('button[name="refresh"]')
            .onWrap('click', function () {
            loadTableData();
            return false;
        }, 'refresh gallery list clicked');
        $('#filterRobot').onWrap('change', loadTableData, 'gallery filter changed');
        $('#fieldOrderBy').onWrap('change', function (e) {
            var fieldData = e.targcurrentColorIndexet.value.split(':');
            var row = parseInt(fieldData[0]);
            $('#galleryTable').bootstrapTable('sortBy', {
                field: row,
                sortOrder: fieldData[1]
            });
            refreshTable();
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
        }
        $('#galleryTable').bootstrapTable('hideLoading');
        refreshTable();
    }
    function updateLike(value, index, row) {
        var myIndex = allRows.indexOf(row);
        row[6] += value;
        row[8] = value > 0;
        $('#galleryTable').bootstrapTable('updateRow', {
            index: myIndex,
            row: row
        });
        refreshTable();
    }
    function showView() {
        initGalleryListEvents();
        $('#filterRobot').val(GUISTATE_C.getRobotGroup());
        loadTableData();
    }
    exports.eventsLike = {
        'click .like': function (e, value, row, index) {
            e.stopPropagation();
            if ($(e.target).data('blocked') == 1)
                return;
            $(e.target).data('blocked', 1);
            PROGRAM.likeProgram(true, row[1], row[3], row[0], function (result) {
                if (result.rc == 'ok') {
                    updateLike(1, index, row);
                    $(e.target).data('blocked', 0);
                }
                else {
                    $(e.target).data('blocked', 0);
                }
                MSG.displayInformation(result, result.message, result.message, row[1]);
            });
            return false;
        },
        'click .dislike': function (e, value, row, index) {
            e.stopPropagation();
            if ($(e.target).data('blocked') == 1)
                return;
            $(e.target).data('blocked', 1);
            PROGRAM.likeProgram(false, row[1], row[3], row[0], function (result) {
                if (result.rc == 'ok') {
                    updateLike(-1, index, row);
                    $(e.target).data('blocked', 0);
                }
                else {
                    $(e.target).data('blocked', 0);
                }
                MSG.displayInformation(result, result.message, result.message, row[1]);
            });
            return false;
        }
    };
    // TODO extend this, if more customization features are available, eg. robot graphics, uploaded images.
    var rowAttributes = function (row, index) {
        var hash = UTIL.getHashFrom(row[0] + row[1] + row[3]);
        currentColorIndex = hash % BACKGROUND_COLORS.length;
        return {
            style: 'background-color :' + BACKGROUND_COLORS[currentColorIndex] + ';' + 'cursor: pointer;  z-index: 1;'
        };
    };
    exports.rowAttributes = rowAttributes;
    exports.titleNumberOfViews = '<span class="galleryIcon typcn typcn-eye-outline" />';
    exports.titleLikes = '<span class="galleryIcon typcn typcn-heart-full-outline" />';
    var formatLike = function (value, row, index) {
        if (GUISTATE_C.isUserLoggedIn()) {
            if (value) {
                return ('<div class="galleryLike"><button href="#" class="dislike galleryLike btn"><span lkey="Blockly.Msg.GALLERY_DISLIKE">' +
                    (Blockly.Msg.GALLERY_DISLIKE || 'GALLERY_DISLIKE') +
                    '</span></button></div>');
            }
            else {
                return ('<div class="galleryLike"><button href="#" class="like galleryLike btn"><span lkey="Blockly.Msg.GALLERY_LIKE">' +
                    (Blockly.Msg.GALLERY_LIKE || 'GALLERY_LIKE') +
                    '</span></button></div>');
            }
        }
        else {
            return '<div style="display:none;" />'; // like is only for logged-in users allowed
        }
    };
    exports.formatLike = formatLike;
    function configureTagsInput() {
        //@ts-ignore
        $('.infoTags').tagsinput();
        $('#galleryTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#galleryList').find('.galleryTags>input').attr('readonly', 'true');
        $('#galleryList').find('span[data-role=remove]').addClass('hidden');
    }
});
