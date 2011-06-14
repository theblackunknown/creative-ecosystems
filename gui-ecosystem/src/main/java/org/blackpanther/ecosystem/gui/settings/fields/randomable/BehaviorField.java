package org.blackpanther.ecosystem.gui.settings.fields.randomable;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
 */
public class BehaviorField
        extends RandomSettingField<BehaviorManager> {

    private JComboBox behaviorSelector;

    public BehaviorField(String name, BehaviorManager[] behaviors) {
        super(name);
        for (BehaviorManager behavior : behaviors)
            behaviorSelector.addItem(behavior);
    }

    @Override
    protected void initializeComponents(String fieldName) {
        super.initializeComponents(fieldName);
        behaviorSelector = new JComboBox();
        behaviorSelector.setName(fieldName);
    }

    @Override
    public JComponent getMainComponent() {
        return behaviorSelector;
    }

    @Override
    public void setValue(BehaviorManager newValue) {
        behaviorSelector.setSelectedItem(newValue);
    }

    @Override
    public BehaviorManager getValue() {
        if (!isRandomized())
            return (BehaviorManager) behaviorSelector.getSelectedItem();
        else
            return (BehaviorManager) behaviorSelector.getModel().getElementAt(
                    Configuration.Configuration.getRandom().nextInt(
                            behaviorSelector.getModel().getSize()));
    }

    @Override
    public FieldMould<BehaviorManager> toMould() {
        return new StateFieldMould<BehaviorManager>(
                behaviorSelector.getName(),
                isRandomized()
                        ? org.blackpanther.ecosystem.factory.generator.random.BehaviorProvider.getInstance()
                        : new org.blackpanther.ecosystem.factory.generator.provided.BehaviorProvider(getValue())
        );
    }
}
