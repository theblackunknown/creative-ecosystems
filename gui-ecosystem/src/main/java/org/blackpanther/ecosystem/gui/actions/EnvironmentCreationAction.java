package org.blackpanther.ecosystem.gui.actions;

import com.nexes.wizard.Wizard;
import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.wizard.EnvironmentCreationWizard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/9/11
 */
public class EnvironmentCreationAction
        extends AbstractAction {

    private static final Logger logger =
            Logger.getLogger(
                    EnvironmentCreationAction.class.getCanonicalName()
            );

    private EnvironmentCreationAction() {
        super("Create a new environment");
    }

    private static class EnvironmentCreationActionHolder {
        private static final EnvironmentCreationAction instance =
                new EnvironmentCreationAction();
    }

    public static EnvironmentCreationAction getInstance() {
        return EnvironmentCreationActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.pauseEvolution();
        switch (EnvironmentCreationWizard.getInstance().showModalDialog()) {
            case Wizard.FINISH_RETURN_CODE:
                Configuration.loadConfiguration(EnvironmentCreationWizard.getInstance()
                        .getModel().getEnvironmentProperties());
                Environment env = new DesignEnvironment();
                env.addAgent(EnvironmentCreationWizard.getInstance()
                        .getModel().getPool());
                Monitor.newEnvironment(env);
                break;
            case Wizard.CANCEL_RETURN_CODE:
                //Nothing to do
                break;
            case Wizard.ERROR_RETURN_CODE:
                throw new RuntimeException("Unexpected close of environment wizard creation");
        }
    }
}
