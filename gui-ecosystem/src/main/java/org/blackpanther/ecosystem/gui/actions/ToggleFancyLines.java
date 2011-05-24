package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.GraphicEnvironment;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class ToggleFancyLines
    extends AbstractAction{

    private ToggleFancyLines(){
        super("Toggle Fancy Lines painting");
    }

    private static class ToggleFancyLinesHolder {
        private static final ToggleFancyLines instance =
            new ToggleFancyLines();
    }

    public static ToggleFancyLines getInstance(){
        return ToggleFancyLinesHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUIMonitor.Monitor.toggleOption(GraphicEnvironment.FANCY_LINE_OPTION);
    }
}
