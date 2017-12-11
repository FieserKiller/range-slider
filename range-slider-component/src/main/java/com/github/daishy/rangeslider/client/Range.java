package com.github.daishy.rangeslider.client;

import com.github.daishy.rangeslider.RangeSlider;

import java.io.Serializable;

/**
 * Represents the value and boundaries for the {@link RangeSlider}. Two ranges are equal, if both the
 * upper and lower value match.
 *
 * @author daishy@github.com
 */
public class Range implements Serializable {

    private static final long serialVersionUID = -1490025607687396331L;

    private int lower;
    private int upper;

    /**
     * Create a new range. The lower value must be less or equal to the upper value.
     *
     * @param lower The lower value.
     * @param upper The upper value.
     */
    public Range(int lower, int upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Lower value must be less/equal of upper value");
        }
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Create a new range with the lower and upper value <code>0</code>.
     */
    public Range() {
        this(0, 0);
    }

    public int getLower() {
        return lower;
    }


    /**
     * Set the lower value. Must be less or equal to the current upper value.
     *
     * @param lower -
     */
    public void setLower(int lower) {
        if (lower > this.upper) {
            throw new IllegalArgumentException("Given lower value is more than the current upper value");
        }
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }

    /**
     * Set the upper value of this range. Must be greater or equal than the current lower value.
     *
     * @param upper -
     */
    public void setUpper(int upper) {
        if (upper < this.lower) {
            throw new IllegalArgumentException("Given upper value is less than the current lower value");
        }
        this.upper = upper;
    }

    /**
     * This will check if the given range lies completely within this range.
     *
     * @param range -
     * @return -
     */
    public boolean contains(Range range) {
        return this.lower <= range.lower && this.upper >= range.upper;
    }

    /**
     * Returns the difference between the upper and lower value.
     *
     * @return -
     */
    public int getDifference() {
        return this.upper - this.lower;
    }

    @Override
    public String toString() {
        return "Range(" + lower + "-" + upper + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        if (lower != range.lower) return false;
        return upper == range.upper;
    }

    @Override
    public int hashCode() {
        int result = lower;
        result = 31 * result + upper;
        return result;
    }
}
