export class Validator {

    _postprocessValue(value) {
        return value
    }

    check(value) {
        return {valid: true, message: ""}
    }
}
