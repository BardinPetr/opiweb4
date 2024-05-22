import {getNewPoints, getPoints, getR} from "./api";

const EVENT_SOURCES = ["point-check-form:sendBtn", "point-check-form:rInput", "table:clear-form"]

class PointResultStorage {
    #callbacks = {
        clear: [], insert: [], config: [], change: []
    };
    #points = [];
    #config = {};

    constructor() {
    }

    async start() {
        $(document).on('pfAjaxUpdated', (xhr, settings) => {
            const source = settings.pfSettings.source.trim();
            if (EVENT_SOURCES.includes(source)) {
                console.log(`Update from ${source}`)
                this.#fetch();
            }
        });

        await this.#fetch();
    }

    async #fetch(partial) {
        console.log("Fetching updates")

        this.#config.r = await getR();
        this.#notify("config", this.#config);

        if (partial) {
            let update = await getNewPoints();
            if (!update.length) return;
            this.#points.push(...update)
        } else {
            this.#points = await getPoints();
        }

        this.#notify("change", this.#points);
    }

    on(type, callback) {
        this.#callbacks[type].push(callback);
    }

    #notify(type, data) {
        this.#callbacks[type].forEach((cb) => cb(data));
    }

    add(point) {
        this.#points.push(point)
        this.#notify("insert", point);
    }

    get() {
        return this.#points;
    }

    getR() {
        return this.#config.r
    }
}

export default PointResultStorage;
