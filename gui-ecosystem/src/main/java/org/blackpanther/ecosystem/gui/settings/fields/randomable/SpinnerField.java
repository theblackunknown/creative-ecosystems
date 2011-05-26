package org.blackpanther.ecosystem.gui.settings.fields.randomable;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class SpinnerField
        extends RandomSettingField<Double> {

    private JSpinner valueSelector;
    protected double min;
    protected double max;

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
