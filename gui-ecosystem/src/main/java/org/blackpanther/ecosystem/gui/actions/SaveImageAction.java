package org.blackpanther.ecosystem.gui.actions;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/9/11
 */
public class SaveImageAction
        extends AbstractAction {

    private JFileChooser fileSaver = new JFileChooser(".");

    private SaveImageAction() {
        super("Save current image.");
        fileSaver.setFileFilter(
                new FileNameExtensionFilter(
                        "Image files", "png", "bmp", "gif", "jpeg"
                )
        );
        fileSaver.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSaver.setMultiSelectionEnabled(false);
    }

    private static class SaveImageHolder {
        private static final SaveImageAction instance =
                new SaveImageAction();
    }

    public static SaveImageAction getInstance() {
        return SaveImageHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Monitor.pauseEvolution();

        BufferedImage image = Monitor.dumpCurrentImage();
        if (image == null)
            return;

        Component parent = e.getSource() instanceof Component
                ? ((Component) e.getSource()).getParent()
                : null;
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

                String extension =
                        gotExtension
                                ? selectedFile.getName().substring(
                                selectedFile.getName().lastIndexOf(".") + 1,
                                selectedFile.getName().length()
                        )
                                : "png";


                if (returnVal == JOptionPane.OK_OPTION) {
                    try {
                        ImageIO.write(
                                image,
                                extension,
                                fileSaver.getSelectedFile());
                        JOptionPane.showMessageDialog(
                                parent,
                                "File saved : " + selectedFile.getName() + (
                                        gotExtension
                                                ? ""
                                                : ".png"),
                                "Save operation",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(
                                parent,
                                "Couldn't save image file : "
                                        + e1.getLocalizedMessage(),
                                "Save operation",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
        }

        Monitor.resumeEvolution();
    }
}
