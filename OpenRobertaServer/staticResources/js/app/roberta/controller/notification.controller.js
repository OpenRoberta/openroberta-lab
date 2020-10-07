define(
	["require", "exports", "guiState.model", "guiState.controller", "notification.model", "comm"],
	function (require, exports) {
		const guiStateModel = require("guiState.model");
		const guiStateController = require("guiState.controller");
		const notificationModel = require("notification.model");
		const comm = require("comm");

		let activeNotifications = []

		const fadingDuration = 400;

		const notificationElement = $("#releaseInfo");
		const notificationElementTitle = notificationElement.children('#releaseInfoTitle');
		const notificationElementDescription = notificationElement.children('#releaseInfoContent');

		const $notificationForm = $("#notificationForm");
		const $notificationFileUpload = $('#notificationFileUpload');
		const $notificationFileDownload = $('#notificationFileDownload');

		exports.init = function () {
			initNotificationModal();

			notificationModel.getNotifications(function (result) {
				initNotification(result.notifications);
			});

			comm.onNotificationsAvailableCallback(function () {
				console.log("New notifications are available")
				removeActiveEventListeners();
				console.log("Removed active events", activeNotifications);

				notificationModel.getNotifications(function (result) {
					initNotification(result.notifications);
				});
			});
		};

		exports.showNotificationModal = function () {
			notificationModel.getNotifications(function (result) {
				setDownloadContent(result.notifications);
				$('#modal-notifications').modal("show");
			});
		};

		function showModalAlert(context, content) {
			const $alert = $('#notification-modal-alert');
			$alert
				.html(content)
				.removeClass()
				.addClass("alert")
				.addClass("alert-" + context)
				.slideDown()
				.delay(6000)
				.slideUp();
		}

		function initNotificationModal() {
			$notificationForm.on('submit', function (e) {
				e.preventDefault();
				let uploadedFiles = $notificationFileUpload.prop("files");

				if (uploadedFiles.length > 0) {
					const fileReader = new FileReader();
					fileReader.onload = function () {
						notificationModel.postNotifications(fileReader.result, function (result) {
							if (result.rc === "ok" && result.message === "ORA_NOTIFICATION_SUCCESS") {
								$notificationForm[0].reset();
								showModalAlert("success", "The notifications were transmitted successfully");
								setDownloadContent(JSON.parse(fileReader.result));
							} else {
								showModalAlert("danger", result.message);
							}
						});
					};
					fileReader.readAsText(uploadedFiles[0])
				}
			});
		}

		function setDownloadContent(jsonContent) {
			const data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(jsonContent, null, "\t"));
			$notificationFileDownload
				.attr("href", "data:" + data)
		}

		function removeActiveEventListeners() {
			for (const notification of activeNotifications) {
				notification.removeActiveEvents();
			}
			activeNotifications = [];
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

		function initNotification(notifications) {
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

				function showNotificationsIfConditions(specificConditions) {
					const genericConditions = notification.conditions;

					let specificConditionsFulfilled = !specificConditions || specificConditions.every(cond => evaluateCondition(cond));
					let genericConditionsFulfilled = !genericConditions || notification.conditions.every(cond => evaluateCondition(cond));

					if (genericConditionsFulfilled && specificConditionsFulfilled) {
						showNotifications(notification.handlers)
						if (notification.once) removeActiveEvents();
					}
				}

				if (notification.triggers && notification.triggers.length > 0) {
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
								showNotificationsIfConditions(trigger.conditions);
							}, notificationState);
						}

						// Class changed event listeners
						if (addClass || removeClass) {
							registerEventListener(selector, "classChange", function (e) {
								if (addClass && $(selector).hasClass(addClass)) {
									showNotificationsIfConditions(trigger.conditions);
								}
								if (removeClass && !$(selector).hasClass(removeClass)) {
									showNotificationsIfConditions(trigger.conditions);
								}
							}, notificationState)
						}
					}
				} else {
					// Run directly
					showNotificationsIfConditions();
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
			if (condition.endTime) {
				let endTime = new Date(condition.endTime);
				let now = new Date(Date.now());
				return endTime >= now;
			}
			if (condition.startTime) {
				let startTime = new Date(condition.startTime);
				let now = new Date(Date.now());
				return startTime <= now;
			}
			return true
		}

		function parseSelector(element) {
			return element.htmlId ? `#${(element.htmlId)}` : element.htmlSelector;
		}

		function parseLocalized(object) {
			let localizedDescription = object[guiStateController.getLanguage()];
			return localizedDescription || object["en"];
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
						const time = elementMarker.time || 5 * 60 * 1000;
						showBadgeOnElementForTime($element, content, time);
					}
				}
				if (handler.startScreen) {
					const startScreen = handler.startScreen;
					const content = parseLocalized(startScreen.content);
					showStartScreenNotification(content, startScreen.time);
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
					this.remove();
				});
		}

		function showStartScreenNotification(content, time) {
			const $startupMessage = $("#startup-message-statustext");
			const $element = $('<h4 style="display: none">' + content + '</h4>')

			$element
				.appendTo($startupMessage)
				.fadeIn();

			if (time) {
				$element
					.delay(time)
					.fadeOut()
					.queue(function () {
						$(this).remove();
					})
			}
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