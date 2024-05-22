export const range = (start, end, step = 1) =>
    [...Array(Math.ceil((end - start) / step) + 1).keys()].map(
        (i) => start + i * step
    );

export const absMax = (...arr) => Math.max(...arr.map((i) => Math.abs(i)));

export function toFixedString(val, len) {
    let [dec, frac] = val.toString().split('.');
    if (frac == null) return dec;
    return `${dec}.${frac.substring(0, Math.min(len, frac.length))}`
}
