define([ 'require', 'exports', 'progList.controller', 'progDelete.controller', 'progShare.controller', 'simulation.simulation', 'roberta.language',
        'roberta.navigation', 'log', 'util', 'comm', 'roberta.brick-configuration', 'roberta.program', 'roberta.program.sharing', 'roberta.user-state',
        'roberta.user', 'roberta.robot', 'roberta.brickly', 'blocks', 'blocks-msg', 'enjoyHint', 'jquery', 'jquery-cookie', 'jquery-ui', 'datatables' ],
        function(require, exports, PROGLIST_CONTROLLER, PROGDELETE_CONTROLLER, PROGSHARE_CONTROLLER, SIM, LANGUAGE, ROBERTA_NAVIGATION, LOG, UTIL, COMM,
                ROBERTA_BRICK_CONFIGURATION, ROBERTA_PROGRAM, ROBERTA_PROGRAM_SHARING, userState, ROBERTA_USER, ROBERTA_ROBOT, BRICKLY, Blockly, Blockly,
                EnjoyHint, $) {

            var id;

            /**
             * Select row in programs-/configurations-/relations-datatable
             */
            var start;
            function selectionFn(event) {
                $(event.target.parentNode).toggleClass('selected');
                var rowIndex = event.currentTarget.rowIndex;

                if (!start) {
                    start = rowIndex;
                }

                // Shift-Click ?
                if (event.shiftKey) {
                    var end = rowIndex;
                    $('#programNameTable tbody tr').removeClass("selected");
                    for (i = Math.min(start, end); i <= Math.max(start, end); i++) {
                        if (!$(event.target.parentNode.parentNode.childNodes).eq(i).hasClass("selected")) {
                            $(event.target.parentNode.parentNode.childNodes).eq(i).addClass("selected");
                        }
                    }

                    // Clear browser text selection mask
                    if (window.getSelection) {
                        if (window.getSelection().empty) { // Chrome
                            window.getSelection().empty();
                        } else if (window.getSelection().removeAllRanges) { // Firefox
                            window.getSelection().removeAllRanges();
                        }
                    } else if (document.selection) { // IE?
                        document.selection.empty();
                    }
                }
                start = rowIndex;
            }

//            /**
//             * Initialize table of programs
//             */
//            function initProgramNameTable() {
//                var columns = [ {
//                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>Name des Programms</span>",
//                    "sClass" : "programs"
//                }, {
//                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
//                    "sClass" : "programs"
//                }, {
//                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_SHARED'>Geteilt</span>",
//                    "sClass" : "programs"
//                }, {
//                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
//                    "sClass" : "programs"
//                }, {
//                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
//                    "sClass" : "programs"
//                }, {
//                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung mit Hundertstel Sekunden</span>",
//                    "sClass" : "programs hidden"
//                } ];
//                var $programs = $('#programNameTable');
//
//                var oTable = $programs.dataTable({
//                    "sDom" : '<lip>t<r>',
//                    "aaData" : [],
//                    "aoColumns" : columns,
//                    "aoColumnDefs" : [ { // format fields
//                        "aTargets" : [ 3, 4 ], // indexes of columns to be formatted
//                        "sType" : "date-de",
//                        "mRender" : function(data) {
//                            return UTIL.formatDate(data);
//                        }
//                    }, {
//                        "aTargets" : [ 2 ], // indexes of columns to be formatted
//                        "mRender" : function(data, type, row) {
//                            if (data === 'WRITE') {
//                                var returnval = "<span lkey='Blockly.Msg.POPUP_SHARE_WRITE'>" + Blockly.Msg.POPUP_SHARE_WRITE + "</span>";
//                            } else if (data === "READ") {
//                                var returnval = "<span lkey='Blockly.Msg.POPUP_SHARE_READ'>" + Blockly.Msg.POPUP_SHARE_READ + "</span>";
//                            } else if (data === true) {
//                                var returnval = "<span lkey='Blockly.Msg.POPUP_SHARE_READ'>X</span>";
//                            } else if (data === false) {
//                                var returnval = "<span lkey='Blockly.Msg.POPUP_SHARE_READ'>-</span>";
//                            }
//                            return returnval;
//                        }
//                    } ],
//                    "bJQueryUI" : true,
//                    "oLanguage" : {
//                        "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'></span>" // Die Tabelle ist leer
//                    },
//                    "fnDrawCallback" : function() {
//                    },
//                    "scrollY" : UTIL.calcDataTableHeight(),
//                    "scrollCollapse" : true,
//                    "paging" : false,
//                    "bInfo" : false
//                });
//
//                $(window).resize(function() {
//                    var oSettings = oTable.fnSettings();
//                    oSettings.oScroll.sY = UTIL.calcDataTableHeight();
//                    oTable.fnDraw(false); // redraw the table
//                });
//
//                $('#programNameTable tbody').onWrap('click', 'tr', selectionFn);
//                $('#programNameTable tbody').onWrap('dblclick', 'tr', function(event) {
//                    selectionFn(event);
//                    $('#loadFromListing').click();
//                });
//            }

            /**
             * Initialize configurations table
             */
            function initConfigurationNameTable() {
                var columns = [ {
                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CONFIGURATION_NAME'>Name der Konfiguration</span>",
                    "sClass" : "configurations"
                }, {
                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
                    "sClass" : "configurations"
                }, {
                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Erzeugt am</span>",
                    "sClass" : "configurations"
                }, {
                    "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
                    "sClass" : "configurations"
                } ];
                var $configurations = $('#configurationNameTable');

                var oTable = $configurations.dataTable({
                    "sDom" : '<lip>t<r>',
                    "aaData" : [],
                    "aoColumns" : columns,
                    "aoColumnDefs" : [ { // format date fields
                        "aTargets" : [ 2, 3 ], // indexes of columns to be formatted
                        "sType" : "date-de",
                        "mRender" : function(data) {
                            return UTIL.formatDate(data);
                        }
                    } ],
                    "bJQueryUI" : true,
                    "oLanguage" : {
                        "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'></span>" //Die Tabelle ist leer
                    },
                    "fnDrawCallback" : function() {
                    },
                    "scrollY" : UTIL.calcDataTableHeight(),
                    "scrollCollapse" : true,
                    "paging" : false,
                    "bInfo" : false
                });

                $(window).resize(function() {
                    var oSettings = oTable.fnSettings();
                    oSettings.oScroll.sY = UTIL.calcDataTableHeight();
                    oTable.fnDraw(false); // redraw the table
                });

                $('#configurationNameTable tbody').onWrap('click', 'tr', selectionFn);
                $('#configurationNameTable tbody').onWrap('dblclick', 'tr', function(event) {
                    selectionFn(event);
                    $('#loadConfigurationFromListing').click();
                });
            }

//    /**
//     * Initialize relations table
//     */
//    function initRelationsTable() {
//        var columns = [ {
//            "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>Name des Programms</span>",
//            "sClass" : "relations hidden"
//        }, {
//            "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Erzeugt von</span>",
//            "sClass" : "relations hidden"
//        }, {
//            "sTitle" : "<span lkey='Blockly.Msg.DATATABLE_SHARED_WITH'>Geteilt mit</span>",
//            "sClass" : "relations"
//        }, {
//            "sTitle" : "<span lkey='Blockly.Msg.POPUP_SHARE_READ'>Lesen</span>",
//            "sClass" : "relations"
//        }, {
//            "sTitle" : "<span lkey='Blockly.Msg.POPUP_SHARE_WRITE'>Schreiben</span>",
//            "sClass" : "relations"
//        } ];
//        var $relations = $('#relationsTable');
//
//        var oTable = $relations.dataTable({
//            "sDom" : '<lip>t<r>',
//            "aaData" : [],
//            "aoColumns" : columns,
//            "aoColumnDefs" : [ { // format language dependant fields
//                "aTargets" : [ 3 ], // indexes of columns to be formatted
//                "mRender" : function(data, type, row) {
//                    var checked = '';
//                    if (row[4] === 'WRITE' || data === 'READ') {
//                        checked = 'checked';
//                    }
//                    var returnval = "<td><input class='readRight' type='checkbox' name='right' value='READ' " + checked + "></td>";
//                    return returnval;
//                }
//            }, {
//                "aTargets" : [ 4 ], // indexes of columns to be formatted
//                "mRender" : function(data, type, row) {
//                    var checked = '';
//                    if (data === 'WRITE') {
//                        checked = 'checked';
//                    }
//                    var returnval = "<td><input class='writeRight' type='checkbox' name='right' value='WRITE' " + checked + "></td>";
//                    return returnval;
//                }
//            } ],
//            "bJQueryUI" : true,
//            "oLanguage" : {
//                "sEmptyTable" : "<span lkey='Blockly.Msg.DATATABLE_EMPTY_TABLE'></span>" // Die Tabelle ist leer
//            },
//            "fnDrawCallback" : function() {
//            },
//            "scrollY" : UTIL.calcDataTableHeight(),
//            "scrollCollapse" : true,
//            "paging" : false,
//            "bInfo" : false
//        });
//
//        $(window).resize(function() {
//            var oSettings = oTable.fnSettings();
//            oSettings.oScroll.sY = UTIL.calcDataTableHeight();
//            oTable.fnDraw(false); // redraw the table
//        });
//
//        $('#relationsTable tbody').onWrap('click', 'tr', selectionFn);
//        $('#relationsTable_wrapper').css('margin', 0);
//    }

            /**
             * Initialize popups
             */
            function initPopups() {

                $('.cancelPopup').onWrap('click', function() {
                    $('.ui-dialog-titlebar-close').click();
                });

                $('#about-join').onWrap('click', function() {
                    $('#show-about').modal('hide');
                });

                ROBERTA_PROGRAM_SHARING.init();
                ROBERTA_USER.initUserForms();
                ROBERTA_ROBOT.initRobotForms();
                ROBERTA_PROGRAM.initProgramForms();
                ROBERTA_BRICK_CONFIGURATION.initBrickConfigurationForms();

                var target = document.location.hash.split("&");

                if (target[0] === "#forgotPassword") {
                    $('#passOld').val(target[1]);
                    $('#resetPassLink').val(target[1]);
                    $('#show-startup-message').modal('hide');
                    ROBERTA_USER.showResetPassword();
                }
            }

            /**
             * Initialize tabs
             */
            function initTabs() {
                PROGLIST_CONTROLLER.init();
                PROGDELETE_CONTROLLER.init();
                PROGSHARE_CONTROLLER.init();
                $('#tabProgList').onWrap('show.bs.tab', function() {
                    ROBERTA_NAVIGATION.beforeActivateProgList();
                });
                $('#tabConfList').onWrap('show.bs.tab', function() {
                    ROBERTA_NAVIGATION.beforeActivateConfList();
                });
                ROBERTA_NAVIGATION.initNavigation();
            }

            /**
             * Initialize logging
             */
            function initLogging() {
                $('#clearLog').onWrap('click', function() {
                    $('#log li').remove();
                }, 'clear LOG list');
            }

            /**
             * Regularly ping the server to keep status information up-to-date
             */
            function pingServer() {
                if (userState.doPing) {
                    COMM.ping(function(result) {
                        ROBERTA_ROBOT.setState(result);
                    });
                }
            }

            /**
             * Handle server errors
             */
            function handleServerErrors() {
                // TODO more?        
                LOG.info('Server is not available or network is down');
                userState.doPing = false;
                $('#message').attr('lkey', Blockly.Msg.SERVER_NOT_AVAILABLE);
                $('#message').html(Blockly.Msg.SERVER_NOT_AVAILABLE);
                $('#show-message').on('hidden.bs.modal', function(e) {
                    $("#show-message").modal("show");
                });
                $("#show-message").modal("show");
            }

            /**
             * Wait for all blocklies ready.
             * 
             */
            function initBlockly() {
                var language = LANGUAGE.initializeLanguages();
                LANGUAGE.switchLanguage(language, true);
            }

            /**
             * Initializations
             */
            function init() {
                // Pace.start();
                COMM.setErrorFn(handleServerErrors);
                initLogging();
                userState.initUserState();
                $('#tabProgramName').text(userState.program);
                $('#tabConfigurationName').text(userState.configuration);
                initTabs();
                initPopups();
                ROBERTA_NAVIGATION.initHeadNavigation();
                ROBERTA_NAVIGATION.setHeadNavigationMenuState('logout');
                UTIL.initDataTables();
                //initProgramNameTable();
                initConfigurationNameTable();
                // initRelationsTable();
                COMM.json("/toolbox", {
                    "cmd" : "loadT",
                    "name" : userState.toolbox,
                    "owner" : " "
                }, function(result) {
                    ROBERTA_PROGRAM.injectBlockly(result);
                    ROBERTA_ROBOT.initRobot();
                    initBlockly();
                    BRICKLY.init();
                    $(".pace").fadeOut(1000, function() {
                        $(".cover").fadeOut(500);
                        if (!$.cookie("OpenRoberta_hideStartUp")) {
                            $("#show-startup-message").modal("show");
                        }
                    });
                });

                $('#menuTabProgram').parent().addClass('disabled');
                $('#tabProgram').addClass('tabClicked');
                $('#head-navigation-configuration-edit').css('display', 'none');

                // Workaround to set the focus on input fields with attribute 'autofocus'
                $('.modal').on('shown.bs.modal', function() {
                    $(this).find('[autofocus]').focus();
                });

                $(window).on('beforeunload', function(e) {
                    if (!userState.programSaved || !userState.configurationSaved) {
                        if (userState.id === -1) {
                            // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD']);
                            return Blockly.Msg.POPUP_BEFOREUNLOAD;
                        } else {
                            // Maybe a Firefox-Problem?                alert(Blockly.Msg['POPUP_BEFOREUNLOAD_LOGGEDIN']);
                            return Blockly.Msg.POPUP_BEFOREUNLOAD_LOGGEDIN;
                        }
                    }
                });

                $('[rel="tooltip"]').tooltip({
                    placement : "right"
                });

                UTIL.checkVisibility(function() {
                    var visible = UTIL.checkVisibility();
                    LOG.info("this tab visible: " + visible);
                    if (!visible) {
                        SIM.setPause(true);
                        // TODO do more?
                    }
                });

                var ping = setInterval(function() {
                    pingServer()
                }, 3000);

            }
            exports.init = init;
        });
