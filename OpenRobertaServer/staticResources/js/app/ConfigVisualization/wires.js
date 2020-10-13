define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var DEFAULT_COLORS = { "5V": "#f01414", "GND": "#333333" };
    var DARK = 50;
    var Point = /** @class */ (function () {
        function Point(x, y) {
            this.x = x;
            this.y = y;
        }
        return Point;
    }());
    var WirePoint = /** @class */ (function () {
        function WirePoint(position) {
            this.pos = position;
            this.next = null;
        }
        Object.defineProperty(WirePoint.prototype, "position", {
            get: function () {
                return this.pos;
            },
            enumerable: false,
            configurable: true
        });
        return WirePoint;
    }());
    var WireDrawer = /** @class */ (function () {
        function WireDrawer(origin, destination) {
            this.head = new WirePoint(origin);
            this.head.next = new WirePoint(destination);
            this.toOrthoLines_();
        }
        WireDrawer.prototype.addPoint_ = function (prevPoint, position) {
            var newPoint = new WirePoint(position);
            newPoint.next = prevPoint.next;
            prevPoint.next = newPoint;
        };
        WireDrawer.prototype.toOrthoLines_ = function () {
            var _a = this.head.pos, x1 = _a.x, y1 = _a.y;
            var _b = this.head.next.pos, x2 = _b.x, y2 = _b.y;
            if (x1 === x2 || y1 === y2)
                return;
            var x = x1 < x2 ? Math.max(x1, x2) : Math.min(x1, x2);
            var y = y1 < y2 ? Math.min(y1, y2) : Math.max(y1, y2);
            this.addPoint_(this.head, { x: x, y: y });
        };
        Object.defineProperty(WireDrawer.prototype, "path", {
            get: function () {
                var moveto = this.head.position;
                var path = "M " + moveto.x + " " + moveto.y;
                var current = this.head.next;
                while (current !== null) {
                    var lineto = current.position;
                    path = path + " L " + lineto.x + " " + lineto.y;
                    current = current.next;
                }
                return path;
            },
            enumerable: false,
            configurable: true
        });
        WireDrawer.darken = function (color) {
            var dark = -DARK;
            color = color.slice(1);
            var num = parseInt(color, 16);
            var r = (num >> 16) + dark;
            r = r < 0 ? 0 : r;
            var b = ((num >> 8) & 0x00FF) + dark;
            b = b < 0 ? 0 : b;
            var g = (num & 0x0000FF) + dark;
            g = g < 0 ? 0 : g;
            var darkColor = g | (b << 8) | (r << 16);
            return ("#" + darkColor.toString(16));
        };
        WireDrawer.getColor = function (block, name) {
            return DEFAULT_COLORS[name] ? DEFAULT_COLORS[name] : WireDrawer.darken(block.colour_);
        };
        return WireDrawer;
    }());
    exports.default = WireDrawer;
});
