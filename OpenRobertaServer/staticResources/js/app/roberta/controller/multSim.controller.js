/**
 * @fileOverview Multiple Simulate robots
 * @author Akshat Khare <akshat.khare08@gmail.com>
 */
/**
 * Controller for multiple simulation part of the project
 */
define(['exports', 'message', 'util', 'progList.model', 'program.controller', 'program.model', 'guiState.controller', 'simulation.simulation', 'jquery'], function(
    exports, MSG, UTIL, PROGLIST, PROG_C, PROGRAM_M, GUISTATE_C, SIM, $) {

    function showListProg() {
        PROGLIST.loadProgList(function(result) {
            if (result.rc === "ok" && result.programNames.length > 1) {
                $("#multipleRobotsTable").bootstrapTable('destroy'); //refreshing the table
                var dataarr = []; //Array having data to be displayed in table shown
                var robottype = GUISTATE_C.getRobot();
                result.programNames.forEach(function(item, i, oriarray) {
                    dataarr.push({
                        name: item[0],
                        robot: robottype,
                        creator: item[1],
                        date: item[4]
                    });
                });
                $('#multipleRobotsTable').bootstrapTable({
                    height: 400,
                    sortName: "name",
                    toggle: "multipleRobotsTable",
                    iconsPrefix: 'typcn',
                    search: true,
                    icons: {
                        paginationSwitchDown: 'typcn-document-text',
                        paginationSwitchUp: 'typcn-book',
                        refresh: 'typcn-refresh',
                    },
                    pagination: 'true',
                    buttonsAlign: 'right',
                    resizable: 'true',

                    columns: [{
                        field: 'name',
                        title: "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>" + (Blockly.Msg.DATATABLE_PROGRAM_NAME || "Name des Programms") + "</span>",
                        sortable: true
                    }, {
                        field: 'creator',
                        title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || "Erzeugt von") + "</span>",
                        sortable: true
                    }, {
                        field: 'date',
                        title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || "Erzeugt am") + "</span>",
                        sortable: true,
                        formatter: UTIL.formatDate
                    }, {
                        checkbox: true,
                        valign: 'middle',
                    }],
                    data: dataarr
                });
                $("#loadMultipleSimPrograms").off();
                $("#loadMultipleSimPrograms").on("click", function() {
                    var selections = $("#multipleRobotsTable").bootstrapTable('getSelections');
                    var selectedprograms = [];
                    for (var i = 0; i < selections.length; i++) {
                        var tempfind = result.programNames.filter(function(ele) {
                            return selections[i].name === ele[0];
                        })[0];
                        selectedprograms.push(tempfind);
                    }
                    var extractedprograms = [];
                    numberOfPrograms = 0;
                    selectedprograms.forEach(function(item, i, oriarray) {
                        PROGRAM_M.loadProgramFromListing(item[0], item[1], item[3], function(dat) {
                            if (dat.rc != "ok") {
                                //TODO
                                alert("failed loading program for item " + i + ", check console");
                                console.log("failed item is ", item);
                            }
                            dat.savedName = item[0];
                            extractedprograms[i] = dat;
                            var xmlTextProgram = dat.progXML;
                            var configName;
                            var xmlConfigText;
                            if (dat.configName === undefined) {
                                if (dat.confXML === undefined) {
                                    configName = undefined;
                                    xmlConfigText = undefined;
                                } else {
                                    configName = undefined;
                                    xmlConfigText = dat.confXML;
                                }
                            } else {
                                configName = dat.configName;
                                xmlConfigText = dat.confXML;
                            }
                            var language = GUISTATE_C.getLanguage();
                            PROGRAM_M.runInSim(dat.savedName, configName, xmlTextProgram, xmlConfigText, language, function(result) {
                                numberOfPrograms++;
                                if (result.rc === "ok") {
                                    for (var resultProp in result)
                                        extractedprograms[i][resultProp] = result[resultProp];
                                } else {
                                    MSG.displayInformation(result, "", result.message, "");
                                }
                                if (selectedprograms.length === numberOfPrograms) {
                                    if (extractedprograms.length >= 1) {
                                        simulateMultiple(extractedprograms);
                                    } else {
                                        $("#showMultipleSimPrograms").modal('hide');
                                    }
                                }
                            });
                        });
                    });
                });
                $("#showMultipleSimPrograms").modal("show");
            } else {
                if (result.rc === "ok") {
                    result.rc = "error";
                }
                MSG.displayInformation(result, "", 'POPUP_MULTROBOTS_NOPROGRAMS', "");
            }
        });
    }
    exports.showListProg = showListProg;

    function simulateMultiple(programs) {
        $("#showMultipleSimPrograms").modal('hide');
        const INITIAL_WIDTH = 0.5;
        SIM.init(programs, true, GUISTATE_C.getRobotGroup());
        $('#debugMode, #simControlBreakPoint, #simControlStepOver, #simControlStepInto, #simVariables').hide();
        $(".sim").removeClass('hide');
        $('#simButtonsCollapse').collapse({
            'toggle': false
        });
        if ($("#blockly").hasClass("rightActive") && !$("#simDiv").hasClass("rightActive")) {
            $('#blockly').closeRightView(function() {
                $('#blockly').openRightView('sim', INITIAL_WIDTH);
            });
        } else if (!$("#simDiv").hasClass("rightActive")) {
            $('#blockly').openRightView('sim', INITIAL_WIDTH);
        }
    }
});
