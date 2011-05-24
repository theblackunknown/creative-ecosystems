package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public class EnvironmentSave
        extends FileBrowserAction {

    private EnvironmentSave() {
        super("Save current environment", "Environment files", "env");
    }

    private static class EnvironmentSaveActionHolder {
        private static final EnvironmentSave instance =
                new EnvironmentSave();
    }

    public static EnvironmentSave getInstance() {
        return EnvironmentSaveActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        save((Component) e.getSource(), Monitor.dumpCurrentEnvironment());
    }

    public void save(Environment backup) {
        save(null, backup);
    }

    public void save(Component parent, Environment env) {
        Monitor.pauseEvolution();
        if (env == null)
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
                        os.writeObject(env);
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
