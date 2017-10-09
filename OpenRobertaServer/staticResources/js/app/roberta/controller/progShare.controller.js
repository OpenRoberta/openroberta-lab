define([ 'require', 'exports', 'log', 'util', 'message', 'comm', 'guiState.controller', 'galleryList.controller', 'program.model', 'blocks-msg', 'jquery',
        'bootstrap-table' ], function(require, exports, LOG, UTIL, MSG, COMM, GUISTATE_C, GALLERY_C, PROGRAM, Blockly, $) {

    function init() {
        initView();
        initEvents();
        LOG.info('init program sharing view');
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
                title : "<span lkey='Blockly.Msg.DATATABLE_SHARED_WITH'>" + (Blockly.Msg.DATATABLE_SHARED_WITH || "Geteilt mit") + "</span>",
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
        $('#galleryPreview').bootstrapTable({
            height : 410,
            cardView : 'true',
            rowStyle : GALLERY_C.rowStyle,
            rowAttributes : GALLERY_C.rowAttributes,
            resizable : 'true',
            iconsPrefix : 'typcn',
            columns : [ {
                sortable : true,
              //visible : false,
                formatter : GALLERY_C.formatRobot,
            }, {
                sortable : true,
                formatter : GALLERY_C.formatProgramName,
            }, {
                sortable : true,
                formatter : GALLERY_C.formatProgramDescription,
            }, {
                title : GALLERY_C.titleAuthor,
                sortable : true,
            }, {
                title : GALLERY_C.titleDate,
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                title : GALLERY_C.titleNumberOfViews,
                sortable : true,
            }, {
                title : GALLERY_C.titleLikes,
                sortable : true,
            }, {
                sortable : true,
                formatter : GALLERY_C.formatTags,
            }, {
                visible : false,
            } ]
        });
    }

    function initEvents() {

        // triggered from the progList
        $('#show-relations').onWrap('updateAndShow', function(e, data) {
            showShareWithUser(data);
            return false;
        });

        // triggered from the progList
        $('#share-with-gallery').onWrap('updateAndShow', function(e, row) {
            showShareWithGallery(row);
            return false;
        });

        // click on the ok button from modal
        $('#shareProgram').onWrap('click', function(e) {
            var data = $('#relationsTable').bootstrapTable('getData');
            var row = data[0];
            if (updateShareWithUser(row)) {
                updateSharedWithUsers();
            }
        });
        // click on the ok button from modal
        $('#shareWithGallery').onWrap('click', function(e) {
            var table = $('#share-with-gallery .modal-body').find(">:first-child");
            if (table) {
                table.show();
            } else {
                $('#textShareGallery').show();
            }
            updateShareWithGallery($('#share-with-gallery').data('action'));
        });

        $('#cancelShareWithGallery').onWrap('click', function(e) {
            var table = $('#share-with-gallery .modal-body').find(">:first-child");
            if (table) {
                table.show();
            } else {
                $('#textShareGallery').show();
            }
            $('#share-with-gallery').modal("hide");
        });
    }

    function showShareWithUser(data) {
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
                    if (user !== 'Gallery') {
                        $('#relationsTable').bootstrapTable('insertRow', {
                            index : -1,
                            row : {
                                name : data[0],
                                owner : data[1],
                                sharedWith : user,
                                read : right,
                                write : right,
                            }
                        });
                    }
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
            $('#relationsTable').find('input :first').focus();
        });
        $('#show-relations').modal("show");
    }

    function showShareWithGallery(row) {
        var progName = row[0];
        var authorName = row[3];
        $('#share-with-gallery h3').html('');
        $('#galleryPreview').html('');
        $('#textShareGallery').html('');
        $('#share-with-gallery').data('progName', progName);
        $('#share-with-gallery').data('user', authorName);
        // check if this program has already shared with the gallery
        PROGRAM.loadProgramFromListing(progName, "Gallery", authorName, function(result) {
            if (result.rc === 'ok') { // already shared!
                //TODO create usefull text at least for german and english.
                MSG.displayInformation({'rc':'error'}, 'GALLERY_SHARED_ALREADY', 'GALLERY_SHARED_ALREADY', progName);
            } else {
                $('#textShareGallery').html(Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY);
                $('#share-with-gallery').data('action', 'add');
                PROGRAM.loadProgramEntity(progName, GUISTATE_C.getUserAccountName(), GUISTATE_C.getUserAccountName(), function(result) {
                    if (result.rc === 'ok') {
                        var progName = row[0];
                        $('#share-with-gallery h3').text(Blockly.Msg.BUTTON_DO_UPLOAD_GALLERY.replace("$", progName));
                        $('#galleryPreview').bootstrapTable("load", new Array(result.program));
                        $('.infoTags').tagsinput();
                        $('#galleryPreview .bootstrap-tagsinput').addClass('galleryTags');
                        $('#galleryPreview').find('.galleryTags>input').attr('readonly', 'true');
                        $('#galleryPreview').find('span[data-role=remove]').addClass('hidden');
                        $('#share-with-gallery').modal("show");
                    }                   
                });
            }
        });
    }

    /**
     * Update rights for all users with which this program is shared, no
     * selected right will remove sharing
     */
    function updateSharedWithUsers() {
        var data = $('#relationsTable').bootstrapTable('getData');
        // start from second row, first is already handled in updateShareWithUser()
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
        $('#progList').find('button[name="refresh"]').trigger('click');
        $('#show-relations').modal("hide");
    }

    /**
     * Update rights for sharing with the gallery
     * 
     */
    function updateShareWithGallery(action) {
        var progName = $('#share-with-gallery').data('progName');
        PROGRAM.shareProgramWithGallery(progName, function(result) {
            if (result.rc === 'ok') {
                LOG.info("share program " + progName + " with Gallery");
                $('#progList').find('button[name="refresh"]').trigger('click');
            }
            MSG.displayInformation(result, result.message, result.message, progName);
        });       
        $('#share-with-gallery').modal("hide");
    }

    /**
     * Share program with another user.
     * 
     * @param row
     *            {Array} - input row
     * @returns {Bool} - false, if user to share with does not exist or user is
     *          identical with owner
     * 
     */

    function updateShareWithUser(row) {
        var data = $('#relationsTable').bootstrapTable('getData');
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
                    MSG.displayInformation(result, result.message, result.message, values.shareWithInput);
                    LOG.info("share program " + row.name + " with '" + values.shareWithInput + " having right '" + right + "'");
                    $('#progList').find('button[name="refresh"]').trigger('click');
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

    var rowStyle = function(row, index) {
        return {
            classes : 'typcn typcn-' + row[2] // the robot typicon as background image
        }
    }

    var eventAddShare = {
        'click #addShare' : function(e, value, row, index) {
            updateShareWithUser(row);
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
});
