package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public class Launcher {

    public static void main(String[] args) {
        //Show them our wonderful GUI
        createAndShowGUI();
    }

    public static void createAndShowGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WorldFrame.getInstance().setVisible(true);
                WorldFrame.getInstance().validate();
                Environment env = new DesignEnvironment(
                        Configuration.Configuration.getParameter()
                );
            }
        });
    }
}