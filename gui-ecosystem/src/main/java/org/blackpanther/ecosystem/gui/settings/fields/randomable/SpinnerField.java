package org.blackpanther.ecosystem.gui.settings.fields.randomable;

import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;
import org.blackpanther.ecosystem.factory.generator.ValueProvider;
import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;

import static org.blackpanther.ecosystem.ApplicationConstants.*;
import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.agent.CreatureConstants.*;
import static org.blackpanther.ecosystem.agent.ResourceConstants.RESOURCE_ENERGY;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class SpinnerField
        extends RandomSettingField<Double> {

    protected JSpinner valueSelector;

    public SpinnerField(String name, SpinnerModel model) {
        super(name);
        valueSelector.setModel(model);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        super.initializeComponents(fieldName);
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
        if (!isRandomized())
            return (Double) valueSelector.getValue();
        else {
            SpinnerNumberModel model = (SpinnerNumberModel) valueSelector.getModel();
            Double min = (Double) model.getMinimum();
            Double max = (Double) model.getMaximum();
            return min
                    + Configuration.getRandom().nextDouble() * (max - min);
        }
    }

    @Override
    public FieldMould<Double> toMould() {
        return new StateFieldMould<Double>(
                valueSelector.getName(),
                generateValueProvider()
        );
    }

    private ValueProvider<Double> generateValueProvider() {
        if (!isRandomized())
            return new org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider((Double) valueSelector.getValue());
        else if (valueSelector.getName().equals(CREATURE_ORIENTATION_LAUNCHER)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(-Math.PI, Math.PI);
        } else if (valueSelector.getName().equals(CREATURE_ORIENTATION)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(0.0, Geometry.PI_2);
        } else if (valueSelector.getName().equals(CREATURE_SPEED)
                || valueSelector.getName().equals(CREATURE_SPEED_LAUNCHER)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(0.0,
                    Configuration.getParameter(SPEED_THRESHOLD, Double.class));
        } else if (valueSelector.getName().equals(CREATURE_ENERGY)
                || valueSelector.getName().equals(RESOURCE_ENERGY)
                || valueSelector.getName().equals(CREATURE_FECUNDATION_COST)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(0.0,
                    Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class));
        } else if (valueSelector.getName().equals(CREATURE_SENSOR_RADIUS)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(0.0,
                    Configuration.getParameter(SENSOR_THRESHOLD, Double.class));
        } else if (valueSelector.getName().equals(CREATURE_SENSOR_RADIUS)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(0.0,
                    Configuration.getParameter(SENSOR_THRESHOLD, Double.class));
        } else if (valueSelector.getName().equals(CREATURE_CURVATURE)) {
            return new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(
                    -Configuration.getParameter(CURVATURE_THRESHOLD, Double.class),
                    Configuration.getParameter(CURVATURE_THRESHOLD, Double.class));
        } else
            throw new IllegalStateException("unknown properties : " + valueSelector.getName());
    }
}
