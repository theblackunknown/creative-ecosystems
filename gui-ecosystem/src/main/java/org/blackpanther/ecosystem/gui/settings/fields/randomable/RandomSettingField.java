package org.blackpanther.ecosystem.gui.settings.fields.randomable;

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
                this,
                "setRandomized",
                "source.selected"
        ));
    }

    public void setRandomized(boolean isRandomized){
        randomized.setSelected(isRandomized);
        getMainComponent().setEnabled(!isRandomized);
    }

    @Override
    protected void placeComponents(JPanel layout) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.ipadx = 20;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        layout.add(createLabeledField(
                "R",
                randomized,
                CHECKBOX_DIMENSION
        ), constraints);

        constraints.insets = new Insets(0,5,0,5);
        constraints.ipadx = 60;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        layout.add(createLabeledField(
                getMainComponent().getName(),
                getMainComponent(),
                FIELD_DIMENSION
        ), constraints);
    }

    @Override
    public boolean isRandomized() {
        return randomized.isSelected();
    }
}
