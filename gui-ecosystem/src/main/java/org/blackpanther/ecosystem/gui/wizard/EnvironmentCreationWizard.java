package org.blackpanther.ecosystem.gui.wizard;

import com.nexes.wizard.Wizard;
import org.blackpanther.ecosystem.gui.WorldFrame;

/**
 * @author MACHIZAUD Andréa
 * @version 5/10/11
 */
public class EnvironmentCreationWizard
        extends Wizard {

    private EnvironmentCreationWizard() {
        super(WorldFrame.getInstance(), new CreationModel());
        registerWizardPanel(
                InitialParameterPanelDescriptor.IDENTIFIER,
                new InitialParameterPanelDescriptor());
        registerWizardPanel(
                PlacementStrategyPanelDescriptor.IDENTIFIER,
                new PlacementStrategyPanelDescriptor());
    }

    private static class EnvironmentCreationWizardHolder {
        private static final EnvironmentCreationWizard instance =
                new EnvironmentCreationWizard();
    }

    public static EnvironmentCreationWizard getInstance() {
        return EnvironmentCreationWizardHolder.instance;
    }

    @Override
    public CreationModel getModel() {
        return (CreationModel) super.getModel();
    }

    @Override
    public int showModalDialog() {
        setCurrentPanel(InitialParameterPanelDescriptor.IDENTIFIER);
        return super.showModalDialog();
    }
}
