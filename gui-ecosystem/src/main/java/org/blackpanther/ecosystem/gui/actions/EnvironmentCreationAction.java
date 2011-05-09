package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

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
        Monitor.stopEvolution();
        logger.info("Environment creation action");
    }
}
