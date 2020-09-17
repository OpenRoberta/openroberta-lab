define([ 'exports', 'message', 'log', 'jquery', 'jquery-validate', 'bootstrap' ], function(exports, MSG, LOG, $) {

    const ANIMATION_DURATION = 750;

    var ratioWorkspace = 1;
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
        if (null == obj || "object" != typeof obj)
            return obj;

        // Handle Date
        if (obj instanceof Date) {
            copy = new Date();
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
            for ( var attr in obj) {
                if (obj.hasOwnProperty(attr))
                    copy[attr] = clone(obj[attr]);
            }
            return copy;
        }

        throw new Error("Unable to copy obj! Its type isn't supported.");
    }
    exports.clone = clone;

    function isEmpty(obj) {
        return Object.keys(obj).length === 0 && obj.constructor === Object;
    }

    exports.isEmpty = isEmpty;

    function getPropertyFromObject(obj, prop, arrayIndex) {
        //property not found
        if (typeof obj === 'undefined')
            return false;

        //index of next property split
        var _index = prop.indexOf('.')

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

    exports.getPropertyFromObject = getPropertyFromObject

    function setObjectProperty(obj, prop, value, arrayIndex) {
        //property not found
        if (typeof obj === 'undefined')
            return false;

        //index of next property split
        var _index = prop.indexOf('.')

        //property split found; recursive call
        if (_index > -1) {
            //get object at property (before split), pass on remainder
            return setObjectProperty(obj[prop.substring(0, _index)], prop.substr(_index + 1), value, arrayIndex);
        }

        //no split; get property
        if (arrayIndex != undefined) {
            return obj[prop][arrayIndex] = value;
        }
        obj[prop] = value;
    }

    exports.setObjectProperty = setObjectProperty

    /**
     * Format date
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
        } else {
            return "";
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
     * Calculate height of data table
     */
    function calcDataTableHeight() {
        return Math.round($(window).height() - 100);
    }
    exports.calcDataTableHeight = calcDataTableHeight;

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
            $('#single-modal-form').off('submit');
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

    function showSingleListModal(customize, onSubmit, onHidden, validator) {
        $('#single-modal-list-form').onWrap('submit', function(e) {
            e.preventDefault();
            onSubmit();
        });
        $('#single-modal-list').onWrap('hidden.bs.modal', function() {
            $('#single-modal-list-form').unbind('submit');
            onHidden();
        });
        setFocusOnElement($("#singleModalListInput"));
        $("#single-modal-list").modal('show');
    }
    exports.showSingleListModal = showSingleListModal;

    /**
     * Helper to show the information on top of the share modal.
     * 
     */
    function showMsgOnTop(msg) {
        $('#show-message').find('button').removeAttr("data-dismiss");
        $('#show-message').find('button').one('click', function(e) {
            $('#show-message').modal("hide");
            $('#show-message').find('button').attr("data-dismiss", "modal");
        });
        MSG.displayInformation({
            rc : "not ok"
        }, "", msg);
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
     * Rounds a number to required decimal and clips value to the range [0, 255]
     * (Range of UltraSound sensor)
     * 
     * @param value
     *            {Number} - to be rounded
     * @param decimals
     *            {Number} - number of decimals after rounding
     * @return {Number} rounded and clipped number
     * 
     */
    function roundUltraSound(value, decimals) {
        var ultraReading = round(value, decimals);
        if (ultraReading > 255) {
            ultraReading = 255;
        }

        return ultraReading;
    }
    exports.roundUltraSound = roundUltraSound;

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

    /**
     * Returns the basename (i.e. "hello" in "C:/folder/hello.txt")
     * 
     * @param path
     *            {String} - path
     */
    function getBasename(path) {
        var base = new String(path).substring(path.lastIndexOf('/') + 1);
        if (base.lastIndexOf(".") != -1) {
            base = base.substring(0, base.lastIndexOf("."));
        }
        return base;
    }
    exports.getBasename = getBasename;

    function destroyClickedElement(event) {
        document.body.removeChild(event.target);
    }

    function download(fileName, content) {
        if ('Blob' in window && navigator.userAgent.toLowerCase().match(/iPad|iPhone|Android/i) == null) {
            var contentAsBlob = new Blob([ content ], {
                type : 'application/octet-stream'
            });
            if ('msSaveOrOpenBlob' in navigator) {
                navigator.msSaveOrOpenBlob(contentAsBlob, fileName);
            } else {
                var downloadLink = document.createElement('a');
                downloadLink.download = fileName;
                downloadLink.innerHTML = 'Download File';
                downloadLink.href = window.URL.createObjectURL(contentAsBlob);
                downloadLink.onclick = destroyClickedElement;
                downloadLink.style.display = 'none';
                document.body.appendChild(downloadLink);
                downloadLink.click();
            }
        } else {
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute('href', 'data:text/' + fileName.substring(fileName.indexOf('.') + 1) + ';charset=utf-8,' + encodeURIComponent(content));
            downloadLink.setAttribute('download', fileName);
            downloadLink.style.display = 'none';
            document.body.appendChild(downloadLink);
            downloadLink.onclick = destroyClickedElement;
            downloadLink.click();
        }
    }
    exports.download = download;

    function getHashFrom(string) {
        var hash = 0;
        for (var i = 0; i < string.length; i++) {
            hash = ((hash << 5) - hash) + string.charCodeAt(i++);
        }
        return (hash < 0) ? ((hash * -1) + 0xFFFFFFFF) : hash;
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
            } else {
                break;
            }
        }
        return counter - 1;
    }
    exports.countBlocks = countBlocks;

    function isLocalStorageAvailable() {
        try {
            localStorage.setItem("test", "test");
            localStorage.removeItem("test");
            return true;
        } catch (e) {
            return false;
        }
    }
    exports.isLocalStorageAvailable = isLocalStorageAvailable;

    function alertTab(tabIdentifier) {
        clearTabAlert(tabIdentifier);
        $('#' + tabIdentifier).width(); // trigger a reflow to sync animations
        $('#' + tabIdentifier).prepend('<span class="typcn typcn-warning-outline"></span>') // add alert typicon
        $('#' + tabIdentifier).addClass('blinking');
    }
    exports.alertTab = alertTab;

    function clearTabAlert(tabIdentifier) {
        $('#' + tabIdentifier).children().remove('.typcn') // remove alert typicon
        $('#' + tabIdentifier).removeClass('blinking');
    }
    exports.clearTabAlert = clearTabAlert;

    var __entityMap = {
        "&" : "&amp;",
        "<" : "&lt;",
        ">" : "&gt;",
        '"' : '&quot;',
        "'" : '&#39;',
        "/" : '&#x2F;'
    };

    String.prototype.escapeHTML = function() {
        return String(this).replace(/[&<>"'\/]/g, function(s) {
            return __entityMap[s];
        });
    }

    $.fn.draggable = function(opt) {

        opt = $.extend({
            handle : "",
            cursor : "move",
            draggableClass : "draggable",
            activeHandleClass : "active-handle"
        }, opt);

        var $selected = null;
        var $elements = (opt.handle === "") ? this : this.find(opt.handle);

        $elements.css('cursor', opt.cursor).on("mousedown touchstart", function(e) {
            var pageX = e.pageX || e.originalEvent.touches[0].pageX;
            var pageY = e.pageY || e.originalEvent.touches[0].pageY;
            if (opt.handle === "") {
                $selected = $(this);
                $selected.addClass(opt.draggableClass);
            } else {
                $selected = $(this).parent();
                $selected.addClass(opt.draggableClass).find(opt.handle).addClass(opt.activeHandleClass);
            }
            var drg_h = $selected.outerHeight(), drg_w = $selected.outerWidth(), pos_y = $selected.offset().top + drg_h - pageY, pos_x = $selected.offset().left
                    + drg_w - pageX;
            $(document).on("mousemove touchmove", function(e) {
                var pageX = e.pageX || e.originalEvent.touches[0].pageX;
                var pageY = e.pageY || e.originalEvent.touches[0].pageY;
                // special case movable slider between workspace and right divs
                if (opt.axis == 'x') {
                    var left = pageX + pos_x - drg_w;
                    var left = Math.min(left, $('#main-section').width() - 80);
                    var left = Math.max(left, 42);
                    $selected.offset({
                        top : 0,
                        left : left - 4
                    });
                    $('#blockly').width(left + 3);
                    $('.rightMenuButton').css({
                        'right' : $(window).width() - left
                    });
                    $('.fromRight').css({
                        'width' : $(window).width() - $('#blockly').width()
                    })
                    ratioWorkspace = $('#blockly').outerWidth() / $('#main-section').outerWidth();
                    $(window).resize();
                } else {
                    $selected.offset({
                        top : pageY + pos_y - drg_h,
                        left : pageX + pos_x - drg_w
                    });
                }
                $selected.css({
                    right : 'auto',
                });
            }).on("mouseup touchend", function() {
                $(this).off("mousemove touchmove"); // Unbind events from document
                if ($selected !== null) {
                    $selected.removeClass(opt.draggableClass);
                    $selected = null;
                }
            });
        }).on("mouseup touchend", function() {
            if ($selected) {
                if (opt.handle === "") {
                    $selected.removeClass(opt.draggableClass);
                } else {
                    $selected.removeClass(opt.draggableClass).find(opt.handle).removeClass(opt.activeHandleClass);
                }
            }
            $selected = null;
        });
        return this;
    };


    const originalAddClass = $.fn.addClass;
    $.fn.addClass = function() {
        let result = originalAddClass.apply(this, arguments);
        $(this).trigger("classChange");
        return result;
    }

    const originalRemoveClass = $.fn.removeClass;
    $.fn.removeClass = function() {
        let result = originalRemoveClass.apply(this, arguments);
        $(this).trigger("classChange");
        return result;
    }

    $.fn.closeRightView = function(opt_callBack) {
        Blockly.hideChaff();
        $('.fromRight.rightActive').addClass('shifting');
        $('.blocklyToolboxDiv').css('display', 'inherit');
        var that = this; //$('#blockly')
        $('.fromRight.rightActive').animate({
            width : 0
        }, {
            duration : ANIMATION_DURATION,
            start : function() {
                $(".modal").modal("hide");
                $('.rightMenuButton.rightActive').removeClass('rightActive');
            },
            step : function(now) {
                that.width($('#main-section').outerWidth() - now);
                $('.rightMenuButton').css('right', now);
                ratioWorkspace = $('#blockly').outerWidth() / $('#main-section').outerWidth();
                $(window).resize();
            },
            done : function() {
                that.width($('#main-section').outerWidth());
                $('.rightMenuButton').css('right', 0);
                $('.fromRight.rightActive.shifting').removeClass('shifting');
                ratioWorkspace = 1;
                $('.fromRight').width(0);
                that.removeClass('rightActive');
                $('.fromRight.rightActive').removeClass('rightActive');
                $('#sliderDiv').hide();
                $(window).resize();
                if (typeof opt_callBack == 'function') {
                    opt_callBack();
                }
            }
        });
    };

    $.fn.openRightView = function(viewName, initialViewWidth, opt_callBack) {
        Blockly.hideChaff();
        var width;
        var smallScreen;
        if ($(window).width() < 768) {
            smallScreen = true;
            width = this.width() - 52;
        } else {
            smallScreen = false;
            width = this.width() * initialViewWidth;
        }
        if ($('#blockly').hasClass('rightActive')) {
            $('.fromRight.rightActive').removeClass('rightActive');
            $('.rightMenuButton.rightActive').removeClass('rightActive');
            $('#' + viewName + 'Div, #' + viewName + 'Button').addClass('rightActive');
            $(window).resize();
            if (smallScreen) {
                $('.blocklyToolboxDiv').css('display', 'none');
            }
            if (typeof opt_callBack == 'function') {
                opt_callBack();
            }
            return;
        }
        this.addClass('rightActive');
        $('#' + viewName + 'Div, #' + viewName + 'Button').addClass('rightActive');
        var that = this;
        $('.fromRight.rightActive').animate({
            width : width
        }, {
            duration : ANIMATION_DURATION,
            start : function() {
                $('#' + viewName + 'Div').addClass('shifting');
            },
            step : function(now, tween) {
                that.width($('#main-section').outerWidth() - now);
                $('.rightMenuButton').css('right', now);
                ratioWorkspace = $('#blockly').outerWidth() / $('#main-section').outerWidth();
                $(window).resize();
            },
            done : function() {
                $('#sliderDiv').show();
                that.width($('#main-section').outerWidth() - $('.fromRight.rightActive').width());
                $('.rightMenuButton').css('right', $('.fromRight.rightActive').width());
                $('#' + viewName + 'Div').removeClass('shifting');
                ratioWorkspace = $('#blockly').outerWidth() / $('#main-section').outerWidth();
                $(window).resize();
                if (smallScreen) {
                    $('.blocklyToolboxDiv').css('display', 'none');
                }
                $('#sliderDiv').css({
                    'left' : that.width() - 7
                });
                if (typeof opt_callBack == 'function') {
                    opt_callBack();
                }
            }
        });
    };

    $(window).resize(function() {
        var parentWidth = $('#main-section').outerWidth();
        var height = Math.max($('#blockly').outerHeight(), $('#brickly').outerHeight());

        var rightWidth = (1 - ratioWorkspace) * parentWidth;
        var leftWidth = ratioWorkspace * parentWidth;

        if (!$('.fromRight.rightActive.shifting').length > 0) {
            if ($('.fromRight.rightActive').length > 0) {
                $('.fromRight.rightActive').width(rightWidth);
                $('.rightMenuButton').css('right', rightWidth);
                $('#sliderDiv').css('left', leftWidth - 7);
            }
            $('#blockly').width(leftWidth);
        } else {
            leftWidth = $('#blockly').outerWidth();
        }

        if ($('#blocklyDiv')) {
            $('#blocklyDiv').width(leftWidth - 4);
            $('#blocklyDiv').height(height);
        }
        if ($('#bricklyDiv')) {
            $('#bricklyDiv').width(parentWidth);
            $('#bricklyDiv').height(height);
        }
        // here comes a fix for a strange browser behavior while zoom is not 100%. It is just in case (e.g. chrome 125% works fine, 110% not). 
        // Seems that either the returned sizes from the browser sometimes include margins/borders and sometimes not or that the assigned sizes behave 
        // different (with and without margins/borders).
        var diff = $('#main-section').outerWidth() - $('#blocklyDiv').outerWidth() - rightWidth;
        if (diff != 0) {
            $('#blocklyDiv').width(leftWidth - 4 + diff);
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
        if (workspace && workspace instanceof Blockly.Workspace) {
            var allBlocks = workspace.getAllBlocks();
            for (var i = 0; i < allBlocks.length; i++) {
                var icons = allBlocks[i].getIcons();
                for (var k = 0; k < icons.length; k++) {
                    var block = icons[k].block_;
                    if (block.error) {
                        block.error.dispose();
                        block.render();
                    } else if (block.warning) {
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
        for ( var annoId in annotations) {
            var block = workspace.getBlockById(annoId);
            if (block) {
                var anno = annotations[annoId];
                for ( var annoType in anno) {
                    var annoMsg = Blockly.Msg[anno[annoType]] || anno[annoType] || "unknown error";
                    switch (annoType) {
                    case "ERROR":
                        block.setErrorText(annoMsg);
                        block.error.setVisible(true);
                        break;
                    case "WARNING":
                        block.setWarningText(annoMsg);
                        block.warning.setVisible(true);
                        break;
                    default:
                        console.warn("Unsupported annotation: " + annoType);
                    }
                }
            }
        }
    }
    exports.annotateBlocks = annotateBlocks;

});
