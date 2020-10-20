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
            this.renderBlockBackground(block);
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
        const xml = (<any>window).Blockly.Xml.workspaceToDom(this.workspace);
        xml.querySelector('block[type=robot]').parentNode.remove();
        return xml;
    }

    injectRobotBoard(): void {
        if (this.robotXml) {
            (this.robotXml as any).remove();
        }
        (<any>window).Blockly.Blocks['robot'] = createRobotBlock(this.currentRobot);
        const robotXml = `<instance x="250" y="250"><block type="robot" id="robot"></block></instance>`;
        const oParser = new DOMParser();
        this.robotXml = oParser.parseFromString(robotXml, 'text/xml').firstChild;
        this.dom.appendChild(this.robotXml);
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

        if (event.type !== (<any>window).Blockly.Events.UI)
            this.renderBlockBackground(block);
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
        const { matrix } = this.workspace.getCanvas().transform.baseVal.getItem(0);
        this.connections.forEach(({ blockId, position, connectedTo, wireSvg }) => {
            const block = this.workspace.getBlockById(blockId);
            if (!block) {
                return;
            }
            const blockPosition = block.getRelativeToSurfaceXY();
            const origin = {
                x: matrix.e + this.workspace.scale * (blockPosition.x + position.x + SEP),
                y: matrix.f + this.workspace.scale * (blockPosition.y + position.y + SEP),
            }
            const robotConnection = this.robot.getPortByName(connectedTo)
            if (!robotConnection) {
                return;
            }
            const destination = {
                x: matrix.e + this.workspace.scale * (robotPosition.x + robotConnection.position.x + SEP),
                y: matrix.f + this.workspace.scale * (robotPosition.y + robotConnection.position.y + SEP),
            }
            const drawer = new WireDrawer(origin, destination);
            wireSvg.setAttribute('d', drawer.path);
            wireSvg.setAttribute('stroke-width', STROKE * this.workspace.scale);
        });
    }
    
    updateBlockPorts = (block) => {
        const positionX = block.width + 4;
        block.ports.forEach(port => {
            const position = port.position;
            port.moveTo({ ...position, x: positionX });
        });

        this.connections = this.connections.map(({ position, ...others }) => {
            if (others.blockId !== block.id) {
                return { position, ...others };
            }
            return {
                position: { ...position, x: positionX },
                ...others,
            };
        });
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
        const position = { x: block.width + 4, y: matrix.f + 6 };
        const port = new Port(block.getSvgRoot(), name, position);
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

    renderBlockBackground = (block) => {
        if (!block) {
            return;
        }
        const newWidth = block.width + 16;
        let path = block.svgPath_.getAttribute('d');
        path = path.replace(block.width.toString(), newWidth.toString());
        block.svgPath_.setAttribute('d', path);
    }
}