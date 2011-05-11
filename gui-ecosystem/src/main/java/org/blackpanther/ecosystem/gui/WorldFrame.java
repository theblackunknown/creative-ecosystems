package org.blackpanther.ecosystem.gui;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.blackpanther.ecosystem.gui.actions.EnvironmentCreationAction;
import org.blackpanther.ecosystem.gui.actions.LoadConfigurationAction;
import org.blackpanther.ecosystem.gui.actions.SaveImageAction;

import javax.swing.*;
import java.awt.*;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class WorldFrame
        extends JFrame {


    private WorldFrame() {
        super();
        setTitle("GUI Ecosystem Evolution Visualizer");
        try {
            UIManager.setLookAndFeel(
                    new NimbusLookAndFeel()
            );
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
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

    private static class WorldFrameHolder {
        private static final WorldFrame instance =
            new WorldFrame();
    }

    public static WorldFrame getInstance(){
        return WorldFrameHolder.instance;
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu environment = new JMenu("Environment");

        JMenuItem createEnvironment = new JMenuItem(
                EnvironmentCreationAction.getInstance());

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
