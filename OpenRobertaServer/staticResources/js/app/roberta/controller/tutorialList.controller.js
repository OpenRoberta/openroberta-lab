/**
 * @fileOverview Provides the view/tab for all tutorials available. Tutorials
 *               are shown in a bootstrap table with card view. From this view
 *               one can select (double click) a tutorial to load it into the
 *               program (blockly) view to work with it.
 * 
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
define([ 'require', 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'progTutorial.controller', 'galleryList.controller', 'blockly',
        'jquery', 'bootstrap-table', 'bootstrap-tagsinput' ], function(require, exports, LOG, UTIL, COMM, MSG, GUISTATE_C, TUTORIAL_C, GALLERYLIST_C, Blockly,
        $) {

    var BACKGROUND_COLORS = [ '#33B8CA', '#EBC300', '#39378B', '#005A94', '#179C7D', '#F29400', '#E2001A', '#EB6A0A', '#8FA402', '#BACC1E', '#9085BA',
            '#FF69B4', '#DF01D7' ];
    var currentColorIndex;
    var tutorialList;
    /**
     * Initialize table of tutorials
     */
    function init() {
        tutorialList = GUISTATE_C.getListOfTutorials();
        for ( var tutorial in tutorialList) {
            if (tutorialList.hasOwnProperty(tutorial)) {
                $("#head-navigation-tutorial-dropdown").append("<li class='" + tutorialList[tutorial].language + " " + tutorialList[tutorial].robot
                        + "'><a href='#' id='" + tutorial + "' class='menu tutorial typcn typcn-mortar-board'>" + tutorialList[tutorial].name + "</a></li>");
            }
        }
        initTutorialList();
        initTutorialListEvents();
    }
    exports.init = init;

    function initTutorialList() {

        $('#tutorialTable').bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            toolbar : '#tutorialListToolbar',
            showRefresh : 'true',
            cardView : 'true',
            rowStyle : GALLERYLIST_C.rowStyle,
            rowAttributes : rowAttributes,
            sortName : 'index',
            sortOrder : 'asc',
            search : true,
            buttonsAlign : 'right',
            resizable : 'true',
            iconsPrefix : 'typcn',
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                field : 'robot',
                sortable : true,
                formatter : formatRobot,
            }, {
                field : 'name',
                sortable : true,
                formatter : formatName,
            }, {
                field : 'overview.description',
                sortable : true,
                formatter : formatTutorialOverview,
            }, {
                field : 'overview.goal',
                sortable : true,
                formatter : formatTutorialOverview,
            }, {
                field : 'overview.previous',
                sortable : true,
                formatter : formatTutorialOverview,
            }, {
                field : 'time',
                title : titleTime,
                sortable : true,
            }, {
                field : 'age',
                title : titleAge,
                sortable : true,
            }, {
                field : 'sim',
                title : titleSim,
                sortable : true,
                formatter : formatSim,
            }, {
                field : 'level',
                title : titleLevel,
                sortable : true,
                formatter : formatLevel,
            }, {
                field : 'tags',
                sortable : true,
                formatter : formatTags,
            }, {
                field : 'index',
                visible : false,
            }, {
                field : 'group',
                visible : false,
            } ]
        });
        $('#tutorialTable').bootstrapTable('togglePagination');
    }

    function initTutorialListEvents() {

        $(window).resize(function() {
            $('#tutorialTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });

        $('#tabTutorialList').on('show.bs.tab', function(e) {
            guiStateController.setView('tabTutorialList');
            updateTutorialList();
        });

        $('#tutorialTable').on('all.bs.table', function(e) {
            configureTagsInput();
        });

        $('#tutorialList').find('button[name="refresh"]').onWrap('click', function() {
            updateTutorialList();
            return false;
        }, "refresh tutorial list clicked");

        $('#tutorialTable').onWrap('click-row.bs.table', function($element, row) {
            $element.stopPropagation();
            $element.preventDefault();
            TUTORIAL_C.loadFromTutorial(row.id);
        }, "Load program from tutorial double clicked");

        $('#backTutorialList').onWrap('click', function() {
            $('#tabProgram').trigger('click');
            return false;
        }, "back to program view");

        $('#tutorialTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#tutorialTable').bootstrapTable('resetWidth');
        });

        function updateTutorialList() {
            var tutorialArray = [];
            for ( var tutorial in tutorialList) {
                if (tutorialList.hasOwnProperty(tutorial) && tutorialList[tutorial].language === GUISTATE_C.getLanguage().toUpperCase()) {
                    tutorialList[tutorial].id = tutorial;
                    tutorialArray.push(tutorialList[tutorial]);
                }
            }
            $('#tutorialTable').bootstrapTable("load", tutorialArray);
        }
    }
    var rowAttributes = function(row, index) {
        var hash = UTIL.getHashFrom(row.robot + row.name + row.index);
        currentColorIndex = hash % BACKGROUND_COLORS.length;
        return {
            style : 'background-color :' + BACKGROUND_COLORS[currentColorIndex] + ';' + //
            'padding: 24px 24px 6px 24px; border: solid 12px white; z-index: 1; cursor: pointer;'
        }
    }
    var titleTime = '<span class="tutorialIcon typcn typcn-stopwatch" />';

    var titleSim = '<span class="tutorialIcon typcn typcn-simulation" />';

    var titleAge = '<span class="tutorialIcon typcn typcn-group" />';

    var titleLevel = '<span class="tutorialIcon typcn typcn-mortar-board"/>';

    var formatRobot = function(robot, row, index) {
        return '<div class="typcn typcn-' + GUISTATE_C.findGroup(robot) + '"></div>';
    }

    var formatName = function(value, row, index) {
        return '<div class="galleryProgramname">' + value + '</div>';
    }

    var formatTutorialOverview = function(overview, row, index) {
        switch (overview) {
        case row.overview.description:
            return '<div class="tutorialOverview color' + currentColorIndex + '">' + overview + '</div>';
        case row.overview.goal:
            return '<div class="tutorialOverview color' + currentColorIndex + '"><b>Lernziel: </b>' + overview + '</div>';
        case row.overview.previous:
            return '<div class="tutorialOverview color' + currentColorIndex + '"><b>Vorkenntnisse: </b>' + overview + '</div>';
        default:
            return '';
        }
    }

    var formatTags = function(tags, row, index) {
        if (!tags) {
            tags = "&nbsp;";
        }
        return '<input class="infoTags" type="text" value="' + tags + '" data-role="tagsinput"/>';
    }
    exports.formatTags = formatTags;

    var formatSim = function(sim, row, index) {
        if (sim && (sim === "sim" || sim === 1)) {
            return 'ja<span style="display:none;">simulation</span>';
        } else {
            return 'nein<span style="display:none;">real</span>';
        }
    }

    var formatLevel = function(level, row, index) {
        var html = "";
        if (level) {
            var maxLevel = isNaN(level) ? level.split("/")[1] : 3;
            var thisLevel = isNaN(level) ? level.split("/")[0] : level;
            for (var i = 1; i <= maxLevel; i++) {
                if (i <= thisLevel) {
                    html = '<span style="left: 0;" class="tutorialLevel typcn typcn-star-full-outline"/>' + html;
                } else {
                    html = '<span class="tutorialLevel typcn typcn-star-outline"/>' + html;
                }
            }
            html = '<span class="tutorialLevelStars" style="left:' + (maxLevel * 16 + 20) + ';">' + html;
            html += '</span>'
        }
        return html;
    }

    function configureTagsInput() {
        $('.infoTags').tagsinput();
        $('#tutorialTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#tutorialList').find('.tutorialTags>input').attr('readonly', 'true');
        $('#tutorialList').find('span[data-role=remove]').addClass('hidden');
    }
    exports.configureTagsInput = configureTagsInput;
});
