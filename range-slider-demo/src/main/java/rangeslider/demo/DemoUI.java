package rangeslider.demo;

import com.github.daishy.rangeslider.RangeConverter;
import com.github.daishy.rangeslider.RangeSlider;
import com.github.daishy.rangeslider.client.Range;
import com.github.daishy.rangeslider.client.RangeSliderState;
import com.vaadin.annotations.Title;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import java.util.EnumSet;


@Title("Test Application")
@SpringUI
public class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        FormLayout layout = new FormLayout();

        RangeSlider rangeSlider = new RangeSlider("RangeSlider", new Range(0, 20));
        rangeSlider.setWidth("50%");//setSizeFull();
        layout.addComponent(rangeSlider);

        Slider slider = new Slider("Vaadin-Slider", 0, 10);
        layout.addComponent(slider);

        TextField value = new TextField("Value");
        //value.setReadOnly(true);
        layout.addComponent(value);

        TextField boundaries = new TextField("Boundaries");
        layout.addComponent(boundaries);

        Button changeValue = new Button("Set to [4, 6]", event -> {
            rangeSlider.setValue(new Range(4, 6));
        });
        layout.addComponent(changeValue);

        TextField step = new TextField("step");
        layout.addComponent(step);

        TextField minimumDifference = new TextField("miniumDifference");
        layout.addComponent(minimumDifference);

        TextField maximumDifference = new TextField("maximumDifference");
        layout.addComponent(maximumDifference);

        CheckBox readonly = new CheckBox("Readonly");
        layout.addComponent(readonly);

        ComboBox<RangeSliderState.Tooltips> tooltips = new ComboBox<>("Tooltips", EnumSet.allOf(RangeSliderState.Tooltips.class));
        layout.addComponent(tooltips);


        Binder<RangeSlider> binder = new Binder<RangeSlider>();
        binder.setBean(rangeSlider);

        binder.forField(value)
                .withConverter(new RangeConverter())
                .bind(RangeSlider::getValue, RangeSlider::setValue);
        binder.forField(boundaries)
                .withConverter(new RangeConverter())
                .bind(RangeSlider::getBoundaries, RangeSlider::setBoundaries);
        binder.forField(step)
                .withConverter(new StringToIntegerConverter("No valid integer"))
                .bind(RangeSlider::getStep, RangeSlider::setStep);
        binder.forField(readonly)
                .bind(RangeSlider::isReadOnly, RangeSlider::setReadOnly);
        binder.forField(tooltips)
                .bind(RangeSlider::getTooltips, RangeSlider::setTooltips);
        binder.forField(minimumDifference)
                .withConverter(new OptionalIntegerConverter())
                .bind(RangeSlider::getMinimumDifference, RangeSlider::setMinimumDifference);
        binder.forField(maximumDifference)
                .withConverter(new OptionalIntegerConverter())
                .bind(RangeSlider::getMaximumDifference, RangeSlider::setMaximumDifference);

        Runnable update = () -> value.setValue(rangeSlider.getValue().getLower() + " - " + rangeSlider.getValue().getUpper());
        rangeSlider.addValueChangeListener(event -> update.run());
        update.run();

        setContent(layout);
    }

    private static class OptionalIntegerConverter implements Converter<String, Integer> {

        @Override
        public Result<Integer> convertToModel(String value, ValueContext context) {
            if (value != null && !value.isEmpty()) {
                try {
                    return Result.ok(Integer.valueOf(value));
                } catch (NumberFormatException e) {
                    return Result.error("Not a number");
                }
            }
            return Result.ok(null);
        }

        @Override
        public String convertToPresentation(Integer value, ValueContext context) {
            return value != null ? value.toString() : "";
        }
    }
}
