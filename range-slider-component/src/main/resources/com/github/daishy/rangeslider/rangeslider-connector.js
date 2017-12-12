window.com_github_daishy_rangeslider_RangeSlider = function () {
    var connector = this;

    // Create a container for the slider
    var container = document.createElement("div");
    container.className = "range-slider-container";
    this.getElement().appendChild(container);

    function arrayEqual(arr1, arr2) {
        if (arr1.length !== arr2.length)
            return false;
        for (var i = arr1.length; i--;) {
            if (arr1[i] !== arr2[i])
                return false;
        }

        return true;
    }

    // wrapper-function to create and update the slider.
    function createSlider(state) {
        var previousValue = null;
        var start = [state.lowerBoundary, state.upperBoundary];
        if (container.noUiSlider) {
            start = container.noUiSlider.get();
            previousValue = start;
            container.noUiSlider.destroy();
        }

        options = {
            'connect': true,
            'start': start,
            'range': {
                'min': state.lowerBoundary,
                'max': state.upperBoundary
            },
            'step': state.step,
            'format': {
                to: function (value) {
                    return value.toString();
                },
                from: function (value) {
                    return value;
                }
            }
        };

        if (state.minimumDifference !== undefined && state.minimumDifference !== null) {
            options['margin'] = state.minimumDifference;
        }
        if (state.maximumDifference !== undefined && state.maximumDifference !== null) {
            options['limit'] = state.maximumDifference;
        }
        if (state.tooltips === "ALWAYS" || state.tooltips === "ON_CHANGE" || state.tooltips === "ON_FOCUS") {
            options['tooltips'] = [true, true];
        }

        noUiSlider.create(container, options);

        // Mark the slider as readonly if requested
        if (state.readonly) {
            container.setAttribute("disabled", true);
        }
        else {
            container.removeAttribute("disabled");
        }

        // Notify the server in case the component changed
        container.noUiSlider.on('set', function (values) {
            console.debug("Value changed to ", values[0], values[1], container.noUiSlider.get());
            connector.valueChanged(values[0], values[1]);
        });

        // Change the tooltip-toggle-class based on the chosen state. By default the tooltip is hidden,
        // and only displayed if 'range-slider-show-tooltips'-class is added to the container.
        container.className = container.className.replace(" range-slider-show-tooltips", ""); // Always clear first
        // set up the event-listener for ON_FOCUS
        container.addEventListener("focus", function (event) {
            if (state.tooltips === "ON_FOCUS") {
                container.className += " range-slider-show-tooltips";
            }
        }, true);
        container.addEventListener("blur", function (event) {
            if (state.tooltips === "ON_FOCUS") {
                container.className = container.className.replace(" range-slider-show-tooltips", "");
            }
        }, true);
        if (state.tooltips === "ON_CHANGE") {
            container.noUiSlider.on('start', function () {
                container.className += " range-slider-show-tooltips";
            });
            container.noUiSlider.on('end', function () {
                container.className = container.className.replace(" range-slider-show-tooltips", "");
            });
        }
        else if (state.tooltips === "ALWAYS") {
            container.className += " range-slider-show-tooltips";
        }
        else if (state.tooltips === "NEVER") {
            // nothing to do here.
        }
        else if (state.tooltips === "ON_FOCUS") {
            // nothing to do here, the listener above will handle this case.
        }

        // check if we recreated the slider and an option changed the displayed value. If thats the case
        // notify the connector of the changed value
        if (previousValue != null && !arrayEqual(previousValue, container.noUiSlider.get())) {
            var values = container.noUiSlider.get();
            connector.valueChanged(values[0], values[1]);
        }
    }

    createSlider(this.getState());


    // Handle changes from the server-side
    this.onStateChange = function () {
        var state = this.getState();

        // currently we just drop the slider and recreate it, because some options require dropping the
        // entire slider and just recreating is currently the easier option. updateOptions below is the alternative
        // solution but does not support Tooltips. May change in the future to a more efficient solution.
        createSlider(state);

        // updateOptions can only update 'margin', 'limit',  'step', 'range', 'animate' and 'snap'
        // container.noUiSlider.updateOptions(options);
    };


    // Exposed function to set the components value from the server-side.
    connector.setValue = function (lower, upper) {
        console.debug("Changing value to ", lower, upper);
        container.noUiSlider.set([lower, upper]);
    }
}
;