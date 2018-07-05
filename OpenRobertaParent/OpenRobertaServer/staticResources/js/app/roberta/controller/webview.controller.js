define([ 'exports', 'guiState.controller', 'wedo.model', 'util', 'log', 'message', 'blocks', 'jquery' ], function(exports, GUISTATE_C, WEDO_M, UTIL, LOG, MSG,
        Blockly, $) {

    var ready;
    var aLanguage;
    /**
     * Init webview
     */
    function init(language) {
        aLanguage = language;
        ready = $.Deferred();
        var a = {};
        a.target = 'internal';
        a.op = {};
        a.op.type = 'identify';
        if (jsToAppInterface(a) !== "ok") {
            // Obviously not in an Open Roberta webview
            ready.resolve(language);
            console.log("No webview detected!")
        }
        return ready.promise();
    }
    exports.init = init;

    function appToJsInterface(jsonData) {
        try {
            var data = JSON.parse(jsonData);
            if (!data.target || !data.op || !data.op.type) {
                throw "invalid arguments";
            }
            if (data.target == "internal") {
                if (data.op.type == "identify") {
                    ready.resolve(aLanguage, data.op.app.name);
                } else {
                    throw "invalid arguments";
                }
            } else if (data.target == "wedo" && GUISTATE_C.getRobot() == "wedo") {
                if (data.op.type == "scan" && data.op.state == "appeared") {
                    $('#show-available-connections').trigger('add', data.op);
                } else if (data.op.type == "scan" && data.op.state == "error") {
                    $('#show-available-connections').modal('hide');
                } else if (data.op.type == "scan" && data.op.state == "disappeared") {
                    console.log(data);
                } else if (data.op.type == "connect" && data.op.state == "connected") {
                    $('#show-available-connections').trigger('connect', data.op);
                    WEDO_M.update(data);
                    var bricklyWorkspace = GUISTATE_C.getBricklyWorkspace();
                    var blocks = bricklyWorkspace.getAllBlocks();
                    for (var i = 0; i < blocks.length; i++) {
                        if (blocks[i].type === "robBrick_WeDo-Brick") {
                            var field = blocks[i].getField("VAR");
                            field.setValue(data.op.brickname.replace(/\s/g, ''));
                            blocks[i].render();
                            var dom = Blockly.Xml.workspaceToDom(bricklyWorkspace);
                            var xml = Blockly.Xml.domToText(dom);
                            GUISTATE_C.setConfigurationXML(xml);
                            bricklyWorkspace.setVisible(false);
                            break;
                        }
                    }
                } else {
                    WEDO_M.update(data);
                }
            } else {
                throw "invalid arguments";
            }
        } catch (error) {
            LOG.error("appToJsInterface >" + error + " caused by: " + jsonData);
        }
    }
    exports.appToJsInterface = appToJsInterface;

    function jsToAppInterface(data) {
        try {
            OpenRoberta.jsToAppInterface(JSON.stringify(data));
            return "ok";
        } catch (error) {
            LOG.error("jsToAppInterface >" + error + " caused by: " + data);
            return "error";
        }
    }
    exports.jsToAppInterface = jsToAppInterface;
});
