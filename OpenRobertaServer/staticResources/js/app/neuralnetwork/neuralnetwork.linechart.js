/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
define(["require", "exports", "d3"], function (require, exports, d3) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.AppendingLineChart = void 0;
    /**
     * A multi-series line chart that allows you to append new data points
     * as data becomes available.
     */
    var AppendingLineChart = /** @class */ (function () {
        function AppendingLineChart(container, lineColor, initLineChart) {
            if (initLineChart === void 0) { initLineChart = true; }
            this.data = [];
            this.svg = null;
            this.isInitialised = false;
            this.minY = Number.MAX_VALUE;
            this.maxY = Number.MIN_VALUE;
            this.container = container;
            this.lineColor = lineColor;
            initLineChart && this.drawLineChart();
        }
        AppendingLineChart.prototype.drawLineChart = function (redraw) {
            if (redraw === void 0) { redraw = false; }
            this.isInitialised = true;
            var node = this.container.node();
            var totalWidth = node.offsetWidth;
            var totalHeight = node.offsetHeight;
            var margin = { top: 0, right: 0, bottom: 0, left: 0 };
            var width = totalWidth - margin.left - margin.right;
            var height = totalHeight - margin.top - margin.bottom;
            this.xScale = d3.scale.linear().domain([0, 0]).range([0, width]);
            this.yScale = d3.scale.linear().domain([0, 0]).range([height, 0]);
            if (this.svg !== null) {
                d3.select(this.svg)[0][0][0][0].remove();
            }
            this.svg = this.container
                .append('svg')
                .attr('width', width - margin.left - margin.right)
                .attr('height', height - margin.top - margin.bottom);
            this.path = this.svg.append('g').attr('transform', "translate(2, 2) scale(0.98, 0.9)").append('path').attr('class', 'line').style({
                fill: 'none',
                stroke: this.lineColor,
                'stroke-width': '1.5px',
            });
            redraw && this.redraw();
        };
        AppendingLineChart.prototype.reset = function () {
            this.data = [];
            this.isInitialised && this.redraw();
            this.minY = Number.MAX_VALUE;
            this.maxY = Number.MIN_VALUE;
        };
        AppendingLineChart.prototype.addDataPoint = function (dataPoint) {
            this.minY = Math.min(this.minY, dataPoint);
            this.maxY = Math.max(this.maxY, dataPoint);
            this.data.push({ x: this.data.length + 1, y: dataPoint });
            this.redraw();
        };
        AppendingLineChart.prototype.addDataPoints = function (dataPoints) {
            this.minY = Math.min.apply(Math, __spreadArray([this.minY], dataPoints, false));
            this.maxY = Math.max.apply(Math, __spreadArray([this.maxY], dataPoints, false));
            this.data = dataPoints.map(function (dataPoint, index) {
                return { x: index + 1, y: dataPoint };
            });
            this.redraw();
        };
        AppendingLineChart.prototype.redraw = function () {
            var _this = this;
            // Adjust the x and y domain.
            this.xScale.domain([1, this.data.length]);
            this.yScale.domain([this.minY, this.maxY]);
            // Adjust all the <path> elements (lines).
            var getPathMap = function () {
                return d3.svg
                    .line()
                    .x(function (d) { return _this.xScale(d.x); })
                    .y(function (d) { return _this.yScale(d.y); });
            };
            this.path.datum(this.data).attr('d', getPathMap());
        };
        return AppendingLineChart;
    }());
    exports.AppendingLineChart = AppendingLineChart;
});
