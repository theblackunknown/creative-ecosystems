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
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public class SaveImageAction
        extends FileBrowserAction {

    private SaveImageAction() {
        super("Save current image",
                "Image files",
                "png", "bmp", "gif", "jpeg"
                );
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

                String extension =
                        gotExtension
                                ? selectedFile.getName().substring(
                                selectedFile.getName().lastIndexOf(".") + 1,
                                selectedFile.getName().length()
                        )
                                : "png";


                File file = gotExtension ?
                        selectedFile
                        : new File(selectedFile.getAbsolutePath() + ".png");

                if (returnVal == JOptionPane.OK_OPTION) {
                    try {
                        ImageIO.write(
                                image,
                                extension,
                                file);
                        JOptionPane.showMessageDialog(
                                parent,
                                "File saved : " + file.getName(),
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
    }
}
