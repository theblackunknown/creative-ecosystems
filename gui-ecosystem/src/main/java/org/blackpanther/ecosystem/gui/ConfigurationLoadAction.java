package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.actions.FileBrowserAction;

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
 * @version 5/18/11
 */
public class ConfigurationLoadAction
        extends FileBrowserAction {

    private ConfigurationLoadAction() {
        super("Load external configuration",
                "Configuration files",
                "environment-conf");
    }

    private static class ConfigurationLoadActionHolder {
        private static final ConfigurationLoadAction instance =
                new ConfigurationLoadAction();
    }

    public static ConfigurationLoadAction getInstance() {
        return ConfigurationLoadActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Component parent = e.getSource() instanceof Component
                ? ((Component) e.getSource()).getParent()
                : null;

        int returnVal = JOptionPane.showConfirmDialog(parent,
                "Current environment will be lost !",
                "Warning before loading external conf",
                JOptionPane.YES_NO_OPTION);

        if (returnVal == JOptionPane.OK_OPTION) {
            switch (fc.showOpenDialog(parent)) {
                case JFileChooser.APPROVE_OPTION:
                    File selectedFile = fc.getSelectedFile();

                    try {
                        ObjectInputStream os = new ObjectInputStream(
                                new FileInputStream(
                                        selectedFile));
                        Properties environmentProperties = (Properties) os.readObject();
                        os.close();
                        Monitor.removeEnvironment();
                        Configuration.Configuration.loadConfiguration(environmentProperties);
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
        } else {
            JOptionPane.showMessageDialog(parent,"Loading aborted");
        }
    }
}
