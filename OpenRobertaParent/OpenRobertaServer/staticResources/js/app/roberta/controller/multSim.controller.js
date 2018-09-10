/**
 * @fileOverview Multiple Simulate robots
 * @author Akshat Khare <akshat.khare08@gmail.com>
 */
/**
 * Controller for multiple simulation part of the project
 */
define(['exports','util', 'progList.model','program.controller', 'program.model','guiState.controller','guiState.model','simulation.simulation','user.model', 'jquery'],
        function(exports, UTIL, PROGLIST,PROG_C, PROGRAM_M,GUISTATE_C, GUISTATE_M, SIM, USER,  $){
    function init(){
        //currently for debugging purpose logged in a default user
//        debugger;
   //un comment following lines to switch default login on     
//        USER.login("a","123456",function(result){
//            if (result.rc === "ok") {
//                GUISTATE_C.setLogin(result);
//                if (result.userId === 1) {
//                    $('#menuAddStatusTextWrap').removeClass('hidden');
//                }
//            }
//            MSG.displayInformation(result, "MESSAGE_USER_LOGIN", result.message, GUISTATE_C.getUserName());
////            debugger;
//        });
    }
    exports.init = init;
    
    function showListProg(){
        PROGLIST.loadProgList(function(result){
            if(result.rc === "ok"){
                $("#mtable").bootstrapTable('destroy'); //refreshing the table
                $("#simModal .btn-primary").off('click'); // deleting any click event attached to this button 
                var dataarr = []; //Array having data to be displayed in table shown
                var programsparsed =0; //iterator for the synchronously running for loops
                var robottype = GUISTATE_M.gui.robot;
                if(result.programNames.length ===0){
                    $("#voidList").show(); // error reporting in the modal itself
                    $("#voidList").text("There is no saved program to be simulated. Please save the program and then run this feature"); //error message
                    $("#simModal .btn-primary").hide(); //hiding the run selected button
                }else{
                    $("#voidList").hide(); // hiding the error reporting part
                    $("#simModal .btn-primary").show(); // showing the run selected button if hidden
                }
                result.programNames.forEach(function(item, i, oriarray){
                    dataarr.push({name: item[0], robot: robottype, creator: item[1], date: item[4]});
                    programsparsed++;
                    if(programsparsed===oriarray.length ){ //the end of loop
                        $('#mtable').bootstrapTable({
                            height : 400,
                            sortName: "name",
                            toggle: "mtable",
                            iconsPrefix : 'typcn', 
                            search: true,
                            icons : {
                                paginationSwitchDown : 'typcn-document-text',
                                paginationSwitchUp : 'typcn-book',
                                refresh : 'typcn-refresh',
                            },
                            pagination : 'true',
                            buttonsAlign : 'right',
                            resizable : 'true',
                            
                            columns: [
                            {
                                field: 'name',
                                title: 'Program Name',
                                sortable: true
                            },{
                                field: 'creator',
                                title: 'Creator',
                                sortable: true
                            },{
                                field: 'date',
                                title: 'Creation Date',
                                formatter : UTIL.formatDate,
                                sortable: true
                            },{
                                checkbox : true,
                                valign : 'middle',
                            }],
                            data: dataarr
                        });
                        $("#simModal .btn-primary").show();
                        //the css manipulation has been done in roberta.css
//                        $("#simModal .fixed-table-header").css({"background-color": "#B3BFB8"});
                        $("#simModal .btn-primary").on("click",function(){
                            console.log("Selections will be executed");
                            var selections = $("#mtable").bootstrapTable('getSelections');
                            var selectedprograms = [];
                            for(var i=0;i<selections.length;i++){
                                var tempfind = oriarray.find(function(ele){
                                    var thisarrm = oriarray;
                                    return selections[i].name=== ele[0];
                                });
                                selectedprograms.push(tempfind);
                            }
                            console.log('Selections obtained via getSelections: are ' + JSON.stringify($("#mtable").bootstrapTable('getSelections')));
//                            alert("The following programs would be executed: "+ JSON.stringify($("#mtable").bootstrapTable('getSelections')));
                            var extractedprograms =[];
                            var numprogs = selectedprograms.length;
                            var numloadprogs =0;
                            selectedprograms.forEach(function(item,i,oriarray){
                                PROGRAM_M.loadProgramFromListing(item[0], item[1],item[3], function(dat){
                                    if(dat.rc != "ok"){
                                        alert("failed loading program for item "+ i+", check console");
                                        console.log("failed item is ", item);
                                    }
                                    dat.savedName = item[0];
                                    extractedprograms[i] = dat;
                                    numloadprogs++;
                                    if(numloadprogs===oriarray.length ){
                                        console.log("finished");
                                        var jslist = [];
//                                        console.log(extractedprograms);;
                                        var programsfetched = 0
                                        extractedprograms.forEach(function(item,i,oriarray){
                                            
                                            var xmlTextProgram = item.programText;
                                            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
                                            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
                                            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

                                            var language = GUISTATE_C.getLanguage();
                                            
                                            PROGRAM_M.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, function(result) {
//                                                console.log("lets see the result");
//                                                console.log(result);
                                                if (result.rc === "ok") {
                                                    //                    MSG.displayMessage("MESSAGE_EDIT_START", "TOAST", GUISTATE_C.getProgramName());
                                                    extractedprograms[i]["result"] = result;
//                                                    console.log("added result for ", i);
                                                    programsfetched++;
                                                    if(programsfetched === oriarray.length){
//                                                        console.log("reached end");
                                                        
                                                        console.log(extractedprograms);
                                                        simulateMultiple(extractedprograms);
                                                    }
//                                                    console.log(extractedprograms);
//                                                    var robotgroup = GUISTATE_C.getRobotGroup();
//                                                    SIM.init(result.javaScriptProgram, true, GUISTATE_C.getRobotGroup());
//                                                    $(".sim").removeClass('hide');
//                                                    $('#simButtonsCollapse').collapse({
//                                                        'toggle' : false
//                                                    });
////                                                    if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
////                                                        TOUR_C.getInstance().trigger('startSim');
////                                                    }
//                                                    $('#blockly').openRightView('sim', INITIAL_WIDTH);
                                                } else {
                                                    MSG.displayInformation(result, "", result.message, "");
                                                    alert("failed loading js for item "+i+" check console");
                                                    console.log("failed loading js for item ", item);
                                                }
//                                                console.log("ran fetching for", i);
//                                                PROGRAM_C.reloadProgram(result);
                                            });
                                            
                                        });
                                    }
                                });
                            });
                        });
                    }
                });
            }else{
                // the user is not logged in, this code might never be executed until the user deletes the disabled class and click the multiplication button
                console.log(result.message);
                $("#mtable").bootstrapTable('destroy'); //destroy the table if still present
                $("#simModal .btn-primary").hide();
            }
        });
    }
    exports.showListProg = showListProg;
    
    function simulateMultiple(programs){
        console.log("function is called");
        console.log(programs);
        $("#simModal").modal('toggle');
        
        //lets try running first program
//        var jsprog = programs[0].result.javaScriptProgram;
//        var robotgroup = GUISTATE_C.getRobotGroup();
//        SIM.init(jsprog, true, robotgroup);
//        $(".sim").removeClass('hide');
//        $('#simButtonsCollapse').collapse({
//            'toggle' : false
//        });
        const INITIAL_WIDTH = 0.5;
//        $('#blockly').openRightView('sim', INITIAL_WIDTH);
//        PROG_C.reloadProgram(programs[0].result);
        SIM.initMultiple(programs, true, GUISTATE_C.getRobotGroup());
        $(".sim").removeClass('hide');
        $('#simButtonsCollapse').collapse({
            'toggle' : false
        });
//        if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
//            TOUR_C.getInstance().trigger('startSim');
//        }
        $('#blockly').openRightView('sim', INITIAL_WIDTH);
        
        
    }

    //deprecated as previously information about the robottype was fetched from the database but it was understood later that it can be directly extracted from guistate.model.js
    function showListProgOld(){
        console.log("guistate info is ");
        console.log(GUISTATE_M.gui.robot);
        PROGLIST.loadProgList(function(result){
            console.log(result);
            if(result.rc === "ok"){
                $("#mtable").bootstrapTable('destroy'); //refreshing the table
                var dataarr = []; //Array having data to be displayed in table shown
                var programsparsed =0; //iterator for the synchronously running for loops
                result.programNames.forEach(function(item, i, oriarray){
                    PROGRAM_M.loadProgramFromListing(item[0], item[1],item[3], function(dat){
                        //dat is the program result obtained for each item
                        var myparser = dat.programText;
                        var parser = new DOMParser();
                        var xmlDocm = parser.parseFromString(myparser,"text/xml");
                        var robottype = xmlDocm.documentElement.attributes.robottype.nodeValue; // it is the robot type , say ev3 for some program item
                        dataarr.push({name: item[0], robot: robottype, creator: item[1]});
                        programsparsed++;
                        if(programsparsed===oriarray.length ){ //the end of loop
                            $('#mtable').bootstrapTable({
                                height : 400,
                                sortName: "name",
                                toggle: "mtable",
                                iconsPrefix : 'typcn',
                                icons : {
                                    paginationSwitchDown : 'typcn-document-text',
                                    paginationSwitchUp : 'typcn-book',
                                    refresh : 'typcn-refresh',
                                },
                                pagination : 'true',
                                buttonsAlign : 'right',
                                resizable : 'true',
                                
                                columns: [
                                {
                                    field: 'name',
                                    title: 'Program Name',
                                    sortable: true
                                }, {
                                    field: 'robot',
                                    title: 'Robot Name',
                                    sortable: true
                                },{
                                    field: 'creator',
                                    title: 'Creator',
                                    sortable: true
                                },{
                                    checkbox : true,
                                    valign : 'middle',
                                }],
                                data: dataarr
                            });
                            $("#simModal .btn-primary").show();
                            //the css manipulation has been done in roberta.css
//                            $("#simModal .fixed-table-header").css({"background-color": "#B3BFB8"});
                            $("#simModal .btn-primary").on("click",function(){
                                console.log("Selections will be executed");
                                console.log('Selections obtained via getSelections: are ' + JSON.stringify($("#mtable").bootstrapTable('getSelections')));
                                alert("The following programs would be executed: "+ JSON.stringify($("#mtable").bootstrapTable('getSelections')));
                            });
                        }

                    });                     
                });
            }else{
                // the user is not logged in
                console.log(result.message);
                $("#mtable").bootstrapTable('destroy'); //destroy the table if still present
                $("#simModal .btn-primary").hide();
            }
        });
    }
    exports.showListProgOld = showListProgOld
});  
