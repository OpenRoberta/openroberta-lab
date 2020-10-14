export class Port {
    element_: any;
    position_: any;
    constructor(parent, name, position) {
        this.position_ = position;
        this.element_ = (<any>window).Blockly.createSvgElement('rect', {
            'class': 'port',
            'width': 5,
            'height': 5,
            'fill': 'red',
            'stroke': 'black',
            'stroke-width': 1,
            'transform': `translate(${position.x}, ${position.y})`,
            'r': 3,
        }, parent);
        if (name) {
            this.element_.tooltip = name;
            (<any>window).Blockly.Tooltip.bindMouseEvents(parent);
        }
    }

    moveTo(position) {
        this.position_ = position;
        this.element_.setAttribute('transform', `translate(${position.x}, ${position.y})`);
    }

    get element() {
        return this.element_;
    }

    get position() {
        return this.position_;
    }
}
