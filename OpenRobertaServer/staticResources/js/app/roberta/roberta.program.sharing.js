define([ 'exports', 'message', 'log', 'util', 'roberta.user-state', 'roberta.program', 'rest.program', 'roberta.robot', 'jquery', 'jquery-validate' ],
        function(exports, MSG, LOG, UTIL, userState, ROBERTA_PROGRAM, PROGRAM, ROBERTA_ROBOT, $) {

            function init() {
                $('#share-program-form').onWrap('submit', function(e) {
                    e.preventDefault();
                    shareProgramsFromListing();
                });

                // show relations of program
                $('#shareFromListing').onWrap('click', function() {
                    var $programRow = $('#programNameTable .selected');
                    if ($programRow.length > 0) {
                        var shared = $programRow[0].children[2].textContent;
                        if (shared === 'X' || shared === '-') {
                            var programName = $programRow[0].children[0].textContent;
                            var headShare = Blockly.Msg.BUTTON_DO_SHARE + ' (' + programName + ')';
                            $('#headShare').text(headShare);
                            $('#programShareWith :not(btn)').val('');
                            $('#read').prop("checked", true);
                            $('#write').prop("checked", false);
                            PROGRAM.refreshProgramRelationsList(programName, showRelations);
                        }
                    }
                }, 'show relations of program');
            }
            exports.init = init;

            /**
             * Share the programs that were selected in program list
             */
            function shareProgramsFromListing() {
                // set rights for the user in the text input field 
                var shareWith = $('#programShareWith').val();
                var alreadyShared = false;
                $("#relationsTable tbody tr").each(function(index) {
                    var $this = $(this);
                    var cols = $this.children("td");
                    var userToShareWith = cols.eq(2).text();
                    // your cannot share programs twice
                    if (userToShareWith === shareWith) {
                        alreadyShared = true;
                    }
                })
                if (shareWith === userState.name || alreadyShared) {
                    // you cannot share programs with yourself
                    MSG.displayInformation({
                        rc : "not ok"
                    }, "", "ORA_USER_TO_SHARE_SAME_AS_LOGIN_USER");
                    //MSG.displayMessage("ORA_USER_TO_SHARE_SAME_AS_LOGIN_USER", "POPUP", "");
                } else {
                    if (shareWith !== '') {
                        var right = $('#write:checked').val();
                        if (!right) {
                            right = $('#read:checked').val();
                        }
                        if (right) {
                            var $programRow = $('#programNameTable .selected');
                            var programName = $programRow[0].children[0].textContent;
                            LOG.info("share program " + programName + " with '" + shareWith + " having right '" + right + "'");
                            PROGRAM.shareProgram(programName, shareWith, right, function(result) {
                                MSG.displayInformation(result, "", result.message);
                                if (result.rc === 'ok') {
                                    $('#show-relations').modal('hide');
                                    PROGRAM.refreshList(ROBERTA_PROGRAM.showPrograms);
                                }
                            });
                        }
                    } else {
                        // set rights as set by user in relations table
                        $("#relationsTable tbody tr").each(function(index) {
                            var $this = $(this);
                            var cols = $this.children("td");
                            var programName = cols.eq(0).text();
                            var userToShareWith = cols.eq(2).text();
                            if (userToShareWith !== '') {
                                var readRight = cols.eq(3).children("input:checked").val();
                                var writeRight = cols.eq(4).children("input:checked").val();
                                var right = 'NONE';
                                if (writeRight === 'WRITE') {
                                    right = writeRight;
                                } else if (readRight === 'READ') {
                                    right = readRight;
                                }
                                LOG.info("share program " + programName + " with '" + userToShareWith + " having right '" + right + "'");
                                PROGRAM.shareProgram(programName, userToShareWith, right, function(result) {
                                    if (result.rc === 'ok') {
                                        UTIL.response(result);
                                        ROBERTA_ROBOT.setState(result);
                                        if (right === 'NONE') {
                                            MSG.displayInformation(result, "MESSAGE_RELATION_DELETED", result.message, programName);
                                        }
                                        $('#show-relations').modal('hide');
                                        PROGRAM.refreshList(ROBERTA_PROGRAM.showPrograms);
                                    } else {
                                        MSG.displayInformation(result, "", result.message);
                                    }
                                });
                            }
                        });
                    }
                }
                $('#programShareWith').val('');
            }

            /**
             * Display relations of programs in a table
             * 
             * @param {result}
             *            result object of server call
             */
            function showRelations(result) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    var $table = $('#relationsTable').dataTable();
                    $table.fnClearTable();
                    if (result.relations.length > 0) {
                        $table.fnAddData(result.relations);
                    }
                    // This is a WORKAROUND for a known bug in Jquery-datatables:
                    // If scrollY is set the column headers have the wrong width,
                    // because the browser is not able to calculate them correctly. So
                    // after a while (in this case 200 ms) the columns have to be readjusted.
                    setTimeout(function() {
                        $table.fnAdjustColumnSizing();
                    }, 200);
                    $("#show-relations").modal('show');
                }
            }
            exports.showRelations = showRelations;
        });
