package org.blackpanther.ecosystem.gui.settings.fields.mutable;

import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class BehaviorField
        extends MutableSettingField<BehaviorManager> {

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
    protected JComponent getMainComponent() {
        return behaviorSelector;
    }

    @Override
    public void setValue(BehaviorManager newValue) {
        behaviorSelector.setSelectedItem(newValue);
    }

    @Override
    public BehaviorManager getValue() {
        return (BehaviorManager) behaviorSelector.getSelectedItem();
    }

    @Override
    public FieldMould<BehaviorManager> toMould() {
        return new StateFieldMould<BehaviorManager>(
                behaviorSelector.getName(),
                new org.blackpanther.ecosystem.factory.generator.provided.BehaviorProvider((BehaviorManager) behaviorSelector.getSelectedItem())
        );
    }
}
