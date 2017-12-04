package com.github.daishy.rangeslider;

import com.github.daishy.rangeslider.client.Range;
import com.github.daishy.rangeslider.client.RangeSliderState;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractJavaScriptComponent;
import elemental.json.JsonArray;

import java.util.Objects;

/**
 * The component.
 *
 * @author daishy@github.com
 */
@JavaScript({"generated/rangeslider.min.js"})
@StyleSheet({"generated/rangeslider.min.css"})
// For debugging / testing include the files directly:
//@JavaScript({"nouislider.js", "rangeslider-connector.js"})
//@StyleSheet({"nouislider.css"})
public class RangeSlider extends AbstractJavaScriptComponent implements HasValue<Range> {

    /**
     * The current value of the field.
     */
    private Range value;

    /**
     * Create the new range-slider. The value of this field is initialized with the given boundary.
     *
     * @param boundary The lower and upper boundaries for the range.
     */
    public RangeSlider(Range boundary) {
        this(null, boundary);
    }

    /**
     * Create a new range-slider. The initial value is initialized with the boundary.
     *
     * @param caption  -
     * @param boundary -
     */
    public RangeSlider(String caption, Range boundary) {
        this(caption, boundary, boundary);
    }

    /**
     * Create a new Rangeslider.
     *
     * @param caption  The caption of the field.
     * @param boundary The minimal and maximal value.
     * @param value    The start-value.
     */
    public RangeSlider(String caption, Range boundary, Range value) {
        if (!boundary.contains(value)) {
            throw new IllegalArgumentException("The start value `" + value + "` does not fit within the boundary `" + boundary + "`");
        }
        this.getState().boundaries = boundary;
        this.value = value;
        this.setCaption(caption);

        this.addFunction("valueChanged", this::onValueChanged);
    }


    /**
     * The Callback-methode that is called from the JS-Side each time the value changes.
     *
     * @param data The raw JSON-data.
     */
    private void onValueChanged(JsonArray data) {
        String rawLower = data.getString(0);
        String rawUpper = data.getString(1);
        // raw value is like 2.0
        int lower = Double.valueOf(rawLower).intValue();
        int upper = Double.valueOf(rawUpper).intValue();
        this.setValue(new Range(lower, upper), true);
    }


    @Override
    public void setValue(Range value) {
        this.setValue(value, false);
    }

    /**
     * Update the value. If the new and old value are the same, nothing is done. If the component is readonly and the
     * change was triggered by a user-event the change is ignored (Should not be possible in the first place). If the
     * value is set programmatically the new value is propagated to the js-component. Regardless of the origin, a change-event
     * is fired.
     *
     * @param value          The new value.
     * @param userOriginated <code>true</code> if the change was caused by the user changing the ui-component,
     *                       <code>false</code> if the value was updated via the java-api.
     */
    protected void setValue(Range value, boolean userOriginated) {
        // if the current and the new value are the same do nothing
        if (Objects.equals(value, this.value)) {
            return;
        }

        // if we are in read-only mode also do nothing
        if (userOriginated && this.isReadOnly()) {
            return;
        }

        // check the values are in bound
        if (!this.getBoundaries().contains(value)) {
            throw new IllegalArgumentException("The given value " + value + " does not match the current boundary " + this.getBoundaries());
        }

        Range oldValue = this.value;
        this.value = value;

        // if the value was set programmatically update the js-component
        if (!userOriginated) {
            this.callFunction("setValue", value.getLower(), value.getUpper());
        }

        if (!userOriginated) {
            markAsDirty();
        }

        fireEvent(new ValueChangeEvent<>(this, oldValue, userOriginated));
    }

    @Override
    public Range getValue() {
        return this.value;
    }

    @Override
    protected RangeSliderState getState() {
        return (RangeSliderState) super.getState();
    }

    public void setStep(Integer step) {
        this.getState().step = step;
    }

    public Integer getStep() {
        return this.getState().step;
    }

    /**
     * Update the boundaries of the slider. If the current value does not fit to the new boundaries the lower and/or
     * upper value is changed accordingly.
     *
     * @param boundaries -
     */
    public void setBoundaries(Range boundaries) {
        if (!boundaries.contains(this.value)) {
            this.adaptValueToFitNewBoundaries(boundaries);
        }

        this.getState().boundaries = boundaries;
    }

    private void adaptValueToFitNewBoundaries(Range boundaries) {
        int newLower = Math.max(this.value.getLower(), boundaries.getLower());
        int newUpper = Math.min(this.value.getUpper(), boundaries.getUpper());
        this.setValue(new Range(newLower, newUpper), false);
    }

    /**
     * Shortcut for  {@link RangeSlider#setBoundaries(Range)}.
     *
     * @param lower -
     * @param upper -
     */
    public void setBoundaries(int lower, int upper) {
        this.setBoundaries(new Range(lower, upper));
    }

    /**
     * Get the current boundaries.
     *
     * @return -
     */
    public Range getBoundaries() {
        return this.getState().boundaries;
    }

    public RangeSliderState.Tooltips getTooltips() {
        return this.getState().tooltips;
    }

    public void setTooltips(RangeSliderState.Tooltips value) {
        this.getState().tooltips = value;
    }

    /**
     * Sets the minimum difference allowed between the lower and upper value. <code>null</code> will
     * remove the restriction. If the minimum difference is larger than the current boundaries an exception is thrown.
     * If the minimum difference is not met by the current value, the upper value is changed to match.
     *
     * @param minimumDifference -
     */
    public void setMinimumDifference(Integer minimumDifference) {
        if (minimumDifference != null && minimumDifference > this.getBoundaries().getDifference()) {
            throw new IllegalArgumentException("The current boundaries " + this.getBoundaries() + " don't allow for a minimum difference of " + minimumDifference);
        }
        this.getState().minimumDifference = minimumDifference;
    }

    /**
     * Get the current minimal difference allowed.
     *
     * @return -
     */
    public Integer getMinimumDifference() {
        return this.getState().minimumDifference;
    }

    /**
     * returns true if there currently is a minimum difference set.
     *
     * @return -
     */
    public boolean hasMinimumDifference() {
        return this.getState().minimumDifference != null;
    }


    /**
     * Sets the maximum difference allowed between the lower and upper value. <code>null</code> will
     * remove the restriction.
     *
     * @param maximumDifference -
     */
    public void setMaximumDifference(Integer maximumDifference) {
        if (maximumDifference != null && maximumDifference > this.getBoundaries().getDifference()) {
            throw new IllegalArgumentException("The current boundaries " + this.getBoundaries() + " don't allow for a maximum distance of " + maximumDifference);
        }
        this.getState().maximumDifference = maximumDifference;
    }

    /**
     * Returns the current maximum difference or null if none set.
     *
     * @return -
     */
    public Integer getMaximumDifference() {
        return this.getState().maximumDifference;
    }

    /**
     * Returns true if there currently is a maximum difference set.
     *
     * @return -
     */
    public boolean hasMaximumDifference() {
        return this.getState().maximumDifference != null;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        super.setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return super.isRequiredIndicatorVisible();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.getState().readonly = readOnly;
    }

    @Override
    public boolean isReadOnly() {
        return this.getState().readonly;
    }


    @Override
    public Registration addValueChangeListener(HasValue.ValueChangeListener<Range> listener) {
        return addListener(ValueChangeEvent.class, listener,
                ValueChangeListener.VALUE_CHANGE_METHOD);
    }
}
