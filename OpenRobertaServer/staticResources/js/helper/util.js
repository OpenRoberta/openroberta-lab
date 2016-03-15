define([ 'require', 'exports', 'roberta.user-state', 'message', 'log', 'jquery', 'jquery-ui', 'jquery-validate', 'datatables', 'bootstrap' ], function(require,
        exports) {

    var $ = require('jquery');
    var MSG = require('message');
    var LOG = require('log');
    var userState = require('roberta.user-state');

    /**
     * Set cookie
     * 
     * @memberof UTIL
     * 
     * @param {key}
     *            Key of the cookie
     * @param {value}
     *            Value of the cookie
     */
    function setCookie(key, value) {
        var expires = new Date();
        expires.setTime(expires.getTime() + (30 * 24 * 60 * 60 * 1000));
        document.cookie = key + '=' + value + ';expires=' + expires.toUTCString();
    }

    exports.setCookie = setCookie;

    /**
     * Get cookie
     * 
     * @memberof UTIL
     * 
     * @param {key}
     *            Key of the cookie to read
     */
    function getCookie(key) {
        var keyValue = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
        return keyValue ? keyValue[2] : null;
    }

    exports.getCookie = getCookie;

    /**
     * Format date
     * 
     * @memberof UTIL
     * 
     * @param {date}
     *            date from server to be formatted
     */
    function formatDate(dateLong) {
        if (dateLong) {
            var date = new Date(dateLong);
            var datestring = ("0" + date.getDate()).slice(-2) + "." + ("0" + (date.getMonth() + 1)).slice(-2) + "." + date.getFullYear() + ", "
                    + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
            return datestring;
        }
        return "";
    }

    exports.formatDate = formatDate;

    /**
     * Convert date into numeric value
     * 
     * @memberof UTIL
     * 
     * @param {d}
     *            date in the form 'dd.mm.yyyy, hh:mm:ss'
     */
    function parseDate(d) {
        if (d) {
            var dayPart = d.split(', ')[0];
            var timePart = d.split(', ')[1];
            var day = dayPart.split('.')[0];
            var month = dayPart.split('.')[1] - 1;
            var year = dayPart.split('.')[2];
            var hour = timePart.split(':')[0];
            var minute = timePart.split(':')[1];
            var second = timePart.split(':')[2];
            var mseconds = timePart.split('.')[1];
            var date = new Date(year, month, day, hour, minute, second, mseconds);
            return date.getTime();
        }
        return 0;
    }

    exports.parseDate = parseDate;

    /**
     * Format result of server call for logging
     * 
     * @memberof UTIL
     * 
     * @param {result}
     *            Result-object from server call
     */
    function formatResultLog(result) {
        var str = "{";
        var comma = false;
        for (key in result) {
            if (comma) {
                str += ',';
            } else {
                comma = true;
            }
            str += '"' + key + '":';
            if (result.hasOwnProperty(key)) {
                // The output of items is limited to the first 100 characters
                if (result[key].length > 100) {
                    str += '"' + JSON.stringify(result[key]).substring(1, 100) + ' ..."';
                } else {
                    str += JSON.stringify(result[key]);
                }
            }
        }
        str += '}';
        return str;
    }

    exports.formatResultLog = formatResultLog;

    /**
     * Extension of Jquery-datatables for sorting German date fields
     * 
     * @memberof UTIL
     */
    function initDataTables() {
        $.extend($.fn.dataTableExt.oSort['date-de-asc'] = function(a, b) {
            a = parseDate(a);
            b = parseDate(b);
            return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },

        $.fn.dataTableExt.oSort['date-de-desc'] = function(a, b) {
            a = parseDate(a);
            b = parseDate(b);
            return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        });
    }

    exports.initDataTables = initDataTables;

    /**
     * Calculate height of data table
     * 
     * @memberof exports
     */
    function calcDataTableHeight() {
        return Math.round($(window).height() - 260);
    }

    exports.calcDataTableHeight = calcDataTableHeight;

    function cacheBlocks(workspace) {
        userState.programBlocksSaved = null;
        userState.programBlocks = null;
        if (Blockly.mainWorkspace !== null) {
            var xmlProgram = Blockly.Xml.workspaceToDom(workspace);
            userState.programBlocksSaved = Blockly.Xml.domToText(xmlProgram);
            var blocks = workspace.getTopBlocks();
            for (var i = 0; i < blocks.length; i++) {
                if (blocks[i].type == "robControls_start") {
                    var pos = blocks[i].getRelativeToSurfaceXY();
                    blocks[i].moveBy(25 - pos.x, 25 - pos.y);
                    break;
                }
            }
            xmlProgram = Blockly.Xml.workspaceToDom(workspace);
            userState.programBlocks = Blockly.Xml.domToText(xmlProgram);
            var blocks = workspace.getTopBlocks();
            for (var i = 0; i < blocks.length; i++) {
                if (blocks[i].type == "robControls_start") {
                    var pos = blocks[i].getRelativeToSurfaceXY();
                    blocks[i].moveBy(25 - pos.x, 25 - pos.y);
                    break;
                }
            }
        }
    }

    exports.cacheBlocks = cacheBlocks;

    function checkVisibility() {
        var stateKey, eventKey, keys = {
            hidden : "visibilitychange",
            webkitHidden : "webkitvisibilitychange",
            mozHidden : "mozvisibilitychange",
            msHidden : "msvisibilitychange"
        };
        for (stateKey in keys) {
            if (stateKey in document) {
                eventKey = keys[stateKey];
                break;
            }
        }
        return function(c) {
            if (c) {
                document.addEventListener(eventKey, c);
            }
            return !document[stateKey];
        };
    }

    exports.checkVisibility = checkVisibility;

    function setFocusOnElement($elem) {
        setTimeout(function() {
            if ($elem.is(":visible") == true) {
                $elem.focus();
            }
        }, 800);
    }

    exports.setFocusOnElement = setFocusOnElement;

    function showSingleModal(customize, onSubmit, onHidden, validator) {
        customize();
        $('#single-modal-form').onWrap('submit', function(e) {
            e.preventDefault();
            onSubmit();
        });
        $('#single-modal').onWrap('hidden.bs.modal', function() {
            $('#single-modal-form').unbind('submit');
            $('#singleModalInput').val('');
            $('#single-modal-form').validate().resetForm();
            onHidden();
        });
        $('#single-modal-form').removeData('validator');
        $('#single-modal-form').validate(validator);
        setFocusOnElement($("#singleModalInput"));
        $("#single-modal").modal('show');
    }

    exports.showSingleModal = showSingleModal;

    /**
     * Handle result of server call
     * 
     * @param {result}
     *            Result-object from server call
     */
    function response(result) {
        LOG.info('result from server: ' + formatResultLog(result));
        if (result.rc != 'ok') {
            MSG.displayMessage(result.message, "POPUP", "");
        }
    }

    exports.response = response;

    /**
     * Rounds a number to required decimal
     * 
     * @param value
     *            {Number} - to be rounded
     * @param decimals
     *            {Number} - number of decimals after rounding
     * @return {Number} rounded number
     * 
     */
    function round(value, decimals) {
        return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
    }

    exports.round = round;

    /**
     * Get the sign of the number.
     * 
     * @param x
     *            {Number} -
     * @return {Number} - 1 if it is positive number o/w return -1
     */
    function sgn(x) {
        return (x > 0) - (x < 0);
    }

    exports.sgn = sgn;

});
