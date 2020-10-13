import { Port } from './port';
import { ROBOTS } from './const.robots';

class RobotViewField extends ((<any>window).Blockly.Field as { new(): any; }) {
    board_: any;
    sourceBlock_: any;
    visible_: any;
    element_: any;
    ports_: any[];
    size_: any;
    width_: number;
    height_: number;
    robot: any;
    static EDITABLE = false;

    static rectElement_ = null;

    constructor(robot) {
        super();
        this.robot = robot;
        this.height_ = Number(200);
        this.width_ = Number(300);
        this.size_ = new (<any>window).goog.math.Size(this.width_, this.height_ + 2 * (<any>window).Blockly.BlockSvg.INLINE_PADDING_Y);
        this.ports_ = [];
    }

    init() {
        if (this.element_) {
            return;
        }

        this.element_ = (<any>window).Blockly.createSvgElement('g', {}, null);
        if (!this.visible_) {
            this.element_.style.display = 'none';
        }

        this.initBoardView_();
        this.initPorts_();

        this.sourceBlock_.getSvgRoot().appendChild(this.element_);
    }

    initBoardView_() {
        const workspace = (<any>window).Blockly.getMainWorkspace();

        this.board_ = (<any>window).Blockly.createSvgElement('image', {}, this.element_);

        var robotSrc = workspace.options.pathToMedia + "robots/" + this.robot.group + "_" + this.robot.name + ".svg";
        var groupSrc = workspace.options.pathToMedia + "robots/" + this.robot.group + ".svg";

        const board = this.board_;

        urlExists(robotSrc, function() {
            board.setAttributeNS(
                'http://www.w3.org/1999/xlink',
                'xlink:href',
                robotSrc
            );
        }, function() {
            board.setAttributeNS(
                'http://www.w3.org/1999/xlink',
                'xlink:href',
                groupSrc
            );
        });
    }

    initPorts_() {
        const portsGroupSvg = (<any>window).Blockly.createSvgElement('g', {}, this.element_);
        let robots = ROBOTS[this.robot.group + '_' + this.robot.name] || ROBOTS[this.robot.group];
        this.ports_ = robots.map((props) => {
            const { name, position } = props;
            const port = new Port(portsGroupSvg, name, position);
            return { portSvg: port.element, ...props };
        });
    }

    getPortByName(portName) {
        const index = this.ports_["findIndex"](port => port.name === portName);
        return this.ports_[index];
    }

    setPosition(position) {
        if (!position) {
            return;
        }
    }
}

export function createRobotBlock(robotIdentifier) {
    return {
        init() {
            this.type_ = 'robot';
            this.svgPath_.remove();
            this.robot_ = new RobotViewField(identifierToRobot(robotIdentifier));
            this.appendDummyInput()
                .setAlign((<any>window).Blockly.ALIGN_CENTRE)
                .appendField(this.robot_, 'ROBOT');

            this.getPortByName = (portName) => {
                return this.robot_.getPortByName(portName);
            }
        },
    }
}

export function isRobotVisualized(robotIdentifier) {
    return ROBOTS[robotIdentifier] !== undefined;
}

function identifierToRobot(robotIdentifier) {
    const splits = robotIdentifier.split('_');
    var robot = {};
    robot["group"] = splits[0];
    robot["name"] = splits[1];
    return robot;
}

function urlExists(url, posCallback, negCallback) {
    var client = new XMLHttpRequest();
    client.onload = function() {
        if ((this as any).status === 200) {
            posCallback();
        } else {
            negCallback();
        }
    }
    client.open("HEAD", url, true);
    client.send();
}