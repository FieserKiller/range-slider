package com.github.daishy.rangeslider;

import com.github.daishy.rangeslider.client.Range;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 * Convenience-Class for converting {@link Range}-Objects to a string-representation in a textfield and back.
 *
 * @author daishy@github.com
 */
public class RangeConverter implements Converter<String, Range> {

    private String message;

    public RangeConverter(String message) {
        this.message = message;
    }

    public RangeConverter() {
        this("The entered value is not a valid range (e.g. 0-10).");
    }

    @Override
    public Result<Range> convertToModel(String value, ValueContext context) {
        try {
            String[] values = value.split("-");
            int lower = Integer.valueOf(values[0].trim());
            int upper = Integer.valueOf(values[1].trim());
            return Result.ok(new Range(lower, upper));
        } catch (Exception e) {
            return Result.error(this.message);
        }
    }

    @Override
    public String convertToPresentation(Range value, ValueContext context) {
        return String.format("%d - %d", value.getLower(), value.getUpper());
    }
}
