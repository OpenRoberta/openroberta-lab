/**
 * @fileOverview Math for a simple robot simulation
 * @author Beate Jost <beate.jost@smail.inf.h-brs.de>
 * @version 0.1
 */

/**
 * SIMATH helper for calculations in ORsimulation
 * 
 * @namespace
 */
var SIMATH = {};
(function($) {
    /**
     * Convert from degree to radians.
     * 
     * @memberOf SIMATH
     * @param {Number}
     *            degree to convert
     * @returns {Number} radians
     */
    SIMATH.toRadians = function(degree) {
        return degree * (Math.PI / 180);
    };
    /**
     * Convert from radians to degree.
     * 
     * @memberOf SIMATH
     * @param {Number}
     *            radians to convert
     * @returns {Number} degree
     */
    SIMATH.toDegree = function(radians) {
        return radians * (180 / Math.PI);
    };
    /**
     * Get intersection point from two lines.
     * 
     * @memberOf SIMATH
     * @param {line1}
     *            one line
     * @param {line2}
     *            another line
     * @returns {point} or null, if no intersection found
     */
    SIMATH.getIntersectionPoint = function(line1, line2) {
        var d = (line1.x1 - line1.x2) * (line2.y1 - line2.y2) - (line1.y1 - line1.y2) * (line2.x1 - line2.x2);
        if (d == 0)
            return null;
        var xi = ((line2.x1 - line2.x2) * (line1.x1 * line1.y2 - line1.y1 * line1.x2) - (line1.x1 - line1.x2) * (line2.x1 * line2.y2 - line2.y1 * line2.x2))
                / d;
        var yi = ((line2.y1 - line2.y2) * (line1.x1 * line1.y2 - line1.y1 * line1.x2) - (line1.y1 - line1.y2) * (line2.x1 * line2.y2 - line2.y1 * line2.x2))
                / d;

        if (xi < Math.min(line1.x1, line1.x2) - 0.01 || xi > Math.max(line1.x1, line1.x2) + 0.01) {
            return null;
        }
        if (xi < Math.min(line2.x1, line2.x2) - 0.01 || xi > Math.max(line2.x1, line2.x2) + 0.01) {
            return null;
        }
        if (yi < Math.min(line1.y1, line1.y2) - 0.01 || yi > Math.max(line1.y1, line1.y2) + 0.01) {
            return null;
        }
        if (yi < Math.min(line2.y1, line2.y2) - 0.01 || yi > Math.max(line2.y1, line2.y2) + 0.01) {
            return null;
        }
        return {
            x : xi,
            y : yi
        };
    };
    /**
     * Get four lines from a rectangle.
     * 
     * @memberOf SIMATH
     * @param {rect}
     *            a rectangle
     * @returns {Array} four lines
     */
    SIMATH.getLinesFromRect = function(rect) {
        return [ {
            x1 : rect.x,
            x2 : rect.x,
            y1 : rect.y,
            y2 : rect.y + rect.h
        }, {

            x1 : rect.x,
            x2 : rect.x + rect.w,
            y1 : rect.y,
            y2 : rect.y
        }, {
            x1 : rect.x + rect.w,
            x2 : rect.x,
            y1 : rect.y + rect.h,
            y2 : rect.y + rect.h
        }, {
            x1 : rect.x + rect.w,
            x2 : rect.x + rect.w,
            y1 : rect.y + rect.h,
            y2 : rect.y
        } ];
    };
    /**
     * Calculate the square of a number.
     * 
     * @memberOf SIMATH
     * @param {Number}
     *            x value to square
     * @returns {Number} square of x
     */
    SIMATH.sqr = function(x) {
        return x * x
    };
    /**
     * Get the distance of two points.
     * 
     * @memberOf SIMATH
     * @param {p1}
     *            one point
     * @param {p1}
     *            x another point
     * @returns {distance}
     */
    SIMATH.getDistance = function(p1, p2) {
        return SIMATH.sqr(p1.x - p2.x) + SIMATH.sqr(p1.y - p2.y)
    };
    /**
     * Get the shortest distance from a point to a line as a vector.
     * 
     * @memberOf SIMATH
     * @param {p}
     *            one point
     * @param {p1}
     *            start point of line
     * @param {p2}
     *            p2 end point of line
     * @returns {distance}
     */
    SIMATH.getDistanceToLine = function(p, p1, p2) {
        var d = SIMATH.getDistance(p1, p2);
        if (d == 0)
            return p1;
        var t = ((p.x - p1.x) * (p2.x - p1.x) + (p.y - p1.y) * (p2.y - p1.y)) / d;
        if (t < 0)
            return p1;
        if (t > 1)
            return p2;
        return ({
            x : p1.x + t * (p2.x - p1.x),
            y : p1.y + t * (p2.y - p1.y)
        });
    };
    /**
     * Convert a rgb value to hsv value.
     * 
     * @memberOf SIMATH
     * @param {Number}
     *            r red value
     * @param {Number}
     *            g green value
     * @param {Number}
     *            b blue value
     * @returns {Array} hsv value
     */
//copy from http://stackoverflow.com/questions/2348597/why-doesnt-this-javascript-rgb-to-hsl-code-work
    SIMATH.rgbToHsv = function(r, g, b) {
        var min = Math.min(r, g, b), max = Math.max(r, g, b), delta = max - min, h, s, v = max;

        v = Math.floor(max / 255 * 100);
        if (max != 0)
            s = Math.floor(delta / max * 100);
        else {
            // black
            return [ 0, 0, 0 ];
        }

        if (r == max)
            h = (g - b) / delta; // between yellow & magenta
        else if (g == max)
            h = 2 + (b - r) / delta; // between cyan & yellow
        else
            h = 4 + (r - g) / delta; // between magenta & cyan

        h = Math.floor(h * 60); // degrees
        if (h < 0)
            h += 360;

        return [ h, s, v ];
    };
    /**
     * Map a hsv value to a color name.
     * 
     * @memberOf SIMATH
     * @param {Array}
     *            hsv value
     * @returns {Enum} color
     */
    SIMATH.getColor = function(hsv) {
        if (hsv[2] <= 10)
            return COLOR_ENUM.BLACK;
        if ((hsv[0] < 10 || hsv[0] > 350) && hsv[1] > 90 && hsv[2] > 50)
            return COLOR_ENUM.RED;
        if (hsv[0] > 40 && hsv[0] < 70 && hsv[1] > 90 && hsv[2] > 50)
            return COLOR_ENUM.YELLOW;
        if (hsv[0] < 50 && hsv[1] > 50 && hsv[1] < 100 && hsv[2] < 50)
            return COLOR_ENUM.BROWN;
        if (hsv[1] < 10 && hsv[2] > 90)
            return COLOR_ENUM.WHITE;
        if (hsv[0] > 70 && hsv[0] < 160 && hsv[1] > 80)
            return COLOR_ENUM.GREEN;
        if (hsv[0] > 200 && hsv[0] < 250 && hsv[1] > 90 && hsv[2] > 50)
            return COLOR_ENUM.BLUE;
        return COLOR_ENUM.NONE;
    }
})($);
