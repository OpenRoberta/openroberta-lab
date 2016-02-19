define([ 'exports', 'blocks', 'roberta.program', 'roberta.robot', 'roberta.user-state', 'comm', 'util' ], function(exports, Blockly, ROBERTA_PROGRAM,
        ROBERTA_ROBOT, userState, COMM, UTIL) {

    /**
     * Show toolbox
     * 
     * @param {result}
     *            result of server call
     * @param {toolbox}
     *            toolbox to show
     */
    function showToolbox(result, toolbox) {
        workspace = ROBERTA_PROGRAM.getBlocklyWorkspace();
        UTIL.response(result);
        if (result.rc === 'ok') {
            userState.toolbox = toolbox;
            workspace.updateToolbox(result.data);
            ROBERTA_ROBOT.setState(result);
            if (toolbox === "beginner") {
                $('#menuToolboxBeginner').parent().addClass('disabled');
                $('#menuToolboxExpert').parent().removeClass('disabled');
                $('#menuToolboxSimulation').parent().removeClass('disabled');
            } else if (toolbox === "expert") {
                $('#menuToolboxExpert').parent().addClass('disabled');
                $('#menuToolboxBeginner').parent().removeClass('disabled');
                $('#menuToolboxSimulation').parent().removeClass('disabled');
            }
            userState.blocklyTranslated = true;
        }
    }

    /**
     * Load toolbox from server
     * 
     * @param {toolbox}
     *            toolbox to be loaded
     */
    function loadToolbox(toolboxName) {
        COMM.json("/toolbox", {
            "cmd" : "loadT",
            "name" : toolboxName,
            "owner" : " "
        }, function(toolbox) {
            showToolbox(toolbox, toolboxName);
        });
    }
    exports.loadToolbox = loadToolbox;
});
