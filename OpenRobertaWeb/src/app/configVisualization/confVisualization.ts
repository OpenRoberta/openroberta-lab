import WireDrawer from './wires';
import { getRobotConfig, RobotBoardConfig } from './const.robots';
import { createRobotBlock } from './robotBlock';
import { Port } from './port';

import * as $ from 'jquery';

const SEP = 2.5;
const STROKE = 1.8;

// fix for IE which does not have the remove function
if (!('remove' in Element.prototype)) {
    (Element.prototype as any).remove = function () {
        if (this.parentNode) {
            this.parentNode.removeChild(this);
        }
    };
}

interface Connection {
    blockId: string;
    blockPort: Port;
    connectedTo: string;
    name?: string;
    position: any;
    wireSvg: any;
}

export class CircuitVisualization {
    components: {};
    connections: Connection[];
    robot: any;
    robotXml: Node;
    wireGroup: any;
    currentRobot: string;
    dom: any;
    workspace: any;
    scale: number = 1;
    observers: MutationObserver[] = [];
    private initialBlockIds: Set<string> = new Set();
    private isUpdating: boolean = false;
    private pendingAutoLayout: boolean = false;

    constructor(workspace: any, dom: string) {
        this.dom = dom;
        this.workspace = workspace;
        if (!(<any>window).Blockly) {
            throw new Error('Blockly required');
        }
        this.components = {};
        this.connections = [];
        this.currentRobot = this.workspace.device + '_' + this.workspace.subDevice;
        this.wireGroup = (<any>window).Blockly.createSvgElement('g', { id: 'wireGroup' }, this.workspace.getCanvas());
        this.injectRobotBoard();
        this.workspace.addChangeListener(this.onChangeListener);
    }

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
        return getRobotConfig(robotGroup, robot) !== undefined;
    }

    private get robotConfig(): RobotBoardConfig | undefined {
        return this.workspace ? getRobotConfig(this.workspace.device, this.workspace.subDevice) : undefined;
    }

    private hasAutoLayout(): boolean {
        return Boolean(this.robotConfig?.autoLayout);
    }

    private hasVerticalPinOut(): boolean {
        return this.robotConfig?.pinOut === 'vertical';
    }

    private hasPortManagement(): boolean {
        return Boolean(this.robotConfig?.portFieldNames?.length);
    }

    private getPortFieldNames(): string[] {
        return this.robotConfig?.portFieldNames || [];
    }

    reset(): void {
        const currentRobot = this.workspace.device + '_' + this.workspace.subDevice;
        if (currentRobot !== this.currentRobot) {
            this.currentRobot = currentRobot;
            this.dom = this.getXml();
            this.clear();
            this.injectRobotBoard();
        }
    }

    refresh(): void {
        if (this.hasAutoLayout()) {
            this.autoLayoutBlocks();
        } else {
            this.workspace.getAllBlocks().forEach((block) => {
                this.updateBlockPorts(block);
                this.renderConnections(this.connections);
            });
        }
    }

    dispose(): void {
        this.workspace.removeChangeListener(this.onChangeListener);
        this.wireGroup.remove();
        this.observers.forEach((observer) => observer.disconnect());
        this.observers = [];
    }

    getXml(): string {
        return (<any>window).Blockly.Xml.workspaceToDom(this.workspace);
    }

    injectRobotBoard(): void {
        const Blockly = (<any>window).Blockly;
        Blockly.Blocks['robConf_robot'] = createRobotBlock(this.currentRobot);

        if (!this.dom.querySelector('block[type=robConf_robot]')) {
            const robotXml = `<instance x="289" y="183"><block type="robConf_robot" id="robot"></block></instance>`;
            const oParser = new DOMParser();
            const robotElement = oParser.parseFromString(robotXml, 'text/xml').firstChild;
            this.dom.appendChild(robotElement);
        }

        const configDecl = this.robotConfig?.configDecl;
        if (configDecl && configDecl.length > 0 && this.dom.querySelectorAll) {
            const builtinBlockTypes = Array.from(new Set(configDecl.map((decl) => `robConf_${decl.type}`)));
            const selector = builtinBlockTypes.map((t) => `block[type=${t}]`).join(', ');
            this.dom.querySelectorAll(selector).forEach((builtinBlock: Element) => {
                const instanceEl = builtinBlock.parentElement;
                if (instanceEl && instanceEl.tagName.toLowerCase() === 'instance') {
                    instanceEl.remove();
                } else {
                    builtinBlock.remove();
                }
            });
        }

        Blockly.Xml.domToWorkspace(this.dom, this.workspace);
        this.robot = this.workspace.getBlockById('robot');
        this.initialBlockIds = new Set(this.workspace.getAllBlocks().map((b: any) => b.id));
        this.updateAllDifferentialDriveStates();
        this.autoLayoutBlocks();
    }

    clear = () => {
        this.connections.forEach((connection) => connection.wireSvg.remove());
        this.connections = [];
        while (this.workspace.getAllBlocks().length) {
            this.workspace.getAllBlocks()[0].dispose();
        }
    };

    private getBlockPortFieldNames(block: any): string[] {
        const configuredPortFieldNames = this.getPortFieldNames();
        if (configuredPortFieldNames.length === 0 || !block || !block.inputList || block.id === 'robot' || !block.isMovable()) {
            return [];
        }
        const names: string[] = [];
        block.inputList.forEach((input) => {
            input.fieldRow.forEach((field) => {
                if (field.name && configuredPortFieldNames.includes(field.name) && block.isDeletable()) {
                    names.push(field.name);
                }
            });
        });
        return names;
    }

    private hasMotorOnPort(portName: string): boolean {
        if (!portName || portName === '' || portName === '-') {
            return false;
        }
        for (const block of this.workspace.getAllBlocks()) {
            if (block.type === 'robConf_motor' && block.getFieldValue('PORT') === portName) {
                return true;
            }
        }
        return false;
    }

    private hasPortConflict(sourceBlock: any, sourceField: string, portValue: string): boolean {
        if (!portValue || portValue === '' || portValue === '-') {
            return false;
        }
        if (sourceBlock.type === 'robConf_differentialdrive') {
            for (const ownField of this.getBlockPortFieldNames(sourceBlock)) {
                if (ownField !== sourceField && sourceBlock.getFieldValue(ownField) === portValue) {
                    return true;
                }
            }
            return false;
        }
        for (const ownField of this.getBlockPortFieldNames(sourceBlock)) {
            if (ownField !== sourceField && sourceBlock.getFieldValue(ownField) === portValue) {
                return true;
            }
        }
        for (const otherBlock of this.workspace.getAllBlocks()) {
            if (otherBlock.id === sourceBlock.id || otherBlock.id === 'robot' || otherBlock.type === 'robConf_differentialdrive') {
                continue;
            }
            for (const otherField of this.getBlockPortFieldNames(otherBlock)) {
                if (otherBlock.getFieldValue(otherField) === portValue) {
                    return true;
                }
            }
        }
        return false;
    }

    private isComponentNameAvailable(name: string, excludeBlock?: any): boolean {
        if (!name) {
            return false;
        }
        const normalized = name.toLowerCase();
        for (const block of this.workspace.getAllBlocks()) {
            if (block === excludeBlock || block.id === 'robot') {
                continue;
            }
            const otherName = block.getFieldValue && block.getFieldValue('NAME');
            if (otherName && otherName.toLowerCase() === normalized) {
                return false;
            }
        }
        return true;
    }

    private getNextNumericMotorName(excludeBlock?: any): string {
        let index = 1;
        while (!this.isComponentNameAvailable('M' + index, excludeBlock)) {
            index++;
        }
        return 'M' + index;
    }

    private assignInitialPortForNewBlock(block: any): void {
        if (!this.hasPortManagement() || !block || block.id === 'robot' || block.type === 'robConf_differentialdrive') {
            return;
        }
        const portFields = this.getBlockPortFieldNames(block);
        if (portFields.length === 0) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        this.isUpdating = true;
        Blockly.Events.disable();
        try {
            let renderedChanged = false;
            for (const fieldName of portFields) {
                const currentVal = block.getFieldValue(fieldName);
                if (this.hasPortConflict(block, fieldName, currentVal)) {
                    const field = block.getField(fieldName);
                    const options: [string, string][] = field && typeof field.getOptions_ === 'function' ? field.getOptions_() : [];
                    let assigned = '';
                    for (const opt of options) {
                        const candidate = opt[1];
                        if (candidate && candidate !== '' && candidate !== '-' && !this.hasPortConflict(block, fieldName, candidate)) {
                            assigned = candidate;
                            break;
                        }
                    }
                    block.setFieldValue(assigned, fieldName);
                    renderedChanged = true;
                }
            }
            if (block.type === 'robConf_motor' && block.getField && block.getField('NAME')) {
                const assignedPort = block.getFieldValue('PORT');
                let targetName: string;
                if (assignedPort && assignedPort !== '' && assignedPort !== '-') {
                    const desiredName = 'M' + assignedPort;
                    targetName = this.isComponentNameAvailable(desiredName, block) ? desiredName : this.getNextNumericMotorName(block);
                } else {
                    targetName = this.getNextNumericMotorName(block);
                }
                if (block.getFieldValue('NAME') !== targetName) {
                    if (this.workspace && typeof this.workspace.markFocused === 'function') {
                        this.workspace.markFocused();
                    }
                    block.setFieldValue(targetName, 'NAME');
                    renderedChanged = true;
                }
            }
            if (renderedChanged && block.rendered) {
                block.render();
            }
        } finally {
            Blockly.Events.enable();
            this.isUpdating = false;
        }
    }

    private resolvePortConflicts(sourceBlock: any, changedField?: string): void {
        if (!this.hasPortManagement() || !sourceBlock || sourceBlock.id === 'robot' || sourceBlock.type === 'robConf_differentialdrive') {
            return;
        }
        const configuredPortFieldNames = this.getPortFieldNames();
        const fieldsToCheck =
            changedField && configuredPortFieldNames.includes(changedField) ? [changedField] : this.getBlockPortFieldNames(sourceBlock);
        if (fieldsToCheck.length === 0) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        this.isUpdating = true;
        Blockly.Events.disable();
        try {
            for (const fieldName of fieldsToCheck) {
                const newPort = sourceBlock.getFieldValue(fieldName);
                if (!newPort || newPort === '' || newPort === '-') {
                    continue;
                }
                for (const ownField of this.getBlockPortFieldNames(sourceBlock)) {
                    if (ownField !== fieldName && sourceBlock.getFieldValue(ownField) === newPort) {
                        sourceBlock.setFieldValue('', ownField);
                        if (sourceBlock.rendered) {
                            sourceBlock.render();
                        }
                    }
                }
                for (const otherBlock of this.workspace.getAllBlocks()) {
                    if (otherBlock.id === sourceBlock.id || otherBlock.id === 'robot' || otherBlock.type === 'robConf_differentialdrive') {
                        continue;
                    }
                    let modifiedOther = false;
                    for (const otherField of this.getBlockPortFieldNames(otherBlock)) {
                        if (otherBlock.getFieldValue(otherField) === newPort) {
                            otherBlock.setFieldValue('', otherField);
                            modifiedOther = true;
                        }
                    }
                    if (modifiedOther) {
                        if (otherBlock.rendered) {
                            otherBlock.render();
                        }
                        this.updateBlockDisabledState(otherBlock);
                        this.syncBlockPortsAndConnections(otherBlock);
                    }
                }
            }
        } finally {
            Blockly.Events.enable();
            this.isUpdating = false;
        }
    }

    private syncMotorNameWithPortChange(motorBlock: any, oldPort: string, newPort: string): void {
        if (
            !this.hasPortManagement() ||
            !motorBlock ||
            motorBlock.type !== 'robConf_motor' ||
            !motorBlock.getField ||
            !motorBlock.getField('NAME') ||
            !newPort ||
            newPort === '' ||
            newPort === '-' ||
            oldPort === newPort
        ) {
            return;
        }
        const currentName = motorBlock.getFieldValue('NAME');
        const isDefaultPortMotorName = Boolean(
            currentName && this.robotConfig?.ports?.some((p) => currentName === 'M' + p.name)
        );
        if (!isDefaultPortMotorName) {
            return;
        }
        const desiredName = 'M' + newPort;
        if (desiredName === currentName || !this.isComponentNameAvailable(desiredName, motorBlock)) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        const wasUpdating = this.isUpdating;
        this.isUpdating = true;
        Blockly.Events.disable();
        try {
            if (this.workspace && typeof this.workspace.markFocused === 'function') {
                this.workspace.markFocused();
            }
            motorBlock.setFieldValue(desiredName, 'NAME');
            if (motorBlock.rendered) {
                motorBlock.render();
            }
        } finally {
            Blockly.Events.enable();
            this.isUpdating = wasUpdating;
        }
    }

    private syncDifferentialDriveWithMotorPortChange(motorBlock: any, oldPort: string, newPort: string): void {
        if (
            !this.hasPortManagement() ||
            !motorBlock ||
            motorBlock.type !== 'robConf_motor' ||
            !oldPort ||
            oldPort === '' ||
            oldPort === '-' ||
            !newPort ||
            newPort === '' ||
            newPort === '-' ||
            oldPort === newPort
        ) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        const wasUpdating = this.isUpdating;
        this.isUpdating = true;
        Blockly.Events.disable();
        try {
            for (const block of this.workspace.getAllBlocks()) {
                if (block.type !== 'robConf_differentialdrive') {
                    continue;
                }
                let updated = false;
                if (block.getFieldValue('MOTOR_L') === oldPort) {
                    block.setFieldValue(newPort, 'MOTOR_L');
                    updated = true;
                }
                if (block.getFieldValue('MOTOR_R') === oldPort) {
                    block.setFieldValue(newPort, 'MOTOR_R');
                    updated = true;
                }
                if (updated) {
                    if (block.rendered) {
                        block.render();
                    }
                    this.updateBlockDisabledState(block);
                    this.syncBlockPortsAndConnections(block);
                }
            }
        } finally {
            Blockly.Events.enable();
            this.isUpdating = wasUpdating;
        }
    }

    private updateAllDifferentialDriveStates(): void {
        if (!this.hasPortManagement()) {
            return;
        }
        for (const block of this.workspace.getAllBlocks()) {
            if (block.type === 'robConf_differentialdrive') {
                this.updateBlockDisabledState(block);
            }
        }
    }

    private updateBlockDisabledState(block: any): void {
        if (!this.hasPortManagement() || !block || block.id === 'robot' || typeof block.setDisabled !== 'function') {
            return;
        }
        if (!block.isMovable() || !block.isDeletable()) {
            if (block.disabled) {
                block.setDisabled(false);
            }
            return;
        }
        const portFields = this.getBlockPortFieldNames(block);
        if (portFields.length === 0) {
            return;
        }
        let isDisconnected = portFields.some((fieldName) => {
            const val = block.getFieldValue(fieldName);
            return !val || val === '' || val === '-' || (this.robot && !this.robot.getPortByName(val));
        });
        if (!isDisconnected && block.type === 'robConf_differentialdrive') {
            const leftPort = block.getFieldValue('MOTOR_L');
            const rightPort = block.getFieldValue('MOTOR_R');
            if (leftPort === rightPort || !this.hasMotorOnPort(leftPort) || !this.hasMotorOnPort(rightPort)) {
                isDisconnected = true;
            }
        }
        const Blockly = (<any>window).Blockly;
        const wasUpdating = this.isUpdating;
        this.isUpdating = true;
        Blockly.Events.disable();
        try {
            block.setDisabled(isDisconnected);
        } finally {
            Blockly.Events.enable();
            this.isUpdating = wasUpdating;
        }
    }

    private syncBlockPortsAndConnections(block: any): void {
        if (!block || block.id === 'robot') {
            return;
        }
        if (block.ports) {
            block.ports.forEach((port) => port.element.remove());
            block.ports = [];
        }
        this.deleteConnections([block.id]);
        this.createBlockPorts(block);
        this.renderBlockConnections(block);
    }

    private scheduleAutoLayout(): void {
        if (!this.hasAutoLayout()) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        if (Blockly.dragMode_ && Blockly.dragMode_ !== 0) {
            this.pendingAutoLayout = true;
            const onMouseUp = () => {
                document.removeEventListener('mouseup', onMouseUp, true);
                setTimeout(() => {
                    if (this.pendingAutoLayout && (!Blockly.dragMode_ || Blockly.dragMode_ === 0)) {
                        this.pendingAutoLayout = false;
                        this.autoLayoutBlocks();
                    }
                }, 0);
            };
            document.addEventListener('mouseup', onMouseUp, true);
        } else {
            this.pendingAutoLayout = false;
            this.autoLayoutBlocks();
        }
    }

    private autoLayoutBlocks(): void {
        if (!this.hasAutoLayout() || !this.robot) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        const wasUpdating = this.isUpdating;
        this.isUpdating = true;
        Blockly.Events.disable();
        try {
            const hubPos = this.robot.getRelativeToSurfaceXY();
            const hubX = hubPos.x;
            const hubY = hubPos.y;
            const rawHubWidth = this.robot.robot_ && this.robot.robot_.width ? parseFloat(this.robot.robot_.width) : 0;
            const hubWidth = rawHubWidth > 0 ? rawHubWidth : 217;

            const allBlocks: any[] = this.workspace
                .getTopBlocks(false)
                .filter((b: any) => b.id !== 'robot' && b.isDeletable());
            const diffDrives: any[] = [];
            const leftConnected: { block: any; portName: string; orderKey: number }[] = [];
            const rightConnected: { block: any; portName: string; orderKey: number }[] = [];
            const disconnected: any[] = [];

            allBlocks.forEach((block) => {
                if (block.type === 'robConf_differentialdrive') {
                    diffDrives.push(block);
                    return;
                }
                const activePort = block.ports && block.ports.length > 0 ? block.ports[0].connectedTo : null;
                const robotPort = activePort ? this.robot.getPortByName(activePort) : null;
                if (!activePort || !robotPort) {
                    disconnected.push(block);
                    return;
                }
                if (robotPort.position.x < hubWidth / 2) {
                    leftConnected.push({ block, portName: activePort, orderKey: robotPort.position.y });
                } else {
                    rightConnected.push({ block, portName: activePort, orderKey: robotPort.position.y });
                }
            });

            leftConnected.sort((a, b) => a.orderKey - b.orderKey);
            rightConnected.sort((a, b) => a.orderKey - b.orderKey);

            const moveBlockTo = (block: any, targetX: number, targetY: number) => {
                const cur = block.getRelativeToSurfaceXY();
                const dx = targetX - cur.x;
                const dy = targetY - cur.y;
                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                    block.moveBy(dx, dy);
                }
            };

            // Position differential drive block(s) at top-left so its ports sit above the hub top edge
            let diffY = Math.max(20, hubY - 135);
            diffDrives.forEach((ddBlock) => {
                const targetX = Math.max(20, hubX - 66 - (ddBlock.width || 148));
                moveBlockTo(ddBlock, targetX, diffY);
                diffY += (ddBlock.height || 132) + 16;
            });

            const startColumnY = Math.max(diffY, hubY + 68);

            // Position left column connected blocks
            const leftRightEdgeX = hubX - 66;
            let leftY = startColumnY;
            leftConnected.forEach(({ block, portName }) => {
                const robotPort = this.robot.getPortByName(portName);
                const idealY = hubY + (robotPort ? robotPort.position.y : 105) - 37;
                const targetY = Math.max(leftY, idealY);
                const targetX = Math.max(15, leftRightEdgeX - (block.width || 120));
                moveBlockTo(block, targetX, targetY);
                leftY = targetY + (block.height || 42) + 16;
            });

            // Position right column connected blocks
            const rightLeftEdgeX = hubX + hubWidth + 54;
            let rightY = Math.max(20, hubY + 68);
            rightConnected.forEach(({ block, portName }) => {
                const robotPort = this.robot.getPortByName(portName);
                const idealY = hubY + (robotPort ? robotPort.position.y : 105) - 37;
                const targetY = Math.max(rightY, idealY);
                moveBlockTo(block, rightLeftEdgeX, targetY);
                rightY = targetY + (block.height || 42) + 16;
            });

            // Position disconnected blocks at the bottom of the shorter column so they never overlap
            disconnected.forEach((block) => {
                if (leftY <= rightY) {
                    const targetX = Math.max(15, leftRightEdgeX - (block.width || 120));
                    moveBlockTo(block, targetX, leftY);
                    leftY += (block.height || 42) + 16;
                } else {
                    moveBlockTo(block, rightLeftEdgeX, rightY);
                    rightY += (block.height || 42) + 16;
                }
            });

            this.workspace.getAllBlocks().forEach((block: any) => {
                if (block.id !== 'robot' && block.ports) {
                    this.updateBlockPorts(block);
                }
            });
            this.renderConnections(this.connections);
        } finally {
            Blockly.Events.enable();
            this.isUpdating = wasUpdating;
        }
    }

    onChangeListener = (event) => {
        if (this.isUpdating) {
            return;
        }
        const Blockly = (<any>window).Blockly;
        if (event.type === Blockly.Events.MOVE && this.pendingAutoLayout && (!Blockly.dragMode_ || Blockly.dragMode_ === 0)) {
            this.pendingAutoLayout = false;
            this.autoLayoutBlocks();
            return;
        }

        let blockId;
        if (!event.blockId) {
            if (event.newValue) {
                blockId = event.newValue;
            } else {
                return;
            }
        } else {
            blockId = event.blockId;
        }
        var block = this.workspace.getBlockById(blockId);

        switch (event.type) {
            case Blockly.Events.CREATE:
                if (!block) {
                    break;
                }
                const isInitialBlock = this.initialBlockIds.has(block.id);
                this.initialBlockIds.delete(block.id);
                if (!isInitialBlock) {
                    this.assignInitialPortForNewBlock(block);
                }
                this.updateBlockDisabledState(block);
                this.createBlockPorts(block);
                this.initEventListeners(block);
                this.renderBlockConnections(block);
                while (block && block.getChildren().length > 0) {
                    block = block.getChildren()[0];
                    this.initialBlockIds.delete(block.id);
                    if (!isInitialBlock) {
                        this.assignInitialPortForNewBlock(block);
                    }
                    this.updateBlockDisabledState(block);
                    this.createBlockPorts(block);
                    this.initEventListeners(block);
                    this.renderBlockConnections(block);
                }
                this.updateAllDifferentialDriveStates();
                if (!isInitialBlock || this.initialBlockIds.size === 0) {
                    this.scheduleAutoLayout();
                }
                break;
            case Blockly.Events.CHANGE:
                if (!block || block.id === 'robot') {
                    break;
                }
                if (this.hasPortManagement() && event.element === 'field' && this.getPortFieldNames().includes(event.name)) {
                    this.resolvePortConflicts(block, event.name);
                    if (block.type === 'robConf_motor' && event.name === 'PORT') {
                        this.syncMotorNameWithPortChange(block, event.oldValue, event.newValue);
                        this.syncDifferentialDriveWithMotorPortChange(block, event.oldValue, event.newValue);
                    }
                    this.updateBlockDisabledState(block);
                    this.updateAllDifferentialDriveStates();
                    this.syncBlockPortsAndConnections(block);
                    this.scheduleAutoLayout();
                } else {
                    this.updateBlockPorts(block);
                    this.updateConnections(block);
                    this.renderBlockConnections(block);
                }
                break;
            case Blockly.Events.DELETE:
                this.deleteConnections(event.ids);
                if (block && block.ports) {
                    block.ports.forEach((port) => port.element.remove());
                }
                this.updateAllDifferentialDriveStates();
                this.scheduleAutoLayout();
                break;
        }
    };

    private initEventListeners(block) {
        if (!block || !block.svgGroup_ || block._hasConfObserver) {
            return;
        }
        block._hasConfObserver = true;
        const observer = new MutationObserver(() => {
            return this.renderBlockConnections(block);
        });

        observer.observe(block.svgGroup_, {
            childList: false,
            subtree: false,
            attributes: true,
            attributeFilter: ['transform'],
        });

        this.observers.push(observer);
    }

    private renderBlockConnections(block: any) {
        if (block.id === 'robot') {
            return this.renderConnections(this.connections);
        } else {
            let blockIds = [];
            blockIds.push(block.id);
            while (block && block.getChildren().length > 0) {
                block = block.getChildren()[0];
                blockIds.push(block.id);
            }
            return this.renderConnections(this.connections.filter(({ blockId }) => blockIds.includes(blockId)));
        }
    }

    private renderConnections(connections: Connection[]): void {
        if (connections.length === 0 || !this.robot) {
            return;
        }
        const robotPosition = this.robot.getRelativeToSurfaceXY();
        const rawHubWidth = this.robot.robot_ && this.robot.robot_.width ? parseFloat(this.robot.robot_.width) : 0;
        const rawHubHeight = this.robot.robot_ && this.robot.robot_.height ? parseFloat(this.robot.robot_.height) : 0;
        const hubWidth = rawHubWidth > 0 ? rawHubWidth : 217;
        const hubHeight = rawHubHeight > 0 ? rawHubHeight : 342;
        const useHubBounds = this.hasAutoLayout();
        const verticalPinOut = this.hasVerticalPinOut();

        connections.forEach(({ blockId, position, connectedTo, wireSvg, blockPort }) => {
            const block = this.workspace.getBlockById(blockId);
            if (!block) {
                return;
            }
            const robotConnection = this.robot.getPortByName(connectedTo);
            if (!robotConnection) {
                return;
            }
            if (this.needToUpdateBlockPorts(block, position, connectedTo)) {
                this.updateBlockPorts(block);
                position.x = this.calculatePortPosition(block, connectedTo);
            }
            const blockPosition = block.getRelativeToSurfaceXY();
            const origin = {
                x: blockPosition.x + position.x + SEP,
                y: blockPosition.y + position.y + SEP,
            };
            const destination = {
                x: robotPosition.x + robotConnection.position.x + SEP,
                y: robotPosition.y + robotConnection.position.y + SEP,
            };

            const blockCorners = this.calculateBlockCorners(block);
            const wireShouldWrap = this.shouldWireWrap(block, destination);
            const useDynamicRouting = useHubBounds && block.isDeletable();
            const hubBounds = useDynamicRouting
                ? {
                      left: robotPosition.x,
                      right: robotPosition.x + hubWidth,
                      top: robotPosition.y,
                      bottom: robotPosition.y + hubHeight,
                      isRightPort: robotConnection.position.x >= hubWidth / 2,
                  }
                : undefined;
            const drawer = new WireDrawer(
                origin,
                destination,
                block.ports.indexOf(blockPort),
                useDynamicRouting || wireShouldWrap ? blockCorners : undefined,
                hubBounds,
                useDynamicRouting && verticalPinOut
            );

            wireSvg.setAttribute('d', drawer.path);
            wireSvg.setAttribute('stroke-width', STROKE);
        });
        $(this.wireGroup).remove().appendTo(this.workspace.getCanvas());
    }

    private shouldWireWrap(block, destination) {
        const {
            lowerRight: { x: rightEdge, y: lowerEdge },
            upperLeft: { x: leftEdge, y: upperEdge },
        } = this.calculateBlockCorners(block);

        return leftEdge - WireDrawer.SEPARATOR <= destination.x && destination.x <= rightEdge + WireDrawer.SEPARATOR;
    }

    private calculateBlockCorners(block) {
        const relativeUpperLeft = block.getRelativeToSurfaceXY();
        return {
            upperLeft: relativeUpperLeft,
            lowerRight: {
                x: relativeUpperLeft.x + block.width,
                y: relativeUpperLeft.y + block.height,
            },
        };
    }

    private needToUpdateBlockPorts(block: any, portPosition: any, connectedTo: any): boolean {
        if (connectedTo && this.robot && this.robot.getPortByName(connectedTo)) {
            return portPosition.x !== this.calculatePortPosition(block, connectedTo);
        }
        return false;
    }

    updateBlockPorts = (block) => {
        if (!block || !block.ports) {
            return;
        }
        block.ports.forEach((port) => {
            if (!this.robot || !this.robot.getPortByName(port.connectedTo)) {
                return;
            }
            const position = port.position;
            port.moveTo({ ...position, x: this.calculatePortPosition(block, port.connectedTo) });
        });

        this.connections = this.connections.map(({ position, connectedTo, ...others }) => {
            if (others.blockId !== block.id || !this.robot || !this.robot.getPortByName(connectedTo)) {
                return { position, connectedTo, ...others };
            }
            return {
                position: { ...position, x: this.calculatePortPosition(block, connectedTo) },
                connectedTo,
                ...others,
            };
        });
    };

    private calculatePortPosition(block, connectedTo) {
        const portObj = this.robot.getPortByName(connectedTo);
        if (!portObj) {
            return -SEP;
        }
        const blockPosition = block.getRelativeToSurfaceXY().x + block.width / 2;
        const robotPortPosition = this.robot.getRelativeToSurfaceXY().x + portObj.position.x;

        if (blockPosition < robotPortPosition) {
            return block.width - SEP;
        }
        return -SEP;
    }

    createBlockPorts = (block) => {
        if (!block || block.id === 'robot' || !this.robot) {
            return;
        }
        block.ports = [];
        if (block.inputList.length > 0) {
            block.inputList.forEach((input, index) => {
                if (index === 0) {
                    if (this.robot.getPortByName(block.confBlock)) {
                        this.appendPortAndConnection(block, input.fieldRow[0].textElement_, name, block.confBlock);
                    }
                } else {
                    input.fieldRow.forEach(({ fieldGroup_, name, value_ }) => {
                        name = name || value_;
                        if (name) {
                            const fieldVal = block.getFieldValue(name);
                            const connectedTo =
                                value_ && this.robot.getPortByName(block.confBlock + ' ' + value_)
                                    ? block.confBlock + ' ' + value_
                                    : fieldVal && fieldVal !== '-' && this.robot.getPortByName(fieldVal)
                                    ? fieldVal
                                    : this.robot.getPortByName(name)
                                    ? name
                                    : null;
                            if (connectedTo) {
                                this.appendPortAndConnection(block, fieldGroup_, name, connectedTo);
                            }
                        }
                    });
                }
            });
        } else {
            if (this.robot.getPortByName(block.confBlock)) {
                this.appendPortAndConnection(block, block.inputList[0], block.confBlock, block.confBlock);
            }
        }
    };

    appendPortAndConnection = (block, svgElement, name, connectedTo) => {
        if (!svgElement || !svgElement.transform || svgElement.transform.baseVal.numberOfItems === 0) {
            return;
        }
        const { matrix } = svgElement.transform.baseVal.getItem(0);
        const position = {
            x: this.calculatePortPosition(block, connectedTo),
            y: matrix.f + 6,
        };
        const port = new Port(block.getSvgRoot(), name, { ...position }, connectedTo);
        block.ports.push(port);
        const wireColor = WireDrawer.getColor(block, name);
        const wireSvg = (<any>window).Blockly.createSvgElement(
            'path',
            {
                fill: 'none',
                stroke: wireColor,
                'stroke-width': STROKE,
                'stroke-linecap': 'round',
                'stroke-linejoin': 'round',
            },
            this.wireGroup
        );

        this.connections.push({
            blockId: block.id,
            connectedTo: connectedTo,
            blockPort: port,
            name,
            position: { ...position },
            wireSvg,
        });
    };

    updateConnections = (block) => {
        let connections = this.connections.filter((connection) => connection.blockId === block.id);
        connections = connections.map(({ name, ...others }) => ({
            name,
            ...others,
            connectedTo: this.robot.getPortByName(block.confBlock + ' ' + block.getFieldValue(name))
                ? block.confBlock + ' ' + block.getFieldValue(name)
                : block.getFieldValue(name) || others.connectedTo,
        }));
        this.connections = this.connections.filter((connection) => connection.blockId !== block.id);
        this.connections = [...this.connections, ...connections];
    };

    deleteConnections = (blockIds) => {
        this.connections = this.connections.filter((connection) => {
            if (blockIds.includes(connection.blockId)) {
                connection.wireSvg.remove();
                return false;
            }
            return true;
        });
    };
}
