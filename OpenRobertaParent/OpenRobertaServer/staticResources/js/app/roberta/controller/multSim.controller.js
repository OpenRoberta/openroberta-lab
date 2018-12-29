/**
 * @fileOverview Multiple Simulate robots
 * @author Akshat Khare <akshat.khare08@gmail.com>
 */
/**
 * Controller for multiple simulation part of the project
 */
define([ 'exports', 'util', 'progList.model', 'program.controller', 'program.model', 'guiState.controller', 'guiState.model', 'simulation.simulation',
        'user.model', 'jquery' ], function(exports, UTIL, PROGLIST, PROG_C, PROGRAM_M, GUISTATE_C, GUISTATE_M, SIM, USER, $) {

    function showListProg() {
        PROGLIST.loadProgList(function(result) {
            if (result.rc === "ok") {
                $("#mtable").bootstrapTable('destroy'); //refreshing the table
                $("#simModal .btn-primary").off('click'); // deleting any click event attached to this button 
                var dataarr = []; //Array having data to be displayed in table shown
                var programsparsed = 0; //iterator for the synchronously running for loops
                var robottype = GUISTATE_M.gui.robot;
                if (result.programNames.length === 0) {
                    $("#voidList").show(); // error reporting in the modal itself
                    $("#voidList").text("There is no saved program to be simulated. Please save the program and then run this feature"); //error message
                    $("#simModal .btn-primary").hide(); //hiding the run selected button
                } else {
                    $("#voidList").hide(); // hiding the error reporting part
                    $("#simModal .btn-primary").show(); // showing the run selected button if hidden
                }
                result.programNames.forEach(function(item, i, oriarray) {
                    dataarr.push({
                        name : item[0],
                        robot : robottype,
                        creator : item[1],
                        date : item[4]
                    });
                    programsparsed++;
                    if (programsparsed === oriarray.length) { //the end of loop
                        $('#mtable').bootstrapTable({
                            height : 400,
                            sortName : "name",
                            toggle : "mtable",
                            iconsPrefix : 'typcn',
                            search : true,
                            icons : {
                                paginationSwitchDown : 'typcn-document-text',
                                paginationSwitchUp : 'typcn-book',
                                refresh : 'typcn-refresh',
                            },
                            pagination : 'true',
                            buttonsAlign : 'right',
                            resizable : 'true',

                            columns : [ {
                                field : 'name',
                                title : 'Program Name',
                                sortable : true
                            }, {
                                field : 'creator',
                                title : 'Creator',
                                sortable : true
                            }, {
                                field : 'date',
                                title : 'Creation Date',
                                formatter : UTIL.formatDate,
                                sortable : true
                            }, {
                                checkbox : true,
                                valign : 'middle',
                            } ],
                            data : dataarr
                        });
                        $("#simModal .btn-primary").show();
                        $("#simModal .btn-primary").on("click", function() {
                            var selections = $("#mtable").bootstrapTable('getSelections');
                            var selectedprograms = [];
                            for (var i = 0; i < selections.length; i++) {
                                var tempfind = oriarray.find(function(ele) {
                                    var thisarrm = oriarray;
                                    return selections[i].name === ele[0];
                                });
                                selectedprograms.push(tempfind);
                            }
                            var extractedprograms = [];
                            var numprogs = selectedprograms.length;
                            var numloadprogs = 0;
                            selectedprograms.forEach(function(item, i, oriarray) {
                                PROGRAM_M.loadProgramFromListing(item[0], item[1], item[3], function(dat) {
                                    if (dat.rc != "ok") {
                                        //TODO
                                        alert("failed loading program for item " + i + ", check console");
                                        console.log("failed item is ", item);
                                    }
                                    dat.savedName = item[0];
                                    extractedprograms[i] = dat;
                                    numloadprogs++;
                                    if (numloadprogs === oriarray.length) {
                                        var jslist = [];
                                        var programsfetched = 0
                                        extractedprograms.forEach(function(item, i, oriarray) {

                                            var xmlTextProgram = item.programText;
                                            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
                                            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
                                            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

                                            var language = GUISTATE_C.getLanguage();

                                            PROGRAM_M.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(
                                                    result) {
                                                if (result.rc === "ok") {
                                                    extractedprograms[i]["result"] = result;
                                                    programsfetched++;
                                                    if (programsfetched === oriarray.length) {
                                                        simulateMultiple(extractedprograms);
                                                    }
                                                } else {
                                                    MSG.displayInformation(result, "", result.message, "");
                                                    //TODO
                                                    alert("failed loading js for item " + i + " check console");
                                                    console.log("failed loading js for item ", item);
                                                }
                                            });

                                        });
                                    }
                                });
                            });
                        });
                    }
                });
            } else {
                // the user is not logged in, this code might never be executed until the user deletes the disabled class and click the multiplication button
                $("#mtable").bootstrapTable('destroy'); //destroy the table if still present
                $("#simModal .btn-primary").hide();
            }
        });
    }
    exports.showListProg = showListProg;

    function simulateMultiple(programs) {
        $("#simModal").modal('toggle');
        const INITIAL_WIDTH = 0.5;
        SIM.initMultiple(programs, true, GUISTATE_C.getRobotGroup());
        $(".sim").removeClass('hide');
        $('#simButtonsCollapse').collapse({
            'toggle' : false
        });
        $('#blockly').openRightView('sim', INITIAL_WIDTH);
    }
});
