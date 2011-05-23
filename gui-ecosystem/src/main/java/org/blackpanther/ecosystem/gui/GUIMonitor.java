package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.gui.actions.EnvironmentSaveAction;
import org.blackpanther.ecosystem.gui.commands.EnvironmentCommands;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;
import org.blackpanther.ecosystem.gui.settings.EnvironmentBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public enum GUIMonitor {
    Monitor;

    private GraphicEnvironment drawPanel;
    private EnvironmentBoard environmentBoard;
    private EnvironmentCommands environmentCommandsPanel;

    /*=========================================================================
                             REGISTER
      =========================================================================*/

    public void registerEnvironmentCommandsPanel(EnvironmentCommands environmentCommands) {
        this.environmentCommandsPanel = environmentCommands;
    }

    public void registerDrawPanel(GraphicEnvironment panel) {
        this.drawPanel = panel;
    }

    public void registerEnvironmentInformationPanel(EnvironmentBoard environmentBoard) {
        this.environmentBoard = environmentBoard;
    }

    /*=========================================================================
                          MESSAGES
      =========================================================================*/

    /**
     * Update all panels that an environment has been removed
     */
    public void removeEnvironment() {
        require(drawPanel != null);
        require(environmentBoard != null);
        proposeSaveEnvironment(drawPanel.getBackup());
        drawPanel.unsetEnvironment();
        environmentBoard.clearBoard();
        environmentCommandsPanel.environmentUnset();
    }

    /**
     * Update all panels that an environment has been set
     */
    public void setCurrentEnvironment(Environment env) {
        require(drawPanel != null);
        require(environmentBoard != null);
        drawPanel.setEnvironment(env);
        environmentBoard.updateInformation(
                EnvironmentInformation.dump(env, EnvironmentInformation.State.NOT_YET_STARTED));
        environmentBoard.updateInformation(Configuration);
        environmentCommandsPanel.environmentSet();
    }

    /**
     * Notify information panel of current environment state
     */
    public void updateEnvironmentInformation(Environment env, EnvironmentInformation.State state) {
        require(environmentBoard != null);
        require(drawPanel != null);
        require(environmentCommandsPanel != null);
        environmentBoard.updateInformation(EnvironmentInformation.dump(env, state));
        switch (state) {
            case RUNNING:
                break;
            case PAUSED:
                drawPanel.stopSimulation();
                break;
            case FROZEN:
                environmentCommandsPanel.environmentFrozen();
                break;
        }
    }

    /**
     * Notify information panel that application configuration has changed
     */
    public void notifyConfigurationUpdate() {
        require(environmentBoard != null);
        environmentBoard.updateInformation(Configuration);
    }

    /**
     * Update evolution flow according to user interaction on evolution's button
     */
    public void interceptEnvironmentEvolutionFlow(String buttonLabel) {
        require(drawPanel != null);
        if (buttonLabel.equals(EnvironmentCommands.START_ENVIRONMENT)) {
            drawPanel.runSimulation();
            environmentBoard.notifyRun();
        } else if (buttonLabel.equals(EnvironmentCommands.STOP_ENVIRONMENT)) {
            environmentBoard.notifyPause();
            drawPanel.stopSimulation();
        }
    }

    /**
     * Update panel option
     */
    public void updateOption(int option, boolean activated) {
        require(drawPanel != null);
        drawPanel.setOption(option, activated);
    }

    public void paintBounds(boolean shouldBePainted) {
        updateOption(GraphicEnvironment.BOUNDS_OPTION, shouldBePainted);
    }

    public void paintResources(boolean shouldBePainted) {
        updateOption(GraphicEnvironment.RESOURCE_OPTION, shouldBePainted);
    }

    public void paintCreatures(boolean shouldBePainted) {
        updateOption(GraphicEnvironment.CREATURE_OPTION, shouldBePainted);
    }

    /**
     * delegator to fetch panel's image
     */
    public BufferedImage dumpCurrentImage() {
        return drawPanel.dumpCurrentImage();
    }

    /**
     * delegator to fetch panel's environment
     */
    public Environment dumpCurrentEnvironment() {
        return drawPanel.dumpCurrentEnvironment();
    }

    /**
     * delegator to fetch setting's configuration
     */
    public FieldsConfiguration fetchConfiguration() {
        require(environmentBoard != null);
        return environmentBoard.createConfiguration();
    }

    /**
     * Global pause action,
     * ordered by internal components
     */
    public void pauseEvolution() {
        require(drawPanel != null);
        require(environmentBoard != null);
        require(environmentCommandsPanel != null);
        drawPanel.stopSimulation();
        environmentBoard.notifyPause();
        environmentCommandsPanel.notifyPause();
    }

    /**
     * Set a new, clean environment
     */
    public void resetEnvironment() {
        require(environmentBoard != null);
        removeEnvironment();
        //pray for that instruction to make things more delightful
        System.gc();
        environmentBoard.updateConfiguration();
        Environment env = new Environment(
                Configuration.getParameter(SPACE_WIDTH, Double.class),
                Configuration.getParameter(SPACE_HEIGHT, Double.class)
        );
        setCurrentEnvironment(env);
    }

    /**
     * delegator to set panel's background color
     */
    public void setBackgroundColor(Color color) {
        require(drawPanel != null);
        drawPanel.applyBackgroundColor(color);
    }

    /**
     * delegator to set panel's agent drop mode
     */
    public void setDropMode(GraphicEnvironment.DropMode mode) {
        require(drawPanel != null);
        drawPanel.setDropMode(mode);
    }

    /**
     * delegator to disable commands' option button
     */
    public void disableOptionButton(int option) {
        require(environmentCommandsPanel != null);
        environmentCommandsPanel.disableOptionButton(option);
    }

    private void proposeSaveEnvironment(Environment backup) {
        if (backup != null)
            switch (JOptionPane.showConfirmDialog(
                    null,
                    "Would you like to save initial environment state ?",
                    "Save this environment's configuration",
                    JOptionPane.YES_NO_OPTION)) {
                case JOptionPane.OK_OPTION:
                    EnvironmentSaveAction.getInstance().save(backup);
                    break;
            }
    }

    public void addCreatures(Collection<Creature> creatures) {
        require(drawPanel != null);
        drawPanel.addCreatures(creatures);
    }

    public void addResources(Collection<Resource> creatures) {
        require(drawPanel != null);
        drawPanel.addResources(creatures);
    }
}
