define([ 'exports', 'log', 'message', 'comm', 'util', 'userGroup.model', 'guiState.controller', 'language.controller', 'jquery', 'blocks-msg', 'bootstrap-table', 'bootstrap-tagsinput', 'blocks' ], function(exports, LOG, MSG, COM, UTIL, USERGROUP,
        GUISTATE_C, LANG, $, Blockly) {
    
    //TODO: If user logs out and is in one of this views, change to program view
    var $userGroupTable;
    var $userGroupMemberTable;
    
    function showPanel() {
        $userGroupTable.bootstrapTable('showLoading');
        USERGROUP.loadUserGroupList(function(data) {
            if (data.rc === 'ok') {
                $userGroupTable.bootstrapTable('load', data.userGroups);
            } else {
                $userGroupTable.bootstrapTable('removeAll');
                MSG.displayInformation(data, data.cause, data.cause);
            }
            setTimeout(function() {
                //The fronted can not calculate the height of the table if it tries to directly after the table is filled.
                //So we wait 250ms. If you set it to a shorter amount, it wont work again.
                $userGroupTable.bootstrapTable('resetView', {
                    height: UTIL.calcDataTableHeight()
                });
                $userGroupTable.find('[data-toggle="tooltip"]').tooltip();
                $userGroupTable.bootstrapTable('hideLoading');
            }, 250);
        })
        $('#tabUserGroupList').click();
        if (GUISTATE_C.getView() !== 'tabUserGroupList') {
            GUISTATE_C.setView('tabUserGroupList');
        }
    }
    exports.showPanel = showPanel;
    
    /**
     * Initialize table of tutorials
     */
    function init() {
        $userGroupTable = $('#userGroupTable');
        $userGroupMemberTable = $('#userGroupMemberTable');
        
        initUserGroupListTable();
        initUserGroupEvents();
        
        initUserGroupMemberListTable();
        initUserGroupMemberEvents();
        initAddMembersToGroupEvents();
        
        LOG.info('UserGroup list-view initialized.');
    }
    exports.init = init;
    
    function initUserGroupListTable() {
        var $actionItemsTemplate = $userGroupTable.find('.action-items-template');
    
        $actionItemsTemplate.remove();
        
        $userGroupTable.bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            pageList : '[ 10, 25, All ]',
            toolbar : '#userGroupListToolbar',
            toolbarAlign: 'none',
            showRefresh : true,
            sortName : 'created',
            sortOrder : 'desc',
            showPaginationSwitch : true,
            pagination : 'true',
            buttonsAlign : 'right',
            resizable : true,
            iconsPrefix : 'typcn',
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                title : "<span lkey='Blockly.Msg.DATATABLE_USERGROUP_NAME'>" + (Blockly.Msg.DATATABLE_USERGROUP_NAME || "Name der Gruppe") + "</span>",
                field: 'name',
                sortable : true,
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_MEMBERS'>" + (Blockly.Msg.DATATABLE_MEMBERS || "Mitglieder") + "</span>",
                field: 'members',
                sortable : true,
                sorter : function(a, b) {
                    return (a.length || 0) - (b.length || 0);
                },
                formatter : function(value, row, index) {
                    return value.length || 0;
                }
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_SHARED_PROGRAMS'>" + (Blockly.Msg.DATATABLE_SHARED_PROGRAMS || "Geteilte Programme") + "</span>",
                field: 'programs',
                sortable : false,
                formatter : function(value, row, index) {

                    if (!value || !value.length) {
                        
                        var $returnValue = $('<div><span lkey="Blockly.Msg.SHARE_PROGRAMS_USERGROUP_HINT" data-translation-targets="title" data-toggle="tooltip" data-container="body" data-placement="right" title="">-</span></div>');
                        LANG.translate($returnValue);
                        return $returnValue.html();
                    }
                    
                    var programFormatter = function (program) {
                        var relationIconKey = '';
                        if (program.right === 'READ') {
                            relationIconKey = 'eye';
                        } else if (program.right === 'X_WRITE') {
                            relationIconKey = 'key';
                        } else if (program.right === 'WRITE') {
                            relationIconKey = 'pencil';
                        }
                        
                        var $returnValue = $('<div>' 
                                    + '<span>'
                                        + '<span class="typcn typcn-' + program.robot + '"></span> '
                                        + '<span class="typcn typcn-' + relationIconKey + '"></span> '
                                        + program.name
                                    + '</span>'
                                + '</div>');
                        LANG.translate($returnValue);
                        
                        return $returnValue.html();
                    }
                    
                    if (value.length === 1) {
                        return programFormatter(value[0]);
                    } else {
                        var entries = value.map(function(program) {return programFormatter(program);});
                        
                        var $returnValue = $('<div>'
                                + '<div style="white-space:nowrap;">' 
                                    + '<span style="float:left;">'
                                        + entries.shift()
                                    + '</span>'
                                    + '<a class="collapsed showRelations" href="#" style="float:right;" href="#" data-toggle="collapse" data-target=".relation' + index + '"></a>'
                                + '</div>'
                            + entries.map(function(entry){
                                return '<div style="clear:both;" class="collapse relation' + index + '"> ' + entry + '</div>';
                              }).join('')
                            + '</div>');
                        LANG.translate($returnValue);
                        return $returnValue.html();
                    }
                },
                events: {
                    'click .showRelations' : function(e, value, row, index) {
                        e.stopPropagation();
                        var collapseName = '.relation' + index;
                        $(collapseName).collapse('toggle');
                    }
                }
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || "Erzeugt am") + "</span>",
                field: 'created',
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                checkbox : true,
                valign : 'middle',
            }, {
                title : '<a href="#" id="deleteUserGroups" class="deleteSome disabled" rel="tooltip" lkey="Blockly.Msg.USERGROUP_LIST_DELETE_ALL_TOOLTIP" data-original-title="" data-container="body" title="" data-translation-targets="title data-original-title">'
                    + '<span class="typcn typcn-delete"></span></a>',
                events : {
                    'click .delete' : function(e, value, row, index) {
                        e.stopPropagation();
                        
                        var groupName = typeof row.name === undefined ? null : row.name,
                            groupMembers = typeof row.members === undefined ? [] : row.members,
                            $button = $(this),
                            deleteFunction = function () {
                                USERGROUP.deleteUserGroup(row.name, function (data) {
                                    if (data.rc === 'ok') {
                                        $userGroupTable.bootstrapTable('remove', {field: 'name', values: [row.name]})
                                    } else {
                                        UTIL.showMsgOnTop(data.message);
                                    }
                                });
                            };
                        
                        if (groupName === null || $button.hasClass('disabled')) {
                            return;
                        }
                        
                        if (!groupMembers.length) {
                            deleteFunction();
                            return;
                        } else if (groupMembers.filter(function (groupMember) {return !groupMember.hasDefaultPassword;}).length === 0) {
                            var modalMessageKey = 'USERGROUP_DELETE_WITH_MEMBERS_WARNING',
                                modalMessage = Blockly.Msg[modalMessageKey] || 'Are your sure that you want to delete the usergroup including all members? No member did log in so far.';
                            $('#show-message-confirm').one('shown.bs.modal', function(e) {
                                $('#confirm').off();
                                $('#confirm').on('click', function(e) {
                                    e.preventDefault();
                                    deleteFunction(true);
                                });
                                $('#confirmCancel').off();
                                $('#confirmCancel').on('click', function(e) {
                                    e.preventDefault();
                                    $('.modal').modal('hide');
                                });
                            });
                            MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);
                        } else {
                            UTIL.showMsgOnTop('ORA_GROUP_DELETE_ERROR_GROUP_HAS_MEMBERS');
                        }
                    }
                },
                align : 'left',
                valign : 'top',
                formatter : function(value, row, index) {
                    return $actionItemsTemplate.find('td').html();
                },
                width : '117px',
            }, ]
        });
        
        $('#userGroupList').find('[data-toggle="tooltip"]').tooltip();
        LANG.translate($('#userGroupList'));
    }
    /* This is an internal function and a part of the initialization. Do not export it. */
    

    function initUserGroupEvents() {
        $(window).resize(function() {
            $userGroupTable.bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });
        
        $userGroupTable.closest('#userGroupList').find('button[name="refresh"]').onWrap('click', function(evt) {
            evt.preventDefault();
            showPanel();
        }, 'refreshed usergroup view');
        
        $userGroupTable.onWrap('check-all.bs.table', function($element, rows) {
            $userGroupTable.closest('#userGroupList').find('.deleteSome').removeClass('disabled');
            $userGroupTable.find('.delete').addClass('disabled');
        }, 'check all usergroups');

        $userGroupTable.onWrap('check.bs.table', function($element, row) {
            $userGroupTable.closest('#userGroupList').find('.deleteSome').removeClass('disabled');
            $userGroupTable.find('.delete').addClass('disabled');
        }, 'check one usergroup');

        $userGroupTable.onWrap('uncheck-all.bs.table', function($element, rows) {
            $userGroupTable.closest('#userGroupList').find('.deleteSome').addClass('disabled');
            $userGroupTable.find('.delete').filter(':not([data-status="disabled"])').removeClass('disabled');
        }, 'uncheck all usergroups');

        $userGroupTable.onWrap('uncheck.bs.table', function($element, row) {
            var selectedRows = $userGroupTable.bootstrapTable('getSelections');
            if (!selectedRows || selectedRows.length === 0) {
                $userGroupTable.closest('#userGroupList').find('.deleteSome').addClass('disabled');
                $userGroupTable.find('.delete').filter(':not([data-status="disabled"])').removeClass('disabled');
            }
        }, 'uncheck one usergroup');

        $userGroupTable.closest('#userGroupList').find('.deleteSome').onWrap('click', function() {
            var selectedRows = $userGroupTable.bootstrapTable('getSelections'),
                $deleteAllButton = $userGroupTable.closest('#userGroupList').find('.deleteSome');
            
            if (!selectedRows || selectedRows.length === 0 || $deleteAllButton.hasClass('disabled')) {
                return;
            }

            var groupNames = selectedRows.map(function(row) {
                return row.name;
            });
            if (selectedRows.reduce(function(carry, element) {
                return carry || (element && element.members && element.members.length > 0);
            }, false)) {
                if (selectedRows.reduce(function(carry, element) {
                        return carry || (element && element.members && element.members.filter(function(member) {return !member.hasDefaultPassword;}).length > 0);
                    }, false)) {
                    //Logged in users exist. Delete them first.
                    MSG.displayInformation({}, '', 'ORA_GROUP_DELETE_ERROR_GROUP_HAS_MEMBERS'); 
                } else {
                    var modalMessageKey = 'USERGROUP_DELETE_WITH_MEMBERS_WARNING',
                        modalMessage = Blockly.Msg[modalMessageKey] || 'Are your sure that you want to delete the usergroup including all members? No member did log in so far.';
                    $('#show-message-confirm').one('shown.bs.modal', function(e) {
                        $('#confirm').off();
                        $('#confirm').on('click', function(e) {
                            e.preventDefault();
                            USERGROUP.deleteUserGroups(groupNames, function (data) {
                                if (data.rc === 'ok') {
                                    $userGroupTable.bootstrapTable('remove', {field: 'name', values: groupNames})
                                } else {
                                    UTIL.showMsgOnTop(data.message);
                                }
                            });
                        });
                        $('#confirmCancel').off();
                        $('#confirmCancel').on('click', function(e) {
                            e.preventDefault();
                            $('.modal').modal('hide');
                        });
                    });
                MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);  
                }
            } else {
                //No group has members. => Delete all directly
                USERGROUP.deleteUserGroups(groupNames, function (data) {
                    if (data.rc === 'ok') {
                        $userGroupTable.bootstrapTable('remove', {field: 'name', values: groupNames})
                    } else {
                        UTIL.showMsgOnTop(data.message);
                    }
                });
            }
        }, 'Bulk delete usergroup');

        $('#backUserGroupList').onWrap('click', function() {
            $('#tabProgram').click();
            return false;
        }, "closed usergroup view and went back to program view.");
        
        $userGroupTable.onWrap('click-row.bs.table', function(e, rowData, row) {
            openDetailUserGroupView(rowData);
        }, "show usergroup member view");

        initCreateUserGroupEvents();
    }
    /* This is an internal function and a part of the initialization. Do not export it. */
    
    function initCreateUserGroupEvents() {
        
        $('#showCreateUserGroupPopup').click(function() {
            $('#create-user-group').modal('show');
            return false;
        });

        $('#create-user-group .close-button').click(function() {
            $('#create-user-group').modal('hide');
            return false;
        });
        
        $('#userGroupNameInput').keydown(function() {
            $('#create-user-group label[for="createAccountName"] ~ .hint').hide();
        });
        
        $('#initialMembersInput').keydown(function() {
            $('#create-user-group label[for="initialMembersInput"] ~ .hint').hide();
        });
        
        $('#create-user-group .accept-button').click(function() {
            var groupName = $('#userGroupNameInput').val(),
                initialMembersCount = $('#initialMembersInput').val();
            
            if (initialMembersCount > 99) {
                $hint = $('#create-user-group label[for="initialMembersInput"] ~ .hint');
                $hint.attr('lkey', 'Blockly.Msg.ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED');
                $hint.text(Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED'] || 'There can be no more than 99 members in a group.');
                $hint.show();
                return;
            }
            
            USERGROUP.createUserGroup(groupName, initialMembersCount, function (data) {
                var $hint;
                switch(data.cause) {
                    case 'ORA_GROUP_CREATE_SUCCESS':
                        //TODO: Add group to current list
                        var tableData = $userGroupTable.bootstrapTable("getData");
                        
                        //Clone array, because the original array is directly linked to the bootstrap table.
                        //No need to clone the items in it, though, normal reference copy is enough.
                        tableData = tableData.map(function (item) {return item;});
                        tableData.unshift(data.userGroup);
                        
                        $userGroupTable.bootstrapTable("showLoading");
                        $userGroupTable.bootstrapTable('removeAll');
                        $userGroupTable.bootstrapTable("load", tableData);
                        $userGroupTable.bootstrapTable("hideLoading");
                        
                        $('#create-user-group .hint').hide();
                        $('#create-user-group').modal('hide');
                        
                        $('#userGroupNameInput').val('');
                        $('#initialMembersInput').val(20)
                        break;
                    case 'ORA_GROUP_ERROR_MISSING_RIGHTS_TO_BE_GROUP_OWNER':
                    case 'ORA_GROUP_CREATE_ERROR_GROUP_LIMIT_REACHED':
                    case 'ORA_GROUP_ERROR_NAME_INVALID':
                    case 'ORA_GROUP_CREATE_ERROR_GROUP_ALREADY_EXISTS':
                        $hint = $('#create-user-group label[for="createAccountName"] ~ .hint');
                        $hint.attr('lkey', 'Blockly.Msg.' + data.cause);
                        $hint.text(Blockly.Msg[data.cause] || data.cause);
                        $hint.show();
                        break;
                    default:
                        UTIL.showMsgOnTop(data.message);
                        
                }
            });
            return false;
        });
    }
    /* This is an internal function and a part of the initialization. Do not export it. */
    
    function initUserGroupMemberListTable() {
        var $memberPasswordResetTemplate = $userGroupMemberTable.find('.reset-password-template'),
            $memberActionItemsTemplate = $userGroupMemberTable.find('.action-items-template'),
            $memberActionItemsHeaderTemplate = $userGroupMemberTable.find('.action-items-header-template');
        
        $memberPasswordResetTemplate.remove();
        $memberActionItemsTemplate.remove();
        $memberActionItemsHeaderTemplate.remove();
        
        $userGroupMemberTable.bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            pageList : '[ 10, 25, All ]',
            toolbar : '#userGroupMemberListToolbar',
            toolbarAlign: 'none',
            showRefresh : true,
            sortName : 'account',
            sortOrder : 'asc',
            showPaginationSwitch : true,
            pagination : true,
            buttonsAlign : 'right',
            resizable : true,
            iconsPrefix : 'typcn',
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                title : "<span lkey='Blockly.Msg.MENU_USER_TOOLTIP'>" + (Blockly.Msg.MENU_USER_TOOLTIP || "User") + "</span>",
                field: 'account',
                formatter : function(value, row, index) {
                    return value.substr(value.lastIndexOf('_') + 1);
                },
                sortable : true,
            }, {
                title : "<span lkey='Blockly.Msg.POPUP_PASSWORD'>" + (Blockly.Msg.POPUP_PASSWORD || "Password") + "</span>",
                field: 'password',
                formatter : function(value, row, index) {
                    return row.hasDefaultPassword ? row.account : '************';
                },
                sortable : false,
            }, {
                checkbox : true,
                valign : 'middle',
            }, {
                title : $memberActionItemsHeaderTemplate.find('td').html(),
                events : {
                    'click .delete' : function(e, value, row, index) {
                        e.stopPropagation();

                        var $button = $(this);
                        
                        if ($button.hasClass('disabled') || !row.account) {
                            return;
                        }
                        
                        var deleteFunction = function(members) {
                                if (!members) {
                                    return;
                                }
                                var memberAccountNames = members.map(function(member){
                                    return member.account;
                                });
                                
                                USERGROUP.deleteGroupMembers(memberAccountNames[0].split(':', 2)[0], memberAccountNames, function (data) {
                                    if (data.rc === 'ok') {
                                        $userGroupMemberTable.bootstrapTable('remove', {
                                            field: 'account',
                                            values: memberAccountNames
                                        });
                                    } else {
                                        UTIL.showMsgOnTop(data.message);
                                    }
                                });
                            }
                        
                        /*
                         * 
                        var hasDefaultPassword = members.reduce(function(carry, member) {
                            return carry && member.hasDefaultPassword;
                        }, true);
                         */
                        
                        var modalMessageKey = row.hasDefaultPassword ? 'DELETE_USERGROUP_MEMBER_WARNING' : 'DELETE_USERGROUP_MEMBER_AFTER_LOGIN_WARNING',
                            modalMessage = Blockly.Msg[modalMessageKey] || 'The member you want to delete might have create own programs and did already log in. Are you sure, that you want to delete the member?';
                        $('#show-message-confirm').one('shown.bs.modal', function(e) {
                            $('#confirm').off();
                            $('#confirm').on('click', function(e) {
                                e.preventDefault();
                                deleteFunction([row]);
                            });
                            $('#confirmCancel').off();
                            $('#confirmCancel').on('click', function(e) {
                                e.preventDefault();
                                $('.modal').modal('hide');
                            });
                        });
                        MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);
                    },
                    'click .reset-password' : function(e, value, row, index) {
                        e.stopPropagation();
                        
                        var $button = $(this);
                        if ($button.hasClass('disabled') || !row.account) {
                            return;
                        }
                        
                        USERGROUP.setUserGroupMemberDefaultPassword(row.account.split(':', 2)[0], row.account, function (data) {
                            if (data.rc === 'ok') {
                                row.hasDefaultPassword = true;
                                $userGroupMemberTable.bootstrapTable('updateRow', {
                                    index: index,
                                    row: row,
                                    replace: true
                                });
                                $button.addClass('disabled');
                            } else {
                                UTIL.showMsgOnTop(data.message);
                            }
                        });
                    }
                },
                align : 'left',
                valign : 'top',
                formatter : function(value, row, index) {
                    var $element = $memberActionItemsTemplate.clone(true);
                    if (row.hasDefaultPassword) {
                        var $button = $element.find('.reset-password');
                        $button.addClass('disabled');
                        $button.attr('data-status', 'disabled');
                    }
                    LANG.translate($element);
                    return $element.find('td').html();
                },
                width : '117px',
            }, ]
        });
        $('#userGroupMemberList').find('[data-toggle="tooltip"]').tooltip();
        LANG.translate($('#userGroupMemberList'));
    }
    /* This is an internal function and a part of the initialization. Do not export it. */
    

    function initUserGroupMemberEvents() {
        $(window).resize(function() {
            $userGroupMemberTable.bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });
        
        $userGroupMemberTable.closest('#userGroupMemberList').find('button[name="refresh"]').onWrap('click', function(evt) {
            evt.preventDefault();
            
            var groupName = $('#userGroupMemberListHeader').text();
            
            $userGroupMemberTable.bootstrapTable('showLoading');
            USERGROUP.loadUserGroup(groupName, function(data) {
                if (data.rc === 'ok' && typeof data.userGroup !== 'undefined' && typeof data.userGroup.members !== 'undefined') {
                    $userGroupMemberTable.bootstrapTable('load', data.userGroup.members);
                } else {
                    $userGroupMemberTable.bootstrapTable('removeAll');
                    MSG.displayInformation(data, data.cause, data.cause);
                }
                setTimeout(function() {
                    //The fronted can not calculate the height of the table if it tries to directly after the table is filled.
                    //So we wait 250ms. If you set it to a shorter amount, it wont work again.
                    $userGroupMemberTable.bootstrapTable('resetView', {
                        height: UTIL.calcDataTableHeight()
                    });
                    $userGroupMemberTable.bootstrapTable('hideLoading');
                }, 250);
            });
        }, 'refreshed usergroup member view');
        
        $userGroupMemberTable.onWrap('check-all.bs.table', function(e, rows) {
            $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome').removeClass('disabled');
            
            var atLeastOneHasNotDefaultPassword = rows.reduce(function(noDefaultPasswordFound, row) {
                    return noDefaultPasswordFound || !row.hasDefaultPassword;
                }, false);
            
            if (atLeastOneHasNotDefaultPassword) {
                $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').removeClass('disabled');
            }

            $userGroupMemberTable.find('.delete, .reset-password').addClass('disabled');
        }, 'check all usergroups');

        $userGroupMemberTable.onWrap('check.bs.table', function(e, row, $element) {
            $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome').removeClass('disabled');
            if (!row.hasDefaultPassword) {
                $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').removeClass('disabled');
            }
            $userGroupMemberTable.find('.delete, .reset-password').addClass('disabled');
        }, 'check one usergroup');

        $userGroupMemberTable.onWrap('uncheck-all.bs.table', function(e, rows) {
            $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome, .resetPasswords').addClass('disabled');
            $userGroupMemberTable.find('.delete, .reset-password').filter(':not([data-status="disabled"])').removeClass('disabled');
        }, 'uncheck all usergroups');

        $userGroupMemberTable.onWrap('uncheck.bs.table', function(e, row, $element) {
            var selectedRows = $userGroupMemberTable.bootstrapTable('getSelections');
            if (!selectedRows || selectedRows.length === 0) {
                $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome, .resetPasswords').addClass('disabled');
                $userGroupMemberTable.find('.delete, .reset-password').filter(':not([data-status="disabled"])').removeClass('disabled');
            } else {
                var atLeastOneHasNotDefaultPassword = selectedRows.reduce(function(noDefaultPasswordFound, row) {
                        return noDefaultPasswordFound || !row.hasDefaultPassword;
                    }, false);
                if (!atLeastOneHasNotDefaultPassword) {
                    $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').addClass('disabled');
                }
            }
            
        }, 'uncheck one usergroup');

        
        $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome').onWrap('click', function() {
            var selectedRows = $userGroupMemberTable.bootstrapTable('getSelections'),
                $deleteSomeButton = $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome');
            
            if (!selectedRows || selectedRows.length === 0 || $deleteSomeButton.hasClass('disabled')) {
                return;
            }
            
            var hasDefaultPassword = selectedRows.reduce(function(carry, member) {
                    return carry && member.hasDefaultPassword;
                }, true),
                deleteFunction = function(members) {
                    if (!members) {
                        return;
                    }
                    var memberAccountNames = members.map(function(member){
                        return member.account;
                    });
                    
                    USERGROUP.deleteGroupMembers(memberAccountNames[0].split(':', 2)[0], memberAccountNames, function (data) {
                        if (data.rc === 'ok') {
                            $userGroupMemberTable.bootstrapTable('remove', {
                                field: 'account',
                                values: memberAccountNames
                            });
                        } else {
                            UTIL.showMsgOnTop(data.message);
                        }
                    });
                };
            
            var modalMessageKey = hasDefaultPassword ? 'DELETE_USERGROUP_MEMBER_WARNING' : 'DELETE_USERGROUP_MEMBER_AFTER_LOGIN_WARNING',
                modalMessage = Blockly.Msg[modalMessageKey] || 'The member you want to delete might have create own programs and did already log in. Are you sure, that you want to delete the member?';
            $('#show-message-confirm').one('shown.bs.modal', function(e) {
                $('#confirm').off();
                $('#confirm').on('click', function(e) {
                    e.preventDefault();
                    deleteFunction(selectedRows);
                });
                $('#confirmCancel').off();
                $('#confirmCancel').on('click', function(e) {
                    e.preventDefault();
                    $('.modal').modal('hide');
                });
            });
            MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);
        }, 'Bulk removed members of usergroup.');
        
        $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').onWrap('click', function() {
            var selectedRows = $userGroupMemberTable.bootstrapTable('getSelections'),
                $resetAllButton = $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords');
            
            if (!selectedRows || selectedRows.length === 0 || $resetAllButton.hasClass('disabled')) {
                return;
            }
            
            var userGroupName = selectedRows[0].account.split(':', 2)[0],
                memberAccounts = selectedRows.filter(function(row) {
                    return !row.hasDefaultPassword;
                }).map(function(row) {
                    return row.account;
                });
            
            USERGROUP.setUserGroupMemberDefaultPasswords(userGroupName, memberAccounts, function (data) {
                if (data.rc === 'ok') {
                    $userGroupMemberTable.closest('#userGroupMemberList').find('button[name="refresh"]').click();
                } else {
                    UTIL.showMsgOnTop(data.message);
                }
            });
        }, 'Bulk resetted passwords of usergroup members.');
        
        $('#backUserGroupMemberList').click(function() {
            showPanel();
            return false;
        });
        
    }
    /* This is an internal function and a part of the initialization. Do not export it. */
    
    function initAddMembersToGroupEvents() {
        
        var $addMembersModal = $('#user-group-add-members'),
            $memberCountInput = $addMembersModal.find('#additionalMembersInput'),
            $memberCountInputHint = $addMembersModal.find('label[for="additionalMembersInput"] ~ .hint');
        
        $('#showAddMembersPopup').click(function() {
            $addMembersModal.modal('show');
            return false;
        });

        $addMembersModal.find('.close-button').click(function() {
            $addMembersModal.modal('hide');
            $memberCountInputHint.hide();
            return false;
        });
        
        $memberCountInput.keydown(function() {
            $memberCountInputHint.hide();
        });
        
        $addMembersModal.find('.accept-button').click(function() {
            var additionalMembersCount = $memberCountInput.val(),
                groupName = $('#userGroupMemberListHeader').text().trim();
            
            additionalMembersCount = isNaN(additionalMembersCount) ? 0 : parseInt(Number(additionalMembersCount));
            
            if (additionalMembersCount <= 0) {
                $memberCountInputHint.attr('lkey', 'Blockly.Msg.ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE');
                $memberCountInputHint.text(Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'] || 'The number must be at least 1.');
                $memberCountInputHint.show();
                return;
            }
            if (additionalMembersCount > 99) {
                $memberCountInputHint.attr('lkey', 'Blockly.Msg.ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED');
                $memberCountInputHint.text(Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED'] || 'There can be no more than 99 members in a group.');
                $memberCountInputHint.show();
                return;
            }
            
            USERGROUP.addGroupMembers(groupName, additionalMembersCount, function (data) {
                switch(data.cause) {
                    case 'ORA_GROUP_ADD_MEMBER_SUCCESS':

                        $memberCountInputHint.hide();
                        $addMembersModal.modal('hide');
                        $memberCountInput.val('');
                        if (data.userGroup) {
                            openDetailUserGroupView(data.userGroup);
                        }
                        break;
                    case 'ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED':
                    case 'ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE':
                        $memberCountInputHint.attr('lkey', 'Blockly.Msg.' + data.cause);
                        $memberCountInputHint.text(Blockly.Msg[data.cause] || data.cause);
                        $memberCountInputHint.show();
                        break;
                    default:
                        UTIL.showMsgOnTop(data.message);
                        
                }
            });
            return false;
        });
    }
    /* This is an internal function and a part of the initialization. Do not export it. */
    
    function openDetailUserGroupView (userGroupData) {
        if (userGroupData == null || typeof userGroupData.name !== 'string') {
            MSG.displayPopupMessage('ORA_GROUP_GET_MEMBERS_ERROR', 'Could not open group detail view for that group.', 'OK');
            return;
        }
        
        $('#userGroupMemberListHeader').html(userGroupData.name.trim() || '&nbsp;');
        
        $userGroupMemberTable.bootstrapTable('showLoading');
        $userGroupMemberTable.bootstrapTable('removeAll');
        $userGroupMemberTable.bootstrapTable('load', userGroupData.members);

        setTimeout(function() {
            //The fronted can not calculate the height of the table if it tries to directly after the table is filled.
            //So we wait 250ms. If you set it to a shorter amount, it wont work again.
            $userGroupMemberTable.bootstrapTable('resetView', {
                height: UTIL.calcDataTableHeight()
            });
            $userGroupMemberTable.bootstrapTable('hideLoading');
        }, 250);
        $('#tabUserGroupMemberList').click();
    }
});
