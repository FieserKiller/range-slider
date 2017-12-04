package com.github.daishy.rangeslider.client;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * The state-object for the {@link RangeSliderState}.
 *
 * @author daishy@github.com
 */
public class RangeSliderState extends JavaScriptComponentState {
    /**
     * What are the min- and max-values for the slider?
     */
    public Range boundaries = new Range(0, 0);

    /**
     * The step-size each side of the slider takes
     */
    public Integer step = 1;

    /**
     * The minimum distance between the lower and upper value
     */
    public Integer minimumDifference = null;

    /**
     * The maximum difference between the lower and upper value
     */
    public Integer maximumDifference = null;

    /**
     * Is the slider readonly for the user?
     */
    public boolean readonly = false;

    /**
     * Should the slider show the current values?
     */
    public enum Tooltips {
        ALWAYS, ON_CHANGE, NEVER
    }

    public Tooltips tooltips = Tooltips.ALWAYS;
}

