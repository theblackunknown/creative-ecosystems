package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.*;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.AgentFactory.StandardAgent;
import static org.blackpanther.ecosystem.helper.AgentFactory.RandomAgent;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class Launcher {

    public static void main(String[] args) {
        int numberOfAgent = 50;

        //Set default behaviour
        Configuration.setParameter(
                AGENT_DEFAULT_BEHAVIOUR_MANAGER,
                new McCormackDraughtsmanBehaviour(),
                BehaviorManager.class

        );
        //Create an environment
        Environment environment = new DesignEnvironment(
                Configuration.getParameter(ENVIRONMENT_WIDTH, Integer.class),
                Configuration.getParameter(ENVIRONMENT_HEIGHT, Integer.class)
        );
        List<Agent> initialPool = new ArrayList<Agent>(numberOfAgent);
        double Xoffset = 75.0;
        double YOffset = 25.0;
        for (int i = numberOfAgent; --i > 0;)
            initialPool.add(
                    StandardAgent(
                            i * Xoffset,
                            i * YOffset
                    )
            );
        //Add a single agent
        environment.addAgent(
                initialPool
        );
        //Show them our wonderful GUI
        createAndShowGUI(environment);
        //TODO - Run simulation
    }

    //TODO Return GUI Monitor
    public static void createAndShowGUI(Environment env) {
        JFrame mainFrame = new WorldFrame(env);
        mainFrame.setVisible(true);
    }
}
