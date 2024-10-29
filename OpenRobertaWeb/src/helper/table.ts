import * as GUISTATE_C from 'guiState.controller';
// @ts-ignore
import * as Blockly from 'blockly';
import * as UTIL from 'util.roberta';

export namespace CommonTable {
    export const options = {
        buttonsAlign: 'right',
        formatLoadingMessage: function () {
            return '<div class="pace"></div>';
        },
        // @ts-ignore
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
}

export namespace CardView {
    export const options = {
        cardView: 'true',
        pageList: [12, 24, 48, 96],
        pageSize: 12,
        pagination: true,
        search: true,
        rowStyle: {
            classes: 'col-xxl-2 col-lg-3 col-md-4 col-sm-6',
        },
    };
    export function robot(robot: string): string {
        return '<div class="typcn typcn-' + GUISTATE_C.findGroup(robot) + '"></div>';
    }

    export function robotImage(robot: string, row): string {
        return '<div class="text-center robotImage"><img src="/css/img/system_preview/' + robot + '.jpg" class="w-50" alt="' + row + '"/></div>';
    }

    export function name(value: string): string {
        return '<div class="cardViewName">' + value + '</div>';
    }

    export function description(description: string) {
        return '<div class="cardViewDescription">' + description + '</div>';
    }

    export function programDescription(xml: string) {
        let myDescription = getAttributeFromXml(xml, 'description');
        return description(myDescription);
    }

    export function programTags(xml): string {
        let myTags = getAttributeFromXml(xml, 'tags');
        return tags(myTags);
    }
    export function tags(tags): string {
        if (!tags) {
            tags = '&nbsp;';
        }
        return '<input class="infoTags" type="text" value="' + tags + '" data-role="tagsinput"/>';
    }

    function getAttributeFromXml(xml: string, attribute: string): string {
        let xmlDoc = Blockly.Xml.textToDom(xml, Blockly.getMainWorkspace());
        let text = xmlDoc.getAttribute(attribute);
        return text ? text : '&nbsp;';
    }

    export function titleTypcn(value: any, typicon: string) {
        return '<div><div class="cardViewLabel tutorialIcon typcn typcn-' + typicon + '"></div><span>' + value + '</span></div>';
    }

    export function titleLabel(text: string, label: string, type: string) {
        let myLabel: string = Blockly.Msg[label] || label;
        return (
            '<div class="' +
            type +
            '"><b><div class="cardViewLabel" lkey="Blockly.Msg.' +
            label +
            '">' +
            myLabel +
            ':&nbsp;</div></b><span>' +
            text +
            '</span></div>'
        );
    }
}
