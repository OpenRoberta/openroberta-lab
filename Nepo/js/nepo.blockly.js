define(["require", "exports", "blockly", "nepo.variables", "nepo.procedures", "nepo.theme.classic", "nepo.sensor", "nepo.configuration", "nepo.action", "utils/nepo.logger", "nepo.msg", "nepo.blockly.overridings", "nepo.extensions"], function (require, exports, Blockly, Variables, Procedures, Theme, nepo_sensor_1, nepo_configuration_1, nepo_action_1, nepo_logger_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.Nepo = void 0;
    var LOG = new nepo_logger_1.Log();
    var Nepo = /** @class */ (function () {
        function Nepo() {
        }
        Nepo.inject = function (blocklyId, bricklyId, programToolbox, configToolbox) {
            // TODO: call this somewhere in the Open Roberta Lab static resources whenever a new plugin is chosen. Make sure you got the correct files from a rest call.
            this.bricklyWorkspace = Blockly.inject(bricklyId, {
                "toolbox": configToolbox,
                zoom: {
                    controls: true,
                    wheel: true,
                    startScale: 1.0,
                    maxScale: 3,
                    minScale: 0.3,
                    scaleSpeed: 1.2
                },
                move: {
                    scrollbars: false,
                    drag: true,
                    wheel: false
                },
                trashcan: true,
                theme: Theme.nepoStyle
            });
            this.blocklyWorkspace = Blockly.inject(blocklyId, {
                "toolbox": programToolbox,
                zoom: {
                    controls: true,
                    wheel: true,
                    startScale: 1.0,
                    maxScale: 3,
                    minScale: 0.3,
                    scaleSpeed: 1.2
                },
                move: {
                    scrollbars: false,
                    drag: true,
                    wheel: false
                },
                trashcan: true,
                theme: Theme.nepoStyle
            });
            this.blocklyWorkspace.registerToolboxCategoryCallback("NEPO_VARIABLE", Variables.flyoutCallback);
            this.blocklyWorkspace.registerToolboxCategoryCallback("NEPO_PROCEDURE", Procedures.flyoutCallback);
            // add default start block
            var xml = Blockly.Xml.textToDom("<xml xmlns='https://developers.google.com/blockly/xml'>" +
                "<block type='controls_start' id='mm;%5J+ugrVfS:/UE}.G' x='200' y='56'>" +
                "<mutation xmlns='http://www.w3.org/1999/xhtml' declare='false'></mutation>" +
                "</block>" +
                "</xml>");
            Blockly.Xml.domToWorkspace(xml, this.blocklyWorkspace);
            // TODO use this only for developement
            // blocklyWorkspace.addChangeListener(function(event) { console.log(event) });
            return { "blocklyWorkspace": this.blocklyWorkspace, "bricklyWorkspace": this.bricklyWorkspace };
        };
        Nepo.initCommonBlocks = function (commonBlocks) {
            var b = [];
            for (var _i = 0, commonBlocks_1 = commonBlocks; _i < commonBlocks_1.length; _i++) {
                var block = commonBlocks_1[_i];
                var a = {};
                for (var key in block) {
                    var keyTmp = key;
                    if (key.indexOf("args") >= 0) {
                        keyTmp = "args";
                    }
                    if (key.indexOf("message") >= 0) {
                        keyTmp = "message";
                    }
                    switch (keyTmp) {
                        case "type":
                        case "output":
                        case "mutator":
                        case "inputsInline":
                        case "style":
                        case "extensions":
                        case "nextStatement":
                            a[key] = block[key];
                            break;
                        case "message":
                            this.checkMessages(block, key);
                            a[key] = block[key];
                            break;
                        case "args":
                            this.checkOptions(block, key);
                            a[key] = block[key];
                            break;
                        default:
                            console.error("error " + keyTmp);
                    }
                }
                if (a["type"] == "controls_start")
                    console.log(a);
                // define defaults:
                if (!a["output"] && !a["nextStatement"]) {
                    a["previousStatement"] = true;
                    a["nextStatement"] = true;
                }
                if (a["type"] == "controls_start")
                    console.log(a);
                // extensions
                if (!(a["extensions"] instanceof Array)) {
                    a["extensions"] = [];
                }
                if (!this.checkExtensionTooltip(a["extensions"])) {
                    a["extensions"].push("tooltip_extension");
                }
                b.push(a);
            }
            return b;
        };
        Nepo.defineBlocks = function (jsonCommonBlocks, jsonRobotBlocks) {
            Nepo.robot = jsonRobotBlocks["robot"];
            Nepo.robotGroup = jsonRobotBlocks["robotGroup"];
            Nepo.defineDataTypes(jsonRobotBlocks["dataTypes"]);
            Nepo.defineListTypes(jsonRobotBlocks["dataTypes"]);
            Nepo.defineSensors(jsonRobotBlocks["sensors"], Nepo.robot, Nepo.robotGroup);
            Nepo.defineConfigurations(jsonRobotBlocks["configurations"], Nepo.robot, Nepo.robotGroup);
            Nepo.defineActions(jsonRobotBlocks["actions"], Nepo.robot, Nepo.robotGroup);
            Nepo.defineCommonBlocks(Nepo.initCommonBlocks(jsonCommonBlocks['blocks']));
        };
        Nepo.defineSensors = function (sensors, robot, robotGroup) {
            var getSamples = sensors["getSample"];
            for (var _i = 0, getSamples_1 = getSamples; _i < getSamples_1.length; _i++) {
                var getSample = getSamples_1[_i];
                Blockly.Blocks["sensor_" + getSample["name"].toLowerCase() + "_getSample"] = new nepo_sensor_1.Sensor(getSample, robot, robotGroup);
            }
            var others = sensors["other"];
            if (others) {
                Blockly.defineBlocksWithJsonArray(Nepo.initCommonBlocks(others));
            }
        };
        Nepo.defineConfigurations = function (configurations, robot, robotGroup) {
            for (var _i = 0, configurations_1 = configurations; _i < configurations_1.length; _i++) {
                var conf = configurations_1[_i];
                Blockly.Blocks[conf["category"].toLowerCase() + "_" + conf["name"].toLowerCase()] = new nepo_configuration_1.Configuration(conf, robot, robotGroup);
            }
            var others = configurations["other"];
            if (others) {
                Blockly.defineBlocksWithJsonArray(Nepo.initCommonBlocks(others));
            }
        };
        Nepo.defineActions = function (actions, robot, robotGroup) {
            for (var _i = 0, actions_1 = actions; _i < actions_1.length; _i++) {
                var action = actions_1[_i];
                Blockly.Blocks["action_" + action["name"].toLowerCase() + "_do"] = new nepo_action_1.Action(action, robot, robotGroup);
            }
            // let others = configurations["other"];
            // if (others) {
            // 	Blockly.defineBlocksWithJsonArray(Nepo.initCommonBlocks(others))
            // }
        };
        Nepo.defineDataTypes = function (dataTypes) {
            this.dataTypes = dataTypes;
            var dropdownTypes = [];
            Object.values(this.dataTypes).forEach(function (type) {
                if (!!Blockly.Msg["DATA_TYPE_" + type.toUpperCase()]) {
                    dropdownTypes.push([Blockly.Msg["DATA_TYPE_" + type.toUpperCase()], type]);
                }
                else {
                    dropdownTypes.push(["DATA_TYPE_" + type.toUpperCase(), type]);
                    LOG.warn("Blockly message does not exists", "DATA_TYPE_" + type.toUpperCase());
                }
            });
            this.dropdownTypes = dropdownTypes;
            LOG.info("defined data types", this.dataTypes);
        };
        Nepo.defineListTypes = function (listTypes) {
            this.listTypes = listTypes;
            var dropdownTypes = [];
            Object.values(this.dataTypes).forEach(function (type) {
                if (!!Blockly.Msg["DATA_TYPE_" + type.toUpperCase()]) {
                    dropdownTypes.push([Blockly.Msg["DATA_TYPE_" + type.toUpperCase()], type]);
                }
                else {
                    dropdownTypes.push(["DATA_TYPE_" + type.toUpperCase(), type]);
                    LOG.warn("Blockly message does not exists", "DATA_TYPE_" + type.toUpperCase());
                }
            });
            this.dropdownListTypes = dropdownTypes;
            LOG.info("defined data list types", this.listTypes);
        };
        Nepo.defineCommonBlocks = function (commonBlocks) {
            var commonBlocksExtended = commonBlocks;
            Blockly.defineBlocksWithJsonArray(commonBlocksExtended);
        };
        Nepo.checkExtensionTooltip = function (extensions) {
            for (var ex in extensions) {
                if (extensions[ex].indexOf("tooltip") >= 0) {
                    return true;
                }
            }
            return false;
        };
        Nepo.checkMessages = function (block, key) {
            var value = block[key];
            var reg = new RegExp("message" + "(\\d+)");
            var m = key.match(reg);
            var msg = "%{BKY_" + block.type.toUpperCase() + "}";
            if (m != null) {
                if (m[1] == "0") {
                    if (value.indexOf("BKY") >= 0 && !value.startsWith(msg)) {
                        console.warn("Missing message for " + block.type + ": " + value);
                        return;
                    }
                }
                if (value.indexOf("BKY") >= 0) {
                    var mes = value.slice(6, value.indexOf("}"));
                    if (Blockly.Msg[mes] == undefined) {
                        console.warn("No message for " + value + " defined!");
                    }
                }
            }
            else {
                console.warn("Bad key for message: " + key);
                return;
            }
        };
        Nepo.checkOptions = function (block, key) {
            if (block[key] instanceof Array) {
                block[key].forEach(function (element) {
                    if (element["options"] && element["options"] instanceof Array) {
                        element["options"].forEach(function (option) {
                            if (option[0].indexOf("BKY") >= 0) {
                                var mes = option[0].slice(6, option[0].indexOf("}"));
                                if (Blockly.Msg[mes] == undefined) {
                                    console.warn("No message for " + option[0] + " defined!");
                                }
                            }
                        });
                    }
                });
            }
        };
        Nepo.setCommonBlockDescription = function (commonBlockDescription) {
            this.commonBlockDescription = commonBlockDescription;
        };
        Nepo.setRobotBlockDescription = function (robotBlockDescription) {
            this.robotBlockDescription = robotBlockDescription;
        };
        return Nepo;
    }());
    exports.Nepo = Nepo;
});
//# sourceMappingURL=nepo.blockly.js.map