const DEFAULT_COLORS = { "5V": "#f01414", "GND": "#333333" };
const DARK = 50;

class Point {
    x: number;
    y: number;

    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }
}

class WirePoint {

    next: WirePoint;
    pos: Point;

    constructor(position: Point) {
        this.pos = position;
        this.next = null;
    }

    get position() {
        return this.pos;
    }
}

export default class WireDrawer {

    head: WirePoint;

    constructor(origin, destination) {
        this.head = new WirePoint(origin);
        this.head.next = new WirePoint(destination);
        this.toOrthoLines_();
    }

    addPoint_(prevPoint, position) {
        const newPoint = new WirePoint(position);
        newPoint.next = prevPoint.next;
        prevPoint.next = newPoint;
    }

    toOrthoLines_() {
        const { x: x1, y: y1 } = this.head.pos;
        const { x: x2, y: y2 } = this.head.next.pos;

        if (x1 === x2 || y1 === y2)
            return;

        const x = x1 < x2 ? Math.max(x1, x2) : Math.min(x1, x2);
        const y = y1 < y2 ? Math.min(y1, y2) : Math.max(y1, y2);

        this.addPoint_(this.head, { x, y });
    }

    get path() {
        const moveto = this.head.position;
        let path = `M ${moveto.x} ${moveto.y}`;

        let current = this.head.next;
        while (current !== null) {
            const lineto = current.position;
            path = `${path} L ${lineto.x} ${lineto.y}`;
            current = current.next;
        }
        return path;
    }

    static darken(color) {
        let dark = -DARK;
        color = color.slice(1);
        let num = parseInt(color, 16);
        let r = (num >> 16) + dark;
        r = r < 0 ? 0 : r;
        let b = ((num >> 8) & 0x00FF) + dark;
        b = b < 0 ? 0 : b;
        let g = (num & 0x0000FF) + dark;
        g = g < 0 ? 0 : g;
        let darkColor = g | (b << 8) | (r << 16);
        return ("#" + darkColor.toString(16));
    }

    static getColor(block: any, name: string): string {
        return DEFAULT_COLORS[name] ? DEFAULT_COLORS[name] : WireDrawer.darken(block.colour_);
    }
}