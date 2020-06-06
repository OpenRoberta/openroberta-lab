define(["require", "exports", "blockly", "ajv", "nepo.schema.robot", "nepo.schema.common", "nepo.blockly"], function (require, exports, Blockly, Ajv, schemaRobot, schemaCommon, nepo_blockly_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = void 0;
    var $blocklyArea = $("#blocklyArea");
    var $bricklyArea = $("#bricklyArea");
    var $blocklyDiv = $("#blocklyDiv");
    var $bricklyDiv = $("#bricklyDiv");
    var ajv = new Ajv();
    window.LOG = true;
    function init() {
        $("#exportP").click(function () {
            toXml($("#importExportP"), blocklyWorkspace);
        });
        $("#importP").click(function () {
            fromXml($("#importExportP"), blocklyWorkspace);
        });
        $("#exportC").click(function () {
            toXml($("#importExportC"), bricklyWorkspace);
        });
        $("#importC").click(function () {
            fromXml($("#importExportC"), bricklyWorkspace);
        });
        var programToolbox = $("#program-toolbox")[0];
        var configToolbox = $("#config-toolbox")[0];
        var commonBlockDescription = null;
        var robotBlockDescription = null;
        $.getJSON("../OpenRobertaRobot/src/main/resources/nepoBlocks.json", function (result) {
            if (validateSchema(result, schemaCommon.schema)) {
                commonBlockDescription = result;
            }
        }).always(function () {
            $.getJSON("../RobotMbed/src/main/resources/calliope/nepoBlocks.json", function (result) {
                if (validateSchema(result, schemaRobot.schema)) {
                    robotBlockDescription = result;
                }
            }).always(function () {
                if (!(commonBlockDescription && robotBlockDescription)) {
                    console.assert(commonBlockDescription && robotBlockDescription ? true : false, "error TODO more");
                    return;
                }
                nepo_blockly_1.Nepo.defineBlocks(commonBlockDescription, robotBlockDescription);
                var workspaces = nepo_blockly_1.Nepo.inject("blocklyDiv", "bricklyDiv", programToolbox, configToolbox);
                window.blocklyWorkspace = workspaces["blocklyWorkspace"];
                window.bricklyWorkspace = workspaces["bricklyWorkspace"];
                resizeAll();
                Blockly.svgResize(blocklyWorkspace);
                Blockly.svgResize(bricklyWorkspace);
            });
        });
    }
    exports.init = init;
    function validateSchema(json, schema) {
        var validate = ajv.compile(schema);
        var valid = validate(json);
        if (!valid) {
            console.error(validate.errors);
        }
        else {
            console.log("validation against schema " + schema.title + " succeded");
        }
        ;
        return valid;
    }
    function toXml($out, workspace) {
        var xml = Blockly.Xml.workspaceToDom(workspace);
        $out.html(Blockly.Xml.domToPrettyText(xml));
    }
    function fromXml($in, workspace) {
        if (!$in.text()) {
            return;
        }
        var xml = Blockly.Xml.textToDom($in.text());
        Blockly.Xml.domToWorkspace(xml, workspace);
    }
    function resizeAll() {
        function resizeDiv($div, $area) {
            var x = $area.offset().left;
            var y = $area.offset().top;
            $div.offset({ top: y, left: x });
            $div.width($area.width());
            $div.height($area.height());
        }
        resizeDiv($blocklyDiv, $blocklyArea);
        resizeDiv($bricklyDiv, $bricklyArea);
        Blockly.svgResize(blocklyWorkspace);
        Blockly.svgResize(bricklyWorkspace);
    }
    ;
    $(window).on("resize", function () {
        resizeAll();
    });
});
//# sourceMappingURL=playground.js.map