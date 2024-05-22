import FloatValidator from "./FloatValidator";
import DoubleRange from "./DoubleRange";

class ClientRangeValidator {
    validate(element, value) {
        let range = new DoubleRange(element.data("range"));
        let validator = new FloatValidator(range)
        let {valid, message} = validator.check(value)

        if (!valid)
            throw {summary: message, detail: message}
    }
}

export default () => {
    const doubleValidatorText = 'Value "{0}" is not a number';
    PrimeFaces.locales['en'].messages['jakarta.faces.converter.DoubleConverter.DOUBLE'] =
        PrimeFaces.locales['en'].messages['jakarta.faces.converter.DoubleConverter.DOUBLE_detail'] = doubleValidatorText;
    PrimeFaces.validator['RangeExternalValidated'] = new ClientRangeValidator();
};