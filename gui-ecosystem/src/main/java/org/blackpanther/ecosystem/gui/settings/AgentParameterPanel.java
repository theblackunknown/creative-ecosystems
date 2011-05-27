package org.blackpanther.ecosystem.gui.settings;

import org.blackpanther.ecosystem.agent.ResourceConstants;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.gui.settings.fields.SettingField;
import org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.blackpanther.ecosystem.ApplicationConstants.*;
import static org.blackpanther.ecosystem.agent.CreatureConstants.*;
import static org.blackpanther.ecosystem.agent.ResourceConstants.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public abstract class AgentParameterPanel
        extends ParameterPanel {

    protected AgentParameterPanel(String name) {
        super(name);
    }

    public void updateFieldRange(String traitName, Double thresholdValue) {
        if (traitName.equals(CURVATURE_THRESHOLD)) {
            updateSpinnerField(CREATURE_CURVATURE, -thresholdValue, thresholdValue);
        } else if (traitName.equals(SPEED_THRESHOLD)) {
            updateSpinnerField(CREATURE_SPEED, 0.0, thresholdValue);
            updateSpinnerField(CREATURE_SPEED_LAUNCHER, 0.0, thresholdValue);
        } else if (traitName.equals(SENSOR_THRESHOLD)) {
            updateSpinnerField(CREATURE_SENSOR_RADIUS, 0.0, thresholdValue);
        } else if (traitName.equals(ENERGY_AMOUNT_THRESHOLD)) {
            updateSpinnerField(RESOURCE_ENERGY, 0.0, thresholdValue);
            updateSpinnerField(CREATURE_ENERGY, 0.0, thresholdValue);
            updateSpinnerField(CREATURE_FECUNDATION_COST, 0.0, thresholdValue);
        } else
            throw new RuntimeException("Unknown threshold : " + traitName);
    }

    private void updateSpinnerField(String fieldName, Double min, Double max) {
        SpinnerField spinner = (SpinnerField) parameters.get(fieldName);
        if (spinner != null)
            spinner.updateRange(min, max);
    }

    @Override
    protected void generateContent(Box layout) {
        Box state = Box.createVerticalBox();
        Box genotype = Box.createVerticalBox();

        state.setBorder(BorderFactory.createTitledBorder("State"));
        genotype.setBorder(BorderFactory.createTitledBorder("Genotype"));

        fillUpState(state);
        fillUpGenotype(genotype);

        if (state.getComponentCount() > 0)
            layout.add(state);
        if (genotype.getComponentCount() > 0)
            layout.add(genotype);
    }

    abstract void fillUpState(Box layout);

    abstract void fillUpGenotype(Box layout);

    @SuppressWarnings("unchecked")
    public void updateInformation(FieldsConfiguration information) {
        for (Map.Entry<String, SettingField> entry : parameters.entrySet())
            entry.getValue().setValue(
                    information.getValue(entry.getKey()));
    }

    public Collection<FieldMould> getMoulds() {
        Collection<FieldMould> moulds = new ArrayList<FieldMould>(parameters.size());
        for (SettingField field : parameters.values())
            moulds.add(field.toMould());
        return moulds;
    }
}
