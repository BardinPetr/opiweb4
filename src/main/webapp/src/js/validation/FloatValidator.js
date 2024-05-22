import {Validator} from "./Validator";

const EPS = 1e-6;
const isFloat = (x) => /^-?\d+(\.\d+)?$/.test(x);

class FloatValidator extends Validator {
    #min;
    #max;
    #minInclusive;
    #maxInclusive;

    constructor({min, max, minInclusive, maxInclusive}) {
        super();
        this.#min = min;
        this.#max = max;
        this.#minInclusive = minInclusive;
        this.#maxInclusive = maxInclusive;
    }

    check(value) {
        if (value === null || value === undefined)
            return {
                valid: false,
                message: "Can't be empty",
            };

        if (value.toString().length > 6)
            return {
                valid: false,
                message: "Precision exceeded",
            };

        if (!isFloat(value))
            return {
                valid: false,
                message: "Should be a number",
            };

        const val = this._postprocessValue(value);

        const message = `Should be in range ${this.#minInclusive ? "[" : "("}${this.#min}, ${this.#max}${this.#maxInclusive ? "]" : ")"}`;

        const maxDelta = this.#max - val;
        const minDelta = val - this.#min;
        const valid =
            (this.#maxInclusive ? (Math.abs(maxDelta) < EPS || maxDelta > EPS) : maxDelta > EPS) &&
            (this.#minInclusive ? (Math.abs(minDelta) < EPS || minDelta > EPS) : minDelta > EPS);

        return {
            valid,
            message: valid ? "OK" : message,
        };
    }

    _postprocessValue(value) {
        return parseFloat(value);
    }
}

export default FloatValidator;
