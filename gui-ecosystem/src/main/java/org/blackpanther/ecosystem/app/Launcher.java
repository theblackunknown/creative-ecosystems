package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.LogManager;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:59 CEST 2011
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