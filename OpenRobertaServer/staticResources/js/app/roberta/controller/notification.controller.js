define(
	["require", "exports", "guiState.model", "guiState.controller"],
	function (require, exports) {
		const guiStateModel = require("guiState.model");
		const guiStateController = require("guiState.controller");

		const notifications = [
			{
				once: true,
				triggers: [
					{
						generic: {
							robot: "calliope2017NoBlue"
						}
					},
					{
						htmlId: "tabProgram",
						event: "click",
						conditions: [
							{
								guiModel: "robot", // guiStateModel.gui.robot
								equals: "calliope2017NoBlue",
							},
						]
					},
					{
						htmlId: "menu-calliope2017NoBlue",
						event: "click",
						conditions: [
							{
								guiModel: "view",
								equals: "tabProgram"
							}
						]
					},
					{
						htmlSelector: ".popup-robot[data-type='calliope2017NoBlue']:not(.slick-cloned)",
						event: "click"
					}
				],
				/*conditions: [
					Here it is also possible to define conditions
				],*/
				handlers: [
					{
						popupNotification: {
							title: {
								de: "Konfiguration",
								en: "Configuration",
							},
							description: {
								de: "Hey, Calliope benutzt nun eine Konfiguration",
								en: "Hey, Calliope now uses a configuration",
							}
						}
					},
					{
						elementMarker: {
							htmlId: "tabConfiguration",
							content: {
								de: "Neu",
								en: "new"
							}
						}
					}
				]
			},
			{
				once: true,
				triggers: [
					{
						htmlId: "simButton",
						addClass: "rightActive",
					},
				],
				conditions: [
					{
						guiModel: "robot",
						equals: "ev3lejosv1",
					},
				],
				handlers: [
					{
						popupNotification: {
							time: 5000, // ms
							title: {
								de: "Simulationsanpassung",
								en: "Simulation changes",
							},
							description: {
								de: "Hey, wir haben die Simulation angepasst.",
								en: "Hey, we changed the simulation ...",
							}
						},
					},
					{
						elementMarker: {
							htmlId: "debugMode",
							content: {
								de: "Was ganz blödes"
							}
						}
					}
				]
			}
		]

		const fadingDuration = 400;

		const notificationElement = $("#releaseInfo");
		const notificationElementTitle = notificationElement.children('#releaseInfoTitle');
		const notificationElementDescription = notificationElement.children('#releaseInfoContent');

		exports.init = function () {
			console.log("Init notifications, here load config from guiState / use from guiState")
			initEvents();
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

		function initEvents() {
			for (const notification of notifications) {
				let unregisterFunctions = []
				let genericConditions = notification.conditions;

				function unregisterAllEventListeners() {
					unregisterFunctions.forEach(f => f())
				}

				function registerEventListener(selector, event, eventHandler) {
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
						unregisterFunctions.push(() => $element.off(event, eventHandler));
					} else {
						// Use delegate event handler if element is not yet present
						$(document).on(event, selector, eventHandler);
						unregisterFunctions.push(() => $element.off(event, selector, eventHandler));
					}
				}

				function showNotificationsIfConditionsAreFulfilled(e, specificConditions) {
					let specificConditionsAreTrue = !specificConditions || specificConditions.every(cond => evaluateCondition(cond));
					let genericConditionsAreTrue = !genericConditions || notification.conditions.every(cond => evaluateCondition(cond));

					if (genericConditionsAreTrue && specificConditionsAreTrue) {
						showNotifications(notification.handlers)
						if (notification.once) unregisterAllEventListeners();
					}
				}

				for (const trigger of notification.triggers) {
					const event = trigger.event;
					const addClass = trigger.addClass;
					const removeClass = trigger.removeClass;

					function eventHandler(e) {
						showNotificationsIfConditionsAreFulfilled(e, trigger.conditions);
					}

					let selector = parseSelector(trigger);

					if (!selector) {
						console.error("The trigger doesn't have a selector set", trigger);
						continue;
					}

					// "Normal" event listeners
					if (event) {
						registerEventListener(selector, event, eventHandler);
					}

					// Class changed event listeners
					if(addClass || removeClass) {
						registerEventListener(selector, "classChange", function (e) {
							if (addClass && $(selector).hasClass(addClass)) {
								eventHandler(e);
							}
							if (removeClass && !$(selector).hasClass(removeClass)) {
								eventHandler(e);
							}
						})
					}



				}
			}
		}

		function showBadgeOnElementForTime($element, content, time = 5 * 60 * 1000) {
			let $badge = $(`<span class='badge badge-primary' style="display:none;">${content}</span>`);
			$badge.appendTo($element);
			$badge
				.fadeIn()
				.delay(time)
				.fadeOut()
				.remove();
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