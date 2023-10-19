define(["require", "exports", "guiState.controller", "blockly", "util.roberta"], function (require, exports, GUISTATE_C, Blockly, UTIL) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.CardView = exports.CommonTable = void 0;
    var CommonTable;
    (function (CommonTable) {
        CommonTable.options = {
            buttonsAlign: 'right',
            formatLoadingMessage: function () {
                return '<div class="pace"></div>';
            },
            height: UTIL.calcDataTableHeight,
            icons: {
                paginationSwitchDown: 'typcn-document-text',
                paginationSwitchUp: 'typcn-book',
                refresh: 'typcn-refresh',
            },
            iconsPrefix: 'typcn',
            showRefresh: 'true',
            resizable: 'true',
        };
    })(CommonTable = exports.CommonTable || (exports.CommonTable = {}));
    var CardView;
    (function (CardView) {
        CardView.options = {
            cardView: 'true',
            pageList: [12, 24, 48, 96],
            pageSize: 12,
            pagination: true,
            search: true,
            rowStyle: {
                classes: 'col-xxl-2 col-lg-3 col-md-4 col-sm-6',
            },
        };
        function robot(robot) {
            return '<div class="typcn typcn-' + GUISTATE_C.findGroup(robot) + '"></div>';
        }
        CardView.robot = robot;
        function robotImage(robot, row) {
            return '<div class="text-center robotImage"><img src="/css/img/system_preview/' + robot + '.jpg" class="w-50" alt="' + row + '"/></div>';
        }
        CardView.robotImage = robotImage;
        function name(value) {
            return '<div class="cardViewName">' + value + '</div>';
        }
        CardView.name = name;
        function description(description) {
            return '<div class="cardViewDescription">' + description + '</div>';
        }
        CardView.description = description;
        function programDescription(xml) {
            var myDescription = getAttributeFromXml(xml, 'description');
            return description(myDescription);
        }
        CardView.programDescription = programDescription;
        function programTags(xml) {
            var myTags = getAttributeFromXml(xml, 'tags');
            return tags(myTags);
        }
        CardView.programTags = programTags;
        function tags(tags) {
            if (!tags) {
                tags = '&nbsp;';
            }
            return '<input class="infoTags" type="text" value="' + tags + '" data-role="tagsinput"/>';
        }
        CardView.tags = tags;
        function getAttributeFromXml(xml, attribute) {
            var xmlDoc = Blockly.Xml.textToDom(xml, Blockly.getMainWorkspace());
            var text = xmlDoc.getAttribute(attribute);
            return text ? text : '&nbsp;';
        }
        function titleTypcn(value, typicon) {
            return '<div><div class="cardViewLabel tutorialIcon typcn typcn-' + typicon + '"></div><span>' + value + '</span></div>';
        }
        CardView.titleTypcn = titleTypcn;
        function titleLabel(text, label, type) {
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
        CardView.titleLabel = titleLabel;
    })(CardView = exports.CardView || (exports.CardView = {}));
});
