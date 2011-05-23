package org.blackpanther.ecosystem.gui.settings.fields;

import org.blackpanther.ecosystem.factory.fields.FieldMould;

import javax.swing.*;
import java.awt.*;

import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public abstract class SettingField<T>
        extends JPanel {
    public static final Dimension CHECKBOX_DIMENSION = new Dimension(20, 20);


    protected SettingField(String name) {
        super();
        initializeComponents(name);
        setLayout(new GridBagLayout());
        placeComponents(this);
    }

    abstract protected void initializeComponents(String fieldName);

    protected void placeComponents(JPanel layout) {

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridheight = GridBagConstraints.REMAINDER;

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.ipadx = 60;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        layout.add(createLabeledField(
                getMainComponent().getName(),
                getMainComponent()
        ), constraints);
    }

    abstract protected JComponent getMainComponent();

    abstract public void setValue(T newValue);

    abstract public T getValue();

    abstract public FieldMould<T> toMould();
}
