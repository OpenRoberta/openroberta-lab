import WireDrawer from './wires';
import { ROBOTS } from "./const.robots";
import { createRobotBlock } from "./robotBlock";
import { Port } from "./port";

const SEP = 2.5;
const STROKE = 1.8;

// fix for IE which does not have the remove function
if (!('remove' in Element.prototype)) {
    (Element.prototype as any).remove = function() {
        if (this.parentNode) {
            this.parentNode.removeChild(this);
        }
    };
}

export class CircuitVisualization {
    components: {};
    connections: any;
    robot: any;
    robotXml: Node;
    wireGroup: any;
    currentRobot: string;
    dom: any;
    workspace: any;
    scale: number = 1;

    constructor(workspace: any, dom: string) {
        this.dom = dom;
        this.workspace = workspace;
        if (!((<any>window).Blockly)) {
            throw new Error('Blockly required');
        }
        this.components = {};
        this.connections = [];
        this.currentRobot = this.workspace.device + "_" + this.workspace.subDevice;
        this.injectRobotBoard();
        this.workspace.addChangeListener(this.onChangeListener);
        this.wireGroup = (<any>window).Blockly.createSvgElement('g', {}, this.workspace.svgGroup_);
        document.getElementById("bricklyDiv").addEventListener('mousemove', this.handler);
        document.getElementById("bricklyDiv").addEventListener('click', this.handler);
        document.getElementById("bricklyDiv").addEventListener('touchmove', this.handler);
    }

    handler = (function(event) {
        if ((<any>window).Blockly.dragMode_ == (<any>window).Blockly.DRAG_FREE || this.workspace.isScrolling) {
            this.renderConnections();
        }
        if (this.workspace.scale !== this.scale) {
            this.scale = this.workspace.scale;
            this.renderConnections();
        }
    }).bind(this);

    static domToWorkspace(dom, workspace) {
        const confVis = new CircuitVisualization(workspace, dom);
        return {
            dispose: confVis.dispose.bind(confVis),
            refresh: confVis.refresh.bind(confVis),
            resetRobot: confVis.reset.bind(confVis),
            getXml: confVis.getXml.bind(confVis),
        };
    }

    public static isRobotVisualized(robotGroup: string, robot: string): boolean {
        return ROBOTS[robotGroup + "_" + robot] || ROBOTS[robotGroup] !== undefined;
    }

    reset(): void {
        const currentRobot = this.workspace.device + "_" + this.workspace.subDevice;
        if (currentRobot !== this.currentRobot) {
            this.currentRobot = currentRobot;
            this.dom = this.getXml();
            this.clear();
            this.injectRobotBoard();
        }
    }

    refresh(): void {
        this.workspace.getAllBlocks().forEach(block => {
            this.updateBlockPorts(block);
            this.renderConnections();
        });
    }

    dispose(): void {
        this.workspace.removeChangeListener(this.onChangeListener);
        document.getElementById("bricklyDiv").removeEventListener('mousemove', this.handler);
        document.getElementById("bricklyDiv").removeEventListener('touchmove', this.handler);
        document.getElementById("bricklyDiv").removeEventListener('click', this.handler);
        this.wireGroup.remove();
    }

    getXml(): string {
        return (<any>window).Blockly.Xml.workspaceToDom(this.workspace);
    }

    injectRobotBoard(): void {
        (<any>window).Blockly.Blocks['robConf_robot'] = createRobotBlock(this.currentRobot);

        if (!this.dom.querySelector("block[type=robConf_robot]")) {
            const robotXml = `<instance x="250" y="250"><block type="robConf_robot" id="robot"></block></instance>`;
            const oParser = new DOMParser();
            const robotElement = oParser.parseFromString(robotXml, 'text/xml').firstChild;
            this.dom.appendChild(robotElement);
        }

        (<any>window).Blockly.Xml.domToWorkspace(this.dom, this.workspace);
        this.robot = this.workspace.getBlockById('robot');
    }

    clear = () => {
        while (this.workspace.getAllBlocks().length) {
            this.workspace.getAllBlocks()[0].dispose();
        }
    }

    onChangeListener = (event) => {
        this.renderConnections();
        if (!event.blockId) {
            return;
        }
        const block = this.workspace.getBlockById(event.blockId);

        switch (event.type) {
            case (<any>window).Blockly.Events.CREATE:
                this.createBlockPorts(block);
                break;
            case (<any>window).Blockly.Events.CHANGE:
                this.updateBlockPorts(block);
                this.updateConnections(block);
                break;
            case (<any>window).Blockly.Events.DELETE:
                this.deleteConnections(event.blockId);
                if (block && block.ports) {
                    block.ports.forEach(port => port.element.remove());
                }
                break;
        }
    }

    renderConnections(): void {
        if (this.connections.length === 0) {
            return;
        }
        const robotPosition = this.robot.getRelativeToSurfaceXY();
        this.connections.forEach(({ blockId, position, connectedTo, wireSvg }) => {
            const block = this.workspace.getBlockById(blockId);
            if (!block) {
                return;
            }
            if (this.needToUpdateBlockPorts(block, position, connectedTo)) {
                this.updateBlockPorts(block);
            }
            const blockPosition = block.getRelativeToSurfaceXY();
            const origin = this.calculateAbsolutePosition({
                x: blockPosition.x + position.x + SEP,
                y: blockPosition.y + position.y + SEP,
            });
            const robotConnection = this.robot.getPortByName(connectedTo)
            if (!robotConnection) {
                return;
            }
            const destination = this.calculateAbsolutePosition({
                x: robotPosition.x + robotConnection.position.x + SEP,
                y: robotPosition.y + robotConnection.position.y + SEP
            });
            const drawer = new WireDrawer(origin, destination, this.calculateAbsoluteBlockCorners(block));
            wireSvg.setAttribute('d', drawer.path);
            wireSvg.setAttribute('stroke-width', STROKE * this.workspace.scale);
        });
    }

    private calculateAbsoluteBlockCorners(block) {
        const relativeUpperLeft = block.getRelativeToSurfaceXY();
        return {
            upperLeft: this.calculateAbsolutePosition(relativeUpperLeft),
            lowerRight: this.calculateAbsolutePosition({
                x: relativeUpperLeft.x + block.width,
                y: relativeUpperLeft.y + block.height
            })
        };
    }

    private calculateAbsolutePosition(pos: { x, y }) {
        const { matrix } = this.workspace.getCanvas().transform.baseVal.getItem(0);
        return {
            x: matrix.e + this.workspace.scale * pos.x,
            y: matrix.f + this.workspace.scale * pos.y
        };
    }

    private needToUpdateBlockPorts(block: any, portPosition: any, connectedTo: any): boolean {
        if (connectedTo) {
            return portPosition.x !== this.calculatePortPosition(block, connectedTo);
        }
    }
    updateBlockPorts = (block) => {
        block.ports.forEach(port => {
            const position = port.position;
            port.moveTo({ ...position, x: this.calculatePortPosition(block, port.connectedTo) });
        });

        this.connections = this.connections.map(({ position, connectedTo, ...others }) => {
            if (others.blockId !== block.id) {
                return { position, connectedTo, ...others };
            }
            return {
                position: { ...position, x: this.calculatePortPosition(block, connectedTo) },
                connectedTo,
                ...others,
            };
        });
    }

    private calculatePortPosition(block, connectedTo) {
        const blockPosition = (block.getRelativeToSurfaceXY().x) + (block.width / 2);
        const robotPortPosition = (this.robot.getRelativeToSurfaceXY().x) + (this.robot.getPortByName(connectedTo).position.x);

        if (blockPosition < robotPortPosition)
            return block.width - SEP;
        else
            return -SEP;
    }

    createBlockPorts = (block) => {
        block.ports = [];
        block.inputList.forEach((input, index) => {
            if (index === 0) {
                if (this.robot.getPortByName(block.confBlock)) {
                    this.appendPortAndConnection(block, input.fieldRow[0].textElement_, name, block.confBlock);
                }
            } else {
                input.fieldRow.forEach(({ fieldGroup_, name, value_ }) => {
                    name = name || value_;
                    if (name) {
                        const connectedTo = this.robot.getPortByName(block.confBlock + " " + value_) ? block.confBlock + " " + value_ : this.robot.getPortByName(block.getFieldValue(name)) ? block.getFieldValue(name) : this.robot.getPortByName(name) ? name : null;
                        if (connectedTo) {
                            this.appendPortAndConnection(block, fieldGroup_, name, connectedTo);
                        }
                    }
                });
            }
        });
    }

    appendPortAndConnection = (block, svgElement, name, connectedTo) => {
        const { matrix } = svgElement.transform.baseVal.getItem(0);
        const position = {
            x: this.calculatePortPosition(block, connectedTo),
            y: matrix.f + 6
        };
        const port = new Port(block.getSvgRoot(), name, position, connectedTo);
        block.ports.push(port);
        const wireColor = WireDrawer.getColor(block, name);
        const wireSvg = (<any>window).Blockly.createSvgElement('path', {
            'fill': 'none',
            'stroke': wireColor,
            'stroke-width': STROKE,
            'stroke-linecap': 'round',
            'stroke-linejoin': 'round',
        }, this.wireGroup);

        this.connections.push({
            blockId: block.id,
            connectedTo: connectedTo,
            name,
            position,
            wireSvg,
        });
    }

    updateConnections = (block) => {
        let connections = this.connections.filter(connection => connection.blockId === block.id);
        connections = connections.map(({ name, ...others }) => ({
            name,
            ...others,
            connectedTo: this.robot.getPortByName(block.confBlock + " " + block.getFieldValue(name)) ? block.confBlock + " " + block.getFieldValue(name) : block.getFieldValue(name) || others.connectedTo,
        }));
        this.connections = this.connections.filter(connection => connection.blockId !== block.id);
        this.connections = [...this.connections, ...connections];
        this.renderConnections();
    }

    deleteConnections = (blockId) => {
        this.connections = this.connections.filter(connection => {
            if (connection.blockId === blockId) {
                connection.wireSvg.remove();
                return false;
            }
            return true;
        });
    }
}