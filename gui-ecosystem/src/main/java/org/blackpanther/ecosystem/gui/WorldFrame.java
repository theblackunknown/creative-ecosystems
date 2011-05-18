package org.blackpanther.ecosystem.gui;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.blackpanther.ecosystem.gui.actions.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.net.URL;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class WorldFrame
        extends JFrame
implements WindowStateListener {

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
        addWindowStateListener(this);
        pack();
        setExtendedState(MAXIMIZED_BOTH);
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        validate();
        validateTree();
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
