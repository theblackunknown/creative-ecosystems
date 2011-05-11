package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.WorldFrame;
import org.blackpanther.ecosystem.placement.Strategy;

import javax.swing.*;

import static org.blackpanther.ecosystem.placement.Strategy.generatePopulation;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
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
            }
        });
    }
}
