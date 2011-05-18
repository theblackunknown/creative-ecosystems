package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.DesignEnvironment;
import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.Resource;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Properties;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:55 CEST 2011
 */
public enum GUIMonitor {
    Monitor;

    private GraphicEnvironment drawPanel;
    private EnvironmentInformationPanel environmentInformationPanel;
    private EnvironmentCommands environmentCommandsPanel;


    public void registerEnvironmentCommandsPanel(EnvironmentCommands environmentCommands) {
        this.environmentCommandsPanel = environmentCommands;
    }

    public void registerDrawPanel(GraphicEnvironment panel) {
        this.drawPanel = panel;
    }

    public void registerEnvironmentInformationPanel(EnvironmentInformationPanel environmentInformationPanel) {
        this.environmentInformationPanel = environmentInformationPanel;
    }

    public void recreateEnvironment(){
        require(environmentInformationPanel != null);
        environmentInformationPanel.recreateEnvironment();
    }

    public void removeEnvironment() {
        require(drawPanel != null);
        require(environmentInformationPanel != null);
        drawPanel.unsetEnvironment();
        environmentInformationPanel.clearBoard();
        environmentCommandsPanel.environmentUnset();
    }

    public void setCurrentEnvironment(Environment env) {
        require(drawPanel != null);
        require(environmentInformationPanel != null);
        drawPanel.setEnvironment(env);
        environmentInformationPanel.updateInformation(
                EnvironmentInformation.dump(env, EnvironmentInformation.State.NOT_YET_STARTED));
        environmentInformationPanel.updateInformation(Configuration);
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

    public void notifyConfigurationUpdate() {
        require(environmentInformationPanel != null);
        environmentInformationPanel.updateInformation(Configuration);
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

    public void paintBounds(boolean shouldBePainted) {
        require(drawPanel != null);
        drawPanel.setOption(GraphicEnvironment.BOUNDS_OPTION, shouldBePainted);
    }

    public void paintResources(boolean shouldBePainted) {
        require(drawPanel != null);
        drawPanel.setOption(GraphicEnvironment.RESOURCE_OPTION, shouldBePainted);
    }

    public BufferedImage dumpCurrentImage() {
        return drawPanel.dumpCurrentImage();
    }

    public Environment dumpCurrentEnvironment() {
        return drawPanel.dumpCurrentEnvironment();
    }

    public void pauseEvolution() {
        require(drawPanel != null);
        require(environmentInformationPanel != null);
        require(environmentCommandsPanel != null);
        drawPanel.stopSimulation();
        environmentInformationPanel.notifyPause();
        environmentCommandsPanel.notifyPause();
    }

    public void resetEnvironment(
            Collection<Agent> agentPool,
            Collection<Resource> resourcePool) {
        removeEnvironment();
        //pray for that instruction to make things more delightful
        System.gc();
        Environment env = new DesignEnvironment(
                Configuration.getParameter(SPACE_WIDTH, Double.class),
                Configuration.getParameter(SPACE_HEIGHT, Double.class)
        );
        env.addAgent(agentPool);
        env.addResource(resourcePool);
        setCurrentEnvironment(env);
    }

    public void setBackgroundColor(Color color) {
        require(drawPanel != null);
        drawPanel.applyBackgroundColor(color);
    }
}
