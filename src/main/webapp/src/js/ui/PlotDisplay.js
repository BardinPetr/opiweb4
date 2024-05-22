import {range} from "../utils/utils";
import BaseCanvas from "./BaseCanvas.js";

class AxesDisplay extends BaseCanvas {
    _ratio = 1;
    _ptsWidth = 0;
    _ptsHeight = 0;
    #targetDimension = 0;
    _storedDrawParams = 0;

    constructor(canvas, onClick) {
        super(canvas, onClick);
    }

    _onClick({offsetX, offsetY}) {
        this._onClickCallback(this._fromCanvas([offsetX, offsetY]));
    }

    setup(params) {
        super.setup(params);
        this.#targetDimension = params.targetDimension;
    }

    redraw(params) {
        super.redraw(params);

        const [xDim, yDim] = this.#targetDimension;
        this._ratio = Math.floor(
            Math.min(this._width / (xDim + 2), this._height / (yDim + 2))
        );

        this._ptsWidth = Math.floor(this._width / this._ratio);
        this._ptsHeight = Math.floor(this._height / this._ratio);

        this.#drawAxes();
    }

    _toCanvas([x, y]) {
        return [
            this._width / 2 + x * this._ratio,
            this._height / 2 - y * this._ratio,
        ];
    }

    _fromCanvas([x, y]) {
        return [
            (x - this._width / 2) / this._ratio,
            (this._height / 2 - y) / this._ratio,
        ];
    }

    drawArrow(from, to, width, size) {
        super.drawArrow(this._toCanvas(from), this._toCanvas(to), width, size);
    }

    drawLine(from, to, width) {
        super.drawLine(this._toCanvas(from), this._toCanvas(to), width);
    }

    drawCircle(center, radius, color) {
        super.drawCircle(this._toCanvas(center), radius * this._ratio, color);
    }

    drawText(pos, text, textBaseline = "middle", textAlign = "left") {
        this._env((c) => {
            c.textBaseline = textBaseline;
            c.textAlign = textAlign;
            c.fillText(text, ...this._toCanvas(pos));
        });
    }

    #drawAxes(tickPeriod = 1, tickSize = 5) {
        this.drawArrow([0, -this._ptsHeight / 2], [0, this._ptsHeight / 2], 2);
        this.drawArrow([-this._ptsWidth / 2, 0], [this._ptsWidth / 2, 0], 2);

        tickSize /= this._ratio;

        this.drawText([2 * tickSize, (this._ptsHeight / 2) * 0.95], "y");
        this.drawText([(this._ptsWidth / 2) * 0.95, -2 * tickSize], "x");

        for (const i of range(
            Math.floor(-this._ptsHeight / 2 + 1),
            this._ptsHeight / 2 - 1,
            tickPeriod
        )) {
            if (Math.round(i) === 0) continue;
            this.drawText([tickSize + tickSize / 4, i], i, "middle", "left");
            this.drawLine([-tickSize, i], [tickSize, i], 1);
        }
        for (const i of range(
            Math.floor(-this._ptsWidth / 2 + 1),
            this._ptsWidth / 2 - 1,
            tickPeriod
        )) {
            if (Math.round(i) === 0) continue;
            this.drawText([i, 2 * tickSize], i, "middle", "center");
            this.drawLine([i, -tickSize], [i, tickSize], 1);
        }
    }
}

export default AxesDisplay;
