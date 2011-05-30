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
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
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

        setJMenuBar(buildMenuBar());

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

        JMenu environment = new JMenu("Environment");
        JMenu painting = new JMenu("Paint options");

        environment.add(ConfigurationSave.getInstance());
        environment.add(ConfigurationLoad.getInstance());
        environment.addSeparator();
        environment.add(EnvironmentSave.getInstance());
        environment.add(EnvironmentSaveBackup.getInstance());
        environment.addSeparator();
        environment.add(EnvironmentLoad.getInstance());
        environment.addSeparator();
        environment.add(SaveImageAction.getInstance());

        JCheckBox[] togglers = new JCheckBox[5];
        for (int i= 0; i < togglers.length; i ++) {
            togglers[i] = new JCheckBox();
            togglers[i].setSelected(true);
            painting.add(togglers[i]);
        }

        togglers[0].setAction(ToggleBounds.getInstance());
        togglers[1].setAction(ToggleCreatures.getInstance());
        togglers[2].setAction(ToggleResources.getInstance());
        togglers[3].setAction(ToggleLineObstruction.getInstance());
        togglers[4].setAction(TogglePerlinNoise.getInstance());

        painting.addSeparator();
        painting.add(ChangeBackgroundColor.getInstance());

        menuBar.add(environment);
        menuBar.add(painting);

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
