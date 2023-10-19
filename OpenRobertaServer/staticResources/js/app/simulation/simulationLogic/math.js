/**
 * @fileOverview Math for a simple robot simulation
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 * @version 0.1
 */
define(["require", "exports", "simulation.constants", "simulation.objects"], function (require, exports, simulation_constants_1, simulation_objects_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.rgbToHex = exports.hexToHsv = exports.hexToRGB = exports.epsilonEqual = exports.transform = exports.getColor = exports.rgbToHsv = exports.checkInObstacle = exports.isPointInsideRectangle = exports.getDistanceToLine = exports.getDistance = exports.sqr = exports.getDistanceToCircle = exports.getLinesFromRect = exports.isLineAlignedToPoint = exports.intersects = exports.getMiddleIntersectionCircleRect = exports.inside = exports.getIntersectionPointsCircle = exports.getIntersectionPointsCircle2 = exports.getMiddleIntersectionPointCircle = exports.getMiddleIntersectionPointCircles = exports.getClosestIntersectionPointCircle = exports.getIntersectionPoint = exports.toDegree = exports.toRadians = exports.getDotProduct = void 0;
    function getDotProduct(A, B) {
        var Alength = Math.sqrt(A.x * A.x + A.y * A.y);
        var Blength = Math.sqrt(B.x * B.x + B.y * B.y);
        if (Alength == 0 || Blength == 0) {
            return null;
        }
        return (A.x / Alength) * (B.x / Blength) + (A.y / Alength) * (B.y / Blength);
    }
    exports.getDotProduct = getDotProduct;
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
    function toRadians(degree) {
        return degree * (Math.PI / 180);
    }
    exports.toRadians = toRadians;
    /**
     * Convert from radians to degree.
     *
     * @param radians {number}
     *            radians to convert
     * @returns  {number} degree
     */
    function toDegree(radians) {
        return radians * (180 / Math.PI);
    }
    exports.toDegree = toDegree;
    /**
     * Get intersection point from two lines.
     *
     * @param Point {line1}
     *            one line
     * @param Point{line2}
     *            another line
     * @returns {Point} point or null, if no intersection found
     */
    var getIntersectionPoint = function (line1, line2) {
        var d = (line1.x1 - line1.x2) * (line2.y1 - line2.y2) - (line1.y1 - line1.y2) * (line2.x1 - line2.x2);
        if (d === 0) {
            return null;
        }
        var xi = ((line2.x1 - line2.x2) * (line1.x1 * line1.y2 - line1.y1 * line1.x2) - (line1.x1 - line1.x2) * (line2.x1 * line2.y2 - line2.y1 * line2.x2)) / d;
        var yi = ((line2.y1 - line2.y2) * (line1.x1 * line1.y2 - line1.y1 * line1.x2) - (line1.y1 - line1.y2) * (line2.x1 * line2.y2 - line2.y1 * line2.x2)) / d;
        if (!isLineAlignedToPoint({ x: xi, y: yi }, line1)) {
            return null;
        }
        if (!isLineAlignedToPoint({ x: xi, y: yi }, line2)) {
            return null;
        }
        return {
            x: xi,
            y: yi,
        };
    };
    exports.getIntersectionPoint = getIntersectionPoint;
    /**
     * Finds the closest intersection from the intersections of a line
     * @memberOf exports
     * @param  {line}
     *              a line
     * @return {x, y}
     *              closest intersection point (coordinate)
     */
    var getClosestIntersectionPointCircle = function (line, circle) {
        var intersections = (0, exports.getIntersectionPointsCircle)(line, circle);
        if (intersections.length == 1) {
            return intersections[0]; // one intersection
        }
        if (intersections.length == 2) {
            var dist1 = getDistance({ x: line.x1, y: line.y1 }, intersections[0]);
            var dist2 = getDistance({ x: line.x1, y: line.y1 }, intersections[1]);
            if (dist1 < dist2) {
                return intersections[0];
            }
            else {
                return intersections[1];
            }
        }
        return null; // no intersections at all
    };
    exports.getClosestIntersectionPointCircle = getClosestIntersectionPointCircle;
    var getMiddleIntersectionPointCircles = function (circle1, circle2) {
        /*let d = Math.sqrt(Math.pow(circle2.x - circle1.x, 2) + Math.pow(circle2.y - circle1.y, 2));
        let a = circle1.r * circle1.r - circle2.r * circle2.r + (d * d) / (2 * d);
        let h = Math.sqrt(Math.pow(circle1.r, 2) - a * a);
        if (circle1.r * circle1.r - a * a <= 0) {
            return null;
        }
        let aX = circle1.x + (a * (circle2.x - circle1.x)) / d;
        let aY = circle1.y + (a * (circle2.y - circle1.y)) / d;
        let bX = aX + (h * (circle2.y - circle1.y)) / d;
        let bY = aY - (h * (circle2.x - circle1.x)) / d;*/
        var dSq = (circle2.x - circle1.x) * (circle2.x - circle1.x) + (circle2.y - circle1.y) * (circle2.y - circle1.y);
        if (dSq < (circle1.r + circle2.r) * (circle1.r + circle2.r)) {
            return { x: (circle1.x + circle2.x) / 2, y: (circle1.y + circle2.y) / 2 }; // TODO this is only true if r1 and r2 are the same
        }
        return null;
    };
    exports.getMiddleIntersectionPointCircles = getMiddleIntersectionPointCircles;
    var getMiddleIntersectionPointCircle = function (line, circle, tolerance) {
        tolerance = tolerance || 0;
        var circleWTolerance = { x: circle.x, y: circle.y, r: circle.r + tolerance };
        var intersections = (0, exports.getIntersectionPointsCircle)(line, circleWTolerance);
        if (intersections.length == 1) {
            return intersections[0]; // one intersection
        }
        if (intersections.length == 2) {
            return { x: 0.5 * (intersections[0].x + intersections[1].x), y: 0.5 * (intersections[0].y + intersections[1].y) };
        }
        return null; // no intersections at all
    };
    exports.getMiddleIntersectionPointCircle = getMiddleIntersectionPointCircle;
    /**
     * Finds the intersection between a circles border
     * and a line from the origin to the otherLineEndPoint.
     * @memberOf exports
     * @param  {line}
     *              a line
     * @return {{x, y}[]}
     *              array with point(s) of the intersection
     */
    var getIntersectionPointsCircle2 = function (line, circle) {
        var dx, dy, A, B, C, det, t;
        dx = line.x2 - line.x1;
        dy = line.y2 - line.y1;
        A = dx * dx + dy * dy;
        B = 2 * (dx * (line.x1 - circle.x) + dy * (line.y1 - circle.y));
        C = (line.x1 - circle.x) * (line.x1 - circle.x) + (line.y1 - circle.y) * (line.y1 - circle.y) - circle.r * circle.r;
        det = B * B - 4 * A * C;
        if (A <= 0.0000001 || det < 0) {
            return [];
        }
        else if (det == 0) {
            // One solution.
            t = -B / (2 * A);
            var intersection1 = { x: line.x1 + t * dx, y: line.y1 + t * dy };
            if (isLineAlignedToPoint(intersection1, line))
                return [intersection1];
            return [];
        }
        else {
            // Two solutions.
            t = (-B + Math.sqrt(det)) / (2 * A);
            var intersection1 = { x: line.x1 + t * dx, y: line.y1 + t * dy };
            t = (-B - Math.sqrt(det)) / (2 * A);
            var intersection2 = { x: line.x1 + t * dx, y: line.y1 + t * dy };
            if (isLineAlignedToPoint(intersection1, line) && isLineAlignedToPoint(intersection2, line))
                return [intersection1, intersection2];
            return [];
        }
    };
    exports.getIntersectionPointsCircle2 = getIntersectionPointsCircle2;
    var getIntersectionPointsCircle = function (line, circle) {
        var pointA = { x: line.x1, y: line.y1 };
        var pointB = { x: line.x2, y: line.y2 };
        var center = { x: circle.x, y: circle.y };
        var radius = circle.r;
        var onLine = function (p1, p2, p) {
            if (epsilonEqual(Math.sqrt(getDistance(p1, p)) + Math.sqrt(getDistance(p2, p)), Math.sqrt(getDistance(p1, p2)), 2)) {
                return true;
            }
            return false;
        };
        var baX = pointB.x - pointA.x;
        var baY = pointB.y - pointA.y;
        var caX = center.x - pointA.x;
        var caY = center.y - pointA.y;
        var a = baX * baX + baY * baY;
        var bBy2 = baX * caX + baY * caY;
        var c = caX * caX + caY * caY - radius * radius;
        var pBy2 = bBy2 / a;
        var q = c / a;
        var disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return [];
        }
        // if disc == 0 ... dealt with later
        var tmpSqrt = Math.sqrt(disc);
        var abScalingFactor1 = -pBy2 + tmpSqrt;
        var abScalingFactor2 = -pBy2 - tmpSqrt;
        var p1 = { x: pointA.x - baX * abScalingFactor1, y: pointA.y - baY * abScalingFactor1 };
        if (disc == 0) {
            // abScalingFactor1 == abScalingFactor2
            return onLine(pointA, pointB, p1) ? [p1] : [];
        }
        var p2 = { x: pointA.x - baX * abScalingFactor2, y: pointA.y - baY * abScalingFactor2 };
        p1 = onLine(pointA, pointB, p1) ? p1 : null;
        p2 = onLine(pointA, pointB, p2) ? p2 : null;
        return p1 && p2 ? [p1, p2] : p1 ? [p1] : p2 ? [p2] : [];
    };
    exports.getIntersectionPointsCircle = getIntersectionPointsCircle;
    var inside = function (circle, rect) {
        if (circle.x - circle.r <= rect.x) {
            return false;
        }
        if (circle.y - circle.r <= rect.y) {
            return false;
        }
        if (circle.x + circle.r >= rect.x + rect.w) {
            return false;
        }
        if (circle.y + circle.r >= rect.y + rect.w) {
            return false;
        }
        return true;
    };
    exports.inside = inside;
    var getMiddleIntersectionCircleRect = function (circle, rect, lines) {
        if ((0, exports.intersects)(circle, rect)) {
            for (var i = 0; i < lines.length; i++) {
                var p = (0, exports.getMiddleIntersectionPointCircle)(lines[i], circle);
                if (p) {
                    return p;
                }
            }
            return null;
        }
        else {
            return null;
        }
    };
    exports.getMiddleIntersectionCircleRect = getMiddleIntersectionCircleRect;
    var intersects = function (circle, rect) {
        var clamp = function (value, min, max) {
            return value < min ? min : value > max ? max : value;
        };
        // Find the closest point to the circle within the rectangle
        var closestX = clamp(circle.x, rect.x, rect.x + rect.w);
        var closestY = clamp(circle.y, rect.y, rect.y + rect.h);
        // Calculate the distance between the circle's center and this closest point
        var distX = circle.x - closestX;
        var distY = circle.y - closestY;
        // If the distance is less than the circle's radius, an intersection occurs
        var distSq = distX * distX + distY * distY;
        return distSq < circle.r * circle.r;
    };
    exports.intersects = intersects;
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
    function isLineAlignedToPoint(p, line) {
        if (p.x < Math.min(line.x1, line.x2) - 0.01 || p.x > Math.max(line.x1, line.x2) + 0.01) {
            return false;
        }
        if (p.y < Math.min(line.y1, line.y2) - 0.01 || p.y > Math.max(line.y1, line.y2) + 0.01) {
            return false;
        }
        return true;
    }
    exports.isLineAlignedToPoint = isLineAlignedToPoint;
    var getLinesFromRect = function (rect) {
        return [
            { x1: rect.x, y1: rect.y, x2: rect.x + rect.w, y2: rect.y },
            { x1: rect.x + rect.w, y1: rect.y, x2: rect.x + rect.w, y2: rect.x + rect.h },
            { x1: rect.x + rect.w, y1: rect.y + rect.h, x2: rect.x, y2: rect.y + rect.h },
            { x1: rect.x, y1: rect.y + rect.h, x2: rect.x, y2: rect.y },
        ];
    };
    exports.getLinesFromRect = getLinesFromRect;
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
    var getDistanceToCircle = function (point, circle) {
        var vX = point.x - circle.x;
        var vY = point.y - circle.y;
        var magV = Math.sqrt(vX * vX + vY * vY);
        var aX = circle.x + (vX / magV) * circle.r;
        var aY = circle.y + (vY / magV) * circle.r;
        return {
            x: point.x - aX,
            y: point.y - aY,
        };
    };
    exports.getDistanceToCircle = getDistanceToCircle;
    /**
     * Calculate the square of a number.
     *
     * @memberOf exports
     * @param { Number }
     * x value to square
     * @returns { Number } square of x
     */
    var sqr = function (x) {
        return x * x;
    };
    exports.sqr = sqr;
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
        return (0, exports.sqr)(p1.x - p2.x) + (0, exports.sqr)(p1.y - p2.y);
    }
    exports.getDistance = getDistance;
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
    exports.getDistanceToLine = getDistanceToLine;
    var isPointInsideRectangle = function (p, rect) {
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
        }
        else {
            return false;
        }
    };
    exports.isPointInsideRectangle = isPointInsideRectangle;
    var checkInObstacle = function (obstacle, myCheckPoint) {
        if (obstacle instanceof simulation_objects_1.RectangleSimulationObject) {
            return (myCheckPoint.rx > obstacle.x &&
                myCheckPoint.rx < obstacle.x + obstacle.w &&
                myCheckPoint.ry > obstacle.y &&
                myCheckPoint.ry < obstacle.y + obstacle.h);
        }
        else if (obstacle instanceof simulation_objects_1.TriangleSimulationObject) {
            var areaOrig = Math.floor(Math.abs((obstacle.bx - obstacle.ax) * (obstacle.cy - obstacle.ay) - (obstacle.cx - obstacle.ax) * (obstacle.by - obstacle.ay)));
            var area1 = Math.floor(Math.abs((obstacle.ax - myCheckPoint.rx) * (obstacle.by - myCheckPoint.ry) - (obstacle.bx - myCheckPoint.rx) * (obstacle.ay - myCheckPoint.ry)));
            var area2 = Math.floor(Math.abs((obstacle.bx - myCheckPoint.rx) * (obstacle.cy - myCheckPoint.ry) - (obstacle.cx - myCheckPoint.rx) * (obstacle.by - myCheckPoint.ry)));
            var area3 = Math.floor(Math.abs((obstacle.cx - myCheckPoint.rx) * (obstacle.ay - myCheckPoint.ry) - (obstacle.ax - myCheckPoint.rx) * (obstacle.cy - myCheckPoint.ry)));
            if (area1 + area2 + area3 <= areaOrig) {
                return true;
            }
            return false;
        }
        else if (obstacle instanceof simulation_objects_1.CircleSimulationObject) {
            return ((myCheckPoint.rx - obstacle.x) * (myCheckPoint.rx - obstacle.x) + (myCheckPoint.ry - obstacle.y) * (myCheckPoint.ry - obstacle.y) <=
                obstacle.r * obstacle.r);
        }
        else {
            return ((0, exports.isPointInsideRectangle)({
                x: myCheckPoint.rx,
                y: myCheckPoint.ry,
            }, {
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
            }) ||
                (0, exports.isPointInsideRectangle)({
                    x: myCheckPoint.rx,
                    y: myCheckPoint.ry,
                }, {
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
                }));
        }
    };
    exports.checkInObstacle = checkInObstacle;
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
    var rgbToHsv = function (r, g, b) {
        var min = Math.min(r, g, b), max = Math.max(r, g, b), delta = max - min, h, s, v = max;
        v = Math.floor((max / 255) * 100);
        if (max !== 0) {
            s = Math.floor((delta / max) * 100);
        }
        else {
            // black
            return [0, 0, 0];
        }
        if (r === max) {
            h = (g - b) / delta; // between yellow & magenta
        }
        else if (g === max) {
            h = 2 + (b - r) / delta; // between cyan & yellow
        }
        else {
            h = 4 + (r - g) / delta; // between magenta & cyan
        }
        h = Math.floor(h * 60); // degrees
        if (h < 0) {
            h += 360;
        }
        return [h, s, v];
    };
    exports.rgbToHsv = rgbToHsv;
    /**
     * Map a hsv value to a color name.
     *
     * @memberOf exports
     * @param {Array}
     *            hsv value
     * @returns {Enum} color
     */
    var getColor = function (hsv) {
        if (hsv[2] <= 10) {
            return [simulation_constants_1.default.COLOR_ENUM.BLACK, '#000000'];
        }
        if ((hsv[0] < 10 || hsv[0] > 350) && hsv[1] > 90 && hsv[2] > 50) {
            return [simulation_constants_1.default.COLOR_ENUM.RED, '#FA010C'];
        }
        if (hsv[0] > 40 && hsv[0] < 70 && hsv[1] > 90 && hsv[2] > 50) {
            return [simulation_constants_1.default.COLOR_ENUM.YELLOW, '#F7F700'];
        }
        if (hsv[0] < 50 && hsv[1] > 50 && hsv[1] < 100 && hsv[2] < 50) {
            return [simulation_constants_1.default.COLOR_ENUM.BROWN, '#EBC300'];
        }
        if (hsv[1] < 10 && hsv[2] > 90) {
            return [simulation_constants_1.default.COLOR_ENUM.WHITE, '#FFFFFF'];
        }
        if (hsv[0] > 70 && hsv[0] < 160 && hsv[1] > 80) {
            return [simulation_constants_1.default.COLOR_ENUM.GREEN, '#00852a'];
        }
        if (hsv[0] > 165 && hsv[0] <= 200 && hsv[1] > 70 && hsv[2] > 75) {
            return [simulation_constants_1.default.COLOR_ENUM.NONE, '#33B8CA'];
        }
        if (hsv[0] > 200 && hsv[0] < 250 && hsv[1] > 90 && hsv[2] > 50) {
            return [simulation_constants_1.default.COLOR_ENUM.BLUE, '#1e5aa8'];
        }
        return [simulation_constants_1.default.COLOR_ENUM.NONE, '#EBC300'];
    };
    exports.getColor = getColor;
    // TODO type PointRobotWorld
    function transform(pose, point) {
        var sin = Math.sin(pose.theta);
        var cos = Math.cos(pose.theta);
        point.rx = point.x * cos - point.y * sin + pose.x;
        point.ry = point.x * sin + point.y * cos + pose.y;
    }
    exports.transform = transform;
    function epsilonEqual(num1, num2, epsilon) {
        return Math.abs(num1 - num2) <= epsilon;
    }
    exports.epsilonEqual = epsilonEqual;
    function hexToRGB(hex) {
        var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
        hex = hex.replace(shorthandRegex, function (m, r, g, b) {
            return r + r + g + g + b + b;
        });
        var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
        return result ? [parseInt(result[1], 16), parseInt(result[2], 16), parseInt(result[3], 16)] : null;
    }
    exports.hexToRGB = hexToRGB;
    function hexToHsv(hex) {
        var rgb = hexToRGB(hex);
        return (0, exports.rgbToHsv)(rgb[0], rgb[1], rgb[2]);
    }
    exports.hexToHsv = hexToHsv;
    var rgbToHex = function (r, g, b) {
        return '#' +
            [r, g, b]
                .map(function (x) {
                var hex = x.toString(16);
                return hex.length === 1 ? '0' + hex : hex;
            })
                .join('');
    };
    exports.rgbToHex = rgbToHex;
});
