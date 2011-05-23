package org.blackpanther.ecosystem.gui.settings.fields.mutable;

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
public abstract class MutableSettingField<T>
        extends SettingField<T>
        implements Mutable {

    private JCheckBox mutable;

    public MutableSettingField(String name) {
        super(name);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        mutable = new JCheckBox();

        mutable.addActionListener(EventHandler.create(
                ActionListener.class,
                mutable,
                "setSelected",
                "source.selected"
        ));
    }

    @Override
    protected void placeComponents(JPanel layout) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(0,5,0,5);
        constraints.ipadx = 115;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        layout.add(createLabeledField(
                getMainComponent().getName(),
                getMainComponent(),
                FIELD_DIMENSION
        ), constraints);

        constraints.insets = new Insets(0,0,0,0);
        constraints.ipadx = 20;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        layout.add(createLabeledField(
                "M",
                mutable,
                CHECKBOX_DIMENSION
        ), constraints);
    }

    public boolean isMutable() {
        return mutable.isSelected();
    }
}
