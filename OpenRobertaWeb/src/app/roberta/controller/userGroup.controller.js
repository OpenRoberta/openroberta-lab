import * as LOG from 'log';
import * as MSG from 'message';
import * as COM from 'comm';
import * as UTIL from 'util';
import * as USER from 'user.model';
import * as USERGROUP from 'userGroup.model';
import * as GUISTATE_C from 'guiState.controller';
import * as LANG from 'language.controller';
import * as $ from 'jquery';
import * as Blockly from 'blockly';
import 'bootstrap-table';
import 'bootstrap-tagsinput';

var $userGroupTable;
var $userGroupMemberTable;
var userGroupMemberThreshold = 99;
var memberNameValidators = {};

function showPanel() {
    $userGroupTable.bootstrapTable('showLoading');
    USERGROUP.loadUserGroupList(function (data) {
        if (data.rc === 'ok') {
            $userGroupTable.bootstrapTable('load', data.userGroups);
        } else {
            $userGroupTable.bootstrapTable('removeAll');
            MSG.displayInformation(data, data.cause, data.cause);
        }
        setTimeout(function () {
            //The fronted can not calculate the height of the table if it tries to directly after the table is filled.
            //So we wait 250ms. If you set it to a shorter amount, it wont work again.
            $userGroupTable.bootstrapTable('resetView', {
                height: UTIL.calcDataTableHeight(),
            });
            $userGroupTable.find('[data-toggle="tooltip"]').tooltip();
            $userGroupTable.bootstrapTable('hideLoading');
        }, 250);
    });
    $('#tabUserGroupList').clickWrap();
    if (GUISTATE_C.getView() !== 'tabUserGroupList') {
        GUISTATE_C.setView('tabUserGroupList');
    }
}

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
}
export { showPanel, init };

function initUserGroupListTable() {
    var $actionItemsTemplate = $userGroupTable.find('.action-items-template');

    $actionItemsTemplate.remove();

    $userGroupTable.bootstrapTable({
        height: UTIL.calcDataTableHeight(),
        pageList: '[ 10, 25, All ]',
        toolbar: '#userGroupListToolbar',
        toolbarAlign: 'none',
        showRefresh: true,
        sortName: 'created',
        sortOrder: 'desc',
        showPaginationSwitch: true,
        pagination: true,
        buttonsAlign: 'right',
        resizable: true,
        iconsPrefix: 'typcn',
        icons: {
            paginationSwitchDown: 'typcn-document-text',
            paginationSwitchUp: 'typcn-book',
            refresh: 'typcn-refresh',
        },
        columns: [
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_USERGROUP_NAME'>" + (Blockly.Msg.DATATABLE_USERGROUP_NAME || 'Name der Gruppe') + '</span>',
                field: 'name',
                sortable: true,
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_MEMBERS'>" + (Blockly.Msg.DATATABLE_MEMBERS || 'Mitglieder') + '</span>',
                field: 'members',
                sortable: true,
                sorter: function (a, b) {
                    return (a.length || 0) - (b.length || 0);
                },
                formatter: function (value, row, index) {
                    return value.length || 0;
                },
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_SHARED_PROGRAMS'>" + (Blockly.Msg.DATATABLE_SHARED_PROGRAMS || 'Geteilte Programme') + '</span>',
                field: 'programs',
                sortable: false,
                formatter: function (value, row, index) {
                    if (!value || !value.length) {
                        var $returnValue = $(
                            '<div><span lkey="Blockly.Msg.SHARE_PROGRAMS_USERGROUP_HINT" data-translation-targets="title" data-toggle="tooltip" data-container="body" data-placement="right" title="">-</span></div>'
                        );
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

                        var $returnValue = $(
                            '<div>' +
                                '<span>' +
                                '<span class="typcn typcn-' +
                                program.robot +
                                '"></span> ' +
                                '<span class="typcn typcn-' +
                                relationIconKey +
                                '"></span> ' +
                                program.name +
                                '</span>' +
                                '</div>'
                        );
                        LANG.translate($returnValue);

                        return $returnValue.html();
                    };

                    if (value.length === 1) {
                        return programFormatter(value[0]);
                    } else {
                        var entries = value.map(function (program) {
                            return programFormatter(program);
                        });

                        var $returnValue = $(
                            '<div>' +
                                '<div style="white-space:nowrap;">' +
                                '<span style="float:left;">' +
                                entries.shift() +
                                '</span>' +
                                '<a class="collapsed showRelations" href="#" style="float:right;" href="#" data-toggle="collapse" data-target=".relation' +
                                index +
                                '"></a>' +
                                '</div>' +
                                entries
                                    .map(function (entry) {
                                        return '<div style="clear:both;" class="collapse relation' + index + '"> ' + entry + '</div>';
                                    })
                                    .join('') +
                                '</div>'
                        );
                        LANG.translate($returnValue);
                        return $returnValue.html();
                    }
                },
                events: {
                    'click .showRelations': function (e, value, row, index) {
                        e.stopPropagation();
                        var collapseName = '.relation' + index;
                        $(collapseName).collapse('toggle');
                    },
                },
            },
            {
                title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || 'Erzeugt am') + '</span>',
                field: 'created',
                sortable: true,
                formatter: UTIL.formatDate,
            },
            {
                checkbox: true,
                valign: 'middle',
            },
            {
                title:
                    '<a href="#" id="deleteUserGroups" class="deleteSome disabled" rel="tooltip" lkey="Blockly.Msg.USERGROUP_LIST_DELETE_ALL_TOOLTIP" data-original-title="" data-container="body" title="" data-translation-targets="title data-original-title">' +
                    '<span class="typcn typcn-delete"></span></a>',
                events: {
                    'click .delete': function (e, value, row, index) {
                        e.stopPropagation();

                        var groupName = typeof row.name === undefined ? null : row.name,
                            groupMembers = typeof row.members === undefined ? [] : row.members,
                            $button = $(this),
                            deleteFunction = function () {
                                USERGROUP.deleteUserGroup(row.name, function (data) {
                                    if (data.rc === 'ok') {
                                        $userGroupTable.bootstrapTable('remove', { field: 'name', values: [row.name] });
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
                        } else if (
                            groupMembers.filter(function (groupMember) {
                                return !groupMember.hasDefaultPassword;
                            }).length === 0
                        ) {
                            var modalMessageKey = 'USERGROUP_DELETE_WITH_MEMBERS_WARNING',
                                modalMessage =
                                    Blockly.Msg[modalMessageKey] ||
                                    'Are your sure that you want to delete the usergroup including all members? No member did log in so far.';
                            $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
                                $('#confirm').off();
                                $('#confirm').on('click', function (e) {
                                    e.preventDefault();
                                    deleteFunction(true);
                                });
                                $('#confirmCancel').off();
                                $('#confirmCancel').on('click', function (e) {
                                    e.preventDefault();
                                    $('.modal').modal('hide');
                                });
                            });
                            MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);
                        } else {
                            UTIL.showMsgOnTop('ORA_GROUP_DELETE_ERROR_GROUP_HAS_MEMBERS');
                        }
                    },
                },
                align: 'left',
                valign: 'top',
                formatter: function (value, row, index) {
                    return $actionItemsTemplate.find('td').html();
                },
                width: '117px',
            },
        ],
    });

    $('#userGroupList').find('[data-toggle="tooltip"]').tooltip();
    $userGroupTable.bootstrapTable('togglePagination');
    LANG.translate($('#userGroupList'));
}
/* This is an internal function and a part of the initialization. Do not export it. */

function initUserGroupEvents() {
    var $userGroupList = $userGroupTable.closest('#userGroupList');

    $(window).resize(function () {
        $userGroupTable.bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
    });

    $userGroupList.find('[data-toggle="tooltip"]').tooltip();
    $('#create-user-group').find('[data-toggle="tooltip"]').tooltip();

    $userGroupList.find('button[name="refresh"]').onWrap(
        'click',
        function (evt) {
            evt.preventDefault();
            showPanel();
        },
        'refreshed usergroup view'
    );

    $userGroupTable.onWrap(
        'check-all.bs.table',
        function ($element, rows) {
            $userGroupList.find('.deleteSome').removeClass('disabled');
            $userGroupTable.find('.delete').addClass('disabled');
        },
        'check all usergroups'
    );

    $userGroupTable.onWrap(
        'check.bs.table',
        function ($element, row) {
            $userGroupList.find('.deleteSome').removeClass('disabled');
            $userGroupTable.find('.delete').addClass('disabled');
        },
        'check one usergroup'
    );

    $userGroupTable.onWrap(
        'uncheck-all.bs.table',
        function ($element, rows) {
            $userGroupList.find('.deleteSome').addClass('disabled');
            $userGroupTable.find('.delete').filter(':not([data-status="disabled"])').removeClass('disabled');
        },
        'uncheck all usergroups'
    );

    $userGroupTable.onWrap(
        'uncheck.bs.table',
        function ($element, row) {
            var selectedRows = $userGroupTable.bootstrapTable('getSelections');
            if (!selectedRows || selectedRows.length === 0) {
                $userGroupList.find('.deleteSome').addClass('disabled');
                $userGroupTable.find('.delete').filter(':not([data-status="disabled"])').removeClass('disabled');
            }
        },
        'uncheck one usergroup'
    );

    $userGroupList.find('.deleteSome').onWrap(
        'click',
        function () {
            var selectedRows = $userGroupTable.bootstrapTable('getSelections'),
                $deleteAllButton = $userGroupList.find('.deleteSome');

            if (!selectedRows || selectedRows.length === 0 || $deleteAllButton.hasClass('disabled')) {
                return;
            }

            var groupNames = selectedRows.map(function (row) {
                return row.name;
            });
            if (
                selectedRows.reduce(function (carry, element) {
                    return carry || (element && element.members && element.members.length > 0);
                }, false)
            ) {
                if (
                    selectedRows.reduce(function (carry, element) {
                        return (
                            carry ||
                            (element &&
                                element.members &&
                                element.members.filter(function (member) {
                                    return !member.hasDefaultPassword;
                                }).length > 0)
                        );
                    }, false)
                ) {
                    //Logged in users exist. Delete them first.
                    MSG.displayInformation({}, '', 'ORA_GROUP_DELETE_ERROR_GROUP_HAS_MEMBERS');
                } else {
                    var modalMessageKey = 'USERGROUP_DELETE_WITH_MEMBERS_WARNING',
                        modalMessage =
                            Blockly.Msg[modalMessageKey] ||
                            'Are your sure that you want to delete the usergroup including all members? No member did log in so far.';
                    $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
                        $('#confirm').off();
                        $('#confirm').on('click', function (e) {
                            e.preventDefault();
                            USERGROUP.deleteUserGroups(groupNames, function (data) {
                                if (data.rc === 'ok') {
                                    $userGroupTable.bootstrapTable('remove', { field: 'name', values: groupNames });
                                } else {
                                    UTIL.showMsgOnTop(data.message);
                                }
                            });
                        });
                        $('#confirmCancel').off();
                        $('#confirmCancel').on('click', function (e) {
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
                        $userGroupTable.bootstrapTable('remove', { field: 'name', values: groupNames });
                    } else {
                        UTIL.showMsgOnTop(data.message);
                    }
                });
            }
        },
        'bulk delete usergroup'
    );

    $('#backUserGroupList').onWrap(
        'click',
        function () {
            $('#tabProgram').clickWrap();
            return false;
        },
        'closed usergroup view and went back to program view.'
    );

    $userGroupTable.onWrap(
        'click-row.bs.table',
        function (e, rowData, row) {
            openDetailUserGroupView(rowData);
        },
        'show usergroup member view'
    );

    initCreateUserGroupEvents();
}
/* This is an internal function and a part of the initialization. Do not export it. */

function initCreateUserGroupEvents() {
    var $createUserGroupModal = $('#create-user-group'),
        $createUserGroupForm = $createUserGroupModal.find('#user-group-form');

    $('#showCreateUserGroupPopup').clickWrap(function () {
        $createUserGroupForm.validate();
        $createUserGroupModal.modal('show');
        return false;
    });

    $('#create-user-group .close-button').clickWrap(function () {
        $createUserGroupModal.modal('hide');
        return false;
    });

    $.validator.addMethod(
        'isValidGroupName',
        function (value, element) {
            return value.trim() !== '' && !/[^a-zA-Z0-9=+!?.,%#+&^@_\- ]/gi.test(value.trim());
        },
        Blockly.Msg['ORA_GROUP_ERROR_NAME_INVALID']
    );
    $.validator.addMethod(
        'isOptionalIntBiggerEquals0Regex',
        function (value, element) {
            return /^\d*$/.test(value.trim());
        },
        Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE']
    );
    $.validator.addMethod(
        'isOptionalOrNotOverThreshold',
        function (value, element) {
            return isNaN(value) || value.trim() === '' || parseInt(value.trim()) <= userGroupMemberThreshold;
        },
        Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED']
    );

    $createUserGroupForm.removeData('validator');
    $createUserGroupForm.validate({
        rules: {
            name: {
                required: true,
                isValidGroupName: true,
            },
            initialMembers: {
                isOptionalIntBiggerEquals0Regex: true,
                isOptionalOrNotOverThreshold: true,
            },
        },
        errorClass: 'form-invalid',
        errorPlacement: function (label, element) {
            label.insertBefore(element.parent());
        },
        messages: {
            name: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                isValidGroupName: Blockly.Msg['ORA_GROUP_ERROR_NAME_INVALID'],
            },
            initialMembers: {
                isOptionalIntBiggerEquals0Regex: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                isOptionalOrNotOverThreshold: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED'],
                min: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                max: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                step: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                number: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
            },
        },
    });

    $('#create-user-group .accept-button').clickWrap(function () {
        var validator = $createUserGroupForm.validate(),
            groupName = $('#userGroupNameInput').val(),
            initialMembersCount = $('#initialMembersInput').val().trim(),
            initialMembers = [],
            formatter = new Intl.NumberFormat('en-US', { minimumIntegerDigits: 2 });

        if (!$createUserGroupForm.valid()) {
            return;
        }

        initialMembersCount = initialMembersCount === '' ? 0 : parseInt(initialMembersCount);

        for (var i = 1; i <= initialMembersCount; i++) {
            initialMembers.push(formatter.format(i));
        }

        USERGROUP.createUserGroup(groupName, initialMembers, function (data) {
            if (data.rc === 'ok') {
                var tableData = $userGroupTable.bootstrapTable('getData');

                //Clone array, because the original array is directly linked to the bootstrap table.
                //No need to clone the items in it, though, normal reference copy is enough.
                tableData = tableData.map(function (item) {
                    return item;
                });
                tableData.unshift(data.userGroup);

                $userGroupTable.bootstrapTable('showLoading');
                $userGroupTable.bootstrapTable('removeAll');
                $userGroupTable.bootstrapTable('load', tableData);
                $userGroupTable.bootstrapTable('hideLoading');

                $createUserGroupModal.modal('hide');

                $('#userGroupNameInput').val('');
                $('#initialMembersInput').val(0);
            } else {
                switch (data.cause) {
                    case 'ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE':
                    case 'ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED':
                        validator.showErrors({
                            initialMembers: Blockly.Msg[data.cause],
                        });
                        break;
                    case 'ORA_GROUP_ERROR_NAME_INVALID':
                    case 'ORA_GROUP_CREATE_ERROR_GROUP_ALREADY_EXISTS':
                    default:
                        validator.showErrors({
                            name: Blockly.Msg[data.cause],
                        });
                }
            }
        });
        return false;
    });
}
/* This is an internal function and a part of the initialization. Do not export it. */

function initUserGroupMemberListTable() {
    var $memberPasswordResetTemplate = $userGroupMemberTable.find('.reset-password-template'),
        $memberActionItemsTemplate = $userGroupMemberTable.find('.action-items-template'),
        $memberActionItemsHeaderTemplate = $userGroupMemberTable.find('.action-items-header-template'),
        $memberNameTemplate = $userGroupMemberTable.find('.edit-member-template'),
        nameChangeValidateOptions = {
            rules: {
                name: {
                    maxlength: 25,
                    loginRegex: true,
                },
            },
            errorClass: 'form-invalid',
            errorPlacement: function (label, element) {
                label.insertBefore(element);
            },
            messages: {
                name: {
                    maxlength: Blockly.Msg['VALIDATION_MAX_LENGTH'],
                    loginRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS'],
                },
            },
        };

    $memberPasswordResetTemplate.remove();
    $memberActionItemsTemplate.remove();
    $memberActionItemsHeaderTemplate.remove();
    $memberNameTemplate.remove();

    $.validator.addMethod(
        'loginRegex',
        function (value, element) {
            return this.optional(element) || /^[a-zA-Z0-9=+!?.,%#+&^@_\- ]+$/gi.test(value);
        },
        Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS']
    );

    $userGroupMemberTable.bootstrapTable({
        height: UTIL.calcDataTableHeight(),
        pageList: '[ 10, 25, All ]',
        toolbar: '#userGroupMemberListToolbar',
        toolbarAlign: 'none',
        showRefresh: true,
        sortName: 'account',
        sortOrder: 'asc',
        showPaginationSwitch: true,
        pagination: true,
        buttonsAlign: 'right',
        resizable: true,
        iconsPrefix: 'typcn',
        icons: {
            paginationSwitchDown: 'typcn-document-text',
            paginationSwitchUp: 'typcn-book',
            refresh: 'typcn-refresh',
        },
        columns: [
            {
                title: "<span lkey='Blockly.Msg.MENU_USER_TOOLTIP'>" + (Blockly.Msg.MENU_USER_TOOLTIP || 'User') + '</span>',
                field: 'account',
                formatter: function (value, row, index) {
                    var $memberNameTemplateClone = $memberNameTemplate.find('td').clone(false),
                        name = value.substr(value.lastIndexOf(':') + 1);

                    $memberNameTemplateClone.find('.member-name').text(name);
                    $memberNameTemplateClone.find('input').attr('value', name);
                    if (!row.hasDefaultPassword) {
                        $memberNameTemplateClone.find('.member-name-toggle-button').css('visibility', 'hidden');
                    }
                    if (row.id === 0) {
                        $memberNameTemplate.find('.active').removeClass('active');
                        $memberNameTemplateClone.find('.edit-member-name').addClass('active');
                        $memberNameTemplateClone.find('.member-name-edit-button').addClass('typcn-plus');
                        $memberNameTemplateClone.find('.member-name-edit-button').removeClass('typcn-tick');
                    }
                    if ($memberNameTemplateClone.find('.active').length === 0) {
                        $memberNameTemplateClone.find('.member-name').addClass('active');
                    }

                    return $memberNameTemplateClone.html();
                },
                events: {
                    'click .member-name-toggle-button': function (e, value, row, index) {
                        var $self = $(this).closest('td'),
                            oldName = value.substr(value.lastIndexOf(':') + 1),
                            newName = $self.find('input').first().val();

                        if (typeof memberNameValidators[index] === 'undefined') {
                            memberNameValidators[index] = $self.find('form').first().validate(nameChangeValidateOptions);
                        }

                        if ($self.find('.member-name').hasClass('active')) {
                            $self.find('.member-name').removeClass('active');
                            $self.find('.edit-member-name').addClass('active');
                            $self.find('.member-name-column').addClass('active');
                            $(document.body).on('click', function (e) {
                                if (
                                    $(e.target).closest('tr[data-index="' + index + '"]').length === 0 &&
                                    !$self.find('.member-name-toggle-button').hasClass('disabled') &&
                                    $(e.target).closest('.modal').length === 0
                                ) {
                                    if (typeof newName === 'undefined' || newName === '' || newName === oldName) {
                                        $self.find('input').first().val(oldName);
                                    }

                                    $self.find('.member-name').addClass('active');
                                    $self.find('.edit-member-name').removeClass('active');
                                    $self.find('.member-name-column').removeClass('active');
                                    $(document.body).off(e);
                                }
                            });
                            $self.find('input').first().select();
                        } else {
                            $self.find('.member-name').addClass('active');
                            $self.find('.edit-member-name').removeClass('active');
                            $self.find('.member-name-column').removeClass('active');
                        }
                    },
                    'dblclick .member-name.active': function (e, value, row, index) {
                        var $toggleButton = $(this).closest('td').find('.member-name-toggle-button');
                        if (
                            $toggleButton.is(':visible') &&
                            $toggleButton.css('visibility') !== 'hidden' &&
                            !$toggleButton.hasClass('disabled') &&
                            !$toggleButton.prop('disabled')
                        ) {
                            $(this).closest('td').find('.member-name-toggle-button').clickWrap();
                        }
                    },
                    'click .member-name-edit-button': function (e, value, row, index) {
                        var $button = $(this),
                            $self = $button.closest('td'),
                            $input = $self.find('input').first(),
                            oldName = value.substr(value.lastIndexOf(':') + 1),
                            newName = $input.val();

                        if (typeof memberNameValidators[index] === 'undefined') {
                            memberNameValidators[index] = $self.find('form').first().validate(nameChangeValidateOptions);
                        }

                        if (!$self.find('form').valid()) {
                            return;
                        }

                        if (typeof newName === 'undefined' || newName === '' || newName === oldName) {
                            if (row.id !== 0) {
                                $self.find('.member-name-toggle-button').clickWrap();
                            }
                            return;
                        }

                        $button.addClass('typcn-arrow-sync');
                        $button.removeClass('typcn-tick');
                        $button.addClass('iais-loading-spin');
                        $self.find('input').prop('disabled', true);
                        $self.find('.member-name-toggle-button').addClass('disabled');

                        USERGROUP.updateMemberAccount(value, $('#userGroupMemberListHeader').text(), newName, function (data) {
                            if (data.rc === 'ok') {
                                if (row.id !== 0) {
                                    //clone the current row data
                                    var rowData = JSON.parse(JSON.stringify(row));
                                    rowData.account = $('#userGroupMemberListHeader').text() + ':' + newName;

                                    delete memberNameValidators[index];

                                    $userGroupMemberTable.bootstrapTable('updateRow', {
                                        index: index,
                                        row: rowData,
                                        replace: true,
                                    });
                                } else {
                                    $userGroupMemberTable.bootstrapTable('append', {
                                        id: 123,
                                        account: $('#userGroupMemberListHeader').text() + ':' + newName,
                                        hasDefaultPassword: true,
                                    });
                                }
                            } else {
                                memberNameValidators[index].showErrors({
                                    name: Blockly.Msg[data.cause] || data.message,
                                });
                                $button.removeClass('iais-loading-spin');
                                $button.addClass('typcn-tick');
                                $button.removeClass('typcn-arrow-sync');
                                $self.find('input').prop('disabled', false);
                                $self.find('.member-name-toggle-button').removeClass('disabled');
                            }
                        });
                    },
                    'keydown input': function (e, value, row, index) {
                        if (e.originalEvent.keyCode === 13) {
                            e.preventDefault();
                            $(this).closest('td').find('.member-name-edit-button').clickWrap();
                            return false;
                        }
                    },
                    'submit form': function (e) {
                        e.preventDefault();
                        $(this).closest('td').find('.member-name-edit-button').clickWrap();
                        return false;
                    },
                },
                sortable: true,
                sorter: function (a, b) {
                    if (a === '') {
                        return 1;
                    }
                    if (b === '') {
                        return -1;
                    }
                    return a.localeCompare(b);
                },
            },
            {
                title: "<span lkey='Blockly.Msg.POPUP_PASSWORD'>" + (Blockly.Msg.POPUP_PASSWORD || 'Password') + '</span>',
                field: 'password',
                formatter: function (value, row, index) {
                    if (row.id === 0 && row.account === '') {
                        return '';
                    }
                    return row.hasDefaultPassword ? row.account : '************';
                },
                sortable: false,
                width: '33.3333%',
            },
            {
                title: '<input name="btSelectAll" type="checkbox">',
                formatter: function (value, row, index) {
                    if (row.id === 0) {
                        return '';
                    }
                    return '<input type="checkbox" name="btSelectItem" data-index="' + index + '">';
                },
                valign: 'middle',
                halign: 'center',
                align: 'center',
                width: '37px',
            },
            {
                title: $memberActionItemsHeaderTemplate.find('td').html(),
                events: {
                    'click .delete': function (e, value, row, index) {
                        e.stopPropagation();

                        var $button = $(this);

                        if ($button.hasClass('disabled') || !row.account) {
                            return;
                        }

                        var deleteFunction = function (members) {
                            if (!members) {
                                return;
                            }
                            var memberAccountNames = members.map(function (member) {
                                return member.account;
                            });

                            USERGROUP.deleteGroupMembers(memberAccountNames[0].split(':', 2)[0], memberAccountNames, function (data) {
                                if (data.rc === 'ok') {
                                    $userGroupMemberTable.bootstrapTable('remove', {
                                        field: 'account',
                                        values: memberAccountNames,
                                    });
                                } else {
                                    UTIL.showMsgOnTop(data.message);
                                }
                            });
                        };

                        /*
                     * 
                    var hasDefaultPassword = members.reduce(function(carry, member) {
                        return carry && member.hasDefaultPassword;
                    }, true);
                     */

                        var modalMessageKey = row.hasDefaultPassword ? 'DELETE_USERGROUP_MEMBER_WARNING' : 'DELETE_USERGROUP_MEMBER_AFTER_LOGIN_WARNING',
                            modalMessage =
                                Blockly.Msg[modalMessageKey] ||
                                'The member you want to delete might have create own programs and did already log in. Are you sure, that you want to delete the member?';
                        $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
                            $('#confirm').off();
                            $('#confirm').on('click', function (e) {
                                e.preventDefault();
                                deleteFunction([row]);
                            });
                            $('#confirmCancel').off();
                            $('#confirmCancel').on('click', function (e) {
                                e.preventDefault();
                                $('.modal').modal('hide');
                            });
                        });
                        MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);
                    },
                    'click .reset-password': function (e, value, row, index) {
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
                                    replace: true,
                                });
                                $button.addClass('disabled');
                            } else {
                                UTIL.showMsgOnTop(data.message);
                            }
                        });
                    },
                },
                align: 'left',
                valign: 'top',
                formatter: function (value, row, index) {
                    if (row.id === 0 && row.account === '') {
                        return '';
                    }
                    var $element = $memberActionItemsTemplate.clone(true);
                    if (row.hasDefaultPassword) {
                        var $button = $element.find('.reset-password');
                        $button.addClass('disabled');
                        $button.attr('data-status', 'disabled');
                    }
                    LANG.translate($element);
                    return $element.find('td').html();
                },
                width: '117px',
            },
        ],
    });
    $('#userGroupMemberList').find('[data-toggle="tooltip"]').tooltip();
    $userGroupMemberTable.bootstrapTable('togglePagination');
    LANG.translate($('#userGroupMemberList'));
}
/* This is an internal function and a part of the initialization. Do not export it. */

function initUserGroupMemberEvents() {
    $(window).resize(function () {
        $userGroupMemberTable.bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
    });

    $userGroupMemberTable
        .closest('#userGroupMemberList')
        .find('button[name="refresh"]')
        .onWrap(
            'click',
            function (evt) {
                evt.preventDefault();

                var groupName = $('#userGroupMemberListHeader').text();

                $userGroupMemberTable.bootstrapTable('showLoading');
                USERGROUP.loadUserGroup(groupName, function (data) {
                    if (data.rc === 'ok' && typeof data.userGroup !== 'undefined' && typeof data.userGroup.members !== 'undefined') {
                        var members = data.userGroup.members;
                        members.push({
                            id: 0,
                            account: '',
                            hasDefaultPassword: false,
                        });
                        $userGroupMemberTable.bootstrapTable('load', data.userGroup.members);
                        memberNameValidators = {};
                    } else {
                        $userGroupMemberTable.bootstrapTable('removeAll');
                        MSG.displayInformation(data, data.cause, data.cause);
                    }
                    setTimeout(function () {
                        //The fronted can not calculate the height of the table if it tries to directly after the table is filled.
                        //So we wait 250ms. If you set it to a shorter amount, it wont work again.
                        $userGroupMemberTable.bootstrapTable('resetView', {
                            height: UTIL.calcDataTableHeight(),
                        });
                        $userGroupMemberTable.bootstrapTable('hideLoading');
                    }, 250);
                });
            },
            'refreshed usergroup member view'
        );

    $userGroupMemberTable.onWrap(
        'check-all.bs.table',
        function (e, rows) {
            $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome').removeClass('disabled');

            var atLeastOneHasNotDefaultPassword = rows.reduce(function (noDefaultPasswordFound, row) {
                return noDefaultPasswordFound || !row.hasDefaultPassword;
            }, false);

            if (atLeastOneHasNotDefaultPassword) {
                $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').removeClass('disabled');
            }

            $userGroupMemberTable.find('.delete, .reset-password').addClass('disabled');
        },
        'check all usergroups'
    );

    $userGroupMemberTable.onWrap(
        'check.bs.table',
        function (e, row, $element) {
            $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome').removeClass('disabled');
            if (!row.hasDefaultPassword) {
                $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').removeClass('disabled');
            }
            $userGroupMemberTable.find('.delete, .reset-password').addClass('disabled');
        },
        'check one usergroup'
    );

    $userGroupMemberTable.onWrap(
        'uncheck-all.bs.table',
        function (e, rows) {
            $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome, .resetPasswords').addClass('disabled');
            $userGroupMemberTable.find('.delete, .reset-password').filter(':not([data-status="disabled"])').removeClass('disabled');
        },
        'uncheck all usergroups'
    );

    $userGroupMemberTable.onWrap(
        'uncheck.bs.table',
        function (e, row, $element) {
            var selectedRows = $userGroupMemberTable.bootstrapTable('getSelections');
            if (!selectedRows || selectedRows.length === 0) {
                $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome, .resetPasswords').addClass('disabled');
                $userGroupMemberTable.find('.delete, .reset-password').filter(':not([data-status="disabled"])').removeClass('disabled');
            } else {
                var atLeastOneHasNotDefaultPassword = selectedRows.reduce(function (noDefaultPasswordFound, row) {
                    return noDefaultPasswordFound || !row.hasDefaultPassword;
                }, false);
                if (!atLeastOneHasNotDefaultPassword) {
                    $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords').addClass('disabled');
                }
            }
        },
        'uncheck one usergroup'
    );

    $userGroupMemberTable
        .closest('#userGroupMemberList')
        .find('.deleteSome')
        .onWrap(
            'click',
            function () {
                var selectedRows = $userGroupMemberTable.bootstrapTable('getSelections'),
                    $deleteSomeButton = $userGroupMemberTable.closest('#userGroupMemberList').find('.deleteSome');

                if (!selectedRows || selectedRows.length === 0 || $deleteSomeButton.hasClass('disabled')) {
                    return;
                }

                var hasDefaultPassword = selectedRows.reduce(function (carry, member) {
                        return carry && member.hasDefaultPassword;
                    }, true),
                    deleteFunction = function (members) {
                        if (!members) {
                            return;
                        }
                        var memberAccountNames = members.map(function (member) {
                            return member.account;
                        });

                        USERGROUP.deleteGroupMembers(memberAccountNames[0].split(':', 2)[0], memberAccountNames, function (data) {
                            if (data.rc === 'ok') {
                                $userGroupMemberTable.bootstrapTable('remove', {
                                    field: 'account',
                                    values: memberAccountNames,
                                });
                            } else {
                                UTIL.showMsgOnTop(data.message);
                            }
                        });
                    };

                var modalMessageKey = hasDefaultPassword ? 'DELETE_USERGROUP_MEMBER_WARNING' : 'DELETE_USERGROUP_MEMBER_AFTER_LOGIN_WARNING',
                    modalMessage =
                        Blockly.Msg[modalMessageKey] ||
                        'The member you want to delete might have create own programs and did already log in. Are you sure, that you want to delete the member?';
                $('#show-message-confirm').oneWrap('shown.bs.modal', function (e) {
                    $('#confirm').off();
                    $('#confirm').on('click', function (e) {
                        e.preventDefault();
                        deleteFunction(selectedRows);
                    });
                    $('#confirmCancel').off();
                    $('#confirmCancel').on('click', function (e) {
                        e.preventDefault();
                        $('.modal').modal('hide');
                    });
                });
                MSG.displayPopupMessage(modalMessageKey, modalMessage, 'OK', Blockly.Msg.POPUP_CANCEL);
            },
            'Bulk removed members of usergroup.'
        );

    $userGroupMemberTable
        .closest('#userGroupMemberList')
        .find('.resetPasswords')
        .onWrap(
            'click',
            function () {
                var selectedRows = $userGroupMemberTable.bootstrapTable('getSelections'),
                    $resetAllButton = $userGroupMemberTable.closest('#userGroupMemberList').find('.resetPasswords');

                if (!selectedRows || selectedRows.length === 0 || $resetAllButton.hasClass('disabled')) {
                    return;
                }

                var userGroupName = selectedRows[0].account.split(':', 2)[0],
                    memberAccounts = selectedRows
                        .filter(function (row) {
                            return !row.hasDefaultPassword;
                        })
                        .map(function (row) {
                            return row.account;
                        });

                USERGROUP.setUserGroupMemberDefaultPasswords(userGroupName, memberAccounts, function (data) {
                    if (data.rc === 'ok') {
                        $userGroupMemberTable.closest('#userGroupMemberList').find('button[name="refresh"]').clickWrap();
                    } else {
                        UTIL.showMsgOnTop(data.message);
                    }
                });
            },
            'Bulk resetted passwords of usergroup members.'
        );

    $('#backUserGroupMemberList').clickWrap(function () {
        showPanel();
        return false;
    });
}

/* This is an internal function and a part of the initialization. Do not export it. */
function initAddMembersToGroupEvents() {
    var $addMembersModal = $('#user-group-add-members'),
        $addMembersForm = $addMembersModal.find('form'),
        $memberCountInput = $addMembersForm.find('#additionalMembersInput'),
        $memberCountInputHint = $addMembersModal.find('label[for="additionalMembersInput"] ~ .hint');

    $('#showAddMembersPopup').clickWrap(function () {
        $addMembersForm.validate();
        $addMembersModal.modal('show');
        return false;
    });

    $addMembersModal.find('.close-button').clickWrap(function () {
        $addMembersModal.modal('hide');
        return false;
    });

    $.validator.addMethod(
        'isIntBiggerThan1Regex',
        function (value, element) {
            return /^\s*0*[1-9]\d*\s*$/.test(value.trim());
        },
        Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE']
    );
    $.validator.addMethod(
        'notOverThreshold',
        function (value, element) {
            return !isNaN(value) && $userGroupMemberTable.bootstrapTable('getData').length - 1 + parseInt(value.trim()) <= userGroupMemberThreshold;
        },
        Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED']
    );

    $addMembersForm.removeData('validator');
    $addMembersForm.validate({
        rules: {
            additionalMembers: {
                required: true,
                isIntBiggerThan1Regex: true,
                notOverThreshold: true,
            },
        },
        errorClass: 'form-invalid',
        errorPlacement: function (label, element) {
            label.insertBefore(element.parent());
        },
        messages: {
            additionalMembers: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                isIntBiggerThan1Regex: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                notOverThreshold: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_LIMIT_REACHED'],
                min: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                max: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                step: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
                number: Blockly.Msg['ORA_GROUP_ADD_MEMBER_ERROR_SMALLER_THAN_ONE'],
            },
        },
    });

    $addMembersForm.submit(function (e) {
        e.preventDefault();
        $addMembersModal.find('.accept-button').clickWrap();
        return false;
    });

    $memberCountInput.keydown(function (e) {
        if (e.originalEvent.keyCode === 13) {
            e.stopPropagation();
            e.preventDefault();
            $addMembersModal.find('.accept-button').clickWrap();
            return false;
        }
    });

    $addMembersModal.find('.accept-button').clickWrap(function () {
        var validator = $addMembersForm.validate();

        if (!$addMembersForm.valid()) {
            return;
        }

        var additionalMembersCount = parseInt($memberCountInput.val().trim()),
            additionalMembers = [],
            formatter = new Intl.NumberFormat('en-US', { minimumIntegerDigits: 2 }),
            currentMaximum = 0,
            groupName = $('#userGroupMemberListHeader').text().trim(),
            rows = $userGroupMemberTable.bootstrapTable('getData');

        currentMaximum = rows.reduce(function (maximum, row) {
            if (row === null || row.id === 0) {
                return maximum;
            }
            var accountName = row.account.substr(row.account.indexOf(':') + 1);
            if (isNaN(accountName)) {
                return maximum;
            }
            accountName = parseInt(accountName);
            return Math.max(maximum, accountName);
        }, currentMaximum);

        for (var i = currentMaximum + 1; i <= currentMaximum + additionalMembersCount; i++) {
            additionalMembers.push(formatter.format(i));
        }

        USERGROUP.addGroupMembers(groupName, additionalMembers, function (data) {
            if (data.rc === 'ok') {
                $addMembersModal.modal('hide');
                $memberCountInput.val('');
                if (data.userGroup) {
                    openDetailUserGroupView(data.userGroup);
                }
            } else {
                validator.showErrors({
                    additionalMembers: Blockly.Msg[data.cause],
                });
            }
        });
        return false;
    });
}

/* This is an internal function and a part of the initialization. Do not export it. */
function openDetailUserGroupView(userGroupData) {
    if (userGroupData == null || typeof userGroupData.name !== 'string') {
        MSG.displayPopupMessage('ORA_GROUP_GET_MEMBERS_ERROR', 'Could not open group detail view for that group.', 'OK');
        return;
    }

    $('#userGroupMemberListHeader').html(userGroupData.name.trim() || '&nbsp;');
    $('#additionalMembersInput').val('');

    var members = userGroupData.members.map(function (member) {
        return member;
    });

    members.push({
        id: 0,
        account: '',
        hasDefaultPassword: false,
    });

    $userGroupMemberTable.bootstrapTable('showLoading');
    $userGroupMemberTable.bootstrapTable('removeAll');
    $userGroupMemberTable.bootstrapTable('load', members);
    memberNameValidators = {};

    setTimeout(function () {
        //The fronted can not calculate the height of the table if it tries to directly after the table is filled.
        //So we wait 250ms. If you set it to a shorter amount, it wont work again.
        $userGroupMemberTable.bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight(),
        });
        $userGroupMemberTable.bootstrapTable('hideLoading');
    }, 250);
    $('#tabUserGroupMemberList').clickWrap();
}
