define(["require", "exports", "message", "log", "jquery", "blockly", "interpreter.util", "guiState.controller", "jquery-validate", "bootstrap"], function (require, exports, MSG, LOG, $, Blockly, U, GUISTATE_C) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getRobotGroupsPrettyPrint = exports.cleanUri = exports.RGBAToHexA = exports.addVariableValue = exports.toFixedPrecision = exports.extendMouseEvent = exports.getWebAudio = exports.initMicrophone = exports.isWebBleSupported = exports.isWebUsbSupported = exports.isChromeOS = exports.isWindowsOS = exports.isChromium = exports.isEdge = exports.isIE = exports.closeSimRobotWindow = exports.openSimRobotWindow = exports.checkInCircle = exports.removeLinks = exports.annotateBlocks = exports.clearAnnotations = exports.clearTabAlert = exports.alertTab = exports.isLocalStorageAvailable = exports.countBlocks = exports.getHashFrom = exports.download = exports.getBasename = exports.sgn = exports.round = exports.response = exports.showMsgOnTop = exports.showSingleListModal = exports.showSingleModal = exports.setFocusOnElement = exports.checkVisibility = exports.calcDataTableHeight = exports.formatResultLog = exports.parseDate = exports.formatDate = exports.setObjectProperty = exports.getPropertyFromObject = exports.isMobile = exports.isEmpty = exports.clone = exports.base64decode = exports.arrayToCsv = exports.csvToArray = exports.activationDisplayName = exports.renameNeuron = exports.getAllBlocks = exports.getTheStartBlock = exports.getLinesFromRectangle = void 0;
    var ANIMATION_DURATION = 750;
    function getLinesFromRectangle(myObj) {
        return [
            {
                x1: myObj.x,
                x2: myObj.x,
                y1: myObj.y,
                y2: myObj.y + myObj.h
            },
            {
                x1: myObj.x,
                x2: myObj.x + myObj.w,
                y1: myObj.y,
                y2: myObj.y
            },
            {
                x1: myObj.x + myObj.w,
                x2: myObj.x,
                y1: myObj.y + myObj.h,
                y2: myObj.y + myObj.h
            },
            {
                x1: myObj.x + myObj.w,
                x2: myObj.x + myObj.w,
                y1: myObj.y + myObj.h,
                y2: myObj.y
            }
        ];
    }
    exports.getLinesFromRectangle = getLinesFromRectangle;
    /**
     * @return the (unique) start block from the program. Must exist.
     */
    function getTheStartBlock() {
        var startBlock = null;
        for (var _i = 0, _a = Blockly.Workspace.getByContainer('blocklyDiv').getTopBlocks(); _i < _a.length; _i++) {
            var block = _a[_i];
            if (!block.isDeletable()) {
                return block;
            }
        }
        throw 'start block not found. That is impossible.';
    }
    exports.getTheStartBlock = getTheStartBlock;
    /**
     * @return all block from the program.
     */
    function getAllBlocks() {
        return Blockly.Workspace.getByContainer('blocklyDiv').getAllBlocks();
    }
    exports.getAllBlocks = getAllBlocks;
    /**
     * rename the block drop down for neuron names used in the program after a neuron was renamed
     *
     * @param {string}
     *            oldName Configuration title to rename.
     * @param {string}
     *            newName New configuration name.
     */
    function renameNeuron(oldName, newName) {
        var blocks = getAllBlocks();
        for (var x = 0; x < blocks.length; x++) {
            var block = blocks[x];
            if (!block.dependNeuron) {
                continue;
            }
            var dependNeuron = void 0;
            if (typeof block.dependNeuron === 'function') {
                dependNeuron = block.dependNeuron();
            }
            else {
                dependNeuron = block.dependNeuron;
            }
            var dropDown = dependNeuron.dropDown;
            if (!Array.isArray(dropDown)) {
                dropDown = [dropDown];
            }
            for (var d = 0; d < dropDown.length; d++) {
                var index = -1;
                for (var i = 0; i < dropDown[d].menuGenerator_.length; i++) {
                    if (dropDown[d].menuGenerator_[i][1] === oldName) {
                        index = i;
                        break;
                    }
                }
                if (index >= 0) {
                    dropDown[d].menuGenerator_[index][0] = newName;
                    dropDown[d].menuGenerator_[index][1] = newName;
                    if (dropDown[d].value_ === oldName) {
                        dropDown[d].setValue(newName);
                    }
                }
                else {
                    dropDown[d].menuGenerator_.push([newName, newName]);
                    if (dropDown[d].arrow_) {
                        dropDown[d].arrow_.replaceChild(document.createTextNode(dropDown[d].sourceBlock_.RTL ? Blockly.FieldDropdown.ARROW_CHAR + ' ' : ' ' + Blockly.FieldDropdown.ARROW_CHAR), dropDown[d].arrow_.childNodes[0]);
                    }
                    dropDown[d].render_();
                }
            }
            block.render();
        }
    }
    exports.renameNeuron = renameNeuron;
    exports.activationDisplayName = {
        linear: 'Linear',
        relu: 'ReLU',
        tanh: 'Tanh',
        sigmoid: 'Sigmoid',
        bool: 'Bool(0,1)'
    };
    var csvToArray = function (data, delimiter, omitFirstRow) {
        if (delimiter === void 0) { delimiter = ';'; }
        if (omitFirstRow === void 0) { omitFirstRow = false; }
        return data
            .slice(omitFirstRow ? data.indexOf('\n') + 1 : 0)
            .split('\n')
            .filter(function (val) { return val.length !== 0; })
            .map(function (val) { return val.split(delimiter); });
    };
    exports.csvToArray = csvToArray;
    var arrayToCsv = function (data, delimiter) {
        if (delimiter === void 0) { delimiter = ';'; }
        return data.map(function (v) { return v.join(delimiter); }).join('\n');
    };
    exports.arrayToCsv = arrayToCsv;
    var ratioWorkspace = 1;
    var simRobotWindowPositions = [];
    /**
     * Decode base64 string to array of bytes
     *
     * @param b64string
     *            A base64 encoded string
     */
    function base64decode(b64string) {
        var byteCharacters = atob(b64string);
        var byteNumbers = new Array(byteCharacters.length);
        for (var i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        return new Uint8Array(byteNumbers);
    }
    exports.base64decode = base64decode;
    function clone(obj) {
        var copy;
        // Handle the 3 simple types, and null or undefined
        if (null == obj || 'object' != typeof obj)
            return obj;
        // Handle Date
        if (obj instanceof Date) {
            copy = new Date();
            // @ts-ignore
            copy.setTime(obj.getTime());
            return copy;
        }
        // Handle Array
        if (obj instanceof Array) {
            copy = [];
            for (var i = 0, len = obj.length; i < len; i++) {
                copy[i] = clone(obj[i]);
            }
            return copy;
        }
        // Handle Object
        if (obj instanceof Object) {
            copy = {};
            for (var attr in obj) {
                if (obj.hasOwnProperty(attr))
                    copy[attr] = clone(obj[attr]);
            }
            return copy;
        }
        throw new Error('Unable to copy obj! Its type isn\'t supported.');
    }
    exports.clone = clone;
    function isEmpty(obj) {
        return Object.keys(obj).length === 0 && obj.constructor === Object;
    }
    exports.isEmpty = isEmpty;
    function isMobile() {
        return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
    }
    exports.isMobile = isMobile;
    function getPropertyFromObject(obj, prop, arrayIndex) {
        //property not found
        if (typeof obj === 'undefined')
            return false;
        //index of next property split
        var _index = prop.indexOf('.');
        //property split found; recursive call
        if (_index > -1) {
            //get object at property (before split), pass on remainder
            return getPropertyFromObject(obj[prop.substring(0, _index)], prop.substr(_index + 1), arrayIndex);
        }
        //no split; get property
        if (arrayIndex != undefined) {
            return obj[prop][arrayIndex];
        }
        return obj[prop];
    }
    exports.getPropertyFromObject = getPropertyFromObject;
    function setObjectProperty(obj, prop, value, arrayIndex) {
        //property not found
        if (typeof obj === 'undefined')
            return false;
        //index of next property split
        var _index = prop.indexOf('.');
        //property split found; recursive call
        if (_index > -1) {
            //get object at property (before split), pass on remainder
            return setObjectProperty(obj[prop.substring(0, _index)], prop.substr(_index + 1), value, arrayIndex);
        }
        //no split; get property
        if (arrayIndex != undefined) {
            return (obj[prop][arrayIndex] = value);
        }
        obj[prop] = value;
    }
    exports.setObjectProperty = setObjectProperty;
    /**
     * Format date
     *
     * @param {date}
     *            date from server to be formatted
     */
    function formatDate(dateLong) {
        if (dateLong) {
            var date = new Date(dateLong);
            var datestring = ('0' + date.getDate()).slice(-2) +
                '.' +
                ('0' + (date.getMonth() + 1)).slice(-2) +
                '.' +
                date.getFullYear() +
                ', ' +
                ('0' + date.getHours()).slice(-2) +
                ':' +
                ('0' + date.getMinutes()).slice(-2);
            return datestring;
        }
        else {
            return '';
        }
    }
    exports.formatDate = formatDate;
    /**
     * Convert date into numeric value
     *
     * @param {d}
     *            date in the form 'dd.mm.yyyy, hh:mm:ss'
     */
    function parseDate(d) {
        if (d) {
            var dayPart = d.split(', ')[0];
            var timePart = d.split(', ')[1];
            var day = Number(dayPart.split('.')[0]);
            var month = Number(dayPart.split('.')[1]) - 1;
            var year = Number(dayPart.split('.')[2]);
            var hour = Number(timePart.split(':')[0]);
            var minute = Number(timePart.split(':')[1]);
            var second = Number(timePart.split(':')[2]);
            var mseconds = Number(timePart.split('.')[1]);
            var date = new Date(year, month, day, hour, minute, second, mseconds);
            return date.getTime();
        }
        return 0;
    }
    exports.parseDate = parseDate;
    /**
     * Format result of server call for logging
     *
     * @param {result}
     *            Result-object from server call
     */
    function formatResultLog(result) {
        var str = '{';
        var comma = false;
        for (var key in result) {
            if (comma) {
                str += ',';
            }
            else {
                comma = true;
            }
            str += '"' + key + '":';
            if (result.hasOwnProperty(key)) {
                // The output of items is limited to the first 100 characters
                if (result[key].length > 100) {
                    str += '"' + JSON.stringify(result[key]).substring(1, 100) + ' ..."';
                }
                else {
                    str += JSON.stringify(result[key]);
                }
            }
        }
        str += '}';
        return str;
    }
    exports.formatResultLog = formatResultLog;
    /**
     * Calculate height of data table
     */
    function calcDataTableHeight() {
        return Math.round($(window).height() - 100);
    }
    exports.calcDataTableHeight = calcDataTableHeight;
    function checkVisibility() {
        var stateKey, eventKey, keys = {
            hidden: 'visibilitychange',
            webkitHidden: 'webkitvisibilitychange',
            mozHidden: 'mozvisibilitychange',
            msHidden: 'msvisibilitychange'
        };
        for (stateKey in keys) {
            if (stateKey in document) {
                eventKey = keys[stateKey];
                break;
            }
        }
        return function (c) {
            if (c) {
                document.addEventListener(eventKey, c);
            }
            return !document[stateKey];
        };
    }
    exports.checkVisibility = checkVisibility;
    function setFocusOnElement($elem) {
        setTimeout(function () {
            if ($elem.is(':visible') == true) {
                $elem.focus();
            }
        }, 800);
    }
    exports.setFocusOnElement = setFocusOnElement;
    function showSingleModal(customize, onSubmit, onHidden, validator) {
        customize();
        $('#single-modal-form').onWrap('submit', function (e) {
            e.preventDefault();
            onSubmit();
        }, 'sim start clicked');
        $('#single-modal').onWrap('hidden.bs.modal', function () {
            $('#single-modal-form').off('submit');
            $('#singleModalInput').val('');
            $('#single-modal-form').validate().resetForm();
            onHidden();
        }, 'sim start clicked');
        $('#single-modal-form').removeData('validator');
        $('#single-modal-form').validate(validator);
        setFocusOnElement($('#singleModalInput'));
        $('#single-modal').modal('show');
    }
    exports.showSingleModal = showSingleModal;
    function showSingleListModal(customize, onSubmit, onHidden, validator) {
        $('#single-modal-list-form').onWrap('submit', function (e) {
            e.preventDefault();
            onSubmit();
        }, 'sim start clicked');
        $('#single-modal-list').onWrap('hidden.bs.modal', function () {
            $('#single-modal-list-form').unbind('submit');
            onHidden();
        }, 'sim start clicked');
        setFocusOnElement($('#singleModalListInput'));
        $('#single-modal-list').modal('show');
    }
    exports.showSingleListModal = showSingleListModal;
    /**
     * Helper to show the information on top of the share modal.
     *
     */
    function showMsgOnTop(msg) {
        $('#show-message').find('button').removeAttr('data-bs-dismiss');
        $('#show-message')
            .find('button')
            .oneWrap('click', function (e) {
            $('#show-message').modal('hide');
            $('#show-message').find('button').attr('data-bs-dismiss', 'modal');
        });
        MSG.displayInformation({
            rc: 'not ok'
        }, '', msg);
    }
    exports.showMsgOnTop = showMsgOnTop;
    /**
     * Handle result of server call
     *
     * @param {result}
     *            Result-object from server call
     */
    function response(result) {
        LOG.info('result from server: ' + formatResultLog(result));
        if (result.rc != 'ok') {
            MSG.displayMessage(result.message, 'POPUP', '');
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
        return parseFloat(value.toFixed(decimals));
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
        return Number(x > 0) - Number(x < 0);
    }
    exports.sgn = sgn;
    /**
     * Returns the basename (i.e. "hello" in "C:/folder/hello.txt")
     *
     * @param path
     *            {String} - path
     */
    function getBasename(path) {
        var base = String(path).substring(path.lastIndexOf('/') + 1);
        if (base.lastIndexOf('.') != -1) {
            base = base.substring(0, base.lastIndexOf('.'));
        }
        return base;
    }
    exports.getBasename = getBasename;
    function destroyClickedElement(event) {
        document.body.removeChild(event.target);
    }
    function download(fileName, content) {
        if ('Blob' in window && navigator.userAgent.toLowerCase().match(/iPad|iPhone|Android/i) == null) {
            var contentAsBlob = new Blob([content], {
                type: 'application/octet-stream'
            });
            if ('msSaveOrOpenBlob' in navigator) {
                //@ts-ignore
                navigator.msSaveOrOpenBlob(contentAsBlob, fileName);
            }
            else {
                var downloadLink_1 = document.createElement('a');
                downloadLink_1.download = fileName;
                downloadLink_1.innerHTML = 'Download File';
                downloadLink_1.href = window.URL.createObjectURL(contentAsBlob);
                downloadLink_1.onclick = destroyClickedElement;
                downloadLink_1.style.display = 'none';
                document.body.appendChild(downloadLink_1);
                setTimeout(function () {
                    downloadLink_1.click();
                }, 0);
            }
        }
        else {
            var downloadLink_2 = document.createElement('a');
            downloadLink_2.setAttribute('href', 'data:text/' + fileName.substring(fileName.indexOf('.') + 1) + ';charset=utf-8,' + encodeURIComponent(content));
            downloadLink_2.setAttribute('download', fileName);
            downloadLink_2.style.display = 'none';
            document.body.appendChild(downloadLink_2);
            downloadLink_2.onclick = destroyClickedElement;
            setTimeout(function () {
                downloadLink_2.click();
            }, 0);
        }
    }
    exports.download = download;
    function getHashFrom(string) {
        var hash = 0;
        for (var i = 0; i < string.length; i++) {
            hash = (hash << 5) - hash + string.charCodeAt(i++);
        }
        return hash < 0 ? hash * -1 + 0xffffffff : hash;
    }
    exports.getHashFrom = getHashFrom;
    function countBlocks(xmlString) {
        var counter = 0;
        var pos = 0;
        while (true) {
            pos = xmlString.indexOf('<block', pos);
            if (pos != -1) {
                counter++;
                pos += 6;
            }
            else {
                break;
            }
        }
        return counter - 1;
    }
    exports.countBlocks = countBlocks;
    function isLocalStorageAvailable() {
        try {
            localStorage.setItem('test', 'test');
            localStorage.removeItem('test');
            return true;
        }
        catch (e) {
            return false;
        }
    }
    exports.isLocalStorageAvailable = isLocalStorageAvailable;
    function alertTab(tabIdentifier) {
        clearTabAlert(tabIdentifier);
        $('#' + tabIdentifier).width(); // trigger a reflow to sync animations
        $('#' + tabIdentifier).prepend('<span class="typcn typcn-warning-outline"></span>'); // add alert typicon
        $('#' + tabIdentifier).addClass('blinking');
    }
    exports.alertTab = alertTab;
    function clearTabAlert(tabIdentifier) {
        $('#' + tabIdentifier)
            .children()
            .remove('.typcn'); // remove alert typicon
        $('#' + tabIdentifier).removeClass('blinking');
    }
    exports.clearTabAlert = clearTabAlert;
    var __entityMap = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        '\'': '&#39;',
        '/': '&#x2F;'
    };
    //@ts-ignore
    String.prototype.escapeHTML = function () {
        return String(this).replace(/[&<>"'\/]/g, function (s) {
            return __entityMap[s];
        });
    };
    $.fn.draggable = function (opt) {
        opt = $.extend({
            handle: '',
            cursor: 'move',
            draggableClass: 'draggable',
            activeHandleClass: 'active-handle'
        }, opt);
        var $selected = null;
        var $elements = opt.handle === '' ? this : this.find(opt.handle);
        $elements
            .css('cursor', opt.cursor)
            .on('mousedown touchstart', function (e) {
            var pageX = e.pageX || e.originalEvent.touches[0].pageX;
            var pageY = e.pageY || e.originalEvent.touches[0].pageY;
            if (opt.handle === '') {
                $selected = $(this);
                $selected.addClass(opt.draggableClass);
            }
            else {
                $selected = $(this).parent();
                $selected.addClass(opt.draggableClass).find(opt.handle).addClass(opt.activeHandleClass);
            }
            var drg_h = $selected.outerHeight(), drg_w = $selected.outerWidth(), pos_y = $selected.offset().top + drg_h - pageY, pos_x = $selected.offset().left + drg_w - pageX;
            $(document)
                .on('mousemove touchmove', function (e) {
                var pageX = e.pageX || e.originalEvent.touches[0].pageX;
                var pageY = e.pageY || e.originalEvent.touches[0].pageY;
                var newXPosition = pageX + pos_x - drg_w;
                var newYPosition = pageY + pos_y - drg_h;
                if (opt.constraint == 'window') {
                    if (newXPosition >= $(window).width() - 19) {
                        newXPosition = $(window).width() - 20;
                    }
                    else if (newXPosition <= 19 - $selected.width()) {
                        newXPosition = 18 - $selected.width();
                    }
                    var headerSize = 92;
                    if (newYPosition >= $(window).height() - 19) {
                        newYPosition = $(window).height() - 20;
                    }
                    else if (newYPosition <= 19 - $selected.height() + headerSize) {
                        newYPosition = 18 - $selected.height() + headerSize;
                    }
                }
                // special case movable slider between workspace and right divs
                if (opt.axis == 'x') {
                    var left = pageX + pos_x - drg_w;
                    left = Math.min(left, $('#main-section').width() - 24);
                    left = Math.max(left, 42);
                    $selected.offset({
                        top: 0,
                        left: left
                    });
                    $('#blocklyDiv').outerWidth(left);
                    /* $('.rightMenuButton').css({
                        right: $(window).width() - left,
                    });*/
                    $('.fromRight').css({
                        width: $('#main-section').width() - $('#blocklyDiv').outerWidth()
                    });
                    ratioWorkspace = $('#blocklyDiv').outerWidth() / $('#main-section').width();
                    $(window).trigger('resize');
                }
                else {
                    $selected.offset({
                        top: newYPosition,
                        left: newXPosition
                    });
                }
                $selected.css({
                    right: 'auto'
                });
            })
                .on('mouseup touchend', function () {
                $(document).off('mousemove touchmove'); // Unbind events from document
                if ($selected !== null) {
                    $selected.removeClass(opt.draggableClass);
                    $selected = null;
                }
            });
        })
            .on('mouseup touchend', function () {
            if ($selected) {
                if (opt.handle === '') {
                    $selected.removeClass(opt.draggableClass);
                }
                else {
                    $selected.removeClass(opt.draggableClass).find(opt.handle).removeClass(opt.activeHandleClass);
                }
            }
            $selected = null;
        });
        return this;
    };
    var originalAddClass = $.fn.addClass;
    $.fn.addClass = function () {
        var result = originalAddClass.apply(this, arguments);
        $(this).trigger('classChange');
        return result;
    };
    var originalRemoveClass = $.fn.removeClass;
    $.fn.removeClass = function () {
        var result = originalRemoveClass.apply(this, arguments);
        $(this).trigger('classChange');
        return result;
    };
    $.fn.toggleSimPopup = function (position) {
        if ($(this).is(':hidden')) {
            $(this).css({
                top: position.top + $('#header').height() + 12,
                left: position.left
            });
        }
        $(this).animate({
            opacity: 'toggle',
            top: 'toggle'
        }, 300);
        $(this).draggable({
            constraint: 'window'
        });
    };
    $.fn.closeRightView = function (opt_callBack) {
        if ($('.fromRight.rightActive').hasClass('shifting')) {
            return;
        }
        $('.fromRight.rightActive').addClass('shifting');
        $('.blocklyToolboxDiv').css('display', 'inherit');
        var that = this; //$('#blocklyDiv')
        $('.fromRight.rightActive').animate({
            width: 0
        }, {
            duration: ANIMATION_DURATION,
            start: function () {
                $('.modal').modal('hide');
                $('.rightMenuButton.rightActive').removeClass('rightActive');
            },
            step: function (now) {
                $(window).trigger('resize');
                that.width($('#main-section').width() - Math.ceil(now));
                /*$('.rightMenuButton').css('right', now);*/
                ratioWorkspace = $('#blocklyDiv').outerWidth() / $('#main-section').outerWidth();
            },
            done: function () {
                that.width($('#main-section').outerWidth());
                /* $('.rightMenuButton').css('right', 0);*/
                ratioWorkspace = 1;
                $('.fromRight').width(0);
                that.removeClass('rightActive');
                $('.fromRight.rightActive').removeClass('rightActive');
                $('#sliderDiv').hide();
                $(window).trigger('resize');
                if (typeof opt_callBack == 'function') {
                    opt_callBack();
                }
                $('.fromRight').trigger('closed');
            },
            always: function () {
                $('.fromRight.shifting').removeClass('shifting');
            }
        });
    };
    $.fn.openRightView = function ($view, initialViewWidth, opt_callBack) {
        if ($('.fromRight.rightActive').hasClass('shifting')) {
            return;
        }
        var $blockly = $('#blocklyDiv');
        var width;
        var smallScreen;
        if ($(window).width() < 768) {
            smallScreen = true;
            width = $blockly.outerWidth() - 52;
        }
        else {
            smallScreen = false;
            width = $blockly.outerWidth() * initialViewWidth;
        }
        if ($blockly.hasClass('rightActive')) {
            $('.fromRight.rightActive').removeClass('rightActive');
            $('.rightMenuButton.rightActive').removeClass('rightActive');
            $view.addClass('rightActive');
            $(this).addClass('rightActive');
            $(window).trigger('resize');
            if (smallScreen) {
                $('.blocklyToolboxDiv').css('display', 'none');
            }
            if (typeof opt_callBack == 'function') {
                opt_callBack();
            }
            if (!$(this).attr('id').startsWith('sim')) {
                closeSimRobotWindow();
            }
            $('.fromRight').trigger('closed');
            return;
        }
        $blockly.addClass('rightActive');
        $view.addClass('shifting rightActive');
        $(this).addClass('rightActive');
        $('.fromRight.rightActive').animate({
            width: width
        }, {
            duration: ANIMATION_DURATION,
            step: function (now, tween) {
                $blockly.outerWidth($('#main-section').width() - now);
                /*$('.rightMenuButton').css('right', Math.floor(now));*/
                ratioWorkspace = $('#blocklyDiv').outerWidth() / $('#main-section').width();
                $(window).trigger('resize');
            },
            done: function () {
                $('#sliderDiv').show();
                $blockly.outerWidth($('#main-section').width() - $('.fromRight.rightActive').width());
                /*  $('.rightMenuButton').css('right', $('.fromRight.rightActive').width());*/
                ratioWorkspace = $('#blocklyDiv').outerWidth() / $('#main-section').width();
                $(window).trigger('resize');
                if (smallScreen) {
                    $('.blocklyToolboxDiv').css('display', 'none');
                }
                $('#sliderDiv').css({
                    left: $blockly.outerWidth()
                });
                if (typeof opt_callBack == 'function') {
                    opt_callBack();
                }
            },
            always: function () {
                $view.removeClass('shifting');
            }
        });
    };
    $(window).on('resize', function () {
        var mainWidth = $('#main-section').width();
        var parentWidth = mainWidth;
        var height = $('#main-section').height(); //Math.max($('#blocklyDiv').outerHeight(), $('#brickly').outerHeight());
        var rightWidth = (1 - ratioWorkspace) * parentWidth;
        var leftWidth = ratioWorkspace * parentWidth;
        if (!$('.fromRight.rightActive.shifting').length > false) {
            if ($('.fromRight.rightActive').length > 0) {
                $('.fromRight.rightActive').width(rightWidth);
                /*$('.rightMenuButton').css('right', rightWidth);*/
                $('#sliderDiv').css('left', leftWidth);
            }
            $('#blocklyDiv').outerWidth(leftWidth);
        }
        else {
            leftWidth = $('#blocklyDiv').outerWidth();
        }
        if ($('#blocklyDiv')) {
            $('#blocklyDiv').outerWidth(leftWidth);
            $('#blocklyDiv').height(height);
            $('.blocklyToolboxDiv').height($('.blocklyToolboxDiv').height() - 36);
            if (leftWidth < 768) {
                $('#program .blocklyToolboxDiv').addClass('small');
            }
            else {
                $('#program .blocklyToolboxDiv').removeClass('small');
            }
        }
        if ($('#bricklyDiv')) {
            $('#bricklyDiv').width(parentWidth);
            $('#bricklyDiv').height(height);
            if (parentWidth < 768) {
                $('#configuration .blocklyToolboxDiv').addClass('small');
            }
            else {
                $('#configuration .blocklyToolboxDiv').removeClass('small');
            }
        }
        $('.simWindow:visible').each(function (index, robotWindowElement) {
            if (robotWindowElement.offsetLeft >= $(window).width() - 20) {
                $('#' + robotWindowElement.id).css({
                    left: '' + ($(window).width() - 20)
                });
            }
            if (robotWindowElement.offsetTop >= $(window).height() - 20) {
                $('#' + robotWindowElement.id).css({
                    top: '' + ($(window).height() - 20)
                });
            }
        });
        // here comes a fix for a strange browser behavior while zoom is not 100%. It is just in case (e.g. chrome 125% works fine, 110% not).
        // Seems that either the returned sizes from the browser sometimes include margins/borders and sometimes not or that the assigned sizes behave
        // different (with and without margins/borders).
        var diff = mainWidth - $('#blocklyDiv').outerWidth() - rightWidth;
        if (diff != 0) {
            $('#blocklyDiv').outerWidth(leftWidth + diff);
        }
        var workspace = Blockly.getMainWorkspace();
        if (workspace) {
            Blockly.svgResize(workspace);
        }
    });
    /**
     * Remove error and warning annotation from all blocks located in this
     * workspace. Usually this is done with a reload of all blocks, but here we
     * only want to remove the annotations.
     *
     * @param {workspacee}
     *            workspace
     */
    function clearAnnotations(workspace) {
        if (workspace) {
            var allBlocks = workspace.getAllBlocks();
            for (var i = 0; i < allBlocks.length; i++) {
                var icons = allBlocks[i].getIcons();
                for (var k = 0; k < icons.length; k++) {
                    var block = icons[k].block_;
                    if (block.error) {
                        block.error.dispose();
                        block.render();
                    }
                    else if (block.warning) {
                        block.warning.dispose();
                        block.render();
                    }
                }
            }
        }
    }
    exports.clearAnnotations = clearAnnotations;
    /**
     * Annotate the visible configuration blocks with warnings and errors
     * generated server side.
     *
     * @param {object}
     *            confAnnos - {block id, {type of annotation, message key}}
     */
    function annotateBlocks(workspace, annotations) {
        for (var annoId in annotations) {
            var block = workspace.getBlockById(annoId);
            if (block) {
                var anno = annotations[annoId];
                for (var annoType in anno) {
                    var annoMsg = Blockly.Msg[anno[annoType]] || anno[annoType] || 'unknown error';
                    switch (annoType) {
                        case 'ERROR':
                            block.setErrorText(annoMsg);
                            block.error.setVisible(true);
                            break;
                        case 'WARNING':
                            block.setWarningText(annoMsg);
                            block.warning.setVisible(true);
                            break;
                        default:
                            console.warn('Unsupported annotation: ' + annoType);
                    }
                }
            }
        }
    }
    exports.annotateBlocks = annotateBlocks;
    function removeLinks($elem) {
        $elem
            .filter(function () {
            return $(this).attr('href') && ($(this).attr('href').indexOf('http') === 0 || $(this).attr('href').indexOf('javascript:linkTo') === 0);
        })
            .each(function () {
            $(this).removeAttr('href');
        });
    }
    exports.removeLinks = removeLinks;
    function checkInCircle(px, py, cx, cy, r) {
        return (px - cx) * (px - cx) + (py - cy) * (py - cy) <= r * r;
    }
    exports.checkInCircle = checkInCircle;
    /**
     * open simRobotWindow if it was previously closed with
     * closeSimRobotWindow() and the robot has not been changed
     * @param duration
     *            {Number} - duration (optional) how long the simRobotWindow should take to show
     */
    function openSimRobotWindow() {
        $('.simWindow-openedButHidden').each(function (index, robotWindowElement) {
            var position = $(window).width() * simRobotWindowPositions[robotWindowElement.id];
            $('#' + robotWindowElement.id).animate({
                opacity: 'show',
                left: '' + position
            }, ANIMATION_DURATION);
        });
        $('.simWindow').removeClass('simWindow-openedButHidden');
    }
    exports.openSimRobotWindow = openSimRobotWindow;
    /**
     * close SimRobotWindow and remember it
     * closing SimRobotWindow using this method will show it again if opeSimRobotWindow() is called
     * @param duration
     *            {Number} - duration (optional) how long the simRobotWindow should take to hide
     */
    function closeSimRobotWindow() {
        var SimWindows = $('.simWindow:visible');
        SimWindows.each(function (index, robotWindowElement) {
            var relativePosition;
            if ($(window).width() !== 0) {
                relativePosition = Math.abs((robotWindowElement.offsetLeft / $(window).width()) % 1);
            }
            else {
                relativePosition = 0;
            }
            simRobotWindowPositions[robotWindowElement.id] = relativePosition;
        });
        SimWindows.addClass('simWindow-openedButHidden').animate({
            opacity: 'hide',
            left: '' + $(window).width()
        }, ANIMATION_DURATION);
    }
    exports.closeSimRobotWindow = closeSimRobotWindow;
    function isIE() {
        var ua = window.navigator.userAgent;
        var ie = ua.indexOf('MSIE ');
        var ie11 = ua.indexOf('Trident/');
        if (ie > -1 || ie11 > -1) {
            return true;
        }
        return false;
    }
    exports.isIE = isIE;
    function isEdge() {
        var ua = window.navigator.userAgent;
        var edge = ua.indexOf('Edge');
        return edge > -1;
    }
    exports.isEdge = isEdge;
    function isChromium() {
        var ua = window.navigator.userAgent;
        var chrome = ua.indexOf('Chrome');
        return chrome > -1;
    }
    exports.isChromium = isChromium;
    function isIOS() {
        return navigator.userAgent.toLowerCase().match(/iPad|iPhone/i) !== null;
    }
    function isWindowsOS() {
        return navigator.userAgent.indexOf('Windows') != -1;
    }
    exports.isWindowsOS = isWindowsOS;
    function isChromeOS() {
        return navigator.userAgent.indexOf('CrOS') != -1;
    }
    exports.isChromeOS = isChromeOS;
    function isWebUsbSupported() {
        //webUSB is currently only supported by chromium browsers like google chrome, Edge, Microsoft Edge, etc.
        //webUSB is currently not supported on any IOS device
        return isChromium() && !isIOS();
    }
    exports.isWebUsbSupported = isWebUsbSupported;
    function isWebBleSupported() {
        //older versions of opera without chromium will not support webble
        return isChromium() && !isIOS();
    }
    exports.isWebBleSupported = isWebBleSupported;
    function initMicrophone(robot) {
        // TODO if (navigator.mediaDevices === undefined) {
        //navigator.mediaDevices = {};
        //}
        navigator.mediaDevices.getUserMedia = navigator.mediaDevices.getUserMedia || navigator['webkitGetUserMedia'] || navigator['mozGetUserMedia'];
        try {
            // ask for an audio input
            var mediaDevices = navigator.mediaDevices;
            mediaDevices
                .getUserMedia({
                audio: {
                    echoCancellation: false,
                    autoGainControl: false,
                    noiseSuppression: false,
                    //@ts-ignore
                    highpassFilter: false
                }
            })
                .then(function (stream) {
                var mediaStreamSource = robot.webAudio.context.createMediaStreamSource(stream);
                //@ts-ignore
                robot.sound = Volume.createAudioMeter(robot.webAudio.context);
                mediaStreamSource.connect(robot.sound);
            }, function () {
                console.log('Sorry, but there is no microphone available on your system');
            });
        }
        catch (e) {
            console.log('Sorry, but there is no microphone available on your system');
        }
    }
    exports.initMicrophone = initMicrophone;
    var thisWebAudio;
    function getWebAudio() {
        if (!thisWebAudio) {
            thisWebAudio = new (/** @class */ (function () {
                function class_1() {
                }
                return class_1;
            }()))();
            var AudioContext_1 = window.AudioContext || window['webkitAudioContext'] || false;
            if (AudioContext_1) {
                thisWebAudio.context = AudioContext_1;
            }
            else {
                thisWebAudio.context = null;
                //@ts-ignore
                thisWebAudio.oscillator = null;
                console.log('Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox');
            }
        }
        return thisWebAudio;
    }
    exports.getWebAudio = getWebAudio;
    function extendMouseEvent(e, scale, $layer) {
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $layer.offset().top;
        var left = $layer.offset().left;
        // @ts-ignore
        e.startX = (X - left) / scale;
        // @ts-ignore
        e.startY = (Y - top) / scale;
    }
    exports.extendMouseEvent = extendMouseEvent;
    function toFixedPrecision(value, precision) {
        var power = Math.pow(10, precision || 0);
        return String(Math.round(Number(value) * power) / power);
    }
    exports.toFixedPrecision = toFixedPrecision;
    function addVariableValue($elem, name, value) {
        if (value === undefined) {
            return;
        }
        switch (typeof value) {
            case 'number': {
                $elem.append('<div><label>' + name + ' :  </label><span> ' + round(value, 2) + '</span></div>');
                break;
            }
            case 'string':
            case 'boolean': {
                $elem.append('<div><label>' + name + ' :  </label><span> ' + value + '</span></div>');
                break;
            }
            case 'object': {
                if (value === null) {
                    $elem.append('<div><label>' + name + ' :  </label><span> null </span></div>');
                }
                else {
                    //@ts-ignore
                    for (var i = 0; i < value.length; i++) {
                        addVariableValue($elem, name + ' [' + String(i) + ']', value[i]);
                    }
                }
                break;
            }
            default: {
                U.warn('unexpected variable type received');
                break;
            }
        }
    }
    exports.addVariableValue = addVariableValue;
    function RGBAToHexA(rgba) {
        var r = (+rgba[0]).toString(16), g = (+rgba[1]).toString(16), b = (+rgba[2]).toString(16), a = (+rgba[3]).toString(16);
        if (r.length == 1)
            r = '0' + r;
        if (g.length == 1)
            g = '0' + g;
        if (b.length == 1)
            b = '0' + b;
        if (a.length == 1)
            a = '0' + a;
        return '#' + r + g + b + a;
    }
    exports.RGBAToHexA = RGBAToHexA;
    function cleanUri() {
        var location = new URL(document.location.toString());
        var clean_uri = location.protocol + '//' + location.host;
        window.history.replaceState({}, document.title, clean_uri);
    }
    exports.cleanUri = cleanUri;
    //TODO: Robot group names exists in plugin properties
    function getRobotGroupsPrettyPrint(opt_robotGroup) {
        var robots = GUISTATE_C.getRobots();
        var groups = {};
        var coerceName = function (name, group) {
            if (group === 'arduino')
                return 'Nepo4Arduino';
            if (group === 'ev3')
                return 'Ev3';
            return GUISTATE_C.getMenuRobotRealName(name);
        };
        if (opt_robotGroup) {
            return coerceName(opt_robotGroup, opt_robotGroup);
        }
        for (var propt in robots) {
            var group = robots[propt].group;
            var name_1 = robots[propt].name;
            if (group && !groups[group]) {
                groups[group] = coerceName(name_1, group);
            }
        }
        return groups;
    }
    exports.getRobotGroupsPrettyPrint = getRobotGroupsPrettyPrint;
});
