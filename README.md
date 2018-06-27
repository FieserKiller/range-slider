# TimeRangeSlider Add-on for Vaadin 8

This is a modified version of Daishys RangeSlider https://github.com/Daishy/range-slider
I changed tooltip formatting from integer to HH:MM, so you can display timeranges nicely. 

An interval of 0..1440 (A day has 1440 minutes...) will result in tooltips showing 00:00..24:00.

See screenshot if you don't get it yet :)

Big props go to Daishy et al..


RangeSlider is a UI component add-on for Vaadin 8 that currently displays a
slider with a lower and upper handle, to select a range of values. The Slider is based on Vaadins HasValue 
and can be used with a Binder.

This addon is based massivly on the great noUiSlider-javascript-component, see
https://refreshless.com/nouislider/ or https://github.com/leongersen/noUiSlider/ 
for more infos. 

## Features
* changing the step size between values
* changing the minimal and maximal difference allowed between the lower and upper value
* Value-Tooltips can be displayed never, always, on-focus or on-change.
* slider can be read-only.
* slider integrates in the vaadin-8 binder

## Known Limitations
* Currently only integer-ranges are supported.
* Styling adapted to valo, other themes may require some tuning. See the range-slider.css for more infos. 

## Building and running demo

```bash
git clone git://github.com/FieserKiller/range-slider.git
mvn clean install
cd range-slider-demo
mvn spring-boot:run
```
To see the demo, navigate to http://localhost:8080/

 
## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see `LICENSE`.

## Usage and Examples

Create a new field as you would any other vaadin-field-component via the provided constructors.


Binding field via a binder:
```java
RangeSlider slider = new RangeSlider("time-range", new Range(0, 1440));
Binder<> binder = new Binder<Data>();
binder.forField(slider).bind(Data::getTimeRange, Data::setTimeRange);
```

Setting a minimal and maximal difference between the values:
```java
RangeSlider slider = new RangeSlider("time-range", new Range(0, 1440));
slider.setMinimalDifference(15); // values must be at least 15 minutes apart
slider.setMaximalDifference(300); // but no more than 300 minutesapart
```

Set the stepsize that are allowed
```java
RangeSlider slider = new RangeSlider("time-range", new Range(0, 1440));
slider.setStep(15); // 15 minutes steps 
```

Show Tooltips for the selected values:
```java
RangeSlider slider = new RangeSlider("time-range", new Range(0, 1440));
slider.setTooltips(RangeSliderState.Tooltips.NEVER); // Never showing the selected values
slider.setTooltips(RangeSliderState.Tooltips.ON_CHANGE); // Show selected values if slider is moved
slider.setTooltips(RangeSliderState.Tooltips.ON_CHANGE); // Always show the selected values
```

