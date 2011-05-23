package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.LogManager;

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
        try {
            LogManager.getLogManager().readConfiguration(
                    Launcher.class.getClassLoader().getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WorldFrame.getInstance().setVisible(true);
                WorldFrame.getInstance().validate();
            }
        });
    }
}