package org.blackpanther.ecosystem.gui;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.blackpanther.ecosystem.gui.actions.*;
import org.blackpanther.ecosystem.gui.commands.EnvironmentCommands;
import org.blackpanther.ecosystem.gui.settings.EnvironmentBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class WorldFrame
        extends JFrame {

    public static final String ICON_PATH = "org/blackpanther/black-cat-icon.png";
    public static final Image APPLICATION_ICON = fetchApplicationIcon();

    //Set UI Manager
    static {
        try {
            UIManager.setLookAndFeel(
                    new NimbusLookAndFeel()
            );
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static Image fetchApplicationIcon() {
        try {
            URL resourceURL = WorldFrame.class.getClassLoader()
                    .getResource(ICON_PATH);
            return ImageIO.read(resourceURL);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private WorldFrame() {
        super();
        setTitle("GUI Ecosystem Evolution Visualizer");
        if (APPLICATION_ICON != null)
            setIconImage(APPLICATION_ICON);

        setJMenuBar(buildMenuBar());

        GraphicEnvironment graphicEnvironment =
                new GraphicEnvironment();
        Monitor.registerDrawPanel(
                graphicEnvironment
        );

        EnvironmentBoard environmentBoard =
                new EnvironmentBoard();
        Monitor.registerEnvironmentInformationPanel(
                environmentBoard
        );

        EnvironmentCommands environmentCommands =
                new EnvironmentCommands();
        Monitor.registerEnvironmentCommandsPanel(
                environmentCommands
        );

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(
                wrapComponent(environmentBoard, BorderLayout.WEST),
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
        pack();
        setExtendedState(MAXIMIZED_BOTH);

        Monitor.resetEnvironment();
    }

    private static class WorldFrameHolder {
        private static final WorldFrame instance =
                new WorldFrame();
    }

    public static WorldFrame getInstance() {
        return WorldFrameHolder.instance;
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu environment = new JMenu("Environment");

        file.add(LoadConfigurationAction.getInstance());
        file.add(SaveImageAction.getInstance());

        environment.add(ConfigurationSaveAction.getInstance());
        environment.add(ConfigurationLoadAction.getInstance());
        environment.addSeparator();
        environment.add(EnvironmentSaveAction.getInstance());
        environment.add(EnvironmentLoadAction.getInstance());

        menuBar.add(file);
        menuBar.add(environment);

        return menuBar;
    }

    /**
     * Convenience method.<br/>
     * Don't hate me because of the trick...
     */
    private static JPanel wrapComponent(final Component c, final String flag) {
        return new JPanel(new BorderLayout()) {{
            add(c, flag);
        }};
    }
}
