package org.blackpanther.ecosystem.gui.settings.fields.randomable;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;
import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class SpinnerField
        extends RandomSettingField<Double> {

    private JSpinner valueSelector;
    private double min;
    private double max;

    public SpinnerField(String name, SpinnerModel model, double min, double max) {
        super(name);
        this.min = min;
        this.max = max;
        valueSelector.setModel(model);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        super.initializeComponents(fieldName);
        valueSelector = new JSpinner();
        valueSelector.setName(fieldName);
    }

    @Override
    protected JComponent getMainComponent() {
        return valueSelector;
    }

    @Override
    public void setValue(Double newValue) {
        valueSelector.setValue(newValue);
    }

    @Override
    public Double getValue() {
        if (!isRandomized())
            return (Double) valueSelector.getValue();
        else
            return min
                    + Configuration.Configuration.getRandom().nextDouble() * (max - min);
    }

    @Override
    public FieldMould<Double> toMould() {
        return new StateFieldMould<Double>(
                valueSelector.getName(),
                isRandomized()
                        ? new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(min,max)
                        : new org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider((Double) valueSelector.getValue())
        );
    }
}