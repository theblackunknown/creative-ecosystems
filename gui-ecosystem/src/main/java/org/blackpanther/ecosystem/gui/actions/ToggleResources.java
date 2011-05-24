package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.GraphicEnvironment;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author MACHIZAUD Andréa
 * @version 5/24/11
 */
public class ToggleResources
    extends AbstractAction{

    private ToggleResources(){
        super("Toggle Resources painting");
    }

    private static class ToggleResourcesHolder {
        private static final ToggleResources instance =
            new ToggleResources();
    }

    public static ToggleResources getInstance(){
        return ToggleResourcesHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUIMonitor.Monitor.toggleOption(GraphicEnvironment.RESOURCE_OPTION);
    }
}
