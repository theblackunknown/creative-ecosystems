package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.gui.formatter.WindowDimensionFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 5/4/11
 */
public class GraphicEnvironmentSetting extends JPanel {

    private JLabel environmentLabel;
    private JLabel environmentAgentStatistics;
    private JFormattedTextField environmentDimensionField;

    private static final Dimension DEFAULT_DIMENSION = new Dimension(200, 100);
    private static final Dimension FIELD_DIMENSION = new Dimension(80, 20);

    public GraphicEnvironmentSetting() {
        super();
        setPreferredSize(DEFAULT_DIMENSION);
        //Find a better one
        setLayout(new GridBagLayout());

        environmentLabel =
                new JLabel("No environment set");
        environmentAgentStatistics =
                new JLabel("No environment set");
        environmentDimensionField =
                new JFormattedTextField(
                        WindowDimensionFormatter.getInstance()
                );
        environmentDimensionField.setPreferredSize(
                FIELD_DIMENSION
        );
        JLabel environmentDimensionLabel =
                new JLabel("Environment dimension : ");
        environmentDimensionLabel.setLabelFor(environmentDimensionField);

        environmentDimensionField.addActionListener(
                EventHandler.create(
                        ActionListener.class,
                        Monitor,
                        "updateEnvironmentDimension",
                        "source.value"
                ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        //Environment Label
        add(
                environmentLabel,
                constraints
        );
        //Environment Agent statistics
        add(
                environmentAgentStatistics,
                constraints
        );

    }

    void setEnvironmentId(Long environmentId) {
        this.environmentLabel.setText(
                "Environment id : " + Long.toHexString(environmentId)
        );
    }

    void setEnvironmentAgentNumber(Integer agentNumber) {
        this.environmentAgentStatistics.setText(String.format(
                "Agent number : %d", agentNumber
        ));
    }

    void setEnvironmentDimension(Dimension2D environmentDimension) {
        this.environmentDimensionField.setText(String.format(
                "%f.2fx%.2f",
                environmentDimension.getWidth(),
                environmentDimension.getHeight()
        ));
    }

    void setEnvironmentDimensionEditable(boolean editable) {
        this.environmentDimensionField.setEditable(
                editable
        );
    }
}
