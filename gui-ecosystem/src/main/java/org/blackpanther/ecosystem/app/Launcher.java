package org.blackpanther.ecosystem.app;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.management.monitor.Monitor;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.AgentFactory.RandomAgent;
import static org.blackpanther.ecosystem.helper.AgentFactory.StandardAgent;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class Launcher {

    public static void main(String[] args) {
        //Create an environment
        Environment environment = new DesignEnvironment();
        //Add agents
        environment.addAgent(
                generatePopulation(
                        Launcher.GenerationType.STANDARD_POSITION_PROVIDED,
                        400.0,400.0,50.0
                ));
        //Show them our wonderful GUI
        createAndShowGUI();
        Monitor.setCurrentEnvironment(environment);
    }

    public static void createAndShowGUI() {
        JFrame mainFrame = new WorldFrame();
        mainFrame.setVisible(true);
    }

    enum GenerationType {
        STANDARD_POSITION_PROVIDED,
        STANDARD_POSITION_RANDOMIZED,
        RANDOM;
    }

    private static Collection<Agent> generatePopulation(
            GenerationType genType,
            Object... additionalParameters) {
        List<Agent> pool = new ArrayList<Agent>();
        switch (genType) {
            case STANDARD_POSITION_PROVIDED: {
                Double XLimit = (Double) additionalParameters[0];
                Double YLimit = (Double) additionalParameters[1];
                Double step = (Double) additionalParameters[2];

                for (double i = -XLimit; i < XLimit; i += step) {
                    for (double j = -YLimit; j < YLimit; j += step) {
                        pool.add(
                                StandardAgent(i, j)
                        );
                    }
                }
                break;
            }
            case STANDARD_POSITION_RANDOMIZED: {
                Integer numberOfAgent = (Integer) additionalParameters[0];
                for (int number = numberOfAgent; number-- > 0;) {
                    Point2D randomPoint = new Point2D.Double(
                            (Configuration.getParameter(RANDOM, Random.class).nextDouble()
                                    * Configuration.getParameter(SPAWN_ABSCISSA_THRESHOLD, Double.class) * 2)
                                    - Configuration.getParameter(SPAWN_ABSCISSA_THRESHOLD, Double.class),
                            (Configuration.getParameter(RANDOM, Random.class).nextDouble()
                                    * Configuration.getParameter(SPAWN_ORDINATE_THRESHOLD, Double.class) * 2)
                                    - Configuration.getParameter(SPAWN_ORDINATE_THRESHOLD, Double.class)
                    );
                    pool.add(
                            StandardAgent(randomPoint.getX(), randomPoint.getY())
                    );
                }
                break;
            }
            case RANDOM: {
                Integer numberOfAgent = (Integer) additionalParameters[0];
                for (int number = numberOfAgent; number-- > 0;)
                    pool.add(
                            RandomAgent()
                    );
                break;
            }
        }
        return pool;
    }
}
