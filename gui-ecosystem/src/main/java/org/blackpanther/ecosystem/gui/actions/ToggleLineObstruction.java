package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.blackpanther.ecosystem.ApplicationConstants.LINE_OBSTRUCTION_OPTION;
import static org.blackpanther.ecosystem.Configuration.Configuration;

/**
 * @author MACHIZAUD Andréa
 * @version 26/05/11
 */
public class ToggleLineObstruction
        extends AbstractAction {

    private ToggleLineObstruction() {
        super("Toggle line obstruction");
    }

    private static class ToggleLineObstructionHolder {
        private static final ToggleLineObstruction instance =
                new ToggleLineObstruction();
    }

    public static ToggleLineObstruction getInstance() {
        return ToggleLineObstructionHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            Configuration.setParameter(
                    LINE_OBSTRUCTION_OPTION,
                    ((JCheckBox) e.getSource()).isSelected(),
                    Boolean.class
            );
        } else
            Configuration.setParameter(
                    LINE_OBSTRUCTION_OPTION,
                    !Configuration.getParameter(LINE_OBSTRUCTION_OPTION, Boolean.class),
                    Boolean.class
            );
    }
}
