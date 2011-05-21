package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.*;
import org.blackpanther.ecosystem.behaviour.PredatorBehaviour;
import org.blackpanther.ecosystem.behaviour.PreyBehaviour;
import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;

import java.awt.*;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.AgentFactory.StandardAgent;

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
                        Configuration.getParameter(SPACE_WIDTH, Double.class),
                        Configuration.getParameter(SPACE_HEIGHT, Double.class)
                );
                Agent predator = StandardAgent(Color.BLUE, -10.0, -7.5, Math.PI * ( 1.8 / 4.0 ), 1.3, new PredatorBehaviour());
                Agent prey = StandardAgent(Color.RED, -2.0, -5.0, Math.PI * ( 2.2 / 4.0 ), 1.0, new PreyBehaviour());
                env.addAgent(predator);
                env.addAgent(prey);
                GUIMonitor.Monitor.setCurrentEnvironment(env);
            }
        });
    }
}