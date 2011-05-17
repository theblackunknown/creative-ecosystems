package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;
import org.blackpanther.ecosystem.gui.WorldFrame;
import org.blackpanther.ecosystem.helper.AgentFactory;
import org.blackpanther.ecosystem.placement.GenerationStrategy;

import javax.swing.*;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;
import static org.blackpanther.ecosystem.placement.GenerationStrategy.GenerationType;
import static org.blackpanther.ecosystem.placement.GenerationStrategy.generatePopulation;

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
//                Environment env = new DesignEnvironment(
//                        Configuration.getParameter(SPACE_WIDTH, Double.class),
//                        Configuration.getParameter(SPACE_HEIGHT, Double.class)
//                );
//                for (double x = -400.0, y = 0.0;
//                     x < 400.0 && y > -400.0;
//                     x += 1.0, y -= 1.0)
//                    env.addAgent(AgentFactory.StandardAgent(x, y, Math.PI/4));
////                env.addAgent(
////                        generatePopulation(
////                                Agent.class,
////                                GenerationType.RANDOM,
////                                1000L
////                        )
////                );
//                env.addResource(
//                        generatePopulation(
//                                Resource.class,
//                                GenerationStrategy.GenerationType.STANDARD_POSITION_PROVIDED,
//                                200.0,
//                                200.0,
//                                10.0
//                        )
//                );
//                Monitor.setCurrentEnvironment(env);
            }
        });
    }
}