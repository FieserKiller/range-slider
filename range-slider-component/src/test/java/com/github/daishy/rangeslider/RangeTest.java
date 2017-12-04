package com.github.daishy.rangeslider;

import com.github.daishy.rangeslider.client.Range;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author smca@viessmann.com
 */
public class RangeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void defaultConstructorShouldInitializeWithZero() {
        Range range = new Range();
        assertThat(range.getLower(), is(equalTo(0)));
        assertThat(range.getUpper(), is(equalTo(0)));
    }

    @Test
    public void givenValidUpperAndLowerConstructorShouldWork() {
        Range range = new Range(0, 10);
        assertThat(range.getUpper(), is(equalTo(10)));
        assertThat(range.getLower(), is(equalTo(0)));
    }

    @Test
    public void givenInvalidBoundsConstructorShouldThrowException() {
        this.thrown.expect(IllegalArgumentException.class);
        new Range(10, 5);
    }

    @Test
    public void givenEqualBoundsConstructorShouldWork() {
        new Range(1, 1);
    }

    @Test
    public void differenceShouldBeCalculatedCorrectly() {
        assertThat(new Range(0, 1).getDifference(), is(equalTo(1)));
        assertThat(new Range(1, 11).getDifference(), is(equalTo(10)));
        assertThat(new Range(-5, 5).getDifference(), is(equalTo(10)));
    }

    @Test
    public void givenTwoRangesEqualsShouldReturnCorrectResult() {
        assertTrue(new Range(0, 10).equals(new Range(0, 10)));
        assertFalse(new Range(0, 5).equals(new Range(0, 10)));
    }

    @Test
    public void givenValueGreaterThanUpperSetLowerShouldThrowException() {
        Range range = new Range(5, 10);
        this.thrown.expect(IllegalArgumentException.class);
        range.setLower(20);
    }

    @Test
    public void givenValueEqualToThanUpperSetLowerShouldThrowException() {
        Range range = new Range(5, 10);
        range.setLower(10);
        assertThat(range.getLower(), is(equalTo(10)));
    }

    @Test
    public void givenValueLessThanUpperSetLowerShouldSetValue() {
        Range range = new Range(5, 10);
        range.setLower(8);
        assertThat(range.getLower(), is(equalTo(8)));
    }

    @Test
    public void givenValueLessThanLowerSetUpperShouldThrowException() {
        Range range = new Range(5, 10);
        this.thrown.expect(IllegalArgumentException.class);
        range.setUpper(3);
    }

    @Test
    public void givenValueEqualToUpperSetUpperSetValue() {
        Range range = new Range(5, 10);
        range.setUpper(5);
        assertThat(range.getUpper(), is(equalTo(5)));
    }

    @Test
    public void givenValueLessThanUpperSetUpperShouldSetValue() {
        Range range = new Range(5, 10);
        range.setUpper(8);
        assertThat(range.getUpper(), is(equalTo(8)));
    }

    @Test
    public void containsShouldWorkAsExpected() {
        assertTrue(new Range(-2, 8).contains(new Range(5, 6)));
        assertTrue(new Range(-2, 8).contains(new Range(-2, 8)));

        assertFalse(new Range(-2, 8).contains(new Range(-5, 5)));
        assertFalse(new Range(-2, 8).contains(new Range(5, 15)));
        assertFalse(new Range(-2, 8).contains(new Range(-5, 15)));
    }

}
