define(["require","exports","comm"],(function(i,o,t){Object.defineProperty(o,"__esModule",{value:!0}),o.postNotifications=o.getNotifications=void 0;o.getNotifications=function(i){t.json("/notifications/getNotifications",{},(function(o){"ok"===o.rc&&"ORA_SERVER_SUCCESS"===o.message&&i(o)}),"load notifications")};o.postNotifications=function(i,o){t.json("/notifications/postNotifications",{notifications:i},o,"send notifications to server")}}));
//# sourceMappingURL=notification.model.js.map
//# sourceMappingURL=notification.model.js.map
