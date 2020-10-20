import { Port } from './port';
import { ROBOTS } from './const.robots';

class RobotViewField extends ((<any>window).Blockly.Field as { new(): any; }) {
    board_: any;
    sourceBlock_: any;
    visible_: any;
    element_: any;
    ports_: any[];
    size: any;
    width: number;
    height: number;
    robot: any;
    static EDITABLE = false;

    static rectElement_ = null;

    constructor(robot) {
        super();
        this.robot = robot;
        this.robotImageSrc = ROBOTS[this.robot.group + '_' + this.robot.name] ? (this.robot.group + '_' + this.robot.name) : ROBOTS[this.robot.group] ? this.robot.group : null;
        if (this.robotImageSrc) {
            this.width = ROBOTS[this.robotImageSrc]["width"];
            this.height = ROBOTS[this.robotImageSrc]["height"];
            this.size = new (<any>window).goog.math.Size(this.width, this.height + 2 * (<any>window).Blockly.BlockSvg.INLINE_PADDING_Y);
            this.ports_ = [];
        } else {
            console.error('robot image invalid!');
        }
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
        let robotSrc = workspace.options.pathToMedia + "robots/" + this.robotImageSrc + ".svg";
        const board = this.board_;
        board.setAttribute('href', robotSrc);
        board.setAttribute('x', 0);
        board.setAttribute('y', 0);
        board.setAttribute('width', this.width);
        board.setAttribute('height', this.height);
    }

    initPorts_() {
        const portsGroupSvg = (<any>window).Blockly.createSvgElement('g', {}, this.element_);
        let robot = ROBOTS[this.robot.group + '_' + this.robot.name] || ROBOTS[this.robot.group];
        this.ports_ = robot["ports"].map((props) => {
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

function identifierToRobot(robotIdentifier) {
    const splits = robotIdentifier.split('_');
    var robot = {};
    robot["group"] = splits[0];
    robot["name"] = splits[1];
    return robot;
}