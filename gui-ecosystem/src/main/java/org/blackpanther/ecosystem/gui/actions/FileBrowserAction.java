package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author MACHIZAUD Andréa
 * @version 5/18/11
 */
public abstract class FileBrowserAction
        extends AbstractAction {

    protected final JFileChooser fc = new JFileChooser(".");

    public FileBrowserAction(
            String name,
            String fileDescription,
            String... extensions) {
        super(name);
        fc.setFileFilter(
                new FileNameExtensionFilter(
                        fileDescription, extensions
                )
        );
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
    }
}
