define(["require", "exports", "util", "guiState.controller", "jquery", "blockly", "bootstrap-table"], function (require, exports, UTIL, GUISTATE_C, $, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = void 0;
    /**
     * Initialize table of programs
     */
    function init() {
        initLogList();
        initLogListEvents();
    }
    exports.init = init;
    function initLogList() {
        $('#logTable').bootstrapTable({
            height: UTIL.calcDataTableHeight(),
            pageList: '[ 10, 25, All ]',
            toolbar: '#logListToolbar',
            showRefresh: 'true',
            showPaginationSwitch: 'true',
            pagination: 'true',
            buttonsAlign: 'right',
            rowStyle: rowStyle,
            resizable: 'true',
            iconsPrefix: 'typcn',
            icons: {
                paginationSwitchDown: 'typcn-document-text',
                paginationSwitchUp: 'typcn-book',
                refresh: 'typcn-delete',
            },
            columns: [
                {
                    title: 'no.',
                    sortable: true,
                    align: 'center',
                    width: '75px',
                    field: '0',
                },
                {
                    title: 'type',
                    sortable: true,
                    align: 'center',
                    width: '75px',
                    field: '1',
                },
                {
                    title: 'message',
                    field: '2',
                },
            ],
        });
        $('#logTable').bootstrapTable('togglePagination');
        $('#logList>.bootstrap-table')
            .find('button[name="refresh"]')
            .attr('title', '')
            .attr('rel', 'tooltip')
            .attr('data-placement', 'left')
            .attr('lkey', 'Blockly.Msg.BUTTON_EMPTY_LIST')
            .attr('data-original-title', Blockly.Msg.BUTTON_EMPTY_LIST)
            .tooltip('fixTitle');
    }
    function initLogListEvents() {
        $('#tabLogList').onWrap('show.bs.tab', function () {
            GUISTATE_C.setView('tabLogList');
        });
        $(window).resize(function () {
            $('#logTable').bootstrapTable('resetView', {
                height: UTIL.calcDataTableHeight(),
            });
        });
        $('#logList>.bootstrap-table')
            .find('button[name="refresh"]')
            .onWrap('click', function () {
            $('#logTable').bootstrapTable('removeAll');
            return false;
        }, 'empty log list clicked');
        $('#backLogList').onWrap('click', function () {
            $('#' + GUISTATE_C.getPrevView()).clickWrap();
            return false;
        }, 'back to previous view');
    }
    function rowStyle(row, index) {
        if (row[1] === '[[ERR ]] ') {
            return {
                classes: 'danger',
            };
        }
        else
            return {};
    }
});
