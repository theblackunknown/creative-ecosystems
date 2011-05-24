package org.blackpanther.ecosystem.gui.settings.fields.randomable;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class ColorField
        extends RandomSettingField<Color> {

    private JButton colorSelector;

    public ColorField(String name) {
        super(name);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        super.initializeComponents(fieldName);
        colorSelector = new JButton();
        colorSelector.setName(fieldName);
        colorSelector.addActionListener(EventHandler.create(
                ActionListener.class,
                this,
                "chooseColor"
        ));
    }

    @Override
    public FieldMould<Color> toMould() {
        return new StateFieldMould<Color>(
                colorSelector.getName(),
                isRandomized()
                        ? org.blackpanther.ecosystem.factory.generator.random.ColorProvider.getInstance()
                        : new org.blackpanther.ecosystem.factory.generator.provided.ColorProvider(colorSelector.getBackground())
        );
    }

    @Override
    protected JComponent getMainComponent() {
        return colorSelector;
    }

    @Override
    public void setValue(Color newValue) {
        colorSelector.setBackground(newValue);
    }

    @Override
    public Color getValue() {
        if (!isRandomized())
            return colorSelector.getBackground();
        else
            return new Color(
                    Configuration.Configuration.getRandom().nextFloat(),
                    Configuration.Configuration.getRandom().nextFloat(),
                    Configuration.Configuration.getRandom().nextFloat()
            );
    }

    public void chooseColor() {
        Color selectedColor = JColorChooser.showDialog(
                WorldFrame.getInstance(),
                "Choose agent identifier",
                colorSelector.getBackground());
        if (selectedColor != null)
            colorSelector.setBackground(selectedColor);
    }
}
