package com.github.daishy.rangeslider;

import com.github.daishy.rangeslider.client.Range;
import com.vaadin.data.HasValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Consumer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author daishy@github.com
 */
public class RangeSliderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void givenBoundaryAndNoValueConstructorShouldInitializeValueWithBoundary() {
        RangeSlider slider = new RangeSlider(new Range(0, 20));
        assertThat(slider.getBoundaries(), is(equalTo(new Range(0, 20))));
        assertThat(slider.getValue(), is(equalTo(new Range(0, 20))));
    }

    @Test
    public void givenBoundaryAndValueConstructorShouldNotOverrideValue() {
        RangeSlider slider = new RangeSlider(new Range(0, 20), new Range(5, 10));
        assertThat(slider.getBoundaries(), is(equalTo(new Range(0, 20))));
        assertThat(slider.getValue(), is(equalTo(new Range(5, 10))));
    }

    @Test
    public void givenValueNotWithinBoundaryConstructorShouldThrowException() {
        this.thrown.expect(IllegalArgumentException.class);
        new RangeSlider(new Range(0, 10), new Range(5, 15));
    }

    @Test
    public void givenValidValueSetValueShouldUpdateValue() {
        RangeSlider slider = new RangeSlider(new Range(0, 20));
        slider.setValue(new Range(5, 10));
        assertThat(slider.getValue(), is(equalTo(new Range(5, 10))));
    }

    @Test
    public void givenValidValueSetValueShouldRaiseEvent() {
        RangeSlider slider = new RangeSlider(new Range(0, 20));
        ChangedEvent event = new ChangedEvent(slider);

        slider.setValue(new Range(5, 6));
        assertTrue(event.wasFired());
    }

    @Test
    public void givenReadOnlyFieldUserOriginatedSetValueShouldDoNothing() {
        RangeSlider slider = new RangeSlider(new Range(0, 20));
        ChangedEvent event = new ChangedEvent(slider);

        slider.setReadOnly(true);
        slider.setValue(new Range(5, 6), true);
        assertFalse(event.wasFired());
        assertThat(slider.getValue(), is(equalTo(new Range(0, 20))));
    }

    @Test
    public void givenValueOutOfBoundsSetValueShouldRaiseException() {
        RangeSlider slider = new RangeSlider(new Range(0, 20));
        this.thrown.expect(IllegalArgumentException.class);
        slider.setValue(new Range(-5, -3));
    }

    @Test
    public void givenOldValueSetValueShouldNotFireEvent() {
        RangeSlider slider = new RangeSlider(new Range(5, 10));
        ChangedEvent event = new ChangedEvent(slider);

        slider.setValue(new Range(5, 10));
        assertFalse(event.wasFired());
    }

    @Test
    public void givenNewBoundariesSetBoundariesShouldUpdateTheValueIfNecessary() {
        RangeSlider slider = new RangeSlider(new Range(0, 10), new Range(4, 7));
        ChangedEvent event = new ChangedEvent(slider);

        slider.setBoundaries(new Range(5, 10));
        assertThat(slider.getValue(), is(equalTo(new Range(5, 7))));
        assertTrue(event.wasFired());
        event.reset();

        slider.setBoundaries(new Range(5, 6));
        assertThat(slider.getValue(), is(equalTo(new Range(5, 6))));
        assertTrue(event.wasFired());
    }

    @Test
    public void givenSmallerDifferenceThanCurrentValuesSetMinimalDifferenceShouldNotChangeValues() {
        RangeSlider slider = new RangeSlider(new Range(0, 20), new Range(0, 5));
        slider.setMinimumDifference(3);
        assertThat(slider.getMinimumDifference(), is(equalTo(3)));
        assertThat(slider.getValue(), is(equalTo(new Range(0, 5))));
    }

    @Test
    public void givenNegativeDifferenceSetMinimalDifferenceShouldThrowException() {
        RangeSlider slider = new RangeSlider(new Range(0, 20), new Range(0, 5));

        this.thrown.expect(IllegalArgumentException.class);
        slider.setMinimumDifference(-5);
    }

    @Test
    public void givenDifferenceLargerThanBoundariesSetMinimalDifferenceShouldThrowException() {
        RangeSlider slider = new RangeSlider(new Range(0, 5));

        this.thrown.expect(IllegalArgumentException.class);
        slider.setMinimumDifference(10);
    }

    @Test
    public void givenLargerDifferenceThanCurrentValuesSetMaximalDifferenceShouldNotChangeValues() {
        RangeSlider slider = new RangeSlider(new Range(0, 20), new Range(0, 5));
        slider.setMaximumDifference(6);
        assertThat(slider.getMaximumDifference(), is(equalTo(6)));
        assertThat(slider.getValue(), is(equalTo(new Range(0, 5))));
    }

    @Test
    public void givenNegativeDifferenceSetMaximumDifferenceShouldThrowException() {
        RangeSlider slider = new RangeSlider(new Range(0, 20), new Range(0, 5));

        this.thrown.expect(IllegalArgumentException.class);
        slider.setMaximumDifference(-5);
    }

    @Test
    public void givenDifferenceLargerThanBoundariesSetMaximalDifferenceShouldThrowException() {
        RangeSlider slider = new RangeSlider(new Range(0, 5));

        this.thrown.expect(IllegalArgumentException.class);
        slider.setMaximumDifference(10);
    }


    private void checkMinimalDifference(Range boundary, Range start, int difference, Range expected) {
        checkValue(boundary, start, slider -> slider.setMinimumDifference(difference), expected);
    }

    private void checkMaximalDifference(Range boundary, Range start, int difference, Range expected) {
        checkValue(boundary, start, slider -> slider.setMaximumDifference(difference), expected);
    }

    private void checkValue(Range boundary, Range start, Consumer<RangeSlider> change, Range expected) {
        RangeSlider slider = new RangeSlider(boundary, start);
        change.accept(slider);
        assertThat(slider.getValue(), is(equalTo(expected)));
    }

    @Test
    public void givenNotDivisableStepSetStepWillThrowException() {
        RangeSlider slider = new RangeSlider(new Range(0, 20), new Range(3, 5));
        slider.setMinimumDifference(3);
        slider.setMaximumDifference(5);

        this.thrown.expect(IllegalArgumentException.class);
        slider.setStep(2);
    }


    static class ChangedEvent implements HasValue.ValueChangeListener<Range> {

        private boolean changed = false;

        ChangedEvent(RangeSlider slider) {
            slider.addValueChangeListener(this);
        }

        @Override
        public void valueChange(HasValue.ValueChangeEvent<Range> event) {
            changed = true;
        }

        boolean wasFired() {
            return this.changed;
        }

        void reset() {
            this.changed = false;
        }
    }

}
