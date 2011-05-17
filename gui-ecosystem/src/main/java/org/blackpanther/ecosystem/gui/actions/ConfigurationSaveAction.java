package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.blackpanther.ecosystem.Configuration.textify;
import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/18/11
 */
public class ConfigurationSaveAction
        extends FileBrowserAction {

    private ConfigurationSaveAction() {
        super("Save current configuration",
                "Configuration files",
                "environment-conf");
    }

    private static class ConfigurationSaveActionHolder {
        private static final ConfigurationSaveAction instance =
                new ConfigurationSaveAction();
    }

    public static ConfigurationSaveAction getInstance() {
        return ConfigurationSaveActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component parent = e.getSource() instanceof Component
                ? ((Component) e.getSource()).getParent()
                : null;

        switch (fc.showSaveDialog(parent)) {
            case JFileChooser.APPROVE_OPTION:
                File selectedFile = fc.getSelectedFile();
                int returnVal = JOptionPane.OK_OPTION;
                if (selectedFile.exists()) {
                    returnVal = JOptionPane.showConfirmDialog(
                            parent,
                            "Are you sure you want to overwrite : "
                                    + selectedFile.getName()
                                    + " ?",
                            "File collision !",
                            JOptionPane.YES_NO_OPTION
                    );
                }

                boolean gotExtension = selectedFile.getName().contains(".");

                File file = gotExtension ?
                        selectedFile
                        : new File(selectedFile.getAbsolutePath() + ".environment-conf");
                boolean toDelete = !file.exists();

                if (returnVal == JOptionPane.OK_OPTION) {
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(
                                new FileOutputStream(
                                        file));
                        os.writeObject(textify(Configuration.Configuration.parameters()));
                        os.close();
                        JOptionPane.showMessageDialog(
                                parent,
                                "File saved : " + file.getName(),
                                "Save operation",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(
                                parent,
                                "Couldn't save environment file :\n"
                                        + e1.getLocalizedMessage(),
                                "Save operation",
                                JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                        if (toDelete) {
                            file.delete();
                        }
                    }
                }
        }
    }
}
