/**
 * @fileOverview Provides the view/tab for all programs shared with the gallery.
 *               Programs are edited in a bootstrap table with card view. From
 *               this view one can select (double click) a program to load it
 *               into the program (blockly) view to execute or copy it.
 *               
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
define([ 'require', 'exports', 'log', 'util', 'comm', 'guiState.controller', 'progList.model', 'program.model', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(
        require, exports, LOG, UTIL, COMM, GUISTATE_C, PROGLIST, PROGRAM, Blockly, $) {

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
            sortName : '4',
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
                formatter : formatProgramName,
            }, {
                title : titleOwner,
                sortable : true,
            }, {
                sortable : true,
                visible : false,
            }, {
                title : titleNumberOfBlocks,
                sortable : true,
                formatter : formatNumberOfBlocks,
            }, {
                title : titleDate,
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                sortable : true,
                formatter : formatProgramDescription,
            }, {
                sortable : true,
            }, ]
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

        $('.bootstrap-table').find('button[name="refresh"]').onWrap('click', function() {
            PROGLIST.loadGalleryList(update);
            return false;
        }, "refresh gallery list clicked");

        $('#galleryTable').onWrap('dbl-click-row.bs.table', function($element, row) {
            row.splice(2, 2); // robot type and number of blocks are not needed
            var result = {};
            result.name = row[0];
            result.programShared = 'READ';
            result.timestamp = '';
            PROGRAM.loadProgramFromXML(row[0], row[3], function(result) {
                if (result.rc == "ok") {
                    // on server side we only test case insensitive block names, displaying xml can still fail:
                    try {
                        result.programSaved = false;
                        result.programShared = 'READ';
                        result.programTimestamp = '';
                        GUISTATE_C.setProgram(result, row[1], true);
                        GUISTATE_C.setProgramXML(row[3]);
                        $('#tabProgram').trigger('click');
                    } catch (e) {
                        result.data = xml;
                        result.name = GUISTATE_C.getProgramName();
                        showProgram(result);
                        result.rc = "error";
                        MSG.displayInformation(result, "", Blockly.Msg.ORA_PROGRAM_IMPORT_ERROR, result.name);
                    }
                } else {
                    MSG.displayInformation(result, "", result.message, "");
                }
            });

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

            }
        }
    }

    var rowStyle = function(row, index) {
        return {
            classes : 'col-xl-2 col-lg-3 col-md-4 col-sm-6 typcn typcn-' + row[2] // the robot typicon as background image
        }
    }

    // TODO extend this, if more customization features are available, eg. robot graphics, uploaded images.
    var rowAttributes = function(row, index) {
        var hash = UTIL.getHashFrom(row[0] + row[1] + row[2]);
        var colorIndex = hash % BACKGROUND_COLORS.length;
        return {
            style : 'background-color :' + BACKGROUND_COLORS[colorIndex] + ';' + //
            'padding: 24px ;' + //
            'border: solid 12px white;' + //
            'z-index: 1'
        }
    }
    exports.rowAttributes = rowAttributes;

    var titleOwner = "<span lkey='Blockly.Msg.DATATABLE_BY'>" + (Blockly.Msg.DATATABLE_BY || "von") + "</span>";
    exports.titleOwner = titleOwner;

    var titleNumberOfBlocks = "<span lkey='Blockly.Msg.DATATABLE_NUMBER_OF_BLOCKS'>" + (Blockly.Msg.DATATABLE_NUMBER_OF_BLOCKS || "Blöcke") + "</span>";
    exports.titleNumberOfBlocks = titleNumberOfBlocks;

    var titleDate = "<span lkey='Blockly.Msg.DATATABLE_DATE'>" + (Blockly.Msg.DATATABLE_DATE || "Datum") + "</span>";
    exports.titleDate = titleDate;

    var formatProgramName = function(value, row, index) {
        return '<div style="font-weight:bold; font-size:24px; text-align:center; margin-top:100px">' + value + '</div>';

    }
    exports.formatProgramName = formatProgramName;

    var formatProgramDescription = function(value, row, index) {
        var table = document.getElementById("galleryTable").rows.length;
        var xmlDoc = Blockly.Xml.textToDom(value, Blockly.getMainWorkspace());
        var description = xmlDoc.getAttribute("description");
        var firstParagraph;
        if (description) {
            if (description.startsWith("<div>")) {
                firstParagraph = description.substr(0, description.indexOf('</div>') + 6);
            } else {
                firstParagraph = description.substr(0, description.indexOf('<div'))
            }
            return '<div width:100% style="font-weight:normal; font-size:16px; min-height:45px; max-height:45px; overflow:hidden">' + firstParagraph + '</div>';
        }
        return '<div width:100% style="font-weight:normal; font-size:16px; min-height:45px">&nbsp;</div>';
    }
    exports.formatProgramDescription = formatProgramDescription;

    var formatNumberOfBlocks = function(value, row, index) {
        if (value === parseInt(value, 10) && value !== 0) {
            return value;
        } else {
            return UTIL.countBlocks(row[5]);
        }
    }
    exports.formatNumberOfBlocks = formatNumberOfBlocks;
});
