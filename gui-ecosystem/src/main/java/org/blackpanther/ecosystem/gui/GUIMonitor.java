package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.gui.lightweight.ConfigurationInformation.dump;
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

    public void setCurrentEnvironment(Environment env) {
        require(drawPanel != null);
        require(environmentInformationPanel != null);
        drawPanel.setEnvironment(env);
        environmentInformationPanel.updateInformation(
                EnvironmentInformation.dump(env, EnvironmentInformation.State.NOT_YET_STARTED));
        environmentInformationPanel.updateInformation(dump(Configuration.Configuration));
        environmentCommandsPanel.environmentSet();
    }

    public void updateEnvironmentInformation(Environment env, EnvironmentInformation.State state) {
        require(environmentInformationPanel != null);
        require(drawPanel != null);
        require(environmentCommandsPanel != null);
        environmentInformationPanel.updateInformation(EnvironmentInformation.dump(env, state));
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

    public void interceptEnvironmentEvolutionFlow(String buttonLabel) {
        require(drawPanel != null);
        if (buttonLabel.equals(EnvironmentCommands.START_ENVIRONMENT)) {
            drawPanel.runSimulation();
            environmentInformationPanel.notifyRun();
        } else if (buttonLabel.equals(EnvironmentCommands.STOP_ENVIRONMENT)) {
            environmentInformationPanel.notifyPause();
            drawPanel.stopSimulation();
        }
    }

    public BufferedImage dumpCurrentImage() {
        logger.info("Dump image action delegated to draw panel");
        return drawPanel.dumpCurrentImage();
    }

    public void pauseEvolution() {
        require(drawPanel != null);
        require(environmentInformationPanel != null);
        require(environmentCommandsPanel != null);
        drawPanel.stopSimulation();
        environmentInformationPanel.notifyPause();
        environmentCommandsPanel.notifyPause();
    }
}
