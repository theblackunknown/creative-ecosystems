package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/9/11
 */
public class EnvironmentCreationAction
extends AbstractAction{

    private EnvironmentCreationAction(){
        super("Create a new environment");
    }

    private static class EnvironmentCreationActionHolder {
        private static final EnvironmentCreationAction instance =
            new EnvironmentCreationAction();
    }

    public static EnvironmentCreationAction getInstance(){
        return EnvironmentCreationActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.stopEvolution();
    }
}
