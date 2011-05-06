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

        EnvironmentSetting environmentSetting =
                new EnvironmentSetting();
        GraphicEnvironment graphicEnvironment =
                new GraphicEnvironment(env);
        EnvironmentCommands environmentCommands =
                new EnvironmentCommands();

        Monitor.registerDrawPanel(
                graphicEnvironment
        );
        Monitor.registerEnvironmentSettingsPanel(
                environmentSetting
        );
        Monitor.registerEnvironmentCommandsPanel(
                environmentCommands
        );

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(
                wrapComponent(environmentSetting, BorderLayout.WEST),
                BorderLayout.NORTH
        );
        getContentPane().add(
                graphicEnvironment,
                BorderLayout.CENTER
        );
        getContentPane().add(
                wrapComponent(environmentCommands, BorderLayout.CENTER),
                BorderLayout.SOUTH
        );

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        pack();
        setExtendedState(MAXIMIZED_BOTH);
    }

    /**
     * Convenience method.<br/>
     * Don't hate me because of the trick...
     *
     * @param c component to encapsulate
     * @return Wrapped component
     */
    private static JPanel wrapComponent(final Component c, final String flag) {
        return new JPanel(new BorderLayout()) {{
            add(c, flag);
        }};
    }
}
