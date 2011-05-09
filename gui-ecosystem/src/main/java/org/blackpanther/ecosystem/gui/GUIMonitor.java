package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public enum GUIMonitor {
    Monitor;

    private static final Logger logger =
            Logger.getLogger(
                    GUIMonitor.class.getCanonicalName()
            );

    private GraphicEnvironment drawPanel;
    private EnvironmentSetting environmentSettingPanel;
    private EnvironmentCommands environmentCommandsPanel;


    public void registerEnvironmentCommandsPanel(EnvironmentCommands environmentCommands) {
        this.environmentCommandsPanel = environmentCommands;
    }

    public void registerDrawPanel(GraphicEnvironment panel) {
        this.drawPanel = panel;
    }

    public void registerEnvironmentSettingsPanel(EnvironmentSetting environmentSetting) {
        this.environmentSettingPanel = environmentSetting;
    }

    public void setCurrentEnvironment(Environment env) {
        this.drawPanel.setEnvironment(env);
        updateEnvironmentRunningId(env.getId());
        updateEnvironmentAgentNumber(env.getPool().size());
        updateEnvironmentCycleCount(env.getTime());
        environmentCommandsPanel.environmentSet();
        logger.info("Current environment set to " + env);
    }

    public void updateEnvironmentAgentNumber(Integer agentNumber) {
        require(environmentSettingPanel != null,
                "No environment settings panel to delegate task");
        this.environmentSettingPanel.setEnvironmentAgentNumber(agentNumber);
        logger.finer("Environment's agent number delegated to environment setting panel");
    }

    public void updateEnvironmentCycleCount(Integer count) {
        require(environmentSettingPanel != null,
                "No environment settings panel to delegate task");
        this.environmentSettingPanel.setEnvironmentCycleCount(count);
        logger.finer("Environment's cycle count delegated to environment setting panel");
    }

    public void updateEnvironmentRunningId(Long id) {
        require(environmentSettingPanel != null,
                "No environment settings panel to delegate task");
        this.environmentSettingPanel.setEnvironmentId(id);
        logger.finer("Environment's id delegated to environment setting panel");
    }

    public void interceptEnvironmentEvolutionFlow(String buttonLabel) {
        if (buttonLabel.equals(EnvironmentCommands.START_ENVIRONMENT)) {
            require(drawPanel != null,
                    "No draw panel to delegate task");
            this.drawPanel.runSimulation();
            logger.finer("Delegated environment's evolution's start to draw panel");
        } else if (buttonLabel.equals(EnvironmentCommands.STOP_ENVIRONMENT)) {
            require(drawPanel != null,
                    "No draw panel to delegate task");
            this.drawPanel.stopSimulation();
            logger.finer("Delegated environment's evolution's stop to draw panel");
        } else
            logger.info("No delegate environment's evolution's flow"
                    + "because no environment is set yet");
    }

    //TODO Handle state by a separated label
    public void environmentFrozen() {
        environmentSettingPanel.environmentEnded();
        logger.info("Environment's death's notified to environment setting panel");
    }

    //TODO Ask for a save
    public void stopEvolution() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void pauseEvolution() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void resumeEvolution() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public BufferedImage dumpCurrentImage() {
        logger.info("Dump image action delegated to draw panel");
        return drawPanel.dumpCurrentImage();
    }
}
