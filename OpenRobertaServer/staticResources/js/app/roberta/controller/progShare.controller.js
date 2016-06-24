define([ 'require', 'exports', 'log', 'util', 'message', 'comm', 'rest.program', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG,
        UTIL, MSG, COMM, PROGRAM, Blockly, $) {

    function init() {
        initView();
        initEvents();
    }
    exports.init = init;

    function initView() {
        $('#relationsTable').bootstrapTable({
            height : 400,
            iconsPrefix : 'typcn',
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                title : 'Name',
                field : 'name',
                visible : false,
            }, {
                title : 'Owner',
                field : 'owner',
                visible : false,
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_SHARED_WITH'>Geteilt mit</span>",
                field : 'sharedWith',
                events : eventAddShare,
                formatter : formatSharedWith,
            }, {
                title : "<span class='typcn typcn-eye'></span>",
                field : 'read',
                events : eventCheckRead,
                width : '20px',
                halign : 'center',
                valign : 'middle',
                formatter : formatRead,
            }, {
                title : "<span class='typcn typcn-pencil'></span>",
                field : 'write',
                events : eventCheckWrite,
                width : '20px',
                halign : 'center',
                valign : 'middle',
                formatter : formatWrite,
            }, ]
        });
    }

    function initEvents() {

        // triggered from the progList
        $('#show-relations').onWrap('updateAndShow', function(e, data) {
            updateAndShow(data);
            return false;
        });

        // click on the ok button from modal
        $('#shareProgram').onWrap('click', function(e) {
            var data = $('#relationsTable').bootstrapTable('getData');
            var row = data[0];
            if (addSharedUser(row)) {
                updateSharedUser();
            }
        });
    }

    /**
     * Update rights for all users with which this program is shared, no
     * selected right will remove sharing
     */
    function updateSharedUser() {
        var data = $('#relationsTable').bootstrapTable('getData');
        // start from second row, first is already handled in addSharedUser()
        for (var i = 1; i < data.length; i++) {
            var progName = data[i].name;
            var sharedWith = data[i].sharedWith;
            var rightOld = data[i].read; //or .write
            var changed = true;
            var right = 'NONE';
            if ($("#checkRead" + i).is(':checked')) {
                right = 'READ';
            }
            if ($("#checkWrite" + i).is(':checked')) {
                right = 'WRITE';
            }
            if (right !== rightOld) {
                PROGRAM.shareProgram(progName, sharedWith, right, function(result) {
                    if (result.rc === 'ok') {
                        MSG.displayMessage(result.message, "TOAST", sharedWith);
                        LOG.info("share program " + progName + " with '" + sharedWith + " having right '" + right + "'");
                    }
                });
            }
        }
        $('.bootstrap-table').find('button[name="refresh"]').trigger('click');
        $('#show-relations').modal("hide");
    }

    var formatRead = function(value, row, index) {
        if (value === 'READ') {
            return '<input type="checkbox" id="checkRead' + index + '" checked>';
        }
        return '<input type="checkbox" id="checkRead' + index + '">';
    }

    var formatWrite = function(value, row, index) {
        if (value === 'WRITE') {
            return '<input type="checkbox" id="checkWrite' + index + '" checked>';
        }
        return '<input type="checkbox" id="checkWrite' + index + '">';
    }

    var formatSharedWith = function(value, row, index) {
        if (value === 'no input') {
            return '<div class="input-group"><span class="input-group-btn">' + '<button id="addShare" type="button" style="height:34px" class="btn">'
                    + '<i class="typcn typcn-plus"></i></button></span>' + '<input type="text" name="shareWithInput" class="form-control"/></div>';
        }
        return value;
    }

    function updateAndShow(data) {
        var progName = data[0];
        var shared = '';
        if (!$.isEmptyObject(data[2])) {
            shared = data[2];
        }
        $('#show-relations h3').text(Blockly.Msg.BUTTON_DO_SHARE + ' »' + progName + '«').end();
        // $('#show-relations').find('.modal-header>h3').text(Blockly.Msg.BUTTON_DO_SHARE + ' »' + progName + '«').end();
        $('#relationsTable').bootstrapTable('removeAll');
        if (shared) {
            $.each(shared.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    $('#relationsTable').bootstrapTable('insertRow', {
                        index : 0,
                        row : {
                            name : data[0],
                            owner : data[1],
                            sharedWith : user,
                            read : right,
                            write : right,
                        }
                    });
                });
            });
        }
        // add input row for new user to share with
        $('#relationsTable').bootstrapTable('insertRow', {
            index : 0,
            row : {
                name : data[0],
                owner : data[1],
                sharedWith : 'no input',
                read : 'READ',
                write : '',
            }
        });
        $('#show-relations').one('shown.bs.modal', function(e) {
            $('#relationsTable').bootstrapTable("resetView");
        });
        $('#show-relations').modal("show");
    }

    var eventAddShare = {
        'click #addShare' : function(e, value, row, index) {
            addSharedUser(row);
        }
    };

    var eventCheckRead = {
        'click #checkRead0' : function(e) {
            if (!$(this).is(':checked')) {
                $('#checkWrite0').prop('checked', true);
            }
        }
    };

    var eventCheckWrite = {
        'click #checkWrite0' : function() {
            if (!$(this).is(':checked')) {
                $('#checkRead0').prop('checked', true);
            }
        },
    };

    /**
     * Share program with another user.
     * 
     * @param row
     *            {Array} - input row
     * @returns {Bool} - false, if user to share with does not exist or user is
     *          identical with owner
     * 
     */

    function addSharedUser(row) {
        var $inputs = $('#relationsTable input');
        var values = {};
        $inputs.each(function() {
            values[this.name] = $(this).val();
        });
        // user is empty, ignore input
        if (values.shareWithInput === '') {
            return true;
        }
        // new user and owner are the same?
        if (values.shareWithInput === row.owner) {
            UTIL.showMsgOnTop("ORA_USER_TO_SHARE_SAME_AS_LOGIN_USER");
            return false;
        }
        // already shared with this user?
        var updateShareWith = false;
        var data = $('#relationsTable').bootstrapTable('getData');
        for (var i = 0; i < data.length; i++) {
            if (data[i].sharedWith === values.shareWithInput) {
                updateShareWith = i;
            }
        }
        var returnValue = true;
        var right = '';
        if ($("#checkRead0").is(':checked'))
            right = 'READ';
        if ($("#checkWrite0").is(':checked'))
            right = 'WRITE';
        if (right) { // should not be empty
            PROGRAM.shareProgram(row.name, values.shareWithInput, right, function(result) {
                if (result.rc === 'ok') {
                    if (updateShareWith) {
                        if (right === 'NONE') {
                            $('#relationsTable').bootstrapTable('removeRow', {
                                index : updateShareWith,
                            });
                        } else {
                            $('#relationsTable').bootstrapTable('updateRow', {
                                index : updateShareWith,
                                row : {
                                    name : row.name,
                                    owner : row.owner,
                                    sharedWith : values.shareWithInput,
                                    read : right,
                                    write : right,
                                }
                            });
                        }
                    } else {
                        $('#relationsTable').bootstrapTable('insertRow', {
                            index : 1,
                            row : {
                                name : row.name,
                                owner : row.owner,
                                sharedWith : values.shareWithInput,
                                read : right,
                                write : right,
                            }
                        });
                    }
                    LOG.info("share program " + row.name + " with '" + values.shareWithInput + " having right '" + right + "'");
                    $('.bootstrap-table').find('button[name="refresh"]').trigger('click');
                } else {
                    UTIL.showMsgOnTop(result.message);
                    returnValue = false;
                }
            });
        } else {
            return false;
        }
        return returnValue;
    }
});
