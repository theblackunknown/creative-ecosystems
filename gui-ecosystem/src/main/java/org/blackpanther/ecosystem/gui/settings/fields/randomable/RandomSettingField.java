package org.blackpanther.ecosystem.gui.settings.fields.randomable;

import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.gui.settings.fields.SettingField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public abstract class RandomSettingField<T>
        extends SettingField<T>
        implements Randomable {

    private JCheckBox randomized;

    protected RandomSettingField(String name) {
        super(name);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        randomized = new JCheckBox();
        randomized.addActionListener(EventHandler.create(
                ActionListener.class,
                randomized,
                "setSelected",
                "source.selected"
        ));
    }

    @Override
    protected void placeComponents(JPanel layout) {
        super.placeComponents(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridheight = GridBagConstraints.REMAINDER;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.ipadx = 0;
        constraints.fill = GridBagConstraints.NONE;
        layout.add(createLabeledField(
                "R",
                randomized,
                CHECKBOX_DIMENSION
        ), constraints);
    }

    @Override
    public boolean isRandomized() {
        return randomized.isSelected();
    }
}
