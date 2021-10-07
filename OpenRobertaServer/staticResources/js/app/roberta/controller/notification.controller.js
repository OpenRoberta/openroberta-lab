/*
-------------------------------------------------------------------------

This a typescript source file stored in openroberta-lab/TypeScriptSources
It gets compiled to openroberta-lab/OpenRobertaServer/staticResources/js

DO NOT EDIT THIS IN openroberta-lab/OpenRobertaServer/staticResources/js !

-------------------------------------------------------------------------
*/
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "guiState.model", "guiState.controller", "notification.model", "comm", "jquery"], function (require, exports, guiStateModel, guiStateController, notificationModel, comm, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.showNotificationModal = exports.reloadNotifications = exports.init = void 0;
    var fadingDuration = 400;
    var notificationElement = $("#releaseInfo");
    var notificationElementTitle = notificationElement.children('#releaseInfoTitle');
    var notificationElementDescription = notificationElement.children('#releaseInfoContent');
    var $notificationForm = $("#notificationForm");
    var $notificationFileUpload = $('#notificationFileUpload');
    var $notificationFileDownload = $('#notificationFileDownload');
    var defaultElementMarkerTime = 5 * 60 * 1000;
    var defaultPopupTime = 20 * 1000;
    var defaultStartScreenTime = undefined;
    var activeNotifications = [];
    function loadAndInitNotifications() {
        notificationModel.getNotifications(function (result) {
            activeNotifications = initNotifications(result.notifications);
        });
    }
    function init() {
        initNotificationModal();
        loadAndInitNotifications();
        comm.onNotificationsAvailableCallback(reloadNotifications);
    }
    exports.init = init;
    function reloadNotifications() {
        removeNotifications();
        loadAndInitNotifications();
    }
    exports.reloadNotifications = reloadNotifications;
    /*----------- NOTIFICATION MODAL -----------*/
    function showNotificationModal() {
        notificationModel.getNotifications(function (result) {
            setFileDownloadContent(result.notifications);
            $('#modal-notifications').modal("show");
        });
    }
    exports.showNotificationModal = showNotificationModal;
    function showAlertInNotificationModal(context, content, time) {
        time = time || 6 * 1000;
        var $alert = $('#notification-modal-alert');
        $alert
            .html(content)
            .removeClass()
            .addClass("alert")
            .addClass("alert-" + context)
            .slideDown()
            .delay(time)
            .slideUp();
    }
    function initNotificationModal() {
        $notificationForm.onWrap('submit', function (e) {
            e.preventDefault();
            readFileInputField(function (fileContent) {
                notificationModel.postNotifications(fileContent, function (restResponse) {
                    if (restResponse.rc === "ok" && restResponse.message === "ORA_NOTIFICATION_SUCCESS") {
                        $notificationForm.trigger("reset");
                        showAlertInNotificationModal("success", "The notifications were transmitted successfully");
                        setFileDownloadContent(JSON.parse(fileContent));
                    }
                    else {
                        var errorCode = restResponse.cause;
                        var exceptionMessage = restResponse.parameters && restResponse.parameters.MESSAGE ? ":" + restResponse.parameters.MESSAGE : "";
                        var content = errorCode + exceptionMessage;
                        showAlertInNotificationModal("danger", content, 60 * 1000);
                    }
                });
            });
        });
    }
    function readFileInputField(readyFn) {
        var uploadedFiles = $notificationFileUpload.prop("files");
        if (uploadedFiles.length > 0) {
            readFile(uploadedFiles[0], readyFn);
        }
    }
    function readFile(file, readyFn) {
        var fileReader = new FileReader();
        fileReader.onload = function () { return readyFn(fileReader.result); };
        fileReader.readAsText(file);
    }
    function setFileDownloadContent(jsonContent) {
        var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(jsonContent, null, "\t"));
        $notificationFileDownload
            .attr("href", "data:" + data);
    }
    /*----------- NOTIFICATION HANDLING -----------*/
    function removeNotifications() {
        activeNotifications.forEach(function (notification) {
            notification.removeTriggers();
            notification.hideNotification();
        });
        activeNotifications = [];
    }
    function initNotifications(notificationSpecifications) {
        return notificationSpecifications.map(function (specification) { return new NotificationProcessor(specification); });
    }
    var EventHandler = /** @class */ (function () {
        function EventHandler(selector, event, fn) {
            this.selector = selector;
            this.event = event;
            this.fn = fn;
            this.$element = $(selector);
            this.elementIsPresent = this.$element.length;
        }
        EventHandler.prototype.addTriggers = function () {
            if (this.elementIsPresent) {
                // Use direct event handler if element is present
                this.$element.on(this.event, this.fn);
            }
            else {
                // Use delegate event handler if element is not yet present
                $(document).on(this.event, this.selector, this.fn);
            }
        };
        EventHandler.prototype.removeTriggers = function () {
            if (this.elementIsPresent) {
                this.$element.off(this.event, this.fn);
            }
            else {
                $(document).off(this.event, this.selector, this.fn);
            }
        };
        return EventHandler;
    }());
    var NotificationProcessor = /** @class */ (function () {
        function NotificationProcessor(specification) {
            this.activeEventHandler = [];
            this.notificationHandlers = [];
            this.specification = specification;
            this.setupNotificationHandlers();
            this.addTriggers();
        }
        NotificationProcessor.prototype.setupNotificationHandlers = function () {
            var _this = this;
            this.specification.handlers.forEach(function (handlerSpecification) {
                if (handlerSpecification.popupNotification) {
                    var popup = new PopupNotificationState(handlerSpecification.popupNotification);
                    _this.notificationHandlers.push(popup);
                }
                if (handlerSpecification.elementMarker) {
                    var elementMarker = new ElementMarkerState(handlerSpecification.elementMarker);
                    _this.notificationHandlers.push(elementMarker);
                }
                if (handlerSpecification.startScreen) {
                    var startScreen = new StartScreenNotificationState(handlerSpecification.startScreen);
                    _this.notificationHandlers.push(startScreen);
                }
            });
        };
        NotificationProcessor.prototype.showNotification = function () {
            if (this.specification.once) {
                this.removeTriggers();
            }
            this.notificationHandlers.forEach(function (notification) { return notification.show(); });
        };
        NotificationProcessor.prototype.hideNotification = function () {
            this.notificationHandlers.forEach(function (notification) { return notification.hide(); });
        };
        NotificationProcessor.prototype.evaluateConditionsAndShowNotification = function (specificCondition) {
            var _a = this.specification, generalConditions = _a.condition, ignoreDate = _a.ignoreDate;
            if (NotificationProcessor.evaluateCondition(specificCondition, ignoreDate) && NotificationProcessor.evaluateCondition(generalConditions, ignoreDate)) {
                this.showNotification();
            }
        };
        NotificationProcessor.evaluateCondition = function (conditions, ignoreDate) {
            if (conditions === undefined) {
                return true;
            }
            return conditions.every(function (condition) {
                if (condition.guiModel) {
                    var anyOf = condition.anyOf, equals = condition.equals, notEquals = condition.notEquals;
                    var element_1 = guiStateModel.gui[condition.guiModel];
                    if (anyOf && Array.isArray(anyOf)) {
                        return anyOf.some(function (each) { return element_1 === each; });
                    }
                    if (equals) {
                        return element_1 === equals;
                    }
                    if (notEquals) {
                        if (!Array.isArray(notEquals)) {
                            return element_1 !== notEquals;
                        }
                        return notEquals.every(function (each) { return element_1 !== each; });
                    }
                }
                var selector = parseSelector(condition);
                if (condition.hasClass && selector) {
                    return $(selector).hasClass(condition.hasClass);
                }
                if (!ignoreDate) {
                    if (condition.endTime) {
                        var endTimeDate = parseDateStringWithTimezone(condition.endTime);
                        var now = new Date();
                        return endTimeDate >= now;
                    }
                    if (condition.startTime) {
                        var startTimeDate = parseDateStringWithTimezone(condition.startTime);
                        var now = new Date();
                        return startTimeDate <= now;
                    }
                }
                return true;
            });
        };
        NotificationProcessor.prototype.addEventHandler = function (selector, event, fn) {
            var eventHandler = new EventHandler(selector, event, fn);
            eventHandler.addTriggers();
            this.activeEventHandler.push(eventHandler);
        };
        NotificationProcessor.prototype.addTriggers = function () {
            var _this = this;
            if (!this.specification.triggers || this.specification.triggers > 0) {
                // Directly run notification if conditions are met
                this.evaluateConditionsAndShowNotification();
                return;
            }
            this.specification
                .triggers
                .forEach(function (trigger) {
                var event = trigger.event, addClass = trigger.addClass, removeClass = trigger.removeClass, conditions = trigger.conditions;
                var selector = parseSelector(trigger);
                if (!selector)
                    return;
                // "Normal" event listeners
                if (event) {
                    _this.addEventHandler(selector, event, function () {
                        _this.evaluateConditionsAndShowNotification(conditions);
                    });
                }
                // Class changed event listeners
                if (addClass || removeClass) {
                    _this.addEventHandler(selector, "classChange", function () {
                        if (addClass && $(selector).hasClass(addClass)) {
                            _this.evaluateConditionsAndShowNotification(conditions);
                        }
                        if (removeClass && !$(selector).hasClass(removeClass)) {
                            _this.evaluateConditionsAndShowNotification(conditions);
                        }
                    });
                }
            });
        };
        NotificationProcessor.prototype.removeTriggers = function () {
            this.activeEventHandler.forEach(function (eventHandler) { return eventHandler.removeTriggers(); });
            this.activeEventHandler = [];
        };
        return NotificationProcessor;
    }());
    var NotificationState = /** @class */ (function () {
        function NotificationState(time) {
            this.active = false;
            this.time = time;
        }
        NotificationState.prototype.clearTimerIfExists = function () {
            if (this.timer) {
                clearTimeout(this.timer);
            }
        };
        NotificationState.prototype.setOrResetTimer = function () {
            var _this = this;
            if (this.time) {
                this.clearTimerIfExists();
                this.timer = setTimeout(function () { return _this.hide(); }, this.time);
            }
        };
        NotificationState.prototype.show = function () {
            this.setOrResetTimer();
            if (!this.active) {
                this.showAction();
                this.active = true;
            }
        };
        NotificationState.prototype.hide = function () {
            if (this.active) {
                this.clearTimerIfExists();
                this.hideAction();
                this.active = false;
            }
        };
        return NotificationState;
    }());
    var PopupNotificationState = /** @class */ (function (_super) {
        __extends(PopupNotificationState, _super);
        function PopupNotificationState(popupNotification) {
            var _this = _super.call(this, popupNotification.time || defaultPopupTime) || this;
            _this._title = parseLocalized(popupNotification.title);
            _this._content = parseLocalized(popupNotification.content);
            return _this;
        }
        PopupNotificationState.prototype.hideAction = function () {
            if (notificationElementTitle.html() === this._title && notificationElementDescription.html() === this._content) {
                notificationElement.fadeOut(fadingDuration);
            }
        };
        PopupNotificationState.prototype.showAction = function () {
            notificationElementTitle.html(this._title);
            notificationElementDescription.html(this._content);
            notificationElement.fadeIn(fadingDuration);
        };
        return PopupNotificationState;
    }(NotificationState));
    var ElementMarkerState = /** @class */ (function (_super) {
        __extends(ElementMarkerState, _super);
        function ElementMarkerState(elementMarker) {
            var _this = _super.call(this, elementMarker.time || defaultElementMarkerTime) || this;
            _this._content = parseLocalized(elementMarker.content);
            _this.$element = $(parseSelector(elementMarker));
            _this.$badge = $("<span class='badge badge-primary' style='display:none;'>" + _this._content + "</span>");
            return _this;
        }
        ElementMarkerState.prototype.hideAction = function () {
            if (this.$element.length) {
                this.$badge
                    .fadeOut(fadingDuration)
                    .queue(function () {
                    $(this).remove();
                });
            }
        };
        ElementMarkerState.prototype.showAction = function () {
            if (this.$element.length) {
                this.$badge
                    .appendTo(this.$element)
                    .fadeIn(fadingDuration);
            }
        };
        return ElementMarkerState;
    }(NotificationState));
    var StartScreenNotificationState = /** @class */ (function (_super) {
        __extends(StartScreenNotificationState, _super);
        function StartScreenNotificationState(startScreen) {
            var _this = _super.call(this, startScreen.time || defaultStartScreenTime) || this;
            _this.$startupMessage = $("#startup-message-statustext");
            _this.content = parseLocalized(startScreen.content);
            _this.$element = $('<h4 style="display: none">' + _this.content + '</h4>');
            return _this;
        }
        StartScreenNotificationState.prototype.showAction = function () {
            this.$element
                .appendTo(this.$startupMessage)
                .slideDown(fadingDuration);
        };
        StartScreenNotificationState.prototype.hideAction = function () {
            this.$element
                .slideUp(fadingDuration)
                .queue(function () {
                $(this).remove();
            });
        };
        return StartScreenNotificationState;
    }(NotificationState));
    function parseSelector(element) {
        if (element.htmlId) {
            return "#" + element.htmlId;
        }
        if (element.htmlSelector) {
            return element.htmlSelector;
        }
        return undefined;
    }
    function parseLocalized(object) {
        var localizedDescription = object[guiStateController.getLanguage()];
        return localizedDescription || object["en"];
    }
    /**
     * Parse date from a datestring
     * The parameter must match the format "YYYY-MM-DD HH:mm"
     * This automatically adds the German Timezone (+0200)
     * @param str datestring
     */
    function parseDateStringWithTimezone(str) {
        return new Date(str + " +0200");
    }
});
