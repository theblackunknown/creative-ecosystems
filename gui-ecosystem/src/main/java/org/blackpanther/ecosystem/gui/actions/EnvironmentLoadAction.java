package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 16/05/11
 */
public class EnvironmentLoadAction
        extends AbstractAction {

    private JFileChooser fileSaver = new JFileChooser(".");

    private EnvironmentLoadAction() {
        super("Load an environment");
        fileSaver.setFileFilter(
                new FileNameExtensionFilter(
                        "Environment files", "env"
                )
        );
        fileSaver.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSaver.setMultiSelectionEnabled(false);
    }

    private static class LoadEnvironmentActionHolder {
        private static final EnvironmentLoadAction instance =
                new EnvironmentLoadAction();
    }

    public static EnvironmentLoadAction getInstance() {
        return LoadEnvironmentActionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.pauseEvolution();

        Component parent = e.getSource() instanceof Component
                ? ((Component) e.getSource()).getParent()
                : null;
        switch (fileSaver.showOpenDialog(parent)) {
            case JFileChooser.APPROVE_OPTION:
                File selectedFile = fileSaver.getSelectedFile();

                try {
                    ObjectInputStream os = new ObjectInputStream(
                            new FileInputStream(
                                    selectedFile));
                    Environment env = (Environment) os.readObject();
                    os.close();
                    Monitor.setCurrentEnvironment(env);
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
