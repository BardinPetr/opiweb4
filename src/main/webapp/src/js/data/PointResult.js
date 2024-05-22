class PointResult {
    constructor({x, y, r, result, timestamp, executionTime}) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
        this.timestamp = timestamp;
        this.executionTime = executionTime;
    }
}

export default PointResult;
