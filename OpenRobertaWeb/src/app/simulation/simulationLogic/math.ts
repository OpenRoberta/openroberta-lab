/**
 * @fileOverview Math for a simple robot simulation
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 * @version 0.1
 */

import CONSTANTS from 'simulation.constants';
import { CircleSimulationObject, RectangleSimulationObject, TriangleSimulationObject } from 'simulation.objects';

/**
 * exports helper for calculations in ORsimulation
 *
 * @namespace
 */

/**
 * Convert from degree to radians.
 *
 * @param degree {number}
 *            degree to convert
 * @returns {number} radians
 */
export function toRadians(degree: number): number {
    return degree * (Math.PI / 180);
}

/**
 * Convert from radians to degree.
 *
 * @param radians {number}
 *            radians to convert
 * @returns  {number} degree
 */
export function toDegree(radians: number): number {
    return radians * (180 / Math.PI);
}

/**
 * Get intersection point from two lines.
 *
 * @param Point {line1}
 *            one line
 * @param Point{line2}
 *            another line
 * @returns {Point} point or null, if no intersection found
 */
export const getIntersectionPoint = function (line1: Line, line2: Line): Point {
    var d = (line1.x1 - line1.x2) * (line2.y1 - line2.y2) - (line1.y1 - line1.y2) * (line2.x1 - line2.x2);
    if (d === 0) {
        return null;
    }
    var xi = ((line2.x1 - line2.x2) * (line1.x1 * line1.y2 - line1.y1 * line1.x2) - (line1.x1 - line1.x2) * (line2.x1 * line2.y2 - line2.y1 * line2.x2)) / d;
    var yi = ((line2.y1 - line2.y2) * (line1.x1 * line1.y2 - line1.y1 * line1.x2) - (line1.y1 - line1.y2) * (line2.x1 * line2.y2 - line2.y1 * line2.x2)) / d;

    if (!this.isLineAlignedToPoint({ x: xi, y: yi }, line1)) {
        return null;
    }
    if (!this.isLineAlignedToPoint({ x: xi, y: yi }, line2)) {
        return null;
    }
    return {
        x: xi,
        y: yi,
    };
};

/**
 * Finds the closest intersection from the intersections of a line
 * @memberOf exports
 * @param  {line}
 *              a line
 * @return {x, y}
 *              closest intersection point (coordinate)
 */
export const getClosestIntersectionPointCircle = function (line: Line, circle): Point {
    const intersections = this.getIntersectionPointsCircle(line, circle);

    if (intersections.length == 1) {
        return intersections[0]; // one intersection
    }

    if (intersections.length == 2) {
        const dist1 = getDistance({ x: line.x1, y: line.y1 }, intersections[0]);
        const dist2 = getDistance({ x: line.x1, y: line.y1 }, intersections[1]);

        if (dist1 < dist2) {
            return intersections[0];
        } else {
            return intersections[1];
        }
    }
    return null; // no intersections at all
};

export const getMiddleIntersectionPointCircle = function (line: Line, circle): Point {
    const intersections = this.getIntersectionPointsCircle(line, circle);

    if (intersections.length == 1) {
        return intersections[0]; // one intersection
    }

    if (intersections.length == 2) {
        return { x: 0.5 * (intersections[0].x + intersections[1].x), y: 0.5 * (intersections[0].y + intersections[1].y) };
    }
    return null; // no intersections at all
};

/**
 * Finds the intersection between a circles border
 * and a line from the origin to the otherLineEndPoint.
 * @memberOf exports
 * @param  {line}
 *              a line
 * @return {{x, y}[]}
 *              array with point(s) of the intersection
 */
export const getIntersectionPointsCircle = function (line, circle): Point[] {
    var dx, dy, A, B, C, det, t;

    dx = line.x2 - line.x1;
    dy = line.y2 - line.y1;

    A = dx * dx + dy * dy;
    B = 2 * (dx * (line.x1 - circle.x) + dy * (line.y1 - circle.y));
    C = (line.x1 - circle.x) * (line.x1 - circle.x) + (line.y1 - circle.y) * (line.y1 - circle.y) - circle.r * circle.r;

    det = B * B - 4 * A * C;
    if (A <= 0.0000001 || det < 0) {
        return [];
    } else if (det == 0) {
        // One solution.
        t = -B / (2 * A);
        var intersection1 = { x: line.x1 + t * dx, y: line.y1 + t * dy };

        if (this.isLineAlignedToPoint(intersection1, line)) return [intersection1];

        return [];
    } else {
        // Two solutions.
        t = (-B + Math.sqrt(det)) / (2 * A);
        var intersection1 = { x: line.x1 + t * dx, y: line.y1 + t * dy };
        t = (-B - Math.sqrt(det)) / (2 * A);
        var intersection2 = { x: line.x1 + t * dx, y: line.y1 + t * dy };

        if (this.isLineAlignedToPoint(intersection1, line) && this.isLineAlignedToPoint(intersection2, line)) return [intersection1, intersection2];

        return [];
    }
};

/**
 * Checks if Alignment of lines is correct to sensor
 *
 * @memberOf exports
 * @param {xi}
 *            x coordinate of point
 * @param {yi}
 *            y coordinate of point
 * @param {line}
 *            a line
 * @returns {boolean}
 */
export function isLineAlignedToPoint(p: Point, line: Line): boolean {
    if (p.x < Math.min(line.x1, line.x2) - 0.01 || p.x > Math.max(line.x1, line.x2) + 0.01) {
        return false;
    }
    if (p.y < Math.min(line.y1, line.y2) - 0.01 || p.y > Math.max(line.y1, line.y2) + 0.01) {
        return false;
    }
    return true;
}

export const getLinesFromObj = function (obj) {
    switch (obj.shape) {
        case 'rectangle':
            return [
                {
                    x1: obj.x,
                    x2: obj.x,
                    y1: obj.y,
                    y2: obj.y + obj.h,
                },
                {
                    x1: obj.x,
                    x2: obj.x + obj.w,
                    y1: obj.y,
                    y2: obj.y,
                },
                {
                    x1: obj.x + obj.w,
                    x2: obj.x,
                    y1: obj.y + obj.h,
                    y2: obj.y + obj.h,
                },
                {
                    x1: obj.x + obj.w,
                    x2: obj.x + obj.w,
                    y1: obj.y + obj.h,
                    y2: obj.y,
                },
            ];
        case 'robot':
            return [
                {
                    x1: obj.backLeft.rx,
                    x2: obj.frontLeft.rx,
                    y1: obj.backLeft.ry,
                    y2: obj.frontLeft.ry,
                },
                {
                    x1: obj.frontLeft.rx,
                    x2: obj.frontRight.rx,
                    y1: obj.frontLeft.ry,
                    y2: obj.frontRight.ry,
                },
                {
                    x1: obj.frontRight.rx,
                    x2: obj.backRight.rx,
                    y1: obj.frontRight.ry,
                    y2: obj.backRight.ry,
                },
                {
                    x1: obj.backRight.rx,
                    x2: obj.backLeft.rx,
                    y1: obj.backRight.ry,
                    y2: obj.backLeft.ry,
                },
            ];
        case 'triangle':
            return [
                {
                    x1: obj.ax,
                    x2: obj.bx,
                    y1: obj.ay,
                    y2: obj.by,
                },
                {
                    x1: obj.bx,
                    x2: obj.cx,
                    y1: obj.by,
                    y2: obj.cy,
                },
                {
                    x1: obj.ax,
                    x2: obj.cx,
                    y1: obj.ay,
                    y2: obj.cy,
                },
            ];
        default:
            return false;
    }
};

/**
 * Get distance from a point to a circle's border.
 *
 * @memberOf exports
 * @param {point}
 *            a point
 * @param {circle}
 *            a circle object
 * @returns {distance}
 *           distance
 */
export const getDistanceToCircle = function (point, circle) {
    var vX = point.x - circle.x;
    var vY = point.y - circle.y;
    var magV = Math.sqrt(vX * vX + vY * vY);
    var aX = circle.x + (vX / magV) * circle.r;
    var aY = circle.y + (vY / magV) * circle.r;
    return {
        x: aX,
        y: aY,
    };
};

/**
 * Calculate the square of a number.
 *
 * @memberOf exports
 * @param { Number }
 * x value to square
 * @returns { Number } square of x
 */
export const sqr = function (x) {
    return x * x;
};

/**
 * Get the distance of two points.
 *
 * @memberOf exports
 * @param {p1}
 *            one point
 * @param {p1}
 *            x another point
 * @returns {distance}
 */
function getDistance(p1, p2) {
    return sqr(p1.x - p2.x) + sqr(p1.y - p2.y);
}
/**
 * Get the shortest distance from a point to a line as a vector.
 *
 * @memberOf exports
 * @param {p}
 *            one point
 * @param {p1}
 *            start point of line
 * @param {p2}
 *            p2 end point of line
 * @returns {distance}
 */
function getDistanceToLine(p, p1, p2) {
    var d = getDistance(p1, p2);
    if (d == 0) {
        return p1;
    }
    var t = ((p.x - p1.x) * (p2.x - p1.x) + (p.y - p1.y) * (p2.y - p1.y)) / d;
    if (t < 0) {
        return p1;
    }
    if (t > 1) {
        return p2;
    }
    return {
        x: p1.x + t * (p2.x - p1.x),
        y: p1.y + t * (p2.y - p1.y),
    };
}
export { getDistance, getDistanceToLine };

export const isPointInsideRectangle = function (p, rect) {
    var p1 = rect.p1;
    var p2 = rect.p2;
    var p3 = rect.p3;
    var p4 = rect.p4;
    var t1 = getDistance(p, getDistanceToLine(p, p1, p2));
    var t2 = getDistance(p, getDistanceToLine(p, p2, p3));
    var t3 = getDistance(p, getDistanceToLine(p, p3, p4));
    var t4 = getDistance(p, getDistanceToLine(p, p4, p1));
    var s1 = getDistance(p1, p2);
    var s2 = getDistance(p2, p3);
    if (t1 <= s2 && t3 <= s2 && t2 <= s1 && t4 <= s1) {
        return true;
    } else {
        return false;
    }
};

export const checkInObstacle = function (obstacle, myCheckPoint) {
    if (obstacle instanceof RectangleSimulationObject) {
        return (
            myCheckPoint.rx > obstacle.x &&
            myCheckPoint.rx < obstacle.x + obstacle.w &&
            myCheckPoint.ry > obstacle.y &&
            myCheckPoint.ry < obstacle.y + obstacle.h
        );
    } else if (obstacle instanceof TriangleSimulationObject) {
        var areaOrig = Math.floor(
            Math.abs((obstacle.bx - obstacle.ax) * (obstacle.cy - obstacle.ay) - (obstacle.cx - obstacle.ax) * (obstacle.by - obstacle.ay))
        );
        var area1 = Math.floor(
            Math.abs((obstacle.ax - myCheckPoint.rx) * (obstacle.by - myCheckPoint.ry) - (obstacle.bx - myCheckPoint.rx) * (obstacle.ay - myCheckPoint.ry))
        );
        var area2 = Math.floor(
            Math.abs((obstacle.bx - myCheckPoint.rx) * (obstacle.cy - myCheckPoint.ry) - (obstacle.cx - myCheckPoint.rx) * (obstacle.by - myCheckPoint.ry))
        );
        var area3 = Math.floor(
            Math.abs((obstacle.cx - myCheckPoint.rx) * (obstacle.ay - myCheckPoint.ry) - (obstacle.ax - myCheckPoint.rx) * (obstacle.cy - myCheckPoint.ry))
        );
        if (area1 + area2 + area3 <= areaOrig) {
            return true;
        }
        return false;
    } else if (obstacle instanceof CircleSimulationObject) {
        return (
            (myCheckPoint.rx - obstacle.x) * (myCheckPoint.rx - obstacle.x) + (myCheckPoint.ry - obstacle.y) * (myCheckPoint.ry - obstacle.y) <=
            obstacle.r * obstacle.r
        );
    } else {
        return (
            isPointInsideRectangle(
                {
                    x: myCheckPoint.rx,
                    y: myCheckPoint.ry,
                },
                {
                    p1: {
                        x: obstacle.backLeft.rx,
                        y: obstacle.backLeft.ry,
                    },
                    p2: {
                        x: obstacle.frontLeft.rx,
                        y: obstacle.frontLeft.ry,
                    },
                    p3: {
                        x: obstacle.frontRight.rx,
                        y: obstacle.frontRight.ry,
                    },
                    p4: {
                        x: obstacle.backRight.rx,
                        y: obstacle.backRight.ry,
                    },
                }
            ) ||
            isPointInsideRectangle(
                {
                    x: myCheckPoint.rx,
                    y: myCheckPoint.ry,
                },
                {
                    p1: {
                        x: obstacle.wheelFrontRight.rx,
                        y: obstacle.wheelFrontRight.ry,
                    },
                    p2: {
                        x: obstacle.wheelBackRight.rx,
                        y: obstacle.wheelBackRight.ry,
                    },
                    p3: {
                        x: obstacle.wheelBackLeft.rx,
                        y: obstacle.wheelBackLeft.ry,
                    },
                    p4: {
                        x: obstacle.wheelFrontLeft.rx,
                        y: obstacle.wheelFrontLeft.ry,
                    },
                }
            )
        );
    }
};

/**
 * Convert a rgb value to hsv value.
 *
 * @memberOf exports
 * @param {Number}
 *            r red value
 * @param {Number}
 *            g green value
 * @param {Number}
 *            b blue value
 * @returns {Array} hsv value
 */
//copy from http://stackoverflow.com/questions/2348597/why-doesnt-this-javascript-rgb-to-hsl-code-work
export const rgbToHsv = function (r, g, b) {
    var min = Math.min(r, g, b),
        max = Math.max(r, g, b),
        delta = max - min,
        h,
        s,
        v = max;

    v = Math.floor((max / 255) * 100);
    if (max !== 0) {
        s = Math.floor((delta / max) * 100);
    } else {
        // black
        return [0, 0, 0];
    }
    if (r === max) {
        h = (g - b) / delta; // between yellow & magenta
    } else if (g === max) {
        h = 2 + (b - r) / delta; // between cyan & yellow
    } else {
        h = 4 + (r - g) / delta; // between magenta & cyan
    }
    h = Math.floor(h * 60); // degrees
    if (h < 0) {
        h += 360;
    }
    return [h, s, v];
};

/**
 * Map a hsv value to a color name.
 *
 * @memberOf exports
 * @param {Array}
 *            hsv value
 * @returns {Enum} color
 */
export const getColor = function (hsv) {
    if (hsv[2] <= 10) {
        return CONSTANTS.COLOR_ENUM.BLACK;
    }
    if ((hsv[0] < 10 || hsv[0] > 350) && hsv[1] > 90 && hsv[2] > 50) {
        return CONSTANTS.COLOR_ENUM.RED;
    }
    if (hsv[0] > 40 && hsv[0] < 70 && hsv[1] > 90 && hsv[2] > 50) {
        return CONSTANTS.COLOR_ENUM.YELLOW;
    }
    if (hsv[0] < 50 && hsv[1] > 50 && hsv[1] < 100 && hsv[2] < 50) {
        return CONSTANTS.COLOR_ENUM.BROWN;
    }
    if (hsv[1] < 10 && hsv[2] > 90) {
        return CONSTANTS.COLOR_ENUM.WHITE;
    }
    if (hsv[0] > 70 && hsv[0] < 160 && hsv[1] > 80) {
        return CONSTANTS.COLOR_ENUM.GREEN;
    }
    if (hsv[0] > 200 && hsv[0] < 250 && hsv[1] > 90 && hsv[2] > 50) {
        return CONSTANTS.COLOR_ENUM.BLUE;
    }
    return CONSTANTS.COLOR_ENUM.NONE;
};

// TODO type PointRobotWorld
export function transform(pose, point) {
    const sin = Math.sin(pose.theta);
    const cos = Math.cos(pose.theta);
    point.rx = point.x * cos - point.y * sin + pose.x;
    point.ry = point.x * sin + point.y * cos + pose.y;
}

export function epsilonEqual(num1, num2, epsilon) {
    return Math.abs(num1 - num2) <= epsilon;
}

export function hexToRGB(hex: string): number[] {
    var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
    hex = hex.replace(shorthandRegex, function (m, r, g, b) {
        return r + r + g + g + b + b;
    });
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? [parseInt(result[1], 16), parseInt(result[2], 16), parseInt(result[3], 16)] : null;
}

export function hexToHsv(hex: string): number[] {
    let rgb = hexToRGB(hex);
    return rgbToHsv(rgb[0], rgb[1], rgb[2]);
}
