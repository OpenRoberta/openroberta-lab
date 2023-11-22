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
define(["require", "exports", "util", "message", "guiState.controller", "progList.model", "program.model", "program.controller", "blockly", "table", "jquery", "bootstrap-table", "bootstrap-tagsinput"], function (require, exports, UTIL, MSG, GUISTATE_C, PROGLIST, PROGRAM, PROGRAM_C, Blockly, table_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.titleLikes = exports.titleNumberOfViews = exports.rowAttributes = exports.init = exports.switchLanguage = void 0;
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
    exports.init = init;
    function switchLanguage() {
        $('#galleryTable').bootstrapTable('destroy');
        init();
        if (GUISTATE_C.getView() === 'tabGalleryList') {
            showView();
        }
    }
    exports.switchLanguage = switchLanguage;
    function initGalleryToolbar() {
        var $selectRobot = $('<select id="filterRobot" class="filter form-select"></select>');
        $('#galleryList .search').prepend($selectRobot);
        var groups = getRobotGroups();
        var $filterField = $('#filterRobot');
        for (var group in groups) {
            $filterField.append(new Option(groups[group], group));
        }
        $filterField.append(new Option('All robots', 'all', true, true));
        var $selectOrderBy = $('<label>Order by:</label> <select class="filter form-select" id="fieldOrderBy">' +
            '<option selected="" value="4:desc">Newest</option>' +
            '<option value="4:asc">Oldest</option>' +
            '<option value="1:asc">Program name</option>' +
            '<option value="0:asc">Robot</option>' +
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
                    formatter: table_1.CardView.robot,
                },
                {
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.name,
                },
                {
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.programDescription,
                },
                {
                    title: '',
                    sortable: true,
                    formatter: function (goal) {
                        return table_1.CardView.titleLabel(goal, 'GALLERY_BY', 'cardViewInfo');
                    },
                },
                {
                    sortable: true,
                    title: '',
                    formatter: function (date) {
                        return table_1.CardView.titleLabel(UTIL.formatDate(date.replace(/\s/, 'T')), 'GALLERY_DATE', 'cardViewInfo');
                    },
                },
                {
                    title: '',
                    formatter: function (num) {
                        return table_1.CardView.titleTypcn(num, 'eye-outline');
                    },
                    sortable: true,
                },
                {
                    title: '',
                    formatter: function (num) {
                        return table_1.CardView.titleTypcn(num, 'heart-full-outline');
                    },
                    sortable: true,
                },
                {
                    title: '',
                    sortable: true,
                    formatter: function (value, row) {
                        return table_1.CardView.programTags(row[2]);
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
        var options = __assign(__assign(__assign({}, table_1.CommonTable.options), table_1.CardView.options), myOptions);
        $('#galleryTable').bootstrapTable(options);
        $('#galleryTable').bootstrapTable('togglePagination');
    }
    function initGalleryListEvents() {
        $('#tabGalleryList').onWrap('shown.bs.tab', function () {
            guiStateController.setView('tabGalleryList');
            showView();
            return false;
        }, 'gallery clicked');
        $(window).resize(function () {
            $('#galleryTable').bootstrapTable('resetView', {
                height: UTIL.calcDataTableHeight(),
            });
        });
        $('#tabGalleryList').onWrap('shown.bs.tab', function (e) {
            $(window).trigger('resize');
        }, 'shown gallery list');
        $('#galleryTable').onWrap('post-body.bs.table', function (e) {
            configureTagsInput();
        }, 'page-change gallery list');
        $('#galleryList')
            .find('button[name="refresh"]')
            .onWrap('click', function () {
            loadTableData();
            return false;
        }, 'refresh gallery list clicked');
        $('#galleryTable').onWrap('click-row.bs.table', function ($element, row) {
            PROGRAM_C.loadFromGallery(row);
        }, 'Load program from gallery double clicked');
        $('#backGalleryList').onWrap('click', function () {
            $('#tabProgram').clickWrap();
            return false;
        }, 'back to program view');
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
        var myIndex = allRows.indexOf(row);
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
            if (group === 'arduino')
                return 'Nepo4Arduino';
            if (group === 'ev3')
                return 'Ev3';
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
    exports.rowAttributes = rowAttributes;
    var titleNumberOfViews = '<span class="galleryIcon typcn typcn-eye-outline" />';
    exports.titleNumberOfViews = titleNumberOfViews;
    var titleLikes = '<span class="galleryIcon typcn typcn-heart-full-outline" />';
    exports.titleLikes = titleLikes;
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
            return '<div style="display:none;" />'; // like is only for logged in users allowed
        }
    };
    function configureTagsInput() {
        $('.infoTags').tagsinput();
        $('#galleryTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#galleryList').find('.galleryTags>input').attr('readonly', 'true');
        $('#galleryList').find('span[data-role=remove]').addClass('hidden');
    }
});
