package org.blackpanther.ecosystem.gui.settings.fields;

import org.blackpanther.ecosystem.factory.fields.FieldMould;

import javax.swing.*;
import java.awt.*;

import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public abstract class SettingField<T>
        extends JPanel {
    public static final Dimension CHECKBOX_DIMENSION = new Dimension(25, 50);
    public static final Dimension FIELD_DIMENSION = new Dimension(60, 50);


    protected SettingField(String name) {
        super();
        initializeComponents(name);
        setLayout(new GridBagLayout());
        placeComponents(this);
    }

    abstract protected void initializeComponents(String fieldName);

    protected void placeComponents(JPanel layout) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.ipadx = 20;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        layout.add(createLabeledField(
                getMainComponent().getName(),
                getMainComponent()
        ), constraints);
    }

    public abstract JComponent getMainComponent();

    abstract public void setValue(T newValue);

    abstract public T getValue();

    abstract public FieldMould<T> toMould();
}
