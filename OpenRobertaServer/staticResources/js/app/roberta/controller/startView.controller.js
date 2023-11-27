var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
define(["require", "exports", "guiState.controller", "table", "jquery", "bootstrap-table", "bootstrap-tagsinput"], function (require, exports, GUISTATE_C, table_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.switchLanguage = exports.init = void 0;
    var robots = [];
    var allRows = [];
    /**
     * Initialize table of programs
     */
    function init() {
        var r = GUISTATE_C.getRobots();
        delete r['0'];
        robots = Object.keys(r).map(function (index) {
            var robot = r[index];
            return robot;
        });
        console.log(robots);
        initRobotList();
        /* initGalleryToolbar();*/
        initGalleryListEvents();
    }
    exports.init = init;
    function switchLanguage() {
        $('#galleryTable').bootstrapTable('destroy');
        init();
        if (GUISTATE_C.getView() === 'tabGalleryList') {
            showView();
        }
    }
    exports.switchLanguage = switchLanguage;
    function initRobotToolbar() {
        var $selectRobot = $('<select id="filterRobot" class="filter form-select"></select>');
        $('#galleryList .search').prepend($selectRobot);
        var groups = getRobotGroups();
        var $filterField = $('#filterRobot');
        for (var group in groups) {
            $filterField.append(new Option(groups[group], group));
        }
        $filterField.append(new Option('All robots', 'all', true, true));
        var $selectOrderBy = $('<label>Order by:</label> <select class="filter form-select" id="fieldOrderBy">' +
            '<option selected="" value="4:desc">Newest</option>' +
            '<option value="4:asc">Oldest</option>' +
            '<option value="1:asc">Program name</option>' +
            '<option value="0:asc">Robot</option>' +
            '</select>');
        $('#galleryList .search').append($selectOrderBy);
    }
    function initRobotList() {
        var myLang = GUISTATE_C.getLanguage();
        var startRobots = robots.slice(0, 4);
        var myOptions = {
            columns: [
                {
                    field: 'anouncement',
                    visible: false,
                },
                {
                    field: 'group',
                    sortable: true,
                    title: '',
                    formatter: table_1.CardView.robot,
                },
                {
                    visible: false,
                },
                {
                    visible: false,
                },
                {
                    visible: false,
                },
                {
                    title: '',
                    field: 'realName',
                    sortable: true,
                    formatter: table_1.CardView.name,
                },
                {
                    visible: false,
                },
                {
                    visible: false,
                },
            ],
            filterControl: true,
            locale: myLang,
            rowAttributes: rowAttributes,
            toolbar: '#robotListToolbar',
            cardView: 'true',
            search: true,
            rowStyle: {
                classes: 'col-xxl-3 col-lg-3 col-md-6 col-sm-6 col-xs-12',
            },
            height: 152 + 68,
            showRefresh: false,
            buttons: function () {
                return {
                    popularRobots: {
                        html: '<button class="btn btn-outline-dark active start" data-bs-toggle="button" autocomplete="off" aria-pressed="true" data-original-title="" id="popularRobots" lkey="">' +
                            'Beliebte Systeme' +
                            '</button>',
                    },
                    allRobots: {
                        html: '<button class="btn btn-outline-dark start" data-bs-toggle="button" autocomplete="off" aria-pressed="false" data-original-title="" id="allRobots" lkey="">' +
                            'Alle Systeme und Filteroptionen' +
                            '</button>',
                    },
                };
            },
            buttonsAlign: 'left',
        };
        var options = __assign({}, myOptions);
        $('#robotTable').bootstrapTable(options);
        //$('#robotTable').bootstrapTable('togglePagination');
        $('#robotTable').bootstrapTable('load', startRobots);
    }
    function initGalleryListEvents() {
        $('#popularRobots').onWrap('click', function () {
            $('#allRobots').toggleClass('active');
            $('#allRobots').attr('aria-pressed', 'false');
            var startRobots = robots.slice(0, 4);
            //$('#robotTable').bootstrapTable('refreshOptions', { h
            // eigth: (startRobots.length / 4) * 152 + 68 });
            $('#robotTable').bootstrapTable('load', startRobots);
            console.log($('#robotTable').height());
            /* $('#robotTable').bootstrapTable('resetView', { heigth: $('#robotTable').height() });
             $('#robotTable').bootstrapTable('resetView');*/
        });
        $('#allRobots').onWrap('click', function () {
            $('#popularRobots').toggleClass('active');
            $('#popularRobots').attr('aria-pressed', 'false');
            $('#robotTable').bootstrapTable('load', robots);
            console.log($('#robotTable').height());
            /* $('#robotTable').bootstrapTable('resetView');
     
             $('#robotTable').bootstrapTable('resetView', { heigth: $('#robotTable').height() }); // (robots.length / 4) * $('#robotTable tr:first-child').height() + 68 });*/
        });
        $('#robotTable').on('post-body.bs.table', function (e) {
            console.log($('#robotTable').height() + '  ' + $('#start .fixed-table-toolbar').height());
            $('#robotTable').bootstrapTable('resetView', { heigth: $('#robotTable').height() + $('#start .fixed-table-toolbar').height() });
            $('#robotTable .fixed-table-container.fixed-height').height($('#robotTable').height());
        });
        /*$('#tabGalleryList').onWrap(
            'shown.bs.tab',
            function () {
                GUISTATE_C.setView('tabGalleryList');
                showView();
                return false;
            },
            'gallery clicked'
        );
    
        $(window).resize(function () {
            $('#galleryTable').bootstrapTable('resetView', {
                // @ts-ignore
                height: UTIL.calcDataTableHeight(),
            });
        });
    
        $('#tabGalleryList').onWrap(
            'shown.bs.tab',
            function (e) {
                $(window).trigger('resize');
            },
            'shown gallery list'
        );
    
        $('#galleryTable').onWrap(
            'post-body.bs.table',
            function (e) {
                configureTagsInput();
            },
            'page-change gallery list'
        );
    
        $('#backGalleryList').onWrap(
            'click',
            function () {
                $('#tabProgram').clickWrap();
                return false;
            },
            'back to program view'
        );
    
        $('#galleryTable').on('shown.bs.collapse hidden.bs.collapse', function (e) {
            $('#galleryTable').bootstrapTable('resetWidth');
        });
    
        //$('#filterRobot').onWrap('change', loadTableData, 'gallery filter changed');
    
        $('#fieldOrderBy').change(function (e) {
            // @ts-ignore
            var fieldData = e.target.value.split(':');
            var row = parseInt(fieldData[0]);
            $('#galleryTable').bootstrapTable('sortBy', {
                field: row,
                sortOrder: fieldData[1],
            });
            configureTagsInput();
        });*/
    }
    function showView() {
        $('#filterRobot').val(GUISTATE_C.getRobotGroup());
    }
    //TODO: Robot group names exists in plugin properties
    function getRobotGroups() {
        var robots = GUISTATE_C.getRobots();
        var groups = {};
        var coerceName = function (name, group) {
            if (group === 'arduino')
                return 'Nepo4Arduino';
            if (group === 'ev3')
                return 'Ev3';
            return GUISTATE_C.getMenuRobotRealName(name);
        };
        for (var propt in robots) {
            var group = robots[propt].group;
            var name = robots[propt].name;
            if (group && !groups[group]) {
                groups[group] = coerceName(name, group);
            }
        }
        return groups;
    }
    var rowAttributes = function (row, index) {
        return {
            style: 'background-color :#fff;' + 'cursor: pointer;  z-index: 1;',
        };
    };
    function configureTagsInput() {
        // @ts-ignore
        $('.infoTags').tagsinput();
        $('#galleryTable .bootstrap-tagsinput').addClass('galleryTags');
        $('#galleryList').find('.galleryTags>input').attr('readonly', 'true');
        $('#galleryList').find('span[data-role=remove]').addClass('hidden');
    }
});
