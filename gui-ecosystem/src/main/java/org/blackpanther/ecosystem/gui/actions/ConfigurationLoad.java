package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class ConfigurationLoad
        extends FileBrowserAction {

    private ConfigurationLoad() {
        super("Load external configuration",
                "Configuration files",
                "environment-conf");
    }

    private static class ConfigurationLoadActionHolder {
        private static final ConfigurationLoad instance =
                new ConfigurationLoad();
    }

    public static ConfigurationLoad getInstance() {
        return ConfigurationLoadActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Component parent = e.getSource() instanceof Component
                ? ((Component) e.getSource()).getParent()
                : null;

        switch (fc.showOpenDialog(parent)) {
            case JFileChooser.APPROVE_OPTION:
                File selectedFile = fc.getSelectedFile();

                try {
                    ObjectInputStream os = new ObjectInputStream(
                            new FileInputStream(
                                    selectedFile));
                    Properties environmentProperties = (Properties) os.readObject();
                    FieldsConfiguration agentConfiguration = (FieldsConfiguration) os.readObject();
                    os.close();
                    Configuration.Configuration.loadConfiguration(environmentProperties);
                    Monitor.updateSettingFields(agentConfiguration);
                    JOptionPane.showMessageDialog(
                            parent,
                            "File loaded : " + selectedFile.getName(),
                            "Load operation",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Couldn't load " + selectedFile.getName() + " : "
                                    + e1.getLocalizedMessage(),
                            "Load operation",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Couldn't load " + selectedFile.getName() + " : "
                                    + e1.getLocalizedMessage(),
                            "Load operation",
                            JOptionPane.ERROR_MESSAGE);
                }
        }
    }
}
