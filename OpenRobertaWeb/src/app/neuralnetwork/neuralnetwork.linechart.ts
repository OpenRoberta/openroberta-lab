/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as d3 from 'd3';

type DataPoint = {
    x: number;
    y: number;
};

/**
 * A multi-series line chart that allows you to append new data points
 * as data becomes available.
 */

export class AppendingLineChart {
    private data: DataPoint[] = [];
    private svg = null;
    private xScale;
    private yScale;
    private path;
    private container;
    private lineColor;
    private isInitialised: boolean = false;

    private minY = Number.MAX_VALUE;
    private maxY = Number.MIN_VALUE;

    constructor(container, lineColor: string, initLineChart: boolean = true) {
        this.container = container;
        this.lineColor = lineColor;
        initLineChart && this.drawLineChart();
    }

    public drawLineChart(redraw: boolean = false) {
        this.isInitialised = true;
        let node = this.container.node() as HTMLElement;
        let totalWidth = node.offsetWidth;
        let totalHeight = node.offsetHeight;
        let margin = { top: 0, right: 0, bottom: 0, left: 0 };
        let width = totalWidth - margin.left - margin.right;
        let height = totalHeight - margin.top - margin.bottom;

        this.xScale = d3.scale.linear().domain([0, 0]).range([0, width]);

        this.yScale = d3.scale.linear().domain([0, 0]).range([height, 0]);

        if (this.svg !== null) {
            d3.select(this.svg)[0][0][0][0].remove();
        }

        this.svg = this.container
            .append('svg')
            .attr('width', width - margin.left - margin.right)
            .attr('height', height - margin.top - margin.bottom);

        this.path = this.svg.append('g').attr('transform', `translate(2, 2) scale(0.98, 0.9)`).append('path').attr('class', 'line').style({
            fill: 'none',
            stroke: this.lineColor,
            'stroke-width': '1.5px',
        });

        redraw && this.redraw();
    }

    reset() {
        this.data = [];
        this.isInitialised && this.redraw();
        this.minY = Number.MAX_VALUE;
        this.maxY = Number.MIN_VALUE;
    }

    addDataPoint(dataPoint: number) {
        this.minY = Math.min(this.minY, dataPoint);
        this.maxY = Math.max(this.maxY, dataPoint);

        this.data.push({ x: this.data.length + 1, y: dataPoint });
        this.redraw();
    }

    public addDataPoints(dataPoints: number[]) {
        this.minY = Math.min(this.minY, ...dataPoints);
        this.maxY = Math.max(this.maxY, ...dataPoints);

        this.data = dataPoints.map((dataPoint, index) => {
            return { x: index + 1, y: dataPoint };
        });
        this.redraw();
    }

    private redraw() {
        // Adjust the x and y domain.
        this.xScale.domain([1, this.data.length]);
        this.yScale.domain([this.minY, this.maxY]);
        // Adjust all the <path> elements (lines).
        let getPathMap = () => {
            return d3.svg
                .line<{ x: number; y: number }>()
                .x((d) => this.xScale(d.x))
                .y((d) => this.yScale(d.y));
        };
        this.path.datum(this.data).attr('d', getPathMap());
    }
}
