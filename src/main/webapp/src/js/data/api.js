import {toFixedString} from "../utils/utils";
import PointResult from "../data/PointResult";

const extractReturn = x => x.jqXHR.pfArgs;

const pointResultFromServer =
    ({x, y, r, inside}) =>
        new PointResult({x, y, r, result: inside});

export const checkPointRequest = async (x, y, r) => {
    const {result} = extractReturn(await remoteSendPoint([
        {name: 'x', value: toFixedString(x, 8)},
        {name: 'y', value: toFixedString(y, 8)},
        {name: 'r', value: toFixedString(r, 8)}
    ]));
    return result ? pointResultFromServer(result) : null;
}

export const getR = async () => extractReturn(await remoteGetR()).r;

const extractPoints = (response) => (response.points || []).map(pointResultFromServer);

let lastTime = 0;
export const getNewPoints = async () => {
    const res = extractReturn(await remoteGetNewPoints([
        {name: "from", value: lastTime}
    ]));
    lastTime = Date.now();
    return extractPoints(res);
}

export const getPoints = async () =>
    extractPoints(extractReturn(await remoteGetNewPoints()));

export const getConstraints = async () => {
    const {constraints: [r, x, y]} = extractReturn(await remoteGetConstraints());
    return {r, x, y}
}


