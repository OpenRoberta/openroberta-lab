define(["require", "exports"], function (require, exports) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.Port = void 0;
    var Port = /** @class */ (function () {
        function Port(parent, name, position, connectedTo) {
            this.position_ = position;
            this.element_ = window.Blockly.createSvgElement('rect', {
                'class': 'port',
                'width': 5,
                'height': 5,
                'fill': 'red',
                'stroke': 'black',
                'stroke-width': 1,
                'transform': "translate(" + position.x + ", " + position.y + ")",
                'r': 3,
            }, parent);
            this.connectedTo = connectedTo;
            if (name) {
                this.element_.tooltip = name;
                window.Blockly.Tooltip.bindMouseEvents(parent);
            }
        }
        Port.prototype.moveTo = function (position) {
            this.position_ = position;
            this.element_.setAttribute('transform', "translate(" + position.x + ", " + position.y + ")");
        };
        Object.defineProperty(Port.prototype, "element", {
            get: function () {
                return this.element_;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Port.prototype, "position", {
            get: function () {
                return this.position_;
            },
            enumerable: false,
            configurable: true
        });
        return Port;
    }());
    exports.Port = Port;
});
