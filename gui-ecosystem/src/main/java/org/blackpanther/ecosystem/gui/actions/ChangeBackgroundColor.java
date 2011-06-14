package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 27/05/11
 */
public class ChangeBackgroundColor
        extends AbstractAction {

    private ChangeBackgroundColor(){
        super("Change background color");
    }

    private static class ChangeBackgroundColorHolder {
        private static final ChangeBackgroundColor instance =
            new ChangeBackgroundColor();
    }

    public static ChangeBackgroundColor getInstance(){
        return ChangeBackgroundColorHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(
                WorldFrame.getInstance(),
                "Choose agent identifier",
                Color.LIGHT_GRAY);
        if (selectedColor != null) {
            Monitor.setBackgroundColor(selectedColor);
        }
    }
}
