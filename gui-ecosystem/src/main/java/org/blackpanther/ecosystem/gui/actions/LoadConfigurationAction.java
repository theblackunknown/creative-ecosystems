package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/9/11
 */
public class LoadConfigurationAction extends AbstractAction {

    private JFileChooser fileLoader = new JFileChooser("."); // Start in current directory

    private LoadConfigurationAction() {
        super("Load default parameters");
        fileLoader.setFileFilter(new FileNameExtensionFilter(
                "Application properties", "properties"
        ));
        fileLoader.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileLoader.setMultiSelectionEnabled(false);
    }

    private static class LoadConfigurationHolder {
        private static final LoadConfigurationAction instance =
                new LoadConfigurationAction();
    }

    public static LoadConfigurationAction getInstance() {
        return LoadConfigurationHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.pauseEvolution();

        Component parent = ((Component) e.getSource()).getParent();
        switch (fileLoader.showOpenDialog(parent)) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    Properties loadedProperties = new Properties();
                    loadedProperties.load(
                            new FileReader(fileLoader.getSelectedFile())
                    );
                    Configuration.loadConfiguration(loadedProperties);
                    JOptionPane.showMessageDialog(
                            parent,
                            Configuration.toString(),
                            "Properties successfully loaded",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Property file not found" + e1.getLocalizedMessage(),
                            "Properties not loaded",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Couldn't read your property file : " + e1.getLocalizedMessage(),
                            "Properties not loaded",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
        }
        Monitor.resumeEvolution();

    }
}
