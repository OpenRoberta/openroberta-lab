define(["require", "exports", "util", "guiState.controller", "progTutorial.controller", "cardView", "jquery", "bootstrap-table", "bootstrap-tagsinput"], function (require, exports, UTIL, GUISTATE_C, TUTORIAL_C, CardView, $) {
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
                $('#head-navigation-tutorial-dropdown').append("<li class='" +
                    tutorialList[tutorial].language +
                    ' ' +
                    tutorialList[tutorial].robot +
                    "'><a href='#' id='" +
                    tutorial +
                    "' class='menu tutorial typcn typcn-mortar-board'>" +
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
        $('#tutorialTable').bootstrapTable({
            locale: GUISTATE_C.getLanguage(),
            toolbar: '#tutorialListToolbar',
            height: UTIL.calcDataTableHeight(),
            cardView: 'true',
            rowAttributes: rowAttributes,
            search: 'true',
            showRefresh: 'true',
            sortName: 'index',
            sortOrder: 'asc',
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
                    field: 'robot',
                    title: '',
                    sortable: true,
                    formatter: CardView.robot,
                },
                {
                    field: 'name',
                    title: '',
                    sortable: true,
                    formatter: CardView.name,
                },
                {
                    field: 'overview.description',
                    title: '',
                    sortable: true,
                    formatter: CardView.description,
                },
                {
                    field: 'overview.goal',
                    title: '',
                    sortable: true,
                    formatter: function (goal) {
                        return CardView.label(goal, 'TITLE_GOAL', 'cardViewDescription');
                    },
                },
                {
                    field: 'overview.previous',
                    title: '',
                    sortable: true,
                    formatter: function (goal) {
                        return CardView.label(goal, 'TITLE_PREVIOUS', 'cardViewDescription');
                    },
                },
                {
                    field: 'time',
                    title: CardView.titleTypcn('stopwatch'),
                    sortable: true,
                },
                {
                    field: 'age',
                    title: CardView.titleTypcn('group'),
                    sortable: true,
                },
                {
                    field: 'sim',
                    title: CardView.titleTypcn('simulation'),
                    sortable: true,
                    formatter: formatSim,
                },
                {
                    field: 'level',
                    title: CardView.titleTypcn('mortar-board'),
                    sortable: true,
                    formatter: formatLevel,
                },
                {
                    field: 'tags',
                    title: '',
                    sortable: true,
                    formatter: CardView.tags,
                },
                {
                    field: 'index',
                    title: '',
                    visible: false,
                },
                {
                    field: 'group',
                    title: '',
                    visible: false,
                },
            ],
        });
        $('#tutorialTable').bootstrapTable('togglePagination');
    }
    function initTutorialListEvents() {
        $('#tabTutorialList').onWrap('shown.bs.tab', function () {
            guiStateController.setView('tabTutorialList');
            updateTutorialList();
            return false;
        }, 'tutorial clicked');
        $(window).resize(function () {
            $('#tutorialTable').bootstrapTable('resetView', {
                height: UTIL.calcDataTableHeight(),
            });
        });
        $('#tutorialTable').onWrap('post-body.bs.table', function (e) {
            configureTagsInput();
        }, 'page-change tutorial list');
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
            $('#tabProgram').clickWrap();
            return false;
        }, 'back to program view');
        $('#tutorialTable').on('shown.bs.collapse hidden.bs.collapse', function (e) {
            $('#tutorialTable').bootstrapTable('resetWidth');
        });
    }
    function updateTutorialList() {
        var tutorialArray = [];
        for (var tutorial in tutorialList) {
            if (tutorialList.hasOwnProperty(tutorial) && tutorialList[tutorial].language === GUISTATE_C.getLanguage().toUpperCase()) {
                tutorialList[tutorial].id = tutorial;
                tutorialArray.push(tutorialList[tutorial]);
            }
        }
        $('#tutorialTable').bootstrapTable('load', tutorialArray);
        configureTagsInput();
    }
    var rowAttributes = function (row, index) {
        var hash = UTIL.getHashFrom(row.robot + row.name + row.index);
        currentColorIndex = hash % BACKGROUND_COLORS.length;
        return {
            class: 'col-xxl-2 col-lg-3 col-md-4 col-sm-6',
            style: 'background-color :' +
                BACKGROUND_COLORS[currentColorIndex] +
                ';' + //
                '', // 'border: 4px solid #EEEEEE; z-index: 1; cursor: pointer;',
        };
    };
    var emptyTitle = function () {
        return '<span style="display:none"></span>';
    };
    var formatTutorialOverview = function (overview, row, index, field) {
        return CardView.description(overview, row, index, field);
    };
    var formatSim = function (sim, row, index) {
        if (sim && (sim === 'sim' || sim === 1)) {
            return 'ja';
        }
        else {
            return 'nein';
        }
    };
    var formatLevel = function (level, row, index) {
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
        $('.infoTags').tagsinput();
        $('#tutorialTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#tutorialList').find('.tutorialTags>input').attr('readonly', 'true');
        $('#tutorialList').find('span[data-role=remove]').addClass('hidden');
    }
});
