package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 16/05/11
 */
public class EnvironmentSaveAction
        extends AbstractAction {

    private JFileChooser fileSaver = new JFileChooser(".");

    private EnvironmentSaveAction() {
        super("Save current environment");
        fileSaver.setFileFilter(
                new FileNameExtensionFilter(
                        "Environment files", "env"
                )
        );
        fileSaver.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSaver.setMultiSelectionEnabled(false);
    }

    private static class SaveEnvironmentActionHolder {
        private static final EnvironmentSaveAction instance =
                new EnvironmentSaveAction();
    }

    public static EnvironmentSaveAction getInstance() {
        return SaveEnvironmentActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component parent = e.getSource() instanceof Component
                ? ((Component) e.getSource()).getParent()
                : null;

        Monitor.pauseEvolution();
        Environment env = Monitor.dumpCurrentEnvironment();
        if (env == null) //TODO Make action unavailable
            JOptionPane.showMessageDialog(
                    parent,
                    "No environment set yet",
                    "Save operation",
                    JOptionPane.ERROR_MESSAGE);


        switch (fileSaver.showSaveDialog(parent)) {
            case JFileChooser.APPROVE_OPTION:
                File selectedFile = fileSaver.getSelectedFile();
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
