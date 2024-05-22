class BaseCanvas {
    #sizeUpdateHandler;

    constructor(canvas, onClick) {
        this._onClickCallback = onClick;
        this._storedDrawParams = null;

        this._canvas = $(canvas);
        this._canvas.off("click").on("click", (evt) => this._onClick(evt));

        this._ctx = canvas.getContext("2d");

        $(window).on("resize", () => {
            if (this.#sizeUpdateHandler) clearTimeout(this.#sizeUpdateHandler);
            this.#sizeUpdateHandler = setTimeout(() => {
                this.redraw();
            }, 300);
        });
    }

    _onClick({offsetX, offsetY}) {
        this._onClickCallback([offsetX, offsetY]);
    }

    setup(params) {
    }

    redraw(params) {
        const finalParams = params || this._storedDrawParams;
        this._storedDrawParams = finalParams;

        this._ctx.reset();
        this._ctx.font = "12px arial";

        this._width = this._canvas.width();
        this._height = this._canvas.height();

        this._canvas.prop({width: this._width, height: this._height});
    }

    _env(func) {
        this._ctx.save();
        func(this._ctx);
        this._ctx.restore();
    }

    drawLine(start, end, width = 1) {
        this._env((c) => {
            c.beginPath();
            c.lineWidth = width;
            c.moveTo(...start);
            c.lineTo(...end);
            c.stroke();
        });
    }

    drawArrow([x0, y0], [x1, y1], width = 1, size = 10) {
        this._env((c) => {
            const angle = Math.atan2(y1 - y0, x1 - x0);
            const delta = (isSin, angleDelta) =>
                size * (isSin ? Math.sin : Math.cos)(angle + angleDelta);

            c.beginPath();
            c.lineWidth = width;
            c.moveTo(x0, y0);
            c.lineTo(x1, y1);
            c.moveTo(x1 - delta(false, -Math.PI / 6), y1 - delta(true, -Math.PI / 6));
            c.lineTo(x1, y1);
            c.lineTo(x1 - delta(false, Math.PI / 6), y1 - delta(true, Math.PI / 6));
            c.stroke();
        });
    }

    drawCircle(center, radius, fill) {
        this._env((c) => {
            c.beginPath();
            c.fillStyle = fill;
            c.arc(...center, radius, 0, 2 * Math.PI, true);
            c.fill();
        });
    }
}

export default BaseCanvas;
