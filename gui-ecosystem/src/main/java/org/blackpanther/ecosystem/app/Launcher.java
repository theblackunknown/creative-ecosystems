package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.awt.geom.Point2D;

import static org.blackpanther.ecosystem.helper.Helper.computeOrientation;

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
                WorldFrame.getInstance().validate();
            }
        });
    }
}