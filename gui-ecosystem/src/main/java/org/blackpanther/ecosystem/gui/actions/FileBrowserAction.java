package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
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
