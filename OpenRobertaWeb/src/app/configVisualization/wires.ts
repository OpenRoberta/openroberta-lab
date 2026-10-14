const DEFAULT_COLORS = { '5V': '#f01414', GND: '#333333' };
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

function distance(first: number, second: number) {
    return Math.abs(first - second);
}

function chooseByDistance(
    selectBetween: [number, number],
    comparisonPoint: number,
    comparator: (x1: number, x2: number) => boolean = (x1, x2) => x1 > x2
): number {
    const comparison = comparator(distance(selectBetween[0], comparisonPoint), distance(selectBetween[1], comparisonPoint));
    if (comparison) {
        return selectBetween[0];
    }
    return selectBetween[1];
}

export interface HubBounds {
    left: number;
    right: number;
    top: number;
    bottom: number;
    isRightPort: boolean;
}

export default class WireDrawer {
    public static readonly SEPARATOR = 6;

    private head: WirePoint;
    private readonly blockCorners: {
        upperLeft: Point;
        lowerRight: Point;
    };
    private readonly hubBounds: HubBounds;
    private readonly portIndex: number;
    private readonly left: boolean;
    private readonly threeSegments: boolean;

constructor(origin, destination, portIndex, blockCorners?, hubBounds?: HubBounds, threeSegments: boolean = false) {
        this.blockCorners = blockCorners;
        this.hubBounds = hubBounds;
        this.threeSegments = threeSegments;
        if (blockCorners) {
            this.left = Math.abs(this.blockCorners.upperLeft.x - origin.x) <= Math.abs(this.blockCorners.lowerRight.x - origin.x);
        }

        this.portIndex = portIndex;
        this.head = new WirePoint(origin);
        this.head.next = new WirePoint(destination);
        this.toOrthoLines_();
    }

    private get wireOffset(): number {
        return (this.portIndex + 1) * WireDrawer.SEPARATOR;
    }

    /** Inserts the given waypoints in order between the wire's origin and destination. */
    private setIntermediatePoints_(points: { x: number; y: number }[]) {
        const dest = this.head.next;
        let current = this.head;
        for (const pt of points) {
            const wp = new WirePoint(new Point(pt.x, pt.y));
            current.next = wp;
            current = wp;
        }
        current.next = dest;
    }

    /** Routes a 3-segment (horizontal-vertical-horizontal) path with the vertical segment at `x`. */
    private routeViaVerticalX_(x: number) {
        this.setIntermediatePoints_([
            { x, y: this.head.pos.y },
            { x, y: this.head.next.pos.y },
        ]);
    }

    /** Routes a 3-segment path with the vertical segment centered between origin and destination. */
    private routeMidpointThreeSegments_() {
        const originX = this.head.pos.x;
        const destinationX = this.head.next.pos.x;
        const offset = this.portIndex * WireDrawer.SEPARATOR * (originX < destinationX ? -1 : 1);
        this.routeViaVerticalX_((originX + destinationX) / 2 + offset);
    }

    /** Routes the wire around the hub bounding box (and component block) to prevent crossing over the hub. */
    private routeAroundHub_() {
        const { x: originX, y: originY } = this.head.pos;
        const { x: destinationX, y: destinationY } = this.head.next.pos;

        const { left, right, top, bottom, isRightPort } = this.hubBounds;
        const blockLeft = this.blockCorners ? this.blockCorners.upperLeft.x : originX;
        const blockRight = this.blockCorners ? this.blockCorners.lowerRight.x : originX;
        const blockTop = this.blockCorners ? this.blockCorners.upperLeft.y : originY;
        const blockBottom = this.blockCorners ? this.blockCorners.lowerRight.y : originY;

        const isSameSideOutsideHub = isRightPort
            ? blockLeft > right + WireDrawer.SEPARATOR
            : blockRight < left - WireDrawer.SEPARATOR;

        if (isSameSideOutsideHub) {
            this.routeMidpointThreeSegments_();
            return;
        }

        const margin = 16 + this.wireOffset;
        const outerX = isRightPort
            ? Math.max(right, destinationX) + margin
            : Math.min(left, destinationX) - margin;

        const exitsBlockCleanly = this.blockCorners ? (this.left ? outerX < originX : outerX > originX) : true;
        const clearsHubVertically = originY <= top - margin || originY >= bottom + margin;

        if (exitsBlockCleanly && clearsHubVertically) {
            this.routeViaVerticalX_(outerX);
            return;
        }

        const entryX = !this.left
            ? blockRight < left
                ? (blockRight + left) / 2 - this.portIndex * WireDrawer.SEPARATOR
                : Math.max(blockRight, right) + margin
            : blockLeft > right
            ? (blockLeft + right) / 2 + this.portIndex * WireDrawer.SEPARATOR
            : Math.min(blockLeft, left) - margin;

        if ((isRightPort && entryX > right) || (!isRightPort && entryX < left)) {
            this.routeViaVerticalX_(entryX);
            return;
        }

        const crossesBlockHorizontally =
            Boolean(this.blockCorners) && Math.min(entryX, outerX) < blockRight && Math.max(entryX, outerX) > blockLeft;
        const clearTop = (crossesBlockHorizontally ? Math.min(top, blockTop) : top) - margin;
        const clearBottom = (crossesBlockHorizontally ? Math.max(bottom, blockBottom) : bottom) + margin;
        const clearY = chooseByDistance([clearTop, clearBottom], (originY + destinationY) / 2, (d1, d2) => d1 <= d2);

        this.setIntermediatePoints_([
            { x: entryX, y: originY },
            { x: entryX, y: clearY },
            { x: outerX, y: clearY },
            { x: outerX, y: destinationY },
        ]);
    }

    /** Routes a 3-segment (horizontal-vertical-horizontal) path for vertical pin-out boards. */
    private routeThreeSegments_() {
        if (!this.blockCorners) {
            this.routeMidpointThreeSegments_();
            return;
        }

        // Adjust path around block while keeping horizontal start/end segments
        const xExtra = this.left ? this.head.pos.x - this.wireOffset : this.head.pos.x + this.wireOffset;
        this.routeViaVerticalX_(xExtra);
    }

    /** Routes a 2-segment (vertical-horizontal) path (or detours around the component block) for horizontal pin-out boards. */
    private routeTwoSegments_() {
        const { x: originX, y: originY } = this.head.pos;
        const { x: destinationX, y: destinationY } = this.head.next.pos;

        let x = originX < destinationX ? Math.max(originX, destinationX) : Math.min(originX, destinationX);
        let y = originY < destinationY ? Math.min(originY, destinationY) : Math.max(originY, destinationY);

        if (!this.blockCorners) {
            this.setIntermediatePoints_([{ x, y }]);
            return;
        }

        // Adjust path around block
        const { lowerRight, upperLeft } = this.blockCorners;

        y = chooseByDistance([lowerRight.y + this.wireOffset, upperLeft.y - this.wireOffset], destinationY, (x, y) => x < y);
        const xExtra = this.left ? originX - this.wireOffset : originX + this.wireOffset;

        this.setIntermediatePoints_([
            { x: xExtra, y: originY },
            { x: xExtra, y },
            { x, y },
        ]);
    }

    /** Computes the orthogonal wire waypoints between origin and destination based on the active routing mode. */
    toOrthoLines_() {
        if (this.hubBounds) {
            this.routeAroundHub_();
            return;
        }

        const { x: originX, y: originY } = this.head.pos;
        const { x: destinationX, y: destinationY } = this.head.next.pos;
        if (originX === destinationX || originY === destinationY) return;

        if (this.threeSegments) {
            this.routeThreeSegments_();
        } else {
            this.routeTwoSegments_();
        }
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
        let b = ((num >> 8) & 0x00ff) + dark;
        b = b < 0 ? 0 : b;
        let g = (num & 0x0000ff) + dark;
        g = g < 0 ? 0 : g;
        let darkColor = g | (b << 8) | (r << 16);
        return '#' + darkColor.toString(16);
    }

    static getColor(block: any, name: string): string {
        return DEFAULT_COLORS[name] ? DEFAULT_COLORS[name] : WireDrawer.darken(block.colour_);
    }
}
