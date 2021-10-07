define(["require", "exports"], function (require, exports) {
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
    function distance(first, second) {
        return Math.abs(first - second);
    }
    function chooseByDistance(selectBetween, comparisonPoint, comparator) {
        if (comparator === void 0) { comparator = function (x1, x2) { return x1 > x2; }; }
        var comparison = comparator(distance(selectBetween[0], comparisonPoint), distance(selectBetween[1], comparisonPoint));
        if (comparison) {
            return selectBetween[0];
        }
        return selectBetween[1];
    }
    var WireDrawer = /** @class */ (function () {
        function WireDrawer(origin, destination, portIndex, blockCorners) {
            this.blockCorners = blockCorners;
            if (blockCorners)
                this.left = this.blockCorners.upperLeft.x === origin.x;
            this.portIndex = portIndex;
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
            var _a = this.head.pos, originX = _a.x, originY = _a.y;
            var _b = this.head.next.pos, destinationX = _b.x, destinationY = _b.y;
            if (originX === destinationX || originY === destinationY)
                return;
            var x = originX < destinationX ? Math.max(originX, destinationX) : Math.min(originX, destinationX);
            var y = originY < destinationY ? Math.min(originY, destinationY) : Math.max(originY, destinationY);
            if (!this.blockCorners) {
                this.addPoint_(this.head, { x: x, y: y });
                return;
            }
            // Adjust path around block
            var _c = this.blockCorners, lowerRight = _c.lowerRight, upperLeft = _c.upperLeft;
            var separatorByPortIndex = (this.portIndex + 1) * WireDrawer.SEPARATOR;
            y = chooseByDistance([lowerRight.y + separatorByPortIndex, upperLeft.y - separatorByPortIndex], destinationY, function (x, y) { return x < y; });
            var xExtra = this.left ? originX - separatorByPortIndex : originX + separatorByPortIndex;
            this.addPoint_(this.head, { x: x, y: y });
            this.addPoint_(this.head, {
                x: xExtra,
                y: y
            });
            this.addPoint_(this.head, {
                x: xExtra,
                y: originY
            });
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
        WireDrawer.SEPARATOR = 6;
        return WireDrawer;
    }());
    exports.default = WireDrawer;
});
