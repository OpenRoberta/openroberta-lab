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
define(["require", "exports", "util.roberta", "guiState.controller", "progTutorial.controller", "table", "jquery", "bootstrap-table", "bootstrap-tagsinput"], function (require, exports, UTIL, GUISTATE_C, TUTORIAL_C, table_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.switchLanguage = exports.init = void 0;
    var BACKGROUND_COLORS = ['#33B8CA', '#EBC300', '#005A94', '#179C7D', '#F29400', '#E2001A', '#EB6A0A', '#8FA402', '#BACC1E', '#9085BA', '#FF69B4', '#DF01D7'];
    var currentColorIndex;
    var tutorialList;
    /**
     * Initialize table of tutorials
     */
    function init() {
        tutorialList = GUISTATE_C.getListOfTutorials();
        for (var tutorial in tutorialList) {
            if (tutorialList.hasOwnProperty(tutorial)) {
                $('#head-navigation-tutorial-dropdown').append('<li class=\'' +
                    tutorialList[tutorial].language +
                    ' ' +
                    tutorialList[tutorial].robot +
                    '\'><a href=\'#\' id=\'' +
                    tutorial +
                    '\' class=\'menu tutorial typcn typcn-mortar-board\'>' +
                    tutorialList[tutorial].name +
                    '</a></li>');
            }
        }
        initTutorialList();
        initTutorialListEvents();
    }
    exports.init = init;
    function switchLanguage() {
        $('#tutorialTable').bootstrapTable('destroy');
        init();
        if (GUISTATE_C.getView() === 'tabTutorialList') {
            updateTutorialList();
        }
    }
    exports.switchLanguage = switchLanguage;
    function initTutorialList() {
        var myLang = GUISTATE_C.getLanguage();
        var myOptions = {
            columns: [
                {
                    field: 'robot',
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.robot
                },
                {
                    field: 'name',
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.name
                },
                {
                    field: 'overview.description',
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.description
                },
                {
                    field: 'overview.goal',
                    title: '',
                    sortable: true,
                    formatter: function (goal) {
                        return table_1.CardView.titleLabel(goal, 'TITLE_GOAL', 'cardViewDescription');
                    }
                },
                {
                    field: 'overview.previous',
                    title: '',
                    sortable: true,
                    formatter: function (goal) {
                        return table_1.CardView.titleLabel(goal, 'TITLE_PREVIOUS', 'cardViewDescription');
                    }
                },
                {
                    field: 'time',
                    title: '',
                    formatter: function (time) {
                        return table_1.CardView.titleTypcn(time, 'stopwatch');
                    },
                    sortable: true
                },
                {
                    field: 'age',
                    title: '',
                    formatter: function (age) {
                        return table_1.CardView.titleTypcn(age, 'group');
                    },
                    sortable: true
                },
                {
                    field: 'sim',
                    title: '',
                    formatter: function (sim) {
                        return table_1.CardView.titleTypcn(formatSim(sim), 'simulation');
                    },
                    sortable: true
                },
                {
                    field: 'level',
                    title: '',
                    formatter: function (level) {
                        return table_1.CardView.titleTypcn(formatLevel(level), 'mortar-board');
                    },
                    sortable: true
                },
                {
                    field: 'tags',
                    title: '',
                    sortable: true,
                    formatter: table_1.CardView.tags
                },
                {
                    field: 'index',
                    title: '',
                    visible: false
                },
                {
                    field: 'group',
                    title: '',
                    visible: false
                }
            ],
            locale: myLang,
            rowAttributes: rowAttributes,
            sortName: 'index',
            sortOrder: 'asc',
            toolbar: '#tutorialListToolbar'
        };
        var options = __assign(__assign(__assign({}, table_1.CommonTable.options), table_1.CardView.options), myOptions);
        $('#tutorialTable').bootstrapTable(options);
    }
    function initTutorialListEvents() {
        $('#tabTutorialList').off();
        $('#tutorialTable').off();
        $('#backTutorialList').off();
        $('#tabTutorialList').onWrap('shown.bs.tab', function () {
            //@ts-ignore
            guiStateController.setView('tabTutorialList');
            updateTutorialList();
            return false;
        }, 'tutorial clicked');
        var resizeTable = function () {
            $('#tutorialTable').bootstrapTable('resetView', {
                height: UTIL.calcDataTableHeight()
            });
            configureTagsInput();
        };
        $(window).off('resize', resizeTable);
        $(window).on('resize', resizeTable);
        $('#tutorialTable').onWrap('post-body.bs.table', resizeTable, 'page-change tutorial list');
        $('#tutorialList')
            .find('button[name="refresh"]')
            .onWrap('click', function () {
            updateTutorialList();
            return false;
        }, 'refresh tutorial list clicked');
        $('#tutorialTable').onWrap('click-row.bs.table', function ($element, row) {
            $element.stopPropagation();
            $element.preventDefault();
            TUTORIAL_C.loadFromTutorial(row.id);
        }, 'Load program from tutorial double clicked');
        $('#backTutorialList').onWrap('click', function () {
            $('#' + GUISTATE_C.getPrevView()).tabWrapShow();
            return false;
        }, 'back to program view');
        $('#tutorialTable').on('shown.bs.collapse hidden.bs.collapse', function (e) {
            $('#tutorialTable').bootstrapTable('resetWidth');
        });
    }
    function updateTutorialList() {
        $('#tutorialTable').bootstrapTable('showLoading');
        var tutorialArray = [];
        for (var tutorial in tutorialList) {
            if (tutorialList.hasOwnProperty(tutorial) && tutorialList[tutorial].language === GUISTATE_C.getLanguage().toUpperCase()) {
                tutorialList[tutorial].id = tutorial;
                tutorialArray.push(tutorialList[tutorial]);
            }
        }
        $('#tutorialTable').bootstrapTable('load', tutorialArray);
        configureTagsInput();
        $('#tutorialTable').bootstrapTable('hideLoading');
        $('#tutorialTable').bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight()
        });
    }
    var rowAttributes = function (row, index) {
        var hash = UTIL.getHashFrom(row.robot + row.name + row.index);
        currentColorIndex = hash % BACKGROUND_COLORS.length;
        return {
            class: 'col-xxl-2 col-lg-3 col-md-4 col-sm-6',
            style: 'background-color :' +
                BACKGROUND_COLORS[currentColorIndex] +
                ';' + //
                '' // 'border: 4px solid #EEEEEE; z-index: 1; cursor: pointer;',
        };
    };
    var emptyTitle = function () {
        return '<span style="display:none"></span>';
    };
    var formatTutorialOverview = function (overview, row, index, field) {
        return table_1.CardView.description(overview);
    };
    var formatSim = function (sim) {
        if (sim && (sim === 'sim' || sim === 1)) {
            return 'ja';
        }
        else {
            return 'nein';
        }
    };
    var formatLevel = function (level) {
        var html = '';
        if (level) {
            var maxLevel = isNaN(level) ? level.split('/')[1] : 3;
            var thisLevel = isNaN(level) ? level.split('/')[0] : level;
            for (var i = 1; i <= maxLevel; i++) {
                if (i <= thisLevel) {
                    html = html + '<span style="left: 0;" class="tutorialLevel typcn typcn-star-full-outline"/>';
                }
                else {
                    html = html + '<span class="tutorialLevel typcn typcn-star-outline"/>';
                }
            }
            html = '<span class="tutorialLevelStars">' + html;
            html += '</span>';
        }
        return html;
    };
    function configureTagsInput() {
        //@ts-ignore
        $('.infoTags').tagsinput();
        $('#tutorialTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#tutorialList').find('.tutorialTags>input').attr('readonly', 'true');
        $('#tutorialList').find('span[data-role=remove]').addClass('hidden');
    }
});
