package org.blackpanther.ecosystem.gui.commands;

import org.blackpanther.ecosystem.gui.GraphicEnvironment;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public class EnvironmentCommands
        extends JPanel {

    public static final String STOP_ENVIRONMENT = "Pause";
    public static final String START_ENVIRONMENT = "Start";
    public static final String NO_ENVIRONMENT = "No environment set";
    public static final String FROZEN_ENVIRONMENT = "Environment frozen";

    private JButton evolutionFlowButton;

    private JToggleButton noAction;
    private JToggleButton paintAgent;
    private JToggleButton paintResource;
    private JCheckBox drawCreatures;

    public EnvironmentCommands() {
        super();
        setLayout(new FlowLayout());


        JButton resetEnvironment = new JButton("Generate new environment");
        evolutionFlowButton = new JButton(NO_ENVIRONMENT);
        JCheckBox drawBounds = new JCheckBox("Paint bounds", true);
        JCheckBox drawResources = new JCheckBox("Paint resources", true);
        drawCreatures = new JCheckBox("Paint agents", true);
        JButton backgroundColor = new JButton("Choose background color");

        noAction = new JToggleButton("No action");
        paintAgent = new JToggleButton("Agent dropper");
        paintResource = new JToggleButton("Resource dropper");

        ActionListener toggleListener = EventHandler.create(
                ActionListener.class,
                this,
                "updateDropMode",
                "source"
        );

        noAction.setSelected(true);
        noAction.addActionListener(toggleListener);
        paintAgent.addActionListener(toggleListener);
        paintResource.addActionListener(toggleListener);
        resetEnvironment.addActionListener(EventHandler.create(
                ActionListener.class,
                Monitor,
                "resetEnvironment"
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
        drawCreatures.addActionListener(EventHandler.create(
                ActionListener.class,
                Monitor,
                "paintCreatures",
                "source.selected"
        ));
        backgroundColor.addActionListener(EventHandler.create(
                ActionListener.class,
                this,
                "notifyBackgroundColorChanged"
        ));

        add(noAction);
        add(paintAgent);
        add(paintResource);
        add(Box.createVerticalStrut(40));
        add(resetEnvironment);
        add(evolutionFlowButton);
        add(Box.createVerticalStrut(40));
        add(drawBounds);
        add(drawResources);
        add(drawCreatures);
        add(Box.createVerticalStrut(40));
        add(backgroundColor);

        setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
        );
    }

    public void updateDropMode(JToggleButton button) {
        noAction.setSelected(false);
        paintAgent.setSelected(false);
        paintResource.setSelected(false);
        button.setSelected(true);

        GraphicEnvironment.DropMode mode = GraphicEnvironment.DropMode.NONE;
        if (noAction.isSelected()) {
            mode = GraphicEnvironment.DropMode.NONE;
        } else if (paintAgent.isSelected()) {
            mode = GraphicEnvironment.DropMode.AGENT;
        } else if (paintResource.isSelected()) {
            mode = GraphicEnvironment.DropMode.RESOURCE;
        }
        Monitor.setDropMode(mode);
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

    public void environmentSet() {
        evolutionFlowButton.setText(START_ENVIRONMENT);
        evolutionFlowButton.setEnabled(true);
        drawCreatures.setEnabled(true);
    }

    public void environmentUnset() {
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

    public void disableOptionButton(int option) {
        switch (option) {
            case GraphicEnvironment.CREATURE_OPTION:
                drawCreatures.setSelected(false);
                drawCreatures.setEnabled(false);
                break;
        }
    }
}
