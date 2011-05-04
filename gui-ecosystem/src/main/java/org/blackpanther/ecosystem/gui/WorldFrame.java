package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import java.awt.*;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class WorldFrame
        extends JFrame {

    public WorldFrame(Environment env) {
        super();

        final GraphicEnvironmentSetting environmentSetting =
                new GraphicEnvironmentSetting();
        GraphicEnvironment graphicEnvironment =
                new GraphicEnvironment(env);

        Monitor.registerDrawPanel(
                graphicEnvironment
        );
        Monitor.registerEnvironmentSettingsPanel(
                environmentSetting
        );

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(
                new JPanel() {{
                    setLayout(new BorderLayout());
                    add(environmentSetting);
                }},
                BorderLayout.NORTH
        );
        getContentPane().add(
                graphicEnvironment,
                BorderLayout.CENTER
        );

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        pack();
        setExtendedState(MAXIMIZED_BOTH);
    }
}
