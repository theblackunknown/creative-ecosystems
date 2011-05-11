package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.gui.actions.EnvironmentCreationAction;
import org.blackpanther.ecosystem.gui.lightweight.ConfigurationInformation;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static org.blackpanther.ecosystem.gui.GUIMonitor.Monitor;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class EnvironmentInformationPanel extends JTabbedPane {

    private static final Dimension DEFAULT_DIMENSION = new Dimension(230, 75);
    private static final String LABEL_ENVIRONMENT_ID = "Environment #%s";
    private static final String LABEL_ENVIRONMENT_AGENT = "Agent number : %d";
    private static final String LABEL_ENVIRONMENT_GENERATION = "#Generation %d";

    private final JPanel DEFAULT_TAB = new NoEnvironmentInformationPanel();
    private EnvironmentInformationInstance oldTab;

    public EnvironmentInformationPanel() {
        super();
//        setPreferredSize(DEFAULT_DIMENSION);
        addChangeListener(new EnvironmentSwitcher());
        add(DEFAULT_TAB);
        setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
        );
    }

    @Override
    public EnvironmentInformationInstance getSelectedComponent() {
        return super.getSelectedComponent() instanceof EnvironmentInformationInstance
                ? (EnvironmentInformationInstance) super.getSelectedComponent()
                : null; //Can't select default tab
    }

    void addEnvironment(Environment env, ConfigurationInformation initialParameters) {
        //TODO Handle stuff
        String tabName = String.format(LABEL_ENVIRONMENT_ID, env.getId());
        int tabIndex = indexOfTab(tabName);

        if (tabIndex < 0) {
            JPanel informationTab = new EnvironmentInformationInstance(env,initialParameters);
            add(informationTab);
            remove(DEFAULT_TAB);
            oldTab = getSelectedComponent();
            setSelectedComponent(informationTab);
        }
    }

    void removeEnvironment(Environment env) {
        //TODO Handle stuff
        if (getTabCount() == 0) {
            add(DEFAULT_TAB);
            oldTab = getSelectedComponent();
            setSelectedComponent(null);
        }
    }

    void environmentEnded() {
        if (getSelectedComponent() != null)
            this.getSelectedComponent().environmentEnded();
    }

    public void updateInformation() {
        if (getSelectedComponent() != null)
            this.getSelectedComponent().updateInformation();
    }

    class NoEnvironmentInformationPanel extends JPanel {
        NoEnvironmentInformationPanel() {
            setName("No environment is set");
            JButton createEnvironment =
                    new JButton(EnvironmentCreationAction.getInstance());
            add(createEnvironment);
        }
    }

    class EnvironmentInformationInstance extends JPanel {

        private static final String FROZEN_MARKER = "[FROZEN]";
        private JLabel environmentLabel;
        private JLabel environmentAgentCounter;
        private JLabel environmentGenerationLabel;

        private Environment environmentReference;

        public EnvironmentInformationInstance(Environment env, ConfigurationInformation initialParameters) {
            setName(String.format(LABEL_ENVIRONMENT_ID,
                    Long.toHexString(env.getId())));
            setLayout(new GridBagLayout());

            environmentReference = env;
            environmentLabel =
                    new JLabel(
                            String.format(LABEL_ENVIRONMENT_ID,
                                    Long.toHexString(environmentReference.getId())));

            environmentAgentCounter =
                    new JLabel(
                            String.format(LABEL_ENVIRONMENT_AGENT,
                                    environmentReference.getPool().size()));

            environmentGenerationLabel =
                    new JLabel(
                            String.format(LABEL_ENVIRONMENT_GENERATION,
                                    environmentReference.getTime()));
            JLabel initialParametersLabel =
                    new JLabel(initialParameters.toString());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridwidth = GridBagConstraints.REMAINDER;

            //Environment Label
            add(environmentLabel, constraints);
            add(environmentAgentCounter, constraints);
            add(environmentGenerationLabel, constraints);
            add(initialParametersLabel, constraints);
        }

        public void updateInformation() {
            this.environmentLabel.setText(
                    String.format(LABEL_ENVIRONMENT_ID,
                            Long.toHexString(environmentReference.getId())));
            this.environmentAgentCounter.setText(
                    String.format(LABEL_ENVIRONMENT_AGENT,
                            environmentReference.getPool().size()));
            this.environmentGenerationLabel.setText(
                    String.format(LABEL_ENVIRONMENT_GENERATION,
                            environmentReference.getTime()));
        }

        void environmentEnded() {
            if (!environmentLabel.getText().contains(FROZEN_MARKER)) {
                this.environmentLabel.setText(
                        environmentLabel.getText() + FROZEN_MARKER
                );
            }
        }

        Environment getEnvironment() {
            return environmentReference;
        }
    }

    class EnvironmentSwitcher implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (getSelectedComponent() != null) {
                Monitor.switchEnvironment(
                        oldTab == null
                                ? null
                                : oldTab.getEnvironment(),
                        getSelectedComponent().getEnvironment()
                );
                oldTab = null;
            }
        }
    }
}
