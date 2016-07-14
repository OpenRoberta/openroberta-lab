define([ 'require', 'exports', 'log', 'util', 'comm', 'confList.model', 'roberta.brick-configuration', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(
        require, exports, LOG, UTIL, COMM, CONFLIST_MODEL, ROBERTA_CONF, Blockly, $) {

    /**
     * Initialize table of configurations
     */
    function init() {

        initConfList();
        initConfListEvents();
        LOG.info('init configuration view');
    }
    exports.init = init;

    function initConfList() {

        $('#confNameTable').bootstrapTable(
                {
                    height : UTIL.calcDataTableHeight(),
                    pageList : '[ 10, 25, All ]',
                    toolbar : '#confListToolbar',
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
                    columns : [
                            {
                                title : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>Name der Configuration</span>",
                                sortable : true,
                                field : '0',
                            },
                            {
                                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
                                sortable : true,
                                field : '1',
                            },
                            {
                                title : "<span class='typcn typcn-flow-merge'></span>",
                                field : '2',
                                sortable : true,
                                sorter : sortRelations,
                                formatter : formatRelations,
                                align : 'left',
                                valign : 'middle',
                            },
                            {
                                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
                                sortable : true,
                                field : '3',
                                formatter : UTIL.formatDate
                            },
                            {
                                title : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
                                sortable : true,
                                field : '4',
                                formatter : UTIL.formatDate
                            },
                            {
                                field : '5',
                                checkbox : true,
                                valign : 'middle',
                            },
                            {
                                field : '7',
                                events : eventsDeleteShareLoad,
                                title : '<a href="#" id="deleteSome" class="disabled" title="Delete selected configurations">'
                                        + '<span class="typcn typcn-delete"></span></a>',
                                align : 'left',
                                valign : 'top',
                                formatter : formatDeleteShareLoad,
                                width : '89px',
                            }, ]
                });
        $('#confNameTable').bootstrapTable('togglePagination');
    }

    function initConfListEvents() {

        $(window).resize(function() {
            $('#confNameTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });

        $('#confList>.bootstrap-table').find('button[name="refresh"]').onWrap('click', function() {
            if ($('#tabConfList').data('type') === 'userConf') {
                CONFLIST_MODEL.loadConfList(update);
            } else {
                CONFLIST_MODEL.loadExampleList(update);
            }
            return false;
        }, "refresh configuration list clicked");

        $('#confNameTable').onWrap('dbl-click-row.bs.table', function($element, row) {
            ROBERTA_CONF.loadFromListing(row);
            //$('#blocklyDiv').trigger('load', [ row ]);
        }, "Load configuration from listing double clicked");

        $('#confNameTable').onWrap('check-all.bs.table', function($element, rows) {
            $('#deleteSome').removeClass('disabled');
            $('#shareSome').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.share').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check all configurations');

        $('#confNameTable').onWrap('check.bs.table', function($element, row) {
            $('#deleteSome').removeClass('disabled');
            $('#shareSome').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.share').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check one configuration');

        $('#confNameTable').onWrap('uncheck-all.bs.table', function($element, rows) {
            $('#deleteSome').addClass('disabled');
            $('#shareSome').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.share').removeClass('disabled');
            $('.load').removeClass('disabled');
        }, 'uncheck all configurations');

        $('#confNameTable').onWrap('uncheck.bs.table', function($element, row) {
            var selectedRows = $('#confNameTable').bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('#deleteSome').addClass('disabled');
                $('#shareSome').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.share').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        }, 'uncheck one configuration');

        $('#backConfList').onWrap('click', function() {
            $('#tabConfiguration').trigger('click');
            return false;
        }, "back to configuration");

        $('#deleteSome').onWrap('click', function() {
            var configurations = $('#confNameTable').bootstrapTable('getSelections', {});
            var names = '';
            for (var i = 0; i < configurations.length; i++) {
                names += configurations[i][0];
                names += '<br>';
            }
            $('#confirmDeleteConfName').html(names);
            $('#confirmDeleteConf').one('hide.bs.modal', function(event) {
                CONFLIST_MODEL.loadConfList(update);
            });
            $("#confirmDeleteConf").data('configurations', configurations);
            $("#confirmDeleteConf").modal("show");
            return false;
        }, "delete configurations");

        $('#confNameTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#confNameTable').bootstrapTable('resetWidth');
        });

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                $('#confNameTable').bootstrapTable({});
                $('#confNameTable').bootstrapTable("load", result.confNames);
                if ($('#tabConfList').data('type') === 'userConf') {
                    $('#confNameTable').bootstrapTable("showColumn", '2');
                    $('#confNameTable').bootstrapTable("showColumn", '3');
                    $('#confNameTable').bootstrapTable("showColumn", '5');
                    $('#deleteSome').show();
                } else {
                    $('#confNameTable').bootstrapTable("hideColumn", '2');
                    $('#confNameTable').bootstrapTable("hideColumn", '3');
                    $('#confNameTable').bootstrapTable("hideColumn", '5');
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
            $('#confirmDeleteConfName').html(names);
            $("#confirmDeleteConf").data('configurations', selectedRows);
            $('#confirmDeleteConf').one('hidden.bs.modal', function(event) {
//                if (deleted) {
//                    $('#confNameTable').bootstrapTable('uncheckAll', {});
//                    $('.selected').removeClass('selected');
//                }
            });
            $("#confirmDeleteConf").modal("show");
            return false;
        },
        'click .share' : function(e, value, row, index) {
            if (!row[2].sharedFrom) {
                $('#show-relations').trigger('updateAndShow', [ row ]);
            }
            return false;
        },
        'click .load' : function(e, value, row, index) {
            ROBERTA_CONF.loadFromListing(row);
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
        if ($('#tabConfList').data('type') === 'userConf') {
            result += '<a href="#" class="delete" title="Delete configuration"><span class="typcn typcn-delete"></span></a>';
        }
        result += '<a href="#" class="load" title="Load configuration"><span class="typcn typcn-document"></span></a>';
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
