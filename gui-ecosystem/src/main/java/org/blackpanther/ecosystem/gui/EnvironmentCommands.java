package org.blackpanther.ecosystem.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * TODO Slider to manage evolution speed
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class EnvironmentCommands
        extends JPanel {

    private static final Dimension DEFAULT_DIMENSION = new Dimension(200, 40);

    public static final String STOP_ENVIRONMENT = "Pause";
    public static final String START_ENVIRONMENT = "Start";
    public static final String NO_ENVIRONMENT = "No environment set";
    public static final String FROZEN_ENVIRONMENT = "Environment frozen";

    private JButton evolutionFlowButton;
    private JCheckBox drawBounds;
    private JCheckBox drawResources;

    public EnvironmentCommands() {
        super();
        setPreferredSize(DEFAULT_DIMENSION);
        setLayout(new FlowLayout());

        evolutionFlowButton = new JButton(NO_ENVIRONMENT);
        drawBounds = new JCheckBox("Paint bounds", true);
        drawResources = new JCheckBox("Paint resources", true);

        evolutionFlowButton.setEnabled(false);
        evolutionFlowButton.addActionListener(EventHandler.create(
                ActionListener.class,
                this,
                "switchStartPause"
        ));
        evolutionFlowButton.addActionListener(EventHandler.create(
                ActionListener.class,
                Monitor,
                "interceptEnvironmentEvolutionFlow",
                "source.text"
        ));
        drawBounds.addActionListener(EventHandler.create(
                ActionListener.class,
                Monitor,
                "paintBounds",
                "source.selected"
        ));
        drawResources.addActionListener(EventHandler.create(
                ActionListener.class,
                Monitor,
                "paintResources",
                "source.selected"
        ));

        add(evolutionFlowButton);
        add(drawBounds);
        add(drawResources);

        setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
        );
    }

    public void switchStartPause() {
        if (evolutionFlowButton.getText()
                .equals(STOP_ENVIRONMENT)) {
            evolutionFlowButton.setText(START_ENVIRONMENT);
        } else if (evolutionFlowButton.getText()
                .equals(START_ENVIRONMENT)) {
            evolutionFlowButton.setText(STOP_ENVIRONMENT);
        } else {
            evolutionFlowButton.setText(NO_ENVIRONMENT);
        }
    }

    void environmentSet() {
        evolutionFlowButton.setText(START_ENVIRONMENT);
        evolutionFlowButton.setEnabled(true);
    }

    void environmentUnset() {
        evolutionFlowButton.setText(NO_ENVIRONMENT);
        evolutionFlowButton.setEnabled(false);
    }

    public void environmentFrozen() {
        evolutionFlowButton.setText(FROZEN_ENVIRONMENT);
        evolutionFlowButton.setEnabled(false);
    }

    public void notifyPause() {
        if (evolutionFlowButton.getText().equals(STOP_ENVIRONMENT)) {
            evolutionFlowButton.setText(START_ENVIRONMENT);
        }
    }
}
