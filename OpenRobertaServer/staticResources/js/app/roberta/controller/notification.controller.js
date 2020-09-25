define(
	["require", "exports", "guiState.model", "guiState.controller", "comm"],
	function (require, exports) {
		const guiStateModel = require("guiState.model");
		const guiStateController = require("guiState.controller");
		const comm = require("comm");

		let notifications = []
		let activeNotifications = []

		const fadingDuration = 400;

		const notificationElement = $("#releaseInfo");
		const notificationElementTitle = notificationElement.children('#releaseInfoTitle');
		const notificationElementDescription = notificationElement.children('#releaseInfoContent');

		exports.init = function () {
			fetchNotifications()
				.then(function () {
					initEvents();
				});

			comm.onNotificationsAvailableCallback(function () {
				console.log("New notifications are available")
				removeActiveEventListeners();
				console.log("Removed active events", activeNotifications);
				fetchNotifications()
					.then(function () {
						console.log("Fetched new ones");
						initEvents();
					})
			});
		};

		function removeActiveEventListeners() {
			for (const notification of activeNotifications) {
				notification.removeActiveEvents();
			}
			activeNotifications = [];
		}

		function fetchNotifications() {
			return comm.json("/notifications/getNotifications", {}, function (result) {
				console.log("fetched notifications", result)
				if (result.rc === "ok") {
					notifications = result.notifications;
				}
			}, "Fetch notifications from server");
		}

		function registerEventListener(selector, event, eventHandler, notificationState) {
			let $element = $(selector);
			let elementIsPresent = $element.length;

			/**
			 * Rewriting the .live() method in terms of its successors is straightforward; these are templates for equivalent calls for all three event attachment methods:
			 * $( selector ).live( events, data, handler );                // jQuery 1.3+
			 * $( document ).on( events, selector, data, handler );        // jQuery 1.7+
			 * https://api.jquery.com/live/
			 */

			if (elementIsPresent) {
				// Use direct event handler if element is present
				$element.on(event, eventHandler);
				notificationState.activeEvents.push({
					remove: function () {
						$element.off(event, eventHandler);
					}
				})
			} else {
				// Use delegate event handler if element is not yet present
				$(document).on(event, selector, eventHandler);
				notificationState.activeEvents.push({
					remove: function () {
						$element.off(event, selector, eventHandler)
					}
				});
			}
		}

		function initEvents() {
			for (const notification of notifications) {
				const notificationState = {
					id: notification.id,
					activeEvents: [],
					removeActiveEvents: removeActiveEvents,
				}

				function removeActiveEvents() {
					for (const activeEvent of notificationState.activeEvents) {
						activeEvent.remove();
					}
					notificationState.activeEvents = [];
				}

				function showNotificationsIfConditions(e, specificConditions) {
					const genericConditions = notification.conditions;

					let specificConditionsFulfilled = !specificConditions || specificConditions.every(cond => evaluateCondition(cond));
					let genericConditionsFulfilled = !genericConditions || notification.conditions.every(cond => evaluateCondition(cond));

					if (genericConditionsFulfilled && specificConditionsFulfilled) {
						showNotifications(notification.handlers)
						if (notification.once) removeActiveEvents();
					}
				}

				for (const trigger of notification.triggers) {
					const event = trigger.event;
					const addClass = trigger.addClass;
					const removeClass = trigger.removeClass;

					let selector = parseSelector(trigger);

					if (!selector) {
						console.error("The trigger doesn't have a selector set", trigger);
						continue;
					}

					// "Normal" event listeners
					if (event) {
						registerEventListener(selector, event, function (e) {
							showNotificationsIfConditions(e, trigger.conditions);
						}, notificationState);
					}

					// Class changed event listeners
					if (addClass || removeClass) {
						registerEventListener(selector, "classChange", function (e) {
							if (addClass && $(selector).hasClass(addClass)) {
								showNotificationsIfConditions(e, trigger.conditions);
							}
							if (removeClass && !$(selector).hasClass(removeClass)) {
								showNotificationsIfConditions(e, trigger.conditions);
							}
						}, notificationState)
					}
				}

				activeNotifications.push(notificationState);
			}
		}

		function evaluateCondition(condition) {
			if (condition.guiModel) {
				if (condition.equals) {
					return guiStateModel.gui[condition.guiModel] === condition.equals;
				}
				if (condition.notEquals) {
					return guiStateModel.gui[condition.guiModel] !== condition.notEquals;
				}
			}
			if (condition.domElement) {
				if (condition.hasClass) {
					return $(condition.domElement).hasClass(condition.hasClass);
				}
			}
			return true
		}

		function parseSelector(element) {
			return element.htmlId ? `#${(element.htmlId)}` : element.htmlSelector;
		}

		function parseLocalized(object) {
			let localizedDescription = object[guiStateController.getLanguage()];
			return localizedDescription || object;
		}

		function showNotifications(handlers) {
			for (const handler of handlers) {
				if (handler.popupNotification) {
					let popupNotification = handler.popupNotification;

					const title = parseLocalized(popupNotification.title);
					const description = parseLocalized(popupNotification.description);
					const time = popupNotification.time
					showNotificationForTime(title, description, time);
				}
				if (handler.elementMarker) {
					let elementMarker = handler.elementMarker;

					const $element = $(parseSelector(elementMarker));
					if ($element.length) {
						const content = parseLocalized(elementMarker.content)
						const time = elementMarker.time
						showBadgeOnElementForTime($element, content);
					}
				}
			}
		}

		function showBadgeOnElementForTime($element, content, time = 5 * 60 * 1000) {
			let $badge = $(`<span class='badge badge-primary' style="display:none;">${content}</span>`);
			$badge.appendTo($element)
				.fadeIn()
				.delay(time)
				.fadeOut()
				.queue(function () {
					$(this).remove();
				})
		}

		/**
		 *
		 * @param {String} title
		 * @param {String} description
		 * @param {Number} time
		 */
		function showNotificationForTime(title, description, time = 5 * 60 * 1000) {
			setContent(title, description);
			$(notificationElement)
				.fadeIn(fadingDuration)
				.delay(time)
				.fadeOut(fadingDuration);
		}

		/**
		 * @param {String} title
		 * @param {String} description
		 */
		function showNotification(title, description) {
			setContent(title, description);
			notificationElement.fadeIn(fadingDuration)
		}

		/**
		 * @param {String} title
		 * @param {String} description
		 */
		function setContent(title, description) {
			notificationElementTitle.html(title)
			notificationElementDescription.html(description)
		}

		function hideNotification() {
			notificationElement.fadeOut(fadingDuration)
		}

		exports.showNotification = showNotification;
		exports.hideNotification = hideNotification;
		exports.showNotificationForTime = showNotificationForTime;
	}
);