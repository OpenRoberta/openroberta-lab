define([ 'require', 'exports', 'log', 'util', 'message', 'comm', 'guiState.controller', 'language.controller', 'galleryList.controller', 'program.model', 'userGroup.model', 'blockly', 'jquery',
        'bootstrap-table' ], function(require, exports, LOG, UTIL, MSG, COMM, GUISTATE_C, LANG, GALLERY_C, PROGRAM, USERGROUP, Blockly, $) {

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
            updateSharedWithUsers();
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
        var shared = null;
        if (!$.isEmptyObject(data[2])) {
            shared = data[2];
        }
        $('#show-relations h3').text(Blockly.Msg.BUTTON_DO_SHARE + ' »' + progName + '«').end();
        // $('#show-relations').find('.modal-header>h3').text(Blockly.Msg.BUTTON_DO_SHARE + ' »' + progName + '«').end();
        $('#relationsTable').bootstrapTable('removeAll');
        if (shared) {
            $.each(shared.sharedWith, function(i, shareObj) {
                if (shareObj.type !== 'User' || shareObj.label !== 'Gallery') {
                    $('#relationsTable').bootstrapTable('insertRow', {
                        index : -1,
                        row : {
                            name : data[0],
                            owner : data[1],
                            sharedWith : shareObj,
                            read : shareObj.right,
                            write : shareObj.right,
                        }
                    });
                }
            });
        }
        // add input row for new user group to share with
        $('#relationsTable').bootstrapTable('insertRow', {
            index : 0,
            row : {
                name : data[0],
                owner : data[1],
                sharedWith : {
                    label: null,
                    type: 'UserGroup',
                    right: 'NONE'
                },
                read : 'READ',
                write : '',
            }
        });
        // add input row for new user to share with
        $('#relationsTable').bootstrapTable('insertRow', {
            index : 0,
            row : {
                name : data[0],
                owner : data[1],
                sharedWith : {
                    label: null,
                    type: 'User',
                    right: 'NONE'
                },
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
    function updateSharedWithUsers(rowIndex) {
        var data = $('#relationsTable').bootstrapTable('getData');
        
        for (var i = 0; i < data.length; i++) {
            if (!isNaN(rowIndex) && i != parseInt(rowIndex)) {
                continue;
            }

            var sharedWith = JSON.parse(JSON.stringify(data[i].sharedWith)),
                $shareLabelInput = false
                updateRowIndex = -1,
                right = 'NONE';
            
            if ($("#checkRead" + i).is(':checked') && (sharedWith.type !== 'User' || !GUISTATE_C.isUserMemberOfUserGroup() || sharedWith.label !== GUISTATE_C.getUserUserGroupOwner())) {
                right = 'READ';
            }
            if ($("#checkWrite" + i).is(':checked')) {
                right = 'WRITE';
            }
            
            if (sharedWith.label === null) {
                var $shareLabelInput = $('#relationsTable tr[data-index="' + i + '"] .shareLabelInput'),
                    shareLabel = $shareLabelInput.val();
                
                if (!shareLabel) {
                    continue;
                }
                
                if (sharedWith.type === 'User') {
                    // new user and owner are the same?
                    if (shareLabel === data[i].owner) {
                        if (!isNaN(rowIndex)) {
                            UTIL.showMsgOnTop("ORA_USER_TO_SHARE_SAME_AS_LOGIN_USER");
                            return;
                        }
                        continue;
                    }
                    
                    updateRowIndex = data.map(function(row) {
                        return row.sharedWith !== null ? row.sharedWith.label : '';
                    }).indexOf(shareLabel);
                    
                    if (updateRowIndex >= 0) {
                        sharedWith = JSON.parse(JSON.stringify(data[updateRowIndex].sharedWith));
                    }
                }
                
                sharedWith.label = shareLabel;
            }
            
            if (right !== sharedWith.right) {
                sharedWith.right = right;
                (function (row, shareObj, $shareLabelInput, updateRowIndex) {
                    PROGRAM.shareProgram(row.name, shareObj, function(result) {
                        if (result.rc === 'ok') {
                            if ($shareLabelInput) {
                                $shareLabelInput.val('');
                                if (shareObj.type === 'UserGroup') {
                                    $shareLabelInput.find('option[value="' + shareObj.label + '"]').remove();
                                }
                                if (updateRowIndex < 0) {
                                    $('#relationsTable').bootstrapTable('insertRow', {
                                        index : 2,
                                        row : {
                                            name : row.name,
                                            owner : row.owner,
                                            sharedWith : shareObj,
                                            read : shareObj.right,
                                            write : shareObj.right,
                                        }
                                    });
                                } else {
                                    $('#relationsTable').bootstrapTable('updateRow', {
                                        index : updateRowIndex,
                                        row : {
                                            name : row.name,
                                            owner : row.owner,
                                            sharedWith : shareObj,
                                            read : shareObj.right,
                                            write : shareObj.right,
                                        }
                                    });
                                }
                            }
                            MSG.displayMessage(result.message, "TOAST", shareObj.label);
                            LOG.info("share program " + row.name + " with '" + shareObj.label + "'(" + shareObj.type + ") having right '" + shareObj.right + "'");                      
                            $('#progList').find('button[name="refresh"]').trigger('click');                    
                        } else {
                            UTIL.showMsgOnTop(result.message);
                        }
                    });
                })(data[i], sharedWith, $shareLabelInput, updateRowIndex);
            }
        }
        if (isNaN(rowIndex)) {
            $('#show-relations').modal("hide");
        }
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

    var rowStyle = function(row, index) {
        return {
            classes : 'typcn typcn-' + row[2] // the robot typicon as background image
        }
    }

    var eventAddShare = {
        'click .addShare' : function(e, value, row, index) {
            updateSharedWithUsers(index);
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
        if (row.sharedWith.label === null && row.sharedWith.type === 'UserGroup' || row.sharedWith.type === 'User' && GUISTATE_C.isUserMemberOfUserGroup() && GUISTATE_C.getUserUserGroupOwner() === row.sharedWith.label) {
            return '<input type="checkbox" id="checkRead' + index + '" checked disabled>';
        }
        if (value === 'READ') {
            return '<input type="checkbox" id="checkRead' + index + '" checked>';
        }
        return '<input type="checkbox" id="checkRead' + index + '">';
    }

    var formatWrite = function(value, row, index) {
        if (row.sharedWith.type === 'UserGroup') {
            return '<input type="checkbox" id="checkWrite' + index + '" disabled>';
        }
        if (value === 'WRITE') {
            return '<input type="checkbox" id="checkWrite' + index + '" checked>';
        }
        return '<input type="checkbox" id="checkWrite' + index + '">';
    }

    var formatSharedWith = function(value, row, index) {
        if (value === null || typeof value !== 'object') {
            error.log('unknown share format "' + (typeof value) + '"');
            
            return '';
        }
        if (value.label === null) {
            var typeLabel = '';
            if (value.type === 'User') {
                var $html = $('<div class="input-group">' 
                        + '<label class="input-group-btn" for="shareWithUserInput">' 
                            + '<button type="button" style="height:34px" class="btn disabled editor">' 
                                + '<i class="typcn typcn-user"></i>' 
                            + '</button>' 
                        + '</label>' 
                        + '<span class="input-group-btn">' 
                            + '<button class="addShare btn" type="button" style="height: 34px">'
                                + '<i class="typcn typcn-plus"></i>' 
                            + '</button>' 
                        + '</span>' 
                        + '<input class="shareLabelInput form-control" type="text" name="user.account" lkey="Blockly.Msg.SHARE_WITH_USER" data-translation-targets="placeholder"/>' 
                    + '</div>');
                LANG.translate($html);
                return $('<div></div>').append($html).html();
            }
            if (value.type === 'UserGroup') {
                if (!GUISTATE_C.isUserMemberOfUserGroup()) {
                    USERGROUP.loadUserGroupList(function(data) {
                        if (data.rc == 'ok' && data.userGroups && data.userGroups.length > 0) {
                            var existingUserGroupNames = $('#relationsTable').bootstrapTable('getData').filter(function(dataEntry) {
                                    return dataEntry.sharedWith && dataEntry.sharedWith.type === 'UserGroup';
                                }).map(function(dataEntry) {
                                    return dataEntry.sharedWith.label;
                                }),
                                $td = $('#relationsTable tr[data-index="' + index + '"] script').parent(),
                                html;
                            
                            html = '<div class="input-group" title="" data-original-title lkey="Blockly.Msg.SHARE_WITH_USERGROUP" data-translation-targets="title data-original-title">' 
                                    + '<label class="input-group-btn" for="shareWithUserGroupInput">' 
                                        + '<button type="button" style="height:34px" class="btn disabled editor">' 
                                            + '<i class="typcn typcn-group"></i>' 
                                        + '</button>' 
                                    + '</label>' 
                                    + '<span class="input-group-btn">' 
                                        + '<button class="addShare btn" type="button" style="height: 34px">'
                                            + '<i class="typcn typcn-plus"></i>' 
                                        + '</button>' 
                                    + '</span>' 
                                    + '<select class="shareLabelInput form-control" name="userGroup.name">'
                                        + '<option value="" lkey="Blockly.Msg.SHARE_WITH_USERGROUP" data-translation-targets="html"></option>'
                                        + data.userGroups.filter(function(userGroup) {
                                            return existingUserGroupNames.indexOf(userGroup.name) === -1;
                                        }).reduce(function(carry, userGroup) {
                                            return carry + '<option value="' + userGroup.name + '">' + userGroup.name + '</option>';
                                        }, '')
                                    + '</select>'
                                + '</div>';
                            
                            $td.html(html);
                            LANG.translate($td);
                            Object.keys(eventAddShare).forEach(function (eventKey) {
                                if (!eventAddShare.hasOwnProperty(eventKey)) {
                                    return;
                                }
                                
                                var eventInformation = eventKey.split(' ', 2);
                                $td.find(eventInformation[1]).on(eventInformation[0], function(e) {
                                    eventAddShare[eventKey](e, value, row, index);
                                });
                            });
                            $td.parent().show();
                        }
                    });
                }
                return '<script>$(\'#relationsTable\').find(\'tr[data-index="' + index + '"]\').hide();</script>';
            }
            
            error.log('unknown share type');
            return '';
        }
        
        var typeIconClass = 'warning-outline';
        
        if (value.type === 'User') {
            typeIconClass = 'user';
        } else if (value.type === 'UserGroup') {
            typeIconClass = 'group';
        }
        
        return '<span class="typcn typcn-' + typeIconClass + '"></span> <span class="value">' + value.label + '</span>';
    }
});
