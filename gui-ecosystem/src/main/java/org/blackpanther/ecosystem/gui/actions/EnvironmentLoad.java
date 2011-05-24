package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public class EnvironmentLoad
        extends FileBrowserAction {

    private EnvironmentLoad() {
        super("Load external environment", "Environment files", "env");
    }

    private static class EnvironmentLoadActionHolder {
        private static final EnvironmentLoad instance =
                new EnvironmentLoad();
    }

    public static EnvironmentLoad getInstance() {
        return EnvironmentLoadActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.pauseEvolution();

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
                    Environment environment = (Environment) os.readObject();
                    Properties applicationParameters = (Properties) os.readObject();
                    FieldsConfiguration agentConfiguration = (FieldsConfiguration) os.readObject();
                    os.close();
                    Configuration.loadConfiguration(applicationParameters);
                    Monitor.updateSettingFields(agentConfiguration);
                    Monitor.setCurrentEnvironment(environment);
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
