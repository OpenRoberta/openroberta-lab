define(["require", "exports", "guiState.controller", "startView.model", "table", "jquery", "blockly", "tour.controller", "program.controller", "import.controller", "util.roberta", "message", "bootstrap-table"], function (require, exports, GUISTATE_C, STARTVIEW, table_1, $, Blockly, TOUR_C, PROGRAM_C, IMPORT_C, UTIL, MSG) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = void 0;
    var robots = [];
    var mainCallback;
    var uniqueProgLanguages = [];
    var numPopularRobots = 8;
    function init(callback) {
        mainCallback = callback;
        var ready = $.Deferred();
        var r = GUISTATE_C.getRobots();
        robots = Object.keys(r).map(function (index) {
            var robot = r[index];
            if (robot['sim']) {
                robot['extensions']['sim'] = true;
            }
            else {
                robot['extensions']['sim'] = false;
            }
            delete robot['sim'];
            return robot;
        });
        var iCalliopeNoBlue = robots.findIndex(function (robot, index) {
            return robot.name === 'calliope2017NoBlue';
        });
        var iCalliopeBlue = robots.findIndex(function (robot, index) {
            return robot.name === 'calliope2017';
        });
        if (iCalliopeNoBlue > -1 && iCalliopeBlue > -1) {
            robots[iCalliopeNoBlue].extensions.blue = true;
            robots.splice(iCalliopeBlue, 1);
        }
        robots.forEach(function (row, index) {
            row.rowNum = index;
        });
        var progLanguages = robots.map(function (robot) { return robot.progLanguage; });
        uniqueProgLanguages = progLanguages.filter(function (item, pos) {
            return progLanguages.indexOf(item) == pos;
        });
        var preload = function (src) {
            return new Promise(function (resolve, reject) {
                var img = new Image();
                img.onload = function () {
                    resolve(img);
                };
                img.onerror = function () {
                    console.error('could not preload ' + src);
                    resolve(null);
                };
                img.src = src;
            });
        };
        var preloadAll = function (images) { return Promise.all(images.map(preload)); };
        var images = robots.map(function (robot) { return '/css/img/system_preview/' + robot.name + '.jpg'; });
        $.when(preloadAll(images)).then(function (images) {
            initRobotList();
            initRobotToolbar();
            initRobotListEvents();
            fetchRSSFeed();
            ready.resolve();
        });
        return ready.promise();
    }
    exports.init = init;
    function initRobotToolbar() {
        var selectFeature = $('#startFeatureTemplate').html();
        var selectProgLanguage = $('#startProgrammingLanguageTemplate').html();
        var $selectFeature = $(selectFeature);
        var $selectProgLanguage = $(selectProgLanguage);
        translate($selectFeature);
        translate($selectProgLanguage);
        $('#start .fixed-table-toolbar').append($selectFeature);
        $('#start .fixed-table-toolbar .btn-group:last-child').prepend($selectProgLanguage);
    }
    function initRobotList() {
        var myLang = GUISTATE_C.getLanguage();
        var startRobots = robots.slice(0, numPopularRobots);
        function clickRobot(e, value, row, optBookmark) {
            e.stopPropagation();
            $('.accordion-collapse.show').collapse('hide');
            var extensions = {};
            $(e.target)
                .closest('.card-views')
                .find('.form-check-input')
                .each(function () {
                if ($(this).prop('checked')) {
                    var extension = $(this).val().toString();
                    extensions[extension] = true;
                }
            });
            var robot = row.name;
            if (extensions.hasOwnProperty('blue') && extensions['blue'] && row.name === 'calliope2017NoBlue') {
                robot = 'calliope2017';
            }
            mainCallback(robot, extensions); // TODO call mainCallback(row.name, extensions)
            UTIL.cleanUri();
            if (optBookmark) {
                var uri;
                $(window).oneWrap('hashchange', function () {
                    window.history.replaceState({}, document.title, uri);
                });
                var extensionsArray = Object.keys(extensions).map(function (key) { return key; });
                uri = window.location.toString();
                uri += '?loadSystem=' + robot + '&extensions=' + extensionsArray.join(',');
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
        var myOptions = {
            columns: [
                {
                    field: 'rowNum',
                    sortable: true,
                    visible: false,
                    searchable: false
                },
                {
                    field: 'announcement',
                    title: '',
                    searchable: false,
                    formatter: function (announcement, row, index) {
                        var html = '<a href="#" class="pick typcn typcn-star-outline bookmark" rel="tooltip" data-bs-placement="top" data-bs-original-title="' +
                            Blockly.Msg.START_BOOKMARK_TOOLTIP +
                            '" aria-label="' +
                            Blockly.Msg.START_BOOKMARK_TOOLTIP +
                            '"  lkey="Blockly.Msg.START_BOOKMARK_TOOLTIP" title=""></a>';
                        if (announcement && announcement === 'beta') {
                            return html + '<img class=\'img-beta\' src=\'/css/img/beta.png\' alt=\'beta\' />';
                        }
                        else if (announcement && announcement === 'deprecated') {
                            return html + '<img class=\'img-deprecated\' src=\'/css/img/deprecated.png\' alt=\'deprecated\' />';
                        }
                        else {
                            return html;
                        }
                    },
                    events: {
                        'click .pick': clickBookmark
                    }
                },
                {
                    field: 'group',
                    sortable: true,
                    searchable: true,
                    visible: false
                },
                {
                    field: 'name',
                    title: '',
                    sortable: true,
                    searchable: false,
                    formatter: table_1.CardView.robotImage,
                    events: {
                        'click .robotImage': function (e, value, row) {
                            clickRobot(e, value, row, false);
                        }
                    }
                },
                {
                    field: 'progLanguage',
                    visible: false,
                    sortable: true,
                    searchable: false
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
                    }
                },
                {
                    title: '',
                    field: 'extensions',
                    searchable: false,
                    formatter: function (extensions, row, index) {
                        var myextensions = $('#startSelectionTemplate').html();
                        var $myextensions = $(myextensions);
                        $myextensions.addClass('invisible');
                        $myextensions.find('.accordion-button.collapsed').attr({ 'aria-controls': 'extend_' + index, 'data-bs-target': '#extend_' + index });
                        $myextensions.find('.accordion-collapse.collapse').attr({ id: 'extend_' + index });
                        for (var value in extensions) {
                            // sim is currently not selectable but always available if supported
                            if (value === 'sim') {
                                continue;
                            }
                            if (extensions[value] !== 'never') {
                                $myextensions.removeClass('invisible');
                                $myextensions
                                    .find('#extend_' + value)
                                    .parent()
                                    .removeClass('hidden');
                                if (extensions[value] === 'always') {
                                    $myextensions.find('#extend_' + value).attr({ checked: 'checked', disabled: true, readOnly: true });
                                }
                                translate($myextensions);
                            }
                        }
                        return $myextensions[0].outerHTML;
                    }
                },
                {
                    title: '',
                    field: 'startNow',
                    searchable: false,
                    formatter: function () {
                        return ('<button href="#" lkey="Blockly.Msg.START_START" class="btn start pick typcn typcn-arrow-right" role="button">' +
                            Blockly.Msg.START_START +
                            '</button>');
                    },
                    events: {
                        'click .pick': function (e, value, row) {
                            clickRobot(e, value, row, false);
                        }
                    }
                }
            ],
            locale: myLang,
            toolbar: '#robotListToolbar',
            cardView: 'true',
            search: true,
            rowStyle: {
                //classes: 'col-xxl-2  col-md-4 col-sm-6 col-xs-12',
                classes: 'col-xxl-3 col-sm-6 col-xs-12'
            },
            showRefresh: false,
            buttons: function () {
                return {
                    popularRobots: {
                        html: '<button class="nav-link active start" data-bs-toggle="button" autocomplete="off" aria-pressed="true" data-bs-original-title="" id="popularRobots" lkey="Blockly.Msg.START_POPULAR_ROBOTS">' +
                            Blockly.Msg.START_POPULAR_ROBOTS +
                            '</button>'
                    },
                    allRobots: {
                        html: '<button class="nav-link start" data-bs-toggle="button" autocomplete="off" aria-pressed="false" data-bs-original-title="" id="allRobots" lkey="Blockly.Msg.START_ALL_ROBOTS">' +
                            Blockly.Msg.START_ALL_ROBOTS +
                            '</button><span id="results"></span>&nbsp;<span id="labelResults" lkey="Blockly.Msg.START_RESULTS">' +
                            Blockly.Msg.START_RESULTS +
                            '</span>'
                    }
                };
            },
            buttonsAlign: 'left',
            filterControlContainer: true
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
        $('.mainTab').parent().addClass('invisible');
        $('.notStart').addClass('disabled');
        $('#header').removeClass('shadow');
        GUISTATE_C.resetRobot();
        GUISTATE_C.setView('tabStart');
    }
    function initRobotListEvents() {
        $('#tabStart').onWrap('show.bs.tab', function (e) {
            initView();
        });
        var popularRobots = function () {
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
            var myRobots = robots.slice(0, numPopularRobots);
            $('#robotTable').bootstrapTable('load', myRobots);
            $('#robotTable').bootstrapTable('resetSearch', '');
            $('#robotTable').bootstrapTable('sortBy', { field: 'rowNum', sortOrder: 'asc' });
            $('#robotTable').bootstrapTable('filterBy', []);
            $('#start').animate({ scrollTop: $('.section--white').height() }, 500);
        };
        $('#popularRobots').onWrap('click', popularRobots);
        var allRobots = function () {
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
            }
            else {
                $('#popularRobots').trigger('click');
                popularRobots();
            }
        });
        $('#robotTable').on('post-body.bs.table', function (e, data) {
            $('span#results').text(data.length);
            $('.bookmark').tooltip({ trigger: 'hover' });
        });
        $('#start .btn-group input').onWrap('change', function (e) {
            var myFilter = {};
            var myFilterAlgorithm = {
                filterAlgorithm: function (row, filters) {
                    var foundExt = true;
                    filters.extensions.forEach(function (filterExtension) {
                        if (!row.extensions[filterExtension]) {
                            foundExt = false;
                        }
                        else if (row.extensions[filterExtension] === 'never') {
                            foundExt = false;
                        }
                    });
                    var foundProgLang = false;
                    filters.progLanguage.forEach(function (filterProgLanguage) {
                        if (row.progLanguage === filterProgLanguage) {
                            foundProgLang = true;
                        }
                    });
                    return foundExt && foundProgLang;
                }
            };
            myFilter['progLanguage'] = [];
            myFilter['extensions'] = [];
            $('input.progLang').each(function () {
                if ($(this).prop('checked')) {
                    myFilter['progLanguage'].push($(this).val().toString());
                }
            });
            if (myFilter['progLanguage'].length === 0) {
                myFilter['progLanguage'] = uniqueProgLanguages;
            }
            $('input.feature').each(function () {
                if ($(this).prop('checked')) {
                    myFilter['extensions'].push($(this).val().toString());
                }
            });
            $('#robotTable').bootstrapTable('filterBy', myFilter, myFilterAlgorithm);
        });
        $('#startImportProg').onWrap('click', function (e) {
            e.stopPropagation();
            IMPORT_C.importXmlFromStart(mainCallback);
        }, 'import clicked');
        $('#takeATour').onWrap('click', function (e) {
            e.stopPropagation();
            mainCallback('ev3lejosv1', {}, function () {
                PROGRAM_C.newProgram(true);
                TOUR_C.start('welcome');
            });
        }, 'take a tour clicked');
    }
    function translate($element) {
        $element.find('[lkey]').each(function (index) {
            var lkey = $(this).attr('lkey');
            var key = lkey.replace('Blockly.Msg.', '');
            if ($(this).attr('rel') === 'tooltip') {
                $(this).attr('data-bs-original-title', Blockly.Msg[key]);
            }
            else {
                $(this).html(Blockly.Msg[key]);
            }
        });
    }
    function loadImages(names) {
        var imgPath = '/css/img/system_preview/';
        var i = 0;
        var numLoading = names.length;
        var onload = function () {
            --numLoading === 0;
        };
        var images = {};
        while (i < names.length) {
            var img = (images[names[i]] = new Image());
            img.onload = onload;
            img.onerror = function (e) {
                console.error(e);
            };
            img.src = imgPath + names[i++];
        }
        return images;
    }
    function fetchRSSFeed() {
        var errorFn = function () {
            // $('#section--news').remove(); Just do nothing
        };
        var successFn = function (response) {
            var $newsContainer = $('<section class="section--white" id="section--news"><div class="container-fluid"><div class="row justify-content-center"><div class="col col-12 col-xl-10 col-xxl-8 col-xxxl-6 my-4"><div class="teaser-news"><div class="row justify-content-between align-items-center"><div class="col col-12 col-md-6"><div class="teaser-news--header">Neues aus dem Open Roberta Lab</div><div class="teaser-news--body" id="teaser-news--rss"></div></div><div class="col col-12 col-md-6 col-lg-4 offset-md-0 offset-lg-2 mt-5 mt-md-0" id="teaser-news--rss-image"></div></div></div></div></div></div></section>');
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
});
