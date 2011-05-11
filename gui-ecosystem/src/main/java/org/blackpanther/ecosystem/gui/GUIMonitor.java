package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.actions.SaveImageAction;
import org.blackpanther.ecosystem.gui.lightweight.ConfigurationInformation;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public enum GUIMonitor {
    Monitor;

    private static final Logger logger =
            Logger.getLogger(
                    GUIMonitor.class.getCanonicalName()
            );

    private GraphicEnvironment drawPanel;
    private EnvironmentInformationPanel environmentInformationPanel;
    private EnvironmentCommands environmentCommandsPanel;


    public void registerEnvironmentCommandsPanel(EnvironmentCommands environmentCommands) {
        this.environmentCommandsPanel = environmentCommands;
    }

    public void registerDrawPanel(GraphicEnvironment panel) {
        this.drawPanel = panel;
    }

    public void registerEnvironmentSettingsPanel(EnvironmentInformationPanel environmentInformationPanel) {
        this.environmentInformationPanel = environmentInformationPanel;
    }

    public void setCurrentEnvironment(Environment env, ConfigurationInformation info) {
        require(drawPanel != null,
                "No draw panel to delegate environment change notification");
        require(environmentInformationPanel != null,
                "No information panel to delegate environment change notification");
        pauseEvolution();
        this.drawPanel.setEnvironment(env);
        this.environmentInformationPanel.addEnvironment(env, info);
        environmentCommandsPanel.environmentSet();
        logger.info("Current environment set to " + env);
    }

    public void updateEnvironmentInformation() {
        require(environmentInformationPanel != null,
                "No environment settings panel to delegate task");
        environmentInformationPanel.updateInformation();
        logger.finer("Environment's information delegated to environment setting panel");
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
        environmentInformationPanel.environmentEnded();
        logger.info("Environment's death's notified to environment setting panel");
    }

    public void pauseEvolution() {
        require(drawPanel != null,
                "No draw panel to delegate environment change notification");
        drawPanel.stopSimulation();
    }

    public void resumeEvolution() {
        require(drawPanel != null,
                "No draw panel to delegate environment change notification");
        drawPanel.runSimulation();
    }

    public BufferedImage dumpCurrentImage() {
        logger.info("Dump image action delegated to draw panel");
        return drawPanel.dumpCurrentImage();
    }

    public void switchEnvironment(Environment envA, Environment envB) {
        require(drawPanel != null);
        require(environmentCommandsPanel != null);

        pauseEvolution();
        drawPanel.setEnvironment(envB);
        environmentCommandsPanel.environmentSet();
    }

    public void newEnvironment(Environment env) {
        require(environmentInformationPanel != null);

        environmentInformationPanel.addEnvironment(
                env,
                ConfigurationInformation.dump(Configuration.Configuration)
        );
    }
}
