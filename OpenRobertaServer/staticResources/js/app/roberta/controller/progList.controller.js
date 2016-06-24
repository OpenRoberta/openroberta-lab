define([ 'require', 'exports', 'log', 'util', 'comm', 'progList.model', 'rest.program', 'roberta.program', 'roberta.program.sharing', 'blocks-msg', 'jquery',
        'bootstrap-table' ], function(require, exports, LOG, UTIL, COMM, PROGLIST_MODEL, PROGRAM, ROBERTA_PROGRAM, ROBERTA_PROGRAM_SHARING, Blockly, $) {

    /**
     * Initialize table of programs
     */
    function init() {

        initProgList();
        initProgListEvents();
    }
    exports.init = init;

    function initProgList() {

        $('#programNameTable').bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            pageList : '[ 10, 25, All ]',
            toolbar : '#toolbar',
            showRefresh : 'true',
            showPaginationSwitch : 'true',
            pagination : 'true',
            buttonsAlign : 'right',
            resizable : 'true',
            iconsPrefix : 'typcn',
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                title : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>Name des Programms</span>",
                sortable : true,
                field : '0',
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
                sortable : true,
                field : '1',
            }, {
                title : "<span class='typcn typcn-flow-merge'></span>",
                field : '2',
                sortable : true,
                sorter : sortRelations,
                formatter : formatRelations,
                align : 'left',
                valign : 'middle',
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
                sortable : true,
                field : '3',
                formatter : UTIL.formatDate
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
                sortable : true,
                field : '4',
                formatter : UTIL.formatDate
            }, {
                field : '5',
                checkbox : true,
                valign : 'middle',
            },
//                          TODO implement description in program table
//                            {
//                                title:'description',
//                                field : '6',
//                            },
            {
                field : '7',
                events : eventsDeleteShareLoad,
                title : '<a href="#" id="deleteSome" class="disabled" title="Delete selected programs">' + '<span class="typcn typcn-delete"></span></a>',
                align : 'left',
                valign : 'top',
                formatter : formatDeleteShareLoad,
                width : '89px',
            }, ]
        });
        $('#programNameTable').bootstrapTable('togglePagination');
    }

    function initProgListEvents() {

        $(window).resize(function() {
            $('#programNameTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });

        $('.bootstrap-table').find('button[name="refresh"]').onWrap('click', function() {
            if ($('#tabProgList').data('type') === 'userProgram') {
                PROGLIST_MODEL.loadProgList(update);
            } else {
                PROGLIST_MODEL.loadExampleList(update);
            }
            return false;
        }, "refresh program list clicked");

        $('#programNameTable').onWrap('dbl-click-row.bs.table', function($element, row) {
            ROBERTA_PROGRAM.loadFromListing(row);
            //$('#blocklyDiv').trigger('load', [ row ]);
        }, "Load program from listing double clicked");

        $('#programNameTable').onWrap('check-all.bs.table', function($element, rows) {
            $('#deleteSome').removeClass('disabled');
            $('#shareSome').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.share').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check all programs');

        $('#programNameTable').onWrap('check.bs.table', function($element, row) {
            $('#deleteSome').removeClass('disabled');
            $('#shareSome').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.share').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check one program');

        $('#programNameTable').onWrap('uncheck-all.bs.table', function($element, rows) {
            $('#deleteSome').addClass('disabled');
            $('#shareSome').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.share').removeClass('disabled');
            $('.load').removeClass('disabled');
        }, 'uncheck all programs');

        $('#programNameTable').onWrap('uncheck.bs.table', function($element, row) {
            var selectedRows = $('#programNameTable').bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('#deleteSome').addClass('disabled');
                $('#shareSome').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.share').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        }, 'uncheck one program');

        $('#backProgList').onWrap('click', function() {
            $('#tabProgram').trigger('click');
            return false;
        }, "back to program");

        $('#deleteSome').onWrap('click', function() {
            var programs = $('#programNameTable').bootstrapTable('getSelections', {});
            var names = '';
            for (var i = 0; i < programs.length; i++) {
                names += programs[i][0];
                names += '<br>';
            }
            $('#confirmDeleteProgramName').html(names);
            $('#confirmDeleteProgram').one('hide.bs.modal', function(event) {
                PROGLIST_MODEL.loadProgList(update);
            });
            $("#confirmDeleteProgram").data('programs', programs);
            $("#confirmDeleteProgram").modal("show");
            return false;
        }, "delete programs");

        $('#programNameTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#programNameTable').bootstrapTable('resetWidth');
        });

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                $('#programNameTable').bootstrapTable({});
                $('#programNameTable').bootstrapTable("load", result.programNames);
                if ($('#tabProgList').data('type') === 'userProgram') {
                    $('#programNameTable').bootstrapTable("showColumn", '2');
                    $('#programNameTable').bootstrapTable("showColumn", '3');
                    $('#programNameTable').bootstrapTable("showColumn", '5');
                    $('#deleteSome').show();
                } else {
                    $('#programNameTable').bootstrapTable("hideColumn", '2');
                    $('#programNameTable').bootstrapTable("hideColumn", '3');
                    $('#programNameTable').bootstrapTable("hideColumn", '5');
                    $('#deleteSome').hide();
                }
            }
        }
    }

    var eventsDeleteShareLoad = {
        'click .delete' : function(e, value, row, index) {
            //var deleted = false;
            var selectedRows = [ row ];
            var names = '';
            for (var i = 0; i < selectedRows.length; i++) {
                names += selectedRows[i][0];
                names += '<br>';
            }
            $('#confirmDeleteProgramName').html(names);
            $("#confirmDeleteProgram").data('programs', selectedRows);
            $('#confirmDeleteProgram').one('hidden.bs.modal', function(event) {
//                if (deleted) {
//                    $('#programNameTable').bootstrapTable('uncheckAll', {});
//                    $('.selected').removeClass('selected');
//                }
            });
            $("#confirmDeleteProgram").modal("show");
            return false;
        },
        'click .share' : function(e, value, row, index) {
            if (!row[2].sharedFrom) {
                $('#show-relations').trigger('updateAndShow', [ row ]);
            }
            return false;
        },
        'click .load' : function(e, value, row, index) {
            ROBERTA_PROGRAM.loadFromListing(row);
        }
    };

    var formatRelations = function(value, row, index) {
        if ($.isEmptyObject(value)) {
            return '<span class="typcn typcn-minus"></span>';
        }
        if (value.sharedFrom === 'READ') {
            return '<span class="typcn typcn-eye"></span>';
        }
        if (value.sharedFrom === 'WRITE') {
            return '<span class="typcn typcn-pencil"></span>';
        }
        if (value.sharedWith && Object.keys(value.sharedWith).length == 1) {
            var result = '';
            $.each(value.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    result += '<span>';
                    if (right === 'READ') {
                        result += '<span class="typcn typcn-eye"></span>';
                    } else {
                        result += '<span title="WRITE" class="typcn typcn-pencil"></span>';
                    }
                    result += '&nbsp;';
                    result += user;
                    result += '</span>';
                });
            });
            return result;
        }
        if (value.sharedWith && Object.keys(value.sharedWith).length > 1) {
            var result = '';
            $.each(value.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    if (i == 0) {
                        result += '<div style="white-space:nowrap;"><span style="float:left;">'
                        if (right === 'READ') {
                            result += '<span title="READ" class="typcn typcn-eye"></span>';
                        } else {
                            result += '<span title="WRITE" class="typcn typcn-pencil"></span>';
                        }
                        result += '&nbsp;';
                        result += user;
                        result += '</span><a class="collapsed showRelations" href="#" style="float:right;"'
                                + 'href="#" data-toggle="collapse" data-target=".relation' + index + '"></a></div>';
                    } else {
                        result += '<div style="clear:both;" class="collapse relation' + index + '">';
                        if (right == 'READ') {
                            result += '<span title="READ" class="typcn typcn-eye"></span>';
                        } else {
                            result += '<span title="WRITE" class="typcn typcn-pencil"></span>';
                        }
                        result += '&nbsp';
                        result += user;
                        result += '</div>';
                    }
                });
            });
            return result;
        }
    }

    var formatDeleteShareLoad = function(value, row, index) {
        var result = '';
        if ($('#tabProgList').data('type') === 'userProgram') {
            result += '<a href="#" class="delete" title="Delete program"><span class="typcn typcn-delete"></span></a>';
            if (row[2].sharedFrom) {
                result += '<a href="#" class="share disabled" title="Share program"><span class="typcn typcn-flow-merge"></span></a>';
            } else {
                result += '<a href="#" class="share" title="Share program"><span class="typcn typcn-flow-merge"></span></a>';
            }
        }
        result += '<a href="#" class="load "  title="Load program"><span class="typcn typcn-document"></span></a>';
        return result;
    }

    var sortRelations = function(a, b) {
        if ($.isEmptyObject(a) && $.isEmptyObject(b)) {
            return 0;
        }
        if (a.sharedFrom && b.sharedFrom) {
            if (a.sharedFrom === 'WRITE' && b.sharedFrom === 'WRITE')
                return 0;
            if (a.sharedFrom === 'WRITE')
                return 1;
            else
                return -1;
        }
        if (a.sharedWith && b.sharedWith) {
            var value = {};
            $.each(a.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    value.a = right;
                    return false;
                });
                return false;
            });
            $.each(b.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    value.b = right;
                    return false;
                });
                return false;
            });
            if (value.a === value.b)
                return 0;
            if (value.a === 'WRITE')
                return 1;
            else
                return -1;
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
    }
});
