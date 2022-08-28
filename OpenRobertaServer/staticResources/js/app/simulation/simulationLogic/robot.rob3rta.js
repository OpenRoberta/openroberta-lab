var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "robot.sensors", "robot.actuators", "robot.base.stationary", "blockly"], function (require, exports, robot_sensors_1, robot_actuators_1, robot_base_stationary_1, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotRob3rta = /** @class */ (function (_super) {
        __extends(RobotRob3rta, _super);
        function RobotRob3rta(id, configuration, interpreter, savedName, myListener) {
            var _this = _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
            _this.topView = '<div id="mbedContent"><form id="mbed-form"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons"></div></form></div>';
            _this.timer = new robot_sensors_1.Timer(1);
            _this.configure(configuration);
            return _this;
        }
        RobotRob3rta.prototype.configure = function (configuration) {
            $('#simRobotContent').append(this.topView);
            $.validator.addClassRules('range', { required: true, number: true });
            this.infraredSensor = new robot_sensors_1.Rob3rtaInfraredSensor();
            this.temperatureSensor = new robot_sensors_1.TemperatureSensor();
            this.rgbLedLeft = new robot_actuators_1.RGBLed({ x: 575, y: 227 }, 1);
            this.rgbLedRight = new robot_actuators_1.RGBLed({ x: 183, y: 273 }, 2);
            this.ledLeft = new robot_actuators_1.RGBLed({ x: 470, y: 680 }, 4);
            this.ledRight = new robot_actuators_1.RGBLed({ x: 370, y: 680 }, 3);
            var mySensorPins = [
                {
                    x: 280,
                    y: 750,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: 'A',
                    name: Blockly.Msg.PORT_WHEEL + ' A',
                    touchColors: ['#7f7f7fff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
                {
                    x: 430,
                    y: 750,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: 'B',
                    name: Blockly.Msg.PORT_WHEEL + ' B',
                    touchColors: ['#7f7f7eff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
                {
                    x: 590,
                    y: 770,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: 'C',
                    name: Blockly.Msg.PORT_WHEEL + ' C',
                    touchColors: ['#7f7f7dff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
                {
                    x: 750,
                    y: 730,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: 'D',
                    name: Blockly.Msg.PORT_WHEEL + ' D',
                    touchColors: ['#7f7f7cff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
                {
                    x: 120,
                    y: 170,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: '1',
                    name: Blockly.Msg.PORT_EAR + ' ' + Blockly.Msg.LEFT,
                    touchColors: ['#e12c2eff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
                {
                    x: 670,
                    y: 100,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: '2',
                    name: Blockly.Msg.PORT_EAR + ' ' + Blockly.Msg.RIGHT,
                    touchColors: ['#e12c2fff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
            ];
            this.pinSensors = new robot_sensors_1.Pins(mySensorPins, this.id, { x: -28, y: 30 }, null);
        };
        RobotRob3rta.prototype.handleMouseDown = function (e) {
            //
        };
        RobotRob3rta.prototype.handleMouseMove = function (e) {
            //
        };
        RobotRob3rta.prototype.handleMouseOutUp = function (e) {
            //
        };
        RobotRob3rta.prototype.handleKeyEvent = function (e) {
            throw new Error('Method not implemented.');
        };
        RobotRob3rta.prototype.handleNewSelection = function (who) {
            // nothing to do so far, because only one rob3rta at a time is simulated
        };
        return RobotRob3rta;
    }(robot_base_stationary_1.RobotBaseStationary));
    exports.default = RobotRob3rta;
});
