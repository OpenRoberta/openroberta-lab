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

const SEPARATOR = 6;

function distance(first: number, second: number) {
    return Math.abs(first - second);
}

function chooseByDistance(selectBetween: [number, number], comparisonPoint: number,
    comparator: (x1: number, x2: number) => boolean = (x1, x2) => x1 > x2): number {

    const comparison = comparator(distance(selectBetween[0], comparisonPoint), distance(selectBetween[1], comparisonPoint));
    if (comparison) {
        return selectBetween[0];
    }
    return selectBetween[1];
}


export default class WireDrawer {

    head: WirePoint;
    private readonly blockCorners: {
        upperLeft: Point,
        lowerRight: Point
    };

    constructor(origin, destination, blockCorners) {
        this.blockCorners = blockCorners;
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
        const { x: originX, y: originY } = this.head.pos;
        const { x: destinationX, y: destinationY } = this.head.next.pos;

        if (originX === destinationX || originY === destinationY)
            return;

        let x = originX < destinationX ? Math.max(originX, destinationX) : Math.min(originX, destinationX);
        let y = originY < destinationY ? Math.min(originY, destinationY) : Math.max(originY, destinationY);

        if (!this.collidesWithBlock({ x, y })) {
            this.addPoint_(this.head, { x, y });
            return
        }

        // Adjust path around block

        const { lowerRight, upperLeft } = this.blockCorners;

        y = chooseByDistance([lowerRight.y + SEPARATOR, upperLeft.y - SEPARATOR], destinationY, (x, y) => x < y);
        const xExtra = chooseByDistance([originX + SEPARATOR, originX - SEPARATOR], destinationX, (x, y) => x > y);

        this.addPoint_(this.head, { x, y });

        this.addPoint_(this.head, {
            x: xExtra,
            y: y
        });

        this.addPoint_(this.head, {
            x: xExtra,
            y: originY
        });
    }

    private collidesWithBlock(position: Point) {
        const { lowerRight, upperLeft } = this.blockCorners

        return upperLeft.x <= position.x && position.x <= lowerRight.x
            && upperLeft.y <= position.y && position.y <= lowerRight.y;
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