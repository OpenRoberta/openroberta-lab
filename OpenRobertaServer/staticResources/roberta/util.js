var UTIL = {};
(function($) {
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
    UTIL.setCookie = function(key, value) {
        var expires = new Date();
        expires.setTime(expires.getTime() + (30 * 24 * 60 * 60 * 1000));
        document.cookie = key + '=' + value + ';expires=' + expires.toUTCString();
    };

    /**
     * Get cookie
     * 
     * @memberof UTIL
     * 
     * @param {key}
     *            Key of the cookie to read
     */
    UTIL.getCookie = function(key) {
        var keyValue = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
        return keyValue ? keyValue[2] : null;
    };

    /**
     * Format date
     * 
     * @memberof UTIL
     * 
     * @param {date}
     *            date from server to be formatted
     */
    UTIL.formatDate = function(dateLong) {
        if (dateLong) {
            var date = new Date(dateLong);
            var datestring = ("0" + date.getDate()).slice(-2) + "." + ("0" + (date.getMonth() + 1)).slice(-2) + "." + date.getFullYear() + ", "
                    + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
            return datestring;
        }
        return "";
    };

    /**
     * Convert date into numeric value
     * 
     * @memberof UTIL
     * 
     * @param {d}
     *            date in the form 'dd.mm.yyyy, hh:mm:ss'
     */
    UTIL.parseDate = function(d) {
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
    };

    /**
     * Format result of server call for logging
     * 
     * @memberof UTIL
     * 
     * @param {result}
     *            Result-object from server call
     */
    UTIL.formatResultLog = function(result) {
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
    };

    /**
     * Extension of Jquery-datatables for sorting German date fields
     * 
     * @memberof UTIL
     */
    UTIL.initDataTables = function() {
        jQuery.extend(jQuery.fn.dataTableExt.oSort['date-de-asc'] = function(a, b) {
            a = UTIL.parseDate(a);
            b = UTIL.parseDate(b);
            return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },

        jQuery.fn.dataTableExt.oSort['date-de-desc'] = function(a, b) {
            a = UTIL.parseDate(a);
            b = UTIL.parseDate(b);
            return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        });
    };

    /**
     * Calculate height of data table
     * 
     * @memberof UTIL
     */
    UTIL.calcDataTableHeight = function() {
        return Math.round($(window).height() - 260);
    };

    UTIL.cacheBlocks = function() {
        userState.programBlocksSaved = null;
        userState.programBlocks = null;
        if (Blockly.mainWorkspace !== null) {
            var xmlProgram = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
            userState.programBlocksSaved = Blockly.Xml.domToText(xmlProgram);
            var blocks = Blockly.getMainWorkspace().getTopBlocks();
            for (var i = 0; i < blocks.length; i++) {
                if (blocks[i].type == "robControls_start") {
                    var pos = blocks[i].getRelativeToSurfaceXY();
                    blocks[i].moveBy(25 - pos.x, 25 - pos.y);
                    break;
                }
            }
            xmlProgram = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
            userState.programBlocks = Blockly.Xml.domToText(xmlProgram);
            var blocks = Blockly.getMainWorkspace().getTopBlocks();
            for (var i = 0; i < blocks.length; i++) {
                if (blocks[i].type == "robControls_start") {
                    var pos = blocks[i].getRelativeToSurfaceXY();
                    blocks[i].moveBy(25 - pos.x, 25 - pos.y);
                    break;
                }
            }
        }
    };

    UTIL.checkVisibility = function() {
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
    };

    UTIL.getBricklyFrame = function(id) {
        var iframe = $(id).get(0);
        var doc;

        if (iframe.contentWindow) {
            return iframe.contentWindow;
        }

        if (iframe.window) {
            return iframe.window;
        }

        if (!doc && iframe.contentDocument) {
            doc = iframe.contentDocument;
        }

        if (!doc && iframe.document) {
            doc = iframe.document;
        }

        if (doc && doc.defaultView) {
            return doc.defaultView;
        }

        if (doc && doc.parentWindow) {
            return doc.parentWindow;
        }

        return undefined;
    };
})($);
