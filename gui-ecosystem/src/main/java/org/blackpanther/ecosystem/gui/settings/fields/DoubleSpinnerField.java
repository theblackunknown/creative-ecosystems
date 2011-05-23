package org.blackpanther.ecosystem.gui.settings.fields;

import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
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
    protected JComponent getMainComponent() {
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
                valueSelector.getName(),
                new org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider((Double) valueSelector.getValue())
        );

    }
}
