package org.blackpanther.ecosystem.gui.wizard;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/10/11
 */
public abstract class EnvironmentCreationWizardInternalPanel extends JPanel {

    public static final String DECORATION = "com/nexes/wizard/evolution-ornament.jpg";

    protected EnvironmentCreationWizardInternalPanel() {
        super(new BorderLayout());
        setName("Parameter Initialization");
        setBorder(BorderFactory
                .createEmptyBorder(10, 10, 10, 10));

        Icon icon = new ImageIcon(
                getClass().getClassLoader().getResource(DECORATION));
        JLabel iconPan = new JLabel(icon);
        iconPan.setBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.RAISED));

        JPanel secondaryPanel = new JPanel();
        secondaryPanel.setBorder(BorderFactory
                .createEmptyBorder(10, 20, 10, 20));
        secondaryPanel.add(generateContent(), BorderLayout.NORTH);

        add(iconPan, BorderLayout.WEST);
        add(secondaryPanel, BorderLayout.CENTER);
    }

    protected abstract Component generateContent();
}
