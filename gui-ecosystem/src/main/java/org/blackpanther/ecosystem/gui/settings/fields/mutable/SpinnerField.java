package org.blackpanther.ecosystem.gui.settings.fields.mutable;

import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.GeneFieldMould;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class SpinnerField
        extends org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField
        implements Mutable {

    private JCheckBox mutable;

    public SpinnerField(String name, SpinnerModel model) {
        super(name, model);
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

    @Override
    public void setMutable(boolean mutable) {
        this.mutable.setSelected(mutable);
    }

    @Override
    public FieldMould<Double> toMould() {
        SpinnerNumberModel model = (SpinnerNumberModel) valueSelector.getModel();
        Double min = (Double) model.getMinimum();
        Double max = (Double) model.getMaximum();
        return new GeneFieldMould<Double>(
                getMainComponent().getName(),
                isRandomized()
                        ? new org.blackpanther.ecosystem.factory.generator.random.DoubleProvider(min, max)
                        : new org.blackpanther.ecosystem.factory.generator.provided.DoubleProvider(getValue()),
                isMutable()
        );
    }
}
