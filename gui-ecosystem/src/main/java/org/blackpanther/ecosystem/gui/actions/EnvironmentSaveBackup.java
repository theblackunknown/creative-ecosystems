package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Properties;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class EnvironmentSaveBackup
        extends FileBrowserAction {

    private Environment backupEnvironment;
    private Properties backupApplicationConfiguration;
    private FieldsConfiguration backupParametersConfiguration;

    private EnvironmentSaveBackup() {
        super("Save initial state of this environment", "Environment files", "env");
    }

    private static class EnvironmentSaveBackupHolder {
        private static final EnvironmentSaveBackup instance =
            new EnvironmentSaveBackup();
    }

    public static EnvironmentSaveBackup getInstance(){
        return EnvironmentSaveBackupHolder.instance;
    }

    public void backup(
            Environment environment,
            Configuration applicationConfiguration,
            FieldsConfiguration parametersConfiguration) {
        this.backupEnvironment = environment.clone();
        this.backupApplicationConfiguration = applicationConfiguration.textCopy();
        this.backupParametersConfiguration = parametersConfiguration.clone();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.pauseEvolution();

        Component parent = WorldFrame.getInstance();

        if (backupEnvironment == null)
            JOptionPane.showMessageDialog(
                    parent,
                    "No environment set yet",
                    "Save operation",
                    JOptionPane.ERROR_MESSAGE);

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
                        : new File(selectedFile.getAbsolutePath() + ".env");
                boolean toDelete = !file.exists();

                if (returnVal == JOptionPane.OK_OPTION) {
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(
                                new FileOutputStream(
                                        file));
                        os.writeObject(backupEnvironment);
                        os.writeObject(backupApplicationConfiguration);
                        os.writeObject(backupParametersConfiguration);
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
