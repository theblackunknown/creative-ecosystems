package org.blackpanther.ecosystem.gui.settings.fields.mutable;

import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class BehaviorField
        extends org.blackpanther.ecosystem.gui.settings.fields.randomable.BehaviorField
        implements Mutable {

    private JCheckBox mutable;

    public BehaviorField(String name, BehaviorManager[] behaviors) {
        super(name,behaviors);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        super.initializeComponents(fieldName);

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
        super.placeComponents(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        layout.add(createLabeledField(
                "M",
                mutable,
                CHECKBOX_DIMENSION
        ), constraints);
    }

    @Override
    public boolean isMutable() {
        return mutable.isSelected();
    }
}
