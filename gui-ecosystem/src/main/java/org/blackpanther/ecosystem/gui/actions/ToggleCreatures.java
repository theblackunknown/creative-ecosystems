package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.GraphicEnvironment;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class ToggleCreatures
    extends AbstractAction{

    private ToggleCreatures(){
        super("Toggle Creatures painting when stopped");
    }

    private static class ToggleCreaturesHolder {
        private static final ToggleCreatures instance =
            new ToggleCreatures();
    }

    public static ToggleCreatures getInstance(){
        return ToggleCreaturesHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUIMonitor.Monitor.toggleOption(GraphicEnvironment.CREATURE_OPTION);
    }
}
