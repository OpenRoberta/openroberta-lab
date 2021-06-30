/**
 * @fileOverview Provides the view/tab for all programs shared with the gallery.
 *               Programs are shown in a bootstrap table with card view. From
 *               this view one can select (double click) a program to load it
 *               into the program (blockly) view to execute or copy it.
 * 
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
define([ 'require', 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'progList.model', 'program.model', 'program.controller', 'blockly',
        'jquery', 'bootstrap-table', 'bootstrap-tagsinput' ], function(require, exports, LOG, UTIL, COMM, MSG, GUISTATE_C, PROGLIST, PROGRAM, PROGRAM_C,
        Blockly, $) {

    var BACKGROUND_COLORS = [ '#33B8CA', '#EBC300', '#39378B', '#005A94', '#179C7D', '#F29400', '#E2001A', '#EB6A0A', '#8FA402', '#BACC1E', '#9085BA',
            '#FF69B4', '#DF01D7' ];
    var currentColorIndex;
    var currentViewMode = 'gallery';
    /**
     * Initialize table of programs
     */
    function init() {

        initGalleryToolbar();
        initGalleryList();
        initGalleryListEvents();
    }
    exports.init = init;

    //TODO: Robot group names exists in plugin properties
    function getRobotGroups() {
        var robots = GUISTATE_C.getRobots();
        var groups = {};

        var coerceName = function(name, group) {
            if (group === "arduino")
                return "Nepo4Arduino";
            if (group === "ev3")
                return "Ev3";
            return GUISTATE_C.getMenuRobotRealName(name)
        }

        for ( var propt in robots) {
            var group = robots[propt].group;
            var name = robots[propt].name;
            if (group && !groups[group]) {
                groups[group] = coerceName(name, group);
            }
        }
        return groups;
    }

    function initGalleryToolbar() {
        var groups = getRobotGroups();
        var filterField = $('#filterRobot');
        for ( var group in groups) {
            filterField.append(new Option(groups[group], group));
        }
        filterField.append(new Option("All robots", "all", true, true));
    }

    function initGalleryList() {

        $('#galleryTable').bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            toolbar : '#galleryListToolbar',
            showRefresh : 'true',
            cardView : 'true',
            rowStyle : rowStyle,
            rowAttributes : rowAttributes,
            sortName : 4,
            sortOrder : 'desc',
            search : true,
            buttonsAlign : 'right',
            resizable : 'true',
            iconsPrefix : 'typcn',
            pageSize : 12,
            pageList : [ 12, 24, 48, 96 ],
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                sortable : true,
                //visible : false,
                formatter : formatRobot,
            }, {
                sortable : true,
                formatter : formatProgramName,
            }, {
                sortable : true,
                formatter : formatProgramDescription,
            }, {
                formatter : formatAuthor,
                sortable : true,
            }, {
                sortable : true,
                formatter : formatDate,
            }, {
                title : titleNumberOfViews,
                sortable : true,
            }, {
                title : titleLikes,
                sortable : true,
            }, {
                sortable : true,
                formatter : formatTags,
            }, {
                events : eventsLike,
                formatter : formatLike,
            } ]
        });
        $('#galleryTable').bootstrapTable('togglePagination');
    }

    function initGalleryListEvents() {

        $(window).resize(function() {
            $('#galleryTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });

        $('#tabGalleryList').on('show.bs.tab', function(e) {
            $('#filterRobot').val(GUISTATE_C.getRobotGroup());
            guiStateController.setView('tabGalleryList');
            if ($('#galleryTable').bootstrapTable("getData").length === 0) {
                $(".pace").show(); // Show loading icon and hide gallery table 
            }
            loadGalleryData();
        });

        $('#tabGalleryList').on('shown.bs.tab', function(e) {
            $(window).trigger("resize");
        });

        $('#galleryTable').on('all.bs.table', function(e) {
            configureTagsInput();
        });

        $('#galleryList').find('button[name="refresh"]').onWrap('click', function() {
            loadGalleryData();
            return false;
        }, "refresh gallery list clicked");

        $('#galleryTable').onWrap('click-row.bs.table', function($element, row) {
            PROGRAM_C.loadFromGallery(row);
        }, "Load program from gallery double clicked");

        $('#backGalleryList').onWrap('click', function() {
            $('#tabProgram').trigger('click');
            return false;
        }, "back to program view");

        $('#galleryTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#galleryTable').bootstrapTable('resetWidth');
        });

        $('#filterRobot').onWrap('change', loadGalleryData, "gallery filter changed");

        $('#fieldOrderBy').change(function(e) {
            var fieldData = e.target.value.split(':');
            var row = parseInt(fieldData[0]);
            console.log(row);
            $('#galleryTable').bootstrapTable('refreshOptions', {
                sortName : row,
                sortOrder : fieldData[1],
            });
        });
//        TODO reactivate this once the table-view is improved
//        $('#toogleView').click(function (e) {
//            // toggle button icon
//            var iconClassName = '';
//            if (currentViewMode === 'gallery') {
//                currentViewMode = 'list';
//                iconClassName = 'typcn-th-large';
//            } else {
//                currentViewMode = 'gallery';
//                iconClassName = 'typcn-th-list';
//            }
//            $('#toogleView > i').attr('class', 'typcn ' + iconClassName);
//            $('#galleryTable').bootstrapTable('refreshOptions', {});
//        });
    }

    function loadGalleryData() {
        var params = {};
        var group = $('#filterRobot').val();
        if (group !== 'all') {
            params['group'] = group;
        }
        PROGLIST.loadGalleryList(update, params);
    }

    function update(result) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            $('#galleryTable').bootstrapTable("load", result.programNames);
        }
        $(".pace").fadeOut(300); // Hide loading icon and show gallery table
    }

    function updateLike(value, index, row) {
        var likes = row[6] + value;
        $('#galleryTable').bootstrapTable("updateCell", {
            index : index,
            field : 6,
            value : likes
        });
        var like = value > 0 ? true : false;
        $('#galleryTable').bootstrapTable("updateCell", {
            index : index,
            field : 8,
            value : like
        });
    }

    var eventsLike = {
        'click .like' : function(e, value, row, index) {
            e.stopPropagation();
            if ($(e.target).data('blocked') == 1)
                return;
            $(e.target).data('blocked', 1);
            PROGRAM.likeProgram(true, row[1], row[3], row[0], function(result) {
                if (result.rc == "ok") {
                    updateLike(1, index, row);
                    $(e.target).data('blocked', 0);
                } else {
                    $(e.target).data('blocked', 0);
                }
                MSG.displayInformation(result, result.message, result.message, row[1]);
            });
            return false;

        },
        'click .dislike' : function(e, value, row, index) {
            e.stopPropagation();
            if ($(e.target).data('blocked') == 1)
                return;
            $(e.target).data('blocked', 1);
            PROGRAM.likeProgram(false, row[1], row[3], row[0], function(result) {
                if (result.rc == "ok") {
                    updateLike(-1, index, row);
                    $(e.target).data('blocked', 0);
                } else {
                    $(e.target).data('blocked', 0);
                }
                MSG.displayInformation(result, result.message, result.message, row[1]);
            });
            return false;
        }
    }

    var rowStyle = function(row, index) {
        return {
            classes : currentViewMode === 'gallery' ? 'galleryNode col-xl-2 col-lg-3 col-md-4 col-sm-6' : 'listNode',
        };
    }
    exports.rowStyle = rowStyle;

    // TODO extend this, if more customization features are available, eg. robot graphics, uploaded images.
    var rowAttributes = function(row, index) {
        var hash = UTIL.getHashFrom(row[0] + row[1] + row[3]);
        currentColorIndex = hash % BACKGROUND_COLORS.length;
        return {
            style : 'background-color :' + BACKGROUND_COLORS[currentColorIndex] + ';' + 'border: solid 12px white; cursor: pointer;  z-index: 1;',
        }
    }
    exports.rowAttributes = rowAttributes;

    var titleNumberOfViews = '<span class="galleryIcon typcn typcn-eye-outline" />';
    exports.titleNumberOfViews = titleNumberOfViews;

    var titleLikes = '<span class="galleryIcon typcn typcn-heart-full-outline" />';
    exports.titleLikes = titleLikes;

    var formatRobot = function(value, row, index) {
        return '<div class="typcn typcn-' + row[0] + '"></div>';
    }
    exports.formatRobot = formatRobot;

    var formatProgramName = function(value, row, index) {
        return '<div class="galleryProgramname">' + value + '</div>';
    }
    exports.formatProgramName = formatProgramName;

    var formatProgramDescription = function(value, row, index) {
        var xmlDoc = Blockly.Xml.textToDom(value, Blockly.getMainWorkspace());
        var description = xmlDoc.getAttribute("description");
        if (!description) {
            description = "&nbsp;";
        }
        return '<div class="galleryDescription color' + currentColorIndex + '">' + description + '</div>';
    }
    exports.formatProgramDescription = formatProgramDescription;

    var formatAuthor = function(value, row, index) {
        return "<div class='galleryAuthor'><span class='title' lkey='Blockly.Msg.GALLERY_BY'>" + (Blockly.Msg.GALLERY_BY || "von") + "</span>" + value
                + "</span></div>";
    }
    exports.formatAuthor = formatAuthor;

    var formatDate = function(value, row, index) {
        return ("<span class='title' lkey='Blockly.Msg.GALLERY_DATE'>" + (Blockly.Msg.GALLERY_DATE || "erstellt") + "</span>" + UTIL.formatDate(value.replace(/\s/, 'T')));
    }
    exports.formatDate = formatDate;

    var formatTags = function(value, row, index) {
        var xmlDoc = Blockly.Xml.textToDom(row[2], Blockly.getMainWorkspace());
        var tags = xmlDoc.getAttribute("tags");
        if (!tags) {
            tags = "&nbsp;";
        }
        return '<input class="infoTags" type="text" value="' + tags + '" data-role="tagsinput"/>';
    }
    exports.formatTags = formatTags;

    var formatLike = function(value, row, index) {
        if (GUISTATE_C.isUserLoggedIn()) {
            if (value) {
                return '<div class="galleryLike"><a href="#" class="dislike galleryLike typcn typcn-heart-half-outline"><span lkey="Blockly.Msg.GALLERY_DISLIKE">'
                        + (Blockly.Msg.GALLERY_DISLIKE || 'gefällt mir nicht mehr') + '</span></a></div>';
            } else {
                return '<div class="galleryLike"><a href="#" class="like galleryLike typcn typcn-heart-full-outline"><span lkey="Blockly.Msg.GALLERY_LIKE">'
                        + (Blockly.Msg.GALLERY_LIKE || 'gefällt mir') + '</span></a></div>';
            }
        } else {
            return '<div style="display:none;" />'; // like is only for logged in users allowed
        }
    }

    function configureTagsInput() {
        $('.infoTags').tagsinput();
        $('#galleryTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#galleryList').find('.galleryTags>input').attr('readonly', 'true');
        $('#galleryList').find('span[data-role=remove]').addClass('hidden');
    }
    exports.configureTagsInput = configureTagsInput;
});
