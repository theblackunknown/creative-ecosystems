package org.blackpanther.ecosystem.gui.settings.fields;

import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class DoubleSpinnerField
        extends SettingField<Double> {

    private JSpinner valueSelector;

    public DoubleSpinnerField(String name, SpinnerModel model){
        super(name);
        valueSelector.setModel(model);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        valueSelector = new JSpinner();
        valueSelector.setName(fieldName);
    }

    @Override
    public JComponent getMainComponent() {
        return valueSelector;
    }

    @Override
    public void setValue(Double newValue) {
        valueSelector.setValue(newValue);
    }

    @Override
    public Double getValue() {
        return (Double) valueSelector.getValue();
    }

    @Override
    public FieldMould<Double> toMould() {
        return new StateFieldMould<Double>(
                getMainComponent().getName(),
                new org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider(getValue())
        );
    }
}
