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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
define(["require", "exports", "./port", "./const.robots"], function (require, exports, port_1, const_robots_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.createRobotBlock = void 0;
    var RobotViewField = /** @class */ (function (_super) {
        __extends(RobotViewField, _super);
        function RobotViewField(robot) {
            var _this = _super.call(this) || this;
            _this.robot = robot;
            _this.robotImageSrc = const_robots_1.ROBOTS[_this.robot.group + '_' + _this.robot.name] ? (_this.robot.group + '_' + _this.robot.name) : const_robots_1.ROBOTS[_this.robot.group] ? _this.robot.group : null;
            if (_this.robotImageSrc) {
                _this.width = const_robots_1.ROBOTS[_this.robotImageSrc]["width"];
                _this.height = const_robots_1.ROBOTS[_this.robotImageSrc]["height"];
                _this.size = new window.goog.math.Size(_this.width, _this.height + 2 * window.Blockly.BlockSvg.INLINE_PADDING_Y);
                _this.ports_ = [];
            }
            else {
                console.error('robot image invalid!');
            }
            return _this;
        }
        RobotViewField.prototype.init = function () {
            if (this.element_) {
                return;
            }
            this.element_ = window.Blockly.createSvgElement('g', {}, null);
            if (!this.visible_) {
                this.element_.style.display = 'none';
            }
            this.initBoardView_();
            this.initPorts_();
            this.sourceBlock_.getSvgRoot().appendChild(this.element_);
        };
        RobotViewField.prototype.initBoardView_ = function () {
            var workspace = window.Blockly.getMainWorkspace();
            this.board_ = window.Blockly.createSvgElement('image', {}, this.element_);
            var robotSrc = workspace.options.pathToMedia + "robots/" + this.robotImageSrc + ".svg";
            var board = this.board_;
            board.setAttribute('href', robotSrc);
            board.setAttribute('x', 0);
            board.setAttribute('y', 0);
            board.setAttribute('width', this.width);
            board.setAttribute('height', this.height);
        };
        RobotViewField.prototype.initPorts_ = function () {
            var portsGroupSvg = window.Blockly.createSvgElement('g', {}, this.element_);
            var robot = const_robots_1.ROBOTS[this.robot.group + '_' + this.robot.name] || const_robots_1.ROBOTS[this.robot.group];
            this.ports_ = robot["ports"].map(function (props) {
                var name = props.name, position = props.position;
                var port = new port_1.Port(portsGroupSvg, name, position);
                return __assign({ portSvg: port.element }, props);
            });
        };
        RobotViewField.prototype.getPortByName = function (portName) {
            var index = this.ports_["findIndex"](function (port) { return port.name === portName; });
            return this.ports_[index];
        };
        RobotViewField.prototype.setPosition = function (position) {
            if (!position) {
                return;
            }
        };
        RobotViewField.EDITABLE = false;
        RobotViewField.rectElement_ = null;
        return RobotViewField;
    }(window.Blockly.Field));
    function createRobotBlock(robotIdentifier) {
        return {
            init: function () {
                var _this = this;
                this.type_ = 'robConf_robot';
                this.svgPath_.remove();
                this.robot_ = new RobotViewField(identifierToRobot(robotIdentifier));
                this.appendDummyInput()
                    .setAlign(window.Blockly.ALIGN_CENTRE)
                    .appendField(this.robot_, 'ROBOT');
                this.getPortByName = function (portName) {
                    return _this.robot_.getPortByName(portName);
                };
            },
        };
    }
    exports.createRobotBlock = createRobotBlock;
    function identifierToRobot(robotIdentifier) {
        var splits = robotIdentifier.split('_');
        var robot = {};
        robot["group"] = splits[0];
        robot["name"] = splits[1];
        return robot;
    }
});
