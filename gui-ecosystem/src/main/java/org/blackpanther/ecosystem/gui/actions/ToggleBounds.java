package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.GraphicEnvironment;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class ToggleBounds
    extends AbstractAction{

    private ToggleBounds() {
        super("Toggle Bounds painting");
    }

    private static class ToggleBoundsHolder {
        private static final ToggleBounds instance =
            new ToggleBounds();
    }

    public static ToggleBounds getInstance(){
        return ToggleBoundsHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUIMonitor.Monitor.toggleOption(GraphicEnvironment.BOUNDS_OPTION);
    }
}
