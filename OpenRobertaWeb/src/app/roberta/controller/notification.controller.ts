/*
-------------------------------------------------------------------------

This a typescript source file stored in openroberta-lab/TypeScriptSources 
It gets compiled to openroberta-lab/OpenRobertaServer/staticResources/js

DO NOT EDIT THIS IN openroberta-lab/OpenRobertaServer/staticResources/js !

-------------------------------------------------------------------------
*/

import * as guiStateModel from "guiState.model";
import * as guiStateController from "guiState.controller";
import * as notificationModel from "notification.model";
import * as comm from "comm";
import * as $ from "jquery";

const fadingDuration = 400;

const notificationElement = $("#releaseInfo");

const notificationElementTitle = notificationElement.children('#releaseInfoTitle');
const notificationElementDescription = notificationElement.children('#releaseInfoContent');
const $notificationForm = $("#notificationForm");

const $notificationFileUpload = $('#notificationFileUpload');
const $notificationFileDownload = $('#notificationFileDownload');
const defaultElementMarkerTime = 5 * 60 * 1000;

const defaultPopupTime = 20 * 1000;
const defaultStartScreenTime = undefined;

let activeNotifications: NotificationProcessor[] = []

function loadAndInitNotifications() {
    notificationModel.getNotifications(result => {
        activeNotifications = initNotifications(result.notifications);
    });
}

export function init() {
    initNotificationModal();
    loadAndInitNotifications();

    comm.onNotificationsAvailableCallback(reloadNotifications);
}

export function reloadNotifications() {
    removeNotifications();
    loadAndInitNotifications()
}


/*----------- NOTIFICATION MODAL -----------*/

export function showNotificationModal() {
    notificationModel.getNotifications(function(result) {
        setFileDownloadContent(result.notifications);
        $('#modal-notifications').modal("show");
    });
}

function showAlertInNotificationModal(context, content, time?) {
    time = time || 6 * 1000;
    const $alert = $('#notification-modal-alert');
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
    $notificationForm.onWrap('submit', e => {
        e.preventDefault();
        readFileInputField(fileContent => {
            notificationModel.postNotifications(fileContent, function(restResponse) {
                if (restResponse.rc === "ok" && restResponse.message === "ORA_NOTIFICATION_SUCCESS") {
                    $notificationForm.trigger("reset");
                    showAlertInNotificationModal("success", "The notifications were transmitted successfully");
                    setFileDownloadContent(JSON.parse(fileContent));
                } else {
                    const errorCode = restResponse.cause;
                    const exceptionMessage = restResponse.parameters && restResponse.parameters.MESSAGE ? ":" + restResponse.parameters.MESSAGE : ""
                    const content = errorCode + exceptionMessage

                    showAlertInNotificationModal("danger", content, 60 * 1000);
                }
            });
        });
    });
}

function readFileInputField(readyFn) {
    let uploadedFiles = $notificationFileUpload.prop("files");
    if (uploadedFiles.length > 0) {
        readFile(uploadedFiles[0], readyFn)
    }
}


function readFile(file, readyFn) {
    const fileReader = new FileReader();
    fileReader.onload = () => readyFn(fileReader.result)
    fileReader.readAsText(file);
}

function setFileDownloadContent(jsonContent) {
    const data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(jsonContent, null, "\t"));
    $notificationFileDownload
        .attr("href", "data:" + data)
}


/*----------- NOTIFICATION HANDLING -----------*/

function removeNotifications() {
    activeNotifications.forEach(notification => {
        notification.removeTriggers();
        notification.hideNotification();
    })
    activeNotifications = [];
}

function initNotifications(notificationSpecifications): NotificationProcessor[] {
    return notificationSpecifications.map(specification => new NotificationProcessor(specification));
}

class EventHandler {
    private $element;
    private readonly selector;
    private readonly elementIsPresent: boolean;
    private readonly fn;
    private readonly event: string;

    public addTriggers() {
        if (this.elementIsPresent) {
            // Use direct event handler if element is present
            this.$element.on(this.event, this.fn);
        } else {
            // Use delegate event handler if element is not yet present
            $(document).on(this.event, this.selector, this.fn);
        }
    }

    public removeTriggers() {
        if (this.elementIsPresent) {
            this.$element.off(this.event, this.fn);
        } else {
            $(document).off(this.event, this.selector, this.fn);
        }
    }

    constructor(selector, event, fn) {
        this.selector = selector;
        this.event = event;
        this.fn = fn;
        this.$element = $(selector);
        this.elementIsPresent = this.$element.length;
    }
}


class NotificationProcessor {
    private specification: any;
    private activeEventHandler: EventHandler[] = [];
    private notificationHandlers: NotificationState[] = [];

    private setupNotificationHandlers() {
        this.specification.handlers.forEach(handlerSpecification => {
            if (handlerSpecification.popupNotification) {
                const popup = new PopupNotificationState(handlerSpecification.popupNotification);
                this.notificationHandlers.push(popup);
            }
            if (handlerSpecification.elementMarker) {
                const elementMarker = new ElementMarkerState(handlerSpecification.elementMarker);
                this.notificationHandlers.push(elementMarker);
            }
            if (handlerSpecification.startScreen) {
                const startScreen = new StartScreenNotificationState(handlerSpecification.startScreen);
                this.notificationHandlers.push(startScreen);
            }
        })

    }

    public showNotification() {
        if (this.specification.once) {
            this.removeTriggers();
        }

        this.notificationHandlers.forEach(notification => notification.show());
    }

    public hideNotification() {
        this.notificationHandlers.forEach(notification => notification.hide());
    }

    private evaluateConditionsAndShowNotification(specificCondition?: any) {
        const { condition: generalConditions, ignoreDate } = this.specification;

        if (NotificationProcessor.evaluateCondition(specificCondition, ignoreDate) && NotificationProcessor.evaluateCondition(generalConditions, ignoreDate)) {
            this.showNotification();
        }
    }

    private static evaluateCondition(conditions: any, ignoreDate?: boolean): boolean {
        if (conditions === undefined) {
            return true;
        }

        return conditions.every(condition => {
            if (condition.guiModel) {
                const { anyOf, equals, notEquals } = condition;
                const element = guiStateModel.gui[condition.guiModel];

                if (anyOf && Array.isArray(anyOf)) {
                    return anyOf.some(each => element === each)
                }
                if (equals) {
                    return element === equals;
                }
                if (notEquals) {
                    if (!Array.isArray(notEquals)) {
                        return element !== notEquals;
                    }

                    return notEquals.every(each => element !== each)
                }
            }

            const selector = parseSelector(condition);
            if (condition.hasClass && selector) {
                return $(selector).hasClass(condition.hasClass);
            }

            if (!ignoreDate) {
                if (condition.endTime) {
                    let endTimeDate = parseDateStringWithTimezone(condition.endTime);
                    let now = new Date();
                    return endTimeDate >= now;
                }
                if (condition.startTime) {
                    let startTimeDate = parseDateStringWithTimezone(condition.startTime);
                    let now = new Date();
                    return startTimeDate <= now;
                }
            }

            return true
        });

    }

    private addEventHandler(selector: string, event, fn: () => void) {
        const eventHandler = new EventHandler(selector, event, fn);
        eventHandler.addTriggers();
        this.activeEventHandler.push(eventHandler);
    }

    public addTriggers() {
        if (!this.specification.triggers || this.specification.triggers > 0) {
            // Directly run notification if conditions are met
            this.evaluateConditionsAndShowNotification();
            return;
        }

        this.specification
            .triggers
            .forEach(trigger => {
                const { event, addClass, removeClass, conditions } = trigger;
                const selector = parseSelector(trigger);

                if (!selector) return;

                // "Normal" event listeners
                if (event) {
                    this.addEventHandler(selector, event, () => {
                        this.evaluateConditionsAndShowNotification(conditions);
                    });
                }

                // Class changed event listeners
                if (addClass || removeClass) {
                    this.addEventHandler(selector, "classChange", () => {
                        if (addClass && $(selector).hasClass(addClass)) {
                            this.evaluateConditionsAndShowNotification(conditions);
                        }
                        if (removeClass && !$(selector).hasClass(removeClass)) {
                            this.evaluateConditionsAndShowNotification(conditions);
                        }
                    })
                }
            })
    }

    public removeTriggers() {
        this.activeEventHandler.forEach(eventHandler => eventHandler.removeTriggers());
        this.activeEventHandler = [];
    }

    constructor(specification: any) {
        this.specification = specification;
        this.setupNotificationHandlers();
        this.addTriggers();
    }
}

interface Selector {
    htmlId?: string;
    htmlSelector?: string
}

interface ElementMarkerSpec extends Selector {
    time?: number;
    content: string;
}

interface StartScreenSpec {
    time?: number;
    content: string;
}

interface PopupNotificationSpec {
    time?: number;
    title: string;
    content: string;
}

abstract class NotificationState {
    private active = false;
    private timer;
    private readonly time: number;

    private clearTimerIfExists() {
        if (this.timer) {
            clearTimeout(this.timer);
        }
    }

    private setOrResetTimer() {
        if (this.time) {
            this.clearTimerIfExists();
            this.timer = setTimeout(() => this.hide(), this.time)
        }
    }

    public show() {
        this.setOrResetTimer();
        if (!this.active) {
            this.showAction();
            this.active = true;
        }
    }

    public hide() {
        if (this.active) {
            this.clearTimerIfExists();
            this.hideAction();
            this.active = false;
        }
    }

    protected abstract showAction();

    protected abstract hideAction();

    protected constructor(time: number) {
        this.time = time;
    }
}

class PopupNotificationState extends NotificationState {
    private readonly _title: any;
    private readonly _content: any;

    protected hideAction() {
        if (notificationElementTitle.html() === this._title && notificationElementDescription.html() === this._content) {
            notificationElement.fadeOut(fadingDuration);
        }
    }

    protected showAction() {
        notificationElementTitle.html(this._title)
        notificationElementDescription.html(this._content)
        notificationElement.fadeIn(fadingDuration)
    }

    public constructor(popupNotification: PopupNotificationSpec) {
        super(popupNotification.time || defaultPopupTime);
        this._title = parseLocalized(popupNotification.title);
        this._content = parseLocalized(popupNotification.content);
    }
}


class ElementMarkerState extends NotificationState {
    private readonly _content: any;
    private $badge: any;
    private $element: any;

    constructor(elementMarker: ElementMarkerSpec) {
        super(elementMarker.time || defaultElementMarkerTime);
        this._content = parseLocalized(elementMarker.content);
        this.$element = $(parseSelector(elementMarker));
        this.$badge = $("<span class='badge badge-primary' style='display:none;'>" + this._content + "</span>");
    }

    protected hideAction() {
        if (this.$element.length) {
            this.$badge
                .fadeOut(fadingDuration)
                .queue(function() {
                    $(this).remove();
                })
        }
    }

    protected showAction() {
        if (this.$element.length) {
            this.$badge
                .appendTo(this.$element)
                .fadeIn(fadingDuration);
        }
    }
}

class StartScreenNotificationState extends NotificationState {
    private readonly content: string;
    private $element;
    private $startupMessage = $("#startup-message-statustext");

    constructor(startScreen: StartScreenSpec) {
        super(startScreen.time || defaultStartScreenTime);
        this.content = parseLocalized(startScreen.content);
        this.$element = $('<h4 style="display: none">' + this.content + '</h4>');
    }

    protected showAction() {
        this.$element
            .appendTo(this.$startupMessage)
            .slideDown(fadingDuration);
    }

    protected hideAction() {
        this.$element
            .slideUp(fadingDuration)
            .queue(function() {
                $(this).remove();
            })
    }
}

function parseSelector(element: Selector): string {
    if (element.htmlId) {
        return "#" + element.htmlId;
    }

    if (element.htmlSelector) {
        return element.htmlSelector;
    }

    return undefined;
}

function parseLocalized(object: any): string {
    let localizedDescription = object[guiStateController.getLanguage()];
    return localizedDescription || object["en"];
}

/**
 * Parse date from a datestring
 * The parameter must match the format "YYYY-MM-DD HH:mm"
 * This automatically adds the German Timezone (+0200)
 * @param str datestring
 */
function parseDateStringWithTimezone(str) {
    return new Date(str + " +0200")
}
