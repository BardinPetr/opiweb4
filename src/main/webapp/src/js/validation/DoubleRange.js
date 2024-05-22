const INCLUSIVE_NAME = 'INCLUSIVE';

class DoubleRange {
    constructor({min, minType, max, maxType}) {
        this.min = min;
        this.max = max;
        this.minInclusive = minType === INCLUSIVE_NAME;
        this.maxInclusive = maxType === INCLUSIVE_NAME;
    }
}

export default DoubleRange;