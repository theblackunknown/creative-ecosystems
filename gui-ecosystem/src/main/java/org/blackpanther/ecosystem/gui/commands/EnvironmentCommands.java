package org.blackpanther.ecosystem.gui.commands;

import org.blackpanther.ecosystem.gui.GraphicEnvironment;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
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

    public EnvironmentCommands() {
        super();
        setLayout(new FlowLayout());


        JButton resetEnvironment = new JButton("Generate new environment");
        evolutionFlowButton = new JButton(NO_ENVIRONMENT);

        noAction = new JToggleButton("No action");
        paintAgent = new JToggleButton("Agent dropper");
        paintResource = new JToggleButton("Resource dropper");

        JSlider lineWidthLinear = new JSlider(1, 10, 1);
        JSlider lineWidthExponential = new JSlider(0, 10, 0);
        JSlider colorBlender = new JSlider(0, 1000000, 10000);

        ActionListener toggleListener = EventHandler.create(
                ActionListener.class,
                this,
                "updateDropMode",
                "source"
        );

        lineWidthLinear.addChangeListener(EventHandler.create(
                ChangeListener.class,
                this,
                "updateLineWidthLinear",
                ""
        ));

        lineWidthExponential.addChangeListener(EventHandler.create(
                ChangeListener.class,
                this,
                "updateLineWidthExponential",
                ""
        ));
        colorBlender.addChangeListener(EventHandler.create(
                ChangeListener.class,
                this,
                "updateColorRatio",
                ""
        ));

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

        add(noAction);
        add(paintAgent);
        add(paintResource);
        add(Box.createVerticalStrut(40));
        add(resetEnvironment);
        add(evolutionFlowButton);
        add(Box.createVerticalStrut(40));
        add(lineWidthLinear);
        add(lineWidthExponential);
        add(colorBlender);

        setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
        );
    }

    public void updateLineWidthLinear(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            Monitor.updateLineWidthLinear(source.getValue());
        }
    }

    public void updateLineWidthExponential(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            Monitor.updateLineWidthExponential(source.getValue() / 10.0);
        }
    }

    public void updateColorRatio(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            Monitor.changeColorRatio(source.getValue() / 100.0);
        }
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
}
