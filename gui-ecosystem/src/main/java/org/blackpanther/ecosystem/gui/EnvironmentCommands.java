package org.blackpanther.ecosystem.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Wed May 18 02:01:10 CEST 2011
 */
public class EnvironmentCommands
        extends JPanel {

    public static final String STOP_ENVIRONMENT = "Pause";
    public static final String START_ENVIRONMENT = "Start";
    public static final String NO_ENVIRONMENT = "No environment set";
    public static final String FROZEN_ENVIRONMENT = "Environment frozen";

    private JButton evolutionFlowButton;

    public EnvironmentCommands() {
        super();
        setLayout(new FlowLayout());


        JButton resetEnvironment = new JButton("Generate new environment");
        evolutionFlowButton = new JButton(NO_ENVIRONMENT);
        JCheckBox drawBounds = new JCheckBox("Paint bounds", false);
        JCheckBox drawResources = new JCheckBox("Paint resources", false);
        JButton backgroundColor = new JButton("Choose background color");

        resetEnvironment.addActionListener(EventHandler.create(
                ActionListener.class,
                Monitor,
                "recreateEnvironment"
        ));
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
        backgroundColor.addActionListener(EventHandler.create(
                ActionListener.class,
                this,
                "notifyBackgroundColorChanged"
        ));

        add(resetEnvironment);
        add(evolutionFlowButton);
        add(Box.createVerticalStrut(40));
        add(drawBounds);
        add(drawResources);
        add(Box.createVerticalStrut(40));
        add(backgroundColor);

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

    public void notifyBackgroundColorChanged() {
        Color selectedColor = JColorChooser.showDialog(
                WorldFrame.getInstance(),
                "Choose agent identifier",
                Color.LIGHT_GRAY);
        if (selectedColor != null) {
            Monitor.setBackgroundColor(selectedColor);
        }
    }

}
