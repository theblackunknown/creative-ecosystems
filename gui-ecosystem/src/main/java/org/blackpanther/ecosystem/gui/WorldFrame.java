package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.gui.actions.EnvironmentCreationAction;
import org.blackpanther.ecosystem.gui.actions.LoadConfigurationAction;
import org.blackpanther.ecosystem.gui.actions.SaveImageAction;

import javax.swing.*;
import java.awt.*;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class WorldFrame
        extends JFrame {

    public WorldFrame() {
        super();

        setJMenuBar(buildMenuBar());

        EnvironmentInformationPanel environmentInformationPanel =
                new EnvironmentInformationPanel();
        GraphicEnvironment graphicEnvironment =
                new GraphicEnvironment();
        EnvironmentCommands environmentCommands =
                new EnvironmentCommands();

        Monitor.registerDrawPanel(
                graphicEnvironment
        );
        Monitor.registerEnvironmentSettingsPanel(
                environmentInformationPanel
        );
        Monitor.registerEnvironmentCommandsPanel(
                environmentCommands
        );

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(
                wrapComponent(environmentInformationPanel, BorderLayout.WEST),
                BorderLayout.WEST
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

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu environment = new JMenu("Environment");

        JMenuItem createEnvironment = new JMenuItem(
                EnvironmentCreationAction.getInstance());
        createEnvironment.setEnabled(false);

        file.add(LoadConfigurationAction.getInstance());
        file.add(SaveImageAction.getInstance());

        environment.add(createEnvironment);

        menuBar.add(file);
        menuBar.add(environment);

        return menuBar;
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
