package org.blackpanther.ecosystem.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/4/11
 */
public class EnvironmentSetting extends JPanel {

    private JLabel environmentLabel;
    private JLabel environmentAgentCounter;
    private JLabel environmentCycleCounter;

    private static final Dimension DEFAULT_DIMENSION = new Dimension(200, 75);

    public EnvironmentSetting() {
        super();
        setPreferredSize(DEFAULT_DIMENSION);
        //TODO Find a better one
        setLayout(new GridBagLayout());

        environmentLabel =
                new JLabel("No environment set");
        environmentAgentCounter =
                new JLabel("No environment set");
        environmentCycleCounter =
                new JLabel("No environment set");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        //Environment Label
        add(environmentLabel, constraints);
        add(environmentAgentCounter, constraints);
        add(environmentCycleCounter, constraints);

        setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
        );
    }

    void setEnvironmentId(Long environmentId) {
        this.environmentLabel.setText(
                "Environment id : " + Long.toHexString(environmentId)
        );
    }

    void setEnvironmentAgentNumber(Integer agentNumber) {
        this.environmentAgentCounter.setText(String.format(
                "Agent number : %d", agentNumber
        ));
    }

    void setEnvironmentCycleCount(Integer cycleCount) {
        this.environmentCycleCounter.setText(String.format(
                "#Cycle : %d", cycleCount
        ));
    }

    void environmentEnded() {
        this.environmentLabel.setText(
                environmentLabel.getText() + "[FROZEN]"
        );
    }
}
