import "../utils/jquery.js";
import FigureDisplay from "../views/FigureDisplay.js";
import PointResultStorage from "../data/PointResultStorage.js";
import {checkPointRequest, getConstraints} from "../data/api.js";
import {absMax} from "../utils/utils.js";
import primefacesInjectValidator from "../validation/ClientRangeValidator.js";

class MainPage {
    #window;
    #store;
    #plot;
    #C;
    #disableTimeout;

    constructor() {
        primefacesInjectValidator();

        this.#window = $(window);

        this.#store = new PointResultStorage();
        this.#plot = new FigureDisplay(
            document.getElementById("plot-canvas"),
            this.#store,
            (coord) => this.#pointSelected(coord)
        );

        this.#setup();
    }

    async #setup() {
        this.#C = await getConstraints();

        this.#plot.setup({
            targetDimension: [
                2 * absMax(this.#C.x.min, this.#C.x.max),
                2 * absMax(this.#C.y.min, this.#C.y.max)
            ],
        });

        await this.#store.start();

        this.#window.on("resize", () => this.onResize());
        this.updateTableSize();

        // catch validation errors on R input field and disable plot
        const rInput = $('#point-check-form\\:rInput');
        rInput.on(
            "keyup",
            () => this.#checkPlotDisable(rInput)
        )

        console.info("STARTED");
    }

    #checkPlotDisable(rField) {
        if (this.#disableTimeout)
            clearTimeout(this.#disableTimeout)

        this.#disableTimeout =
            setTimeout(() =>
                    this.#plot.redraw({
                        disable: rField.attr("aria-invalid") === 'true'
                    }),
                300
            )
    }

    onResize() {
        this.updateTableSize();
    }

    updateTableSize() {
        $(".ui-datatable-scrollable-body").css("max-height", this.#window.height() * 0.5)
    }

    async #pointSelected([x, y]) {
        const r = this.#store.getR();
        if (!r) {
            alert("Please select R first");
            return;
        }

        console.log(`Point R=${r} (${x}, ${y})`);

        try {
            const res = await checkPointRequest(x, y, r);
            if (res == null) {
                alert("Invalid request");
                return;
            }
            this.#store.add(res);
        } catch (err) {
            console.error(err);
            alert("Invalid request");
        }
    }
}

$(() => new MainPage())

