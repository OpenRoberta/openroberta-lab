/**
 * @fileOverview Provides the view/tab for all programs shared with the gallery.
 *               Programs are shown in a bootstrap table with card view. From
 *               this view one can select (double click) a program to load it
 *               into the program (blockly) view to execute or copy it.
 * 
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
define([ 'require', 'exports', 'log', 'util', 'comm', 'message', 'guiState.controller', 'progList.model', 'program.model', 'program.controller', 'blocks-msg',
        'jquery', 'bootstrap-table', 'bootstrap-tagsinput' ], function(require, exports, LOG, UTIL, COMM, MSG, GUISTATE_C, PROGLIST, PROGRAM, PROGRAM_C,
        Blockly, $) {

    var BACKGROUND_COLORS = [ '#33B8CA', '#EBC300', '#39378B', '#005A94', '#179C7D', '#F29400', '#E2001A', '#EB6A0A', '#8FA402', '#BACC1E', '#9085BA',
            '#FF69B4', '#DF01D7' ];
    /**
     * Initialize table of programs
     */
    function init() {

        initGalleryList();
        initGalleryListEvents();
        LOG.info('init gallery list view');
    }
    exports.init = init;

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
                title : titleAuthor,
                sortable : true,
            }, {
                title : titleDate,
                sortable : true,
                formatter : UTIL.formatDate
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
            guiStateController.setView('tabGalleryList');
            PROGLIST.loadGalleryList(update);
        });

        $('#galleryTable').on('all.bs.table', function(e) {
            configureTagsInput();
        });

        $('#galleryList').find('button[name="refresh"]').onWrap('click', function() {
            PROGLIST.loadGalleryList(update);
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

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                $('#galleryTable').bootstrapTable("load", result.programNames);
                //configureTagsInput();
            }
        }
    }

    var eventsLike = {
        'click .like' : function(e, value, row, index) {
            e.stopPropagation();
            PROGRAM.likeProgram(true, row[1], row[3], row[0], function(result) {
                if (result.rc == "ok") {
                    $('#galleryList').find('button[name="refresh"]').trigger('click');
                }
                MSG.displayInformation(result, result.message, result.message, row[1]);
            });
            return false;
        },
        'click .dislike' : function(e, value, row, index) {
            e.stopPropagation();
            PROGRAM.likeProgram(false, row[1], row[3], row[0], function(result) {
                if (result.rc == "ok") {
                    $('#galleryList').find('button[name="refresh"]').trigger('click');
                }
                MSG.displayInformation(result, result.message, result.message, row[1]);
            });
            return false;
        }
    }

    var rowStyle = function(row, index) {
        return {
            classes : 'col-xl-2 col-lg-3 col-md-4 col-sm-6'
        };
    }
    exports.rowStyle = rowStyle;

    // TODO extend this, if more customization features are available, eg. robot graphics, uploaded images.
    var rowAttributes = function(row, index) {
        var hash = UTIL.getHashFrom(row[0] + row[1] + row[3]);
        var colorIndex = hash % BACKGROUND_COLORS.length;
        return {
            style : 'background-color :' + BACKGROUND_COLORS[colorIndex] + ';' + //
            'padding: 24px; border: solid 12px white; z-index: 1; cursor: pointer;'
        }
    }
    exports.rowAttributes = rowAttributes;

    var titleAuthor = "<span lkey='Blockly.Msg.GALLERY_BY'>" + (Blockly.Msg.GALLERY_BY || "von") + "</span>";
    exports.titleAuthor = titleAuthor;

    var titleNumberOfViews = '<span class="galleryIcon typcn typcn-eye-outline" />';
    exports.titleNumberOfViews = titleNumberOfViews;

    var titleDate = "<span lkey='Blockly.Msg.GALLERY_DATE'>" + (Blockly.Msg.GALLERY_DATE || "erstellt") + "</span>";
    exports.titleDate = titleDate;

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
        return '<div class="galleryDescription">' + description + '</div>';
    }
    exports.formatProgramDescription = formatProgramDescription;

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
