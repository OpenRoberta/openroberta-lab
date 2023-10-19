/**
 * @fileOverview Provides the view/tab for all tutorials available. Tutorials
 *               are shown in a bootstrap table with card view. From this view
 *               one can select (double click) a tutorial to load it into the
 *               program (blockly) view to work with it.
 *
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
import * as UTIL from 'util.roberta';
import * as GUISTATE_C from 'guiState.controller';
import * as TUTORIAL_C from 'progTutorial.controller';
import { CardView, CommonTable } from 'table';
import * as $ from 'jquery';
import 'bootstrap-table';
import 'bootstrap-tagsinput';

let BACKGROUND_COLORS: string[] = ['#33B8CA', '#EBC300', '#005A94', '#179C7D', '#F29400', '#E2001A', '#EB6A0A', '#8FA402', '#BACC1E', '#9085BA', '#FF69B4', '#DF01D7'];
let currentColorIndex;
let tutorialList;
/**
 * Initialize table of tutorials
 */
export function init(): void {
    tutorialList = GUISTATE_C.getListOfTutorials();
    for (let tutorial in tutorialList) {
        if (tutorialList.hasOwnProperty(tutorial)) {
            $('#head-navigation-tutorial-dropdown').append(
                '<li class=\'' +
                tutorialList[tutorial].language +
                ' ' +
                tutorialList[tutorial].robot +
                '\'><a href=\'#\' id=\'' +
                tutorial +
                '\' class=\'menu tutorial typcn typcn-mortar-board\'>' +
                tutorialList[tutorial].name +
                '</a></li>'
            );
        }
    }
    initTutorialList();
    initTutorialListEvents();
}

export function switchLanguage(): void {
    $('#tutorialTable').bootstrapTable('destroy');
    init();
    if (GUISTATE_C.getView() === 'tabTutorialList') {
        updateTutorialList();
    }
}

function initTutorialList(): void {
    const myLang = GUISTATE_C.getLanguage();
    const myOptions = {
        columns: [
            {
                field: 'robot',
                title: '',
                sortable: true,
                formatter: CardView.robot
            },
            {
                field: 'name',
                title: '',
                sortable: true,
                formatter: CardView.name
            },
            {
                field: 'overview.description',
                title: '',
                sortable: true,
                formatter: CardView.description
            },
            {
                field: 'overview.goal',
                title: '',
                sortable: true,
                formatter: function(goal) {
                    return CardView.titleLabel(goal, 'TITLE_GOAL', 'cardViewDescription');
                }
            },
            {
                field: 'overview.previous',
                title: '',
                sortable: true,
                formatter: function(goal) {
                    return CardView.titleLabel(goal, 'TITLE_PREVIOUS', 'cardViewDescription');
                }
            },
            {
                field: 'time',
                title: '',
                formatter: function(time) {
                    return CardView.titleTypcn(time, 'stopwatch');
                },
                sortable: true
            },
            {
                field: 'age',
                title: '',
                formatter: function(age) {
                    return CardView.titleTypcn(age, 'group');
                },
                sortable: true
            },
            {
                field: 'sim',
                title: '',
                formatter: function(sim) {
                    return CardView.titleTypcn(formatSim(sim), 'simulation');
                },
                sortable: true
            },
            {
                field: 'level',
                title: '',
                formatter: function(level) {
                    return CardView.titleTypcn(formatLevel(level), 'mortar-board');
                },
                sortable: true
            },
            {
                field: 'tags',
                title: '',
                sortable: true,
                formatter: CardView.tags
            },
            {
                field: 'index',
                title: '',
                visible: false
            },
            {
                field: 'group',
                title: '',
                visible: false
            }
        ],
        locale: myLang,
        rowAttributes: rowAttributes,
        sortName: 'index',
        sortOrder: 'asc',
        toolbar: '#tutorialListToolbar'
    };
    const options = { ...CommonTable.options, ...CardView.options, ...myOptions };
    $('#tutorialTable').bootstrapTable(options);
}

function initTutorialListEvents(): void {
    $('#tabTutorialList').off();
    $('#tutorialTable').off();
    $('#backTutorialList').off();
    $('#tabTutorialList').onWrap(
        'shown.bs.tab',
        function(): boolean {
            //@ts-ignore
            guiStateController.setView('tabTutorialList');
            updateTutorialList();
            return false;
        },
        'tutorial clicked'
    );
    var resizeTable = function(): void {
        $('#tutorialTable').bootstrapTable('resetView', {
            height: UTIL.calcDataTableHeight()
        });
        configureTagsInput();
    };
    $(window).off('resize', resizeTable);
    $(window).on('resize', resizeTable);
    $('#tutorialTable').onWrap('post-body.bs.table', resizeTable, 'page-change tutorial list');
    $('#tutorialList')
        .find('button[name="refresh"]')
        .onWrap(
            'click',
            function(): boolean {
                updateTutorialList();
                return false;
            },
            'refresh tutorial list clicked'
        );

    $('#tutorialTable').onWrap(
        'click-row.bs.table',
        function($element, row): void {
            $element.stopPropagation();
            $element.preventDefault();
            TUTORIAL_C.loadFromTutorial(row.id);
        },
        'Load program from tutorial double clicked'
    );

    $('#backTutorialList').onWrap(
        'click',
        function(): boolean {
            $('#' + GUISTATE_C.getPrevView()).tabWrapShow();
            return false;
        },
        'back to program view'
    );

    $('#tutorialTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
        $('#tutorialTable').bootstrapTable('resetWidth');
    });
}

function updateTutorialList() {
    $('#tutorialTable').bootstrapTable('showLoading');
    let tutorialArray: any[] = [];
    for (let tutorial in tutorialList) {
        if (tutorialList.hasOwnProperty(tutorial) && tutorialList[tutorial].language === GUISTATE_C.getLanguage().toUpperCase()) {
            tutorialList[tutorial].id = tutorial;
            tutorialArray.push(tutorialList[tutorial]);
        }
    }
    $('#tutorialTable').bootstrapTable('load', tutorialArray);
    configureTagsInput();
    $('#tutorialTable').bootstrapTable('hideLoading');
    $('#tutorialTable').bootstrapTable('resetView', {
        height: UTIL.calcDataTableHeight()
    });
}
let rowAttributes = function(row, index): any {
    let hash: number = UTIL.getHashFrom(row.robot + row.name + row.index);
    currentColorIndex = hash % BACKGROUND_COLORS.length;
    return {
        class: 'col-xxl-2 col-lg-3 col-md-4 col-sm-6',
        style:
            'background-color :' +
            BACKGROUND_COLORS[currentColorIndex] +
            ';' + //
            '' // 'border: 4px solid #EEEEEE; z-index: 1; cursor: pointer;',
    };
};

let emptyTitle = function(): string {
    return '<span style="display:none"></span>';
};

let formatTutorialOverview = function(overview, row, index, field) {
    return CardView.description(overview);
};

let formatSim = function(sim): string {
    if (sim && (sim === 'sim' || sim === 1)) {
        return 'ja';
    } else {
        return 'nein';
    }
};

let formatLevel = function(level: any) {
    let html: string = '';
    if (level) {
        let maxLevel = isNaN(level) ? level.split('/')[1] : 3;
        let thisLevel = isNaN(level) ? level.split('/')[0] : level;
        for (let i = 1; i <= maxLevel; i++) {
            if (i <= thisLevel) {
                html = html + '<span style="left: 0;" class="tutorialLevel typcn typcn-star-full-outline"/>';
            } else {
                html = html + '<span class="tutorialLevel typcn typcn-star-outline"/>';
            }
        }
        html = '<span class="tutorialLevelStars">' + html;
        html += '</span>';
    }
    return html;
};

function configureTagsInput(): void {
    //@ts-ignore
    $('.infoTags').tagsinput();
    $('#tutorialTable .bootstrap-tagsinput').addClass('galleryTags');
    $('#tutorialList').find('.tutorialTags>input').attr('readonly', 'true');
    $('#tutorialList').find('span[data-role=remove]').addClass('hidden');
}
