package org.blackpanther.ecosystem.gui;

import javax.swing.event.CaretEvent;
import java.awt.geom.Dimension2D;
import java.util.logging.Logger;

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

    private WorldFrame mainFrame;
    private GraphicEnvironment drawPanel;
    private GraphicEnvironmentSetting environmentSettingPanel;

    public void registerMainFrame(WorldFrame frame) {
        this.mainFrame = frame;
    }

    public void registerDrawPanel(GraphicEnvironment panel) {
        this.drawPanel = panel;
    }

    public void registerEnvironmentSettingsPanel(GraphicEnvironmentSetting environmentSetting) {
        this.environmentSettingPanel = environmentSetting;
    }

    public void updateEnvironmentAgentNumber(Integer agentNumber) {
        this.environmentSettingPanel.setEnvironmentAgentNumber(agentNumber);
        logger.info("Environment's agent number delegated to environment setting panel");
    }

    public void updateEnvironmentRunningId(Long id) {
        this.environmentSettingPanel.setEnvironmentId(id);
        logger.info("Environment's id delegated to environment setting panel");
    }
}
