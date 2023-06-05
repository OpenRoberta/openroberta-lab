define(["require", "exports", "guiState.controller", "blockly"], function (require, exports, GUISTATE_C, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.label = exports.titleTypcn = exports.tags = exports.programTags = exports.programDescription = exports.description = exports.name = exports.robot = exports.rowStyle = void 0;
    function rowStyle() {
        return {
            classes: 'col-xxl-2 col-lg-3 col-md-4 col-sm-6',
        };
    }
    exports.rowStyle = rowStyle;
    function robot(robot) {
        return '<div class="typcn typcn-' + GUISTATE_C.findGroup(robot) + '"></div>';
    }
    exports.robot = robot;
    function name(value) {
        return '<div class="cardViewName">' + value + '</div>';
    }
    exports.name = name;
    function description(description) {
        return '<div class="cardViewDescription">' + description + '</div>';
    }
    exports.description = description;
    function programDescription(xml) {
        var myDescription = getAttributeFromXml(xml, 'description');
        return description(myDescription);
    }
    exports.programDescription = programDescription;
    function programTags(xml) {
        var myTags = getAttributeFromXml(xml, 'tags');
        return tags(myTags);
    }
    exports.programTags = programTags;
    function tags(tags) {
        if (!tags) {
            tags = '&nbsp;';
        }
        return '<input class="infoTags" type="text" value="' + tags + '" data-role="tagsinput"/>';
    }
    exports.tags = tags;
    function getAttributeFromXml(xml, attribute) {
        var xmlDoc = Blockly.Xml.textToDom(xml, Blockly.getMainWorkspace());
        var text = xmlDoc.getAttribute(attribute);
        return text ? text : '&nbsp;';
    }
    function titleTypcn(typicon) {
        return '<span class="tutorialIcon typcn typcn-' + typicon + '"></span>';
    }
    exports.titleTypcn = titleTypcn;
    function label(text, label, type) {
        var myLabel = Blockly.Msg[label] || label;
        return ('<div class="' +
            type +
            '"><b><div class="cardViewLabel" lkey="Blockly.Msg.' +
            label +
            '">' +
            myLabel +
            ':&nbsp;</div></b><span>' +
            text +
            '</span></div>');
    }
    exports.label = label;
});
