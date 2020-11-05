define([ 'require', 'exports', 'log', 'util', 'comm', 'message', 'progList.model', 'userGroup.model', 'program.model', 'configuration.controller', 'program.controller',
        'guiState.controller', 'blockly', 'jquery', 'bootstrap-table' ], function(require, exports, LOG, UTIL, COMM, MSG, PROGLIST, USERGROUP, PROGRAM,
        CONFIGURATION_C, PROGRAM_C, GUISTATE_C, Blockly, $) {

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
            toolbar : '#progListToolbar',
            toolbarAlign: 'none',
            showRefresh : 'true',
            sortName : 4,
            sortOrder : 'desc',
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
                title : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>" + (Blockly.Msg.DATATABLE_PROGRAM_NAME || "Name des Programms") + "</span>",
                sortable : true,
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || "Erzeugt von") + "</span>",
                sortable : true,
            }, {
                events : eventsRelations,
                title : "<span class='typcn typcn-flow-merge'></span>",
                sortable : true,
                sorter : sortRelations,
                formatter : formatRelations,
                align : 'left',
                valign : 'middle',
            }, {
                visible : false
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || "Erzeugt am") + "</span>",
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>" + (Blockly.Msg.DATATABLE_ACTUALIZATION || "Letzte Aktualisierung") + "</span>",
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                title: '<input name="btSelectAll" type="checkbox">',
                formatter: function(value, row, index) {
                    if (GUISTATE_C.isUserMemberOfUserGroup() && row[1] === GUISTATE_C.getUserUserGroupOwner()) {
                        return '<input type="checkbox" name="btSelectItem" data-index="' + index + '" disabled>';
                    }
                    return '<input type="checkbox" name="btSelectItem" data-index="' + index + '">';
                },
                valign : 'middle',
                halign: 'center',
                align: 'center',
            }, {
                events : eventsDeleteShareLoad,
                title : titleActions,
                align : 'left',
                valign : 'top',
                formatter : formatDeleteShareLoad,
                width : '117px',
            }, ]
        });
        $('#programNameTable').bootstrapTable('togglePagination');
    }

    function initProgListEvents() {
        var $tabProgList = $('#tabProgList'),
            $progList = $('#progList'),
            $programNameTable = $('#programNameTable'),
            $userGroupOptGroup = $('#progListUserGroupScope'),
            $userGroupSelect = $userGroupOptGroup.closest('select');
        
        $userGroupSelect.detach();
        $progList.find('button[name="refresh"]').parent().prepend($userGroupSelect);

        $(window).resize(function() {
            $programNameTable.bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });
        $tabProgList.on('show.bs.tab', function(e) {
            guiStateController.setView('tabProgList');
            $programNameTable.bootstrapTable("load", []);
            $userGroupSelect.hide();
            if ($tabProgList.data('type') === 'userProgram') {
                $userGroupOptGroup.closest('select').val('userProgram');
                $userGroupOptGroup.find('option').remove();
                if (!GUISTATE_C.isUserMemberOfUserGroup()) {
                    USERGROUP.loadUserGroupList(function(data) {
                        if (data.rc == 'ok' && data.userGroups.length > 0) {
                            data.userGroups.forEach(function(userGroup) {
                                $userGroupOptGroup.append('<option value="' + userGroup.name + '">' + userGroup.name + '</option>');
                            });
                            $userGroupSelect.show();
                        }
                    });
                }
            }
        });

        $tabProgList.on('shown.bs.tab', function(e) {
            switch ($tabProgList.data('type')) {
                case 'userProgram':
                    PROGLIST.loadProgList(update);
                    break;
                case 'exampleProgram':
                default:
                    PROGLIST.loadExampleList(updateExamplePrograms);
            }
        });

        $progList.find('button[name="refresh"]').onWrap('click', function() {
            switch ($tabProgList.data('type')) {
                case 'userProgram':
                    $userGroupSelect.change();
                    break;
                case 'userGroupMemberProgram':
                case 'exampleProgram':
                default:
                    PROGLIST.loadExampleList(updateExamplePrograms);
            }
            return false;
        }, "refresh program list clicked");
        
        $userGroupSelect.change(function(evt) {
            if ($tabProgList.data('type') !== 'userProgram') {
                return;
            }
            var selectVal = $userGroupSelect.val();
            
            if (selectVal === 'userProgram') {
                PROGLIST.loadProgList(update);
            } else {
                PROGLIST.loadProgListFromUserGroupMembers(selectVal, update);
            }
        });

        $programNameTable.onWrap('click-row.bs.table', function($element, row) {
            loadFromListing(row);
        }, "Load program from listing clicked");

        $programNameTable.onWrap('check-all.bs.table', function($element, rows) {
            $programNameTable.find('.deleteSomeProg').removeClass('disabled');
            $programNameTable.find('#shareSome').removeClass('disabled');
            $programNameTable.find('.delete, .share, .gallery, .load').addClass('disabled');
        }, 'check all programs');

        $programNameTable.onWrap('check.bs.table', function(e, row, $element) {
            $programNameTable.find('.deleteSomeProg').removeClass('disabled');
            $programNameTable.find('#shareSome').removeClass('disabled');
            $programNameTable.find('.delete, .share, .gallery, .load').addClass('disabled');
            
        }, 'check one program');

        $programNameTable.onWrap('uncheck-all.bs.table', function($element, rows) {
            $programNameTable.find('.deleteSomeProg').addClass('disabled');
            $programNameTable.find('#shareSome').addClass('disabled');
            $programNameTable.find('.delete, .share, .gallery, .load').filter(':not([data-status="disabled"])').removeClass('disabled');
        }, 'uncheck all programs');

        $programNameTable.onWrap('uncheck.bs.table', function(e, row, $element) {
            var selectedRows = $programNameTable.bootstrapTable('getSelections');
            if (!selectedRows || selectedRows.length === 0) {
                $programNameTable.find('.deleteSomeProg').addClass('disabled');
                $programNameTable.find('#shareSome').addClass('disabled');
                $programNameTable.find('.delete, .share, .gallery, .load').filter(':not([data-status="disabled"])').removeClass('disabled');
            }
        }, 'uncheck one program');

        $('#backProgList').onWrap('click', function() {
            $('#tabProgram').trigger('click');
            return false;
        }, "back to program view")

        $(document).onWrap('click', '.deleteSomeProg', function() {
            var programs = $programNameTable.bootstrapTable('getSelections', {});
            var names = '<br>';
            for (var i = 0; i < programs.length; i++) {
                names += programs[i][0];
                names += '<br>';
            }
            $('#confirmDeleteProgramName').html(names);
            $('#confirmDeleteProgram').one('hide.bs.modal', function(event) {
                PROGLIST.loadProgList(update);
            });
            $("#confirmDeleteProgram").data('programs', programs);
            $("#confirmDeleteProgram").modal("show");
            return false;
        }, "delete programs");

        $programNameTable.on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $programNameTable.bootstrapTable('resetWidth');
        });

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                $('#programNameTable').bootstrapTable("load", result.programNames);
                $('.deleteSomeProg').show();
              } else {
                if (result.cmd === "loadPN") {
                    $('#backProgList').click();
                }
            }

            $('.deleteSomeProg').attr('data-original-title', Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP || "Click here to delete all selected programs.");
            $('#programNameTable').find('.delete').attr('data-original-title', Blockly.Msg.PROGLIST_DELETE_TOOLTIP || 'Click here to delete your program.');
            $('#programNameTable').find('.share').attr('data-original-title', Blockly.Msg.PROGLIST_SHARE_TOOLTIP
                    || "Click here to share your program with a friend.");
            $('#programNameTable').find('.gallery').attr('data-original-title', Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP
                    || "Click here to upload your program to the gallery hence share it with all other users.");
            $('#programNameTable').find('.load').attr('data-original-title', Blockly.Msg.PROGLIST_LOAD_TOOLTIP
                    || 'Click here to load your program in the programming environment.');
            $('#programNameTable').find('[rel="tooltip"]').tooltip();
        }
    }
    
    function updateExamplePrograms(result) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            $('#programNameTable').bootstrapTable("load", result.programNames);
            $('#programNameTable').bootstrapTable("hideColumn", 2 );
            $('#programNameTable').bootstrapTable("hideColumn", 6 );
            $('#programNameTable').bootstrapTable("refreshOptions", {
                "sortName": 0,"sortOrder": "asc"
            });
            $('.deleteSomeProg').hide();
        } else {
            if (result.cmd === "loadPN") {
                $('#backProgList').click();
            }
        }
        $('#programNameTable').find('.load').attr('data-original-title', Blockly.Msg.PROGLIST_LOAD_TOOLTIP
                || 'Click here to load your program in the programming environment.');
        $('#programNameTable').find('[rel="tooltip"]').tooltip();
    }
    
    var eventsRelations = {
        'click .showRelations' : function(e, value, row, index) {
            e.stopPropagation();
            var collapseName = '.relation' + index;
            $(collapseName).collapse('toggle');
        }
    }
    
    var eventsDeleteShareLoad = {
        'click .delete' : function(e, value, row, index) {
            e.stopPropagation();
            var selectedRows = [ row ];
            var names = '<br>';
            for (var i = 0; i < selectedRows.length; i++) {
                names += selectedRows[i][0];
                names += '<br>';
            }
            $('#confirmDeleteProgramName').html(names);
            $("#confirmDeleteProgram").data('programs', selectedRows);
            $('#confirmDeleteProgram').one('hidden.bs.modal', function(event) {
            });
            $("#confirmDeleteProgram").modal("show");
            return false;
        },
        'click .share' : function(e, value, row, index) {
            e.stopPropagation();
            if (!row[2].sharedFrom) {
                $('#show-relations').trigger('updateAndShow', [ row ]);
            }
            return false;
        },
        'click .gallery' : function(e, value, row, index) {
            e.stopPropagation();
            if (!row[2].sharedFrom && !GUISTATE_C.isUserMemberOfUserGroup()) {
                $('#share-with-gallery').trigger('updateAndShow', [ row ]);
            }
            return false;
        },
        'click .load' : function(e, value, row, index) {
            e.stopPropagation();
            loadFromListing(row);
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
        if (value.sharedFrom === 'X_WRITE') {
            return '<span class="typcn typcn-key"></span>';
        }
        if (value.sharedWith && value.sharedWith.length == 1) {
            var result = '';
            $.each(value.sharedWith, function(i, obj) {
                result += '<span>';
                if (obj.type === 'User' && obj.label === 'Roberta') {
                    // should not happen
                } else if (obj.right === 'READ') {
                    result += '<span class="typcn typcn-eye"></span>';
                } else if (obj.right === 'X_WRITE') {
                    result += '<span class="typcn typcn-key"></span>';
                } else if (obj.right === 'WRITE') {
                    result += '<span class="typcn typcn-pencil"></span>';
                }
                
                if (obj.type === 'User') {
                    result += '&nbsp;<span class="typcn typcn-user"></span>';
                } else if (obj.type === 'UserGroup') {
                    result += '&nbsp;<span class="typcn typcn-group"></span>';
                }
                
                result += '&nbsp;';

                result += '<span class="value">';
                result += obj.label;
                result += '</span>';
                result += '</span>';
            });
            return result;
        }
        if (value.sharedWith && Object.keys(value.sharedWith).length > 1) {
            var result = [];
            $.each(value.sharedWith, function(i, obj) {
                var typeLabel = "";
                
                if (obj.type === 'User') {
                    typeLabel = '&nbsp;<span class="typcn typcn-user"></span>';
                } else if (obj.type === 'UserGroup') {
                    typeLabel = '&nbsp;<span class="typcn typcn-group"></span>';
                }
                
                if (obj.type === 'User' && obj.label === 'Roberta') {
                    //Do nothing
                } else if (obj.right === 'READ') {
                    result.push('<span class="typcn typcn-eye"></span>' + typeLabel + '&nbsp;<span class="value">' + obj.label + '</span>');
                } else if (obj.right === 'WRITE') {
                    result.push('<span class="typcn typcn-pencil"></span>' + typeLabel + '&nbsp;<span class="value">' + obj.label + '</span>');
                }
            });
            var resultString = '<div style="white-space:nowrap;"><span style="float:left;">';
            resultString += result[0];
            resultString += '</span><a class="collapsed showRelations" href="#" style="float:right;"'
                    + 'href="#" data-toggle="collapse" data-target=".relation' + index + '"></a></div>';
            for (var i = 1; i < result.length; i++) {
                resultString += '<div style="clear:both;" class="collapse relation' + index + '">';
                resultString += result[i];
                resultString += '</div>';
            }
            return resultString;
        }
    }

    var formatDeleteShareLoad = function(value, row, index) {
        // TODO check here and on the server, if this user is allowed to share programs
        var result = '';
        if ($('#tabProgList').data('type') === 'userProgram') {
            if (!GUISTATE_C.isUserMemberOfUserGroup() || GUISTATE_C.getUserUserGroupOwner() !== row[1]) {
                result += '<a href="#" class="delete" rel="tooltip" lkey="Blockly.Msg.PROGLIST_DELETE_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-delete"></span></a>';
            } else {
                result += '<a href="#" class="delete disabled" data-status="disabled"><span class="typcn typcn-delete"></span></a>';
            }
            if (row[2].sharedFrom) {
                result += '<a href="#" class="share disabled" data-status="disabled"><span class="typcn typcn-flow-merge"></span></a>';
                if (!GUISTATE_C.isUserMemberOfUserGroup()) {
                    result += '<a href="#" class="gallery disabled" data-status="disabled"><span class="typcn typcn-th-large-outline"></span></a>';
                }
            } else {
                result += '<a href="#" class="share" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-flow-merge"></span></a>';
                if (!GUISTATE_C.isUserMemberOfUserGroup()) {
                    result += '<a href="#" class="gallery" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-th-large-outline"></span></a>';
                }
            }
        }
        result += '<a href="#" class="load" rel="tooltip" lkey="Blockly.Msg.PROGLIST_LOAD_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-document"></span></a>';
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
            var value = {
                    a: a.sharedWith[0].right,
                    b: b.sharedWith[0].right
            };
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
    var titleActions = '<a href="#" id="deleteSomeProg" class="deleteSomeProg disabled" rel="tooltip" lkey="Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP" data-original-title="" data-container="body" title="">'
            + '<span class="typcn typcn-delete"></span></a>';

    /**
     * Load the program and configuration that was selected in program list
     */
    function loadFromListing(program) {
        var right = 'none';
        LOG.info('loadFromList ' + program[0]);
        PROGRAM.loadProgramFromListing(program[0], program[1], program[3], function(result) {
            if (result.rc === 'ok') {
                result.programShared = false;
                var alien = program[1] === GUISTATE_C.getUserAccountName() ? null : program[1];
                if (alien) {
                    result.programShared = 'READ';
                }
                if (program[2].sharedFrom) {
                    var right = program[2].sharedFrom;
                    result.programShared = right;
                }
                result.name = program[0];
                GUISTATE_C.setProgram(result, alien);
                GUISTATE_C.setProgramXML(result.progXML);

                if (result.configName === undefined) {
                    if (result.confXML === undefined) {
                        GUISTATE_C.setConfigurationNameDefault();
                        GUISTATE_C.setConfigurationXML(GUISTATE_C.getConfigurationConf());
                    } else {
                        GUISTATE_C.setConfigurationName('');
                        GUISTATE_C.setConfigurationXML(result.confXML);
                    }
                } else {
                    GUISTATE_C.setConfigurationName(result.configName);
                    GUISTATE_C.setConfigurationXML(result.confXML);
                }
                $('#tabProgram').one('shown.bs.tab', function(e) {
                    CONFIGURATION_C.reloadConf();
                    PROGRAM_C.reloadProgram();
                    // this is a temporary function to  inform users about possible data loss from bug issue #924
                    // TODO review this in 4 weeks and remove it if possible                         
                    checkMissingInformaton(result);
                });
                $('#tabProgram').trigger('click');

            }
            MSG.displayInformation(result, "", result.message);
        });
    }
    
    /**
     * this is a temporary function to  inform users about possible data loss from bug issue #924
     * TODO review this in 4 weeks and remove it if possible      
     */
    function checkMissingInformaton(result) {
        if (GUISTATE_C.getRobotGroup() === "calliope" || GUISTATE_C.getRobotGroup() === "microbit") {
            var begin = new Date("2020-10-28 03:00:00").getTime();
            var end = new Date("2020-11-04 03:00:00").getTime();
            var setWarning = function(block) {
                if (GUISTATE_C.getLanguage().toLowerCase() === "de") {
                    block.setWarningText("Hier sind beim letzten Speichern Information verloren gegangen,\nbitte überprüfe die Parameter und sichere das Programm! :-)");
                } else {
                    block.setWarningText("Information was lost during the last save,\nplease check the parameters and store the program! :-)");
                }
                block.warning.setVisible(true);
            }
            if (result.lastChanged && result.lastChanged > begin && result.lastChanged < end) {
                var blocks = Blockly.getMainWorkspace() && Blockly.getMainWorkspace().getAllBlocks();
                blocks.forEach(function(block) {
                    switch (block.type) {
                        case "mbedActions_play_note":
                        case "robSensors_accelerometer_getSample":
                        case "robSensors_gyro_getSample":
                            setWarning(block);
                            break;
                        case "robSensors_getSample":
                            if (block.sensorType_ && (block.sensorType_ === "ACCELEROMETER_VALUE" || block.sensorType_ === "GYRO_ANGLE")) {
                                setWarning(block);
                            }
                            break;
                        default:
                            break;
                    }
                });
            }
        }
    }
    });
