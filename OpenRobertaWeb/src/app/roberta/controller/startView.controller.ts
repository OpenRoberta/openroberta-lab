/**
 * @fileOverview Provides the view/tab as a start screen.
 *               All available robots/systems are shown in a bootstrap table with card view. From this view one can select a system to load it, with or without
 *               extensions (currently only nn), if supported.
 *
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 */
import * as GUISTATE_C from 'guiState.controller';
import * as STARTVIEW from 'startView.model';

import { CardView } from 'table';
import * as $ from 'jquery';
import 'bootstrap-table';
// @ts-ignore
import * as Blockly from 'blockly';
import * as TOUR_C from 'tour.controller';
import * as PROGRAM_C from 'program.controller';
import * as IMPORT_C from 'import.controller';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';

var robots = [];
var mainCallback: Function;
const numPopularRobots: number = 8;

export function init(callback: Function) {
    mainCallback = callback;
    let ready = $.Deferred();
    let r = GUISTATE_C.getRobots();
    robots = Object.keys(r).map(function (index) {
        let robot = r[index];
        robot.startSelection = [];
        return robot;
    });
    const iCalliopeNoBlue = robots.findIndex((robot, index) => {
        return robot.name === 'calliope2017NoBlue';
    });
    const iCalliopeBlue: number = robots.findIndex((robot, index) => {
        return robot.name === 'calliope2017';
    });
    if (iCalliopeNoBlue > -1 && iCalliopeBlue > -1) {
        robots[iCalliopeNoBlue].startSelection.push('blue');
        robots.splice(iCalliopeBlue, 1);
    }
    robots.forEach(function (row, index) {
        row.rowNum = index;
    });

    const preload = (src) =>
        new Promise(function (resolve, reject) {
            const img = new Image();
            img.onload = function () {
                resolve(img);
            };
            img.onerror = function () {
                console.error('could not preload ' + src);
                resolve(null);
            };
            img.src = src;
        });

    const preloadAll = (images) => Promise.all(images.map(preload));
    let images = robots.map((robot) => '/css/img/system_preview/' + robot.name + '.jpg');
    $.when(preloadAll(images)).then((images) => {
        initRobotList();
        initRobotToolbar();
        initRobotListEvents();
        fetchRSSFeed();
        ready.resolve();
    });
    return ready.promise();
}

function initRobotToolbar() {
    let selectFeature = $('#startFeatureTemplate').html();
    let selectProgLanguage = $('#startProgrammingLanguageTemplate').html();
    let $selectFeature = $(selectFeature);
    let $selectProgLanguage = $(selectProgLanguage);
    translate($selectFeature);
    translate($selectProgLanguage);
    $('#start .fixed-table-toolbar').append($selectFeature);
    $('#start .fixed-table-toolbar .btn-group:last-child').prepend($selectProgLanguage);
}

function initRobotList() {
    const myLang = GUISTATE_C.getLanguage();
    const startRobots = robots.slice(0, numPopularRobots);
    function clickRobot(e, value, row, optBookmark?) {
        e.stopPropagation();
        let extensions = [];
        $(e.target)
            .closest('.card-views')
            .find('.form-check-input')
            .each(function () {
                if ($(this).prop('checked')) {
                    let extension: string = $(this).val().toString();
                    extensions.push(extension);
                }
            });
        let robot = row.name;
        if (extensions.indexOf('blue') !== -1 && row.name === 'calliope2017NoBlue') {
            robot = 'calliope2017';
        }
        mainCallback(robot); // TODO call mainCallback(row.name, extensions)
        UTIL.cleanUri();
        if (optBookmark) {
            var uri;
            $(window).oneWrap('hashchange', function () {
                window.history.replaceState({}, document.title, uri);
            });
            uri = window.location.toString();
            uri += '?loadSystem=' + robot;
            window.history.replaceState({}, document.title, uri);
            $('#show-message').oneWrap('hidden.bs.modal', function (e) {
                e.preventDefault();
                UTIL.cleanUri();
            });
            // @ts-ignore
            MSG.displayMessage('POPUP_CREATE_BOOKMARK', 'POPUP', '');
        }
        return false;
    }
    function clickBookmark(e, value, row) {
        clickRobot(e, value, row, true);
    }
    const myOptions = {
        columns: [
            {
                field: 'rowNum',
                sortable: true,
                visible: false,
                searchable: false,
            },
            {
                field: 'announcement',
                title: '',
                searchable: false,
                formatter: function (announcement, row, index) {
                    let html: string =
                        '<a href="#" class="pick typcn typcn-star-outline bookmark" rel="tooltip" data-bs-placement="top" data-bs-original-title="' +
                        Blockly.Msg.START_BOOKMARK_TOOLTIP +
                        '" aria-label="' +
                        Blockly.Msg.START_BOOKMARK_TOOLTIP +
                        '"  lkey="Blockly.Msg.START_BOOKMARK_TOOLTIP" title=""></a>';
                    if (announcement && announcement === 'beta') {
                        return html + "<img class='img-beta' src='/css/img/beta.png' alt='beta' />";
                    } else if (announcement && announcement === 'deprecated') {
                        return html + "<img class='img-deprecated' src='/css/img/deprecated.png' alt='deprecated' />";
                    } else {
                        return html;
                    }
                },
                events: {
                    'click .pick': clickBookmark,
                },
            },
            {
                field: 'group',
                sortable: true,
                searchable: true,
                visible: false,
            },
            {
                field: 'name',
                title: '',
                sortable: true,
                searchable: false,
                formatter: CardView.robotImage,
                events: {
                    'click .robotImage': function (e, value, row) {
                        clickRobot(e, value, row, false);
                    },
                },
            },
            {
                field: 'nn',
                visible: false,
                sortable: true,
                searchable: false,
            },
            {
                field: 'progLanguage',
                visible: false,
                sortable: true,
                searchable: false,
            },
            {
                field: 'realName',
                title: '',
                sortable: true,
                searchable: true,
                searchFormatter: false,
                searchAccentNeutralise: true,
                formatter: function (realName) {
                    return '<div class="robotName">' + realName + '</div>';
                },
            },
            {
                field: 'sim',
                visible: false,
                sortable: true,
                searchable: false,
            },
            {
                title: '',
                field: 'startSelection',
                searchable: false,
                // TODO activate this when nn is seletable for all robots
                formatter: function (startSelection, row) {
                    let myStartSelection = $('#startSelectionTemplate').html();
                    let $myStartSelection = $(myStartSelection);
                    $myStartSelection.addClass('invisible');
                    startSelection.forEach((value) => {
                        $myStartSelection.removeClass('invisible');
                        $myStartSelection
                            .find('#' + value)
                            .parent()
                            .removeClass('hidden');
                    });
                    return $myStartSelection[0].outerHTML;
                },
            },
            {
                title: '',
                field: 'startNow',
                searchable: false,
                formatter: function () {
                    return (
                        '<button href="#" lkey="Blockly.Msg.START_START" class="btn start pick typcn typcn-arrow-right" role="button">' +
                        Blockly.Msg.START_START +
                        '</button>'
                    );
                },
                events: {
                    'click .pick': function (e, value, row) {
                        clickRobot(e, value, row, false);
                    },
                },
            },
        ],
        locale: myLang,
        toolbar: '#robotListToolbar',
        cardView: 'true',
        search: true,
        rowStyle: {
            //classes: 'col-xxl-2  col-md-4 col-sm-6 col-xs-12',
            classes: 'col-xxl-3 col-sm-6 col-xs-12',
        },
        showRefresh: false,
        buttons: function () {
            return {
                popularRobots: {
                    html:
                        '<button class="nav-link active start" data-bs-toggle="button" autocomplete="off" aria-pressed="true" data-bs-original-title="" id="popularRobots" lkey="Blockly.Msg.START_POPULAR_ROBOTS">' +
                        Blockly.Msg.START_POPULAR_ROBOTS +
                        '</button>',
                },
                allRobots: {
                    html:
                        '<button class="nav-link start" data-bs-toggle="button" autocomplete="off" aria-pressed="false" data-bs-original-title="" id="allRobots" lkey="Blockly.Msg.START_ALL_ROBOTS">' +
                        Blockly.Msg.START_ALL_ROBOTS +
                        '</button><span id="results"></span>&nbsp;<span id="labelResults" lkey="Blockly.Msg.START_RESULTS">' +
                        Blockly.Msg.START_RESULTS +
                        '</span>',
                },
            };
        },
        buttonsAlign: 'left',
        filterControlContainer: true,
    };
    $('#robotTable').bootstrapTable(myOptions);
    $('#robotTable').bootstrapTable('load', startRobots);
    $('.bookmark').tooltip({ trigger: 'hover' });
    $('#start input.form-control.search-input').attr('placeholder', Blockly.Msg.START_FORMATSEARCH);
    translate($('#robotTable'));
    $('#start .fixed-table-toolbar>.float-right').toggleClass('hidden');
    $('span#results').text(startRobots.length);
}
function initView() {
    $('#tabProgram, #tabConfiguration').parent().addClass('invisible');
    $('#tabNN, #tabNNLearn').parent().hide();
    $('.notStart').addClass('disabled');
    $('#header').removeClass('shadow');
    GUISTATE_C.resetRobot();
    GUISTATE_C.setView('tabStart');
}
function initRobotListEvents() {
    $('#tabStart').onWrap('show.bs.tab', function (e) {
        initView();
    });
    let popularRobots = function () {
        if (!$('#allRobots').hasClass('active')) {
            $(this).addClass('active');
            return false;
        }
        $('#more').toggleClass('more');
        $('#more').attr('lkey', 'Blockly.Msg.START_ALL_ROBOTS');
        $('#more').text(Blockly.Msg.START_ALL_ROBOTS);
        $('#start .fixed-table-toolbar>.float-right').toggleClass('hidden');
        $('#allRobots').toggleClass('active');
        $('#allRobots').attr('aria-pressed', 'false');
        let myRobots = robots.slice(0, numPopularRobots);
        $('#robotTable').bootstrapTable('load', myRobots);
        $('#robotTable').bootstrapTable('resetSearch', '');
        $('#robotTable').bootstrapTable('sortBy', { field: 'rowNum', sortOrder: 'asc' });
        $('#robotTable').bootstrapTable('filterBy', []);
        $('#start').animate({ scrollTop: $('.section--white').height() }, 500);
    };
    $('#popularRobots').onWrap('click', popularRobots);
    let allRobots = function () {
        if (!$('#popularRobots').hasClass('active')) {
            $(this).addClass('active');
            return false;
        }
        $('input.progLang, input.feature').each(function () {
            $(this).prop('checked', false);
        });
        $('#more').toggleClass('more');
        $('#more').attr('lkey', 'Blockly.Msg.START_POPULAR_ROBOTS');
        $('#more').text(Blockly.Msg.START_POPULAR_ROBOTS);
        $('#start .fixed-table-toolbar>.float-right').toggleClass('hidden');
        $('#popularRobots').toggleClass('active');
        $('#popularRobots').attr('aria-pressed', 'false');
        $('#robotTable').bootstrapTable('load', robots);
        $('#robotTable').bootstrapTable('filterBy', []);
        $('#robotTable').bootstrapTable('sortBy', { field: 'realName', sortOrder: 'asc' });
        $('#start').animate({ scrollTop: $('.section--white').height() }, 500);
        $('#start input.form-control.search-input').trigger('focus');
    };
    $('#allRobots').onWrap('click', allRobots);
    $('#more').onWrap('click', function () {
        var $this = $(this);
        if ($this.hasClass('more')) {
            $('#allRobots').trigger('click');
            allRobots();
        } else {
            $('#popularRobots').trigger('click');
            popularRobots();
        }
    });
    $('#robotTable').on('post-body.bs.table', function (e, data) {
        $('span#results').text(data.length);
        $('.bookmark').tooltip({ trigger: 'hover' });
    });
    $('#start .btn-group input').onWrap('change', function (e) {
        let myFilter: {} = {};
        $('input.progLang').each(function () {
            if ($(this).prop('checked')) {
                if (!myFilter['progLanguage']) {
                    myFilter['progLanguage'] = [];
                }
                myFilter['progLanguage'].push($(this).val().toString());
            }
        });
        $('input.feature').each(function () {
            if ($(this).prop('checked')) {
                myFilter[$(this).val().toString()] = true;
            }
        });
        $('#robotTable').bootstrapTable('filterBy', myFilter);
    });
    $('#startImportProg').onWrap(
        'click',
        function (e) {
            e.stopPropagation();
            IMPORT_C.importXmlFromStart(mainCallback);
        },
        'import clicked'
    );
    $('#takeATour').onWrap(
        'click',
        function (e) {
            e.stopPropagation();
            mainCallback('ev3lejosv1', function () {
                PROGRAM_C.newProgram(true);
                TOUR_C.start('welcome');
            });
        },
        'take a tour clicked'
    );
}

function translate($element: JQuery<HTMLElement>): void {
    $element.find('[lkey]').each(function (index) {
        let lkey = $(this).attr('lkey');
        let key = lkey.replace('Blockly.Msg.', '');
        if ($(this).attr('rel') === 'tooltip') {
            $(this).attr('data-bs-original-title', Blockly.Msg[key]);
        } else {
            $(this).html(Blockly.Msg[key]);
        }
    });
}

function loadImages(names: string[]) {
    let imgPath: string = '/css/img/system_preview/';
    let i = 0;
    let numLoading = names.length;
    const onload = function () {
        --numLoading === 0;
    };
    const images = {};
    while (i < names.length) {
        const img = (images[names[i]] = new Image());
        img.onload = onload;
        img.onerror = function (e) {
            console.error(e);
        };
        img.src = imgPath + names[i++];
    }
    return images;
}

function fetchRSSFeed() {
    let errorFn = function () {
        // $('#section--news').remove(); Just do nothing
    };
    let successFn = function (response) {
        var $newsContainer = $(
            '<section class="section--white" id="section--news"><div class="container-fluid"><div class="row justify-content-center"><div class="col col-12 col-xl-10 col-xxl-8 col-xxxl-6 my-4"><div class="teaser-news"><div class="row justify-content-between align-items-center"><div class="col col-12 col-md-6"><div class="teaser-news--header">Neues aus dem Open Roberta Lab</div><div class="teaser-news--body" id="teaser-news--rss"></div></div><div class="col col-12 col-md-6 col-lg-4 offset-md-0 offset-lg-2 mt-5 mt-md-0" id="teaser-news--rss-image"></div></div></div></div></div></div></section>'
        );
        $newsContainer.insertBefore('footer');
        $(response)
            .find('item')
            .each(function () {
                var $rssImage = $('<div class="teaser-news--body"></div>');
                var rss_title = $(this).find('title').text();
                var rss_description = $(this).find('description').text();
                var rss_link = $(this).find('link').text();
                var rss_image = $(this).find('enclosure').attr('url');
                $rssImage.append('<div><img src="' + rss_image + '" /></div>');
                $('#teaser-news--rss')
                    .append('<h3>' + rss_title + '</h3>')
                    .append('<p class="teaser">' + rss_description + '</p>')
                    .append('<a target="_blank" class="teaser-news--link button button--light" href="' + rss_link + '">Mehr erfahren</a>');
                $('#teaser-news--rss-image').append('<img class="teaser-news--image" src="' + rss_image + '">');
            });
    };
    if (GUISTATE_C.getLanguage() !== 'de') {
        return errorFn();
    }
    STARTVIEW.fetchRSS(successFn, errorFn);
}
