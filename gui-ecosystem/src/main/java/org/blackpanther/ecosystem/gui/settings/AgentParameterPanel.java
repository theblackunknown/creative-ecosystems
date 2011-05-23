package org.blackpanther.ecosystem.gui.settings;

import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.gui.settings.fields.SettingField;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public abstract class AgentParameterPanel
        extends ParameterPanel {

    protected AgentParameterPanel(String name) {
        super(name);
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
        for(Map.Entry<String,SettingField> entry : parameters.entrySet())
            entry.getValue().setValue(
                    information.getValue(entry.getKey()));
    }

    public Collection<FieldMould> getMoulds() {
        Collection<FieldMould> moulds = new ArrayList<FieldMould>(parameters.size());
        for(SettingField field : parameters.values())
            moulds.add(field.toMould());
        return moulds;
    }
}
